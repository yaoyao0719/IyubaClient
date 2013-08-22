package com.iyuba.iyubaclient.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.Context;
import android.text.StaticLayout;
import android.util.Log;
/**
 * 
 * @author Administrator
 * 
 * 功能：将assets目录下指定文件夹及子文件夹和文件拷贝到sd�?
 *
 */
public class CopyFileToSD
{
  private Context mContext;
  private OnFinishedListener oflistener;
  private String targetDir;//拷贝到sd的目标地�?
  private int sourceFileNum;//源文件数
  /**
   * 
   * 把assets目录下指定文件夹里的文件拷贝到指定sd卡目录下
   * @param context
   * @param assetDir  assets目录下指定文件夹�?
   * @param dir  sd卡目录
   */
  public CopyFileToSD(Context context, String assetDir,String dir,OnFinishedListener oflistener) throws IOException
  {
    mContext = context;
    this.oflistener = oflistener;
    
    this.targetDir = dir;
    this.sourceFileNum = mContext.getResources().getAssets().list(assetDir).length;
      if (ListFileUtil.checkSubFiles(targetDir) == sourceFileNum)
      {
        oflistener.onFinishedListener();
        return;
      }

      
    
    CopyAssets(assetDir,dir);
  }
  /**
   * 
   * @param assetDir
   * @param dir
   * 功能�?iYuba/jlpt3/image
   */
  public void CopyAssets(String assetDir,String dir) {
      String[] files;   
      File mWorkingPath = new File(dir);
      //if this directory does not exists, make one. 
      //判断/iYuba/jlpt3/image文件是否存在
      if(!mWorkingPath.exists())    
      {    
        mWorkingPath.mkdirs();   
      } 

         try    
         {    
             files = mContext.getResources().getAssets().list(assetDir);    
//             Log.e(" assets下文件数", "当前assetDir�?+assetDir+"下文件数�?+files.length);
         }    
         catch (IOException e1)    
         {    
//           oflistener.onFinishedListener();
             return;    
         }    
   
         
         for(int i = 0; i < files.length; i++)    
         {    
             try    
             {    
                 String fileName = files[i]; 
                 //we make sure file name not contains '.' to be a folder. 
                 if(!fileName.contains("."))
                 {
                   if(0==assetDir.length())
                   {
                     CopyAssets(fileName,dir+"/"+fileName+"/");
                   }
                   else
                   {
                     CopyAssets(assetDir+"/"+fileName,dir+"/"+fileName+"/");
                   }
                   continue;
                 }
                 File outFile = new File(mWorkingPath, fileName);    
                 if(outFile.exists()) 
                   outFile.delete();
                 InputStream in =null;
                 if(0!=assetDir.length())
                   in = mContext.getAssets().open(assetDir+"/"+fileName);    
                 else
                   in = mContext.getAssets().open(fileName);
                 OutputStream out = new FileOutputStream(outFile);    
         
                 // Transfer bytes from in to out   
                 byte[] buf = new byte[1024];    
                 int len;    
                 while ((len = in.read(buf)) > 0)    
                 {    
                     out.write(buf, 0, len);    
                 }    
         
                 in.close();    
                 out.close();    
             }    
             catch (FileNotFoundException e)    
             {    
                 e.printStackTrace();    
             }    
             catch (IOException e)    
             {    
                 e.printStackTrace();    
             }         
        }
//         Log.e("CopyFileToSD:images",dir+"拷贝成功");
//         Log.e("", "目标地址"+targetDir+"文件�?+ListFileUtil.checkSubFiles(targetDir));
       //�?��目标地址文件夹数
         if (targetDir.equals(dir) && ListFileUtil.checkSubFiles(targetDir) == sourceFileNum)
         {//子文件夹数等�?0
           oflistener.onFinishedListener();
           return;
         }

  }

    
}
