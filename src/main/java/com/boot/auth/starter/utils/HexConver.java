package com.boot.auth.starter.utils;

/**
 * HexConver
 *
 * @author wangli
 * @since 2019/5/16
 */
public class HexConver {

	public static final String HEX_STR = "0123456789ABCDEF";

	/**
	 * byte数组转换为二进制字符串,每个字节以","隔开
	 * **/
	public static String conver2HexStr(byte [] bytes) {
		StringBuffer result = new StringBuffer();
		for(int i = 0;i<bytes.length;i++) {
			result.append(Long.toString(bytes[i] & 0xff, 2)+",");
		}
		return result.toString().substring(0, result.length()-1);
	}

	/**
	 * 二进制字符串转换为byte数组,每个字节以","隔开
	 * **/
	public static byte[] conver2HexToByte(String hex2Str)
	{
		String [] temp = hex2Str.split(",");
		byte [] b = new byte[temp.length];
		for(int i = 0;i<b.length;i++) {
			b[i] = Long.valueOf(temp[i], 2).byteValue();
		}
		return b;
	}


	/**
	 * byte数组转换为十六进制的字符串
	 * **/
	public static String conver16HexStr(byte [] bytes) {
		StringBuffer result = new StringBuffer();
		for(int i = 0;i<bytes.length;i++) {
			if((bytes[i]&0xff)<0x10) {
				result.append("0");
			}
			result.append(Long.toString(bytes[i]&0xff, 16));
		}
		if(result==null) {
			return null;
		}
		return result.toString().toUpperCase();
	}

	/**
	 * 十六进制的字符串转换为byte数组
	 * **/
	public static byte[] conver16HexToByte(String hex16Str) {
		char [] c = hex16Str.toCharArray();
		byte [] b = new byte[c.length/2];
		for(int i = 0;i < b.length;i++) {
			int pos = i * 2;
			b[i] = (byte)(HEX_STR.indexOf(c[pos]) << 4 | HEX_STR.indexOf(c[pos+1]));
		}
		return b;
	}

//	public static void main(String[] args) {
//		String content = "wangliAA99000";
//		System.out.println("原字符串："+content);
//		String hex2Str = conver2HexStr(content.getBytes());
//		System.out.println("\n转换为二进制的表示形式："+hex2Str);
//		byte [] b = conver2HexToByte(hex2Str);
//		System.out.println("二进制字符串还原："+new String(b));
//
//		String hex16Str = conver16HexStr(content.getBytes());
//		System.out.println("\n转换为十六进制的表示形式:"+ hex16Str);
//		System.out.println("十六进制字符串还原:"+ new String(conver16HexToByte(hex16Str)));
//	}
}
