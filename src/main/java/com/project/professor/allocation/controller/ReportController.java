package com.project.professor.allocation.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.professor.allocation.dto.DashboardReportDTO;
import com.project.professor.allocation.dto.ProfessorWorkloadDTO;
import com.project.professor.allocation.service.ReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Reports & Analytics")
@RestController
@RequestMapping(path = "/reports")
public class ReportController {

	private final ReportService reportService;

	public ReportController(ReportService reportService) {
		this.reportService = reportService;
	}

	@Operation(summary = "Get professor weekly workload report")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK")
	})
	@GetMapping(path = "/workload", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ProfessorWorkloadDTO>> getWorkloadReport() {
		return ResponseEntity.ok(reportService.getProfessorWorkloadReport());
	}

	@Operation(summary = "Get overall dashboard summary")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK")
	})
	@GetMapping(path = "/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DashboardReportDTO> getDashboardSummary() {
		return ResponseEntity.ok(reportService.getDashboardSummary());
	}
}
