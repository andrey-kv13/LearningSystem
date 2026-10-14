package students.dev.dto;

public record CreateCourseResponse(
        Long id,
        String name,
        String description,
        CourseProfessorSummary professor
){}
