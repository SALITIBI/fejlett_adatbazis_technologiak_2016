package hu.unideb.inf.universe.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

@XmlType(propOrder = { "radius", "orbitalPeriod", "orbitalSpeed", "eccentricity", "semiMajorAxis", "mass", "moons" })
public class Planet {
	@XmlAttribute
	private String name;
	@XmlElementWrapper(name = "moons")
	@XmlElement(name = "moon")
	private List<Moon> moons;
	@XmlElement
	private Property radius;
	@XmlElement
	private Property orbitalPeriod;
	@XmlElement
	private Property orbitalSpeed;
	@XmlElement
	private Property eccentricity;
	@XmlElement
	private Property semiMajorAxis;
	@XmlElement
	private Property mass;;

	public Planet(String name, List<Moon> moons, Property radius, Property orbitalPeriod, Property orbitalSpeed,
			Property eccentricity, Property semiMajorAxis, Property mass) {
		super();
		this.name = name;
		this.moons = moons;
		this.radius = radius;
		this.orbitalPeriod = orbitalPeriod;
		this.orbitalSpeed = orbitalSpeed;
		this.eccentricity = eccentricity;
		this.semiMajorAxis = semiMajorAxis;
		this.mass = mass;
	}

	public Planet() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Moon> getMoons() {
		return moons;
	}

	public void setMoons(List<Moon> moons) {
		this.moons = moons;
	}

	public Property getRadius() {
		return radius;
	}

	public void setRadius(Property radius) {
		this.radius = radius;
	}

	public Property getOrbitalPeriod() {
		return orbitalPeriod;
	}

	public void setOrbitalPeriod(Property orbitalPeriod) {
		this.orbitalPeriod = orbitalPeriod;
	}

	public Property getOrbitalSpeed() {
		return orbitalSpeed;
	}

	public void setOrbitalSpeed(Property orbitalSpeed) {
		this.orbitalSpeed = orbitalSpeed;
	}

	public Property getEccentricity() {
		return eccentricity;
	}

	public void setEccentricity(Property eccentricity) {
		this.eccentricity = eccentricity;
	}

	public Property getSemiMajorAxis() {
		return semiMajorAxis;
	}

	public void setSemiMajorAxis(Property semiMajorAxis) {
		this.semiMajorAxis = semiMajorAxis;
	}

	public Property getMass() {
		return mass;
	}

	public void setMass(Property mass) {
		this.mass = mass;
	}

	@Override
	public String toString() {
		return "Planet [name=" + name + ", moons=" + moons + ", radius=" + radius + ", orbitalPeriod=" + orbitalPeriod
				+ ", orbitalSpeed=" + orbitalSpeed + ", eccentricity=" + eccentricity + ", semiMajorAxis="
				+ semiMajorAxis + ", mass=" + mass + "]";
	}

}
