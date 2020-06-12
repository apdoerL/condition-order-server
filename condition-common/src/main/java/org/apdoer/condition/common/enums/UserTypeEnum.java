package org.apdoer.condition.common.enums;

public enum UserTypeEnum {
	
	NORMAL(0, "普通用户"),
	OPERATION(1, "运营"),
	MINT_INITIAL(2, "创世"),
	ORGANIZATION(3, "机构"),
	PRIVATE_PLACEMENT(4, "私募"),
	TEAM(5, "团队"),
	FUNDS(6, "基金"),
	BROKER(7, "券商用户"),
	SON(8, "券商子用户"),
	BROKER_OPERATION(9, "券商运营中心"),
	OPERATION_SON(10, "券商经纪商");
	
	private UserTypeEnum(Integer code, String title) {
		this.code = code;
		this.title = title;
	}

	private Integer code;
	private String title;

	public Integer getCode() {
		return code;
	}

	public String getTitle() {
		return title;
	}
	
	public static UserTypeEnum getByCode(Integer code) {
		for (UserTypeEnum e : UserTypeEnum.values()) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}

	public static String getTitle(Integer code) {
		UserTypeEnum e = getByCode(code);
		return e == null ? null : e.getTitle();
	}

	public static Boolean vaild(Integer field) {
		if (null == getByCode(field)) {
			return false;
		}
		return true;
	}
}
