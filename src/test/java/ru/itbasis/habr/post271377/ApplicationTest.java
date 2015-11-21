package ru.itbasis.habr.post271377;

import org.apache.commons.io.IOUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;

public class ApplicationTest {

	private static final String ROOT_PATH = "./src/test/resources/";

	private ByteArrayOutputStream outContent;

	private static String cleanString(final String str) {
		return str.replaceAll("[\t|\n|\r| ]", "");
	}

	@DataProvider(name = "dataJava")
	public static Object[][] dataJava() throws Exception {
		final String expectText = getExpectedText().replaceAll("\\[", "{").replaceAll("]", "}");

		return new Object[][]{
			{new File(ROOT_PATH, "sample0.xlsx"), expectText},
			{new File(ROOT_PATH, "sample0.xls"), expectText}
		};
	}

	@DataProvider(name = "dataJson")
	public static Object[][] dataJson() throws Exception {
		final String expectText = getExpectedText();

		return new Object[][]{
			{new File(ROOT_PATH, "sample0.xlsx"), expectText},
			{new File(ROOT_PATH, "sample0.xls"), expectText}
		};
	}

	private static String getExpectedText() throws Exception {
		final File expectFile = new File(ROOT_PATH, "expect0.json");
		Assert.assertTrue(expectFile.exists());
		return cleanString(IOUtils.toString(new FileInputStream(expectFile)));
	}

	@BeforeMethod
	public void setUp() throws Exception {
		outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
	}

	@AfterMethod
	public void tearDown() throws Exception {
		if (outContent != null) { outContent.close(); }
	}

	@Test(dataProvider = "dataJava", dependsOnMethods = "testJavaToStringArray", groups = "java")
	public void testJavaMainToStringArray(final File file, final String expected) throws Exception {
		Application.main(new String[]{file.getAbsolutePath(), "java"});
		Assert.assertEquals(cleanString(outContent.toString()), expected);
	}

	@Test(dataProvider = "dataJava", groups = "java")
	public void testJavaToStringArray(final File file, final String expectedJson) throws Exception {
		Assert.assertTrue(file.exists());

		final String json = Application.toStringArray(file, "java");
		Assert.assertEquals(cleanString(json), expectedJson);
	}

	@Test(dataProvider = "dataJson", dependsOnMethods = "testJsonToStringArray", groups = "json")
	public void testJsonMainToStringArray(final File file, final String expectedJson) throws Exception {
		Application.main(new String[]{file.getAbsolutePath()});
		Assert.assertEquals(cleanString(outContent.toString()), expectedJson);
	}

	@Test(dataProvider = "dataJson", groups = "json")
	public void testJsonToStringArray(final File file, final String expectedJson) throws Exception {
		Assert.assertTrue(file.exists());

		final String json = Application.toStringArray(file);
		Assert.assertEquals(cleanString(json), expectedJson);
	}
}