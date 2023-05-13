package com.app.dto;

import java.util.List;

import javax.persistence.Lob;

import com.app.model.CourseFile;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseCourseDto {
	
	
	//This must be unique!
	private Integer class_code;
	
	private String name;
	
	/**
	 * This property specifies links to videos, pages etc. related to the course
	 */
	private String resources;
	
	private String site;
	private String prof;
	private String yt;
	private String chat;
	private String maps;
	
	private String details;

	private String professor_name;
	
	private String professor_details;
	
	private Integer year;
	
	private String style;
	
	private List<CourseFile> files;
	
	@Lob
	private byte[] image;
	
	public void addFile(CourseFile file) { files.add(file);}
	public void removeFile(CourseFile file) { files.remove(file);}
	
	
}
