package com.iyuba.iyubaclient.widget.TextPage;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.text.Selection;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * �ı�ȡ��View
 * 
 * @author lijingwei
 * 
 */
public class TextPage extends EditText {
	
	private TextPageSelectTextCallBack tpstc;
	private Context context;
	private int off; // �α�λ��
	private boolean isCanSelect=false;
	private boolean enableSelectText=true;

	float [] oldXY;
	
	public TextPage(Context context) {
		super(context);
		this.context = context;
		initialize();
	}

	public TextPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initialize();
	}

	public TextPage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initialize();
	}

	private void initialize() {
		setGravity(Gravity.TOP);
//		setBackgroundColor(Color.TRANSPARENT);
	}

	@Override
	protected void onCreateContextMenu(ContextMenu menu) {
	}

	@Override
	public boolean getDefaultEditable() {
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		
		if(enableSelectText){
			int action = event.getAction();
			Layout layout = getLayout();
			int line = 0;
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				oldXY=new float []{event.getX(), event.getY()};
//				Log.e("A", String.valueOf(oldXY[0]));
				
				isCanSelect=true;
				break;
			case MotionEvent.ACTION_MOVE:
//				Log.e("X", String.valueOf(event.getX()-oldXY[0]));
//				Log.e("Y", String.valueOf(event.getY()-oldXY[1]));
				if(Math.abs(event.getX()-oldXY[0])>8&&Math.abs(event.getY()-oldXY[1])>8){
					isCanSelect=false;
				}
				break;
			case MotionEvent.ACTION_UP:
				
				if(isCanSelect){
					
					line = layout.getLineForVertical(getScrollY() + (int) event.getY());
					off = layout.getOffsetForHorizontal(line, (int) event.getX());					
					String selectText = getSelectText(off);
					if(selectText.length()>0){
						this.setCursorVisible(true);
						if (tpstc != null) {
							tpstc.selectTextEvent(selectText);
						}	
					}else{
						this.setCursorVisible(false);
					}
						
				}
				break;
			}
		}

		return true;
	}

	/**
	 * ��ȡѡ���ı�,����α�λ���Զ�ƥ��Ӣ�ĵ���
	 * 
	 * @param currOff
	 *            ��ǰ�α�
	 * @return
	 */
	public String getSelectText(int currOff) {
		// ˼·���ѵ�ǰ�α�ֱ�����������ƶ��������ո�ֹͣ������������ȡ�����ַ�ϲ����һ������ĵ���
		int leftOff = currOff, rigthOff = currOff;
		int length = getText().toString().length();
		while (true) { // �����ƶ�
			if (leftOff <= 0) {
				break;
			}
			if (leftOff != 0) {
				leftOff = leftOff - 1;
				if (leftOff < 0) {
					leftOff = 0;
				}
			}
			String selectText = getText().subSequence(leftOff, currOff)
					.toString();
			if (!selectText.matches("^[a-zA-Z'-]*")) {
				leftOff += 1;
				break;
			}
		}
		while (true) { // �����ƶ�
			if (rigthOff >= length) {
				break;
			}
			if (rigthOff != 0) {
				rigthOff = rigthOff + 1;
				if (rigthOff > length) {
					rigthOff = length;
				}
			}
			String selectText = getText().subSequence(currOff, rigthOff)
					.toString();
			if (!selectText.matches("^[a-zA-Z'-]*")) {
				rigthOff -= 1;
				break;
			}
		}
		String endString = "";
		try {
			endString = getText().subSequence(leftOff, rigthOff)
					.toString();
			if (endString.trim().length() > 0) {
				Selection.setSelection(getEditableText(), leftOff,
						rigthOff);
			}
		} catch (Exception e) {
		}
		
		return endString.trim();
	}

	/**
	 * �����ı�ѡ���¼�
	 * 
	 * @param tpstc
	 */
	public void setTextpageSelectTextCallBack(TextPageSelectTextCallBack tpstc) {
		this.tpstc = tpstc;
	}
	
	/**
	 * �Ƿ񿪷�ȡ�ʹ���
	 * @param enableSelectText
	 */
	public void setEnableSelectText(boolean enableSelectText) {
		this.enableSelectText = enableSelectText;
	}

	public void clearSelect(){
		Selection.setSelection(getEditableText(), 0,
				0);
	}
	
}
