package ro.mdx.meditation.exceptions.teacher;

import ro.mdx.meditation.exceptions.GlobalException;
import ro.mdx.meditation.exceptions.StatusCodes;

public class TeacherNotFoundException extends GlobalException {
    public TeacherNotFoundException() {
        super("Teacher not found", StatusCodes.BAD_REQUEST.getCode());
    }
}
