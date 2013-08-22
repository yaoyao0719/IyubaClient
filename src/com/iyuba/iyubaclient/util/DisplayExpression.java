/**
 * 
 */
package com.iyuba.iyubaclient.util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.StrictMode.VmPolicy;
import android.text.StaticLayout;
import android.widget.ImageView;
/**
 * @author yao
 * 显示表情，替换状态中的<img src="">
 */
class DisplayExpression {

	private String text;

	/**
	 * @param string
	 */
	public DisplayExpression(String text) {
		super();
		this.text = text;
	}

	public String findImgNum(String text){
		String check ="<img/s+[^>]*/s*src/s*=/s*([']?)(?<url>/S+)'?[^>]*>";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(text);
		return matcher.toString();	
	}

	public static void main(){
		String textString=" <img src='static/image/smiley/9.gif' class='vm'>";
		DisplayExpression displayExpression=new DisplayExpression(textString);
		displayExpression.findImgNum(textString);
		System.out.println();
	}

}
