package com.medzone.mcloud.base.bean;

import java.util.List;

import com.j256.ormlite.field.DatabaseField;

/**
 * 
 * @author Robert.
 * 
 */
public abstract class BaseMeasureData implements IRuleDetails {

	public static final String	FIELD_FOREIGN_NAME_MASTER_CONTACT_PERSON_ID	= "master_contact_person_id";

	public static final String	NAME_FIELD_MEASURETIME_HELP					= "measureTimeHelp";
	public static final String	NAME_FIELD_MEASURETIME						= "measureTime";
	public static final String	NAME_FIELD_MEASUREU_ID						= "measureUID";
	public static final String	NAME_FIELD_RECORD_ID						= "recordID";
	public static final String	NAME_FIELD_README							= "readme";
	public static final String	NAME_FIELD_SOURCE							= "source";
	public static final String	NAME_FIELD_X								= "x";
	public static final String	NAME_FIELD_Y								= "y";
	public static final String	NAME_FIELD_Z								= "z";
	public static final String	NAME_FIELD_ABNORMAL							= "abnormal";
	public static final String	NAME_FIELD_DATA_CREATE_ID					= "dataCreateID";
	public static final String	NAME_FIELD_IS_TEST_CREATE_DATA				= "isTestCreateData";

	public static final int		INVALID_ID									= -1;
	// ------------------------------------其他属性-----------------------------------------------
	protected List<Rule>		allRules;

	// ------------------------------------库表字段-----------------------------------------------

	@DatabaseField(columnName = NAME_FIELD_MEASURETIME)
	private Long				measureTime;

	@DatabaseField(columnName = NAME_FIELD_MEASUREU_ID)
	private String				measureUID;

	@DatabaseField(columnName = NAME_FIELD_MEASURETIME_HELP)
	private String				measureTimeHelp;

	@DatabaseField(columnName = NAME_FIELD_README)
	private String				readme;

	@DatabaseField(columnName = NAME_FIELD_RECORD_ID)
	private Integer				recordID;

	@DatabaseField(columnName = NAME_FIELD_SOURCE)
	private String				source;

	@DatabaseField(columnName = NAME_FIELD_X)
	private Double				x;

	@DatabaseField(columnName = NAME_FIELD_Y)
	private Double				y;

	@DatabaseField(columnName = NAME_FIELD_Z)
	private Double				z;

	@DatabaseField(columnName = NAME_FIELD_ABNORMAL)
	private Integer				abnormal;

	@DatabaseField(columnName = NAME_FIELD_DATA_CREATE_ID)
	private Integer				dataCreateID;																// 数据创建ID，通常都是自己,云端返回

	@DatabaseField(defaultValue = "0", columnName = NAME_FIELD_IS_TEST_CREATE_DATA)
	private Boolean				isTestCreateData;															// 是否是代测产生的数据

	// ------------------------------------------------Setter/Getter-------------------------

	public Long getMeasureTime() {
		return this.measureTime;
	}

	/**
	 * 解决measureTime与MeasureUid解析出来的时间不一致的问题。
	 */
	private void forceConvertMeasureTime() {
		if (this.measureUID != null) {
			this.measureTime = BaseMeasureDataUtil.parseUID2Millisecond(measureUID);
			this.measureTimeHelp = BaseMeasureDataUtil.parseUID2DateString(measureUID);
		}
	}

	public String getMeasureTimeHelp() {
		return measureTimeHelp;
	}

	public String getMeasureUID() {
		return measureUID;
	}

	public void setMeasureUID(String measureUID) {
		this.measureUID = measureUID;
		forceConvertMeasureTime();
	}

	public String getReadme() {
		return readme;
	}

	public void setReadme(String readme) {
		this.readme = readme;
	}

	public Integer getRecordID() {
		return recordID;
	}

	public void setRecordID(Integer recordID) {
		this.recordID = recordID;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	public Double getZ() {
		return z;
	}

	public void setZ(Double z) {
		this.z = z;
	}

	public Integer getAbnormal() {
		return abnormal;
	}

	public void setAbnormal(Integer abnormal) {
		this.abnormal = abnormal;
	}

	public Integer getDataCreateID() {
		return dataCreateID;
	}

	/**
	 * 数据是由谁创建的，通常情况下均为自己。
	 * 
	 * @param dataCreateID
	 */
	public void setDataCreateID(Integer dataCreateID) {
		this.dataCreateID = dataCreateID;
	}

	public boolean isTestCreateData() {
		return isTestCreateData;
	}

	public void setTestCreateData(boolean isTestCreateData) {
		this.isTestCreateData = isTestCreateData;
	}

	/**
	 * 根据规则判断测量数据是否为健康状态，一般情况下健康含理想，正常。
	 * 
	 * @return boolean
	 */
	public abstract boolean isHealthState();

	public boolean isSameRecord(Object o) {
		BaseMeasureData item2 = (BaseMeasureData) o;
		if (!this.getMeasureUID().equals(item2.getMeasureUID())) {
			return false;
		}
		return true;
	}

}
