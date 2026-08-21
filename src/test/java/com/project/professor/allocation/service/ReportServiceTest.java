package com.project.professor.allocation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.professor.allocation.dto.DashboardReportDTO;
import com.project.professor.allocation.dto.ProfessorWorkloadDTO;
import com.project.professor.allocation.entity.Allocation;
import com.project.professor.allocation.entity.Course;
import com.project.professor.allocation.entity.Department;
import com.project.professor.allocation.entity.Professor;
import com.project.professor.allocation.repository.AllocationRepository;
import com.project.professor.allocation.repository.CourseRepository;
import com.project.professor.allocation.repository.DepartmentRepository;
import com.project.professor.allocation.repository.ProfessorRepository;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

	@Mock
	private ProfessorService professorService;

	@Mock
	private AllocationService allocationService;

	@Mock
	private DepartmentRepository departmentRepository;

	@Mock
	private CourseRepository courseRepository;

	@Mock
	private ProfessorRepository professorRepository;

	@Mock
	private AllocationRepository allocationRepository;

	@InjectMocks
	private ReportService reportService;

	@Test
	void deveCalcularCargaHorariaDoProfessor() {
		Department dept = new Department();
		dept.setName("Computacao");

		Professor prof = new Professor();
		prof.setId(1L);
		prof.setName("Carlos Silva");
		prof.setDepartment(dept);

		Course course = new Course();
		course.setName("Java Advanced");

		Allocation alloc = new Allocation();
		alloc.setId(10L);
		alloc.setDayOfWeek(DayOfWeek.MONDAY);
		alloc.setStartHour(LocalTime.of(8, 0));
		alloc.setEndHour(LocalTime.of(12, 0));
		alloc.setCourse(course);
		alloc.setProfessor(prof);

		when(professorService.findAll()).thenReturn(List.of(prof));
		when(allocationService.findByProfessor(1L)).thenReturn(List.of(alloc));

		List<ProfessorWorkloadDTO> report = reportService.getProfessorWorkloadReport();

		assertThat(report).hasSize(1);
		assertThat(report.get(0).professorName()).isEqualTo("Carlos Silva");
		assertThat(report.get(0).totalHoursPerWeek()).isEqualTo(4.0);
		assertThat(report.get(0).courses()).containsExactly("Java Advanced");
	}

	@Test
	void deveGerarResumoDashboard() {
		when(professorRepository.count()).thenReturn(5L);
		when(departmentRepository.count()).thenReturn(2L);
		when(courseRepository.count()).thenReturn(8L);
		when(allocationRepository.count()).thenReturn(12L);
		when(professorService.findAll()).thenReturn(List.of());

		DashboardReportDTO dashboard = reportService.getDashboardSummary();

		assertThat(dashboard.totalProfessors()).isEqualTo(5L);
		assertThat(dashboard.totalDepartments()).isEqualTo(2L);
		assertThat(dashboard.totalCourses()).isEqualTo(8L);
		assertThat(dashboard.totalAllocations()).isEqualTo(12L);
	}

	@Test
	void deveExportarAgendaCsvEIcs() {
		Department dept = new Department();
		dept.setName("Exatas");

		Professor prof = new Professor();
		prof.setId(1L);
		prof.setName("Ana Silva");
		prof.setDepartment(dept);

		Course course = new Course();
		course.setName("Calculo I");

		Allocation alloc = new Allocation();
		alloc.setId(1L);
		alloc.setDayOfWeek(DayOfWeek.TUESDAY);
		alloc.setStartHour(LocalTime.of(14, 0));
		alloc.setEndHour(LocalTime.of(16, 0));
		alloc.setCourse(course);
		alloc.setProfessor(prof);

		when(professorService.findById(1L)).thenReturn(prof);
		when(allocationService.findByProfessor(1L)).thenReturn(List.of(alloc));

		String csv = reportService.exportProfessorScheduleCsv(1L);
		assertThat(csv).contains("TUESDAY,14:00,16:00,\"Calculo I\",\"Exatas\"");

		String ics = reportService.exportProfessorScheduleIcs(1L);
		assertThat(ics).contains("BEGIN:VCALENDAR").contains("Calculo I").contains("RRULE:FREQ=WEEKLY;BYDAY=TU");
	}
}
