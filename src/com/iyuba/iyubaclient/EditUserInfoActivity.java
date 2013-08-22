package com.iyuba.iyubaclient;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.json.JSONObject;
import org.ths.frame.THSActivity;
import org.ths.frame.network.ClientNetwork;
import org.ths.frame.network.IResponseReceiver;
import org.ths.frame.network.protocol.BaseHttpRequest;
import org.ths.frame.network.protocol.BaseHttpResponse;
import org.ths.frame.util.SDCardUtil;

import com.iyuba.iyubaclient.entity.EditUserInfo;
import com.iyuba.iyubaclient.entity.JudgeZodicaAndConstellation;
import com.iyuba.iyubaclient.manager.AccountManager;
import com.iyuba.iyubaclient.manager.DownLoadCallBack;
import com.iyuba.iyubaclient.manager.DownLoadManager;
import com.iyuba.iyubaclient.manager.UserBasicInfoManager;
import com.iyuba.iyubaclient.procotol.RequestEditUserInfo;
import com.iyuba.iyubaclient.procotol.RequestUserDetailInfo;
import com.iyuba.iyubaclient.procotol.ResponseBasicUserInfo;
import com.iyuba.iyubaclient.procotol.ResponseEditUserInfo;
import com.iyuba.iyubaclient.procotol.ResponseUserDetailInfo;
import com.iyuba.iyubaclient.util.Constant;
import com.iyuba.iyubaclient.widget.dialog.CustomDialog;

