package com.app.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="courses")
public class Course {

	@Id
	@NotNull
	@Column(unique = true)
	private Integer class_code;

	
	@NotNull
	@Length(max = 255)
	private String name; 
	
	/**
	 * This property specifies links to videos, pages etc. related to the course
	 */
	@Length(max = 500)
	@Column(columnDefinition = "varchar(500) default ' ' ")
	private String resources;

	private String site;
	private String prof;
	private String yt;
	private String chat;
	private String maps;
	
	
	@Length(max = 1000)
	@Column(columnDefinition = "varchar(1000) default 'Dettagli del corso a breve disponibili' ")
	private String details;

	@NotBlank
	@NotNull
	@Length(max=50)
	private String professor_name;
	
	@NotNull
	@Length(max=4000)
	private String professor_details;
	
	@NotNull
	@PositiveOrZero //Zero not accepted !
	@Min(value = 1)
	@Max(value = 3)
	private Integer year;
	
	@Lob
//	@Type(type = "org.hibernate.type.ImageType")
	private byte[] image;

	
//	There is the need to give a meaning to this this property
	@Length(max=500)
	@Column(columnDefinition = "varchar(500) default 'default'"  )
	private String style;
	
	@OneToMany(orphanRemoval = true,cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<CourseFile> files;
	
	
	public void addFile(CourseFile file) { files.add(file);}
	public void removeFile(CourseFile file) { files.remove(file);}
	
	
}
