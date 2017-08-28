package com.agileapex;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.common.user.UserHelper;
import com.agileapex.domain.User;
import com.agileapex.persistence.UserPersistence;
import com.agileapex.persistence.UserPersistenceImpl;
import com.agileapex.persistence.db.AdminDatabaseHelper;
import com.agileapex.persistence.db.ClientSchemaDatabaseHelper;
import com.agileapex.session.ApplicationSession;
import com.agileapex.session.SessionDataHelper;
import com.agileapex.ui.window.MainWindow;
import com.agileapex.ui.window.WindowIdentification;
import com.vaadin.Application;
import com.vaadin.terminal.ParameterHandler;

import net.sf.ehcache.CacheManager;

public class AgileApexApplication extends Application implements ParameterHandler {
    private static final Logger logger = LoggerFactory.getLogger(AgileApexApplication.class);
    private static final long serialVersionUID = 8680468286444184105L;
    private MainWindow mainWindow;

    @Override
    public void init() {
        logger.debug("About to init the application.");
        createCaches();
        AdminDatabaseHelper.initialize();
        ClientSchemaDatabaseHelper.initialize();
        setTheme("apextheme");
        mainWindow = new MainWindow();
        mainWindow.addParameterHandler(this);
        setMainWindow(mainWindow);
        ApplicationSession session = new ApplicationSession(this);
        getContext().addTransactionListener(session);
        logger.debug("Application init done.");
    }

    private void createCaches() {
        logger.debug("Creating caches, which are not already created.");
        if (!CacheManager.getInstance().cacheExists("userCache")) {
            logger.debug("Creating users cache.");
            CacheManager.getInstance().addCache("userCache");
        }
        if (!CacheManager.getInstance().cacheExists("taskCache")) {
            logger.debug("Creating tasks cache.");
            CacheManager.getInstance().addCache("taskCache");
        }
        if (!CacheManager.getInstance().cacheExists("productBacklogCache")) {
            logger.debug("Creating product backlog cache.");
            CacheManager.getInstance().addCache("productBacklogCache");
        }
        if (!CacheManager.getInstance().cacheExists("effortCache")) {
            logger.debug("Creating effort cache.");
            CacheManager.getInstance().addCache("effortCache");
        }
        if (!CacheManager.getInstance().cacheExists("releaseCache")) {
            logger.debug("Creating release cache.");
            CacheManager.getInstance().addCache("releaseCache");
        }
        if (!CacheManager.getInstance().cacheExists("sprintCache")) {
            logger.debug("Creating sprint cache.");
            CacheManager.getInstance().addCache("sprintCache");
        }
        if (!CacheManager.getInstance().cacheExists("projectCache")) {
            logger.debug("Creating project cache.");
            CacheManager.getInstance().addCache("projectCache");
        }
    }

    @Override
    public void handleParameters(Map<String, String[]> parameters) {
        if (parameters.containsKey("pid")) {
            String[] pids = parameters.get("pid");
            if (pids.length > 0) {
                String pid = parameters.get("pid")[0];
                if (StringUtils.isNotEmpty(pid)) {
                    logger.debug("pid: {}", pid);
                    ApplicationSession.setPid(pid);
                    UserPersistence userDbService = new UserPersistenceImpl();
                    User user = userDbService.getBySchemaPublicId(pid);
                    if (user != null) {
                        UserHelper userUtil = new UserHelper();
                        if (!userUtil.isRegisteredUser(user)) {
                            ApplicationSession.setUser(user);
                            ApplicationSession.setSessionDataHelper(new SessionDataHelper());
                            ApplicationSession.getSessionDataHelper().setTargetPage(WindowIdentification.PROJECTS);
                            logger.info("User \"{}\" free-sign-in in with public schema id: '{}'", user.getEmail(), pid);
                            mainWindow.changePage();
                        } else {
                            logger.warn("User tried to free-sign-in as already registered user. Is this a hacking attempt? pid: {}", pid);
                        }
                    } else {
                        logger.warn("User not found with public schema id '{}'. Is this a hacking attempt?", pid);
                    }
                }
            }
        }
    }

    @Override
    public void close() {
        super.close();
        logger.debug("About to close the application.");
        ClientSchemaDatabaseHelper.close();
        ApplicationSession.setSessionDataHelper(null);
        ApplicationSession.setUser(null);
    }
}
