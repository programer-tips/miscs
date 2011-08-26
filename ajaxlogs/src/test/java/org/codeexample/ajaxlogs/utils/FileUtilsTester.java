package org.codeexample.ajaxlogs.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class FileUtilsTester {

	@Test
	public void testReadLastNLines() {
		String lastNLines = FileUtils.readLastNLines(FileUtilsTester.class
				.getResource("12.txt").getFile(), 2);
		assertEquals("1" + FileUtils.NEW_LINE + "2", lastNLines);

		lastNLines = FileUtils.readLastNLines(FileUtilsTester.class
				.getResource("123.txt").getFile(), 2);
		assertEquals("2" + FileUtils.NEW_LINE + "3", lastNLines);

		lastNLines = FileUtils.readLastNLines(FileUtilsTester.class
				.getResource("12space.txt").getFile(), 2);
		assertEquals("2" + FileUtils.NEW_LINE + " ", lastNLines);

		lastNLines = FileUtils.readLastNLines(FileUtilsTester.class
				.getResource("12newline.txt").getFile(), 2);
		assertEquals("2" + FileUtils.NEW_LINE, lastNLines);

		lastNLines = FileUtils.readLastNLines(FileUtilsTester.class
				.getResource("space12.txt").getFile(), 2);
		assertEquals("1" + FileUtils.NEW_LINE + "2", lastNLines);

		// error case
		lastNLines = FileUtils.readLastNLines("invalid", 2);
		assertEquals("", lastNLines);
	}

}
