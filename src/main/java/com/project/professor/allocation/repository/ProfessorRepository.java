package com.project.professor.allocation.repository;

import com.project.professor.allocation.entity.Department;
import com.project.professor.allocation.entity.Professor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
	List<Professor> findByDepartment(Department dep);
	
	List<Professor> findByNameContainingIgnoreCase(String name);
}