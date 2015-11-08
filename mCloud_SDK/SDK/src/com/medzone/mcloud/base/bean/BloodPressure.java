package com.medzone.mcloud.base.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;

/**
 * 
 * @author Robert.
 * 
 */
public class BloodPressure extends BaseMeasureData implements Parcelable {

	/**
	 * 这个值取决于服务端newrules表对不同模块的type的定�?
	 */
	public static final String	TAG							= "bp";
	public static final String	UNIT_KPA					= "kpa";
	public static final String	UNIT_MMHG					= "mmHg";
	public static final String	UNIT_RATE					= "bpm";

	public static final String	NAME_FIELD_HIGH				= "value1";
	public static final String	NAME_FIELD_LOW				= "value2";
	public static final String	NAME_FIELD_RATE				= "value3";

	public static final int		PRESSURE_STATE_LOW			= 1;
	public static final int		PRESSURE_STATE_IDEAL		= 2;
	public static final int		PRESSURE_STATE_NORMAL		= 3;
	public static final int		PRESSURE_STATE_NORMAL_HIGH	= 4;
	public static final int		PRESSURE_STATE_MILD			= 5;
	public static final int		PRESSURE_STATE_MODERATE		= 6;
	public static final int		PRESSURE_STATE_SERIOUS		= 7;

	@DatabaseField
	private Float				value1;

	@DatabaseField
	private Float				value2;

	@DatabaseField
	private Integer				value3;

	public BloodPressure() {
		// setTag(TAG);
	}

	public void setHigh(Float high) {
		this.value1 = high;
	}

	public Float getHigh() {
		return value1;
	}

	public float getHighKPA() {
		if (value1 != null) {
			return BaseMeasureDataUtil.convertmmHg2Kpa(value1.floatValue());
		}
		return BloodPressure.INVALID_ID;
	}

	public void setLow(Float low) {
		this.value2 = low;
	}

	public Float getLow() {
		return value2;
	}

	public float getLowKPA() {
		if (value2 != null) {
			return BaseMeasureDataUtil.convertmmHg2Kpa(value2.floatValue());
		}
		return BloodPressure.INVALID_ID;
	}

	public Integer getRate() {
		return value3;
	}

	public void setRate(Integer rate) {
		this.value3 = rate;
	}

	@Override
	public boolean isHealthState() {
		if (getAbnormal() == BloodPressure.PRESSURE_STATE_NORMAL || getAbnormal() == BloodPressure.PRESSURE_STATE_IDEAL || getAbnormal() == BloodPressure.PRESSURE_STATE_NORMAL_HIGH) {
			return true;
		}
		else {
			return false;
		}
	}

	public String getPressureUnit(boolean isKpa) {
		if (isKpa) {
			return UNIT_KPA;
		}
		else {
			return UNIT_MMHG;
		}
	}

	public String getRateUnit() {
		return UNIT_RATE;
	}

	@Override
	public List<Rule> getRulesCollects() {
		if (allRules == null) {
			allRules = new ArrayList<Rule>();

			float[] min1s = new float[] { 0, 0, 90, 120, 0, 130, 0, 140, 0, 0, 160, 0, 180 };
			float[] max1s = new float[] { 89, 89, 119, 129, 119, 139, 129, 159, 139, 159, 179, 0, 0 };

			float[] min2s = new float[] { 0, 60, 0, 0, 80, 0, 85, 0, 90, 100, 0, 110, 0 };
			float[] max2s = new float[] { 59, 79, 79, 84, 84, 89, 89, 99, 99, 109, 109, 0, 109 };
			String[] results = new String[] { "偏低", "理想", "理想", "正常", "正常", "正常偏高", "正常偏高", "轻度", "轻度", "中度", "中度", "重度", "重度" };
			int[] states = new int[] { 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7 };
			for (int i = 0; i < results.length; i++) {
				Rule r = new Rule();
				r.setMin1(min1s[i]);
				r.setMax1(max1s[i]);
				r.setMin2(min2s[i]);
				r.setMax2(max2s[i]);
				r.setMeasureType(TAG);
				r.setResult(results[i]);
				r.setState(states[i]);
				allRules.add(r);
			}
		}
		return allRules;
	}

