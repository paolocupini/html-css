package com.app.exception;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	private String field;
	private String message;
	private List<ValidationException> exceptions = new ArrayList<>();
	
	public ValidationException(String field, String message) {
		super();
		this.field = field;
		this.message = message;
	}

	public ValidationException() {
		super();
	}
	
	public void addException(String field, String message) {
		exceptions.add(new ValidationException(field,message));
	}
	public void removeException(String field) {
		for (ValidationException ex : exceptions) {
			if(ex.getField().equals(field)) {
				exceptions.remove(ex);
				break;
			}
		}
	}
	
	
	
	
	
}
