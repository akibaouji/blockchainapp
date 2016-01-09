package com.misa.test.adapter;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.misa.droid.framework.common.ObjectFactory;
import com.misa.droid.framework.img.ImageTool;
import com.misa.framework.util.CollectionUtility;
import com.misa.framework.util.SecurityUtility;
import com.misa.test.entity.TblVoucherEntity;
import com.misa.test.service.BlockchainService;
import com.misa.test.service.VoucherService;
import com.unionpay.photodemo.ImagePreviewActivity;
import com.unionpay.photodemo.MainActivity;
import com.unionpay.photodemo.R;

public class VoucherAdapter extends TestAdapterBase<TblVoucherEntity>
{
	private BlockchainService blockchainService;
	private VoucherService voucherService;
	private List<TblVoucherEntity> dataList;
	private Context context;
	
	public VoucherAdapter(Context context, List<TblVoucherEntity> dataList) 
	{
		super(context, dataList);
		this.dataList = dataList;
		this.context = context;
		//生成服务对象
		voucherService = (VoucherService)ObjectFactory.getObject("com.misa.test.service.VoucherService");
		blockchainService = (BlockchainService)ObjectFactory.getObject("com.misa.test.service.BlockchainService");
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder viewHolder = null;
		
		if(convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.list_voucher, null);
			viewHolder.imgVoucher = (ImageView)convertView.findViewById(R.id.img_voucher);
			viewHolder.txtHashcode = (TextView)convertView.findViewById(R.id.txt_hashcode);
			viewHolder.btnUpload = (Button)convertView.findViewById(R.id.btn_upload);
			viewHolder.btDelect = (Button)convertView.findViewById(R.id.btn_delect);
			viewHolder.txtVerifyTag = (TextView)convertView.findViewById(R.id.txt_verifyTag);

			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		final TblVoucherEntity entity = (TblVoucherEntity)getItem(position);
		
		if (entity != null)
		{
			Bitmap bitmap = ImageTool.bytes2Bimap(entity.getVdata());
			viewHolder.imgVoucher.setImageBitmap(bitmap);
			viewHolder.txtHashcode.setText(entity.getHashCode());
			
			if (entity.getUploadTag().equals("0"))
			{
				viewHolder.btnUpload.setEnabled(true);
			}
			else
			{
				viewHolder.btnUpload.setEnabled(false);
			}
			
			if(entity.getVerifyTag().equals("1"))
			{
				viewHolder.txtVerifyTag.setText("已验证");
			}
			
			final View convertViewF = convertView;
			final Button btnUploadF = viewHolder.btnUpload;
			final Button btnDelect = viewHolder.btDelect;
			final ImageView imgVoucher = viewHolder.imgVoucher;
			final String hashcode = entity.getHashCode();
			final MainActivity mainActivity = MainActivity.getMainActivity();
			//图片预览
			imgVoucher.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) 
				{
					Intent imagePreviewIntent = new Intent(context, ImagePreviewActivity.class);
					imagePreviewIntent.putExtra("image", entity.getVdata());
					mainActivity.startActivity(imagePreviewIntent); 
				}
			});
			
			//删除
			btnDelect.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(context)
					.setTitle("系统提示")
					.setMessage("确认删除本地文件吗？");
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if(voucherService.delectByHashcode(hashcode))
							{
								Toast.makeText(convertViewF.getContext(), "删除成功！", 1).show();
								dataList.remove(entity);
								if(CollectionUtility.isNullOrEmpty(dataList))
								{
									mainActivity.setLblSorryContractVisible();
								}
								mainActivity.getAdapter().notifyDataSetChanged();
								return;
							}
							else
							{
								Toast.makeText(convertViewF.getContext(), "删除失败！", 1).show();
							}
						}
					});
					builder.setNegativeButton("取消", null);
					builder.create().show();
				}
			});
			
			//上传
			btnUploadF.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) 
				{
					try
					{
						blockchainService.announce(SecurityUtility.randomInt(20), hashcode);
						voucherService.updateUploadflag(hashcode, "1");
						entity.setUploadTag("1");
						if (voucherService.update(entity))
						{
							Toast.makeText(convertViewF.getContext(), "已经上传到服务器!", 1).show();
						}
						btnUploadF.setEnabled(false);
					}
					catch (Exception e)
					{
					}
				}
			});
		}
		
		return convertView;
	}
	
	private static class ViewHolder 
	{
		ImageView imgVoucher;
		TextView txtHashcode;
		TextView txtVerifyTag;
		Button btnUpload;
		Button btDelect;
	}
}
