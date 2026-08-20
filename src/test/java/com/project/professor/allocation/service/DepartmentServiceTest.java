package com.project.professor.allocation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.professor.allocation.entity.Department;
import com.project.professor.allocation.repository.DepartmentRepository;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

	@Mock
	private DepartmentRepository departmentRepository;

	@InjectMocks
	private DepartmentService departmentService;

	@Test
	void deveOrdenarDepartamentosPorNomeAoExecutarFindAll() {
		Department zeta = departamento("Zeta");
		Department alpha = departamento("alpha");
		when(departmentRepository.findAll()).thenReturn(List.of(zeta, alpha));

		assertThat(departmentService.findAll()).containsExactly(alpha, zeta);
	}

	private Department departamento(String name) {
		Department department = new Department();
		department.setName(name);
		return department;
	}
}
