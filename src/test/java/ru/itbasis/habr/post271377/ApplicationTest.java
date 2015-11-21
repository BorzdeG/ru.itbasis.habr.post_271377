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

import static org.testng.Assert.*;

public class ApplicationTest {
	private ByteArrayOutputStream outContent;

	private static String cleanString(final String str) {
		return str.replaceAll("[\t|\n|\r| ]", "");
	}

	@DataProvider(name = "data")
	public static Object[][] data() throws Exception {
		final String rootPath = "./src/test/resources/";

		final File expectFile = new File(rootPath, "expect0.json");
		Assert.assertTrue(expectFile.exists());
		final String expectJson = cleanString(IOUtils.toString(new FileInputStream(expectFile)));

		return new Object[][]{
			{new File(rootPath, "sample0.xlsx"), expectJson},
			{new File(rootPath, "sample0.xls"), expectJson}
		};
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

	@Test(dataProvider = "data", dependsOnMethods = "testToJson")
	public void testMain(final File file, final String expectedJson) throws Exception {
		Application.main(new String[]{file.getAbsolutePath()});
		Assert.assertEquals(cleanString(outContent.toString()), expectedJson);
	}

	@Test(dataProvider = "data")
	public void testToJson(final File file, final String expectedJson) throws Exception {
		Assert.assertTrue(file.exists());

		final String json = new Application().toJson(file);
		Assert.assertEquals(cleanString(json), expectedJson);
	}
}