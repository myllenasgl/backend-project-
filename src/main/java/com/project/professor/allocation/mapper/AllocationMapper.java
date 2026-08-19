package com.project.professor.allocation.mapper;

import com.project.professor.allocation.dto.AllocationRequestDTO;
import com.project.professor.allocation.dto.AllocationResponseDTO;
import com.project.professor.allocation.entity.Allocation;

public final class AllocationMapper {

	private AllocationMapper() {
	}

	public static Allocation toEntity(AllocationRequestDTO dto) {
		Allocation allocation = new Allocation();
		allocation.setDayOfWeek(dto.dayOfWeek());
		allocation.setStartHour(dto.startHour());
		allocation.setEndHour(dto.endHour());
		allocation.setProfessorId(dto.professorId());
		allocation.setCourseId(dto.courseId());
		return allocation;
	}

	public static AllocationResponseDTO toResponseDTO(Allocation allocation) {
		return new AllocationResponseDTO(
				allocation.getId(),
				allocation.getDayOfWeek(),
				allocation.getStartHour(),
				allocation.getEndHour(),
				allocation.getProfessor() == null ? null : ProfessorMapper.toResponseDTO(allocation.getProfessor()),
				allocation.getCourse() == null ? null : CourseMapper.toResponseDTO(allocation.getCourse()));
	}
}
