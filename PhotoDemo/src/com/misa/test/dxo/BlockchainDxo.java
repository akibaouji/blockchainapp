package com.misa.test.dxo;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.misa.framework.dxo.JsonDxoBase;
import com.misa.test.entity.BlockchainDownEntity;
import com.misa.test.entity.BlockchainUpEntity;

public class BlockchainDxo extends JsonDxoBase<BlockchainDownEntity, BlockchainUpEntity>
{
	private Type type1;
	private Type type2;
	
	public BlockchainDxo()
	{
		type1=new TypeToken<BlockchainDownEntity>(){}.getType();
		type2=new TypeToken<List<BlockchainDownEntity>>(){}.getType();
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
