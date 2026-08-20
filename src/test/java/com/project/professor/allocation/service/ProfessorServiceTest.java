package com.project.professor.allocation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.professor.allocation.entity.Professor;
import com.project.professor.allocation.repository.DepartmentRepository;
import com.project.professor.allocation.repository.ProfessorRepository;

@ExtendWith(MockitoExtension.class)
class ProfessorServiceTest {

	@Mock
	private ProfessorRepository professorRepository;

	@Mock
	private DepartmentRepository departmentRepository;

	@InjectMocks
	private ProfessorService professorService;

	@Test
	void deveOrdenarProfessoresPorNomeAoExecutarFindAll() {
		Professor zeta = professor("Zeta");
		Professor alpha = professor("alpha");
		when(professorRepository.findAll()).thenReturn(List.of(zeta, alpha));

		assertThat(professorService.findAll()).containsExactly(alpha, zeta);
	}

	@Test
	void deveEncontrarProfessoresPorNome() {
		Professor professor = professor("Ana Silva");
		when(professorRepository.findByNameContainingIgnoreCase("ana")).thenReturn(List.of(professor));

		assertThat(professorService.findByName("ana")).containsExactly(professor);
	}

	@Test
	void deveEncontrarProfessoresPorDepartamento() {
		Professor professor = professor("Ana Silva");
		when(professorRepository.findByDepartment(any())).thenReturn(List.of(professor));

		assertThat(professorService.findByDepartment(1L)).containsExactly(professor);
	}

	private Professor professor(String name) {
		Professor professor = new Professor();
		professor.setName(name);
		return professor;
	}
}
