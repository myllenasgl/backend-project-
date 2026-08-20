package com.project.professor.allocation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.professor.allocation.entity.Allocation;
import com.project.professor.allocation.repository.AllocationRepository;

@ExtendWith(MockitoExtension.class)
class AllocationServiceTest {

	@Mock
	private AllocationRepository allocationRepository;

	@Mock
	private ProfessorService professorService;

	@Mock
	private CourseService courseService;

	@InjectMocks
	private AllocationService allocationService;

	@Test
	void deveOrdenarAlocacoesPorDiaEHorarioAoExecutarFindAll() {
		Allocation tuesday = alocacao(DayOfWeek.TUESDAY, LocalTime.of(8, 0));
		Allocation mondayLater = alocacao(DayOfWeek.MONDAY, LocalTime.of(10, 0));
		Allocation mondayEarlier = alocacao(DayOfWeek.MONDAY, LocalTime.of(8, 0));
		when(allocationRepository.findAll()).thenReturn(List.of(tuesday, mondayLater, mondayEarlier));

		assertThat(allocationService.findAll()).containsExactly(mondayEarlier, mondayLater, tuesday);
	}

	@Test
	void deveEncontrarAlocacoesPorProfessor() {
		Allocation allocation = alocacao(DayOfWeek.MONDAY, LocalTime.of(8, 0));
		when(allocationRepository.findByProfessor(any())).thenReturn(List.of(allocation));

		assertThat(allocationService.findByProfessor(1L)).containsExactly(allocation);
	}

	@Test
	void deveEncontrarAlocacoesPorCurso() {
		Allocation allocation = alocacao(DayOfWeek.MONDAY, LocalTime.of(8, 0));
		when(allocationRepository.findByCourse(any())).thenReturn(List.of(allocation));

		assertThat(allocationService.findByCourse(1L)).containsExactly(allocation);
	}

	private Allocation alocacao(DayOfWeek day, LocalTime startHour) {
		Allocation allocation = new Allocation();
		allocation.setDayOfWeek(day);
		allocation.setStartHour(startHour);
		return allocation;
	}
}
