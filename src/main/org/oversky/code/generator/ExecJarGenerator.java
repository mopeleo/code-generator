package org.oversky.code.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.oversky.code.util.PropertiesUtil;

public class ExecJarGenerator extends CodeGenerator{

    @Override
    protected String getConfig(String key) {
        if(util == null){
        	Properties prop = new Properties();
            try {
//                System.out.println(System.getProperty("user.dir") + File.separator + "config.properties");
                InputStream in = new FileInputStream(new File(System.getProperty("user.dir") + File.separator + "config.properties"));
                prop.load(in);
                util = new PropertiesUtil(prop);
            } catch (IOException e) {
                e.printStackTrace();
            }        
        }
        return util.getValue(key);
    }

    @Override
    protected String getClassPath(){
        return System.getProperty("user.dir");
    }
	
	public static void main(String[] args) {
	    ExecJarGenerator gen = new ExecJarGenerator();
        gen.projectGenerate(gen.getModelFile());
	}
}
