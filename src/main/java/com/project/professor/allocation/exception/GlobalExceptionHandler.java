package com.project.professor.allocation.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
		List<String> details = ex.getBindingResult().getFieldErrors().stream()
				.map(this::formatFieldError)
				.collect(Collectors.toList());

		ApiError error = new ApiError(
				HttpStatus.BAD_REQUEST.value(),
				"Dados inválidos",
				"Um ou mais campos estão inválidos.",
				details);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex) {
		ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), "Recurso não encontrado", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(ScheduleConflictException.class)
	public ResponseEntity<ApiError> handleScheduleConflict(ScheduleConflictException ex) {
		ApiError error = new ApiError(HttpStatus.CONFLICT.value(), "Conflito de horário", ex.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}

	@ExceptionHandler(BusinessRuleException.class)
	public ResponseEntity<ApiError> handleBusinessRule(BusinessRuleException ex) {
		ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(), "Requisição inválida", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleGeneric(Exception ex) {
		log.error("Erro inesperado não tratado", ex);
		ApiError error = new ApiError(
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Erro interno",
				"Ocorreu um erro inesperado no servidor.");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}

	private String formatFieldError(FieldError fieldError) {
		return fieldError.getField() + ": " + fieldError.getDefaultMessage();
	}
}
