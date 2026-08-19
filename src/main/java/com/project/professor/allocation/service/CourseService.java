package com.project.professor.allocation.service;

import org.springframework.stereotype.Service;
import com.project.professor.allocation.entity.Course;
import com.project.professor.allocation.exception.ResourceNotFoundException;
import com.project.professor.allocation.repository.CourseRepository;

import java.util.List;

@Service
public class CourseService {

	private final CourseRepository courseRepository;

	public CourseService(CourseRepository courseRepository) {
		this.courseRepository = courseRepository;
	}

	public List<Course> findAll() {
		return courseRepository.findAll();
	}

	public Course findById(Long id) {
		return courseRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado com id " + id));
	}

	public Course save(Course course) {
		course.setId(null);
		return courseRepository.save(course);
	}

	public Course update(Course course) {
		Long id = course.getId();

		if (!courseRepository.existsById(id)) {
			throw new ResourceNotFoundException("Curso não encontrado com id " + id);
		}

		return courseRepository.save(course);
	}

	public void deleteById(Long id) {
		if (!courseRepository.existsById(id)) {
			throw new ResourceNotFoundException("Curso não encontrado com id " + id);
		}
		courseRepository.deleteById(id);
	}

}
