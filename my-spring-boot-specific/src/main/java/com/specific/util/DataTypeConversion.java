package com.specific.util;

public class DataTypeConversion {

	private static DataTypeConversion Object = null;  //在类体里面进行对象声明，因为Object是static，所以在程序里只会执行一次 
	
	private DataTypeConversion() {

	}
	public  DataTypeConversion getObject() {
        if (Object == null)              //如果对象Object为null，则new一个对象
            Object = new DataTypeConversion();
        return Object;   //返回创建的对象
	}
	
	public static String getStringByArray(Object[] arr) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			sb.append("," + arr[i]); // append String并不拥有该方法，所以借助StringBuffer
		}
		String str = sb.toString();
		return str.substring(1);
	}
}
