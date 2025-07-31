package ro.mdx.meditation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.mdx.meditation.model.TeacherRemuneration;

import java.util.UUID;

@Repository
public interface RemunerationRepository extends JpaRepository<TeacherRemuneration, UUID> {
}
