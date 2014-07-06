package com.ctriposs.soa.module;

import java.util.HashMap;
import java.util.Map;

import com.leansoft.mxjc.module.pico.PicoType;

public class OCTypeMapper {
	private static Map<String, String> mapping;

	static {
		initMapping();
	}

	private static void initMapping() {
		mapping = new HashMap<String, String>();

		mapping.put(PicoType.INTEGER, SoaOCWrapper.INT);
		mapping.put(PicoType.BOOL, SoaOCWrapper.BOOL);
		mapping.put(PicoType.BYTE, SoaOCWrapper.BYTE);
		mapping.put(PicoType.CHAR, SoaOCWrapper.CHAR);
		mapping.put(PicoType.SHORT, SoaOCWrapper.SHORT);
		mapping.put(PicoType.LONG, SoaOCWrapper.LONG);
		mapping.put(PicoType.FLOAT, SoaOCWrapper.FLOAT);
		mapping.put(PicoType.DOUBLE, SoaOCWrapper.DOUBLE);
		mapping.put(PicoType.ENUM, SoaOCWrapper.ENUM);
		mapping.put(PicoType.DATE, SoaOCWrapper.NSDATE);
		mapping.put(PicoType.STRING, SoaOCWrapper.NSSTRING);
		mapping.put(PicoType.DATA, SoaOCWrapper.NSDATA);
		mapping.put(PicoType.QNAME, SoaOCWrapper.NSSTRING);
	}

	public static String lookupWrapper(String primType) {
		return mapping.get(primType);
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
