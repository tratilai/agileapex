package com.agileapex;

import java.io.File;
import java.net.URL;
import java.util.Collection;

import com.vaadin.Application;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.ApplicationResource;

public class TestApplicationContext implements ApplicationContext {
    private static final long serialVersionUID = -4613328097498980615L;

    @Override
    public void addTransactionListener(TransactionListener listener) {
    }

    @Override
    public File getBaseDirectory() {
        return null;
    }

    @Override
    public Collection<Application> getApplications() {
        return null;
    }

    @Override
    public void removeTransactionListener(TransactionListener listener) {
    }

    @Override
    public String generateApplicationResourceURL(ApplicationResource resource, String urlKey) {
        return null;
    }

    @Override
    public boolean isApplicationResourceURL(URL context, String relativeUri) {
        return false;
    }

    @Override
    public String getURLKey(URL context, String relativeUri) {
        return null;
    }
}
