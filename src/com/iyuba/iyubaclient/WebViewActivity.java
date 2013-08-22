/**
 * 
 */
package com.iyuba.iyubaclient;

import org.ths.frame.THSActivity;

import com.iyuba.iyubaclient.webview.util.BTWebView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.RenderPriority;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author yao
 * 长按取词
 */
public class WebViewActivity extends THSActivity {
	private Button backButton;
	private BTWebView web;
	private TextView textView;
	public ProgressBar proDetail;
    private String appname;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.webview);
		Intent intent=getIntent();
		appname=intent.getStringExtra("appname");
		backButton = (Button) findViewById(R.id.webview_back);
		textView = (TextView) findViewById(R.id.appname);
		proDetail = (ProgressBar) findViewById(R.id.webview_proDetailApp);
		web = (BTWebView) findViewById(R.id.webView);
		textView.setText(appname);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		//web.loadUrl(this.getIntent().getStringExtra("url"));
		web.setURL(this.getIntent().getStringExtra("url"));
		web.requestFocus();
		web.getSettings().setBuiltInZoomControls(true);// 显示放大缩小
		web.getSettings().setSupportZoom(true);// 可放大
		web.getSettings().setRenderPriority(RenderPriority.HIGH);// 提高渲染,加快加载速度
		web.getSettings().setUseWideViewPort(true);
		web.getSettings().setLoadWithOverviewMode(true);
		web.setWebChromeClient(webChromeClient);
		web.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {

			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO Auto-generated method stub
				super.onReceivedError(view, errorCode, description, failingUrl);

			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
			}
		});
		web.setDownloadListener(new DownloadListener() {

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

	private WebChromeClient webChromeClient = new WebChromeClient() {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// TODO Auto-generated method stub

			proDetail.setProgress(newProgress);
			if (newProgress == 100) {
				proDetail.setVisibility(View.GONE);
			} else {
				proDetail.setVisibility(View.VISIBLE);
			}
			super.onProgressChanged(view, newProgress);
		}

	};

	@Override
	public boolean onKeyDown(int keyCoder, KeyEvent event) {
		if (web.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK) {
			web.goBack(); // goBack()表示返回webView的上一页面
			return true;
		} else if (!web.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return false;
	}

	@Override
	public void finish() {

		super.finish();
	}
}
