package com.agileapex.common.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.AgileApexException;

public class PropertyHelper {
    private static final Logger logger = LoggerFactory.getLogger(PropertyHelper.class);
    private static final String PUBLIC_PROPERTIES_FILE = "/agile_apex.properties";
    private static final String INTERNAL_PROPERTIES_FILE = "/internal.properties";
    private static Properties publicProperties;
    private static Properties internalProperties;

    public static <T> T getPublicProp(String key, Class<T> returnType) {
        if (publicProperties == null) {
            loadProperties();
        }
        String value = publicProperties.getProperty(key);
        logger.debug("Getting public property {}={} with return type: {}", key, value, returnType);
        if (returnType.equals(String.class)) {
            return returnType.cast(value);
        } else if (returnType.equals(Integer.class)) {
            return returnType.cast(Integer.parseInt(value));
        } else if (returnType.equals(Long.class)) {
            return returnType.cast(Long.parseLong(value));
        } else if (returnType.equals(Boolean.class)) {
            return returnType.cast(Boolean.parseBoolean(value));
        } else {
            String message = "Unsupported class type. return type: " + returnType + " key: " + key;
            logger.error(message);
            throw new AgileApexException(message);
        }
    }

    public static <T> T getInternalProperty(String key, Class<T> returnType) {
        if (internalProperties == null) {
            loadProperties();
        }
        String value = internalProperties.getProperty(key);
        logger.debug("Getting internal property {}={} with return type: {}", key, value, returnType);
        if (returnType.equals(String.class)) {
            return returnType.cast(value);
        } else if (returnType.equals(Integer.class)) {
            return returnType.cast(Integer.parseInt(value));
        } else if (returnType.equals(Long.class)) {
            return returnType.cast(Long.parseLong(value));
        } else if (returnType.equals(Boolean.class)) {
            return returnType.cast(Boolean.parseBoolean(value));
        } else {
            String message = "Unsupported class type. return type: " + returnType + " key: " + key;
            logger.error(message);
            throw new AgileApexException(message);
        }
    }

    private static void loadProperties() {
        publicProperties = loadPropertiesFile(PUBLIC_PROPERTIES_FILE);
        internalProperties = loadPropertiesFile(INTERNAL_PROPERTIES_FILE);
    }

    private static Properties loadPropertiesFile(String fileName) {
        logger.debug("About to load properties from file. File name: {}", fileName);
        Properties properties = new Properties();
        try {
            InputStream stream = PropertyHelper.class.getResourceAsStream(fileName);
            properties.load(stream);
            logger.debug("Properties file loaded. Number of properties: {}", properties.size());
            stream.close();
        } catch (IOException e) {
            String message = "Could not load properties from file: " + fileName;
            logger.error(message);
            throw new AgileApexException(message, e);
        }
        return properties;
    }
}
