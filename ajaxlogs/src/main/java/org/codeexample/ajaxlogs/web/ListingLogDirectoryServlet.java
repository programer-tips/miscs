package org.codeexample.ajaxlogs.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codeexample.ajaxlogs.services.LogService;

import com.google.gson.Gson;

/**
 * This servlet would list all files under the {project-root}/web-inf/logs
 * 
 */
@WebServlet("/listingLogDirectory")
public class ListingLogDirectoryServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(ListingLogDirectoryServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {

		String logsPath = MyContextListener.getLogsPath();

		String[] logs = LogService.listLogs(logsPath);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(new Gson().toJson(logs));

		logger.info("ListingLogDirectoryServlet returns: "
				+ new Gson().toJson(logs));

		response.getWriter().close();
	}

}
