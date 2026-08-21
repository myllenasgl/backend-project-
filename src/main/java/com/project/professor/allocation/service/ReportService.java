package com.project.professor.allocation.service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.project.professor.allocation.dto.DashboardReportDTO;
import com.project.professor.allocation.dto.ProfessorWorkloadDTO;
import com.project.professor.allocation.entity.Allocation;
import com.project.professor.allocation.entity.Professor;
import com.project.professor.allocation.repository.AllocationRepository;
import com.project.professor.allocation.repository.CourseRepository;
import com.project.professor.allocation.repository.DepartmentRepository;
import com.project.professor.allocation.repository.ProfessorRepository;

@Service
public class ReportService {

	private final ProfessorService professorService;
	private final AllocationService allocationService;
	private final DepartmentRepository departmentRepository;
	private final CourseRepository courseRepository;
	private final ProfessorRepository professorRepository;
	private final AllocationRepository allocationRepository;

	public ReportService(
			ProfessorService professorService,
			AllocationService allocationService,
			DepartmentRepository departmentRepository,
			CourseRepository courseRepository,
			ProfessorRepository professorRepository,
			AllocationRepository allocationRepository) {
		this.professorService = professorService;
		this.allocationService = allocationService;
		this.departmentRepository = departmentRepository;
		this.courseRepository = courseRepository;
		this.professorRepository = professorRepository;
		this.allocationRepository = allocationRepository;
	}

	public List<ProfessorWorkloadDTO> getProfessorWorkloadReport() {
		List<Professor> professors = professorService.findAll();

		return professors.stream().map(professor -> {
			List<Allocation> allocations = allocationService.findByProfessor(professor.getId());

			double totalHours = allocations.stream()
					.mapToDouble(alloc -> {
						if (alloc.getStartHour() != null && alloc.getEndHour() != null) {
							return Duration.between(alloc.getStartHour(), alloc.getEndHour()).toMinutes() / 60.0;
						}
						return 0.0;
					})
					.sum();

			List<String> courseNames = allocations.stream()
					.filter(alloc -> alloc.getCourse() != null && alloc.getCourse().getName() != null)
					.map(alloc -> alloc.getCourse().getName())
					.distinct()
					.collect(Collectors.toList());

			String deptName = (professor.getDepartment() != null) ? professor.getDepartment().getName() : "Sem Departamento";

			return new ProfessorWorkloadDTO(
					professor.getId(),
					professor.getName(),
					deptName,
					allocations.size(),
					Math.round(totalHours * 100.0) / 100.0,
					courseNames
			);
		}).collect(Collectors.toList());
	}

	public DashboardReportDTO getDashboardSummary() {
		long totalProfessors = professorRepository.count();
		long totalDepartments = departmentRepository.count();
		long totalCourses = courseRepository.count();
		long totalAllocations = allocationRepository.count();
		List<ProfessorWorkloadDTO> workloads = getProfessorWorkloadReport();

		return new DashboardReportDTO(
				totalProfessors,
				totalDepartments,
				totalCourses,
				totalAllocations,
				workloads
		);
	}

	public String exportProfessorScheduleCsv(Long professorId) {
		Professor professor = professorService.findById(professorId);
		List<Allocation> allocations = allocationService.findByProfessor(professorId);

		StringBuilder sb = new StringBuilder();
		sb.append("Dia da Semana,Horario Inicial,Horario Final,Curso,Departamento\n");

		for (Allocation alloc : allocations) {
			String day = alloc.getDayOfWeek() != null ? alloc.getDayOfWeek().name() : "";
			String start = alloc.getStartHour() != null ? alloc.getStartHour().toString() : "";
			String end = alloc.getEndHour() != null ? alloc.getEndHour().toString() : "";
			String courseName = (alloc.getCourse() != null && alloc.getCourse().getName() != null)
					? alloc.getCourse().getName() : "";
			String deptName = (professor.getDepartment() != null && professor.getDepartment().getName() != null)
					? professor.getDepartment().getName() : "";

			sb.append(String.format("%s,%s,%s,\"%s\",\"%s\"\n", day, start, end, courseName, deptName));
		}

		return sb.toString();
	}

	public String exportProfessorScheduleIcs(Long professorId) {
		Professor professor = professorService.findById(professorId);
		List<Allocation> allocations = allocationService.findByProfessor(professorId);

		StringBuilder sb = new StringBuilder();
		sb.append("BEGIN:VCALENDAR\r\n");
		sb.append("VERSION:2.0\r\n");
		sb.append("PRODID:-//Professor Allocation System//NONSGML v1.0//PT\r\n");
		sb.append("CALSCALE:GREGORIAN\r\n");
		sb.append("METHOD:PUBLISH\r\n");
		sb.append("X-WR-CALNAME:Agenda - Prof. ").append(professor.getName()).append("\r\n");

		int counter = 1;
		for (Allocation alloc : allocations) {
			String courseName = (alloc.getCourse() != null && alloc.getCourse().getName() != null)
					? alloc.getCourse().getName() : "Disciplina";
			String day = alloc.getDayOfWeek() != null ? alloc.getDayOfWeek().name() : "";
			String start = alloc.getStartHour() != null ? alloc.getStartHour().toString().replace(":", "") + "00" : "080000";
			String end = alloc.getEndHour() != null ? alloc.getEndHour().toString().replace(":", "") + "00" : "100000";

			sb.append("BEGIN:VEVENT\r\n");
			sb.append("UID:allocation-").append(alloc.getId() != null ? alloc.getId() : counter++).append("@professor-allocation\r\n");
			sb.append("SUMMARY:").append(courseName).append(" - Prof. ").append(professor.getName()).append("\r\n");
			sb.append("DESCRIPTION:Aula de ").append(courseName).append(" (").append(day).append(")\r\n");
			sb.append("DTSTART:20260824T").append(start).append("\r\n");
			sb.append("DTEND:20260824T").append(end).append("\r\n");
			sb.append("RRULE:FREQ=WEEKLY;BYDAY=").append(toIcalDay(day)).append("\r\n");
			sb.append("END:VEVENT\r\n");
		}

		sb.append("END:VCALENDAR\r\n");
		return sb.toString();
	}

	private String toIcalDay(String dayOfWeek) {
		if (dayOfWeek == null) return "MO";
		return switch (dayOfWeek.toUpperCase()) {
			case "MONDAY" -> "MO";
			case "TUESDAY" -> "TU";
			case "WEDNESDAY" -> "WE";
			case "THURSDAY" -> "TH";
			case "FRIDAY" -> "FR";
			case "SATURDAY" -> "SA";
			case "SUNDAY" -> "SU";
			default -> "MO";
		};
	}
}
