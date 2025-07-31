package ro.mdx.meditation.exceptions.auth;

import ro.mdx.meditation.exceptions.GlobalException;
import ro.mdx.meditation.exceptions.StatusCodes;

public class BadCredentialException extends GlobalException {
    public BadCredentialException(String message) {
        super(message, StatusCodes.BAD_REQUEST.getCode());
    }
}
