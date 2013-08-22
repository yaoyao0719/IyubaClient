/**
 * 
 *//*
package com.iyuba.iyubaclient.util;

import android.content.Context;
import android.view.View;

*//**
 * @author yao
 * 显示表情表
 *//*
public class AddEmotion extends View{
	
	public AddEmotion(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
	}

	public void addexpression(View view){
		if (expressionGriView.getVisibility() == View.GONE) {
			expressionGriView.setVisibility(View.VISIBLE);
			
			emotionList = BlogHomeActivity.emotions;
			ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
			 
		      for(int i=0;i<70;i++)  
		      { 
		    	emtions = emotionList.get(i);   
	            if (emtions != null) {
	            	HashMap<String, Object> map = new HashMap<String, Object>();
	            
	            	Field f;
					try {
						f = (Field)R.drawable.class.getDeclaredField(emtions.getImageName());
						int j = f.getInt(R.drawable.class);	
				        map.put("ItemImage", j);//添加图像资源的ID  
				        lstImageItem.add(map);  
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (NoSuchFieldException e) {
						e.printStackTrace();
					}catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					
	            }  
		        
		      }  
		      
		    //生成适配器的ImageItem <====> 动态数组的元素，两者一一对应  
		      SimpleAdapter saImageItems = new SimpleAdapter(this, 
		                                                lstImageItem,//数据来源   
		                                                R.layout.blog_emotion_list,
		                                                  
		                                                //动态数组与ImageItem对应的子项          
		                                                new String[] {"ItemImage"},   
		                                                  
		                                                //ImageItem的XML文件里面的一个ImageView
		                                                new int[] {R.id.blog_sendmsg_emotion});  
		      
		      expressionGriView.setAdapter(saImageItems);  
		} else {
			expressionGriView.setVisibility(View.GONE);
		}
		
	}
}
*/