package hu.unideb.inf.universe.main;

import java.util.List;

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

public class Application {

	public static void main(String[] args) throws Exception {
		XQConnection xqc = ConnectionUtil.getConnection("localhost", "1984", "admin", "admin");
		UniverseService us = new UniverseService(xqc);

		// us.doSomething();
		System.out.println("validate: " + us.validate());

		List<Galaxy> galaxies = us.findAllGalaxies();
		System.out.println("galaxies: " + galaxies);

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

		System.out.println("Deleting mineral: " + minerals.get(0) + " on comet: " + comets.get(0));
		us.deleteMineralOnComet(comets.get(0).getName(), minerals.get(0).getElementName());

		minerals = us.findAllMineralsInComet(comets.get(0));
		System.out.println("minerals: " + minerals);

		System.out.println("Deleting comet: " + comets.get(0));
		us.deleteComet(comets.get(0).getName());

		comets = us.findAllCometsInSolarSystem(solarSystems.get(0));
		System.out.println("comets: " + comets);

		System.out.println("Deleting moon: " + moons.get(0));
		us.deleteMoon(moons.get(0).getName());

		moons = us.findAllMoonsAroundPlanet(planets.get(0));
		System.out.println("moons: " + moons);

		System.out.println("Deleting planet: " + planets.get(0));
		us.deletePlanet(planets.get(0).getName());

		planets = us.findAllPlanetsInSolarSystem(solarSystems.get(0));
		System.out.println("planets: " + planets);

		System.out.println("Deleting solar system: " + solarSystems.get(0));
		us.deleteSolarSystem(solarSystems.get(0).getName());

		solarSystems = us.findAllSolarSystemsInGalaxy(galaxies.get(0));
		System.out.println("solar system: " + solarSystems);

		System.out.println("Deleting galaxy: " + galaxies.get(0));
		us.deleteGalaxy(galaxies.get(0).getName());

		galaxies = us.findAllGalaxies();
		System.out.println("galaxies: " + galaxies);

		xqc.close();
	}

}
