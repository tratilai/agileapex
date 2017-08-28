package com.agileapex;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.terminal.gwt.server.ApplicationServlet;
import com.vaadin.terminal.gwt.server.CommunicationManager;
import com.vaadin.ui.Window;

public class AgileApexServlet extends ApplicationServlet {
	private static final long serialVersionUID = 5749577953087594980L;
	private static final Logger logger = LoggerFactory.getLogger(AgileApexServlet.class);

	@Override
	protected boolean handleURI(CommunicationManager applicationManager, Window window, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		boolean handleURI = super.handleURI(applicationManager, window, request, response);
		return handleURI;
	}
}
