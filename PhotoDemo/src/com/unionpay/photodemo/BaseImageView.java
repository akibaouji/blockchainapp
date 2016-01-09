package com.unionpay.photodemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class BaseImageView extends ImageView{

	public BaseImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public BaseImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public BaseImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		// TODO Auto-generated method stub
		super.setImageDrawable(drawable);

	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		// TODO Auto-generated method stub
		super.setImageBitmap(bm);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		/* 避免引用已回收的bitmap异常 */
		try {
			super.onDraw(canvas);
		} catch (Exception e) {
			System.out
					.println("BaseImageView  -> onDraw() Canvas: trying to use a recycled bitmap");
		}
	}
	
	
	

}
