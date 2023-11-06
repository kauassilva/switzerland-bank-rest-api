package com.switzerlandbank.api.resources.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ErrorMapGenerator {
	
	public static Map<String, String> generateErrorGenerator(BindingResult bindingResult) {
		Map<String, String> errorMap = new HashMap<>();
		for (FieldError error : bindingResult.getFieldErrors()) {
			errorMap.put(error.getField(), error.getDefaultMessage());
		}
		return errorMap;
	}
	
	public static String formatErrorMap(Map<String, String> errorMap) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		errorMap.forEach((key, value) -> {
			sb.append(key);
			sb.append(": ");
			sb.append(value);
			sb.append(", ");
		});
		// Remove the last comma and space
		if (!errorMap.isEmpty()) {
			sb.setLength(sb.length() - 2);
		}
		sb.append("}");
		return sb.toString();
	}

}
