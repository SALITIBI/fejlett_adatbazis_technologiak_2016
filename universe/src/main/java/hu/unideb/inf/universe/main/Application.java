package hu.unideb.inf.universe.main;

import javax.xml.xquery.XQConnection;

import hu.unideb.inf.universe.connection.ConnectionUtil;
import hu.unideb.inf.universe.model.Comet;
import hu.unideb.inf.universe.model.Galaxy;
import hu.unideb.inf.universe.model.Mineral;
import hu.unideb.inf.universe.model.Moon;
import hu.unideb.inf.universe.model.Planet;
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
		
		List<Planet> planets = us.findAllPlanetsInSolarSystem(solarSystems.get(0));
		System.out.println("planets: " + planets);
		
		List<Comet> comets = us.findAllCometsInSolarSystem(solarSystems.get(0));
		System.out.println("comets: " + comets);
		
		List<Moon> moons = us.findAllMoonsAroundPlanet(planets.get(0));
		System.out.println("moons: " + moons);
		
		List<Mineral> minerals = us.findAllMineralsInComet(comets.get(0));
		System.out.println("minerals: " + minerals);

		xqc.close();
	}

}
