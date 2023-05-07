package com.app.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.app.model.PostImage;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ResponsePostDto {
	
	private String title;
	
	private String author;
	
	private String preview;
	
	private String body;
	
	private String category;
	
	// default value
	private PostImage image;
	
	

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date published_at;
	
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date event_date;

	
}
