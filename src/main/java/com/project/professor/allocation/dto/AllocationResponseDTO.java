package com.project.professor.allocation.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record AllocationResponseDTO(
		Long id,
		DayOfWeek dayOfWeek,
		LocalTime startHour,
		LocalTime endHour,
		ProfessorResponseDTO professor,
		CourseResponseDTO course
) {
}
