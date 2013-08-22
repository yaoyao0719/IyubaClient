package com.iyuba.iyubaclient;

import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.widget.dialog.CustomDialog;
import com.pcr.android.widget.PullActivateLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnLongClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView.HitTestResult;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ApplicationActivity extends BaseActivity{
	private PullActivateLayout mPullLayout;
	private WebView applicationlist;
	public ProgressBar proDetail;
    private TextView textView;
    private CustomDialog waitingDialog;
    private ImageView app_image,app_down;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applicationlist);
		waitingDialog=waitingDialog();
		initWidget();
		textView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				applicationlist.loadUrl("http://m.iyuba.com.cn/index.jsp?uid="+AccountManager.Instace(mContext).userId);
			}
		});
		app_image.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				applicationlist.loadUrl("http://m.iyuba.com.cn/index.jsp?uid="+AccountManager.Instace(mContext).userId);
			}
		});
		app_down.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, WebActivity.class);
				intent.putExtra("url", "http://app.iyuba.com/android");
				startActivity(intent);
			}
		});
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		System.out.println(mContext+"  ApplicationActivity onNewIntent");
		super.onNewIntent(intent);
		setIntent(intent);
		
	}
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		System.out.println("ApplicationActivity finish");
		super.finish();	
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		System.out.println("ApplicationActivity onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		System.out.println(mContext+"  ApplicationActivity onResume");
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		System.out.println(mContext+"  ApplicationActivity onPause");
		
		super.onPause();
	}

	private void initWidget() {
		// TODO Auto-generated method stub
		applicationlist=(WebView)findViewById(R.id.applicationlist);
		proDetail = (ProgressBar) findViewById(R.id.proDetailApp);
		textView=(TextView)findViewById(R.id.app_text);
		app_down=(ImageView)findViewById(R.id.app_down);
		app_image=(ImageView)findViewById(R.id.app_image);
		applicationlist.buildDrawingCache();
		applicationlist.getSettings().setJavaScriptEnabled(true);
		applicationlist.getSettings().setSupportZoom(true);
		applicationlist.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
		applicationlist.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		applicationlist.getSettings().setSaveFormData(true);
		applicationlist.getSettings().setBlockNetworkImage(false);
		applicationlist.getSettings().setRenderPriority(RenderPriority.HIGH);// 提高渲染,加快加载速度
		applicationlist.setWebChromeClient(webChromeClient);
		applicationlist.setWebViewClient(webViewClient);
		applicationlist.loadUrl("http://m.iyuba.com.cn/index.jsp?uid="+AccountManager.Instace(mContext).userId);
		//applicationlist.loadDataWithBaseURL("http://iyuba.jiangnan.edu.cn/voa/responsive/11.jsp", "", "UTF-8", null);
		
		applicationlist.setDownloadListener(new DownloadListener() {

			@Override
			// TODO Auto-generated method stub
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype,
					long contentLength) {
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});
	}
	private WebChromeClient webChromeClient=new WebChromeClient(){

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// TODO Auto-generated method stub
			
			proDetail.setProgress(newProgress);
			if(newProgress==100){
				proDetail.setVisibility(View.INVISIBLE);
			} else {
				proDetail.setVisibility(View.VISIBLE);
			}
			super.onProgressChanged(view, newProgress);
		}

		
	};
	private WebViewClient webViewClient=new WebViewClient(){
	    @Override
	    public void onPageStarted(WebView view, String url, Bitmap favicon) {
	      // TODO Auto-generated method stub
	      super.onPageStarted(view, url, favicon);
	      waitingDialog.show();
	    }

	    @Override
	    public void onReceivedError(WebView view, int errorCode,
	        String description, String failingUrl) {
	      // TODO Auto-generated method stub
	      super.onReceivedError(view, errorCode, description, failingUrl);
	      waitingDialog.dismiss();
	    }

	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	      view.loadUrl(url);
	      return true;
	    }
	    @Override
		public void onPageFinished(WebView view, String url) {
			waitingDialog.dismiss();
		}
	};
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下的如果是BACK，同时没有重复
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (applicationlist.canGoBack()) {
				applicationlist.goBack();
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	public CustomDialog waitingDialog() {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.wetting_layout, null);
		CustomDialog.Builder customBuilder = new CustomDialog.Builder(this);
		CustomDialog cDialog = customBuilder
				.setContentView(layout).create();
		return cDialog;
	}
}
