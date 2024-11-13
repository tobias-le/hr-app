package cz.cvut.fel.pm2.timely_be.repository;

import cz.cvut.fel.pm2.timely_be.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.lang.NonNull;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByName(String name);

    @Override
    @NonNull
    @Query("SELECT t FROM teams t WHERE t.deleted = false")
    List<Team> findAll();

    @Query("SELECT t FROM teams t LEFT JOIN FETCH t.members WHERE t.id = :teamId AND t.deleted = false")
    Optional<Team> findTeamWithMembers(@Param("teamId") Long teamId);
}
