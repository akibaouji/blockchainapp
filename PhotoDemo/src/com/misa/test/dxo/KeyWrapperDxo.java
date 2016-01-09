package com.misa.test.dxo;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.misa.framework.dxo.JsonDxoBase;
import com.misa.test.entity.BlockchainUpEntity;
import com.misa.test.entity.KeyWrapperEntity;

public class KeyWrapperDxo extends JsonDxoBase<KeyWrapperEntity, BlockchainUpEntity>
{
	private Type type1;
	private Type type2;
	
	public KeyWrapperDxo()
	{
		type1=new TypeToken<KeyWrapperEntity>(){}.getType();
		type2=new TypeToken<List<KeyWrapperEntity>>(){}.getType();
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
