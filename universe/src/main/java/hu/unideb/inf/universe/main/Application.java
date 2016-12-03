package hu.unideb.inf.universe.main;

import javax.xml.xquery.XQConnection;

import hu.unideb.inf.universe.connection.ConnectionUtil;
import hu.unideb.inf.universe.model.Galaxy;
import hu.unideb.inf.universe.model.SolarSystem;
import hu.unideb.inf.universe.model.Star;
import hu.unideb.inf.universe.service.UniverseService;
import java.util.List;

public class Application {

	public static void main(String[] args) throws Exception {
		XQConnection xqc = ConnectionUtil.getConnection("localhost", "1984", "admin", "admin");
		UniverseService us = new UniverseService(xqc);

		// us.doSomething();
		List<Galaxy> galaxies = us.findAllGalaxies();
		System.out.println("galaxies: " + galaxies);
		System.out.println("validate: " + us.validate());
		
		List<SolarSystem> solarSystems = us.findAllSolarSystemsInGalaxy(galaxies.get(0));
		System.out.println("solar system: " + solarSystems);
		
		Star star = us.findStarInSolarSystem(solarSystems.get(0));
		System.out.println("star: " + star);

		xqc.close();
	}

}
