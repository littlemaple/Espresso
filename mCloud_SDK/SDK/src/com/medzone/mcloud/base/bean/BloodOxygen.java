package com.medzone.mcloud.base.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;

import com.j256.ormlite.field.DatabaseField;

/**
 * 
 * @author Robert.
 * 
 */
public class BloodOxygen extends BaseMeasureData {

	public static final String	TAG_OXY					= "oxy";	// 这个值取决于服务端规则库表对不同模块的类型的定义
	// 这个值取决于服务端规则库表对不同模块的类型的定义

	public static final String	NAME_FIELD_OXYGEN		= "oxygen";
	public static final String	NAME_FIELD_RATE			= "rate";

	public static final int		OXYGEN_STATE_IDEAL		= 1;
	public static final int		OXYGEN_STATE_MISSING	= 2;
	public static final int		OXYGEN_STATE_LOW		= 3;

	@DatabaseField(columnName = NAME_FIELD_OXYGEN)
	private Integer				oxygen;							// oxygen

	@DatabaseField(columnName = NAME_FIELD_RATE)
	private Integer				rate;								// rate

	public BloodOxygen() {
//		setTag(TAG_OXY);
	}

	public Integer getOxygen() {
		return oxygen;
	}

	public void setOxygen(Integer oxy) {
		this.oxygen = oxy;
	}

	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}

	// -----------------------------parse------------------------------------

	public static List<BloodOxygen> createBloodOxygenList(JSONArray jsonArray, Account account) {
		List<BloodOxygen> retList = new ArrayList<BloodOxygen>();
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				retList.add(createBloodOxygen(jsonObj, account));
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return retList;

	}

	public static BloodOxygen createBloodOxygen(JSONObject jo, Account account) {

		BloodOxygen bo = new BloodOxygen();
//		bo.setBelongAccount(account);
		return parse(jo, bo);
	}

	public static BloodOxygen updateBloodOxygen(JSONObject jo, BloodOxygen bo) {
		return parse(jo, bo);
	}

	private static BloodOxygen parse(JSONObject jo, BloodOxygen bo) {
		try {
			if (jo.has("recordid") && !jo.isNull("recordid")) {
				bo.setRecordID(jo.getInt("recordid"));
			}
			if (jo.has("measureuid") && !jo.isNull("measureuid")) {

				String uid = jo.getString("measureuid");
				bo.setMeasureUID(uid);
			}
			if (jo.has("source") && !jo.isNull("source")) {
				bo.setSource(jo.getString("source"));
			}
			if (jo.has("readme") && !jo.isNull("readme")) {
				bo.setReadme(jo.getString("readme"));
			}
			if (jo.has("x") && !jo.isNull("x")) {
				bo.setX(jo.getDouble("x"));
			}
			if (jo.has("y") && !jo.isNull("y")) {
				bo.setY(jo.getDouble("y"));
			}
			if (jo.has("z") && !jo.isNull("z")) {
				bo.setZ(jo.getDouble("z"));
			}
			if (jo.has("state") && !jo.isNull("state")) {
				bo.setAbnormal(jo.getInt("state"));
			}
			if (jo.has("uptime") && !jo.isNull("uptime")) {
//				bo.setUptime(jo.getLong("uptime"));
			}
			if (jo.has("value1") && !jo.isNull("value1")) {
				bo.setOxygen(jo.getInt("value1"));
			}
			if (jo.has("value2") && !jo.isNull("value2")) {
				bo.setRate(jo.getInt("value2"));
			}
//			bo.setStateFlag(STATE_SYNCHRONIZED);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return bo;
	}

	@Override
	public List<Rule> getRulesCollects() {

		if (allRules == null) {
			allRules = new ArrayList<Rule>();
			float[] min1s = new float[] { 95f, 90f, 0f };
			float[] max1s = new float[] { 100f, 94f, 89f };
			String[] results = new String[] { "血氧正常", "氧失饱和状态", "低氧血症" };
			int[] states = new int[] { 1, 2, 3 };
			for (int i = 0; i < results.length; i++) {
				Rule r = new Rule();
				r.setMin1(min1s[i]);
				r.setMax1(max1s[i]);
				r.setMin2(0f);
				r.setMax2(0f);
				r.setMeasureType(TAG_OXY);
				r.setResult(results[i]);
				r.setState(states[i]);
				allRules.add(r);
			}
		}
		return allRules;
	}

	@Override
	public boolean isHealthState() {
		if (getAbnormal() == BloodOxygen.OXYGEN_STATE_IDEAL) {
			return true;
		}
		return false;
	}

}
