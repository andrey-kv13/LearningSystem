package students.dev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCourseRequest(
        @NotBlank(message = "Имя курса обязательно")
        String name,
        @NotBlank(message = "Описание курса обязательно")
        String description,
        @NotNull(message = "Id профессора обязателен")
        Long professorId
) {}
