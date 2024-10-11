package cz.cvut.fel.pm2.hrapp.employeemanagement.model;
import jakarta.persistence.*;

import java.util.List;
@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_department_id")
    private Department parentDepartment;

    @OneToMany(mappedBy = "parentDepartment")
    private List<Department> subDepartments;

    @OneToMany(mappedBy = "department")
    private List<Employee> employees;

}