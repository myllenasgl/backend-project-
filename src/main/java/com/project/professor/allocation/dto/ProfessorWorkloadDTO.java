package com.project.professor.allocation.dto;

import java.util.List;

public record ProfessorWorkloadDTO(
		Long professorId,
		String professorName,
		String departmentName,
		Integer totalAllocations,
		Double totalHoursPerWeek,
		List<String> courses
) {
}
