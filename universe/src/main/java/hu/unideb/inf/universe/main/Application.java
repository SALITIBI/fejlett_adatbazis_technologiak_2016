package hu.unideb.inf.universe.main;

import java.util.List;

import javax.xml.xquery.XQConnection;

import hu.unideb.inf.universe.connection.ConnectionUtil;
import hu.unideb.inf.universe.model.Comet;
import hu.unideb.inf.universe.model.Galaxy;
import hu.unideb.inf.universe.model.Mineral;
import hu.unideb.inf.universe.model.Moon;
import hu.unideb.inf.universe.model.Planet;
import hu.unideb.inf.universe.model.Property;
import hu.unideb.inf.universe.model.SolarSystem;
import hu.unideb.inf.universe.model.Star;
import hu.unideb.inf.universe.service.UniverseService;
import hu.unideb.inf.universe.service.UniverseServiceImpl;

public class Application {

	public static void main(String[] args) throws Exception {
		XQConnection xqc = ConnectionUtil.getConnection("localhost", "1984", "admin", "admin");
		UniverseService us = new UniverseServiceImpl(xqc);

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

		Property newQuantity = new Property("kg", 555555.55);
		us.updateMineralOnComet(comets.get(0).getName(), minerals.get(0).getElementName(), newQuantity);
		
		Property quantity = new Property("g", 1024.1024);
		us.addMineralToComet(comets.get(0).getName(), "Element Zero", quantity);

		minerals = us.findAllMineralsInComet(comets.get(0));
		System.out.println("minerals: " + minerals);

		Property newOrbitalPeriodProperty = new Property("day", 7.0);
		us.updateCometOrbitalPeriod(comets.get(0).getName(), newOrbitalPeriodProperty);
		
		Property orbitalPeriod = new Property("year", 2520);
		us.addCometToSolarSystem(solarSystems.get(0).getName(), "Hale–Bopp", orbitalPeriod);
		
		Property radius = new Property("km", 2048.1024);
		us.addMoonToPlanet(planets.get(0).getName(), "Moon-Moon", radius);
		
		us.addPlanetToSolarSystem(solarSystems.get(0).getName(), "Mercury", 
			new Property("km", 2439.7),
			new Property("day", 87.969),
			new Property("kps", 47.3621),
			new Property(null, 0.205630),
			new Property("AU", 0.387098),
			new Property("kg", 3.3011 * Math.pow(10, 23)));
		
		planets = us.findAllPlanetsInSolarSystem(solarSystems.get(0));
		System.out.println("planets: " + planets);
		
		moons = us.findAllMoonsAroundPlanet(planets.get(0));
		System.out.println("moons: " + moons);
		
		comets = us.findAllCometsInSolarSystem(solarSystems.get(0));
		System.out.println("comets: " + comets);
		
//		System.out.println("Deleting mineral: " + minerals.get(0) + " on comet: " + comets.get(0));
//		us.deleteMineralOnComet(comets.get(0).getName(), minerals.get(0).getElementName());
//
//		minerals = us.findAllMineralsInComet(comets.get(0));
//		System.out.println("minerals: " + minerals);
//
//		System.out.println("Deleting comet: " + comets.get(0));
//		us.deleteComet(comets.get(0).getName());
//
//		comets = us.findAllCometsInSolarSystem(solarSystems.get(0));
//		System.out.println("comets: " + comets);
//
//		System.out.println("Deleting moon: " + moons.get(0));
//		us.deleteMoon(moons.get(0).getName());
//
//		moons = us.findAllMoonsAroundPlanet(planets.get(0));
//		System.out.println("moons: " + moons);
//
//		System.out.println("Deleting planet: " + planets.get(0));
//		us.deletePlanet(planets.get(0).getName());
//
//		planets = us.findAllPlanetsInSolarSystem(solarSystems.get(0));
//		System.out.println("planets: " + planets);
//
//		System.out.println("Deleting solar system: " + solarSystems.get(0));
//		us.deleteSolarSystem(solarSystems.get(0).getName());
//
//		solarSystems = us.findAllSolarSystemsInGalaxy(galaxies.get(0));
//		System.out.println("solar system: " + solarSystems);
//
//		System.out.println("Deleting galaxy: " + galaxies.get(0));
//		us.deleteGalaxy(galaxies.get(0).getName());
//
//		galaxies = us.findAllGalaxies();
//		System.out.println("galaxies: " + galaxies);

		xqc.close();
	}

}
