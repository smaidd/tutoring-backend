package ro.mdx.meditation.services;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ro.mdx.meditation.dto.PackDto;
import ro.mdx.meditation.exceptions.packs.PackIntegrityViolation;
import ro.mdx.meditation.mappers.PackMapper;
import ro.mdx.meditation.model.Pack;
import ro.mdx.meditation.repository.PackRepository;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PackService {

    private final PackRepository packRepository;
    private final PackMapper packMapper;


    public List<PackDto> getAllPacks() {
        return packRepository.findAll()
                .stream()
                .map(packMapper::toDto)
                .sorted(Comparator.comparing(PackDto::price).reversed())
                .toList();
    }

    public PackDto savePack(PackDto packDto) {
        Pack pack = packMapper.toEntity(packDto);
        Pack save = packRepository.save(pack);
        return packMapper.toDto(save);
    }

    public void deletePack(UUID packId) {
        try {
            packRepository.deleteById(packId);
        } catch (DataIntegrityViolationException e) {
            throw new PackIntegrityViolation();
        }
    }
}
