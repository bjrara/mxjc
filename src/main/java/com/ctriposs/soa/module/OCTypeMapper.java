package com.ctriposs.soa.module;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.datatype.Duration;
import javax.xml.namespace.QName;

import com.leansoft.mxjc.module.pico.PicoType;

public class OCTypeMapper {
	private static Map<String, String> primitiveMap;
	private static Map<String, String> wrapperMap;

	static {
		initPrimitiveMap();
		initWrapperMap();
	}

	private static void initPrimitiveMap() {
		primitiveMap = new HashMap<String, String>();

		primitiveMap.put(int.class.getName(), SoaOCWrapper.INT);
		primitiveMap.put(boolean.class.getName(), SoaOCWrapper.BOOL);
		primitiveMap.put(byte.class.getName(), SoaOCWrapper.BYTE);
		primitiveMap.put(char.class.getName(), SoaOCWrapper.CHAR);
		primitiveMap.put(short.class.getName(), SoaOCWrapper.SHORT);
		primitiveMap.put(long.class.getName(), SoaOCWrapper.LONG);
		primitiveMap.put(float.class.getName(), SoaOCWrapper.FLOAT);
		primitiveMap.put(double.class.getName(), SoaOCWrapper.DOUBLE);
	}

	private static void initWrapperMap() {
		wrapperMap = new HashMap<String, String>();

		wrapperMap.put(Integer.class.getName(), SoaOCWrapper.NSNUMBER);
		wrapperMap.put(Boolean.class.getName(), SoaOCWrapper.NSNUMBER);
		wrapperMap.put(Byte.class.getName(), SoaOCWrapper.NSNUMBER);
		wrapperMap.put(Character.class.getName(), SoaOCWrapper.NSNUMBER);
		wrapperMap.put(Short.class.getName(), SoaOCWrapper.NSNUMBER);
		wrapperMap.put(Long.class.getName(), SoaOCWrapper.NSNUMBER);
		wrapperMap.put(Float.class.getName(), SoaOCWrapper.NSNUMBER);
		wrapperMap.put(Double.class.getName(), SoaOCWrapper.NSNUMBER);
		wrapperMap.put(Date.class.getName(), SoaOCWrapper.NSDATE);
		wrapperMap.put(String.class.getName(), SoaOCWrapper.NSSTRING);
		wrapperMap.put("byte[]", SoaOCWrapper.NSDATA);
		wrapperMap.put(QName.class.getName(), SoaOCWrapper.NSSTRING);
		wrapperMap.put(Duration.class.getName(), SoaOCWrapper.NSSTRING);
	}

	public static String lookupPrimitive(String javaType) {
		return primitiveMap.get(javaType);
	}

	public static String lookupWrapper(String javaType) {
		return wrapperMap.get(javaType);
	}

	public class SoaOCWrapper {
		public static final String INT = "int";
		public static final String BOOL = "BOOL";
		public static final String BYTE = "Byte";
		public static final String CHAR = "char";
		public static final String SHORT = "short int";
		public static final String LONG = "long int";
		public static final String FLOAT = "float";
		public static final String DOUBLE = "double";
		public static final String ENUM = "enum";
		public static final String NSNUMBER = "NSNumber";
		public static final String NSSTRING = "NSString";
		public static final String NSDATE = "NSDate";
		public static final String NSDATA = "NSData";
		public static final String NSINPUTSTREAM = "NSInputStream";
	}

}
