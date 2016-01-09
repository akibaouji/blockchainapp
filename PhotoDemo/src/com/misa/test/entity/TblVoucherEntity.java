package com.misa.test.entity;

import java.util.Date;

import com.misa.droid.framework.annotation.DbColumn;
import com.misa.droid.framework.annotation.DbTable;
import com.misa.framework.entity.EntityBase;

@DbTable(name="tbl_voucher")
public class TblVoucherEntity extends EntityBase 
{
	public String getHashCode() {
		return hashCode;
	}
	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public byte[] getVdata() {
		return vdata;
	}
	public void setVdata(byte[] vdata) {
		this.vdata = vdata;
	}
	public String getUploadTag() {
		return uploadTag;
	}
	public void setUploadTag(String uploadTag) {
		this.uploadTag = uploadTag;
	}
	
	public String getVerifyTag() {
		return verifyTag;
	}
	public void setVerifyTag(String verifyTag) {
		this.verifyTag = verifyTag;
	}

	@DbColumn(name="hashCode", pk=true)
	private String hashCode;
	
	@DbColumn(name="vdata")
	private byte[] vdata;
	
	@DbColumn(name="uploadtag")
	private String uploadTag;

	@DbColumn(name="updateTime")
	private Date updateTime;
	
	@DbColumn(name="updateUser")
	private String updateUser;
	
	@DbColumn(name="verifyTag")
	private String verifyTag;
}
