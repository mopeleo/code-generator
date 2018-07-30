package org.oversky.code.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertiesUtil {

	public static HashMap<String, String> config = new HashMap<String, String>();
	private static final Pattern PATTERN = Pattern.compile("\\$\\{([^\\}]+)\\}");
	private static ResourceBundle rb = ResourceBundle.getBundle("config");
	
	public static void convert(ResourceBundle properties) {
		HashMap<String, String> tmpConfig = new HashMap<String, String>();
		
		Set<String> keys = properties.keySet();
		for(Iterator<String> i = keys.iterator(); i.hasNext();) {
			String key = i.next();
			String value = properties.getString(key);
			Matcher matcher = PATTERN.matcher(value);
			if(matcher.find()) {
				tmpConfig.put(key, value);
			}else {
				config.put(key, value);
			}
		}
		
		if(tmpConfig.size() > 0) {
			for(Iterator<String> i = tmpConfig.keySet().iterator(); i.hasNext();) {
				String key = i.next();
				String value = loop(tmpConfig.get(key));
				config.put(key, value);
			}
		}
	}
	
	private static String loop(String key){
        //定义matcher匹配其中的路径变量
        Matcher matcher = PATTERN.matcher(key);
        StringBuffer buffer = new StringBuffer();
        boolean flag = false;
        while (matcher.find()) {
            String matcherKey = matcher.group(1);//依次替换匹配到的变量
            String matchervalue = config.get(matcherKey);
            if (matchervalue != null) {
                matcher.appendReplacement(buffer, matcher.quoteReplacement(matchervalue));//quoteReplacement方法对字符串中特殊字符进行转化
                flag = true;
            }
        }
        matcher.appendTail(buffer);
        //flag为false时说明已经匹配不到变量，则不需要再递归查找
        return flag?loop(buffer.toString()):key;
    }
	
	public static void main(String[] args) {
		convert(rb);
		System.out.println(config.get("test2"));
	}
}
