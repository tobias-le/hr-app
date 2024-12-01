package cz.cvut.fel.pm2.timely_be.repository;

import cz.cvut.fel.pm2.timely_be.dto.TeamNameWithIdDto;
import cz.cvut.fel.pm2.timely_be.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.lang.NonNull;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByName(String name);

    @Override
    @NonNull
    @Query("SELECT t FROM teams t WHERE t.deleted = false")
    List<Team> findAll();

    @Query("SELECT t FROM teams t " +
           "LEFT JOIN FETCH t.members " +
           "WHERE t.id = :teamId AND t.deleted = false")
    Optional<Team> findTeamWithMembers(@Param("teamId") Long teamId);
    
    @Query("SELECT t FROM teams t " +
           "LEFT JOIN FETCH t.parentTeam " +
           "WHERE t.id = :teamId AND t.deleted = false")
    Optional<Team> findTeamWithParent(@Param("teamId") Long teamId);

    @Query("SELECT new cz.cvut.fel.pm2.timely_be.dto.TeamNameWithIdDto(t.id, t.name) " +
           "FROM teams t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :namePattern, '%')) " +
           "AND t.deleted = false")
    List<TeamNameWithIdDto> findTeamsByNameContaining(
        @Param("namePattern") String namePattern,
        Pageable pageable);

    @Query(value = """
        WITH RECURSIVE TeamHierarchy AS (
            SELECT * FROM teams t 
            WHERE t.id = :teamId AND t.deleted = false
            
            UNION ALL
            
            SELECT t.* FROM teams t
            INNER JOIN TeamHierarchy th ON t.id = th.parent_team_id 
            WHERE t.deleted = false
        )
        SELECT DISTINCT t.* FROM teams t
        WHERE t.id IN (SELECT id FROM TeamHierarchy)
        """, nativeQuery = true)
    List<Team> findTeamWithCompleteHierarchy(@Param("teamId") Long teamId);

    @Query("SELECT t FROM teams t " +
           "LEFT JOIN FETCH t.members " +
           "LEFT JOIN FETCH t.parentTeam " +
           "WHERE t.id = :teamId AND t.deleted = false")
    Optional<Team> findTeamWithMembersAndParent(@Param("teamId") Long teamId);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM teams t " +
           "JOIN t.members m WHERE m.employeeId = :employeeId AND t.deleted = false")
    boolean isEmployeeInAnyTeam(@Param("employeeId") Long employeeId);

    @Query("SELECT t FROM teams t " +
           "LEFT JOIN FETCH t.members " +
           "LEFT JOIN FETCH t.parentTeam " +
           "WHERE :employeeId IN (SELECT m.employeeId FROM t.members m) " +
           "AND t.deleted = false")
    Optional<Team> findTeamByEmployeeId(@Param("employeeId") Long employeeId);

    @Query("SELECT t FROM teams t " +
           "LEFT JOIN FETCH t.members " +
           "LEFT JOIN FETCH t.parentTeam " +
           "WHERE t.manager.employeeId = :managerId " +
           "AND t.deleted = false")
    Optional<Team> findTeamByManagerId(@Param("managerId") Long managerId);

    @Query("SELECT t FROM teams t WHERE t.name = :name AND t.deleted = false")
    Optional<Team> findByNameAndDeletedFalse(@Param("name") String name);

    @Query("SELECT t FROM teams t WHERE t.name = :name AND t.deleted = true")
    Optional<Team> findByNameAndDeletedTrue(@Param("name") String name);
}
