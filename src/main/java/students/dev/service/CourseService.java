package students.dev.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import students.dev.dto.CourseGroupSummary;
import students.dev.dto.CourseProfessorSummary;
import students.dev.dto.CreateCourseRequest;
import students.dev.dto.CreateCourseResponse;
import students.dev.entity.Course;
import students.dev.entity.Group;
import students.dev.entity.Professor;
import students.dev.exception.CourseNotFoundException;
import students.dev.exception.GroupNotFoundException;
import students.dev.exception.ProfessorNotFoundException;
import students.dev.repository.CourseRepository;
import students.dev.repository.GroupRepository;
import students.dev.repository.ProfessorRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final ProfessorRepository professorRepository;
    private final GroupRepository groupRepository;

    private CreateCourseResponse toResponse(Course course) {
        Professor professor = course.getProfessor();

        List<CourseGroupSummary> listOfCourseGroupSummary = new ArrayList<>();
        for(Group group: course.getStudy_groups())
            listOfCourseGroupSummary.add(new CourseGroupSummary(group.getId(),  group.getName()));

        CourseProfessorSummary courseProfessorSummary = new CourseProfessorSummary(professor.getId(), professor.getFirstName(), professor.getLastName());

        return new CreateCourseResponse(
                course.getId(),
                course.getName(),
                course.getDescription(),
                courseProfessorSummary,
                listOfCourseGroupSummary

        );
    }

    private Course findCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() ->  new CourseNotFoundException(id));
    }

    public Page<CreateCourseResponse> findAll(Pageable pageable) {
        return courseRepository.findAll(pageable).map(this::toResponse);
    }

    public CreateCourseResponse getCourseById(Long id) {
        return toResponse(findCourseById(id));
    }

    private void applyGroups(Course course, List<Long> groupIds) {
        for (Group group : course.getStudy_groups()) {
            group.getCourses().remove(course);
        }
        course.getStudy_groups().clear();

        for (Long groupId : groupIds) {
            Group group = groupRepository.findById(groupId)
                    .orElseThrow(() -> new GroupNotFoundException(groupId));
            course.getStudy_groups().add(group);
            group.getCourses().add(course);
        }

    }

    @Transactional
    public CreateCourseResponse createCourse(CreateCourseRequest request) {
        Professor professor = professorRepository.findById(request.professorId()).orElseThrow(() ->
                new ProfessorNotFoundException(request.professorId()));
        Course course = new Course();
        course.setName(request.name());
        course.setDescription(request.description());
        course.setProfessor(professor);
        applyGroups(course, request.groupIds());
        return toResponse(courseRepository.save(course));
    }

    @Transactional
    public CreateCourseResponse updateCourse(Long id, CreateCourseRequest request) {
        Professor professor = professorRepository.findById(request.professorId()).orElseThrow(() ->
                new ProfessorNotFoundException(request.professorId()));
        Course course = findCourseById(id);
        course.setName(request.name());
        course.setDescription(request.description());
        course.setProfessor(professor);
        applyGroups(course, request.groupIds());
        return toResponse(courseRepository.save(course));
    }

    public void deleteCourse(Long id) {
        Course course = findCourseById(id);
        courseRepository.delete(course);
    }
}