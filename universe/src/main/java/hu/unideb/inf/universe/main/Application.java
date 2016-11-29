package hu.unideb.inf.universe.main;

import javax.xml.xquery.XQConnection;

import hu.unideb.inf.universe.connection.ConnectionUtil;
import hu.unideb.inf.universe.service.UniverseService;

public class Application {

	public static void main(String[] args) throws Exception {
		XQConnection xqc = ConnectionUtil.getConnection("localhost", "1984", "admin", "admin");
		UniverseService us = new UniverseService(xqc);

		// us.doSomething();
		System.out.println("galaxies: " + us.findAllGalaxies());
		System.out.println("validate: " + us.validate());

		xqc.close();
	}

}
