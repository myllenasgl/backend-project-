package com.project.professor.allocation.service;

import java.util.List;
import java.util.Comparator;

import org.springframework.stereotype.Service;

import com.project.professor.allocation.entity.Department;
import com.project.professor.allocation.exception.ResourceNotFoundException;
import com.project.professor.allocation.repository.DepartmentRepository;

@Service
public class DepartmentService {

	private final DepartmentRepository departmentRepository;

	public DepartmentService(DepartmentRepository departmentRepository) {
		this.departmentRepository = departmentRepository;
	}

	public List<Department> findAll() {
		return departmentRepository.findAll().stream()
				.sorted(Comparator.comparing((Department department) -> department.getName(), String.CASE_INSENSITIVE_ORDER))
				.toList();
	}

	public Department findById(Long id) {
		return departmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Departamento não encontrado com id " + id));
	}

	public Department save(Department department) {
		department.setId(null);
		return departmentRepository.save(department);
	}

	public Department update(Department department) {
		Long id = department.getId();

		if (!departmentRepository.existsById(id)) {
			throw new ResourceNotFoundException("Departamento não encontrado com id " + id);
		}

		return departmentRepository.save(department);
	}

	public void deleteById(Long id) {
		if (!departmentRepository.existsById(id)) {
			throw new ResourceNotFoundException("Departamento não encontrado com id " + id);
		}
		departmentRepository.deleteById(id);
	}
}
