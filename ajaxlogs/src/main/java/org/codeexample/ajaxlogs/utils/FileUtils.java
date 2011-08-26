package org.codeexample.ajaxlogs.utils;

public class FileUtils {
	public static final String NEW_LINE = System.getProperty("line.separator");
	public static final String FILE_SEPARATOR = System
			.getProperty("file.separator");

	public static final int R_FEED = '\r';
	public static final int N_FEED = '\n';

	/**
	 * Return the files's last n lines
	 * 
	 * @param fileName
	 * @param lineNumber
	 * @return
	 */
	public static String readLastNLines(String fileName, int lineNumber) {
		StringBuilder sb = new StringBuilder();
		try {
			java.io.File file = new java.io.File(fileName);
			java.io.RandomAccessFile fileHandler = new java.io.RandomAccessFile(
					file, "r");
			long fileLength = file.length() - 1;
			for (long filePointer = fileLength; filePointer != -1
					&& lineNumber > 0; filePointer--) {

				fileHandler.seek(filePointer);
				int readByte = fileHandler.readByte();
				if (readByte == N_FEED) {
					lineNumber--;
					if (lineNumber <= 0)
						break;
				}
				sb.append((char) readByte);
			}
		} catch (java.io.FileNotFoundException e) {
			e.printStackTrace();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}

		return sb.reverse().toString();
	}
}
