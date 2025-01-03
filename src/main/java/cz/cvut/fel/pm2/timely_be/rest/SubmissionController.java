package cz.cvut.fel.pm2.timely_be.rest;

import cz.cvut.fel.pm2.timely_be.dto.NewSubmissionDto;
import cz.cvut.fel.pm2.timely_be.dto.SubmissionDto; // Import SubmissionDto
import cz.cvut.fel.pm2.timely_be.enums.RequestStatus;
import cz.cvut.fel.pm2.timely_be.model.Submission;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeRepository;
import cz.cvut.fel.pm2.timely_be.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/request")
public class SubmissionController {

    private final SubmissionService submissionService;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public SubmissionController(SubmissionService submissionService, EmployeeRepository employeeRepository) {
        this.submissionService = submissionService;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping
    public ResponseEntity<Submission> createSubmission(@RequestBody NewSubmissionDto dto) {
        if (employeeRepository.findById(dto.getEmployeeId()).isPresent()) {
            Submission createdSubmission = submissionService.createSubmission(dto.getEmployeeId(), dto.getMessage());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSubmission);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/pending")
    public ResponseEntity<List<SubmissionDto>> getPendingSubmissions() {
        List<SubmissionDto> pendingSubmissions = submissionService.getPendingSubmissions();
        return ResponseEntity.ok(pendingSubmissions);
    }

    @GetMapping("/all/{employeeId}")
    public ResponseEntity<List<Submission>> getSubmissionsByEmployeeId(@PathVariable Long employeeId) {
        List<Submission> submissions = submissionService.getSubmissionsByEmployeeId(employeeId);
        return ResponseEntity.ok(submissions);
    }

    @PatchMapping("/{submissionId}/{action}")
    public ResponseEntity<Submission> updateSubmissionStatus(@PathVariable Long submissionId, @PathVariable String action) {
        RequestStatus status;
        if ("approve".equalsIgnoreCase(action)) {
            status = RequestStatus.APPROVED;
        } else if ("reject".equalsIgnoreCase(action)) {
            status = RequestStatus.REJECTED;
        } else {
            return ResponseEntity.badRequest().build();
        }

        Submission updatedSubmission = submissionService.updateSubmissionStatus(submissionId, status);
        return ResponseEntity.ok(updatedSubmission);
    }
}