package com.project.professor.allocation.mapper;

import com.project.professor.allocation.dto.CourseRequestDTO;
import com.project.professor.allocation.dto.CourseResponseDTO;
import com.project.professor.allocation.entity.Course;

public final class CourseMapper {

	private CourseMapper() {
	}

	public static Course toEntity(CourseRequestDTO dto) {
		Course course = new Course();
		course.setName(dto.name());
		return course;
	}

	public static CourseResponseDTO toResponseDTO(Course course) {
		return new CourseResponseDTO(course.getId(), course.getName());
	}
}
