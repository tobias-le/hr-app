package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.dto.TeamDTO;
import cz.cvut.fel.pm2.timely_be.mapper.MapperUtils;
import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.model.Team;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeRepository;
import cz.cvut.fel.pm2.timely_be.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static cz.cvut.fel.pm2.timely_be.enums.EmploymentType.FULL_TIME;
import static cz.cvut.fel.pm2.timely_be.enums.EmploymentType.PART_TIME;
import static cz.cvut.fel.pm2.timely_be.utils.TestUtils.createEmployee;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private TeamService teamService;

    @Test
    public void testGetAllTeams() {
        // Given
        var employee1 = createEmployee(FULL_TIME);
        var employee2 = createEmployee(PART_TIME);
        var team1 = employee1.getTeam();
        var team2 = employee2.getTeam();
        var teams = Arrays.asList(team1, team2);

        when(teamRepository.findAll()).thenReturn(teams);

        // When
        var result = teamService.getAllTeams();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());

        var expectedDtos = Arrays.asList(MapperUtils.toTeamDto(team1), MapperUtils.toTeamDto(team2));
        assertEquals(expectedDtos, result);
    }

    @Test
    public void testCreateTeam() {
        // Given
        var manager = createEmployee(FULL_TIME);
        var member = createEmployee(PART_TIME);
        var teamDTO = new TeamDTO();
        teamDTO.setName("New Team");
        teamDTO.setManagerId(manager.getEmployeeId());
        teamDTO.setMembers(Set.of(MapperUtils.toEmployeeDto(member)));

        when(employeeRepository.findById(manager.getEmployeeId())).thenReturn(Optional.of(manager));
        when(employeeRepository.findById(member.getEmployeeId())).thenReturn(Optional.of(member));
        when(teamRepository.save(any(Team.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        var result = teamService.createTeam(teamDTO);

        // Then
        assertNotNull(result);
        assertEquals(teamDTO.getName(), result.getName());
        assertEquals(manager, result.getManager());
        assertEquals(1, result.getMembers().size());
        assertEquals(member, result.getMembers().stream().findFirst().orElseThrow());
    }

    @Test
    public void testGetTeamDetails() {
        // Given
        var employee = createEmployee(FULL_TIME);
        var team = employee.getTeam();
        var teamId = team.getId();

        when(teamRepository.findTeamWithMembersAndParent(teamId)).thenReturn(Optional.of(team));

        // When
        var result = teamService.getTeamDetails(teamId);

        // Then
        assertNotNull(result);
        assertEquals(team.getName(), result.getName());
        assertEquals(team.getManager().getEmployeeId(), result.getManagerId());
        assertEquals(1, result.getMembers().size());
    }

    @Test
    public void testGetTeamDetails_NotFound() {
        // Given
        var teamId = 999L;
        when(teamRepository.findTeamWithMembersAndParent(teamId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> teamService.getTeamDetails(teamId));
    }

    @Test
    public void testUpdateTeam() {
        // Given
        var existingTeam = createEmployee(FULL_TIME).getTeam();
        var newManager = createEmployee(PART_TIME);
        var newMember = createEmployee(FULL_TIME);
        
        var teamDTO = new TeamDTO();
        teamDTO.setName("Updated Team");
        teamDTO.setManagerId(newManager.getEmployeeId());
        teamDTO.setMembers(Set.of(MapperUtils.toEmployeeDto(newMember)));

        when(teamRepository.findById(existingTeam.getId())).thenReturn(Optional.of(existingTeam));
        when(employeeRepository.findById(newManager.getEmployeeId())).thenReturn(Optional.of(newManager));
        when(employeeRepository.findById(newMember.getEmployeeId())).thenReturn(Optional.of(newMember));
        when(teamRepository.save(any(Team.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        var result = teamService.updateTeam(existingTeam.getId(), teamDTO);

        // Then
        assertNotNull(result);
        assertEquals(teamDTO.getName(), result.getName());
        assertEquals(newManager, result.getManager());
        assertEquals(1, result.getMembers().size());
        assertEquals(newMember, result.getMembers().stream().findFirst().orElseThrow());
    }

    @Test
    public void testDeleteTeam() {
        // Given
        var employee = createEmployee(FULL_TIME);
        var team = employee.getTeam();
        team.setMembers(Set.of(employee));

        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(teamRepository.save(any(Team.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        assertDoesNotThrow(() -> teamService.deleteTeam(team.getId()));

        // Then
        assertTrue(team.isDeleted());
        assertNull(employee.getTeam());
    }

    @Test
    public void testDeleteTeam_NotFound() {
        // Given
        var teamId = 999L;
        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> teamService.deleteTeam(teamId));
    }
}
