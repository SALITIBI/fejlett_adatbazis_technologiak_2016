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

	@Test
	public void testAddMineralToComet() throws UniverseException {
		Comet comet = comets.get(0);

		String mineralName = "Oxygen";
		Property mineralQuantity = new Property("g", 500000.0);

		us.addMineralToComet(comet.getName(), mineralName, mineralQuantity);

		Mineral mineral = us.findMineralByComet(comet.getName(), mineralName);

		Assert.assertNotNull(mineral);
		Assert.assertEquals(mineralName, mineral.getElementName());
		Assert.assertEquals(mineralQuantity.getUnit(), mineral.getQuantity().getUnit());
		Assert.assertEquals(mineralQuantity.getValue(), mineral.getQuantity().getValue(), EPSILON);
	}

	@Test
	public void testAddCometToSolarSystem() throws UniverseException {
		SolarSystem solarSystem = solarSystems.get(0);

		String cometName = "KillerComet-1024";
		Property orbitalPeriod = new Property("day", 14.5);

		us.addCometToSolarSystem(solarSystem.getName(), cometName, orbitalPeriod);

		Comet comet = us.findCometByName(cometName);

		Assert.assertNotNull(comet);
		Assert.assertEquals(cometName, comet.getName());
		Assert.assertEquals(orbitalPeriod.getUnit(), comet.getOrbitalPeriod().getUnit());
		Assert.assertEquals(orbitalPeriod.getValue(), comet.getOrbitalPeriod().getValue(), EPSILON);
	}

	@Test
	public void testAddMoonToPlanet() throws UniverseException {
		Planet planet = planets.get(0);

		String moonName = "DeathStar-1024";
		Property radius = new Property("km", 12345.67);

		us.addMoonToPlanet(planet.getName(), moonName, radius);

		Moon moon = us.findMoonByName(moonName);

		Assert.assertNotNull(moon);
		Assert.assertEquals(moonName, moon.getName());
		Assert.assertEquals(radius.getUnit(), moon.getRadius().getUnit());
		Assert.assertEquals(radius.getValue(), moon.getRadius().getValue(), EPSILON);
	}

	@Test
	public void testAddPlanetToSolarSystem() throws UniverseException {
		SolarSystem solarSystem = solarSystems.get(0);

		String planetName = "Pluto";
		Property radius = new Property("km", 98765.4321);
		Property orbitalPeriod = new Property("day", 720.1);
		Property orbitalSpeed = new Property("kps", 2.19);
		Property eccentricity = new Property(null, 0.5);
		Property semiMajorAxis = new Property("AU", 1.3);
		Property mass = new Property("JupiterMass", 1.5);

		us.addPlanetToSolarSystem(solarSystem.getName(), planetName, radius, orbitalPeriod, orbitalSpeed, eccentricity,
				semiMajorAxis, mass);

		Planet planet = us.findPlanetByName(planetName);

		Assert.assertNotNull(planet);

		Assert.assertEquals(planetName, planet.getName());

		Assert.assertEquals(radius.getUnit(), planet.getRadius().getUnit());
		Assert.assertEquals(radius.getValue(), planet.getRadius().getValue(), EPSILON);

		Assert.assertEquals(orbitalPeriod.getUnit(), planet.getOrbitalPeriod().getUnit());
		Assert.assertEquals(orbitalPeriod.getValue(), planet.getOrbitalPeriod().getValue(), EPSILON);

		Assert.assertEquals(orbitalSpeed.getUnit(), planet.getOrbitalSpeed().getUnit());
		Assert.assertEquals(orbitalSpeed.getValue(), planet.getOrbitalSpeed().getValue(), EPSILON);

		Assert.assertEquals(eccentricity.getUnit(), planet.getEccentricity().getUnit());
		Assert.assertEquals(eccentricity.getValue(), planet.getEccentricity().getValue(), EPSILON);

		Assert.assertEquals(semiMajorAxis.getUnit(), planet.getSemiMajorAxis().getUnit());
		Assert.assertEquals(semiMajorAxis.getValue(), planet.getSemiMajorAxis().getValue(), EPSILON);

		Assert.assertEquals(mass.getUnit(), planet.getMass().getUnit());
		Assert.assertEquals(mass.getValue(), planet.getMass().getValue(), EPSILON);
	}

}
