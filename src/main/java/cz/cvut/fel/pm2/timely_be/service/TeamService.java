package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.dto.EmployeeDto;
import cz.cvut.fel.pm2.timely_be.dto.TeamDTO;
import cz.cvut.fel.pm2.timely_be.dto.TeamNameWithIdDto;
import cz.cvut.fel.pm2.timely_be.mapper.MapperUtils;
import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.model.Team;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeRepository;
import cz.cvut.fel.pm2.timely_be.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TeamService {
    private final TeamRepository teamRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository, EmployeeRepository employeeRepository) {
        this.teamRepository = teamRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public Team createTeam(TeamDTO teamDTO) {
        // Check if active team with same name exists
        if (teamRepository.findByNameAndDeletedFalse(teamDTO.getName()).isPresent()) {
            throw new IllegalArgumentException("Team with name '" + teamDTO.getName() + "' already exists");
        }

        // Check if deleted team with same name exists and reuse it
        Optional<Team> deletedTeam = teamRepository.findByNameAndDeletedTrue(teamDTO.getName());
        Team team = deletedTeam.orElse(new Team());
        
        toTeam(teamDTO, team);
        team.setDeleted(false);
        return teamRepository.save(team);
    }

    public List<TeamDTO> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(MapperUtils::toTeamDto)
                .collect(Collectors.toList());
    }

    public TeamDTO getTeamDetails(Long teamId) {
        // Get team with members and immediate parent
        Team teamWithMembers = teamRepository.findTeamWithMembersAndParent(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));
        
        // Get all teams in hierarchy
        List<Team> hierarchyTeams = teamRepository.findTeamWithCompleteHierarchy(teamId);
        
        // Build the hierarchy map
        Map<Long, Team> teamsById = new HashMap<>();
        for (Team team : hierarchyTeams) {
            teamsById.put(team.getId(), team);
        }
        
        // Set up parent relationships
        for (Team team : hierarchyTeams) {
            if (team.getParentTeam() != null) {
                Long parentId = team.getParentTeam().getId();
                Team parent = teamsById.get(parentId);
                if (parent != null) {
                    team.setParentTeam(parent);
                }
            }
        }
        
        // Find the requested team in the hierarchy
        Team teamWithHierarchy = teamsById.get(teamId);
        if (teamWithHierarchy != null) {
            teamWithMembers.setParentTeam(teamWithHierarchy.getParentTeam());
        }
        
        return MapperUtils.toTeamDtoWithHierarchy(teamWithMembers);
    }

    @Transactional
    public Team updateTeam(Long teamId, TeamDTO teamDTO) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));
        
        // Check if another active team with same name exists
        Optional<Team> existingTeam = teamRepository.findByNameAndDeletedFalse(teamDTO.getName());
        if (existingTeam.isPresent() && !existingTeam.get().getId().equals(teamId)) {
            throw new IllegalArgumentException("Team with name '" + teamDTO.getName() + "' already exists");
        }
        
        // Remove team association from previous members who are not in the new member list
        if (team.getMembers() != null) {
            Set<Long> newMemberIds = teamDTO.getMembers().stream()
                    .map(EmployeeDto::getId)
                    .collect(Collectors.toSet());
            
            team.getMembers().stream()
                    .filter(member -> !newMemberIds.contains(member.getEmployeeId()))
                    .forEach(member -> {
                        member.setTeam(null);
                        employeeRepository.save(member);
                    });
        }

        toTeam(teamDTO, team);
        return teamRepository.save(team);
    }

    @Transactional
    public void deleteTeam(Long teamId) {
        log.info("Attempting to delete team with ID: {}", teamId);
        
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> {
                    log.error("Team with ID {} not found", teamId);
                    return new IllegalArgumentException("Team not found");
                });
        
        log.debug("Found team: {} (ID: {})", team.getName(), teamId);
        
        // Update all members to remove their team association
        if (team.getMembers() != null) {
            log.info("Removing team from {} members", team.getMembers().size());
            team.getMembers().forEach(member -> {
                log.debug("Removing team from member: {} (ID: {})", member.getName(), member.getEmployeeId());
                member.setTeam(null);
                employeeRepository.save(member);
            });
        }
        
        // Clear the manager reference
        Employee manager = team.getManager();
        if (manager != null) {
            team.setManager(null);
            employeeRepository.save(manager);
        }
        
        // Clear the members set
        team.setMembers(null);
        
        team.setDeleted(true);
        teamRepository.save(team);
        log.info("Successfully soft-deleted team with ID: {}", teamId);
    }

    private void toTeam(TeamDTO teamDTO, Team team) {
        team.setName(teamDTO.getName());
        
        // Set manager
        Employee manager = employeeRepository.findById(teamDTO.getManagerId())
                .orElseThrow(() -> new IllegalArgumentException("Manager not found"));
        team.setManager(manager);
        
        // Set members
        Set<Employee> newMembers = teamDTO.getMembers()
                .stream()
                .map(employeeDto -> employeeRepository.findById(employeeDto.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Employee with ID " + employeeDto.getId() + " not found")))
                .collect(Collectors.toSet());
        team.setMembers(newMembers);

        // Set parent team if provided
        if (teamDTO.getParentTeam() != null) {
            Team parentTeam = teamRepository.findById(teamDTO.getParentTeam().getTeamId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent team not found"));
            team.setParentTeam(parentTeam);
        } else {
            team.setParentTeam(null);  // Clear parent team if none provided
        }
    }

    public List<TeamNameWithIdDto> autocompleteTeams(String namePattern) {
        if (namePattern == null || namePattern.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return teamRepository.findTeamsByNameContaining(namePattern.trim(), PageRequest.of(0, 5));
    }

    public boolean validateTeamMembership(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new IllegalArgumentException("Employee not found");
        }
        return teamRepository.isEmployeeInAnyTeam(employeeId);
    }

    public TeamDTO getTeamByEmployeeId(Long employeeId) {
        var employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        if (employee.getTeam() == null) {
            return null;
        }
        
        Team team = teamRepository.findTeamByEmployeeId(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("No team found for employee"));
                
        return MapperUtils.toTeamDto(team);
    }

    public TeamDTO getTeamByManagerId(Long managerId) {
        if (!employeeRepository.existsById(managerId)) {
            throw new IllegalArgumentException("Employee not found");
        }
        
        Team team = teamRepository.findTeamByManagerId(managerId)
                .orElse(null);
                
        return team != null ? MapperUtils.toTeamDto(team) : null;
    }
}
