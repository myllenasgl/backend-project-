package com.project.professor.allocation.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import com.project.professor.allocation.entity.Course;

@DataJpaTest
@AutoConfigureTestDatabase
@TestPropertySource(properties = {
		"spring.datasource.url=jdbc:h2:mem:course-test",
		"spring.jpa.hibernate.ddl-auto=create-drop"
})
class CourseRepositoryTest {

	@Autowired
	private CourseRepository courseRepository;

	@Test
	void deveExecutarFindAllAposSalvarCurso() {
		Course course = new Course();
		course.setName("História");
		courseRepository.save(course);

		List<Course> courses = courseRepository.findAll();

		assertThat(courses).extracting(item -> item.getName()).contains("História");
	}
}
