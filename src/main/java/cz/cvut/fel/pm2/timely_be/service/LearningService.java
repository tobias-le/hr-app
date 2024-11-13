package cz.cvut.fel.pm2.timely_be.service;

import cz.cvut.fel.pm2.timely_be.model.Learning;
import cz.cvut.fel.pm2.timely_be.repository.EmployeeRepository;
import cz.cvut.fel.pm2.timely_be.repository.LearningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LearningService {

    private final LearningRepository learningRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public LearningService(LearningRepository learningRepository, EmployeeRepository employeeRepository) {
        this.learningRepository = learningRepository;
        this.employeeRepository = employeeRepository;
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
        return employee.getFinishedLearnings();
    }

    public Iterable<Learning> registerLearningToEmployee(Long employeeId, Long learningId) {
        var employee = employeeRepository.findById(employeeId).orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        var learning = learningRepository.findById(learningId).orElseThrow(() -> new IllegalArgumentException("Learning not found"));
        employee.getFinishedLearnings().add(learning);
        return learningRepository.findAll();
    }
}
