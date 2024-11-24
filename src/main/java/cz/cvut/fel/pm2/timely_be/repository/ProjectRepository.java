package cz.cvut.fel.pm2.timely_be.repository;

import cz.cvut.fel.pm2.timely_be.model.Project;
import cz.cvut.fel.pm2.timely_be.dto.ProjectNameWithIdDto;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT new cz.cvut.fel.pm2.timely_be.dto.ProjectNameWithIdDto(p.projectId, p.name) " +
           "FROM projects p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :namePattern, '%')) " +
           "AND p.projectId NOT IN :excludeIds AND p.deleted = false")
    List<ProjectNameWithIdDto> findProjectsByNameContaining(
        @Param("namePattern") String namePattern,
        @Param("excludeIds") List<Long> excludeIds,
        Pageable pageable);
}
