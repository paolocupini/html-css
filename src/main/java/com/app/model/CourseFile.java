package com.app.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "course_files")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseFile {
	
	@javax.persistence.Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Id;
	
	@Length(min = 3)
	@NotNull
	private String filename;
	

	@NotNull
	@Length(min = 3)
	private String type;
	
	@Lob
	private byte[] data;
	
	public CourseFile(@Length(min = 3) @NotNull String filename, @NotNull @Length(min = 3) String type, byte[] data) {
		super();
		this.filename = filename;
		this.type = type;
		this.data = data;
	}
}
