package com.app.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import lombok.Data;

@Entity
@Data
public class PostImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String filename;
//  private String fileType;

	@Lob
	private byte[] data;

	
	
	public PostImage(byte[] data) {
		super();
		this.data = data;
	}

	public PostImage( byte[] data, String fileName) {
		super();
		this.filename = fileName;
		this.data = data;
	}

	public PostImage() {
		super();
	}

	

}
