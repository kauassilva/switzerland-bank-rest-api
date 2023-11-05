package com.switzerlandbank.api.entities.enums;

public enum Gender {
	
	MALE(1),
	FEMALE(2),
	OTHER(3);
	
	private int code;
	
	private Gender(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
	public static Gender valueOf(int code) {
		for (Gender value : Gender.values()) {
			if (value.getCode() == code) {
				return value;
			}
		}
		throw new IllegalArgumentException("Invalid Gender code!");
	}

}
