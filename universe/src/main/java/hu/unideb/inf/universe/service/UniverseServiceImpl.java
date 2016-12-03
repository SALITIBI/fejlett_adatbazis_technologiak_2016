package hu.unideb.inf.universe.service;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQItemType;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;

import hu.unideb.inf.jaxb.JAXBUtil;
import hu.unideb.inf.universe.exception.UniverseException;
import hu.unideb.inf.universe.main.Application;
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

	public UniverseServiceImpl(XQConnection xqc) {
		if (xqc == null) {
			throw new NullPointerException("Argument xqc is null!");
		}
		this.xqc = xqc;
	}

	@Override
	public boolean validate() {
		try {
			XQExpression xqe = xqc.createExpression();
			String universeXsdPath = Application.class.getClassLoader().getResource("universe.xsd").getPath();
			xqe.executeQuery("for $doc in db:open('universe') return validate:xsd($doc, '" + universeXsdPath + "')");
			return true;
		} catch (XQException ex) {
			return false;
		}
	}

	@Override
	public List<Galaxy> findAllGalaxies() throws UniverseException {
		List<Galaxy> galaxies = new LinkedList<>();

		try {
			XQExpression xqe = xqc.createExpression();
			XQResultSequence rs = xqe.executeQuery("for $galaxy in db:open('universe')//galaxies/* return $galaxy");

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
	public List<SolarSystem> findAllSolarSystemsInGalaxy(Galaxy galaxy) throws UniverseException {
		List<SolarSystem> solarSystems = new LinkedList<>();

		try {
			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $name external;"
				+ " for $solarSystem in db:open('universe')//galaxies/galaxy[@name=$name]/solarSystems/*"
				+ " return $solarSystem");
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
	public Star findStarInSolarSystem(SolarSystem solarSystem) throws UniverseException {
		Star star = null;
		
		try {
			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $name external;"
				+ " for $star in db:open('universe')//galaxies/galaxy/solarSystems/solarSystem[@name=$name]/star"
				+ " return $star");
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
	public List<Planet> findAllPlanetsInSolarSystem(SolarSystem solarSystem) throws UniverseException {
		List<Planet> planets = new LinkedList<>();

		try {
			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $name external;"
				+ " for $planet in db:open('universe')//galaxies/galaxy/solarSystems/solarSystem[@name=$name]/planets/planet"
				+ " return $planet");
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
				"declare variable $name external;"
				+ " for $comet in db:open('universe')//galaxies/galaxy/solarSystems/solarSystem[@name=$name]/comets/comet"
				+ " return $comet");
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
				"declare variable $name external;"
				+ " for $moon in db:open('universe')//galaxies/galaxy/solarSystems/solarSystem/planets/planet[@name=$name]/moons/moon"
				+ " return $moon");
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
				"declare variable $name external;"
				+ " for $mineral in db:open('universe')//galaxies/galaxy/solarSystems/solarSystem/comets/comet[@name=$name]/minerals/mineral"
				+ " return $mineral");
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
	public Mineral findMineralByComet(String cometName, String mineralName) throws UniverseException {
		Mineral mineral = null;
		
		try {
			XQPreparedExpression expr = xqc.prepareExpression(
					"declare variable $cometName external;"
					+ " declare variable $mineralName external;"
					+ " for $mineral in db:open('universe')//galaxies/galaxy/solarSystems/solarSystem/comets/comet[@name=$cometName]/minerals/mineral[@elementName=$mineralName]"
					+ " return $mineral");
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
	public void deleteMineralOnComet(String cometName, String mineralName) throws UniverseException {
		try {
			XQPreparedExpression expr = xqc.prepareExpression(
				"declare variable $cometName external;"
				+ " declare variable $mineralName external;"
				+ " delete nodes db:open('universe')//galaxies/galaxy/solarSystems/solarSystem/comets/comet[@name=$cometName]/minerals/mineral[@elementName=$mineralName]");
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
					"declare variable $name external;"
					+ " delete nodes db:open('universe')//galaxies/galaxy/solarSystems/solarSystem/comets/comet[@name=$name]");
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
					"declare variable $name external;"
					+ " delete nodes db:open('universe')//galaxies/galaxy/solarSystems/solarSystem/planets/planet/moons/moon[@name=$name]");
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
					"declare variable $name external;"
					+ " delete nodes db:open('universe')//galaxies/galaxy/solarSystems/solarSystem/planets/planet[@name=$name]");
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
					"declare variable $name external;"
					+ " delete nodes db:open('universe')//galaxies/galaxy/solarSystems/solarSystem[@name=$name]");
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
					"declare variable $name external;"
					+ " delete nodes db:open('universe')//galaxies/galaxy[@name=$name]");
			expr.bindString(new QName("name"), galaxyName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.executeQuery();
		} catch (XQException e) {
			throw new UniverseException(e);
		}
	}
	
	@Override
	public void updateMineralOnComet(String cometName, String mineralName, Property newQuantity) throws UniverseException {
		try {
			Mineral mineral = findMineralByComet(cometName, mineralName);
			mineral.setQuantity(newQuantity);
			
			String newMineral = JAXBUtil.toXMLFragment(mineral);
			
			XQPreparedExpression expr = xqc.prepareExpression(
					"declare variable $cometName external;"
					+ " declare variable $mineralName external;"
					+ " declare variable $newMineral external;"
					+ " replace node db:open('universe')//galaxies/galaxy/solarSystems/solarSystem/comets/comet[@name=$cometName]/minerals/mineral[@elementName=$mineralName] with $newMineral");
			
			expr.bindString(new QName("cometName"), cometName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindString(new QName("mineralName"), mineralName, xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
			expr.bindDocument(new QName("newMineral"), newMineral, null, null);
			
			expr.executeQuery();
		} catch (XQException | JAXBException e) {
			throw new UniverseException(e);
		}
	}
	
	@Override
	public void doSomething() throws UniverseException {
		try {
			XQExpression xqe = xqc.createExpression();
			XQResultSequence rs = xqe.executeQuery("for $x in db:open('universe')//* return data($x)");
			rs.writeSequence(System.out, null);
			System.out.println();
		} catch (XQException e) {
			throw new UniverseException(e);
		}
	}

}
