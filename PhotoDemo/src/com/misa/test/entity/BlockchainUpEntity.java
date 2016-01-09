package com.misa.test.entity;

import com.misa.framework.entity.EntityBase;

public class BlockchainUpEntity extends EntityBase 
{
	public String getOpType() {
		return opType;
	}
	public void setOpType(String opType) {
		this.opType = opType;
	}
	public String getHashcode() {
		return hashcode;
	}
	public void setHashcode(String hashcode) {
		this.hashcode = hashcode;
	}
	public String getPosId() {
		return posId;
	}
	public void setPosId(String posId) {
		this.posId = posId;
	}
	public String getPhoneId() {
		return phoneId;
	}
	public void setPhoneId(String phoneId) {
		this.phoneId = phoneId;
	}
	public String getKeyId() {
		return keyId;
	}
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
	public String getEncryptedData() {
		return encryptedData;
	}
	public void setEncryptedData(String encryptedData) {
		this.encryptedData = encryptedData;
	}

	private String opType; 	 //������־��upload:�ϴ�
	private String hashcode; //hash
	private String posId; 	 //POS ID
	private String phoneId;  //�ֻ� ID
	private String keyId;    //��Կ ID
	private String encryptedData; //��������
}
