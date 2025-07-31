package ro.mdx.meditation.exceptions.student;

import ro.mdx.meditation.exceptions.GlobalException;
import ro.mdx.meditation.exceptions.StatusCodes;

public class StudentIdMismatchException extends GlobalException {
    public StudentIdMismatchException() {
        super("Student id mismatch", StatusCodes.NOT_FOUND.getCode());
    }
}
