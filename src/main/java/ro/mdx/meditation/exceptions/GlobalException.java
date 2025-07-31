package ro.mdx.meditation.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GlobalException extends RuntimeException {
    private final int statusCode;

    public GlobalException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
