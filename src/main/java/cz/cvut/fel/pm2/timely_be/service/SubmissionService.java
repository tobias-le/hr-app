package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.enums.SubmissionStatus;
import cz.cvut.fel.pm2.timely_be.model.Submission;
import cz.cvut.fel.pm2.timely_be.repository.SubmissionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;

    public SubmissionService(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    public Submission createSubmission(Long employeeId, String message) {
        Submission submission = new Submission();
        submission.setEmployeeId(employeeId);
        submission.setMessage(message);
        submission.setStatus(SubmissionStatus.PENDING);
        submission.setDatetime(LocalDateTime.now());
        return submissionRepository.save(submission);
    }

    public List<Submission> getPendingSubmissions() {
        return submissionRepository.findAllPendingOrderByDatetimeDesc();
    }

    public List<Submission> getSubmissionsByEmployeeId(Long employeeId) {
        return submissionRepository.findAllByEmployeeIdOrderByDatetimeDesc(employeeId);
    }

    public Submission updateSubmissionStatus(Long messageId, SubmissionStatus status) {
        Submission submission = submissionRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found"));
        submission.setStatus(status);
        return submissionRepository.save(submission);
    }
}