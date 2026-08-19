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

import com.project.professor.allocation.dto.CourseRequestDTO;
import com.project.professor.allocation.dto.CourseResponseDTO;
import com.project.professor.allocation.entity.Course;
import com.project.professor.allocation.mapper.CourseMapper;
import com.project.professor.allocation.service.CourseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Courses")
@RestController
@RequestMapping(path = "/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @Operation(summary = "Find all courses")
    @ApiResponses({
    	@ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CourseResponseDTO>> findAll() {
        List<Course> courses = courseService.findAll();
        return ResponseEntity.ok(courses.stream().map(CourseMapper::toResponseDTO).collect(Collectors.toList()));
    }

    @Operation(summary = "Find a course")
    @ApiResponses({
    	@ApiResponse(responseCode = "200", description = "OK"),
    	@ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    @GetMapping(path = "/{course_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseResponseDTO> findById(@PathVariable(name = "course_id") Long id) {
        return ResponseEntity.ok(CourseMapper.toResponseDTO(courseService.findById(id)));
    }

    @Operation(summary = "Save a course")
    @ApiResponses({
    	@ApiResponse(responseCode = "201", description = "Created"),
    	@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseResponseDTO> save(@Valid @RequestBody CourseRequestDTO dto) {
        Course course = courseService.save(CourseMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(CourseMapper.toResponseDTO(course));
    }

    @Operation(summary = "Update a course")
    @ApiResponses({
    	@ApiResponse(responseCode = "200", description = "OK"),
    	@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
    	@ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    @PutMapping(path = "/{course_id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseResponseDTO> update(@PathVariable(name = "course_id") Long id,
                                         @Valid @RequestBody CourseRequestDTO dto) {
        Course course = CourseMapper.toEntity(dto);
        course.setId(id);
        course = courseService.update(course);
        return ResponseEntity.ok(CourseMapper.toResponseDTO(course));
    }

    @Operation(summary = "Delete a course")
    @ApiResponses({
    	@ApiResponse(responseCode = "204", description = "No Content"),
    	@ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    @DeleteMapping(path = "/{course_id}")
    public ResponseEntity<Void> deleteById(@PathVariable(name = "course_id") Long id) {
        courseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
