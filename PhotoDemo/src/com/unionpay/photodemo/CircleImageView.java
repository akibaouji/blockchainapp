package com.unionpay.photodemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.unionpay.photodemo.BaseImageView;

/**
 * 圆形的Imageview
 * @author 瞿峰
 *
 */
public class CircleImageView extends BaseImageView {

	public CircleImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Drawable drawable = getDrawable();

		if (drawable == null || getWidth() == 0 || getHeight() == 0) {
			return;
		}

		// copy bitmap
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap().copy(
				Bitmap.Config.ARGB_8888, true);

		// scaled bitmap
		Bitmap sbmp;
		if (bitmap.getWidth() != getWidth() || bitmap.getHeight() != getWidth())
			sbmp = Bitmap.createScaledBitmap(bitmap, getWidth(), getWidth(),
					false);
		else
			sbmp = bitmap;

		drawBitmap(canvas, sbmp);
	}

	/**
	 * 画圆形图片
	 * 
	 * @param canvas
	 * @param sbmp
	 */
	private void drawBitmap(Canvas canvas, Bitmap sbmp) {
		Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(),
				Config.ARGB_8888);
		Canvas cvs = new Canvas(output);

		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		cvs.drawARGB(0, 255, 255, 255);
		// 画笔颜色
		paint.setColor(Color.parseColor("#7f97d2"));

		float sbmp_cx = 0, sbmp_cy = 0, sbmp_radius = 0;

		sbmp_cx = sbmp.getWidth() / 2;
		sbmp_cy = sbmp.getHeight() / 2;
		sbmp_radius = sbmp.getWidth() / 2;

		cvs.drawCircle(sbmp_cx, sbmp_cy, sbmp_radius, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		cvs.drawBitmap(sbmp, rect, rect, paint);

		canvas.drawBitmap(output, 0, 0, null);
	}

}
