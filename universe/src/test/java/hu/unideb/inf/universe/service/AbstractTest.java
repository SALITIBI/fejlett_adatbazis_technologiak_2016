package hu.unideb.inf.universe.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import hu.unideb.inf.universe.connection.ConnectionUtil;
import hu.unideb.inf.universe.exception.UniverseException;

public class AbstractTest {

	public static final double EPSILON = 0.0000000001;

	private static final String TEST_DB_NAME = "test-universe";

	protected static UniverseService us;

	private static XQConnection xqc;

	private static String TEST_UNIVERSE;

	static {
		String testUniverse = null;

		InputStream is = AbstractTest.class.getResourceAsStream("/fejlett_adatbazis_hazi.xml");
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
			testUniverse = br.lines().collect(Collectors.joining(System.lineSeparator()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		TEST_UNIVERSE = testUniverse;
	}

	@BeforeClass
	public static void init() throws XQException {
		xqc = ConnectionUtil.getConnection("localhost", "1984", "admin", "admin");
		us = new UniverseServiceImpl(xqc, TEST_DB_NAME);
	}

	@Before
	public void setUp() throws UniverseException, XQException {
		dropDatabase(TEST_DB_NAME);
		initDatabase(TEST_DB_NAME);
	}

	private void dropDatabase(String dbName) throws XQException {
		XQExpression expr = xqc.createExpression();
		expr.executeCommand("DROP DB " + dbName);
	}

	private void initDatabase(String dbName) throws XQException {
		XQExpression expr = xqc.createExpression();
		expr.executeCommand("CREATE DB " + dbName + " " + TEST_UNIVERSE);
	}

	@Test
	public void test() {
		Assert.assertTrue(true);
	}

	@AfterClass
	public static void closeConnection() throws XQException {
		xqc.close();
	}

}
