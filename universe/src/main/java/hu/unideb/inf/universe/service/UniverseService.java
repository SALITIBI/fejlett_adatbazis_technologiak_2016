package hu.unideb.inf.universe.service;

import java.util.List;

import hu.unideb.inf.universe.exception.UniverseException;
import hu.unideb.inf.universe.model.Comet;
import hu.unideb.inf.universe.model.Galaxy;
import hu.unideb.inf.universe.model.Mineral;
import hu.unideb.inf.universe.model.Moon;
import hu.unideb.inf.universe.model.Planet;
import hu.unideb.inf.universe.model.Property;
import hu.unideb.inf.universe.model.SolarSystem;
import hu.unideb.inf.universe.model.Star;

public interface UniverseService {

	boolean validate();

	List<Galaxy> findAllGalaxies() throws UniverseException;

	List<SolarSystem> findAllSolarSystemsInGalaxy(Galaxy galaxy) throws UniverseException;

	Star findStarInSolarSystem(SolarSystem solarSystem) throws UniverseException;

	List<Planet> findAllPlanetsInSolarSystem(SolarSystem solarSystem) throws UniverseException;

	List<Comet> findAllCometsInSolarSystem(SolarSystem solarSystem) throws UniverseException;

	List<Moon> findAllMoonsAroundPlanet(Planet planet) throws UniverseException;

	List<Mineral> findAllMineralsInComet(Comet comet) throws UniverseException;

	Mineral findMineralByComet(String cometName, String mineralName) throws UniverseException;

	void deleteMineralOnComet(String cometName, String mineralName) throws UniverseException;

	void deleteComet(String cometName) throws UniverseException;

	void deleteMoon(String moonName) throws UniverseException;

	void deletePlanet(String planetName) throws UniverseException;

	void deleteSolarSystem(String solarSystemName) throws UniverseException;

	void deleteGalaxy(String galaxyName) throws UniverseException;

	void updateMineralOnComet(String cometName, String mineralName, Property newQuantity) throws UniverseException;

	void doSomething() throws UniverseException;

}