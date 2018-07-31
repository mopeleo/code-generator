package org.oversky.code.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertiesUtil {

	private HashMap<String, String> config = new HashMap<String, String>();
	private HashMap<String, String> varMap = new HashMap<String, String>();
	private static final Pattern PATTERN = Pattern.compile("\\$\\{([^\\}]+)\\}");
	
	public PropertiesUtil (ResourceBundle rb) {
		Set<String> keys = rb.keySet();
		for(Iterator<String> i = keys.iterator(); i.hasNext();) {
			String key = i.next();
			String value = rb.getString(key);
			Matcher matcher = PATTERN.matcher(value);
			if(matcher.find()) {
				varMap.put(key, value);
			}else {
				config.put(key, value);
			}
		}
		
		convert();
	}
	
	public PropertiesUtil (Properties properties) {
		Set<Object> keys = properties.keySet();
		for(Iterator<Object> i = keys.iterator(); i.hasNext();) {
			String key = (String)i.next();
			String value = properties.getProperty(key);
			Matcher matcher = PATTERN.matcher(value);
			if(matcher.find()) {
				varMap.put(key, value);
			}else {
				config.put(key, value);
			}
		}
		
		convert();
	}
	
	public String getValue(String key) {
		return this.config.get(key);
	}
	
	private void convert() {
		if(varMap.size() > 0) {
			for(Iterator<String> i = varMap.keySet().iterator(); i.hasNext();) {
				String key = i.next();
				String value = loop(varMap.get(key));
				config.put(key, value);
			}
		}
	}
	
	private String loop(String key){
        //定义matcher匹配其中的变量
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
}
