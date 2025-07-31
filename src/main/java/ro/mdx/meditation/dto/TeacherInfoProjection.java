package ro.mdx.meditation.dto;

import ro.mdx.meditation.model.Presence;
import ro.mdx.meditation.model.Student;
import ro.mdx.meditation.model.Teacher;

import java.util.List;

public interface TeacherInfoProjection {
    List<Presence> getPresences();

    Student getStudent();

    Teacher getTeacher();
}
