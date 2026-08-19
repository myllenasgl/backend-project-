package com.project.professor.allocation.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

public record AllocationRequestDTO(

		@NotNull(message = "Dia da semana é obrigatório")
		DayOfWeek dayOfWeek,

		@NotNull(message = "Horário inicial é obrigatório")
		LocalTime startHour,

		@NotNull(message = "Horário final é obrigatório")
		LocalTime endHour,

		@NotNull(message = "Professor é obrigatório")
		Long professorId,

		@NotNull(message = "Curso é obrigatório")
		Long courseId

) {
}
