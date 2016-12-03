package hu.unideb.inf.universe.service;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import hu.unideb.inf.universe.connection.ConnectionUtil;
import hu.unideb.inf.universe.exception.UniverseException;

public class AbstractTest {

	public static double EPSILON = 0.0000000001;

	private static XQConnection xqc;

	protected static UniverseService us;

	@BeforeClass
	public static void init() throws XQException {
		xqc = ConnectionUtil.getConnection("localhost", "1984", "admin", "admin");
		us = new UniverseServiceImpl(xqc);
	}

	@Before
	public void setUp() throws UniverseException {
		// TODO DROP DB test-universe
		// TODO CREATE DB test-universe fejlett_adatbazis_hazi.xml
	}

	@AfterClass
	public static void closeConnection() throws XQException {
		xqc.close();
	}

}
