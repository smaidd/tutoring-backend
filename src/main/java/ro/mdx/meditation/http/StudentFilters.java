package ro.mdx.meditation.http;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@Builder
public class StudentFilters {
    private Optional<String> subject;
}
