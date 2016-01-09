package com.unionpay.photodemo;

import java.io.File;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.CRC32;

import com.misa.droid.framework.annotation.Injection;
import com.misa.droid.framework.db.IDbInitializer;
import com.misa.droid.framework.helper.DbHelper;
import com.misa.droid.framework.img.ImageTool;
import com.misa.droid.framework.view.ActivityBase;
import com.misa.framework.entity.PageSplitDownEntity;
import com.misa.framework.entity.PageSplitUpEntityBase;
import com.misa.framework.exception.ApplicationException;
import com.misa.framework.exception.DBException;
import com.misa.framework.util.CollectionUtility;
import com.misa.framework.util.StringUtility;
import com.misa.test.adapter.VoucherAdapter;
import com.misa.test.app.PhotoDemoApplication;
import com.misa.test.entity.AnnounceEntity;
import com.misa.test.entity.TblVoucherEntity;
import com.misa.test.service.BlockchainService;
import com.misa.test.service.VoucherService;
import com.misa.test.utility.RSAUtility;
import com.misa.test.view.customize.NewDialog;
import com.misa.test.view.customize.RefreshAndLoadListView;
import com.misa.test.view.customize.RefreshAndLoadListView.ILoadListener;
import com.misa.test.view.customize.RefreshAndLoadListView.IReflashListener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("SdCardPath")
public class MainActivity extends ActivityBase implements IReflashListener, ILoadListener  {
	public static MainActivity mainActivity = null;
	
	private ImageView btnLB;
	private ImageView btnCA;
	private ImageView btnPer;

	private ImageView btnGZ;
	private ImageView btnZX;
	private ImageView btnBack;
	private ImageView imgInfo;

	private LinearLayout page1;
	private LinearLayout page2;
	private RelativeLayout page3;
	private LinearLayout bar;

	private RefreshAndLoadListView listVoucher;
	private TextView lblSorryContract;
	private int currentPageNumber;
	private int numberPerPage = 9;
	private VoucherAdapter adapter;
	
	@Injection(type="com.misa.test.service.VoucherService")
	private VoucherService voucherService;
	
	@Injection(type="com.misa.test.service.BlockchainService")
	private BlockchainService blockchainService;
	
	List<TblVoucherEntity> voucherList = new ArrayList<TblVoucherEntity>();
	
	final int TAKE_PICTURE = 1;
	public static final int SCALE = 15;
	final int DB_VERSION = 7;
	
	final static String create_table_sql = "CREATE TABLE [tbl_voucher] (" +
			"[hashcode] VARCHAR(32) NOT NULL ON CONFLICT FAIL," + 
			"[vdata] BINARY NOT NULL," + 
			"[uploadtag] CHAR(1) NOT NULL," + 
			"[verifyTag] CHAR(1) NOT NULL," + 
			"[updatetime] DATETIME NOT NULL," + 
			"[updateuser] VARCHAR(20) NOT NULL, " +
			"CONSTRAINT [tbl_voucher_1] PRIMARY KEY ([hashcode]))";

	public MainActivity()
	{
		mainActivity = this;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpViews();
		
		listVoucher.setReflashListener(this);
		listVoucher.setLoadListener(this);
		onRefresh();
		
		//获取phoneid
		PhotoDemoApplication.phoneId = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID); 
		try 
		{
			//获取所有公钥
			PhotoDemoApplication.publicKeyList = blockchainService.getAllPublicKey();
		} catch (ApplicationException e) {
			e.printStackTrace();
		} catch (DBException e) {
			e.printStackTrace();
		}
		
