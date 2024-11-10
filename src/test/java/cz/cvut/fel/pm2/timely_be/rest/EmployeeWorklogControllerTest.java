package cz.cvut.fel.pm2.timely_be.rest;

import cz.cvut.fel.pm2.timely_be.dto.AttendanceRecordDto;
import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.service.AttendanceService;
import cz.cvut.fel.pm2.timely_be.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeWorklogController.class)
class EmployeeWorklogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private AttendanceService attendanceService;



    @Test
    void testGetWorklogForEmployee() throws Exception {
        long employeeId = 1L;

        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);

        List<AttendanceRecordDto> mockWorklog = IntStream.range(0, 10) //create 10 records
                .mapToObj(i -> {
                    AttendanceRecordDto dto = new AttendanceRecordDto();
                    dto.setDate(LocalDate.now().minusDays(i)); //different dates for sorting check
                    dto.setClockInTime(LocalDateTime.now());
                    return dto;
                })
                .collect(Collectors.toList());



        when(employeeService.getEmployee(employeeId)).thenReturn(employee);
        when(attendanceService.getAttendanceRecordsByMember(employee)).thenReturn(mockWorklog);

        mockMvc.perform(get("/api/employees/{employeeId}/worklog", employeeId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(5)) // Check that only 5 records are returned
                .andExpect(jsonPath("$[0].date").value(LocalDate.now().toString())) // Check that the first record is the latest
                .andExpect(jsonPath("$[1].date").value(LocalDate.now().minusDays(1).toString())); //check sorting
    }


    @Test
    void testGetWorklogForEmployeeNotFound() throws Exception {
        long employeeId = 1L;
        when(employeeService.getEmployee(employeeId)).thenThrow(new IllegalArgumentException("Employee not found"));

        mockMvc.perform(get("/api/employees/{employeeId}/worklog", employeeId))
                .andExpect(status().isNotFound());
    }
}
