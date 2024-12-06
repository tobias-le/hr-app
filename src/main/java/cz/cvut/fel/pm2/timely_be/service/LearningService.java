package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.dto.LearningAssignmentDto;
import cz.cvut.fel.pm2.timely_be.model.EmployeeLearning;
import cz.cvut.fel.pm2.timely_be.model.Learning;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeLearningRepository;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeRepository;
import cz.cvut.fel.pm2.timely_be.repository.LearningRepository;
import cz.cvut.fel.pm2.timely_be.repository.composite.EmployeeLearningId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LearningService {

    private final LearningRepository learningRepository;
    private final EmployeeLearningRepository employeeLearningRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public LearningService(LearningRepository learningRepository, EmployeeRepository employeeRepository, EmployeeLearningRepository employeeLearningRepository) {
        this.learningRepository = learningRepository;
        this.employeeRepository = employeeRepository;
        this.employeeLearningRepository = employeeLearningRepository;
    }

    public Learning registerLearning(String name, String link){
        Learning learning = new Learning();
        learning.setName(name);
        learning.setLink(link);
        return learningRepository.save(learning);
    }

    public List<Learning> getAllLearnings() {
        return learningRepository.findAll();
    }

    public List<Learning> getLearningsByEmployeeId(Long employeeId) {
        var employee = employeeRepository.findById(employeeId).orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        return employee.getLearnings().stream().map(EmployeeLearning::getLearning).toList();
    }

    public EmployeeLearning registerLearningToEmployee(LearningAssignmentDto learningAssignment) {
        var employeeId = learningAssignment.getEmployeeId();
        var learningId = learningAssignment.getLearningId();
        var employee = employeeRepository.findById(employeeId).orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        var learning = learningRepository.findById(learningId).orElseThrow(() -> new IllegalArgumentException("Learning not found"));
        var newLearningAssignmentToEmployee = new EmployeeLearning();
        var employeeLearningId = new EmployeeLearningId();
        newLearningAssignmentToEmployee.setId(employeeLearningId);
        newLearningAssignmentToEmployee.setEmployee(employee);
        newLearningAssignmentToEmployee.setLearning(learning);
        newLearningAssignmentToEmployee.setDate(LocalDate.now().plusDays(7));
        return employeeLearningRepository.save(newLearningAssignmentToEmployee);
    }
}
