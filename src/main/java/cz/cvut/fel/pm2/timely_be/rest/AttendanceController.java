package cz.cvut.fel.pm2.timely_be.rest;

import cz.cvut.fel.pm2.timely_be.dto.AttendanceRecordDto;
import cz.cvut.fel.pm2.timely_be.dto.AttendanceSummaryDTO;
import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static cz.cvut.fel.pm2.timely_be.mapper.MapperUtils.toAttendanceRecordDto;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/attendance")
@Tag(name = "Attendance", description = "The Attendance API")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
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

    @GetMapping("/project/{projectId}")
    @Operation(summary = "Get attendance records for a project for a specified date range")
    public ResponseEntity<List<AttendanceRecordDto>> getAttendanceRecordsByProject(
        @PathVariable Long projectId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        var attendanceRecords = attendanceService.getAttendanceRecordsByProject(projectId, startDate, endDate);
        return ResponseEntity.ok(attendanceRecords);
    }

    @GetMapping("/project/{projectId}/summary")
    @Operation(summary = "Get an attendance summary for a specified date range on a project")
    public ResponseEntity<AttendanceSummaryDTO> getAttendanceSummaryForProject(
        @PathVariable Long projectId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        var attendanceSummary = attendanceService.getAttendanceSummaryForProject(projectId, startDate, endDate);
        return ResponseEntity.ok(attendanceSummary);
    }

    @PostMapping
    @Operation(summary = "Create a new attendance record")
    public ResponseEntity<AttendanceRecordDto> createAttendanceRecord(@RequestBody AttendanceRecordDto attendanceRecordDto) {
        var attendanceRecord = attendanceService.createAttendanceRecord(attendanceRecordDto);
        return ResponseEntity.created(
                fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(attendanceRecord.getAttendanceId())
                        .toUri()
        ).body(toAttendanceRecordDto(attendanceRecord));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an attendance record by ID")
    public ResponseEntity<Void> deleteAttendanceRecordById(@PathVariable Long id) {
        attendanceService.deleteAttendanceRecordById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an attendance record by ID")
    public ResponseEntity<AttendanceRecordDto> updateAttendanceRecordById(@PathVariable Long id, @RequestBody AttendanceRecordDto attendanceRecordDto) {
        var attendanceRecord = attendanceService.updateAttendanceRecordById(id, attendanceRecordDto);
        return ResponseEntity.ok(toAttendanceRecordDto(attendanceRecord));
    }
}
