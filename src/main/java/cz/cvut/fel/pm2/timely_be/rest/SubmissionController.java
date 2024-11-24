package cz.cvut.fel.pm2.timely_be.rest;

import cz.cvut.fel.pm2.timely_be.dto.SubmissionDto;
import cz.cvut.fel.pm2.timely_be.enums.LeaveStatus;
import cz.cvut.fel.pm2.timely_be.mapper.MapperUtils;
import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.model.Submission;
import cz.cvut.fel.pm2.timely_be.service.SubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/request")
@Tag(name = "Request", description = "The Request API")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping
    @Operation(summary = "Submit a new request")
    public ResponseEntity<SubmissionDto> submitRequest(@RequestBody Map<String, Object> requestBody) {
        Long employeeId = Long.valueOf(requestBody.get("employeeId").toString());
        String message = requestBody.get("message").toString();
        Submission submission = submissionService.createSubmission(employeeId, message);
        SubmissionDto submissionDto = submissionService.getSubmissionDto(submission.getMessageId());
        return ResponseEntity.status(HttpStatus.CREATED).body(submissionDto);
    }


    @GetMapping("/pending")
    @Operation(summary = "Get all pending requests")
    public ResponseEntity<List<SubmissionDto>> getPendingRequests() {
        List<SubmissionDto> submissionDtos = submissionService.getPendingSubmissions();
        return ResponseEntity.ok(submissionDtos);
    }

    @GetMapping("/all/{employeeId}")
    @Operation(summary = "Get all requests for an employee")
    public ResponseEntity<List<Submission>> getRequestsByEmployeeId(@PathVariable Long employeeId) {
        List<Submission> submissions = submissionService.getSubmissionsByEmployeeId(employeeId);
        return ResponseEntity.ok(submissions);
    }

    @PatchMapping("/{messageId}/{status}")
    @Operation(summary = "Update the status of a request")
    public ResponseEntity<Submission> updateRequestStatus(@PathVariable Long messageId, @PathVariable String status) {
        try {
            LeaveStatus leaveStatus = LeaveStatus.valueOf(status.toUpperCase());
            Submission updatedSubmission = submissionService.updateSubmissionStatus(messageId, leaveStatus);
            return ResponseEntity.ok(updatedSubmission);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}