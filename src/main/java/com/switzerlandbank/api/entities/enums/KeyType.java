package com.switzerlandbank.api.entities.enums;

public enum KeyType {
	
	CPF(1),
	EMAIL(2),
	RANDOM(3);
	
	private int code;
	
	private KeyType(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
	public static KeyType valueOf(int code) {
		for (KeyType value : KeyType.values()) {
			if (value.getCode() == code) {
				return value;
			}
		}
		throw new IllegalArgumentException("Invalid KeyType code!");
	}

}
