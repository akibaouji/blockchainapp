package com.misa.test.service;

import java.util.Date;
import java.util.List;
import com.misa.framework.entity.PageSplitDownEntity;
import com.misa.framework.entity.PageSplitUpEntityBase;
import com.misa.framework.util.SecurityUtility;
import com.misa.test.app.PhotoDemoApplication;
import com.misa.test.entity.TblVoucherEntity;

public class VoucherService 
{
	private final static String select_preffix = "select hashcode,vdata,uploadtag,verifyTag,updatetime,updateuser ";
	public String createFakeVoucher()
	{
		String hc = SecurityUtility.randomString(32);
		TblVoucherEntity entity = new TblVoucherEntity();
		entity.setHashCode(hc);
		entity.setVdata(new byte[]{(byte)0x03,(byte)0x04});
		entity.setUpdateTime(new Date());
		entity.setUpdateUser("sptuser");
		
		PhotoDemoApplication.getDbHelper().insert(entity);
		
		return hc;
	}
	
	public TblVoucherEntity selectByHashCode(String hc)
	{
		return PhotoDemoApplication.getDbHelper().selectOne(select_preffix
				+ " from tbl_voucher where hashcode =? ",
				new String[]{hc}, TblVoucherEntity.class);
	}
	
	public List<TblVoucherEntity> selectAll()
	{
		return PhotoDemoApplication.getDbHelper().selectList(select_preffix
				+ " from tbl_voucher order by updatetime desc ", new String[]{}, TblVoucherEntity.class);
	}
	
	
	public PageSplitDownEntity<TblVoucherEntity> selectList(PageSplitUpEntityBase upEntity)
	{
		int currentPageNumber = upEntity.getCurrentPageNumber();
		int numberPerPage = upEntity.getNumberPerPage();
		
		String sql = select_preffix
				+ " from tbl_voucher order by updatetime desc " + getPageLimitQueryClause(numberPerPage, currentPageNumber);
		
		List<TblVoucherEntity> dataList = PhotoDemoApplication.getDbHelper().selectList(sql, new String[]{}, TblVoucherEntity.class);
		
		
		PageSplitDownEntity<TblVoucherEntity> rtn = new PageSplitDownEntity<TblVoucherEntity>();
		rtn.setCurrentPageNumber(currentPageNumber);
		rtn.setNumberPerPage(numberPerPage);
		rtn.setTotalCount(dataList.size());
		rtn.setDataList(dataList);
		return rtn;
	}
	
	public boolean save(TblVoucherEntity entity)
	{
		return PhotoDemoApplication.getDbHelper().insert(entity);
	}
	
	public void updateUploadflag(String hashcode, String flag)
	{
		PhotoDemoApplication.getDbHelper().execute("update tbl_voucher set uploadtag = ? where hashcode = ? ",
				new Object[]{flag, hashcode});
	}
	
	public void updateVerifyTag(String hashcode,String verifyTag)
	{
		PhotoDemoApplication.getDbHelper().execute("update tbl_voucher set verifyTag = ? where hashcode = ? ",
				new Object[]{verifyTag, hashcode});
	}
	
	public boolean update(TblVoucherEntity entity)
	{
		return PhotoDemoApplication.getDbHelper().update(entity);
	}
	
	public boolean delectByHashcode(String hashcode)
	{
		return PhotoDemoApplication.getDbHelper().execute("delete from tbl_voucher where hashcode = ?", new String[]{hashcode});
	}
	
	public void deleteAll()
	{
		PhotoDemoApplication.getDbHelper().execute("delete from tbl_voucher", new String[]{});
	}

	private String getPageLimitQueryClause(int numberPerPage, int currentPageNumber)
	{
		int from;
		int to;
		if (currentPageNumber == 0)
		{
			from = 0;
			to = numberPerPage;
		}
		else
		{
			from= numberPerPage * (currentPageNumber - 1);
			to = numberPerPage;
		}
		
		return " limit " + String.valueOf(from) + ", " + String.valueOf(to) + " ";
	}
}
