package com.project.professor.allocation.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "course")
public class Course {
	
	@OneToMany(mappedBy = "course")
	private List<Allocation> allocations;
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(
			name = "name",nullable = false		
		)
	private String name;
}