	public static List<BloodPressure> createBloodPressureList(JSONArray jsonArray, Account account) {
		List<BloodPressure> retList = new ArrayList<BloodPressure>();
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				retList.add(createBloodPressure(jsonObj, account));
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return retList;
	}

	public static BloodPressure createBloodPressure(JSONObject jo, Account account) {

		BloodPressure bp = new BloodPressure();
		// bp.setBelongAccount(account);

		return parse(jo, bp);
	}

	public static BloodPressure updateBloodPressure(JSONObject jo, BloodPressure bp) {

		return parse(jo, bp);
	}

	private static BloodPressure parse(JSONObject jo, BloodPressure bp) {
		try {
			if (jo.has("recordid") && !jo.isNull("recordid")) {
				bp.setRecordID(jo.getInt("recordid"));
			}
			if (jo.has("measureuid") && !jo.isNull("measureuid")) {

				String uid = jo.getString("measureuid");
				bp.setMeasureUID(uid);

			}
			if (jo.has("source") && !jo.isNull("source")) {
				bp.setSource(jo.getString("source"));
			}
			if (jo.has("readme") && !jo.isNull("readme")) {
				bp.setReadme(jo.getString("readme"));
			}
			if (jo.has("x") && !jo.isNull("x")) {
				bp.setX(jo.getDouble("x"));
			}
			if (jo.has("y") && !jo.isNull("y")) {
				bp.setY(jo.getDouble("y"));
			}
			if (jo.has("z") && !jo.isNull("z")) {
				bp.setZ(jo.getDouble("z"));
			}
			if (jo.has("state") && !jo.isNull("state")) {
				bp.setAbnormal(jo.getInt("state"));
			}
			if (jo.has("uptime") && !jo.isNull("uptime")) {
				// bp.setUptime(jo.getLong("uptime"));
			}
			if (jo.has("value1") && !jo.isNull("value1")) {
				bp.setHigh((float) jo.getDouble("value1"));
			}
			if (jo.has("value2") && !jo.isNull("value2")) {
				bp.setLow((float) jo.getDouble("value2"));
			}
			if (jo.has("value3") && !jo.isNull("value3")) {
				bp.setRate(jo.getInt("value3"));
			}
			// bp.setStateFlag(STATE_SYNCHRONIZED);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return bp;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeFloat(value1 != null ? value1 : 0);
		dest.writeFloat(value2 != null ? value2 : 0);
		dest.writeInt(value3 != null ? value3 : 0);
		dest.writeString(getSource());
		dest.writeString(getMeasureUID());
		// if (getStateFlag() != null) {
		// dest.writeInt(getStateFlag());
		// }
		// else {
		// dest.writeInt(BloodPressure.STATE_NOT_SYNCHRONIZED);
		// }
		// if (getActionFlag() != null) {
		// dest.writeInt(getActionFlag());
		// }
		// else {
		// dest.writeInt(BloodPressure.ACTION_ADD_RECORD);
		// }
		dest.writeDouble(getX() != null ? getX() : 0);
		dest.writeDouble(getY() != null ? getY() : 0);
		dest.writeDouble(getZ() != null ? getZ() : 0);
	}

	private BloodPressure(Parcel source) {
		value1 = source.readFloat();
		value2 = source.readFloat();
		value3 = source.readInt();
		setSource(source.readString());
		setMeasureUID(source.readString());
		// setStateFlag(source.readInt());
		// setActionFlag(source.readInt());
		setX(source.readDouble());
		setY(source.readDouble());
		setZ(source.readDouble());
	}

	public static final Creator<BloodPressure>	CREATOR	= new Creator<BloodPressure>() {

															@Override
															public BloodPressure[] newArray(int size) {
																return new BloodPressure[size];
															}

															@Override
															public BloodPressure createFromParcel(Parcel source) {
																return new BloodPressure(source);
															}
														};

}
