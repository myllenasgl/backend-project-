package com.project.professor.allocation.dto;

import jakarta.validation.constraints.NotBlank;

public record DepartmentRequestDTO(

		@NotBlank(message = "Nome é obrigatório")
		String name

) {
}
