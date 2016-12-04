package hu.unideb.inf.universe.service;

import java.util.List;

import javax.xml.xquery.XQException;

import org.junit.Assert;
import org.junit.Test;

import hu.unideb.inf.universe.exception.UniverseException;
import hu.unideb.inf.universe.model.Comet;
import hu.unideb.inf.universe.model.Galaxy;
import hu.unideb.inf.universe.model.Mineral;
import hu.unideb.inf.universe.model.Moon;
import hu.unideb.inf.universe.model.Planet;
import hu.unideb.inf.universe.model.Property;
import hu.unideb.inf.universe.model.SolarSystem;

public class UniverseServiceTest extends AbstractTest {

	private List<Galaxy> galaxies;
	private List<SolarSystem> solarSystems;
	private List<Comet> comets;
	private List<Mineral> minerals;
	private List<Planet> planets;
	private List<Moon> moons;

	@Override
	public void setUp() throws UniverseException, XQException {
		super.setUp();
		galaxies = us.findAllGalaxies();
		solarSystems = us.findAllSolarSystemsInGalaxy(galaxies.get(0));
		comets = us.findAllCometsInSolarSystem(solarSystems.get(0));
		planets = us.findAllPlanetsInSolarSystem(solarSystems.get(0));
		minerals = us.findAllMineralsInComet(comets.get(0));
		moons = us.findAllMoonsAroundPlanet(planets.get(0));
	}

	@Test
	public void testCometUpdateAndDelete() throws UniverseException {
		Assert.assertFalse(minerals.isEmpty());

		Property newQuantity = new Property("kg", 25.55);
		String cometName = comets.get(0).getName();
		String mineralName = minerals.get(0).getElementName();

		us.updateMineralOnComet(cometName, mineralName, newQuantity);

		Mineral mineral = us.findMineralByComet(cometName, mineralName);
		Assert.assertEquals(newQuantity.getUnit(), mineral.getQuantity().getUnit());
		Assert.assertEquals(newQuantity.getValue(), mineral.getQuantity().getValue(), EPSILON);

		us.deleteMineralOnComet(cometName, mineralName);

		mineral = us.findMineralByComet(cometName, mineralName);
		Assert.assertNull(mineral);
	}

	@Test
	public void testMoonUpdateAndDelete() throws UniverseException {
		Assert.assertFalse(moons.isEmpty());

		Property newRadius = new Property("km", 500.0);
		String moonName = moons.get(0).getName();

		us.updateMoonRadius(moonName, newRadius);

		Moon moon = us.findMoonByName(moonName);
		Assert.assertEquals(newRadius.getUnit(), moon.getRadius().getUnit());
		Assert.assertEquals(newRadius.getValue(), moon.getRadius().getValue(), EPSILON);

		us.deleteMoon(moonName);

		moon = us.findMoonByName(moonName);
		Assert.assertNull(moon);
	}

	@Test
	public void testPlanetUpdate() throws UniverseException {
		Assert.assertFalse(planets.isEmpty());

		Property newRadius = new Property("km", 150000.0);
		String planetName = planets.get(0).getName();

		us.updatePlanetRadius(planetName, newRadius);

		Planet planet = us.findPlanetByName(planetName);
		Assert.assertEquals(newRadius.getUnit(), planet.getRadius().getUnit());
		Assert.assertEquals(newRadius.getValue(), planet.getRadius().getValue(), EPSILON);
	}

	@Test
	public void testGalaxyDelete() throws UniverseException {
		for (Galaxy galaxy : galaxies) {
			us.deleteGalaxy(galaxy.getName());
		}
		galaxies = us.findAllGalaxies();
		Assert.assertTrue(galaxies.isEmpty());
	}

}
