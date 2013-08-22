/**
 * 
 */
package com.iyuba.iyubaclient.word.procotol;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ths.frame.network.protocol.BaseHttpResponse;

import com.iyuba.iyubaclient.procotol.base.VOABaseJsonRequestWord;
import com.iyuba.iyubaclient.sqlite.entity.Words;


/**
 * @author yao
 *请求生词本
 */
public class RequestSynWords extends VOABaseJsonRequestWord {
	public static final String protocolCode="40001";
	private String userId;
	private List<Words> words;
	/**
	 * 
	 * @param wordKey
	 */
	public RequestSynWords(String userId,List<Words> words){
		super(protocolCode);
		this.userId=userId;
		this.words=words;
	}

	@Override
	protected JSONObject fillBody(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		jsonObject.put("UserId", userId);
		JSONArray data=new JSONArray();
		if(words!=null&&words.size()!=0){
			for(Words word:words){
				JSONObject wordData=new JSONObject();
				wordData.put("Key", word.key);
				wordData.put("Audio", word.audio);
				wordData.put("Pron", word.pron);
				wordData.put("Def", word.def);
				wordData.put("FromApp", word.fromApp);
				wordData.put("Status", word.status);
				data.put(wordData);
			}
			jsonObject.put("Data", data);
		}
		return jsonObject;
	}

	@Override
	public BaseHttpResponse createResponse() {
		// TODO Auto-generated method stub
		return new ResponseSynWords();
	}

}

