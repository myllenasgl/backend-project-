package com.project.professor.allocation.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import com.project.professor.allocation.entity.Department;
import com.project.professor.allocation.entity.Professor;

@DataJpaTest
@AutoConfigureTestDatabase
@TestPropertySource(properties = {
		"spring.datasource.url=jdbc:h2:mem:professor-test",
		"spring.jpa.hibernate.ddl-auto=create-drop"
})
class ProfessorRepositoryTest {

	@Autowired
	private ProfessorRepository professorRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Test
	void deveExecutarFindByNameContainingIgnoreCase() {
		Department department = salvarDepartamento();
		Professor professor = new Professor();
		professor.setName("Ana Silva");
		professor.setCpf("52998224725");
		professor.setDepartment(department);
		professorRepository.save(professor);

		List<Professor> professors = professorRepository.findByNameContainingIgnoreCase("ana");

		assertThat(professors).extracting(item -> item.getName()).containsExactly("Ana Silva");
	}

	private Department salvarDepartamento() {
		Department department = new Department();
		department.setName("Computer Science");
		return departmentRepository.save(department);
	}
}
