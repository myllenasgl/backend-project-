package com.project.professor.allocation.mapper;

import com.project.professor.allocation.dto.ProfessorRequestDTO;
import com.project.professor.allocation.dto.ProfessorResponseDTO;
import com.project.professor.allocation.entity.Professor;

public final class ProfessorMapper {

	private ProfessorMapper() {
	}

	public static Professor toEntity(ProfessorRequestDTO dto) {
		Professor professor = new Professor();
		professor.setName(dto.name());
		professor.setCpf(dto.cpf());
		professor.setDepartmentId(dto.departmentId());
		return professor;
	}

	public static ProfessorResponseDTO toResponseDTO(Professor professor) {
		return new ProfessorResponseDTO(
				professor.getId(),
				professor.getName(),
				professor.getCpf(),
				professor.getDepartment() == null ? null : DepartmentMapper.toResponseDTO(professor.getDepartment()));
	}
}
