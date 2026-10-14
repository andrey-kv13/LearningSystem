package students.dev.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import students.dev.dto.CreateScheduleRequest;
import students.dev.dto.CreateScheduleResponse;
import students.dev.entity.Course;
import students.dev.entity.Group;
import students.dev.entity.Professor;
import students.dev.entity.Schedule;
import students.dev.exception.CourseNotFoundException;
import students.dev.exception.GroupNotFoundException;
import students.dev.exception.ProfessorNotFoundException;
import students.dev.exception.ScheduleNotFoundException;
import students.dev.repository.CourseRepository;
import students.dev.repository.GroupRepository;
import students.dev.repository.ProfessorRepository;
import students.dev.repository.ScheduleRepository;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final GroupRepository groupRepository;
    private final ProfessorRepository professorRepository;
    private final CourseRepository courseRepository;

    private CreateScheduleResponse toResponse(Schedule schedule) {
        Group group = schedule.getGroup();
        Professor professor = schedule.getProfessor();
        Course course = schedule.getCourse();
        return new CreateScheduleResponse(
                schedule.getId(),
                group.getId(),
                group.getName(),
                professor.getId(),
                professor.getFirstName(),
                professor.getLastName(),
                course.getId(),
                course.getName(),
                schedule.getStartEducationDate(),
                schedule.getEndEducationDate()
        );
    }

    public Page<CreateScheduleResponse> findAll(Pageable pageable) {
        return scheduleRepository.findAll(pageable).map(this::toResponse);
    }

    private Schedule findScheduleById(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new ScheduleNotFoundException(id));
    }

    public CreateScheduleResponse getById(Long id) {
        return toResponse(findScheduleById(id));
    }

    public CreateScheduleResponse createSchedule(CreateScheduleRequest request) {
        Schedule schedule = new Schedule();
        applyRequest(schedule, request);
        return toResponse(scheduleRepository.save(schedule));
    }

    public CreateScheduleResponse updateSchedule(Long id, CreateScheduleRequest request) {
        Schedule schedule = findScheduleById(id);
        applyRequest(schedule, request);
        return toResponse(scheduleRepository.save(schedule));
    }

    public void deleteSchedule(Long id) {
        Schedule schedule = findScheduleById(id);
        scheduleRepository.delete(schedule);
    }

    private void applyRequest(Schedule schedule, CreateScheduleRequest request) {
        schedule.setGroup(findGroup(request.groupId()));
        schedule.setProfessor(findProfessor(request.professorId()));
        schedule.setCourse(findCourse(request.courseId()));
        schedule.setStartEducationDate(request.startEducationDate());
        schedule.setEndEducationDate(request.endEducationDate());
    }

    private Group findGroup(Long id) {
        return groupRepository.findById(id).orElseThrow(() -> new GroupNotFoundException(id));
    }

    private Professor findProfessor(Long id) {
        return professorRepository.findById(id).orElseThrow(() -> new ProfessorNotFoundException(id));
    }

    private Course findCourse(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new CourseNotFoundException(id));
    }
}
