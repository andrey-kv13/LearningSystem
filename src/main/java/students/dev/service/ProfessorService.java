package students.dev.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import students.dev.dto.CreateProfessorRequest;
import students.dev.dto.CreateProfessorResponse;
import students.dev.entity.Professor;
import students.dev.exception.ProfessorNotFoundException;
import students.dev.repository.ProfessorRepository;

@Service
@RequiredArgsConstructor
public class ProfessorService {
    private final ProfessorRepository professorRepository;

    private CreateProfessorResponse toResponse(Professor professor) {
        return new CreateProfessorResponse(
                professor.getId(),
                professor.getFirstName(),
                professor.getLastName()
        );
    }

    public Page<CreateProfessorResponse> findAll(Pageable pageable) {
        return professorRepository.findAll(pageable).map(this::toResponse);
    }

    public CreateProfessorResponse findProfessorByName(String firstName, String lastName) {
        Professor professor = professorRepository
                .findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new ProfessorNotFoundException(firstName, lastName));
        return toResponse(professor);
    }

    private Professor findProfessorById(Long id) {
        return professorRepository.findById(id).orElseThrow(() -> new ProfessorNotFoundException(id));
    }

    public CreateProfessorResponse getProfessorById(Long id) {

        return toResponse(findProfessorById(id));
    }

    public CreateProfessorResponse createProfessor(CreateProfessorRequest request) {
        Professor professor = new Professor();
        professor.setFirstName(request.firstName());
        professor.setLastName(request.lastName());
        return toResponse(professorRepository.save(professor));
    }

    public CreateProfessorResponse update(Long id, CreateProfessorRequest request) {
        Professor professor = findProfessorById(id);
        professor.setFirstName(request.firstName());
        professor.setLastName(request.lastName());
        return toResponse(professorRepository.save(professor));
    }

    public void deleteProfessor(Long id) {
        Professor professor = findProfessorById(id);
        professorRepository.delete(professor);
    }
}
