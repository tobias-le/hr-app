package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.dto.LearningAssignmentDto;
import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.model.EmployeeLearning;
import cz.cvut.fel.pm2.timely_be.model.Learning;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeLearningRepository;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeRepository;
import cz.cvut.fel.pm2.timely_be.repository.LearningRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static cz.cvut.fel.pm2.timely_be.utils.TestUtils.createLearning;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LearningServiceTest {

    @Mock
    private LearningRepository learningRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeLearningRepository employeeLearningRepository;

    @InjectMocks
    private LearningService learningService;

    @Test
    public void testRegisterLearning(){
        // Given
        var learningName = "New Learning 1";
        var learningLink = "https://www.learning1.com";
        var learning = new Learning();
        learning.setName(learningName);
        learning.setLink(learningLink);

        when(learningRepository.save(any(Learning.class))).thenReturn(learning);
        // When
        var result = learningService.registerLearning(learningName, learningLink);
        // Then

        assertNotNull(result);
        assertEquals(learningName, result.getName());
    }

    @Test
    public void testGetAllLearnings(){
        // Given
        var learning1 = createLearning();
        var learning2 = createLearning();
        var learnings = Arrays.asList(learning1, learning2);
        when(learningRepository.findAll()).thenReturn(learnings);
        // When
        var result = learningService.getAllLearnings();
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(learnings, result);
    }

    @Test
    public void testGetLearningsByEmployeeId() {
        // Given
        var employeeId = 1L;
        var learning1 = new Learning();
        learning1.setLearningId(101L);
        learning1.setName("Learning 1");

        var learning2 = new Learning();
        learning2.setLearningId(102L);
        learning2.setName("Learning 2");

        var employee = new Employee();
        employee.setEmployeeId(employeeId);

        var employeeLearning1 = new EmployeeLearning();
        employeeLearning1.setLearning(learning1);

        var employeeLearning2 = new EmployeeLearning();
        employeeLearning2.setLearning(learning2);

        employee.setLearnings(List.of(employeeLearning1, employeeLearning2));

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        // When
        var result = learningService.getLearningsByEmployeeId(employeeId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Learning 1", result.get(0).getName());
        assertEquals("Learning 2", result.get(1).getName());
    }

    @Test
    public void testRegisterLearningToEmployee() {
        // Given
        var employeeId = 1L;
        var learningId = 101L;
        var learningAssignmentDto = new LearningAssignmentDto();
        learningAssignmentDto.setEmployeeId(employeeId);
        learningAssignmentDto.setLearningId(learningId);

        var employee = new Employee();
        employee.setEmployeeId(employeeId);

        var learning = new Learning();
        learning.setLearningId(learningId);

        var savedEmployeeLearning = new EmployeeLearning();
        savedEmployeeLearning.setEmployee(employee);
        savedEmployeeLearning.setLearning(learning);
        savedEmployeeLearning.setDate(LocalDate.now().plusDays(7));

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(learningRepository.findById(learningId)).thenReturn(Optional.of(learning));
        when(employeeLearningRepository.save(any(EmployeeLearning.class))).thenReturn(savedEmployeeLearning);

        // When
        var result = learningService.registerLearningToEmployee(learningAssignmentDto);

        // Then
        assertNotNull(result);
        assertEquals(LocalDate.now().plusDays(7), result.getDate());
        assertEquals(employeeId, result.getEmployee().getEmployeeId()); // Compare IDs
        assertEquals(learningId, result.getLearning().getLearningId()); // Compare IDs
    }


    @Test
    public void testGetAllProjectsWhenEmpty() {
        // Given
        when(learningRepository.findAll()).thenReturn(Collections.emptyList());
        // When
        var result = learningService.getAllLearnings();
        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

}
