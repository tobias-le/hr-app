package cz.cvut.fel.pm2.timely_be.rest;

import cz.cvut.fel.pm2.timely_be.dto.AttendanceRecordDto;
import cz.cvut.fel.pm2.timely_be.mapper.MapperUtils;
import cz.cvut.fel.pm2.timely_be.model.AttendanceRecord;
import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.model.Project;
import cz.cvut.fel.pm2.timely_be.repository.AttendanceRecordRepository;
import cz.cvut.fel.pm2.timely_be.rest.AttendanceController;
import cz.cvut.fel.pm2.timely_be.service.AttendanceService;
import cz.cvut.fel.pm2.timely_be.service.EmployeeService;
import cz.cvut.fel.pm2.timely_be.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AttendanceController.class)
public class AttendanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AttendanceService attendanceService;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private AttendanceRecordRepository attendanceRecordRepository;

    @Test
    void testAddAttendanceRecord() throws Exception {

        long employeeId = 1L;
        long projectId = 1L;
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        Project project = new Project();
        project.setProjectId(projectId);


        AttendanceRecordDto attendanceRecordDto = new AttendanceRecordDto();
        attendanceRecordDto.setMemberId(employeeId);
        attendanceRecordDto.setProjectId(projectId);
        attendanceRecordDto.setDate(LocalDate.now());
        attendanceRecordDto.setClockInTime(LocalDateTime.now());


        AttendanceRecord savedRecord = new AttendanceRecord();
        savedRecord.setAttendanceId(1L);
        savedRecord.setMember(employee);
        savedRecord.setProject(project);
        savedRecord.setDate(attendanceRecordDto.getDate());
        savedRecord.setClockInTime(attendanceRecordDto.getClockInTime());

        when(employeeService.getEmployee(employeeId)).thenReturn(employee);
        when(projectService.getProjectById(projectId)).thenReturn(project);
        when(attendanceRecordRepository.save(any(AttendanceRecord.class))).thenReturn(savedRecord);


        mockMvc.perform(post("/api/attendance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"memberId": 1, "projectId": 1, "date": "2024-12-08", "clockInTime": "2024-12-08T10:00:00"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/attendance/1"));


    }

    @Test
    void testAddAttendanceRecordSuccess() throws Exception {
        long employeeId = 1L;
        long projectId = 1L;

        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);

        Project project = new Project();
        project.setProjectId(projectId);

        AttendanceRecordDto attendanceRecordDto = new AttendanceRecordDto();
        attendanceRecordDto.setMemberId(employeeId);
        attendanceRecordDto.setProjectId(projectId);
        attendanceRecordDto.setDate(LocalDate.of(2024, 12, 8)); // Specific date
        attendanceRecordDto.setClockInTime(LocalDateTime.of(2024, 12, 8, 10, 0, 0)); // Specific time


        AttendanceRecord savedRecord = new AttendanceRecord();
        savedRecord.setAttendanceId(1L);
        savedRecord.setMember(employee);
        savedRecord.setProject(project);
        savedRecord.setDate(attendanceRecordDto.getDate());
        savedRecord.setClockInTime(attendanceRecordDto.getClockInTime());
        savedRecord.setClockOutTime(null); // Explicitly set to null


        when(employeeService.getEmployee(employeeId)).thenReturn(employee);
        when(projectService.getProjectById(projectId)).thenReturn(project);
        when(attendanceRecordRepository.save(any(AttendanceRecord.class))).thenReturn(savedRecord);


        mockMvc.perform(post("/api/attendance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(attendanceRecordDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/attendance/1"))
                .andExpect(content().json(objectMapper.writeValueAsString(MapperUtils.toAttendanceRecordDto(savedRecord))));
    }
    @Test
    void testAddAttendanceRecordBadRequest() throws Exception {
        long employeeId = 1L;
        long projectId = 1L;
        AttendanceRecordDto attendanceRecordDto = new AttendanceRecordDto();
        attendanceRecordDto.setMemberId(employeeId);
        attendanceRecordDto.setProjectId(projectId);

        when(employeeService.getEmployee(employeeId)).thenThrow(new IllegalArgumentException("Employee not found")); // Throw exception

        mockMvc.perform(post("/api/attendance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(attendanceRecordDto)))
                .andExpect(status().isBadRequest())
                .andExpect(status().isBadRequest());
    }



    @Test
    void testGetProjectsForEmployeeNotFound() throws Exception {
        long employeeId = 1L;

        when(employeeService.getEmployee(employeeId)).thenThrow(new IllegalArgumentException("Employee not found"));

        mockMvc.perform(get("/api/employees/{employeeId}/projects", employeeId))
                .andExpect(status().isNotFound()); // Expect 404 Not Found

    }

}