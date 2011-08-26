package org.codeexample.ajaxlogs.services;

import java.io.File;

import org.codeexample.ajaxlogs.utils.FileUtils;

public class LogService {
	/**
	 * @param path
	 * @return lass files under the directory, return empty string array, if the
	 *         path doesn't exist or is not a directory
	 */
	public static String[] listLogs(String path) {
		File directory = new File(path);

		if (directory.isDirectory()) {
			return directory.list();
		} else {
			return new String[] {};
		}
	}

	/**
	 * @param strFile
	 * @param lineNumber
	 * @return last n lines from the file
	 */
	public static String tail(String strFile, int lineNumber) {
		return FileUtils.readLastNLines(strFile, lineNumber);
	}

}
