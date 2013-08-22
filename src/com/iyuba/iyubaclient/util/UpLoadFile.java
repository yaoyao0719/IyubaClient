/**
 * 
 */
package com.iyuba.iyubaclient.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.widget.ImageButton;

/**
 * @author yao
 * 上次头像、图片等
 */
public class UpLoadFile {
	// 直接通过 HttpMime's MultipartEntity 提交数据到服务器，实现表单提交功能。
	public static String filePost(){
		HttpClient httpClient=new DefaultHttpClient();
		try{
			BufferedReader in=new BufferedReader(new FileReader("config/actionUrl.properties"));
			String actionUrl;
			actionUrl=in.readLine();
			HttpPost httpPost=new HttpPost(actionUrl);
		    // 通过阅读源码可知，要想实现图片上传功能，必须将 MultipartEntity 的模式设置为 BROWSER_COMPATIBLE 。
		    MultipartEntity multiEntity=new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		    //读取图片MIME TYPE类型集
		    Properties mimeTypes=new Properties();
		    mimeTypes.load(new FileInputStream(new File("config/MIMETypes.properties")));
		    //构造图片数据
		    Properties imageParams=new Properties();
		    imageParams.load(new FileInputStream(new File("config/imageParams.properties")));
		    String fileType;
		    for(Map.Entry<Object, Object>image:imageParams.entrySet()){
		    	String path=image.getValue().toString();
		    	fileType=path.substring(path.lastIndexOf("."+1));
		    	FileBody binaryContent=new FileBody(new File(path),mimeTypes.getProperty(fileType).toString());
		    	multiEntity.addPart(image.getKey().toString(), binaryContent);
		    }
		    //构造表单参数数据
		    Properties formDataParams=new Properties();
		    formDataParams.load(new FileInputStream(new File("config/formDataParams.properties")));
            for(Entry<Object, Object> param : formDataParams.entrySet()) {
                multiEntity.addPart(param.getKey().toString(), new StringBody(param.getValue().toString()));
            }
            httpPost.setEntity(multiEntity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();
            if(resEntity != null) {
                String returnContent = EntityUtils.toString(resEntity);
               // EntityUtils.consume(resEntity);
                
                return returnContent; // 返回页面内容
            }
		}catch (Exception e) {
			// TODO: handle exception
		}finally{
			 httpClient.getConnectionManager().shutdown();
		}
		return null;	
	}
	   public static void main(String[] args) {
	        System.out.println("Response content: " + UpLoadFile.filePost());
	    }
}
