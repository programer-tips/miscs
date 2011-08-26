package org.codeexample.ajaxlogs.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codeexample.ajaxlogs.services.LogService;
import org.codeexample.ajaxlogs.utils.FileUtils;

@WebServlet("/tailingLog")
public class TailingLogServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(TailingLogServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		String logFile = req.getParameter("log");
		String strLineNumber = req.getParameter("lastLineNumber");

		int lineNumber = 100;
		try {
			lineNumber = Integer.valueOf(strLineNumber);
		} catch (Exception e) {
			logger.error(strLineNumber + " is not a valid number.", e);
		}

		String logsPath = MyContextListener.getLogsPath()
				+ FileUtils.FILE_SEPARATOR + logFile;

		String lastLines = LogService.tail(logsPath, lineNumber);
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(lastLines);
		response.getWriter().close();
	}
}
