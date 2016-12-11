package hu.unideb.inf.universe.service;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQItemType;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;

import hu.unideb.inf.jaxb.JAXBUtil;
import hu.unideb.inf.universe.exception.UniverseException;
import hu.unideb.inf.universe.model.Comet;
import hu.unideb.inf.universe.model.Galaxy;
import hu.unideb.inf.universe.model.Mineral;
import hu.unideb.inf.universe.model.Moon;
import hu.unideb.inf.universe.model.Planet;
import hu.unideb.inf.universe.model.Property;
import hu.unideb.inf.universe.model.SolarSystem;
import hu.unideb.inf.universe.model.Star;

public class UniverseServiceImpl implements UniverseService {

	private XQConnection xqc;
	private String dbName;

	public UniverseServiceImpl(XQConnection xqc, String dbName) {
		if (xqc == null) {
			throw new NullPointerException("Argument xqc is null!");
		}
		this.xqc = xqc;
		this.dbName = dbName;
	}

	@Override
	public boolean validate() {
		try {
			String universeXsdPath = getClass().getClassLoader().getResource("universe.xsd").getPath();

			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " for $doc in db:open($dbName) return validate:xsd($doc, '" + universeXsdPath + "')");
			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.executeQuery();
			return true;
		} catch (XQException ex) {
			return false;
		}
	}

	@Override
	public List<Galaxy> findAllGalaxies() throws UniverseException {
		List<Galaxy> galaxies = new LinkedList<>();

		try {
			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " for $galaxy in db:open($dbName)//galaxies/* return $galaxy");

			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));

			XQResultSequence rs = expr.executeQuery();

			while (rs.next()) {
				Galaxy galaxy = JAXBUtil.fromXML(Galaxy.class, rs.getItemAsString(null));
				galaxies.add(galaxy);
			}
		} catch (XQException | JAXBException e) {
			throw new UniverseException(e);
		}

		return galaxies;
	}

	@Override
	public List<Comet> cometsThatHaveMoreThanOneMineralOrderedByQuantitySumDesc() throws UniverseException {
		List<Comet> comets = new LinkedList<>();
		
		try {
			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " for $comet in db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/comets/comet"
				+ " let $mineralCount := count($comet/minerals/mineral)"
				+ " let $quantitySum := sum("
				+ " 	if($comet/minerals/mineral/quantity/@unit = 'kg') then"
				+ " 		$comet/minerals/mineral/quantity * 1000"
				+ " 	else"
				+ " 		$comet/minerals/mineral/quantity"
				+ " )"
				+ " where $mineralCount > 1"
				+ " order by $quantitySum descending"
				+ " return $comet");
			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));

			XQResultSequence rs = expr.executeQuery();

			while (rs.next()) {
				Comet comet = JAXBUtil.fromXML(Comet.class, rs.getItemAsString(null));
				comets.add(comet);
			}
		} catch (XQException | JAXBException e) {
			throw new UniverseException(e);
		}

		return comets;
	}

	@Override
	public Double avgOrbitalSpeedOfPlanetsThatHaveMoonsWithRadiusBetween(double lowerBound, double upperBound) throws UniverseException {
		Double avgRotationSpeed = null;
		
		try {
			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " declare variable $lowerBound external;"
				+ " declare variable $upperBound external;"
				+ " let $avg := avg("
				+ " 	for $planet in db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/planets/planet"
				+ " 		let $orbSpeed := "
				+ " 			if ($planet/orbitalSpeed/@unit = 'mps') then"
				+ " 				$planet/orbitalSpeed * 3.6"
				+ " 			else if ($planet/orbitalSpeed/@unit = 'kps') then"
				+ " 				$planet/orbitalSpeed * 3600"
				+ " 			else"
				+ " 				$planet/orbitalSpeed"
				+ " 		for $moon in $planet/moons/moon"
				+ " 			let $moonRadius := "
				+ " 				if ($moon/radius/@unit = 'm') then"
				+ " 					$moon/radius * 0.001"
				+ "  				else if ($moon/radius/@unit = 'SolarRadius') then"
				+ " 					$moon/radius * 695700"
				+ " 				else"
				+ " 					$moon/radius"
				+ " 		where $moonRadius >= $lowerBound and $moonRadius <= $upperBound"
				+ " 		return $orbSpeed"
				+ " )"
				+ " return $avg");
			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindDouble(new QName("lowerBound"), lowerBound, xqc.createAtomicType(XQItemType.XQBASETYPE_DOUBLE));
			expr.bindDouble(new QName("upperBound"), upperBound, xqc.createAtomicType(XQItemType.XQBASETYPE_DOUBLE));
			XQResultSequence rs = expr.executeQuery();

			while (rs.next()) {
				avgRotationSpeed = Double.parseDouble(rs.getItemAsString(null));
			}
		} catch (XQException e) {
			throw new UniverseException(e);
		}
		
		return avgRotationSpeed;
	}

	@Override
	public List<SolarSystem> findSmallPlanetsGroupedBySolarSystem(double upperBound) throws UniverseException {
		List<SolarSystem> solarSystems = new LinkedList<>();
		try {

			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " declare variable $upperBound external;"
				+ " for $solarSystem in db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem"
				+ " 	let $name := $solarSystem/@name"
				+ " 	for $planet in $solarSystem/planets/planet"
				+ " 		let $radius := "
				+ " 			if ($planet/radius/@unit = 'm') then"
				+ " 				$planet/radius * 0.001"
				+ " 			else if ($planet/radius/@unit = 'SolarRadius') then"
				+ " 				$planet/radius * 695700"
				+ " 			else"
				+ " 				$planet/radius"
				+ " where $radius <= $upperBound"
				+ " group by $name"
				+ " return element solarSystem {"
				+ " 	attribute name {$name},"
				+ " 	element planets {$planet}"
				+ " }");
			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindDouble(new QName("upperBound"), upperBound, xqc.createAtomicType(XQItemType.XQBASETYPE_DOUBLE));

			XQResultSequence rs = expr.executeQuery();

			while (rs.next()) {
				SolarSystem solarSystem = JAXBUtil.fromXML(SolarSystem.class, rs.getItemAsString(null));
				solarSystems.add(solarSystem);
			}
		} catch (XQException | JAXBException e) {
			throw new UniverseException(e);
		}

		return solarSystems;
	}

	@Override
	public List<SolarSystem> findAllSolarSystemsInGalaxy(Galaxy galaxy) throws UniverseException {
		List<SolarSystem> solarSystems = new LinkedList<>();

		try {
			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " declare variable $name external;"
				+ " for $solarSystem in db:open($dbName)//galaxies/galaxy[@name=$name]/solarSystems/*"
				+ " return $solarSystem");

			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("name"), galaxy.getName(), xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));

			XQResultSequence rs = expr.executeQuery();

			while (rs.next()) {
				SolarSystem solarSystem = JAXBUtil.fromXML(SolarSystem.class, rs.getItemAsString(null));
				solarSystems.add(solarSystem);
			}
		} catch (XQException | JAXBException e) {
			throw new UniverseException(e);
		}

		return solarSystems;
	}

	@Override
	public List<Planet> findAllPlanetsInSolarSystem(SolarSystem solarSystem) throws UniverseException {
		List<Planet> planets = new LinkedList<>();

		try {
			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " declare variable $name external;"
				+ " for $planet in db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem[@name=$name]/planets/planet"
				+ " return $planet");

			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("name"), solarSystem.getName(), xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));

			XQResultSequence rs = expr.executeQuery();

			while (rs.next()) {
				Planet planet = JAXBUtil.fromXML(Planet.class, rs.getItemAsString(null));
				planets.add(planet);
			}
		} catch (XQException | JAXBException e) {
			throw new UniverseException(e);
		}

		return planets;
	}

	@Override
	public List<Comet> findAllCometsInSolarSystem(SolarSystem solarSystem) throws UniverseException {
		List<Comet> comets = new LinkedList<>();

		try {
			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " declare variable $name external;"
				+ " for $comet in db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem[@name=$name]/comets/comet"
				+ " return $comet");

			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("name"), solarSystem.getName(), xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));

			XQResultSequence rs = expr.executeQuery();

			while (rs.next()) {
				Comet comet = JAXBUtil.fromXML(Comet.class, rs.getItemAsString(null));
				comets.add(comet);
			}
		} catch (XQException | JAXBException e) {
			throw new UniverseException(e);
		}

		return comets;
	}

	@Override
	public List<Moon> findAllMoonsAroundPlanet(Planet planet) throws UniverseException {
		List<Moon> moons = new LinkedList<>();

		try {
			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " declare variable $name external;"
				+ " for $moon in db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/planets/planet[@name=$name]/moons/moon"
				+ " return $moon");

			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("name"), planet.getName(), xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));

			XQResultSequence rs = expr.executeQuery();

			while (rs.next()) {
				Moon moon = JAXBUtil.fromXML(Moon.class, rs.getItemAsString(null));
				moons.add(moon);
			}
		} catch (XQException | JAXBException e) {
			throw new UniverseException(e);
		}

		return moons;
	}

	@Override
	public List<Mineral> findAllMineralsInComet(Comet comet) throws UniverseException {
		List<Mineral> minerals = new LinkedList<>();

		try {
			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " declare variable $name external;"
				+ " for $mineral in db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/comets/comet[@name=$name]/minerals/mineral"
				+ " return $mineral");

			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("name"), comet.getName(), xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));

			XQResultSequence rs = expr.executeQuery();

			while (rs.next()) {
				Mineral mineral = JAXBUtil.fromXML(Mineral.class, rs.getItemAsString(null));
				minerals.add(mineral);
			}
		} catch (XQException | JAXBException e) {
			throw new UniverseException(e);
		}

		return minerals;
	}

	@Override
	public Planet findPlanetByName(String planetName) throws UniverseException {
		Planet planet = null;

		try {
			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " declare variable $name external;"
				+ " for $planet in db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/planets/planet[@name=$name]"
				+ " return $planet");

			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("name"), planetName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));

			XQResultSequence rs = expr.executeQuery();

			if (rs.next()) {
				planet = JAXBUtil.fromXML(Planet.class, rs.getItemAsString(null));
			}
		} catch (XQException | JAXBException e) {
			throw new UniverseException(e);
		}

		return planet;
	}

	@Override
	public Moon findMoonByName(String moonName) throws UniverseException {
		Moon moon = null;

		try {
			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " declare variable $name external;"
				+ " for $moon in db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/planets/planet/moons/moon[@name=$name]"
				+ " return $moon");

			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("name"), moonName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));

			XQResultSequence rs = expr.executeQuery();

			if (rs.next()) {
				moon = JAXBUtil.fromXML(Moon.class, rs.getItemAsString(null));
			}
		} catch (XQException | JAXBException e) {
			throw new UniverseException(e);
		}

		return moon;
	}

	@Override
	public Comet findCometByName(String cometName) throws UniverseException {
		Comet comet = null;

		try {
			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " declare variable $name external;"
				+ " for $comet in db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/comets/comet[@name=$name]"
				+ " return $comet");

			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("name"), cometName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));

			XQResultSequence rs = expr.executeQuery();

			if (rs.next()) {
				comet = JAXBUtil.fromXML(Comet.class, rs.getItemAsString(null));
			}
		} catch (XQException | JAXBException e) {
			throw new UniverseException(e);
		}

		return comet;
	}

	@Override
	public Star findStarInSolarSystem(SolarSystem solarSystem) throws UniverseException {
		Star star = null;

		try {
			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " declare variable $name external;"
				+ " for $star in db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem[@name=$name]/star"
				+ " return $star");

			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("name"), solarSystem.getName(), xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));

			XQResultSequence rs = expr.executeQuery();

			if (rs.next()) {
				star = JAXBUtil.fromXML(Star.class, rs.getItemAsString(null));
			}
		} catch (XQException | JAXBException e) {
			throw new UniverseException(e);
		}

		return star;
	}

	@Override
	public Mineral findMineralByComet(String cometName, String mineralName) throws UniverseException {
		Mineral mineral = null;

		try {
			XQPreparedExpression expr = xqc.prepareExpression("declare variable $dbName external;"
				+ " declare variable $cometName external;"
				+ " declare variable $mineralName external;"
				+ " for $mineral in db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/comets/comet[@name=$cometName]/minerals/mineral[@elementName=$mineralName]"
				+ " return $mineral");

			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("cometName"), cometName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("mineralName"), mineralName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));

			XQResultSequence rs = expr.executeQuery();

			if (rs.next()) {
				mineral = JAXBUtil.fromXML(Mineral.class, rs.getItemAsString(null));
			}
		} catch (XQException | JAXBException e) {
			throw new UniverseException(e);
		}

		return mineral;
	}

	@Override
	public Comet findCometBySolarSystem(String solarSystemName, String cometName) throws UniverseException {
		Comet comet = null;

		try {
			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " declare variable $solarSystemName external;"
				+ " declare variable $cometName external;"
				+ " for $mineral in db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem[@name=$solarSystemName]/comets/comet[@name=$cometName]"
				+ " return $mineral");

			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("solarSystemName"), solarSystemName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("cometName"), cometName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));

			XQResultSequence rs = expr.executeQuery();

			if (rs.next()) {
				comet = JAXBUtil.fromXML(Comet.class, rs.getItemAsString(null));
			}
		} catch (XQException | JAXBException e) {
			throw new UniverseException(e);
		}

		return comet;
	}

	@Override
	public SolarSystem findSolarSystemByName(String solarSystemName) throws UniverseException {
		SolarSystem solarSystem = null;

		try {
			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " declare variable $name external;"
				+ " for $solarSystem in db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem[@name=$name]"
				+ " return $solarSystem");

			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("name"), solarSystemName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));

			XQResultSequence rs = expr.executeQuery();

			if (rs.next()) {
				solarSystem = JAXBUtil.fromXML(SolarSystem.class, rs.getItemAsString(null));
			}
		} catch (XQException | JAXBException e) {
			throw new UniverseException(e);
		}

		return solarSystem;
	}

	@Override
	public Galaxy findGalaxyByName(String galaxyName) throws UniverseException {
		Galaxy galaxy = null;

		try {
			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " declare variable $name external;"
				+ " for $solarSystem in db:open($dbName)//galaxies/galaxy[@name=$name]"
				+ " return $solarSystem");

			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("name"), galaxyName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));

			XQResultSequence rs = expr.executeQuery();

			if (rs.next()) {
				galaxy = JAXBUtil.fromXML(Galaxy.class, rs.getItemAsString(null));
			}
		} catch (XQException | JAXBException e) {
			throw new UniverseException(e);
		}

		return galaxy;
	}

	@Override
	public void deleteMineralOnComet(String cometName, String mineralName) throws UniverseException {
		try {
			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " declare variable $cometName external;"
				+ " declare variable $mineralName external;"
				+ " delete nodes db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/comets/comet[@name=$cometName]/minerals/mineral[@elementName=$mineralName]");

			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("cometName"), cometName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("mineralName"), mineralName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));

			expr.executeQuery();
		} catch (XQException e) {
			throw new UniverseException(e);
		}
	}

	@Override
	public void deleteComet(String cometName) throws UniverseException {
		try {
			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " declare variable $name external;"
				+ " delete nodes db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/comets/comet[@name=$name]");

			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("name"), cometName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));

			expr.executeQuery();
		} catch (XQException e) {
			throw new UniverseException(e);
		}
	}

	@Override
	public void deleteMoon(String moonName) throws UniverseException {
		try {
			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " declare variable $name external;"
				+ " delete nodes db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/planets/planet/moons/moon[@name=$name]");

			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("name"), moonName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));

			expr.executeQuery();
		} catch (XQException e) {
			throw new UniverseException(e);
		}
	}

	@Override
	public void deletePlanet(String planetName) throws UniverseException {
		try {
			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " declare variable $name external;"
				+ " delete nodes db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/planets/planet[@name=$name]");

			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("name"), planetName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));

			expr.executeQuery();
		} catch (XQException e) {
			throw new UniverseException(e);
		}
	}

	@Override
	public void deleteSolarSystem(String solarSystemName) throws UniverseException {
		try {
			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " declare variable $name external;"
				+ " delete nodes db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem[@name=$name]");

			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("name"), solarSystemName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));

			expr.executeQuery();
		} catch (XQException e) {
			throw new UniverseException(e);
		}
	}

	@Override
	public void deleteGalaxy(String galaxyName) throws UniverseException {
		try {
			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " declare variable $name external;"
				+ " delete nodes db:open($dbName)//galaxies/galaxy[@name=$name]");

			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("name"), galaxyName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));

			expr.executeQuery();
		} catch (XQException e) {
			throw new UniverseException(e);
		}
	}

	@Override
	public void updatePlanetRadius(String planetName, Property newRadius) throws UniverseException {
		try {
			Planet planet = findPlanetByName(planetName);
			planet.setRadius(newRadius);

			String newPlanet = JAXBUtil.toXMLFragment(planet);

			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " declare variable $planetName external;"
				+ " declare variable $newPlanet external;"
				+ " replace node db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/planets/planet[@name=$planetName] with $newPlanet");

			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("planetName"), planetName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindDocument(new QName("newPlanet"), newPlanet, null, null);

			expr.executeQuery();
		} catch (XQException | JAXBException e) {
			throw new UniverseException(e);
		}
	}

	@Override
	public void updateMoonRadius(String moonName, Property newRadius) throws UniverseException {
		try {
			Moon moon = findMoonByName(moonName);
			moon.setRadius(newRadius);

			String newMoon = JAXBUtil.toXMLFragment(moon);

			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " declare variable $moonName external;"
				+ " declare variable $newMoon external;"
				+ " replace node db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/planets/planet/moons/moon[@name=$moonName] with $newMoon");

			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("moonName"), moonName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindDocument(new QName("newMoon"), newMoon, null, null);

			expr.executeQuery();
		} catch (XQException | JAXBException e) {
			throw new UniverseException(e);
		}
	}

	@Override
	public void updateMineralOnComet(String cometName, String mineralName, Mineral newMineral) throws UniverseException {
		try {
			

			String newMineralAsXML = JAXBUtil.toXMLFragment(newMineral);

			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " declare variable $cometName external;"
				+ " declare variable $mineralName external;"
				+ " declare variable $newMineral external;"
				+ " replace node db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/comets/comet[@name=$cometName]/minerals/mineral[@elementName=$mineralName] with $newMineral");

			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("cometName"), cometName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("mineralName"), mineralName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindDocument(new QName("newMineral"), newMineralAsXML, null,null);
			expr.executeQuery();
		} catch (XQException | JAXBException e) {
			throw new UniverseException(e);
		}
	}

	@Override
	public void updateCometOrbitalPeriod(String cometName, Property newOrbitalPeriod) throws UniverseException {
		try {
			Comet comet = findCometByName(cometName);
			comet.setOrbitalPeriod(newOrbitalPeriod);

			String newComet = JAXBUtil.toXMLFragment(comet);

			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " declare variable $cometName external;"
				+ " declare variable $newComet external;"
				+ " replace node db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/comets/comet[@name=$cometName] with $newComet");

			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("cometName"), cometName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindDocument(new QName("newComet"), newComet, null, null);

			expr.executeQuery();
		} catch (XQException | JAXBException e) {
			throw new UniverseException(e);
		}
	}

	@Override
	public void updateComet(String oldCometName, Comet newComet) throws UniverseException {
		try {

			String newCometAsXml = JAXBUtil.toXMLFragment(newComet);

			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " declare variable $oldCometName external;"
				+ " declare variable $newComet external;"
				+ " replace node db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/comets/comet[@name=$oldCometName] with $newComet");

			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("oldCometName"), oldCometName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindDocument(new QName("newComet"), newCometAsXml, null, null);

			expr.executeQuery();
		} catch (XQException | JAXBException e) {
			throw new UniverseException(e);
		}
	}

	@Override
	public void updateStarInSolarSystem(String solarSystemName, Star star) throws UniverseException {
		try {
			String newStar = JAXBUtil.toXMLFragment(star);

			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " declare variable $solarSystemName external;"
				+ " declare variable $newStar external;"
				+ " replace node db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem[@name=$solarSystemName]/star with $newStar");

			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("solarSystemName"), solarSystemName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindDocument(new QName("newStar"), newStar, null, null);

			expr.executeQuery();
		} catch (XQException | JAXBException e) {
			throw new UniverseException(e);
		}
	}

	@Override
	public void updatePlanet(String planetName, Planet planet) throws UniverseException {
		try {
			String newPlanet = JAXBUtil.toXMLFragment(planet);

			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " declare variable $planetName external;"
				+ " declare variable $newPlanet external;"
				+ " replace node db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/planets/planet[@name=$planetName] with $newPlanet");

			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("planetName"), planetName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindDocument(new QName("newPlanet"), newPlanet, null, null);

			expr.executeQuery();
		} catch (XQException | JAXBException e) {
			throw new UniverseException(e);
		}
	}

	@Override
	public void updateMoonForPlanet(String moonName, String planetName, Moon moon) throws UniverseException {
		try {
			String newMoon = JAXBUtil.toXMLFragment(moon);

			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " declare variable $planetName external;"
				+ " declare variable $moonName external;"
				+ " declare variable $newMoon external;"
				+ " replace node db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/planets/planet[@name=$planetName]/moons/moon[@name=$moonName] with $newMoon");

			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("planetName"), planetName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("moonName"), moonName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindDocument(new QName("newMoon"), newMoon, null, null);

			expr.executeQuery();
		} catch (XQException | JAXBException e) {
			throw new UniverseException(e);
		}
	}

	@Override
	public void updateGalaxy(String galaxyName, Galaxy galaxy) throws UniverseException {
		try {
			String newGalaxy = JAXBUtil.toXMLFragment(galaxy);
			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " declare variable $oldGalaxyName external;"
				+ " declare variable $newGalaxy external;"
				+ " replace node db:open($dbName)//galaxies/galaxy[@name=$oldGalaxyName] with $newGalaxy");

			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("oldGalaxyName"), galaxyName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindDocument(new QName("newGalaxy"), newGalaxy, null, null);

			expr.executeQuery();
		} catch (XQException | JAXBException e) {
			throw new UniverseException(e);
		}
	}

	@Override
	public void updateGalaxyName(String oldGalaxyName, String newGalaxyName) throws UniverseException {
		Galaxy galaxy = findGalaxyByName(oldGalaxyName);
		galaxy.setName(newGalaxyName);
		updateGalaxy(oldGalaxyName, galaxy);
	}

	@Override
	public void updateSolarSystem(String oldSolarSystemName, SolarSystem newSolarSystem) throws UniverseException {
		try {
			String newSolarSystemAsXml = JAXBUtil.toXMLFragment(newSolarSystem);
			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $dbName external;"
				+ " declare variable $oldSolarSystemName external;"
				+ " declare variable $newSolarSystem external;"
				+ " replace node db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem[@name=$oldSolarSystemName] with $newSolarSystem");

			expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("oldSolarSystemName"), oldSolarSystemName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindDocument(new QName("newSolarSystem"), newSolarSystemAsXml, null, null);

			expr.executeQuery();

		} catch (XQException | JAXBException e) {
			throw new UniverseException(e);
		}
	}

	@Override
	public void updateSolarSystemName(String oldSolarSystemName, String newSolarSystemName) throws UniverseException {
		SolarSystem solarSystem = findSolarSystemByName(oldSolarSystemName);
		solarSystem.setName(newSolarSystemName);
		updateSolarSystem(oldSolarSystemName, solarSystem);
	}

	@Override
	public void addPlanetToSolarSystem(String solarSystemName, String planetName, Property radius, Property orbitalPeriod,
		Property orbitalSpeed, Property eccentricity, Property semiMajorAxis, Property mass) throws UniverseException {
		if (findPlanetByName(planetName) == null) {
			try {
				Planet planet = new Planet(planetName, new LinkedList<>(), radius, orbitalPeriod, orbitalSpeed, eccentricity, semiMajorAxis,
					mass);

				String newPlanet = JAXBUtil.toXMLFragment(planet);

				XQPreparedExpression expr = xqc.prepareExpression(
					"declare variable $dbName external;"
					+ " declare variable $solarSystemName external;"
					+ " declare variable $newPlanet external;"
					+ " insert node ($newPlanet) into db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem[@name=$solarSystemName]/planets");

				expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
				expr.bindString(new QName("solarSystemName"), solarSystemName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
				expr.bindDocument(new QName("newPlanet"), newPlanet, null, null);

				expr.executeQuery();
			} catch (XQException | JAXBException e) {
				throw new UniverseException(e);
			}
		}
	}

	@Override
	public void addMoonToPlanet(String planetName, String moonName, Property radius) throws UniverseException {
		if (findMoonByName(moonName) == null) {
			try {
				Moon moon = new Moon(moonName, radius);
				String newMoon = JAXBUtil.toXMLFragment(moon);

				XQPreparedExpression expr = xqc.prepareExpression(
					"declare variable $dbName external;"
					+ " declare variable $planetName external;"
					+ " declare variable $newMoon external;"
					+ " insert node ($newMoon) into db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/planets/planet[@name=$planetName]/moons");

				expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
				expr.bindString(new QName("planetName"), planetName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
				expr.bindDocument(new QName("newMoon"), newMoon, null, null);

				expr.executeQuery();
			} catch (XQException | JAXBException e) {
				throw new UniverseException(e);
			}
		}
	}

	@Override
	public void addMineralToComet(String cometName, String mineralName, Property quantity) throws UniverseException {
		if (findMineralByComet(cometName, mineralName) == null) {
			try {
				Mineral mineral = new Mineral(mineralName, quantity);
				String newMineral = JAXBUtil.toXMLFragment(mineral);

				XQPreparedExpression expr = xqc.prepareExpression(
					"declare variable $dbName external;"
					+ " declare variable $cometName external;"
					+ " declare variable $newMineral external;"
					+ " insert node ($newMineral) into db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/comets/comet[@name=$cometName]/minerals");

				expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
				expr.bindString(new QName("cometName"), cometName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
				expr.bindDocument(new QName("newMineral"), newMineral, null, null);

				expr.executeQuery();
			} catch (XQException | JAXBException e) {
				throw new UniverseException(e);
			}
		}
	}

	@Override
	public void addCometToSolarSystem(String solarSystemName, String cometName, Property orbitalPeriod) throws UniverseException {
		if (findCometByName(cometName) == null) {
			try {
				Comet comet = new Comet(cometName, orbitalPeriod, new LinkedList<>());
				String newComet = JAXBUtil.toXMLFragment(comet);

				XQPreparedExpression expr = xqc.prepareExpression(
					"declare variable $dbName external;"
					+ " declare variable $solarSystemName external;"
					+ " declare variable $newComet external;"
					+ " insert node ($newComet) into db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem[@name=$solarSystemName]/comets");

				expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
				expr.bindString(new QName("solarSystemName"), solarSystemName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
				expr.bindDocument(new QName("newComet"), newComet, null, null);

				expr.executeQuery();
			} catch (XQException | JAXBException e) {
				throw new UniverseException(e);
			}
		}
	}

	@Override
	public void addSolarSystemToGalaxy(String galaxyName, String solarSystemName, String starName, String starType)
		throws UniverseException {
		if (findSolarSystemByName(solarSystemName) == null) {
			try {
				Star star = new Star(starName, starType);
				SolarSystem solarSystem = new SolarSystem(solarSystemName, new LinkedList<>(), new LinkedList<>(), star);
				String newSolarSystem = JAXBUtil.toXMLFragment(solarSystem);
				XQPreparedExpression expr = xqc.prepareExpression(
					"declare variable $dbName external;"
					+ " declare variable $galaxyName external;"
					+ " declare variable $newSolarSystem external;"
					+ " insert node ($newSolarSystem) into db:open($dbName)//galaxies/galaxy[@name=$galaxyName]/solarSystems");

				expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
				expr.bindString(new QName("galaxyName"), galaxyName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
				expr.bindDocument(new QName("newSolarSystem"), newSolarSystem, null, null);

				expr.executeQuery();
			} catch (XQException | JAXBException e) {
				throw new UniverseException(e);
			}
		}
	}

	@Override
	public void addGalaxyToUniverse(String galaxyName) throws UniverseException {
		if (findGalaxyByName(galaxyName) == null) {
			try {
				Galaxy galaxy = new Galaxy(galaxyName, new LinkedList<>());
				String newGalaxy = JAXBUtil.toXMLFragment(galaxy);
				XQPreparedExpression expr = xqc.prepareExpression(
					"declare variable $dbName external;"
					+ " declare variable $newGalaxy external;"
					+ " insert node ($newGalaxy) into db:open($dbName)//galaxies");

				expr.bindString(new QName("dbName"), dbName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
				expr.bindDocument(new QName("newGalaxy"), newGalaxy, null, null);

				expr.executeQuery();
			} catch (XQException | JAXBException e) {
				throw new UniverseException(e);
			}
		}
	}

}
