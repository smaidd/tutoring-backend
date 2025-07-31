package ro.mdx.meditation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.mdx.meditation.model.TeacherSession;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface TeacherSessionRepository extends JpaRepository<TeacherSession, UUID> {

    @Query("SELECT ts FROM TeacherSession ts " +
            "WHERE ts.teacher.id = :teacherId " +
            "AND function('DATE', ts.classDate) BETWEEN :from AND :to ")
    List<TeacherSession> findTeacherSessions(@Param("teacherId") UUID teacherId,
                                             @Param("from") LocalDate from,
                                             @Param("to") LocalDate to);
}
