package com.project.professor.allocation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.professor.allocation.entity.Course;
import com.project.professor.allocation.repository.CourseRepository;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

	@Mock
	private CourseRepository courseRepository;

	@InjectMocks
	private CourseService courseService;

	@Test
	void deveOrdenarCursosPorNomeAoExecutarFindAll() {
		Course zeta = curso("Zeta");
		Course alpha = curso("alpha");
		when(courseRepository.findAll()).thenReturn(List.of(zeta, alpha));

		assertThat(courseService.findAll()).containsExactly(alpha, zeta);
	}

	private Course curso(String name) {
		Course course = new Course();
		course.setName(name);
		return course;
	}
}
