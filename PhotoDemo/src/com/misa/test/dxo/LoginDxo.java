package com.misa.test.dxo;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.misa.framework.dxo.JsonDxoBase;
import com.misa.test.entity.LoginUpEntity;
import com.misa.test.entity.UserEntity;

public class LoginDxo extends JsonDxoBase<UserEntity, LoginUpEntity> 
{
	private Type type1;
	private Type type2;
	
	public LoginDxo()
	{
		type1=new TypeToken<UserEntity>(){}.getType();
		type2=new TypeToken<List<UserEntity>>(){}.getType();	
	}
	
	@Override
	protected Type getDownEntityType() {
		// TODO Auto-generated method stub
		return type1;
	}

	@Override
	protected Type getDownEntityListType() {
		// TODO Auto-generated method stub
		return type2;
	}
}
