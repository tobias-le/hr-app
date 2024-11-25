package cz.cvut.fel.pm2.timely_be.rest;

import cz.cvut.fel.pm2.timely_be.dto.TeamDTO;
import cz.cvut.fel.pm2.timely_be.dto.TeamNameWithIdDto;
import cz.cvut.fel.pm2.timely_be.mapper.MapperUtils;
import cz.cvut.fel.pm2.timely_be.model.Team;
import cz.cvut.fel.pm2.timely_be.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/teams")
@Tag(name = "Team", description = "The Team API")
public class TeamController {
    @Autowired
    private TeamService teamService;

    @PostMapping
    @Operation(summary = "Create a new team", description = "Creates a new team with the given data")
    public ResponseEntity<TeamDTO> createTeam(@RequestBody TeamDTO teamDTO) {
        Team team = teamService.createTeam(teamDTO);
        return ResponseEntity.created(
                fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(team.getId())
                        .toUri()
        ).body(MapperUtils.toTeamDto(team));
    }

    @GetMapping
    @Operation(summary = "Get all teams", description = "Get a list of all teams")
    public ResponseEntity<List<TeamDTO>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @GetMapping("/{teamId}")
    @Operation(summary = "Get team details", description = "Returns team details including members")
    public ResponseEntity<TeamDTO> getTeam(@PathVariable Long teamId) {
        return ResponseEntity.ok(teamService.getTeamDetails(teamId));
    }

    @PutMapping("/{teamId}")
    @Operation(summary = "Update a team", description = "Updates an existing team with new data")
    public ResponseEntity<TeamDTO> updateTeam(@PathVariable Long teamId, @RequestBody TeamDTO teamDTO) {
        Team updatedTeam = teamService.updateTeam(teamId, teamDTO);
        return ResponseEntity.ok(MapperUtils.toTeamDto(updatedTeam));
    }

    @DeleteMapping("/{teamId}")
    @Operation(summary = "Delete a team", description = "Deletes a team by its ID")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long teamId) {
        teamService.deleteTeam(teamId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/autocomplete")
    @Operation(summary = "Autocomplete team names", description = "Get team names and ids matching the search pattern")
    public ResponseEntity<List<TeamNameWithIdDto>> autocompleteTeams(
            @RequestParam(required = false, defaultValue = "") String query) {
        return ResponseEntity.ok(teamService.autocompleteTeams(query));
    }

    @GetMapping("/validate-membership/{employeeId}")
    @Operation(summary = "Validate team membership", description = "Checks if an employee is already a member of any team")
    public ResponseEntity<Boolean> validateTeamMembership(@PathVariable Long employeeId) {
        return ResponseEntity.ok(teamService.validateTeamMembership(employeeId));
    }

    @GetMapping("/by-employee/{employeeId}")
    @Operation(summary = "Get team by employee ID", description = "Returns the team details for a specific employee")
    public ResponseEntity<TeamDTO> getTeamByEmployeeId(@PathVariable Long employeeId) {
        return ResponseEntity.ok(teamService.getTeamByEmployeeId(employeeId));
    }
}
