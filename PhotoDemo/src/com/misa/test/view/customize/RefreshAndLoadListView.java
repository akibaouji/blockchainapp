package com.misa.test.view.customize;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.misa.framework.util.DateUtility;
import com.unionpay.photodemo.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;


/**
 * @author dp
 *
 */

public class RefreshAndLoadListView extends ListView implements OnScrollListener {
	
	public static final int REFRESH = 0;
	public static final int LOAD = 1;
	private View header;
	private int headerHeight;
	private View footer;//�ײ�����
	private int totalItemCount;
	private int lastVisibleItem;
	private boolean isLoading=false;//���ڼ���
	private ILoadListener loadListener;
	private boolean isFullLoad;
	private int firstVisibleItem;
	private boolean isRemark;// ���listView�����ʱ����
	int startY;// ����ʱ���Yֵ
	private int state;
	//private int scrollState;// ��ǰ����״̬
	private static final int NONE = 0;// ����״̬
	private static final int PULL = 1;// ��ʾ����״̬
	private static final int RELESE = 2;// �ɿ��ͷŵ�״̬
	private static final int REFLASHING = 3;// ˢ��״̬
	private static int LAST_STATE=NONE;
	private IReflashListener reflashListener;//ˢ�����ݵĽӿ�
	
	public RefreshAndLoadListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public RefreshAndLoadListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public RefreshAndLoadListView(Context context) {
		super(context);
		initView(context);
	}
	
	//��ʼ�����棬��Ӷ�������
	private void initView(Context context) {
		LayoutInflater inflater=LayoutInflater.from(context);
		header=inflater.inflate(R.layout.header_layout,null);
		measureView(header);
		headerHeight=header.getMeasuredHeight();
		topPadding(-headerHeight);
		
		LayoutInflater inflater2=LayoutInflater.from(context);
		footer=inflater2.inflate(R.layout.footer_layout,null);
		footer.setVisibility(View.GONE);
		this.addFooterView(footer);
		this.addHeaderView(header);
		this.setFooterDividersEnabled(false);
		this.setOnScrollListener(this);
		this.setOnScrollListener(this);

	}
	//����header���ϱ߾�
	private void topPadding(int topPadding){
		header.setPadding(header.getPaddingLeft(), topPadding, header.getPaddingRight(), header.getPaddingBottom());
		header.invalidate();
	}
	
