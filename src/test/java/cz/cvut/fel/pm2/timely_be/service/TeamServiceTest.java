package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.dto.TeamDTO;
import cz.cvut.fel.pm2.timely_be.dto.TeamNameWithIdDto;
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
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
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

    @Test
    public void testGetTeamDetails_WithParentTeam() {
        // Given
        var parentTeam = new Team();
        parentTeam.setId(1L);
        parentTeam.setName("Parent Team");

        var childTeam = new Team();
        childTeam.setId(2L);
        childTeam.setName("Child Team");
        childTeam.setParentTeam(parentTeam);

        when(teamRepository.findTeamWithMembersAndParent(2L)).thenReturn(Optional.of(childTeam));
        when(teamRepository.findTeamWithCompleteHierarchy(2L)).thenReturn(List.of(parentTeam, childTeam));

        // When
        var result = teamService.getTeamDetails(2L);

        // Then
        assertNotNull(result);
        assertEquals("Child Team", result.getName());
        assertEquals("Parent Team", result.getParentTeam().getName());
    }

    @Test
    public void testDeleteTeam_NoMembers() {
        // Given
        var team = new Team();
        team.setId(1L);
        team.setMembers(Set.of());

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(teamRepository.save(any(Team.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        assertDoesNotThrow(() -> teamService.deleteTeam(1L));

        // Then
        assertTrue(team.isDeleted());
    }

    @Test
    public void testCreateTeam_ManagerNotFound() {
        // Given
        var teamDTO = new TeamDTO();
        teamDTO.setManagerId(999L);

        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> teamService.createTeam(teamDTO));
    }

    @Test
    public void testUpdateTeam_InvalidTeamId() {
        // Given
        var teamDTO = new TeamDTO();
        var invalidTeamId = 999L;

        when(teamRepository.findById(invalidTeamId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> teamService.updateTeam(invalidTeamId, teamDTO));
    }

    @Test
    public void testAutocompleteTeams_ValidPattern() {
        // Given
        var team1 = new Team();
        team1.setId(1L);
        team1.setName("Development");

        var team2 = new Team();
        team2.setId(2L);
        team2.setName("DevOps");

        when(teamRepository.findTeamsByNameContaining("Dev", PageRequest.of(0, 5))).thenReturn(List.of(
                new TeamNameWithIdDto(team1.getId(), team1.getName()), new TeamNameWithIdDto(team2.getId(), team2.getName())));

        // When
        var result = teamService.autocompleteTeams("Dev");

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Development", result.get(0).getName());
        assertEquals("DevOps", result.get(1).getName());
    }

    @Test
    public void testAutocompleteTeams_EmptyPattern() {
        // When
        var result = teamService.autocompleteTeams("");

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testValidateTeamMembership_EmployeeInTeam() {
        // Given
        Long employeeId = 1L;
        when(employeeRepository.existsById(employeeId)).thenReturn(true);
        when(teamRepository.isEmployeeInAnyTeam(employeeId)).thenReturn(true);

        // When
        boolean result = teamService.validateTeamMembership(employeeId);

        // Then
        assertTrue(result);
        verify(teamRepository).isEmployeeInAnyTeam(employeeId);
    }

    @Test
    public void testValidateTeamMembership_EmployeeNotInTeam() {
        // Given
        Long employeeId = 1L;
        when(employeeRepository.existsById(employeeId)).thenReturn(true);
        when(teamRepository.isEmployeeInAnyTeam(employeeId)).thenReturn(false);

        // When
        boolean result = teamService.validateTeamMembership(employeeId);

        // Then
        assertFalse(result);
        verify(teamRepository).isEmployeeInAnyTeam(employeeId);
    }

    @Test
    public void testValidateTeamMembership_EmployeeNotFound() {
        // Given
        Long employeeId = 999L;
        when(employeeRepository.existsById(employeeId)).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> teamService.validateTeamMembership(employeeId));
        verify(teamRepository, never()).isEmployeeInAnyTeam(anyLong());
    }

    @Test
    public void testGetTeamByEmployeeId() {
        // Given
        var employee = createEmployee(FULL_TIME);
        var team = employee.getTeam();
        
        when(employeeRepository.existsById(employee.getEmployeeId())).thenReturn(true);
        when(teamRepository.findTeamByEmployeeId(employee.getEmployeeId())).thenReturn(Optional.of(team));

        // When
        var result = teamService.getTeamByEmployeeId(employee.getEmployeeId());

        // Then
        assertNotNull(result);
        assertEquals(team.getName(), result.getName());
        assertEquals(team.getManager().getEmployeeId(), result.getManagerId());
    }

    @Test
    public void testGetTeamByEmployeeId_EmployeeNotFound() {
        // Given
        Long employeeId = 999L;
        when(employeeRepository.existsById(employeeId)).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class, 
                    () -> teamService.getTeamByEmployeeId(employeeId));
        verify(teamRepository, never()).findTeamByEmployeeId(anyLong());
    }

    @Test
    public void testGetTeamByEmployeeId_TeamNotFound() {
        // Given
        Long employeeId = 1L;
        when(employeeRepository.existsById(employeeId)).thenReturn(true);
        when(teamRepository.findTeamByEmployeeId(employeeId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, 
                    () -> teamService.getTeamByEmployeeId(employeeId));
    }
}
