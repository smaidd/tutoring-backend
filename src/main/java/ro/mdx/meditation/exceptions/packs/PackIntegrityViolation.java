package ro.mdx.meditation.exceptions.packs;

import org.springframework.http.HttpStatus;
import ro.mdx.meditation.exceptions.GlobalException;

public class PackIntegrityViolation extends GlobalException {
    public PackIntegrityViolation() {
        super("Pachetul este folosit de anumiti studenti. Schimbati pachetul studentilor inainte sa stergeti pachetul definitiv.", HttpStatus.BAD_REQUEST.value());
    }
}
