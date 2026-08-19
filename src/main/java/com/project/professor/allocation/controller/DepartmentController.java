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
import org.springframework.web.bind.annotation.RestController;

import com.project.professor.allocation.dto.DepartmentRequestDTO;
import com.project.professor.allocation.dto.DepartmentResponseDTO;
import com.project.professor.allocation.entity.Department;
import com.project.professor.allocation.mapper.DepartmentMapper;
import com.project.professor.allocation.service.DepartmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Departments")
@RestController
@RequestMapping(path = "/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Operation(summary = "Find all departments")
    @ApiResponses({
    	@ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DepartmentResponseDTO>> findAll() {
        List<Department> departments = departmentService.findAll();
        return ResponseEntity.ok(departments.stream().map(DepartmentMapper::toResponseDTO).collect(Collectors.toList()));
    }

    @Operation(summary = "Find a department")
    @ApiResponses({
    	@ApiResponse(responseCode = "200", description = "OK"),
    	@ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    @GetMapping(path = "/{department_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DepartmentResponseDTO> findById(@PathVariable(name = "department_id") Long id) {
        return ResponseEntity.ok(DepartmentMapper.toResponseDTO(departmentService.findById(id)));
    }

    @Operation(summary = "Save a department")
    @ApiResponses({
    	@ApiResponse(responseCode = "201", description = "Created"),
    	@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DepartmentResponseDTO> save(@Valid @RequestBody DepartmentRequestDTO dto) {
        Department department = departmentService.save(DepartmentMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(DepartmentMapper.toResponseDTO(department));
    }

    @Operation(summary = "Update a department")
    @ApiResponses({
    	@ApiResponse(responseCode = "200", description = "OK"),
    	@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
    	@ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    @PutMapping(path = "/{department_id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DepartmentResponseDTO> update(@PathVariable(name = "department_id") Long id,
                                             @Valid @RequestBody DepartmentRequestDTO dto) {
        Department department = DepartmentMapper.toEntity(dto);
        department.setId(id);
        department = departmentService.update(department);
        return ResponseEntity.ok(DepartmentMapper.toResponseDTO(department));
    }

    @Operation(summary = "Delete a department")
    @ApiResponses({
    	@ApiResponse(responseCode = "204", description = "No Content"),
    	@ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    @DeleteMapping(path = "/{department_id}")
    public ResponseEntity<Void> deleteById(@PathVariable(name = "department_id") Long id) {
        departmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
