package ro.mdx.meditation.mappers;

public interface EntityMapper<E, D> {
    D toDto(E entity);
    E toEntity(D dto);
}
