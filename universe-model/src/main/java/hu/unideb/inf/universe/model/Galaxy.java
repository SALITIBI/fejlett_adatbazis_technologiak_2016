package hu.unideb.inf.universe.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
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
	
	
}
