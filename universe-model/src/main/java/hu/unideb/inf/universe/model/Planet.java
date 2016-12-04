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
	private Property mass;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eccentricity == null) ? 0 : eccentricity.hashCode());
		result = prime * result + ((mass == null) ? 0 : mass.hashCode());
		result = prime * result + ((moons == null) ? 0 : moons.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((orbitalPeriod == null) ? 0 : orbitalPeriod.hashCode());
		result = prime * result + ((orbitalSpeed == null) ? 0 : orbitalSpeed.hashCode());
		result = prime * result + ((radius == null) ? 0 : radius.hashCode());
		result = prime * result + ((semiMajorAxis == null) ? 0 : semiMajorAxis.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Planet other = (Planet) obj;
		if (eccentricity == null) {
			if (other.eccentricity != null)
				return false;
		} else if (!eccentricity.equals(other.eccentricity))
			return false;
		if (mass == null) {
			if (other.mass != null)
				return false;
		} else if (!mass.equals(other.mass))
			return false;
		if (moons == null) {
			if (other.moons != null)
				return false;
		} else if (!moons.equals(other.moons))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (orbitalPeriod == null) {
			if (other.orbitalPeriod != null)
				return false;
		} else if (!orbitalPeriod.equals(other.orbitalPeriod))
			return false;
		if (orbitalSpeed == null) {
			if (other.orbitalSpeed != null)
				return false;
		} else if (!orbitalSpeed.equals(other.orbitalSpeed))
			return false;
		if (radius == null) {
			if (other.radius != null)
				return false;
		} else if (!radius.equals(other.radius))
			return false;
		if (semiMajorAxis == null) {
			if (other.semiMajorAxis != null)
				return false;
		} else if (!semiMajorAxis.equals(other.semiMajorAxis))
			return false;
		return true;
	}

	
}
