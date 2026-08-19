package com.project.professor.allocation.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.project.professor.allocation.dto.AllocationRequestDTO;
import com.project.professor.allocation.dto.AllocationResponseDTO;
import com.project.professor.allocation.entity.Allocation;
import com.project.professor.allocation.mapper.AllocationMapper;
import com.project.professor.allocation.service.AllocationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Allocations")
@RestController
@RequestMapping(path = "/allocations")
public class AllocationController {

    private final AllocationService allocationService;

    public AllocationController(AllocationService allocationService) {
        this.allocationService = allocationService;
    }

    @Operation(summary = "Find all allocations")
    @ApiResponses({
    	@ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AllocationResponseDTO>> findAll() {
        List<Allocation> allocations = allocationService.findAll();
        return ResponseEntity.ok(allocations.stream().map(AllocationMapper::toResponseDTO).collect(Collectors.toList()));
    }

    @Operation(summary = "Find an allocation")
    @ApiResponses({
    	@ApiResponse(responseCode = "200", description = "OK"),
    	@ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    @GetMapping(path = "/{allocation_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AllocationResponseDTO> findById(@PathVariable(name = "allocation_id") Long id) {
        return ResponseEntity.ok(AllocationMapper.toResponseDTO(allocationService.findById(id)));
    }

    @Operation(summary = "Find allocations by professor")
    @ApiResponses({
    	@ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping(path = "/professor/{professor_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AllocationResponseDTO>> findByProfessor(@PathVariable(name = "professor_id") Long id) {
        List<Allocation> allocations = allocationService.findByProfessor(id);
        return ResponseEntity.ok(allocations.stream().map(AllocationMapper::toResponseDTO).collect(Collectors.toList()));
    }

    @Operation(summary = "Find allocations by course")
    @ApiResponses({
    	@ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping(path = "/course/{course_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AllocationResponseDTO>> findByCourse(@PathVariable(name = "course_id") Long id) {
        List<Allocation> allocations = allocationService.findByCourse(id);
        return ResponseEntity.ok(allocations.stream().map(AllocationMapper::toResponseDTO).collect(Collectors.toList()));
    }

    @Operation(summary = "Save an allocation")
    @ApiResponses({
    	@ApiResponse(responseCode = "201", description = "Created"),
    	@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
    	@ApiResponse(responseCode = "409", description = "Conflict", content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AllocationResponseDTO> save(@Valid @RequestBody AllocationRequestDTO dto) {
        Allocation allocation = allocationService.save(AllocationMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(AllocationMapper.toResponseDTO(allocation));
    }

    @Operation(summary = "Update an allocation")
    @ApiResponses({
    	@ApiResponse(responseCode = "200", description = "OK"),
    	@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
    	@ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
    	@ApiResponse(responseCode = "409", description = "Conflict", content = @Content)
    })
    @PutMapping(path = "/{allocation_id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AllocationResponseDTO> update(@PathVariable(name = "allocation_id") Long id,
                                             @Valid @RequestBody AllocationRequestDTO dto) {
        Allocation allocation = AllocationMapper.toEntity(dto);
        allocation.setId(id);
        allocation = allocationService.update(allocation);
        return ResponseEntity.ok(AllocationMapper.toResponseDTO(allocation));
    }

    @Operation(summary = "Delete an allocation")
    @ApiResponses({
    	@ApiResponse(responseCode = "204", description = "No Content"),
    	@ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    @DeleteMapping(path = "/{allocation_id}")
    public ResponseEntity<Void> deleteById(@PathVariable(name = "allocation_id") Long id) {
        allocationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
