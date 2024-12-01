package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.dto.SubmissionDto;
import cz.cvut.fel.pm2.timely_be.enums.RequestStatus;
import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.model.Submission;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeRepository;
import cz.cvut.fel.pm2.timely_be.repository.SubmissionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubmissionServiceTest {

    @Mock
    private SubmissionRepository submissionRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private SubmissionService submissionService;

    @Test
    void createSubmission() {
        Long employeeId = 1L;
        String message = "Test message";
        Submission submission = new Submission();
        submission.setEmployeeId(employeeId);
        submission.setMessage(message);
        submission.setDatetime(LocalDateTime.now());
        submission.setStatus(RequestStatus.PENDING);

        when(submissionRepository.save(any(Submission.class))).thenReturn(submission);

        Submission createdSubmission = submissionService.createSubmission(employeeId, message);

        assertEquals(employeeId, createdSubmission.getEmployeeId());
        assertEquals(message, createdSubmission.getMessage());
        assertEquals(RequestStatus.PENDING, createdSubmission.getStatus());
        verify(submissionRepository, times(1)).save(any(Submission.class));
    }

    @Test
    void getPendingSubmissions() {
        Submission submission = new Submission();
        submission.setEmployeeId(1L);
        submission.setMessage("Test message");
        submission.setDatetime(LocalDateTime.now());
        submission.setStatus(RequestStatus.PENDING);
        submission.setMessageId(1L);

        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setName("Test Employee");

        when(submissionRepository.findPendingSubmissions()).thenReturn(List.of(submission));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));


        List<SubmissionDto> pendingSubmissions = submissionService.getPendingSubmissions();

        assertEquals(1, pendingSubmissions.size());

        SubmissionDto submissionDto = pendingSubmissions.get(0);
        assertEquals(submission.getMessageId(), submissionDto.getMessageId());
        assertEquals(employee.getName(), submissionDto.getEmployee().getName());
        verify(submissionRepository, times(1)).findPendingSubmissions();
        verify(employeeRepository, times(1)).findById(1L);
    }


    @Test
    void getPendingSubmissions_employeeNotFound() {
        Submission submission = new Submission();
        submission.setEmployeeId(1L);

        when(submissionRepository.findPendingSubmissions()).thenReturn(List.of(submission));
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> submissionService.getPendingSubmissions());

        verify(submissionRepository, times(1)).findPendingSubmissions();
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void getSubmissionsByEmployeeId() {
        Long employeeId = 1L;
        Submission submission = new Submission();
        submission.setEmployeeId(employeeId);

        when(submissionRepository.findSubmissionsByEmployeeId(employeeId)).thenReturn(List.of(submission));

        List<Submission> submissions = submissionService.getSubmissionsByEmployeeId(employeeId);

        assertEquals(1, submissions.size());
        assertEquals(employeeId, submissions.get(0).getEmployeeId());
        verify(submissionRepository, times(1)).findSubmissionsByEmployeeId(employeeId);
    }

    @Test
    void updateSubmissionStatus() {
        Long submissionId = 1L;
        RequestStatus newStatus = RequestStatus.APPROVED;
        Submission submission = new Submission();
        submission.setMessageId(submissionId);
        submission.setStatus(RequestStatus.PENDING);

        when(submissionRepository.findById(submissionId)).thenReturn(Optional.of(submission));
        when(submissionRepository.save(any(Submission.class))).thenReturn(submission);

        Submission updatedSubmission = submissionService.updateSubmissionStatus(submissionId, newStatus);

        assertEquals(newStatus, updatedSubmission.getStatus());
        verify(submissionRepository, times(1)).findById(submissionId);
        verify(submissionRepository, times(1)).save(any(Submission.class));
    }

    @Test
    void updateSubmissionStatus_submissionNotFound() {
        Long submissionId = 1L;
        RequestStatus newStatus = RequestStatus.APPROVED;

        when(submissionRepository.findById(submissionId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> submissionService.updateSubmissionStatus(submissionId, newStatus));

        verify(submissionRepository, times(1)).findById(submissionId);
        verify(submissionRepository, never()).save(any(Submission.class));
    }
}
