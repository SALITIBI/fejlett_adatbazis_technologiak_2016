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
@XmlType(
		propOrder = {
			"star",
			"planets",
			"comets"
		}
	)
public class SolarSystem {
	@XmlAttribute
	private String name;
	@XmlElementWrapper(name="comets")
	@XmlElement(name="comet")	
	private List<Comet> comets;
	@XmlElementWrapper(name="planets")
	@XmlElement(name="planet")	
	private List<Planet> planets;
	@XmlElement
	private Star star;
	
	public SolarSystem() {
		super();
	}

	public SolarSystem(String name, List<Comet> comets, List<Planet> planets, Star star) {
		super();
		this.name = name;
		this.comets = comets;
		this.planets = planets;
		this.star = star;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Comet> getComets() {
		return comets;
	}

	public void setComets(List<Comet> comets) {
		this.comets = comets;
	}

	public List<Planet> getPlanets() {
		return planets;
	}

	public void setPlanets(List<Planet> planets) {
		this.planets = planets;
	}

	public Star getStar() {
		return star;
	}

	public void setStar(Star star) {
		this.star = star;
	}

	@Override
	public String toString() {
		return "SolarSystem [name=" + name + ", comets=" + comets + ", planets=" + planets + ", star=" + star + "]";
	}
	
	
	
}
