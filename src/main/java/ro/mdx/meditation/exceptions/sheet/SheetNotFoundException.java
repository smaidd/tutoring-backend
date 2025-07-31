package ro.mdx.meditation.exceptions.sheet;

import ro.mdx.meditation.exceptions.GlobalException;
import ro.mdx.meditation.exceptions.StatusCodes;

public class SheetNotFoundException extends GlobalException {
    public SheetNotFoundException() {
        super("Student sheet not found", StatusCodes.BAD_REQUEST.getCode());
    }
}
