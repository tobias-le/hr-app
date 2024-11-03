package cz.cvut.fel.pm2.timely_be.repository;

import cz.cvut.fel.pm2.timely_be.model.AttendanceRecord;
import cz.cvut.fel.pm2.timely_be.model.Employee;
import cz.cvut.fel.pm2.timely_be.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    @Query("SELECT ar FROM AttendanceRecord ar WHERE ar.member = :member")
    List<AttendanceRecord> findByMember(@Param("member") Employee member);
    @Query("SELECT ar FROM AttendanceRecord ar WHERE ar.member.team = :team AND ar.date BETWEEN :startDate AND :endDate")
    List<AttendanceRecord> findByTeamAndDateBetween(@Param("team") Team team, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    @Query("SELECT ar FROM AttendanceRecord ar WHERE ar.member.team.id = :teamId AND ar.date BETWEEN :startOfWeek AND :today")
    List<AttendanceRecord> findByTeamIdAndDateBetween(@Param("teamId") Long teamId, @Param("startOfWeek") LocalDate startOfWeek, @Param("today") LocalDate today);
}