		announceTask(2000);
	}

	public void setUpViews() {
		btnLB = (ImageView) findViewById(R.id.btnLB);
		btnCA = (ImageView) findViewById(R.id.btnCA);
		btnPer = (ImageView) findViewById(R.id.btnPer);
		btnGZ = (ImageView) findViewById(R.id.btn_gz);
		btnZX = (ImageView) findViewById(R.id.btn_zx);
		btnBack = (ImageView) findViewById(R.id.btn_back);
		imgInfo = (ImageView) findViewById(R.id.imgInfo);

		btnLB.setOnClickListener(btnLBClickListener);
		btnCA.setOnClickListener(btnCAClickListener);
		btnPer.setOnClickListener(btnPerClickListener);
		btnGZ.setOnClickListener(btnGZClickListener);
		btnZX.setOnClickListener(btnZXClickListener);
		btnBack.setOnClickListener(btnLBClickListener);

		page1 = (LinearLayout) findViewById(R.id.page1);
		page2 = (LinearLayout) findViewById(R.id.page2);
		page3 = (RelativeLayout) findViewById(R.id.page3);
		bar = (LinearLayout) findViewById(R.id.bar);
		
		listVoucher = (RefreshAndLoadListView) findViewById(R.id.listCommon);
		lblSorryContract = (TextView)findViewById(R.id.lblSorryContract);
		
		dbInit();
		listVoucher = (RefreshAndLoadListView) findViewById(R.id.listCommon);
	}

	OnClickListener btnGZClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
		}
	};
	OnClickListener btnZXClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
		}
	};
	OnClickListener btnLBClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			page1.setVisibility(View.VISIBLE);
			bar.setVisibility(View.VISIBLE);
			btnLB.setImageResource(R.drawable.lb_p_bg);
			page2.setVisibility(View.GONE);
			page3.setVisibility(View.GONE);
			btnPer.setImageResource(R.drawable.per_n_bg);
		}
	};
	
	//相机
	OnClickListener btnCAClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		    Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"voucher.jpg"));  
		    //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换  
		    openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);  
		    startActivityForResult(openCameraIntent, TAKE_PICTURE);  
		}
	};
	OnClickListener btnPerClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			page2.setVisibility(View.VISIBLE);
			bar.setVisibility(View.VISIBLE);
			btnPer.setImageResource(R.drawable.per_p_bg);
			page1.setVisibility(View.GONE);
			page3.setVisibility(View.GONE);
			btnLB.setImageResource(R.drawable.lb_n_bg);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {  
            switch (requestCode) {  
	            case TAKE_PICTURE:  
	                //将保存在本地的图片取出并缩小后显示在界面上  
	                Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/voucher.jpg");  
	                Bitmap newBitmap = ImageTool.zoomBitmap(bitmap, bitmap.getWidth()/SCALE, bitmap.getHeight()/SCALE);
	                //将处理过的图片显示在界面上，并保存到本地  
	                //iv_image.setImageBitmap(bitmap);  
	                bitmap.recycle();
	                
	                CRC32 crc32 = new CRC32();
	                byte[] bitData = ImageTool.bitmap2Bytes(newBitmap);
	                crc32.update(bitData);
	                long crc = crc32.getValue();
	                String hashCode = String.valueOf(crc);
	                
	                TblVoucherEntity entity = new TblVoucherEntity();
	                entity.setHashCode(hashCode);
	                entity.setVdata(bitData);
	                entity.setUploadTag("0"); //未上传
	                entity.setUpdateTime(new Date());
	                entity.setUpdateUser("sptuser");
	                entity.setVerifyTag("0");//默认未验证
	                if (voucherService.save(entity))
	                {
	                	showMessage("券证已经保存，hashcode为" + hashCode);
	                }
	                onRefresh(); //重新刷洗列表
	                break;  
	            default:  
	                break;  
            }  
        }  
	}

	@Override
	public void onLoad() {
		currentPageNumber++;
		QueryVoucherAsync async = new QueryVoucherAsync();
		async.execute(currentPageNumber, RefreshAndLoadListView.LOAD);
	}

	@Override
	public void onRefresh() {
		currentPageNumber = 1;
		QueryVoucherAsync async = new QueryVoucherAsync();
		async.execute(currentPageNumber, RefreshAndLoadListView.REFRESH);
	}
	
	class QueryVoucherAsync extends AsyncTask<Integer, Integer, List<TblVoucherEntity>> 
	{
		NewDialog mDialog;
		int handleIntent = -1;
		
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			mDialog = NewDialog.show(MainActivity.this, "正在加载",true,null);
		}
		
		@Override
		protected List<TblVoucherEntity> doInBackground(Integer... params) 
		{
			handleIntent = params[1];
			int currentPageNumber = params[0];
			PageSplitUpEntityBase upEntity = new PageSplitUpEntityBase();
			upEntity.setCurrentPageNumber(currentPageNumber);
			upEntity.setNumberPerPage(numberPerPage);
			PageSplitDownEntity<TblVoucherEntity> rtn = voucherService.selectList(upEntity);
			return rtn.getDataList();
		}
		
		@Override
		protected void onPostExecute(List<TblVoucherEntity> currentList) 
		{
			super.onPostExecute(currentList);
			if(mDialog!=null)
			{
				mDialog.dismiss();
			}
			try {
				
				if(voucherList!=null)
				{
					switch (handleIntent) 
					{
					case RefreshAndLoadListView.REFRESH:
						if (CollectionUtility.isNullOrEmpty(voucherList))
						{
							voucherList=new ArrayList<TblVoucherEntity>();
						}
						else
						{
							voucherList.clear();
						}
						voucherList.addAll(currentList);
						adapter = new VoucherAdapter(MainActivity.this, voucherList);
						listVoucher.setAdapter(adapter);
						listVoucher.reflashComplete();//通知listView刷新数据完毕（隐藏顶部）
						break;
						
					case RefreshAndLoadListView.LOAD:
						voucherList.addAll(currentList);
						int total = voucherList.size();
						System.out.println(total);
						listVoucher.loadComplete();//通知listView加载数据完毕（隐藏底部）
						break;
					}
					listVoucher.setResultSize(numberPerPage, currentList.size());
					adapter.notifyDataSetChanged();
				} 
				else
				{
					listVoucher.setResultSize(numberPerPage, -1);
					listVoucher.loadComplete();
					listVoucher.reflashComplete();
				}
			}
			catch(Exception e)
			{
				listVoucher.setResultSize(numberPerPage,-1);
				listVoucher.loadComplete();
				listVoucher.reflashComplete();
			}	
			if(CollectionUtility.isNullOrEmpty(currentList))
			{
				listVoucher.setVisibility(View.GONE);
				lblSorryContract.setVisibility(View.VISIBLE);
			}
			else
			{
				listVoucher.setVisibility(View.VISIBLE);
				lblSorryContract.setVisibility(View.GONE);
			}
		}
	}
	
	private void dbInit()
	{
		DbHelper dbHelper = new DbHelper(this, "misa_droid_test.db", DB_VERSION, new IDbInitializer(){
			@Override
			public void onCreate(SQLiteDatabase db) 
			{	
				db.execSQL(create_table_sql);	
			}

			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion,
					int newVersion) 
			{
				try
				{
					String ddl = "DROP TABLE [tbl_voucher]";
					db.execSQL(ddl);
				}
				catch (SQLiteException e)
				{
				}
				
				db.execSQL(create_table_sql);
			}
		});
		
		PhotoDemoApplication.setDbHelper(dbHelper);
	}
	
	/**
	 * 定时刷新任务列表
	 */
	public void announceTask(final int seconds)
	{
	    final Handler handler=new Handler();  
	    Runnable runnable=new Runnable() {  
	        @Override  
	        public void run() {  
	        	//获取所有需要验证的票据，并对验证通过的票据进行标记
	        	List<AnnounceEntity> dataList = null;
	        	try 
	        	{
	        		dataList = blockchainService.getAllTobeVerifiedAnnounce();
				} catch (ApplicationException e) {
					e.printStackTrace();
				} catch (DBException e) {
					e.printStackTrace();
				}
	        	if(!CollectionUtility.isNullOrEmpty(dataList))
	        	{
	        		for(AnnounceEntity entity : dataList)
	        		{
	        			int keyId = Integer.parseInt(entity.getKeyId());
	        			RSAPublicKey publicKey = PhotoDemoApplication.publicKeyList.get(keyId);
	        			try 
	        			{
							String res = RSAUtility.decryptByPublicKey(entity.getEncryptedData(), publicKey);
							if(!StringUtility.isNullOrEmpty(res) && res.equals(entity.getPhoneId()))
							{
								blockchainService.verify(entity.getHashcode()); 		//标记已验证
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
	        		}
	        	}
	        	
	        	//获取所有已标记的标记，并且过滤本机的票据
	        	List<AnnounceEntity> dataList1 = null;
	        	try {
	        		dataList1 = blockchainService.getAllVerifiedAnnounce();
				} catch (ApplicationException e) {
					e.printStackTrace();
				} catch (DBException e) {
					e.printStackTrace();
				}
	        	
	        	if(!CollectionUtility.isNullOrEmpty(dataList1))
	        	{
	        		for(AnnounceEntity entity : dataList1)
	        		{
	        			String hashCode = entity.getHashcode();
	        			if(!StringUtility.isNullOrEmpty(hashCode))
	        			{
	        				for(TblVoucherEntity voucherEntity : voucherList)
		        			{
		        				if(hashCode.equals(voucherEntity.getHashCode()))
		        				{
		        					voucherService.updateVerifyTag(entity.getHashcode(),"1");//已验证
		        					voucherEntity.setVerifyTag("1");
		        					adapter.notifyDataSetChanged();
		        				}
		        			}
	        			}
	        		}
	        	}
	        	
	        	handler.postDelayed(this, seconds);  
	        }  
	    };  
	    handler.postDelayed(runnable, seconds);//定时执行一次runnable.  
	}
	
	/**
	 * 
	 * @param seconds
	 */
	public void verifyTagTask(final int seconds)
	{
		final Handler handler=new Handler();
		Runnable runnable=new Runnable() {  
	        @Override  
	        public void run() 
	        {
	        	
	        	
	        	handler.postDelayed(this, seconds);  
	        }
		 };  
		 handler.postDelayed(runnable, seconds);//定时执行一次runnable.  
	}
	
	public void setLblSorryContractVisible()
	{
		listVoucher.setVisibility(View.GONE);
		lblSorryContract.setVisibility(View.VISIBLE);
	}
	
	public VoucherAdapter getAdapter() {
		return adapter;
	}

	public static MainActivity getMainActivity() {
		return mainActivity;
	}
}