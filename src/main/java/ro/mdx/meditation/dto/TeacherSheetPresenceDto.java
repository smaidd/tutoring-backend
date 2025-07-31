package ro.mdx.meditation.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TeacherSheetPresenceDto {
   private List<StudentDto> students = new ArrayList<>();
   private double remuneration;
}
