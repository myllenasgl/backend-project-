package com.project.professor.allocation.dto;

public record ProfessorResponseDTO(
		Long id,
		String name,
		String cpf,
		DepartmentResponseDTO department
) {
}
