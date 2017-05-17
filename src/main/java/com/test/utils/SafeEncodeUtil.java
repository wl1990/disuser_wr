package com.test.utils;

import org.apache.commons.lang3.StringUtils;
import org.owasp.esapi.ESAPI;


public class SafeEncodeUtil {
	
	public static String safeEncoder(String param){
		//安全编码
		String safeString = ESAPI.encoder().encodeForHTML(param);
		//汉字转码
		if(StringUtils.isNotBlank(safeString)){
			StringBuilder sb = new StringBuilder();
			String charFileds[] = safeString.split(";");
			if(charFileds.length > 1){
				for(int i=0;i<charFileds.length;i++){
					String filed = charFileds[i];
					if(filed.contains("&#x")){
						try {
							String prefix = filed.substring(0, filed.indexOf("&#x"));
							String tailfix = filed.substring(filed.indexOf("&#x")+3, filed.length());
							int ic = Integer.parseInt(tailfix, 16);
							if(ic >= 19968 && ic <= 40869){
								sb.append(prefix).append((char)ic);
							}else{
								sb.append(filed).append(";");
							}
						} catch (NumberFormatException e) {
							sb.append(filed).append(";");
						}
					}else{
						sb.append(filed).append(";");
					}
				}
				if(sb.toString().endsWith(";") && !safeString.endsWith(";")){
					sb = sb.delete(sb.length()-1, sb.length());
				}
				safeString = sb.toString();
			}
		}
		return safeString;
	}
	
	public static void main(String[] args) {
		System.out.println(safeEncoder("werrf中文erertf<html><script></script></html>"));
	}
}
