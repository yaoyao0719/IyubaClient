package com.iyuba.iyubaclient.widget.TextPage;

import java.util.ArrayList;
import java.util.List;



import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 字幕同步View
 * @author lijingwei
 *
 */
public class SubtitleSynView extends ScrollView implements TextPageSelectTextCallBack {
	
	private Context context;
	private LinearLayout subtitleLayout;
	private TextPage textPage;
	private TextPageSelectTextCallBack tpstcb;
	private Spanned content;
	
	public void setTpstcb(TextPageSelectTextCallBack tpstcb) {
		this.tpstcb = tpstcb;
	}

	public SubtitleSynView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initWidget(context);
	}

	public SubtitleSynView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initWidget(context);
	}

	public SubtitleSynView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initWidget(context);
	}

	private void initWidget(Context context){
		this.context=context;
		subtitleLayout=new LinearLayout(this.context);
		subtitleLayout.setOrientation(LinearLayout.VERTICAL);
	}
	
	/**
	 * ������Ļ��
	 * @param subtitleSum
	 */
	public void setSubtitleSum(Spanned content){
		this.content=content;
		subtitleLayout.removeAllViews();
		removeAllViews();
		initSubtitleSum();
	}
	
	public void initSubtitleSum(){
		if(content!=null&&content.length()!=0){
			
				TextPage tp=new TextPage(this.context);
				tp.setBackgroundColor(Color.TRANSPARENT);
				tp.setTextColor(Color.BLACK);
				
				tp.setText(content);
				tp.setTextpageSelectTextCallBack(this);

				subtitleLayout.addView(tp);
			
		}
		addView(subtitleLayout);
	}

	@Override
	public void selectTextEvent(String selectText) {
		// TODO Auto-generated method stub
		tpstcb.selectTextEvent(selectText);

	}
	


	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {			
			super.handleMessage(msg);

			switch (msg.what) {
			case 0:
/*				if (subtitleViews != null && subtitleViews.size() != 0) {
					int center = 0;
					for (int i = 0; i < subtitleViews.size(); i++) {
						TextView textView = (TextView) subtitleViews.get(i);

						if (currParagraph == i + 1) {
							textView.setTextColor(Color.RED);
							center = textView.getTop() + textView.getHeight()/2;
						} else {
							textView.setTextColor(Color.BLACK);
						}
					}

					center -= getHeight() / 2;
					if (center > 0) {
						smoothScrollTo(0, center);
					}else {
						smoothScrollTo(0, 0);
					}
				}*/
				break;
			case 1:
				break;
			}
		}
		
	};


}
