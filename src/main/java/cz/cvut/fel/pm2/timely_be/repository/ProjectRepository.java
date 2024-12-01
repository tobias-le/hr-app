package cz.cvut.fel.pm2.timely_be.repository;

import cz.cvut.fel.pm2.timely_be.model.Project;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM projects p LEFT JOIN FETCH p.members WHERE p.projectId = :projectId AND p.deleted = false")
    Optional<Project> findProjectWithMembers(@Param("projectId") Long projectId);

    @Override
    @NonNull
    @Query("SELECT p FROM projects p WHERE p.deleted = false")
    List<Project> findAll();

    @Query("SELECT p FROM projects p WHERE p.name = :name AND p.deleted = false")
    Optional<Project> findByNameAndDeletedFalse(@Param("name") String name);

    @Query("SELECT p FROM projects p WHERE p.name = :name AND p.deleted = true")
    Optional<Project> findByNameAndDeletedTrue(@Param("name") String name);
}
