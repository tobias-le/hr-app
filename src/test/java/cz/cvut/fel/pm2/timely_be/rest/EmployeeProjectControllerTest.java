package cz.cvut.fel.pm2.timely_be.rest;

import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.model.Project;
import cz.cvut.fel.pm2.timely_be.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeProjectController.class)
class EmployeeProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Test
    void testGetProjectsForEmployee_Success() throws Exception {
        long employeeId = 1L;
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        Project project1 = new Project();
        project1.setProjectId(101L);
        project1.setName("Project A");
        Project project2 = new Project();
        project2.setProjectId(102L);
        project2.setName("Project B");
        employee.setCurrentProjects(List.of(project1, project2));

        when(employeeService.getEmployee(employeeId)).thenReturn(employee);

        mockMvc.perform(get("/api/employees/{employeeId}/projects", employeeId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].projectId").value(101))
                .andExpect(jsonPath("$[0].name").value("Project A"))
                .andExpect(jsonPath("$[1].projectId").value(102))
                .andExpect(jsonPath("$[1].name").value("Project B"));
    }


    @Test
    void testGetProjectsForEmployee_NotFound() throws Exception {
        long employeeId = 1L;
        when(employeeService.getEmployee(employeeId)).thenThrow(new IllegalArgumentException("Employee not found"));

        mockMvc.perform(get("/api/employees/{employeeId}/projects", employeeId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetProjectsForEmployee_NoProjects() throws Exception {
        long employeeId = 1L;
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        employee.setCurrentProjects(Collections.emptyList()); // Empty list of projects

        when(employeeService.getEmployee(employeeId)).thenReturn(employee);

        mockMvc.perform(get("/api/employees/{employeeId}/projects", employeeId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty()); // Check for empty JSON array
    }

}