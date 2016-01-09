package com.misa.test.entity;

import com.misa.framework.entity.EntityBase;

public class KeyWrapperEntity extends EntityBase 
{
	public String getModulus() {
		return modulus;
	}
	public void setModulus(String modulus) {
		this.modulus = modulus;
	}
	public String getExponent() {
		return exponent;
	}
	public void setExponent(String exponent) {
		this.exponent = exponent;
	}
	private String modulus;
	private String exponent;
}
