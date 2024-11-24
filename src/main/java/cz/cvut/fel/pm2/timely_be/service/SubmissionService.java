package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.enums.LeaveStatus;
import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.model.Submission;
import cz.cvut.fel.pm2.timely_be.repository.SubmissionRepository;
import org.springframework.stereotype.Service;
import cz.cvut.fel.pm2.timely_be.dto.SubmissionDto;
import cz.cvut.fel.pm2.timely_be.mapper.MapperUtils;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final EmployeeRepository employeeRepository;
    public SubmissionService(SubmissionRepository submissionRepository, EmployeeRepository employeeRepository) {
        this.submissionRepository = submissionRepository;
        this.employeeRepository = employeeRepository;
    }

    public Submission createSubmission(Long employeeId, String message) {
        Submission submission = new Submission();
        submission.setEmployeeId(employeeId);
        submission.setMessage(message);
        submission.setStatus(LeaveStatus.PENDING);
        submission.setDatetime(LocalDateTime.now());
        return submissionRepository.save(submission);
    }

    public List<SubmissionDto> getPendingSubmissions() {
        return submissionRepository.findAllPendingOrderByDatetimeDesc().stream()
                .map(submission -> {
                    Employee employee = employeeRepository.findById(submission.getEmployeeId())
                            .orElseThrow(() -> new IllegalArgumentException("Employee not found for submission"));
                    return MapperUtils.toSubmissionDto(submission, employee);
                })
                .collect(Collectors.toList());
    }

    public List<Submission> getSubmissionsByEmployeeId(Long employeeId) {
        return submissionRepository.findAllByEmployeeIdOrderByDatetimeDesc(employeeId);
    }

    public SubmissionDto getSubmissionDto(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found"));
        Employee employee = employeeRepository.findById(submission.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found for submission"));
        return MapperUtils.toSubmissionDto(submission, employee);
    }

    public Submission updateSubmissionStatus(Long messageId, LeaveStatus status) {
        Submission submission = submissionRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found"));
        submission.setStatus(status);
        return submissionRepository.save(submission);
    }

}