package com.project.professor.allocation.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.professor.allocation.dto.ProfessorRequestDTO;
import com.project.professor.allocation.dto.ProfessorResponseDTO;
import com.project.professor.allocation.entity.Professor;
import com.project.professor.allocation.mapper.ProfessorMapper;
import com.project.professor.allocation.service.ProfessorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Professors")
@RestController
@RequestMapping(path = "/professors")
public class ProfessorController {

	private final ProfessorService professorService;

	public ProfessorController(ProfessorService professorService) {
		this.professorService = professorService;
	}

	@Operation(summary = "Find all professors")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK")
	})
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ProfessorResponseDTO>> findAll(@RequestParam(name = "name", required = false) String name) {
		List<Professor> professors = name == null ? professorService.findAll() : professorService.findByName(name);
		return ResponseEntity.ok(professors.stream().map(ProfessorMapper::toResponseDTO).collect(Collectors.toList()));
	}

	@Operation(summary = "Find a professor")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK"),
		@ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
	})
	@GetMapping(path = "/{professor_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProfessorResponseDTO> findById(@PathVariable(name = "professor_id") Long id) {
		return ResponseEntity.ok(ProfessorMapper.toResponseDTO(professorService.findById(id)));
	}

	@Operation(summary = "Delete a professor")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "No Content"),
		@ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
	})
	@DeleteMapping(path = "/{professor_id}")
	public ResponseEntity<Void> deleteById(@PathVariable(name = "professor_id") Long id) {
		professorService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Find professors by department")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK")
	})
	@GetMapping(path = "/department/{department_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ProfessorResponseDTO>> findByDepartment(@PathVariable(name = "department_id") Long id) {
		List<Professor> professors = professorService.findByDepartment(id);
		return ResponseEntity.ok(professors.stream().map(ProfessorMapper::toResponseDTO).collect(Collectors.toList()));
	}

	@Operation(summary = "Save a professor")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Created"),
		@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
	})
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProfessorResponseDTO> save(@Valid @RequestBody ProfessorRequestDTO dto) {
		Professor professor = professorService.save(ProfessorMapper.toEntity(dto));
		return ResponseEntity.status(HttpStatus.CREATED).body(ProfessorMapper.toResponseDTO(professor));
	}

	@Operation(summary = "Update a professor")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK"),
		@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
		@ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
	})
	@PutMapping(path = "/{professor_id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProfessorResponseDTO> update(@PathVariable(name = "professor_id") Long id,
			@Valid @RequestBody ProfessorRequestDTO dto) {
		Professor professor = ProfessorMapper.toEntity(dto);
		professor.setId(id);
		professor = professorService.update(professor);
		return ResponseEntity.ok(ProfessorMapper.toResponseDTO(professor));
	}

}
