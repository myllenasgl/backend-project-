package com.project.professor.allocation.service;

import org.springframework.stereotype.Service;
import com.project.professor.allocation.entity.Course;
import com.project.professor.allocation.entity.Professor;
import com.project.professor.allocation.entity.Allocation;
import com.project.professor.allocation.exception.BusinessRuleException;
import com.project.professor.allocation.exception.ResourceNotFoundException;
import com.project.professor.allocation.exception.ScheduleConflictException;
import com.project.professor.allocation.repository.AllocationRepository;

import java.util.List;

@Service
public class AllocationService {

	private final AllocationRepository allocationRepository;
	private final ProfessorService professorService;
	private final CourseService courseService;

	public AllocationService(
			AllocationRepository allocationRepository,
			ProfessorService professorService,
			CourseService courseService) {

		this.allocationRepository = allocationRepository;
		this.professorService = professorService;
		this.courseService = courseService;
	}

	public List<Allocation> findAll() {
		return allocationRepository.findAll();
	}

	public Allocation findById(Long id) {
		return allocationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Alocação não encontrada com id " + id));
	}

	public List<Allocation> findByProfessor(Long professorId) {
		Professor professor = new Professor();
		professor.setId(professorId);
		return allocationRepository.findByProfessor(professor);
	}

	public List<Allocation> findByCourse(Long courseId) {
		Course course = new Course();
		course.setId(courseId);
		return allocationRepository.findByCourse(course);
	}

	public Allocation save(Allocation allocation) {
		allocation.setId(null);
		return saveInternal(allocation);
	}

	public Allocation update(Allocation allocation) {
		Long id = allocation.getId();

		if (!allocationRepository.existsById(id)) {
			throw new ResourceNotFoundException("Alocação não encontrada com id " + id);
		}

		return saveInternal(allocation);
	}

	public void deleteById(Long id) {
		if (!allocationRepository.existsById(id)) {
			throw new ResourceNotFoundException("Alocação não encontrada com id " + id);
		}
		allocationRepository.deleteById(id);
	}

	private Allocation saveInternal(Allocation allocation) {
		if (!isEndHourGreaterThanStartHour(allocation)) {
			throw new BusinessRuleException("O horário final deve ser maior que o horário inicial.");
		}

		if (allocation.getProfessor() == null || allocation.getProfessor().getId() == null) {
			throw new BusinessRuleException("Professor inválido.");
		}

		if (allocation.getCourse() == null || allocation.getCourse().getId() == null) {
			throw new BusinessRuleException("Curso inválido.");
		}

		Professor professor = professorService.findById(allocation.getProfessor().getId());
		Course course = courseService.findById(allocation.getCourse().getId());

		if (hasCollision(allocation)) {
			throw new ScheduleConflictException("O professor já possui uma alocação nesse horário.");
		}

		allocation = allocationRepository.save(allocation);
		allocation.setCourse(course);
		allocation.setProfessor(professor);

		return allocation;
	}

	private boolean isEndHourGreaterThanStartHour(Allocation allocation) {
		return allocation.getStartHour() != null
				&& allocation.getEndHour() != null
				&& allocation.getEndHour().isAfter(allocation.getStartHour());
	}

	private boolean hasCollision(Allocation allocation) {
		List<Allocation> allocations = allocationRepository.findByProfessor(allocation.getProfessor());

		return allocations.stream()
				.filter(existing -> !existing.getId().equals(allocation.getId()))
				.anyMatch(existing -> hasCollision(existing, allocation));
	}

	private boolean hasCollision(Allocation currentAllocation, Allocation newAllocation) {
		return currentAllocation.getDayOfWeek() == newAllocation.getDayOfWeek()
				&& newAllocation.getStartHour().isBefore(currentAllocation.getEndHour())
				&& currentAllocation.getStartHour().isBefore(newAllocation.getEndHour());
	}
}
