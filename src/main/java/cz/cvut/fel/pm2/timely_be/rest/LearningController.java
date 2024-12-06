package cz.cvut.fel.pm2.timely_be.rest;

import cz.cvut.fel.pm2.timely_be.dto.LearningAssignmentDto;
import cz.cvut.fel.pm2.timely_be.dto.LearningDto;
import cz.cvut.fel.pm2.timely_be.model.Learning;
import cz.cvut.fel.pm2.timely_be.service.LearningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static cz.cvut.fel.pm2.timely_be.mapper.MapperUtils.toLearningDto;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/learning")
@Tag(name = "Learning", description = "The Learning API")
public class LearningController {

    private final LearningService learningService;

    @Autowired
    public LearningController(LearningService learningService) {
        this.learningService = learningService;
    }

    @PostMapping
    @Operation(summary = "Register a new learning", description = "Creates a new learning with the given details")
    public ResponseEntity<LearningDto> registerLearning(@RequestBody LearningDto learning) {
        Learning newLearning = learningService.registerLearning(learning.getName(), learning.getLink());
        return ResponseEntity.created(
                fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(newLearning.getLearningId())
                        .toUri()
        ).body(toLearningDto(newLearning));
    }

    @GetMapping
    @Operation(summary = "Get all learnings", description = "Returns a list of all learnings")
    public ResponseEntity<Iterable<Learning>> getAllLearnings() {
        return ResponseEntity.ok(learningService.getAllLearnings());
    }

    @GetMapping("/{employeeId}")
    @Operation(summary = "Get learnings by employee ID", description = "Returns a list of learnings for the given employee")
    public ResponseEntity<Iterable<Learning>> getLearningsByEmployeeId(@PathVariable Long employeeId) {
        return ResponseEntity.ok(learningService.getLearningsByEmployeeId(employeeId));
    }

    @PostMapping("/assign")
    @Operation(summary = "Register learning to employee", description = "Register learning to employee after they finished it")
    public ResponseEntity<Learning> registerLearningToEmployee(@RequestBody LearningAssignmentDto learningAssignment) {
        return ResponseEntity.created(
                fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(learningService.registerLearningToEmployee(learningAssignment)
                                .getLearning().getLearningId())
                        .toUri()
        ).build();
    }
}
