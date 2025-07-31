package ro.mdx.meditation.exceptions.student;

import ro.mdx.meditation.exceptions.GlobalException;
import ro.mdx.meditation.exceptions.StatusCodes;

public class StudentNotFoundException extends GlobalException {
    public StudentNotFoundException() {
        super("Student not found", StatusCodes.BAD_REQUEST.getCode());
    }
}
