package com.misa.test.dxo;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.misa.framework.dxo.JsonDxoBase;
import com.misa.test.entity.AnnounceEntity;
import com.misa.test.entity.BlockchainUpEntity;

public class AnnounceDxo extends JsonDxoBase<AnnounceEntity, BlockchainUpEntity> 
{
	private Type type1;
	private Type type2;
	
	public AnnounceDxo()
	{
		type1=new TypeToken<AnnounceEntity>(){}.getType();
		type2=new TypeToken<List<AnnounceEntity>>(){}.getType();
	}
	
	@Override
	protected Type getDownEntityListType() {
		return type2;
	}

	@Override
	protected Type getDownEntityType() {
		return type1;
	}

}
