package com.misa.test.entity;

import com.misa.framework.entity.EntityBase;

public class UserEntity extends EntityBase
{
	private String uid;
	private String name;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
