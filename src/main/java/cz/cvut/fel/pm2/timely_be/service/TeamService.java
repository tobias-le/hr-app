package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.dto.TeamDTO;
import cz.cvut.fel.pm2.timely_be.mapper.MapperUtils;
import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.model.Team;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeRepository;
import cz.cvut.fel.pm2.timely_be.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        Team team = new Team();
        toTeam(teamDTO, team);
        return teamRepository.save(team);
    }

    public List<TeamDTO> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(MapperUtils::toTeamDto)
                .collect(Collectors.toList());
    }

    public TeamDTO getTeamDetails(Long teamId) {
        Team team = teamRepository.findTeamWithMembers(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));
        return MapperUtils.toTeamDto(team);
    }

    @Transactional
    public Team updateTeam(Long teamId, TeamDTO teamDTO) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));
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
        List<Employee> newMembers = teamDTO.getMembers()
                .stream()
                .map(employeeDto -> employeeRepository.findById(employeeDto.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Employee with ID " + employeeDto.getId() + " not found")))
                .collect(Collectors.toList());
        team.setMembers(newMembers);
    }
}
