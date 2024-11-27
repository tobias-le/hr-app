package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.dto.EmployeeNameWithIdDto;
import cz.cvut.fel.pm2.timely_be.dto.SubmissionDto;
import cz.cvut.fel.pm2.timely_be.enums.RequestStatus;
import cz.cvut.fel.pm2.timely_be.mapper.MapperUtils;
import cz.cvut.fel.pm2.timely_be.model.Submission;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeRepository;
import cz.cvut.fel.pm2.timely_be.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public SubmissionService(SubmissionRepository submissionRepository, EmployeeRepository employeeRepository) {
        this.submissionRepository = submissionRepository;
        this.employeeRepository = employeeRepository;
    }

    public Submission createSubmission(Long employeeId, String message) {
        Submission submission = new Submission();
        submission.setEmployeeId(employeeId);
        submission.setMessage(message);
        submission.setDatetime(LocalDateTime.now());
        submission.setStatus(RequestStatus.PENDING);
        return submissionRepository.save(submission);
    }

    public List<SubmissionDto> getPendingSubmissions() {
        return submissionRepository.findPendingSubmissions().stream()
                .map(submission -> {
                    EmployeeNameWithIdDto employee = employeeRepository.findById(submission.getEmployeeId())
                            .map(MapperUtils::toEmployeeNameWithIdDto)
                            .orElseThrow(() -> new IllegalArgumentException("Employee not found for submission"));
                    return MapperUtils.toSubmissionDto(submission, employee);
                })
                .collect(Collectors.toList());
    }

    public List<Submission> getSubmissionsByEmployeeId(Long employeeId) {
        return submissionRepository.findSubmissionsByEmployeeId(employeeId);
    }

    public Submission updateSubmissionStatus(Long submissionId, RequestStatus status) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found"));
        submission.setStatus(status);
        return submissionRepository.save(submission);
    }
}