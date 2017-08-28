package com.agileapex.session;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.domain.User;
import com.vaadin.Application;
import com.vaadin.service.ApplicationContext.TransactionListener;

/**
 * Holds data for one user session. Uses ThreadLocal pattern.
 */
public class ApplicationSession implements TransactionListener, Serializable {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationSession.class);
    private static final long serialVersionUID = -6183704967677119437L;
    private Application application;
    private SessionDataHelper sessionDataHelper;
    private static String pid;
    
    private static ThreadLocal<ApplicationSession> instance = new ThreadLocal<ApplicationSession>();

    public ApplicationSession(Application application) {
        logger.debug("About to create user session in the ThreadLocal.");
        this.application = application;
        instance.set(this);
    }

    @Override
    public void transactionStart(Application application, Object transactionData) {
        if (this.application == application) {
            instance.set(this);
        }
    }

    @Override
    public void transactionEnd(Application application, Object transactionData) {
        if (this.application == application) {
            instance.set(null);
        }
    }

    public static SessionDataHelper getSessionDataHelper() {
        return instance.get().sessionDataHelper;
    }

    public static void setSessionDataHelper(SessionDataHelper sessionData) {
        if (instance.get() != null) {
            instance.get().sessionDataHelper = sessionData;
        }
    }

    public static User getUser() {
        return (User) instance.get().application.getUser();
    }

    public static void setUser(User user) {
        if (instance.get() != null) {
            instance.get().application.setUser(user);
        }
    }

    public static Application getApplication() {
        return instance.get().application;
    }

    public static void setApplication(Application application) {
        if (instance.get() != null) {
            instance.get().application = application;
        }
    }

	public static String getPid() {
		return pid;
	}

	public static void setPid(String pidPar) {
		pid = pidPar;
	}
}
