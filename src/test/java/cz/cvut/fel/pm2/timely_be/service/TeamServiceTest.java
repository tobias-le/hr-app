package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.mapper.MapperUtils;
import cz.cvut.fel.pm2.timely_be.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static cz.cvut.fel.pm2.timely_be.enums.EmploymentType.FULL_TIME;
import static cz.cvut.fel.pm2.timely_be.enums.EmploymentType.PART_TIME;
import static cz.cvut.fel.pm2.timely_be.utils.TestUtils.createEmployee;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

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
}
