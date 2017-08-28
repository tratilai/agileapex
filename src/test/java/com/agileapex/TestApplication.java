package com.agileapex;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.Application;
import com.vaadin.service.ApplicationContext;

public class TestApplication extends Application {
    private static final long serialVersionUID = -5451432140909122251L;
    private static final Logger logger = LoggerFactory.getLogger(TestApplication.class);

    public TestApplication() {
        ApplicationContext applicationContext = new TestApplicationContext();
        try {
            start(new URL("http://localhost:8080/apex/"), null, applicationContext);
        } catch (MalformedURLException e) {
            logger.error("Test application URL is not good.", e);
        }
    }

    @Override
    public void init() {
    }
}
