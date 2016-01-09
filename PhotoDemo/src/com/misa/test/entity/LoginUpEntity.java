package com.misa.test.entity;

import com.misa.framework.entity.EntityBase;

public class LoginUpEntity extends EntityBase 
{
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	private String name;
	private String password;
}
