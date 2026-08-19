package com.project.professor.allocation.service;

import org.springframework.stereotype.Service;

import com.project.professor.allocation.entity.Department;
import com.project.professor.allocation.entity.Professor;
import com.project.professor.allocation.exception.BusinessRuleException;
import com.project.professor.allocation.exception.ResourceNotFoundException;
import com.project.professor.allocation.repository.DepartmentRepository;
import com.project.professor.allocation.repository.ProfessorRepository;

import java.util.List;

@Service
public class ProfessorService {

	private final ProfessorRepository professorRepository;
	private final DepartmentRepository departmentRepository;

	public ProfessorService(ProfessorRepository professorRepository, DepartmentRepository departmentRepository) {
		this.professorRepository = professorRepository;
		this.departmentRepository = departmentRepository;
	}

	public List<Professor> findAll() {
		return professorRepository.findAll();
	}

	public Professor findById(Long id) {
		return professorRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Professor não encontrado com id " + id));
	}

	public List<Professor> findByName(String partName) {
		return professorRepository.findByNameContainingIgnoreCase(partName);
	}

	public List<Professor> findByDepartment(Long departmentId) {
		Department department = new Department();
		department.setId(departmentId);
		return professorRepository.findByDepartment(department);
	}

	public Professor save(Professor professor) {
		professor.setId(null);
		return saveInternal(professor);
	}

	public Professor update(Professor professor) {
		Long id = professor.getId();

		if (!professorRepository.existsById(id)) {
			throw new ResourceNotFoundException("Professor não encontrado com id " + id);
		}

		return saveInternal(professor);
	}

	public void deleteById(Long id) {
		if (!professorRepository.existsById(id)) {
			throw new ResourceNotFoundException("Professor não encontrado com id " + id);
		}
		professorRepository.deleteById(id);
	}

	private Professor saveInternal(Professor professor) {
		if (professor.getDepartment() == null || professor.getDepartment().getId() == null) {
			throw new BusinessRuleException("Departamento inválido.");
		}

		Department department = departmentRepository.findById(professor.getDepartment().getId())
				.orElseThrow(() -> new BusinessRuleException("Departamento não encontrado."));

		professor.setDepartment(department);
		return professorRepository.save(professor);
	}

}
