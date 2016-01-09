package com.unionpay.photodemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.misa.droid.framework.img.ImageTool;
import com.misa.droid.framework.view.ActivityBase;

public class ImagePreviewActivity extends ActivityBase 
{
	private ImageView imageView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		imageView = (ImageView)findViewById(R.id.progress_iv);	
		Intent intent = getIntent();
		byte[] image = intent.getByteArrayExtra("image");
		Bitmap bitmap = ImageTool.bytes2Bimap(image);
		Bitmap newBitmap = ImageTool.zoomBitmap(bitmap, bitmap.getWidth() * MainActivity.SCALE, bitmap.getHeight() * MainActivity.SCALE);
		imageView.setImageBitmap(newBitmap);
		
		imageView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
