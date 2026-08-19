package com.project.professor.allocation.mapper;

import com.project.professor.allocation.dto.DepartmentRequestDTO;
import com.project.professor.allocation.dto.DepartmentResponseDTO;
import com.project.professor.allocation.entity.Department;

public final class DepartmentMapper {

	private DepartmentMapper() {
	}

	public static Department toEntity(DepartmentRequestDTO dto) {
		Department department = new Department();
		department.setName(dto.name());
		return department;
	}

	public static DepartmentResponseDTO toResponseDTO(Department department) {
		return new DepartmentResponseDTO(department.getId(), department.getName());
	}
}
