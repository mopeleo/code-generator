package org.oversky.code.model;


public class Column {

	private String id;
	private String name;
	private String code;               //格式化后的代码
	private String comment;
	private String datatype;
	private String length;
	private String precision;
	private String defaultValue;       //默认值
	private String mandatory;          //值为1表示不能为空
	private String originCode;         //格式化前原始代码，小写
	private String originDatatype;     //格式化前原数据类型
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getPrecision() {
		return precision;
	}
	public void setPrecision(String precision) {
		this.precision = precision;
	}
	public String getMandatory() {
		return mandatory;
	}
	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}
	public String getOriginCode() {
        return originCode;
    }
    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }
    public String getOriginDatatype() {
        return originDatatype;
    }
    public void setOriginDatatype(String originDatatype) {
        this.originDatatype = originDatatype;
    }
    public String getDefaultValue() {
        return defaultValue;
    }
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
    public boolean equals(Object o){
		if(o == null || !(o instanceof Column)){
			return false;
		}else{
			Column col = (Column)o;
			if(col.getId() == null){
				return false;
			}else{
				return col.getId().equals(id);
			}
		}
	}
}
