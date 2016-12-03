package hu.unideb.inf.universe.service;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQResultSequence;

import hu.unideb.inf.jaxb.JAXBUtil;
import hu.unideb.inf.universe.main.Application;
import hu.unideb.inf.universe.model.Galaxy;
import hu.unideb.inf.universe.model.Planet;
import hu.unideb.inf.universe.model.SolarSystem;
import hu.unideb.inf.universe.model.Star;
import javax.xml.namespace.QName;
import javax.xml.xquery.XQItemType;
import javax.xml.xquery.XQPreparedExpression;

public class UniverseService {

	private XQConnection xqc;

	public UniverseService(XQConnection xqc) {
		if (xqc == null) {
			throw new NullPointerException("Argument xqc is null!");
		}
		this.xqc = xqc;
	}

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

	public List<Galaxy> findAllGalaxies() throws XQException, JAXBException {
		List<Galaxy> galaxies = new LinkedList<>();

		XQExpression xqe = xqc.createExpression();
		XQResultSequence rs = xqe.executeQuery("for $galaxy in db:open('universe')//galaxies/* return $galaxy");

		while (rs.next()) {
			Galaxy galaxy = JAXBUtil.fromXML(Galaxy.class, rs.getItemAsString(null));
			galaxies.add(galaxy);
		}

		return galaxies;
	}

	public List<SolarSystem> findAllSolarSystemsInGalaxy(Galaxy galaxy) throws XQException, JAXBException {
		List<SolarSystem> solarSystems = new LinkedList<>();

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

		return solarSystems;
	}

	public Star findStarInSolarSystem(SolarSystem solarSystem) throws XQException, JAXBException {
		XQPreparedExpression expr = xqc.prepareExpression(
			"declare variable $name external;"
			+ " for $star in db:open('universe')//galaxies/galaxy/solarSystems/solarSystem[@name=$name]/star"
			+ " return $star");
		expr.bindString(new QName("name"), solarSystem.getName(), xqc.createAtomicType(XQItemType.XQBASETYPE_STRING));
		XQResultSequence rs = expr.executeQuery();

		if (rs.next()) {
			return JAXBUtil.fromXML(Star.class, rs.getItemAsString(null));
		}

		return null;
	}
	
	public List<Planet> findAllPlanetsInSolarSystem(SolarSystem solarSystem) throws XQException, JAXBException {
		List<Planet> planets = new LinkedList<>();

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

		return planets;
	}

	public void doSomething() throws XQException {
		XQExpression xqe = xqc.createExpression();
		XQResultSequence rs = xqe.executeQuery("for $x in db:open('universe')//* return data($x)");
		rs.writeSequence(System.out, null);
		System.out.println();
	}

}
