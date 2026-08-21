package com.project.professor.allocation.dto;

import java.util.List;

public record DashboardReportDTO(
		Long totalProfessors,
		Long totalDepartments,
		Long totalCourses,
		Long totalAllocations,
		List<ProfessorWorkloadDTO> professorWorkloads
) {
}
