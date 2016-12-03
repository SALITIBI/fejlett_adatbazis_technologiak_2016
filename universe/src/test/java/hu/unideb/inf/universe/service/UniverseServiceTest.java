package hu.unideb.inf.universe.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import hu.unideb.inf.universe.exception.UniverseException;
import hu.unideb.inf.universe.model.Comet;
import hu.unideb.inf.universe.model.Galaxy;
import hu.unideb.inf.universe.model.Mineral;
import hu.unideb.inf.universe.model.Property;
import hu.unideb.inf.universe.model.SolarSystem;

public class UniverseServiceTest extends AbstractTest {

	private List<Galaxy> galaxies;
	private List<SolarSystem> solarSystems;
	private List<Comet> comets;
	private List<Mineral> minerals;

	@Override
	public void setUp() throws UniverseException {
		super.setUp();

		galaxies = us.findAllGalaxies();
		solarSystems = us.findAllSolarSystemsInGalaxy(galaxies.get(0));
		comets = us.findAllCometsInSolarSystem(solarSystems.get(0));
		minerals = us.findAllMineralsInComet(comets.get(0));
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

}
