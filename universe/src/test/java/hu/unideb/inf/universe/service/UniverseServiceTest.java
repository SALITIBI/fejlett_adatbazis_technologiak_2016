
package hu.unideb.inf.universe.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
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
import hu.unideb.inf.universe.model.Star;

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
	public void testFindPlanetByName() throws UniverseException {
		Planet expectedPlanet = planets.get(0);
		Planet actualPlanet = us.findPlanetByName(expectedPlanet.getName());

		Assert.assertNotNull(actualPlanet);
		Assert.assertEquals(expectedPlanet.getName(), actualPlanet.getName());

		Assert.assertEquals(expectedPlanet.getRadius().getUnit(), actualPlanet.getRadius().getUnit());
		Assert.assertEquals(expectedPlanet.getRadius().getValue(), actualPlanet.getRadius().getValue(), EPSILON);

		Assert.assertEquals(expectedPlanet.getOrbitalPeriod().getUnit(), actualPlanet.getOrbitalPeriod().getUnit());
		Assert.assertEquals(expectedPlanet.getOrbitalPeriod().getValue(), actualPlanet.getOrbitalPeriod().getValue(),
				EPSILON);

		Assert.assertEquals(expectedPlanet.getOrbitalSpeed().getUnit(), actualPlanet.getOrbitalSpeed().getUnit());
		Assert.assertEquals(expectedPlanet.getOrbitalSpeed().getValue(), actualPlanet.getOrbitalSpeed().getValue(),
				EPSILON);

		Assert.assertEquals(expectedPlanet.getEccentricity().getUnit(), actualPlanet.getEccentricity().getUnit());
		Assert.assertEquals(expectedPlanet.getEccentricity().getValue(), actualPlanet.getEccentricity().getValue(),
				EPSILON);

		Assert.assertEquals(expectedPlanet.getSemiMajorAxis().getUnit(), actualPlanet.getSemiMajorAxis().getUnit());
		Assert.assertEquals(expectedPlanet.getSemiMajorAxis().getValue(), actualPlanet.getSemiMajorAxis().getValue(),
				EPSILON);

		Assert.assertEquals(expectedPlanet.getMass().getUnit(), actualPlanet.getMass().getUnit());
		Assert.assertEquals(expectedPlanet.getMass().getValue(), actualPlanet.getMass().getValue(), EPSILON);
	}

	@Test
	public void testFindMoonByName() throws UniverseException {
		Moon expectedMoon = moons.get(0);
		Moon actualMoon = us.findMoonByName(expectedMoon.getName());

		Assert.assertNotNull(actualMoon);

		Assert.assertEquals(expectedMoon.getName(), actualMoon.getName());
		Assert.assertEquals(expectedMoon.getRadius().getUnit(), actualMoon.getRadius().getUnit());
		Assert.assertEquals(expectedMoon.getRadius().getValue(), actualMoon.getRadius().getValue(), EPSILON);
	}

	@Test
	public void testFindCometByName() throws UniverseException {
		Comet expectedComet = comets.get(0);
		Comet actualComet = us.findCometByName(expectedComet.getName());

		Assert.assertNotNull(actualComet);

		Assert.assertEquals(expectedComet.getName(), actualComet.getName());
		Assert.assertEquals(expectedComet.getOrbitalPeriod().getUnit(), actualComet.getOrbitalPeriod().getUnit());
		Assert.assertEquals(expectedComet.getOrbitalPeriod().getValue(), actualComet.getOrbitalPeriod().getValue(),
				EPSILON);
		Assert.assertEquals(expectedComet.getMinerals().size(), actualComet.getMinerals().size());
	}

	@Test
	public void testFindStarInSolarSystem() throws UniverseException {
		SolarSystem solarSystem = solarSystems.get(0);

		Star star = us.findStarInSolarSystem(solarSystem);

		Assert.assertNotNull(star);
	}

	@Test
	public void testFindMineralByComet() throws UniverseException {
		Comet comet = comets.get(0);

		Mineral expectedMineral = minerals.get(0);
		Mineral actualMineral = us.findMineralByComet(comet.getName(), expectedMineral.getElementName());

		Assert.assertNotNull(actualMineral);

		Assert.assertEquals(expectedMineral.getElementName(), actualMineral.getElementName());
		Assert.assertEquals(expectedMineral.getQuantity().getUnit(), actualMineral.getQuantity().getUnit());
		Assert.assertEquals(expectedMineral.getQuantity().getValue(), actualMineral.getQuantity().getValue(), EPSILON);
	}

	@Test
	public void testFindCometBySolarSystem() throws UniverseException {
		SolarSystem solarSystem = solarSystems.get(0);

		Comet expectedComet = comets.get(0);
		Comet actualComet = us.findCometBySolarSystem(solarSystem.getName(), expectedComet.getName());

		Assert.assertNotNull(actualComet);

		Assert.assertEquals(expectedComet.getName(), actualComet.getName());
		Assert.assertEquals(expectedComet.getOrbitalPeriod().getUnit(), actualComet.getOrbitalPeriod().getUnit());
		Assert.assertEquals(expectedComet.getOrbitalPeriod().getValue(), actualComet.getOrbitalPeriod().getValue(),
				EPSILON);
		Assert.assertEquals(expectedComet.getMinerals().size(), actualComet.getMinerals().size());
	}

	@Test
	public void testFindSolarSystemByName() throws UniverseException {
		SolarSystem expectedSolarSystem = solarSystems.get(0);
		SolarSystem actualSolarSystem = us.findSolarSystemByName(expectedSolarSystem.getName());

		Assert.assertNotNull(actualSolarSystem);

		Assert.assertEquals(expectedSolarSystem.getName(), actualSolarSystem.getName());
		Assert.assertEquals(expectedSolarSystem.getComets(), actualSolarSystem.getComets());
		Assert.assertEquals(expectedSolarSystem.getPlanets(), actualSolarSystem.getPlanets());
		Assert.assertEquals(expectedSolarSystem.getStar(), actualSolarSystem.getStar());

	}

	@Test
	public void testFindGalaxyByName() throws UniverseException {

		Galaxy expectedGalaxy = galaxies.get(0);
		Galaxy actualGalaxy = us.findGalaxyByName(expectedGalaxy.getName());

		Assert.assertNotNull(actualGalaxy);

		Assert.assertEquals(expectedGalaxy.getName(), actualGalaxy.getName());
		Assert.assertEquals(expectedGalaxy.getSolarsystems(), actualGalaxy.getSolarsystems());

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
		Mineral newMineral = new Mineral(mineral.getElementName(), newQuantity);

		us.updateMineralOnComet(comet.getName(), mineral.getElementName(), newMineral);

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

	@Test
	public void testAddSolarSystemToGalaxy() throws UniverseException {
		Galaxy galaxy = galaxies.get(0);
		String solarSystemName = "Another System";
		String starName = "Sun-alike";
		String starType = "unary";

		us.addSolarSystemToGalaxy(galaxy.getName(), solarSystemName, starName, starType);

		SolarSystem solarSystem = us.findSolarSystemByName(solarSystemName);

		Assert.assertNotNull(solarSystem);
		Assert.assertEquals(solarSystemName, solarSystem.getName());
		Assert.assertEquals(starName, solarSystem.getStar().getName());
		Assert.assertEquals(starType, solarSystem.getStar().getType());
	}

	@Test
	public void testAddGalaxyToUniverse() throws UniverseException {
		String galaxyName = "My Own Galaxy";

		us.addGalaxyToUniverse(galaxyName);

		Galaxy galaxy = us.findGalaxyByName(galaxyName);

		Assert.assertNotNull(galaxy);
		Assert.assertEquals(galaxyName, galaxy.getName());
	}

	@Test
	public void testUpdateComet() throws UniverseException {
		Comet comet = comets.get(0);

		String newName = "New Comet";
		Property newOrbitalPeriod = new Property("day", 7.0);
		List<Mineral> newMinerals = new LinkedList<>();
		newMinerals.add(new Mineral("Nitrogen", new Property("g", 500)));
		newMinerals.add(new Mineral("Iron", new Property("kg", 6000)));

		Comet newComet = new Comet(newName, newOrbitalPeriod, newMinerals);
		us.updateComet(comet.getName(), newComet);

		comet = us.findCometByName(newName);

		Assert.assertEquals(newOrbitalPeriod, comet.getOrbitalPeriod());
		Assert.assertEquals(newName, comet.getName());
		Assert.assertEquals(newMinerals, comet.getMinerals());
	}

	@Test
	public void testUpdateMoonForPlanet() throws UniverseException {
		Planet planet = planets.get(0);
		Moon moon = planet.getMoons().get(0);

		String newName = "The new moon";
		Property newRadius = new Property("km", 543200);
		Moon newMoon = new Moon(newName, newRadius);

		us.updateMoonForPlanet(moon.getName(), planet.getName(), newMoon);

		planet = us.findPlanetByName(planet.getName());
		Moon actualMoon = planet.getMoons().get(0);
		Moon expectedMoon = newMoon;
		Assert.assertEquals(expectedMoon, actualMoon);
	}

	@Test
	public void testUpdatePlanet() throws UniverseException {
		Planet planet = planets.get(0);
		String newName = "My new planet";
		Property newEccentricity = new Property(null, 0.5);
		Property newOrbitalSpeed = new Property("kps", 50);
		Property newOrbitalPeriod = new Property("day", 333);
		Property newSemiMajorAxis = new Property("AU", 1.5);
		Property newMass = new Property("JupiterMass", 3.5);
		Property newRadius = new Property("km", 444444);
		List<Moon> newMoons = new LinkedList<>();
		newMoons.add(new Moon("ASD Moon", new Property("km", 5343)));
		newMoons.add(new Moon("ASD2 Moon", new Property("km", 3333)));
		Planet newPlanet = new Planet(newName, newMoons, newRadius, newOrbitalPeriod, newOrbitalSpeed, newEccentricity,
				newSemiMajorAxis, newMass);
		us.updatePlanet(planet.getName(), newPlanet);

		Planet actualPlanet = us.findPlanetByName(newName);
		Planet expectedPlanet = newPlanet;

		Assert.assertEquals(expectedPlanet, actualPlanet);
	}

	@Test
	public void testUpdateStarInSolarSystem() throws UniverseException {
		SolarSystem solarSystem = solarSystems.get(0);

		String newName = "New Sun";
		String newType = "binary";
		Star newStar = new Star(newName, newType);

		us.updateStarInSolarSystem(solarSystem.getName(), newStar);

		Star actualStar = us.findStarInSolarSystem(solarSystem);
		Star expectedStar = newStar;
		Assert.assertEquals(expectedStar, actualStar);

	}

	@Test
	public void testUpdateGalaxy() throws UniverseException {
		Galaxy galaxy = galaxies.get(0);
		String newName = "New Galaxy";
		List<SolarSystem> newSolarSystems = new LinkedList<>();
		newSolarSystems.add(new SolarSystem("Naboo System", new LinkedList<>(), new LinkedList<>(),
				new Star("Star of Naboo", "multiple")));
		Galaxy newGalaxy = new Galaxy(newName, newSolarSystems);
		us.updateGalaxy(galaxy.getName(), newGalaxy);
		Galaxy actualGalaxy = us.findGalaxyByName(newName);
		Galaxy expectedGalaxy = newGalaxy;

		Assert.assertNotNull(actualGalaxy);
		Assert.assertEquals(expectedGalaxy, actualGalaxy);
	}

	@Test
	public void testUpdateGalaxyName() throws UniverseException {
		Galaxy galaxy = galaxies.get(0);
		String newName = "Another galaxy";
		us.updateGalaxyName(galaxy.getName(), newName);
		Galaxy actualGalaxy = us.findGalaxyByName(newName);

		Assert.assertNotNull(actualGalaxy);
		Assert.assertEquals(newName, actualGalaxy.getName());

	}

	@Test
	public void testUpdateSolarSystem() throws UniverseException {
		SolarSystem solarSystem = solarSystems.get(0);
		String newName = "New Solar System";
		List<Comet> comets = new LinkedList<>();
		comets.add(new Comet("New Comet", new Property("day", 433), new LinkedList<>()));
		List<Planet> planets = new LinkedList<>();
		Property newEccentricity = new Property(null, 0.5);
		Property newOrbitalSpeed = new Property("kps", 50);
		Property newOrbitalPeriod = new Property("day", 333);
		Property newSemiMajorAxis = new Property("AU", 1.5);
		Property newMass = new Property("JupiterMass", 3.5);
		Property newRadius = new Property("km", 444444);
		List<Moon> newMoons = new LinkedList<>();
		planets.add(new Planet("New Planet", newMoons, newRadius, newOrbitalPeriod, newOrbitalSpeed, newEccentricity,
				newSemiMajorAxis, newMass));
		SolarSystem newSolarSystem = new SolarSystem(newName, comets, planets, new Star("new Star", "binary"));
		SolarSystem expectedSolarSystem = newSolarSystem;
		us.updateSolarSystem(solarSystem.getName(), newSolarSystem);

		SolarSystem actualSolarSystem = us.findSolarSystemByName(newName);

		Assert.assertNotNull(actualSolarSystem);

		Assert.assertEquals(expectedSolarSystem, actualSolarSystem);

	}

	@Test
	public void testUpdateSolarSystemName() throws UniverseException {
		SolarSystem solarSystem = solarSystems.get(0);
		String newName = "New Solar System";

		us.updateSolarSystemName(solarSystem.getName(), newName);

		SolarSystem actualSolarSystem = us.findSolarSystemByName(newName);

		Assert.assertNotNull(actualSolarSystem);

		Assert.assertEquals(newName, actualSolarSystem.getName());

	}

	@Test
	public void testAvgOrbitalSpeedOfPlanetsThatHaveMoonsWithRadiusBetween() throws UniverseException {
		Double lowerBound = new Double(3500);
		Double upperBound = new Double(4000);
		Double actualValue = us.avgOrbitalSpeedOfPlanetsThatHaveMoonsWithRadiusBetween(lowerBound, upperBound);
		Double expectedValue;
		List<Double> orbitalSpeedsOfPlanetsWhichHaveMoonsWithRadiusBetween = new ArrayList<>();
		for (Galaxy galaxy : galaxies) {
			for (SolarSystem solarSystem : galaxy.getSolarsystems()) {
				for (Planet planet : solarSystem.getPlanets()) {
					boolean containsThatKindOfMoon = false;
					for (Moon moon : planet.getMoons()) {
						double radius;
						if (moon.getRadius().getUnit().equals("m")) {
							radius = moon.getRadius().getValue() / 1000;
						} else if (moon.getRadius().getUnit().equals("solarRadius")) {
							radius = moon.getRadius().getValue() * 695700;
						} else {
							radius = moon.getRadius().getValue();
						}
						if (radius >= lowerBound && radius <= upperBound) {
							containsThatKindOfMoon = true;
							break;
						}
					}
					if (containsThatKindOfMoon) {
						double orbitalSpeed;
						if (planet.getOrbitalSpeed().getUnit().equals("mps")) {
							orbitalSpeed = planet.getOrbitalSpeed().getValue() * 3.6;
						} else if (planet.getOrbitalSpeed().getUnit().equals("kps")) {
							orbitalSpeed = planet.getOrbitalSpeed().getValue() * 3600;
						} else {
							orbitalSpeed = planet.getOrbitalSpeed().getValue();
						}
						orbitalSpeedsOfPlanetsWhichHaveMoonsWithRadiusBetween.add(orbitalSpeed);
					}
				}
			}
		}
		double sum = 0;
		for (Double radius : orbitalSpeedsOfPlanetsWhichHaveMoonsWithRadiusBetween) {
			sum += radius;
		}
		expectedValue = sum / orbitalSpeedsOfPlanetsWhichHaveMoonsWithRadiusBetween.size();
		Assert.assertEquals(expectedValue, actualValue);
	}

	@Test
	public void testCometsThatHaveMoreThanOneMineralOrderedByQuantitySumDesc() throws UniverseException {
		List<Comet> actualComets = us.cometsThatHaveMoreThanOneMineralOrderedByQuantitySumDesc();
		List<Comet> expectedComets = new LinkedList<>();
		for (Galaxy galaxy : galaxies) {
			for (SolarSystem solarSystem : galaxy.getSolarsystems()) {
				for (Comet comet : solarSystem.getComets()) {
					if (comet.getMinerals().size() > 1) {
						expectedComets.add(comet);
					}
				}
			}
		}
		Collections.sort(actualComets,
				(o1, o2) -> Double.compare(sumOfQuantityOfMinerals(o2), sumOfQuantityOfMinerals(o1)));
	}

	private double sumOfQuantityOfMinerals(Comet comet) {
		double sum = 0;
		for (Mineral mineral : comet.getMinerals()) {
			double quantity;
			if (mineral.getQuantity().getUnit().equals("kg")) {
				quantity = mineral.getQuantity().getValue() * 1000;
			} else {
				quantity = mineral.getQuantity().getValue();
			}
			sum += quantity;
		}
		return sum;
	}
}
