package com.misa.test.view.customize;

import com.unionpay.photodemo.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
/**
 * ��ɫ��������ֱ����
 * @author dp
 *
 */
public class NewDialog extends Dialog 
{

	public NewDialog(Context context, int theme) 
	{
		super(context, theme);
	}

	public NewDialog(Context context) 
	{
		super(context);
	}
	/**
	 * �����Զ���ProgressDialog
	 * 
	 * @param context
	 *            ������
	 * @param message
	 *            ��ʾ����
	 * @param cancelable
	 *            �Ƿ񰴷��ؼ�ȡ��
	 * @param cancelListener
	 *            ���·��ؼ�����
	 * @return
	 */
	public static NewDialog show(Context context, CharSequence message,
			boolean cancelable, OnCancelListener cancelListener) 
	{
		NewDialog dialog = new NewDialog(context, R.style.Custom_Progress);
		dialog.setContentView(R.layout.dialog_layout);
		if (message == null || message.length() == 0) 
		{
			dialog.findViewById(R.id.lblMessage).setVisibility(View.GONE);
		} 
		else 
		{
			TextView lblMessage = (TextView) dialog.findViewById(R.id.lblMessage);
			lblMessage.setText(message);
		}
		// �����ؼ��Ƿ�ȡ��
		dialog.setCancelable(cancelable);
		// �������ؼ�����
		dialog.setOnCancelListener(cancelListener);
		// ���þ���
		dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		// ���ñ�����͸����
		lp.dimAmount = 0.2f;
		dialog.getWindow().setAttributes(lp);
		// dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		dialog.show();
		return dialog;
	}
}
