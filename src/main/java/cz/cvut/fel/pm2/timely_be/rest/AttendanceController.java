package cz.cvut.fel.pm2.timely_be.rest;

import cz.cvut.fel.pm2.timely_be.dto.AttendanceRecordDto;
import cz.cvut.fel.pm2.timely_be.dto.AttendanceSummaryDTO;
import cz.cvut.fel.pm2.timely_be.mapper.MapperUtils;
import cz.cvut.fel.pm2.timely_be.model.AttendanceRecord;
import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.model.Project;
import cz.cvut.fel.pm2.timely_be.service.AttendanceService;
import cz.cvut.fel.pm2.timely_be.service.EmployeeService;
import cz.cvut.fel.pm2.timely_be.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cz.cvut.fel.pm2.timely_be.repository.AttendanceRecordRepository;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@Tag(name = "Attendance", description = "The Attendance API")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final EmployeeService employeeService;
    private final ProjectService projectService;
    private final AttendanceRecordRepository attendanceRecordRepository;

    @Autowired
    public AttendanceController(AttendanceService attendanceService,
                                EmployeeService employeeService,
                                ProjectService projectService,
                                AttendanceRecordRepository attendanceRecordRepository) {
        this.attendanceService = attendanceService;
        this.employeeService = employeeService;
        this.projectService = projectService;
        this.attendanceRecordRepository = attendanceRecordRepository;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an attendance record by ID")
    public ResponseEntity<AttendanceRecordDto> getAttendanceRecordById(@PathVariable Long id) {
        var attendanceRecord = attendanceService.getAttendanceRecordById(id);
        return attendanceRecord.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/member/{memberId}")
    @Operation(summary = "Get attendance records for a specific member")
    public ResponseEntity<List<AttendanceRecordDto>> getAttendanceRecordsByMember(@PathVariable Long memberId) {
        var member = new Employee();
        member.setEmployeeId(memberId);
        var attendanceRecords = attendanceService.getAttendanceRecordsByMember(member);
        return ResponseEntity.ok(attendanceRecords);
    }

    @GetMapping("/team/{teamId}")
    @Operation(summary = "Get attendance records for a team for past work week")
    public ResponseEntity<List<AttendanceRecordDto>> getAttendanceRecordsByTeamSinceStartOfWeek(@PathVariable Long teamId) {
        var attendanceRecordsByTeam = attendanceService.getAttendanceRecordsByTeamSinceStartOfWeek(teamId);
        return ResponseEntity.ok(attendanceRecordsByTeam);
    }

    @GetMapping("/team/{teamId}/summary")
    @Operation(summary = "Get an attendance summary for the current work week")
    public ResponseEntity<AttendanceSummaryDTO> getCurrentWeekAttendanceSummary(@PathVariable Long teamId) {
        // Call the service method to get the current week's attendance performance
        var attendancePerformance = attendanceService.getCurrentWeekAttendancePerformance(teamId);
        return ResponseEntity.ok(attendancePerformance);
    }

    @PostMapping
    @Operation(summary = "Add attendance record", description = "Adds a new attendance record")
    public ResponseEntity<AttendanceRecordDto> addAttendanceRecord(@RequestBody AttendanceRecordDto attendanceRecordDto) {
        try {
            Employee member = employeeService.getEmployee(attendanceRecordDto.getMemberId());
            Project project = projectService.getProjectById(attendanceRecordDto.getProjectId());

            AttendanceRecord attendanceRecord = new AttendanceRecord();
            attendanceRecord.setMember(member);
            attendanceRecord.setDate(attendanceRecordDto.getDate() != null ? attendanceRecordDto.getDate() : LocalDate.now());
            attendanceRecord.setClockInTime(attendanceRecordDto.getClockInTime() != null ? attendanceRecordDto.getClockInTime() : LocalDateTime.now());
            attendanceRecord.setClockOutTime(attendanceRecordDto.getClockOutTime());
            attendanceRecord.setProject(project);

            AttendanceRecord savedRecord = attendanceRecordRepository.save(attendanceRecord);

            AttendanceRecordDto savedRecordDto = MapperUtils.toAttendanceRecordDto(savedRecord);
            return ResponseEntity.created(URI.create("/api/attendance/" + savedRecord.getAttendanceId())).body(savedRecordDto); //201 created

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); //return only 400 Bad Request
        }
    }
}
