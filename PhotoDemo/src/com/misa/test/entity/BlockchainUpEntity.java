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

	private String opType; 	 //操作标志：upload:上传
	private String hashcode; //hash
	private String posId; 	 //POS ID
	private String phoneId;  //手机 ID
	private String keyId;    //密钥 ID
	private String encryptedData; //加密数据
}
