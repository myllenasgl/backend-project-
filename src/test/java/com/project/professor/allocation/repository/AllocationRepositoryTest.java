package com.project.professor.allocation.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import com.project.professor.allocation.entity.Allocation;
import com.project.professor.allocation.entity.Course;
import com.project.professor.allocation.entity.Department;
import com.project.professor.allocation.entity.Professor;

@DataJpaTest
@AutoConfigureTestDatabase
@TestPropertySource(properties = {
		"spring.datasource.url=jdbc:h2:mem:allocation-test",
		"spring.jpa.hibernate.ddl-auto=create-drop"
})
class AllocationRepositoryTest {

	@Autowired
	private AllocationRepository allocationRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private ProfessorRepository professorRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Test
	void deveExecutarFindByProfessor() {
		Professor professor = salvarProfessor();
		Course course = new Course();
		course.setName("Algorithms");
		course = courseRepository.save(course);
		Allocation allocation = new Allocation();
		allocation.setDayOfWeek(DayOfWeek.MONDAY);
		allocation.setStartHour(LocalTime.of(8, 0));
		allocation.setEndHour(LocalTime.of(10, 0));
		allocation.setProfessor(professor);
		allocation.setCourse(course);
		allocationRepository.save(allocation);

		List<Allocation> allocations = allocationRepository.findByProfessor(professor);

		assertThat(allocations).hasSize(1);
		assertThat(allocations.get(0).getStartHour()).isEqualTo(LocalTime.of(8, 0));
	}

	private Professor salvarProfessor() {
		Department department = new Department();
		department.setName("Computer Science");
		department = departmentRepository.save(department);
		Professor professor = new Professor();
		professor.setName("Ana Silva");
		professor.setCpf("52998224725");
		professor.setDepartment(department);
		return professorRepository.save(professor);
	}
}
