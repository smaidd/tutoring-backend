package ro.mdx.meditation.services;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ro.mdx.meditation.dto.PackDto;
import ro.mdx.meditation.dto.SubjectDto;
import ro.mdx.meditation.mappers.SubjectMapper;
import ro.mdx.meditation.model.Subject;
import ro.mdx.meditation.repository.SubjectRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;

    public List<SubjectDto> findAll() {
        return subjectRepository.findAll().stream().map(subjectMapper::toDto).toList();
    }

    public SubjectDto saveSubject(SubjectDto subject) {
        Subject save = subjectRepository.save(subjectMapper.toEntity(subject));
        return subjectMapper.toDto(save);
    }
}
