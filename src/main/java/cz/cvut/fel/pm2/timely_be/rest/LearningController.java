package cz.cvut.fel.pm2.timely_be.rest;

import cz.cvut.fel.pm2.timely_be.model.Learning;
import cz.cvut.fel.pm2.timely_be.service.LearningService;
import cz.cvut.fel.pm2.timely_be.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @Operation(summary = "Register a new learning", description = "Creates a new learning with the given link")
    public ResponseEntity<Learning> registerLearning(String name, String link) {
        Learning newLearning = learningService.registerLearning(name, link);
        return ResponseEntity.created(
                fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(newLearning.getLearningId())
                        .toUri()
        ).body(newLearning);
    }

    @GetMapping
    @Operation(summary = "Get all learnings", description = "Returns a list of all learnings")
    public ResponseEntity<Iterable<Learning>> getAllLearnings() {
        return ResponseEntity.ok(learningService.getAllLearnings());
    }

    @GetMapping("/{employeeId}")
    @Operation(summary = "Get learnings by employee ID", description = "Returns a list of learnings for the given employee")
    public ResponseEntity<Iterable<Learning>> getLearningsByEmployeeId(Long employeeId) {
        return ResponseEntity.ok(learningService.getLearningsByEmployeeId(employeeId));
    }

    @PostMapping("/{employeeId}/{learningId}")
    @Operation(summary = "Register learning to employee", description = "Register learning to employee after they finished it")
    public ResponseEntity<Iterable<Learning>> registerLearningToEmployee(Long employeeId, Long learningId) {
        return ResponseEntity.ok(learningService.registerLearningToEmployee(employeeId, learningId));
    }

}
