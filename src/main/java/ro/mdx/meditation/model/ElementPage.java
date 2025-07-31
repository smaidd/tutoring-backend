package ro.mdx.meditation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ElementPage<O> {
    private List<O> content;
    private int totalPages;
    private long totalElements;
}
