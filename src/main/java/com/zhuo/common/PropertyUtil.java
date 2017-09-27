package com.zhuo.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zzx
 * @time 2017年9月27日
 * @Desc 读取配置文件
 *
 */
public class PropertyUtil {
	private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);
	private static Properties properties;
	static {
		loadProps();
	}

	synchronized static private void loadProps() {
		logger.info("开始加载properties文件......");
		properties = new Properties();
		InputStream in = null;
		String file = "/weburl.properties";
		in = PropertyUtil.class.getResourceAsStream(file);
		if (in != null) {
			try {
				logger.info("读取文件: " + file);
				properties.load(in);
			} catch (IOException e) {
				logger.error("加载文件异常");
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					logger.info(file+"文件流关闭异常");
				}
			}
		} else {
			logger.error("没有找到文件: " + file);
		}
	}
	
	public static String getProperty(String key) {
		if(null == properties) {
			loadProps();
		}
		return properties.getProperty(key);
	}
	
	public static String getProperty(String key, String defaultValue) {
		if(null == properties) {
			loadProps();
		}
		return properties.getProperty(key, defaultValue);
	}
}
