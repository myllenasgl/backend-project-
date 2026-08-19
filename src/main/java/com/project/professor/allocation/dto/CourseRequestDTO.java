package com.project.professor.allocation.dto;

import jakarta.validation.constraints.NotBlank;

public record CourseRequestDTO(

		@NotBlank(message = "Nome é obrigatório")
		String name

) {
}
