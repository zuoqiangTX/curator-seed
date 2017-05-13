package org.throwable.config;

import java.util.Map;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/4/23 1:28
 */
public interface ConfigurationCenter {

	 void addConfiguration(String key, String value) throws Exception;

	void deleteConfiguration(String key) throws Exception;

	void updateConfiguration(String key, String value) throws Exception;

	String getConfiguration(String key) throws Exception;

	Map<String, String> getAllConfiguration() throws Exception;
}
