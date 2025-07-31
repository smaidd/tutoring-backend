package ro.mdx.meditation.exceptions;

import lombok.Getter;

@Getter
public enum StatusCodes {
    NOT_FOUND(404),
    FORBIDDEN(403),
    BAD_REQUEST(500);

    private final int code;

    StatusCodes(int code) {
        this.code = code;
    }

}
