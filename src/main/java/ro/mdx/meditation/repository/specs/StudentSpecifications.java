package ro.mdx.meditation.repository.specs;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import ro.mdx.meditation.model.Student;
import ro.mdx.meditation.model.Subject;

import java.util.UUID;

public class StudentSpecifications {

    private StudentSpecifications() {
    }

    public static Specification<Student> hasSubjectId(String subjectId) {
        return (root, query, cb) -> {
            Join<Student, Subject> subjects = root.join("subjects");
            return cb.equal(subjects.get("id"), UUID.fromString(subjectId));
        };
    }
}