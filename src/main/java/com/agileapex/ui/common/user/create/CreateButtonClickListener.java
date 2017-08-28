package com.agileapex.ui.common.user.create;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agileapex.common.SchemaUtil;
import com.agileapex.common.security.DigestHelper;
import com.agileapex.domain.Authorization;
import com.agileapex.domain.User;
import com.agileapex.domain.UserStatus;
import com.agileapex.persistence.UserPersistence;
import com.agileapex.persistence.UserPersistenceImpl;
import com.agileapex.session.ApplicationSession;
import com.agileapex.ui.common.user.UserDetailsValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class CreateButtonClickListener implements Button.ClickListener {
    private static final Logger logger = LoggerFactory.getLogger(CreateButtonClickListener.class);
    private static final long serialVersionUID = -3004065475020808686L;
    private final CreateNewUserPopup layout;

    public CreateButtonClickListener(CreateNewUserPopup layout) {
        this.layout = layout;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        create();
    }

    private void create() {
        logger.debug("Create a new user button clicked.");
        UserDetailsValidator validator = new UserDetailsValidator();
        if (validator.isValid(null, layout.getWindow(), layout.userNameTextField, layout.firstNameTextField, layout.lastNameTextField, false, layout.passwordTextField, layout.confirmPasswordTextField)) {
            User user = setDataFromPopupFields();
            UserPersistence userDbService = new UserPersistenceImpl();
            userDbService.create(user);
            layout.getParent().removeWindow(layout);
            layout.parentLayout.refresh();
            logger.debug("New user created: {}", user);
        }
    }

    private User setDataFromPopupFields() {
        String userName = StringUtils.trimToEmpty((String) layout.userNameTextField.getValue());
        String firstName = StringUtils.trimToEmpty((String) layout.firstNameTextField.getValue());
        String lastName = StringUtils.trimToEmpty((String) layout.lastNameTextField.getValue());
        DigestHelper digestHelper = new DigestHelper();
        final byte[] passwordSalt = digestHelper.getNewSalt();
        final byte[] passwordDigest = digestHelper.getDigest(StringUtils.trimToEmpty((String) layout.passwordTextField.getValue()), passwordSalt);
        Authorization authorization = (Authorization) layout.authorizationSelect.getValue();
        String schemaInternalId = ApplicationSession.getUser().getSchemaInternalId();
        SchemaUtil schemaUtil = new SchemaUtil();
        String schemaPublicId = schemaUtil.createUniquePublicId();
        User user = new User(userName, passwordDigest, passwordSalt, firstName, lastName, UserStatus.REGISTERED, authorization, schemaInternalId, schemaPublicId);
        logger.debug("About to create user: {}", user);
        return user;
    }
}
