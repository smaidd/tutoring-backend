package ro.mdx.meditation.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.mdx.meditation.model.StudentSheet;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface StudentSheetRepository extends JpaRepository<StudentSheet, UUID> {
    Optional<StudentSheet> findByStudentId(UUID studentId);

    @Query("SELECT s FROM StudentSheet s JOIN s.presence p WHERE p.professor.id = :teacherId")
    Page<StudentSheet> findAllByTeacher(@Param("teacherId") UUID teacherId, Pageable pageable);
}
