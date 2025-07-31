package ro.mdx.meditation.repository.specs;

import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class SpecsUtil {

    private SpecsUtil() {
    }

    public static <T> Specification<T> combineSpecifications(List<Specification<T>> specs) {
        if (specs == null || specs.isEmpty()) {
            return null;
        }

        Specification<T> combined = specs.getFirst();
        for (int i = 1; i < specs.size(); i++) {
            combined = combined.and(specs.get(i));
        }

        return combined;
    }
}
