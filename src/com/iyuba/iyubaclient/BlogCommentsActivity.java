package com.iyuba.iyubaclient;

import java.util.ArrayList;
import java.util.List;

import org.ths.frame.THSActivity;
import org.ths.frame.network.ClientNetwork;
import org.ths.frame.network.DefNetStateReceiverImpl;
import org.ths.frame.network.IResponseReceiver;
import org.ths.frame.network.protocol.BaseHttpRequest;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.TimeUtil;

import com.iyuba.iyubaclient.adapter.CommentsListAdapter;
import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.manager.DataManager;
import com.iyuba.iyubaclient.procotol.RequestGetComments;
import com.iyuba.iyubaclient.procotol.RequestSendComments;
import com.iyuba.iyubaclient.procotol.ResponseGetComments;
import com.iyuba.iyubaclient.procotol.ResponseSendComments;
import com.iyuba.iyubaclient.sqlite.entity.BlogComment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class BlogCommentsActivity extends THSActivity {

	private Button backButton;
	private ListView commentsListView;
	private CommentsListAdapter commentsListAdapter;
	private EditText contentEdit;
	private List<BlogComment> voaComments = new ArrayList<BlogComment>();
	private BlogComment blogCommentForSend = new BlogComment();
	private String blogId, id;
	private String type = "blogid";
	private int pageNum = 1;
	private TextView comment_subject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commentlist);
		blogId = DataManager.Instace().blogContent.blogid;
		initWidget();
	}

	private void initWidget() {
		// TODO Auto-generated method stub
		backButton = (Button) findViewById(R.id.comment_back_btn);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		comment_subject = (TextView) findViewById(R.id.comment_subject);
		comment_subject.setText(DataManager.Instace().blogContent.subject);
		commentsListView = (ListView) findViewById(R.id.commentsListView);
		commentsListAdapter = new CommentsListAdapter(mContext, voaComments);
		commentsListView.setAdapter(commentsListAdapter);
		commentsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (arg2 == 0) {

				}else{
					/*contentEdit.findFocus();
					((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(contentEdit, 0); 
					contentEdit.setText("回复 "+voaComments.get(arg2-1).username+":");
					Editable etext = contentEdit.getText();
					int position = etext.length();
					Selection.setSelection(etext, position);
					item.grade=voaComments.get(arg2-1).grade;*/
				}	
			}
		});
		contentEdit = (EditText) findViewById(R.id.comments_content);
		contentEdit.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub

				if (EditorInfo.IME_ACTION_SEND == actionId) {

					String content = contentEdit.getText().toString();
					if (content != null && content.length() != 0) {
						blogCommentForSend = new BlogComment();
						blogCommentForSend.author = AccountManager
								.Instace(mContext).userName;
						blogCommentForSend.authorid = AccountManager
								.Instace(mContext).userId;
						blogCommentForSend.message = content;
						blogCommentForSend.dateline = String.valueOf(System
								.currentTimeMillis() / 1000);
						handler.sendEmptyMessage(2);
					} else {
						Toast.makeText(mContext, "评论不能为空", Toast.LENGTH_SHORT)
								.show();
					}

				}
				return true;
			}
		});
		handler.sendEmptyMessage(0);
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0: // 获取最新评论
				ClientNetwork.Instace().asynGetResponse(
						new RequestGetComments(blogId, type, pageNum),
						new IResponseReceiver() {

							@Override
							public void onResponse(BaseHttpRequest request,
									BaseHttpResponse response) {
								// TODO Auto-generated method stub
								ResponseGetComments responseGetComments = (ResponseGetComments) response;
								if (responseGetComments.blogComments != null
										&& responseGetComments.blogComments
												.size() != 0) {
									voaComments
											.addAll(responseGetComments.blogComments);
									handler.sendEmptyMessage(1);
								}

							}
						}, new DefNetStateReceiverImpl(), null);
				break;
			case 1: // 刷新列表
				commentsListAdapter.notifyDataSetChanged();
				break;
			case 2: // 发送评论
				ClientNetwork.Instace().asynGetResponse(
						new RequestSendComments(
								AccountManager.Instace(mContext).userId,
								blogId, type,
								AccountManager.Instace(mContext).userName,
								AccountManager.Instace(mContext).userId,
								blogCommentForSend.message),
						new IResponseReceiver() {

							@Override
							public void onResponse(BaseHttpRequest request,
									BaseHttpResponse response) {
								// TODO Auto-generated method stub
								ResponseSendComments responseSendComments = (ResponseSendComments) response;
								if (responseSendComments.isSendSuccess) {
									voaComments.add(0, blogCommentForSend);
									handler.sendEmptyMessage(3);
									handler.sendEmptyMessage(1);
								}
							}
						}, new DefNetStateReceiverImpl(), null);
				break;
			case 3:
				contentEdit.setText("");
				break;
			}
		}
	};

}
