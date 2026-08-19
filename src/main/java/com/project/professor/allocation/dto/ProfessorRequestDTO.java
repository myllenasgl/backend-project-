package com.project.professor.allocation.dto;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProfessorRequestDTO(

		@NotBlank(message = "Nome é obrigatório")
		String name,

		@NotBlank(message = "CPF é obrigatório")
		@CPF(message = "CPF inválido")
		String cpf,

		@NotNull(message = "Departamento é obrigatório")
		Long departmentId

) {
}