import android.R.integer;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EditUserInfoActivity extends THSActivity {

	private TextView editGender, editBirthday, editResideLocation;
	private LinearLayout editPortrait;
	private ResponseUserDetailInfo userDetailInfo;
	private String userImgSrc;
	private Bitmap userBitmap;
	private ImageView iveditPortrait;
	private Button editinfo_back_btn, editinfo_save_btn;
	private final static int GENDER_DIALOG = 4;// 性别选择
	private final static int DATE_DIALOG = 5;// 日期选择
	private final static int PORTRAIT_DIALOG = 6;// 头像选择
	private final static int ADDRESS_DIALOG = 7;// 省市选择
	private Calendar calendar = null;
	private EditUserInfo editUserInfo = new EditUserInfo();
	private CustomDialog waitingDialog;
	public static final String IMAGE_UNSPECIFIED = "image/*";
	public static final int PHOTOHRAPH = 1;// 拍照
	public static final int PHOTOZOOM = 2; // 缩放
	public static final int PHOTORESOULT = 3;// 结果
	public static final int NONE = 0;
	private Boolean isChangePor = false;
	/*
	 * 变量声明 newName：上传后在服务器上的文件名称 uploadFile：要上传的文件路径 actionUrl：服务器对应的程序路径
	 */
	private String actionUrl = "http://172.16.94.220:8081/iyubaApi/v2/avatar?uid=";
	private String uploadFile;
	private String newName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edituserinfo);
		waitingDialog = waitingDialog();
		initWidget();
		LoadInfo();
		setListener();
		DownLoadUserImg();
	}

	private void LoadInfo() {
		// TODO Auto-generated method stub
		handler.sendEmptyMessage(0);
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				waitingDialog.show();
				handler.sendEmptyMessage(1);
				break;
			case 1:
				ClientNetwork.Instace().asynGetResponse(
						new RequestUserDetailInfo(
								AccountManager.Instace(mContext).userId),
						new IResponseReceiver() {

							@Override
							public void onResponse(BaseHttpRequest request,
									BaseHttpResponse response) {
								// TODO Auto-generated method stub
								ResponseUserDetailInfo responseUserDetailInfo = (ResponseUserDetailInfo) response;
								if (responseUserDetailInfo.result.equals("211")) {
									UserBasicInfoManager.Instace().responseUserDetailInfo = responseUserDetailInfo;
									System.out
											.println("responseUserDetailInfo=="
													+ responseUserDetailInfo.realname
													+ responseUserDetailInfo.birthday);
									userDetailInfo = responseUserDetailInfo;
								}
								handler.sendEmptyMessage(2);
							}
						});
				break;
			case 2:
				waitingDialog.dismiss();
				setText();
				break;
			case 3:
				editinfo_save_btn.setClickable(true);
				Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				Toast.makeText(mContext, "修改失败，请重新提交", Toast.LENGTH_SHORT)
						.show();
				break;
			case 5:
				// 保存修改的头像

				break;
			case 6:
				editinfo_save_btn.setClickable(true);
				showDialog("头像修改成功");
				// 将本地缓存旧图片删除
				File f = new File("/sdcard/com.iyuba.iyubaclient/image/"
						+ AccountManager.Instace(mContext).userId + ".jpeg");
				f.delete();
				// 下载新头像
				DownLoadUserImg();
				break;
			case 7:
				showDialog("提交失败，请重新上传");
				break;
			case 8:
				showDialog("头像修改失败，请重新上传");
				break;
			default:
				break;
			}
		}
	};
	String success;
	String failure;

	// 上传头像、文件到服务器上
	private void uploadFile() {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		try {
			URL url = new URL(actionUrl
					+ AccountManager.Instace(mContext).userId);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/* 允许Input、Output，不使用Cache */
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			/* 设定传送的method=POST */
			con.setRequestMethod("POST");
			/* setRequestProperty */
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			/* 设定DataOutputStream */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; "
					+ "name=\"file1\";filename=\"" + newName + "\"" + end);
			ds.writeBytes(end);
			/* 取得文件的FileInputStream */
			FileInputStream fStream = new FileInputStream(uploadFile);
			/* 设定每次写入1024bytes */
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			/* 从文件读取数据到缓冲区 */
			while ((length = fStream.read(buffer)) != -1) { /* 将数据写入DataOutputStream中 */
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			/* close streams */
			fStream.close();
			ds.flush();
			/* 取得Response内容 */
			InputStream is = con.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			/* 将Response显示于Dialog */
			success = b.toString().trim();
			JSONObject jsonObject = new JSONObject(success.substring(
					success.indexOf("{"), success.lastIndexOf("}") + 1));
			System.out.println("cc=====" + jsonObject.getString("status"));
			if (jsonObject.getString("status").equals("0")) {// status 为0则修改成功
				handler.sendEmptyMessage(6);
			} else {
				handler.sendEmptyMessage(8);
			}
			/* 关闭DataOutputStream */
			ds.close();
		} catch (Exception e) {
			e.printStackTrace();
			failure = e.getMessage();
			handler.sendEmptyMessage(7);
		}
	}

	/* 显示Dialog的method */
	private void showDialog(String mess) {
		new AlertDialog.Builder(EditUserInfoActivity.this).setTitle("")
				.setMessage(mess)
				.setNegativeButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

	private void setListener() {
		// TODO Auto-generated method stub
		editinfo_back_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		editPortrait.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showListDia();
			}
		});

		editGender.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(GENDER_DIALOG);
			}
		});
		editBirthday.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG);
			}
		});
		editResideLocation.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		editinfo_save_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				editinfo_save_btn.setClickable(false);
				Calendar cal = new GregorianCalendar(editUserInfo
						.getEdBirthYear(), editUserInfo.getEdBirthMonth(),
						editUserInfo.getEdBirthDay());
				String constellation = JudgeZodicaAndConstellation
						.date2Constellation(cal);
				String zodiac = JudgeZodicaAndConstellation.date2Zodica(cal);
				editUserInfo.setEdZodiac(zodiac);
				editUserInfo.setEdConstellation(constellation);
				System.out.println(constellation + "  " + zodiac);
				String value = editUserInfo.getEdGender() + ","
						+ editUserInfo.getEdBirthYear() + ","
						+ editUserInfo.getEdBirthMonth() + ","
						+ editUserInfo.getEdBirthDay() + ","
						+ editUserInfo.getEdConstellation() + ","
						+ editUserInfo.getEdZodiac();

				String key = "gender,birthyear,birthmonth,birthday,constellation,zodiac";

				ClientNetwork.Instace().asynGetResponse(
						new RequestEditUserInfo(AccountManager
								.Instace(mContext).userId, key, value),
						new IResponseReceiver() {

							@Override
							public void onResponse(BaseHttpRequest request,
									BaseHttpResponse response) {
								// TODO Auto-generated method

								ResponseEditUserInfo responseEditUserInfo = (ResponseEditUserInfo) response;
								if (responseEditUserInfo.result.equals("221")) {
									handler.sendEmptyMessage(3);
								} else {
									handler.sendEmptyMessage(4);
								}
							}
						});

				if (isChangePor) {
					handler.sendEmptyMessage(5);// 修改头像
					Thread upload = new Thread() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							super.run();
							uploadFile();
						}
					};
					upload.start();
				}
			}
		});
	}

	private void showListDia() {
		final String[] mList = { "相机拍摄", "手机相册" };
		AlertDialog.Builder listDia = new AlertDialog.Builder(
				EditUserInfoActivity.this);
		listDia.setTitle("选择头像");
		listDia.setItems(mList, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				/* 下标是从0开始的 */
				showClickMessage(which);
			}

		});
		listDia.create().show();
	}

	private void showClickMessage(int which) {
		// TODO Auto-generated method stub
		switch (which) {
		case 0:
			// 相机拍摄
			System.out.println("相机拍摄");
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
					Environment.getExternalStorageDirectory(), "temp.jpg")));
			startActivityForResult(intent, PHOTOHRAPH);
			break;
		case 1:
			// 手机相册中选择
			System.out.println("手机相册中选择");
			Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT, null);
			intent1.setDataAndType(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					IMAGE_UNSPECIFIED);
			intent1.putExtra("xiangce", "xiangce");
			startActivityForResult(intent1, PHOTOZOOM);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == NONE)
			return;
		// 拍照
		if (requestCode == PHOTOHRAPH) {
			// 设置文件保存路径这里放在跟目录下
			File picture = new File(Environment.getExternalStorageDirectory()
					+ "/temp.jpg");
			uploadFile = Environment.getExternalStorageDirectory()
					+ "/temp.jpg";
			startPhotoZoom1(Uri.fromFile(picture));
		}

		if (data == null)
			return;

		// 读取相册缩放图片
		if (requestCode == PHOTOZOOM) {
			startPhotoZoom(data.getData());
		}
		// 处理结果
		if (requestCode == PHOTORESOULT) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0
																		// -100)压缩文件
				iveditPortrait.setImageBitmap(photo);
				isChangePor = true;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	public void startPhotoZoom1(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTORESOULT);
	}
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTORESOULT);
		Bitmap photo = intent.getExtras().getParcelable("data");
		String[] proj = { MediaColumns.DATA };
		Cursor cursor = managedQuery(intent.getData(), proj, null, null, null);
		// 按我个人理解 这个是获得用户选择的图片的索引值
		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		uploadFile = cursor.getString(column_index);
		System.out.println("camera-------- 相册图片的地址--------" + uploadFile);

	}

	public Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		Builder builder = new AlertDialog.Builder(mContext);
		switch (id) {
		case GENDER_DIALOG:
			builder.setTitle("性别");
			builder.setSingleChoiceItems(R.array.gender, 0,
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							String genderString = getResources()
									.getStringArray(R.array.gender)[which];
							editGender.setText(genderString);
							if (genderString.equals("1")) {
								editUserInfo.setEdGender("1");
							} else {
								editUserInfo.setEdGender("2");
							}
						}
					});
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});
			dialog = builder.create();
			break;
		case DATE_DIALOG:
			calendar = Calendar.getInstance();
			dialog = new DatePickerDialog(this,
					new DatePickerDialog.OnDateSetListener() {
						public void onDateSet(DatePicker dp, int year,
								int month, int dayOfMonth) {
							editUserInfo.setEdBirthDay(dayOfMonth);
							editUserInfo.setEdBirthYear(year);
							editUserInfo.setEdBirthMonth(month);
							editBirthday.setText(year + "-" + (month + 1) + "-"
									+ dayOfMonth);
						}
					}, calendar.get(Calendar.YEAR), // 传入年份
					calendar.get(Calendar.MONTH), // 传入月份
					calendar.get(Calendar.DAY_OF_MONTH) // 传入天数
			);
			dialog.setTitle("出生日期");
			break;
		case PORTRAIT_DIALOG:
			builder.setTitle("选择头像");

			dialog = builder.create();
			break;
		case ADDRESS_DIALOG:
			break;
		default:
			break;
		}
		return dialog;

	}

	private void setText() {
		// TODO Auto-generated method stub
		if (userDetailInfo.gender.equals("1")) {
			editGender.setText("男");
		} else if (userDetailInfo.gender.equals("2")) {
			editGender.setText("女");
		}
		editBirthday.setText(userDetailInfo.birthday);
		editResideLocation.setText(userDetailInfo.resideLocation);
		iveditPortrait.setImageBitmap(userBitmap);
	}

	private void initWidget() {
		// TODO Auto-generated method stub
		iveditPortrait = (ImageView) findViewById(R.id.iveditPortrait);
		editGender = (TextView) findViewById(R.id.editGender);
		editBirthday = (TextView) findViewById(R.id.editBirthday);
		editResideLocation = (TextView) findViewById(R.id.editResideLocation);
		editPortrait = (LinearLayout) findViewById(R.id.editPortrait);
		editinfo_back_btn = (Button) findViewById(R.id.editinfo_back_btn);
		editinfo_save_btn = (Button) findViewById(R.id.editinfo_save_btn);
	}

	private void DownLoadUserImg() {
		// TODO Auto-generated method stub
		Thread t = new Thread() {
			public void run() {
				if (SDCardUtil.hasSDCard()) {
					File file = new File(Constant.APP_DATA_PATH
							+ Constant.SDCARD_PORTEAIT_PATH
							+ AccountManager.Instace(mContext).userId + ".jpeg");
					if (file.exists()) {
						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inSampleSize = 2;
						userBitmap = BitmapFactory
								.decodeFile(
										Constant.APP_DATA_PATH
												+ Constant.SDCARD_PORTEAIT_PATH
												+ AccountManager
														.Instace(mContext).userId
												+ ".jpeg", options);
						handler.sendEmptyMessage(5);
					} else {
						DownLoadManager
								.Instace(mContext)
								.downLoadImage(
										Constant.IMAGE_DOWN_PATH
												+ AccountManager
														.Instace(mContext).userId,
										AccountManager.Instace(mContext).userId,
										new DownLoadCallBack() {
											@Override
											public void downLoadSuccess(
													Bitmap image) {
												// TODO Auto-generated method
												// stub
												userBitmap = image;
												handler.sendEmptyMessage(5);
											}

											@Override
											public void downLoadFaild(
													String error) {
												// TODO Auto-generated method
												// stub
											}
										}, true);
					}

				}
			}
		};
		t.start();
	}

	public CustomDialog waitingDialog() {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.wetting_layout, null);
		CustomDialog.Builder customBuilder = new CustomDialog.Builder(this);
		CustomDialog cDialog = customBuilder.setContentView(layout).create();
		return cDialog;
	}
}
