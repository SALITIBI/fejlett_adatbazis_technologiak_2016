package hu.unideb.inf.universe.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Galaxy {
	@XmlAttribute
	private String name;
	@XmlElementWrapper(name="solarSystems")
	@XmlElement(name="solarSystem")	
	private List<SolarSystem> solarsystems;
	
	public Galaxy() {
		super();
	}
	public Galaxy(String name, List<SolarSystem> solarsystems) {
		super();
		this.name = name;
		this.solarsystems = solarsystems;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<SolarSystem> getSolarsystems() {
		return solarsystems;
	}
	public void setSolarsystems(List<SolarSystem> solarsystems) {
		this.solarsystems = solarsystems;
	}
	@Override
	public String toString() {
		return "Galaxy [name=" + name + ", solarsystems=" + solarsystems + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((solarsystems == null) ? 0 : solarsystems.hashCode());
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
		Galaxy other = (Galaxy) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (solarsystems == null) {
			if (other.solarsystems != null)
				return false;
		} else if (!solarsystems.equals(other.solarsystems))
			return false;
		return true;
	}
	
	
}
