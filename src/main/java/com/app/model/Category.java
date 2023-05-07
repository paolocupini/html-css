package com.app.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "categories")
@Data
public class Category {

	@Id
	private String name;

	public Category(String name) {
		super();
		this.name = name;
	}

	public Category() {
		super();
	}
}
