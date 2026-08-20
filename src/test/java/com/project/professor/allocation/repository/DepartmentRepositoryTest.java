package com.project.professor.allocation.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import com.project.professor.allocation.entity.Department;

@DataJpaTest
@AutoConfigureTestDatabase
@TestPropertySource(properties = {
		"spring.datasource.url=jdbc:h2:mem:department-test",
		"spring.jpa.hibernate.ddl-auto=create-drop"
})
class DepartmentRepositoryTest {

	@Autowired
	private DepartmentRepository departmentRepository;

	@Test
	void deveExecutarFindAllAposSalvarDepartamento() {
		Department department = new Department();
		department.setName("Matemática");
		departmentRepository.save(department);

		List<Department> departments = departmentRepository.findAll();

		assertThat(departments).extracting(item -> item.getName()).contains("Matemática");
	}
}
