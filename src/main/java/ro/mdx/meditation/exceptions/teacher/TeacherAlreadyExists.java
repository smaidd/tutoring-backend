package ro.mdx.meditation.exceptions.teacher;

import ro.mdx.meditation.exceptions.GlobalException;
import ro.mdx.meditation.exceptions.StatusCodes;

public class TeacherAlreadyExists extends GlobalException {
    public TeacherAlreadyExists() {
        super("Profesorul deja exista", StatusCodes.BAD_REQUEST.getCode());
    }
}
