package com.misa.test.entity;

public class AnnounceEntity extends BlockchainUpEntity
{
	private boolean verified;
	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}
}
