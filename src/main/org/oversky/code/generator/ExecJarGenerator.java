package org.oversky.code.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ExecJarGenerator extends CodeGenerator{

    private Properties prop = null;
    
    public ExecJarGenerator(){
        prop = new Properties();
        try {
//            System.out.println(System.getProperty("user.dir") + File.separator + "config.properties");
            InputStream in = new FileInputStream(new File(System.getProperty("user.dir") + File.separator + "config.properties"));
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }        
    }
    
    @Override
    protected String getConfig(String key) {
        return prop.getProperty(key);
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
