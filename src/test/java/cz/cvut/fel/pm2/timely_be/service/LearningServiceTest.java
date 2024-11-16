package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.repository.EmployeeLearningRepository;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeRepository;
import cz.cvut.fel.pm2.timely_be.repository.LearningRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    public void testGetLearnings(){
        // Given
        // When
        // Then
    }

}
