package ro.mdx.meditation.enums;

import lombok.Getter;

@Getter
public enum Packs {
    Z_UNO(4),
    Z_DUO(8),
    Z_EXAMEN(12);

    final int numberOfSessions;

    Packs(int numberOfSessions) {
        this.numberOfSessions = numberOfSessions;
    }
}
