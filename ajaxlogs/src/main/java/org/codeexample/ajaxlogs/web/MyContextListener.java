package org.codeexample.ajaxlogs.web;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.codeexample.ajaxlogs.utils.FileUtils;

/**
 * Application Lifecycle Listener implementation class MyContextListener
 * 
 */
public class MyContextListener implements ServletContextListener {

	static Logger logger = Logger.getLogger(MyContextListener.class);

	private static ScheduledExecutorService scheduler;

	private static String LOGS_PATH = "";

	/**
	 * Default constructor.
	 */
	public MyContextListener() {
	}

	public static String[] LOGS = { "error.log", "warning.log", "debug.log",
			"trace.log" };

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent event) {
		try {
			ServletContext context = event.getServletContext();
			LOGS_PATH = context.getRealPath("/") + FileUtils.FILE_SEPARATOR
					+ "WEB-INF" + FileUtils.FILE_SEPARATOR + "logs";

			// PropertyConfigurator.configure(context.getRealPath("/")
			// + FileUtils.FILE_SEPARATOR + "WEB-INF"
			// + FileUtils.FILE_SEPARATOR + "log4j.xml");
			File file = new File(LOGS_PATH);
			if (!file.exists()) {
				file.mkdir();
			}
			System.setProperty("logsPath", LOGS_PATH);
			logger.info("logsPath: " + LOGS_PATH);
			logger.info("contextInitialized started");
			dumpLogsRepeatedly(LOGS_PATH, LOGS);
			logger.info("contextInitialized ended");
		} catch (IOException e) {
			logger.error("Throws exception", e);
		}
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent event) {
		logger.info("contextDestroyed started");
		if (scheduler != null) {
			scheduler.shutdown();
		}
		logger.info("contextDestroyed ended");
	}

	/**
	 * @return the root directory of logs that we want to monitor
	 */
	public static String getLogsPath() {
		return LOGS_PATH;
	}

	private static void dumpLogsRepeatedly(final String directory,
			final String[] files) throws IOException {

		File file = new File(directory);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				logger.error("uable to create the directory: " + directory, e);
			}
		}

		final List<File> fileList = new ArrayList<File>();

		for (int i = 0; i < files.length; i++) {
			file = new File(directory + FileUtils.FILE_SEPARATOR + files[i]);
			if (!file.exists()) {
				try {
					file.createNewFile();
					fileList.add(file);
				} catch (IOException e) {
					logger.error("Error to create new file: " + file);
				}
			} else {
				fileList.add(file);
			}
		}
		if (fileList.isEmpty()) {
			logger.error("No logs to dump, directory: " + directory);
			return;
		}

		// this thread would write some records to the LOGS
		Runnable dumpThread = new Runnable() {
			@Override
			public void run() {
				DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				for (File file : fileList) {
					BufferedWriter bw = null;
					try {
						bw = new BufferedWriter(new FileWriter(file, true));
						bw.write("New log appended to file: " + file + ", at: "
								+ sdf.format(new Date()));
						bw.newLine();
					} catch (IOException e) {
						logger.error("throw IOException: " + e);
					} finally {
						if (bw != null) {
							try {
								bw.close();
							} catch (IOException e1) {
								logger.error("close throws exception:" + e1);
							}
						}
					}
				}
			}
		};

		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleWithFixedDelay(dumpThread, 100, 100,
				TimeUnit.MILLISECONDS);
	}
}
