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
	public void deleteMineralOnComet() throws UniverseException {
		Comet comet = comets.get(0);
		for (Mineral mineral : minerals) {
			us.deleteMineralOnComet(comet.getName(), mineral.getElementName());
		}

		minerals = us.findAllMineralsInComet(comet);

		Assert.assertTrue(minerals.isEmpty());
	}

	@Test
	public void deleteComet() throws UniverseException {
		for (Comet comet : comets) {
			us.deleteComet(comet.getName());
		}

		comets = us.findAllCometsInSolarSystem(solarSystems.get(0));

		Assert.assertTrue(comets.isEmpty());
	}

	@Test
	public void testDeleteMoon() throws UniverseException {
		for (Moon moon : moons) {
			us.deleteMoon(moon.getName());
		}

		moons = us.findAllMoonsAroundPlanet(planets.get(0));

		Assert.assertTrue(moons.isEmpty());
	}

	@Test
	public void testDeletePlanet() throws UniverseException {
		for (Planet planet : planets) {
			us.deletePlanet(planet.getName());
		}

		planets = us.findAllPlanetsInSolarSystem(solarSystems.get(0));

		Assert.assertTrue(planets.isEmpty());
	}

	@Test
	public void testDeleteSolarSystem() throws UniverseException {
		for (SolarSystem solarSystem : solarSystems) {
			us.deleteSolarSystem(solarSystem.getName());
		}

		solarSystems = us.findAllSolarSystemsInGalaxy(galaxies.get(0));

		Assert.assertTrue(solarSystems.isEmpty());
	}

	@Test
	public void testGalaxyDelete() throws UniverseException {
		for (Galaxy galaxy : galaxies) {
			us.deleteGalaxy(galaxy.getName());
		}

		galaxies = us.findAllGalaxies();

		Assert.assertTrue(galaxies.isEmpty());
	}

	@Test
	public void testUpdatePlanetRadius() throws UniverseException {
		Planet planet = planets.get(0);

		Property newRadius = new Property("km", 25000.0);
		us.updatePlanetRadius(planet.getName(), newRadius);

		planet = us.findPlanetByName(planet.getName());

		Assert.assertEquals(newRadius.getUnit(), planet.getRadius().getUnit());
		Assert.assertEquals(newRadius.getValue(), planet.getRadius().getValue(), EPSILON);
	}

	@Test
	public void testUpdateMoonRadius() throws UniverseException {
		Moon moon = moons.get(0);

		Property newRadius = new Property("km", 5000.0);
		us.updateMoonRadius(moon.getName(), newRadius);

		moon = us.findMoonByName(moon.getName());

		Assert.assertEquals(newRadius.getUnit(), moon.getRadius().getUnit());
		Assert.assertEquals(newRadius.getValue(), moon.getRadius().getValue(), EPSILON);
	}

	@Test
	public void testUpdateMineralOnComet() throws UniverseException {
		Comet comet = comets.get(0);
		Mineral mineral = comet.getMinerals().get(0);

		Property newQuantity = new Property("g", 3000.0);
		us.updateMineralOnComet(comet.getName(), mineral.getElementName(), newQuantity);

		mineral = us.findMineralByComet(comet.getName(), mineral.getElementName());

		Assert.assertEquals(newQuantity.getUnit(), mineral.getQuantity().getUnit());
		Assert.assertEquals(newQuantity.getValue(), mineral.getQuantity().getValue(), EPSILON);
	}

	@Test
	public void testUpdateCometOrbitalPeriod() throws UniverseException {
		Comet comet = comets.get(0);

		Property newOrbitalPeriod = new Property("day", 7.0);
		us.updateCometOrbitalPeriod(comet.getName(), newOrbitalPeriod);

		comet = us.findCometByName(comet.getName());

		Assert.assertEquals(newOrbitalPeriod.getUnit(), comet.getOrbitalPeriod().getUnit());
		Assert.assertEquals(newOrbitalPeriod.getValue(), comet.getOrbitalPeriod().getValue(), EPSILON);
	}

}
