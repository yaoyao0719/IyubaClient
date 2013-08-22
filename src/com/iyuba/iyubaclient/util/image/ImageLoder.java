package com.iyuba.iyubaclient.util.image;



import org.ths.frame.runtime.RuntimeManager;

import android.graphics.Bitmap;

/**
 * 根据url自动获取图片
 *	依赖于其他几个类包括
 *
 *ImageMemoryCache.java
 *优先从内存缓存中获取图片
 *
 *ImageFileCache.java	
 *其次寻找SD卡中
 *
 *ImageGetFromHttp.java
 *最后去网上下载
 *
 */
public class ImageLoder {
	
	private static ImageMemoryCache memoryCache =new ImageMemoryCache(RuntimeManager.getContext());
	private static ImageFileCache fileCache = new ImageFileCache();
	public Bitmap getBitmap(String url) {
	    // 从内存缓存中获取图片
	    Bitmap result = memoryCache.getBitmapFromCache(url);
	    if (result == null) {
	        // 文件缓存中获取
	        result = fileCache.getImage(url);
	        if (result == null) {
	            // 从网络获取
	            result = ImageGetFromHttp.downloadBitmap(url);
	            if (result != null) {
	                fileCache.saveBitmap(result, url);
	                memoryCache.addBitmapToCache(url, result);
	            }
	        } else {
	            // 添加到内存缓存
	            memoryCache.addBitmapToCache(url, result);
	        }
	    }
	    return result;
	}

}
