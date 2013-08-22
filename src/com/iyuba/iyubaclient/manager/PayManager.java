package com.iyuba.iyubaclient.manager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.ths.frame.network.ClientNetwork;
import org.ths.frame.network.IResponseReceiver;
import org.ths.frame.network.protocol.BaseHttpRequest;

import com.iyuba.iyubaclient.feed.procotol.RequestNewInfo;
import com.iyuba.iyubaclient.feed.procotol.ResponseNewInfo;

import com.iyuba.iyubaclient.pay.procotol.RequestPayVip;
import com.iyuba.iyubaclient.pay.procotol.ResponsePayVip;

import org.ths.frame.network.protocol.BaseHttpResponse;
import android.content.Context;
import android.view.View;

public class PayManager {
	private static PayManager instance;
	private static Context mContext;
	private int pay_amount;
	private Date lasttime;

	public static PayManager Instance(Context mContext) {
		if (instance == null) {
			instance = new PayManager(mContext);
		}
		PayManager.mContext = mContext;
		return instance;
	}

	private PayManager(Context mContext) {
	}

	public void checkPaidRecord(final String userId,
			final OnResultListener resultListener) {

	/*	ClientSession.Instace().asynGetResponse(new PayRecordRequest(userId),
				new IResponseReceiver() {

					@Override
					public void onResponse(BaseHttpResponseOld response,
							BaseHttpRequestOld request, int rspCookie) {
						PayRecordResponse payRecordResponse = (PayRecordResponse) response;
						if (payRecordResponse.result.equals("1"))// 已付费，把记录插入数据库
						{
							resultListener.OnSuccessListener(null);
						} else {// 未付费，到付费请�?
							resultListener.OnFailureListener(null);
						}

					}
				}, null, null);*/
	}

	/**
	 * 
	 * @param userId
	 * @param resultListener
	 *            功能：查询用户余�?
	 */
	public void checkAmount(final String userId,
			final OnResultListener resultListener) {
		/*ClientSession.Instace().asynGetResponse(new CheckAmountRequest(userId),
				new IResponseReceiver() {
					@Override
					public void onResponse(BaseHttpResponseOld response,
							BaseHttpRequestOld request, int rspCookie) {
						CheckAmountResponse checkAmountResponse = (CheckAmountResponse) response;
						if (checkAmountResponse.result.equals("0"))// 查询失败，提示错误信�?
						{
							resultListener
									.OnSuccessListener(checkAmountResponse.msg);
						} else {//  查询成功，显示余额
							// 刷新余额提示
							resultListener
									.OnSuccessListener(checkAmountResponse.amount);
						}
					}
				}, null, null);*/
	}


	/**
	 * 
	 * @param userId
	 *            AccountManager.Instace(mContext).userId
	 * @param amount
	 *            10
	 * @param year
	 * @param resultListener
	 *            功能：
	 */
	public void payAmount(final String userId, final String amount, final String productId,
			final OnResultListener resultListener) {
		ClientNetwork.Instace().asynGetResponse(
				new RequestPayVip(
						AccountManager.Instace(mContext).userId,amount,productId), new IResponseReceiver() {

					@Override
					public void onResponse(BaseHttpRequest request,
							BaseHttpResponse response) {
						ResponsePayVip responsePayVip=(ResponsePayVip)response;
						if (responsePayVip.result.equals("1"))// 支付成功
						{
							pay_amount = Integer.parseInt(amount);
							getrecord();
							resultListener
									.OnSuccessListener(responsePayVip.amount);
						} else {// 支付失败
							if (Integer.parseInt(responsePayVip.amount) < Integer
									.parseInt(amount))// 余额不足
							{// 提示用户余额不足，并跳转到充值页�?
								resultListener
										.OnFailureListener(responsePayVip.amount);
							} else {
								resultListener
										.OnFailureListener(responsePayVip.msg);
							}

						}
					}
				});
	/*	ClientSession.Instace().asynGetResponse(new PayRequest(userId, amount),
				new IResponseReceiver() {
					@Override
					public void onResponse(BaseHttpResponseOld response,
							BaseHttpRequestOld request, int rspCookie) {
						PayResponse payResponse = (PayResponse) response;

						if (payResponse.result.equals("1"))// 支付成功
						{
							pay_amount = Integer.parseInt(amount);
							getrecord();
							resultListener
									.OnSuccessListener(payResponse.amount);
						} else {// 支付失败
							if (Integer.parseInt(payResponse.amount) < Integer
									.parseInt(amount))// 余额不足
							{// 提示用户余额不足，并跳转到充值页�?
								resultListener
										.OnFailureListener(payResponse.amount);
							} else {
								resultListener
										.OnFailureListener(payResponse.msg);
							}

						}
					}
				}, null, null);*/
	}

	private void getrecord() {
		/*String userName = AccountManager.Instace(mContext).userName;
		User user;
		UserOp userOp = new UserOp(mContext);
		user = userOp.selectData(userName);
		if (user == null) {
			user = new User(userName);
			userOp.saveData(user);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			lasttime = sdf.parse(user.deadline);
		} catch (ParseException e) {
		}
		Calendar c = Calendar.getInstance();
		c.setTime(lasttime);
		if (lasttime.before(new User().getNetTime()) && user.isvip == 1) {
			struct_user.restruct(mContext, pay_amount);
		} else {
			struct_user.struct_by_deadline(mContext, pay_amount, c);
		}*/
	}
}