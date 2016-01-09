package com.misa.test.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class TestAdapterBase<T> extends BaseAdapter
{
	protected LayoutInflater layoutInflater;
	protected Context context;
	protected List<T> dataList;
	
	public TestAdapterBase(Context context, List<T> dataList)
	{
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
		this.dataList = dataList;
	}
	
	@Override
	public int getCount() 
	{
		return dataList == null ? 0 : dataList.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return this.dataList.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		return null;
	}

}