	//֪ͨ������ռ�õĸ߶ȺͿ��
	private void measureView(View view)
	{
		ViewGroup.LayoutParams p=view.getLayoutParams();
		if(p==null)
		{
			p=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		
		int width=ViewGroup.getChildMeasureSpec(0,0,p.width);
		int height;
		int tempHeight=p.height;
		if(tempHeight>0){
			height=MeasureSpec.makeMeasureSpec(tempHeight,MeasureSpec.EXACTLY);
		}else
		{
			height=MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED);
		}
		view.measure(width, height);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) 
	{
		this.firstVisibleItem = firstVisibleItem;
		this.totalItemCount=totalItemCount;
		this.lastVisibleItem = firstVisibleItem+visibleItemCount;
		
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) 
	{
		//this.scrollState = scrollState;
		
		if(totalItemCount==lastVisibleItem )
		{
			if(!isLoading && !isFullLoad){
				isLoading=true;
				footer.findViewById(R.id.loadLayout).setVisibility(View.VISIBLE);
				footer.setVisibility(View.VISIBLE);
				//���ظ�������
				loadListener.onLoad();
				
			}
		}

	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			LAST_STATE=NONE;
			if(firstVisibleItem==0)
			{
				isRemark=true;
				startY=(int) ev.getY();
			}
		break;
		case MotionEvent.ACTION_MOVE:
			onMove(ev);
		break;
		case MotionEvent.ACTION_UP:
			LAST_STATE=NONE;
			if(state==RELESE){
				state=REFLASHING;
				//������������
				reflashViewByState();
				reflashListener.onRefresh();
			}else if(state==PULL){
				state=NONE;
				isRemark=false;
				reflashViewByState();
			}
		break;

		default:
		break;
		}
		return super.onTouchEvent(ev);
	}
	//�ж��ƶ������еĲ���
	private void onMove(MotionEvent ev)
	{
		if(!isRemark){
			return;
		}
		int tempY = (int) ev.getY();
		int space = tempY - startY;
		int topPadding = (space - headerHeight)/3;
		switch (state) {
		case NONE:
			LAST_STATE=NONE;
			if (space > 0) {
				state = PULL;
				reflashViewByState();
			}
			break;
		case PULL:
			LAST_STATE=PULL;
			topPadding(topPadding);
			if (space > headerHeight+30 && state==SCROLL_STATE_TOUCH_SCROLL) {
				state = RELESE;
				reflashViewByState();
			}
			break;
		case RELESE:
			LAST_STATE=RELESE;
			topPadding(topPadding);
			if (space < headerHeight) {
				state = PULL;
				reflashViewByState();
			} else if (space <= 0) {
				state = NONE;
				isRemark = false;
				reflashViewByState();
			}
			break;
		case REFLASHING:

			break;

		default:
			break;
		}
	}
	// ���ݵ�ǰ״̬�ı������ʾ
	private void reflashViewByState(){
		TextView lblTip = (TextView) header.findViewById(R.id.tip);
		ImageView imgArrow = (ImageView) header.findViewById(R.id.arrow);
		ProgressBar progressBar = (ProgressBar) header.findViewById(R.id.progress);
		
		//��ͷ����Ч��
		RotateAnimation animation = new RotateAnimation(0, 180,RotateAnimation.RELATIVE_TO_SELF, 0.5f,RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(500);
		animation.setFillAfter(true);
		RotateAnimation animation2 = new RotateAnimation(180, 1,RotateAnimation.RELATIVE_TO_SELF, 0.5f,RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation2.setDuration(500);
		animation2.setFillAfter(true);
		switch (state) {
		case NONE:
			imgArrow.clearAnimation();
			topPadding(-headerHeight);
			break;
		case PULL:
			imgArrow.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			lblTip.setText("��������ˢ��");
			if(LAST_STATE!=NONE)
			{
				imgArrow.clearAnimation();
				imgArrow.setAnimation(animation2);
			}
			break;
		case RELESE:
			imgArrow.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			lblTip.setText("�ɿ�����ˢ��");
			imgArrow.clearAnimation();
			imgArrow.setAnimation(animation);
			break;
		case REFLASHING:
			topPadding(50);
			imgArrow.clearAnimation();
			imgArrow.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
			lblTip.setText("����ˢ��");
			break;

		default:
			break;
		}
	}
	
	//���ݻ�ȡ������
	public void reflashComplete()
	{
		state=NONE;
		isRemark=false;
		reflashViewByState();
		
		TextView lblLastUpdateTime=(TextView) header.findViewById(R.id.lastUpdateTime);
		SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm:ss");
		Date date=DateUtility.getDate();
		String time=dateFormat.format(date);
		lblLastUpdateTime.setText("�ϴ�ˢ��ʱ��"+time);
	}
	
	//��ȡ������
	public void loadComplete()
	{
		isLoading=false;
		footer.findViewById(R.id.loadLayout).setVisibility(View.GONE);
	}
			
	public void setLoadListener(ILoadListener loadListener)
	{
		this.loadListener=loadListener;
	}
	
	//���ظ������ݻص��ӿ�
	public interface ILoadListener{
		public void onLoad();
	}
	
	public void setReflashListener(IReflashListener reflashListener)
	{
		this.reflashListener=reflashListener;	
	}
	
	public interface IReflashListener
	{
		public void onRefresh();
	}
	
	public void setResultSize(int requestSize,int resultSize)
	{
		if(resultSize<requestSize && resultSize>=0)
		{
			isFullLoad=true;
			loadComplete();
		}
		else if(resultSize==requestSize)
		{
			isFullLoad=false;
		}
		else if(resultSize<0)
		{
			isFullLoad=true;
			loadComplete();
		}
	}
}
