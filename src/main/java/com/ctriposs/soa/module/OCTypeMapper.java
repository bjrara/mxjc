package com.ctriposs.soa.module;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Date;
import java.util.Currency;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import com.sun.xml.xsom.impl.util.Uri;

public class OCTypeMapper {
	/**
	 * java to objc primitive type mapping
	 */
	private static Map<String, String> primitiveMap;

	/**
	 * java.lang to objc type mapping
	 */
	private static Map<String, String> languageMap;

	/**
	 * java.util to objc type mapping
	 */
	private static Map<String, String> utilityMap;

	/**
	 * javax.xml to objc type mapping
	 */
	private static Map<String, String> xmlMap;

	/**
	 * java.net to objc type mapping
	 */
	private static Map<String, String> urlMap;

	/**
	 * java.math to objc type mapping
	 */
	private static Map<String, String> mathMap;

	static {
		initPrimitiveMap();
		initLanguageMap();
		initUtilityMap();
		initXMLMap();
		initUrlMap();
		initMathMap();
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

	private static void initLanguageMap() {
		languageMap = new HashMap<String, String>();

		languageMap.put(Boolean.class.getName(), SoaOCWrapper.NSNUMBER);
		languageMap.put(Integer.class.getName(), SoaOCWrapper.NSNUMBER);
		languageMap.put(Long.class.getName(), SoaOCWrapper.NSNUMBER);
		languageMap.put(Double.class.getName(), SoaOCWrapper.NSNUMBER);
		languageMap.put(Float.class.getName(), SoaOCWrapper.NSNUMBER);
		languageMap.put(Short.class.getName(), SoaOCWrapper.NSNUMBER);
		languageMap.put(Byte.class.getName(), SoaOCWrapper.NSNUMBER);
		languageMap.put(Character.class.getName(), SoaOCWrapper.NSNUMBER);
		languageMap.put(String.class.getName(), SoaOCWrapper.NSSTRING);
	}

	/**
	 * initialize java.util to objc type mapping
	 */
	private static void initUtilityMap() {
		utilityMap = new HashMap<String, String>();

		utilityMap.put(Date.class.getName(), SoaOCWrapper.NSDATE);
		utilityMap.put(Locale.class.getName(), SoaOCWrapper.NSSTRING);
		utilityMap.put(Currency.class.getName(), SoaOCWrapper.NSSTRING);
		utilityMap.put(GregorianCalendar.class.getName(), SoaOCWrapper.NSDATE);
		utilityMap.put(TimeZone.class.getName(), SoaOCWrapper.NSSTRING);
	}

	/**
	 * initialize javax.xml to objc type mapping
	 */
	private static void initXMLMap() {
		xmlMap = new HashMap<String, String>();

		xmlMap.put(XMLGregorianCalendar.class.getName(), SoaOCWrapper.NSDATE);
		xmlMap.put(Duration.class.getName(), SoaOCWrapper.NSDATE);
		xmlMap.put(QName.class.getName(), SoaOCWrapper.NSSTRING);
		xmlMap.put(Object.class.getName(), SoaOCWrapper.NSOBJECT);
	}

	/**
	 * initialize java.net to objc type mapping
	 */
	private static void initUrlMap() {
		urlMap = new HashMap<String, String>();

		urlMap.put(URL.class.getName(), SoaOCWrapper.NSSTRING);
		urlMap.put(Uri.class.getName(), SoaOCWrapper.NSSTRING);
	}

	private static void initMathMap() {
		mathMap = new HashMap<String, String>();

		mathMap.put(BigDecimal.class.getName(), SoaOCWrapper.NSNUMBER);
		mathMap.put(BigInteger.class.getName(), SoaOCWrapper.NSNUMBER);
	}

	public static String lookupPrimitive(String javaType) {
		return primitiveMap.get(javaType);
	}

	public static String lookupNonPrimitive(String javaType) {
		if (languageMap.containsKey(javaType)) {
			return languageMap.get(javaType);
		}

		if (xmlMap.containsKey(javaType)) {
			return xmlMap.get(javaType);
		}

		if (utilityMap.containsKey(javaType)) {
			return utilityMap.get(javaType);
		}

		if (urlMap.containsKey(javaType)) {
			return urlMap.get(javaType);
		}

		if (mathMap.containsKey(javaType)) {
			return mathMap.get(javaType);
		}

		return null;
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
		public static final String NSOBJECT = "NSObject";
		public static final String NSINPUTSTREAM = "NSInputStream";
	}

}
