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
public class Comet {
	@XmlAttribute
	private String name;
	@XmlElement
	private Property orbitalPeriod;
	@XmlElementWrapper(name="minerals")
	@XmlElement(name="mineral")	
	private List<Mineral> minerals;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Property getOrbitalPeriod() {
		return orbitalPeriod;
	}

	public void setOrbitalPeriod(Property orbitalPeriod) {
		this.orbitalPeriod = orbitalPeriod;
	}

	public List<Mineral> getMinerals() {
		return minerals;
	}

	public void setMinerals(List<Mineral> minerals) {
		this.minerals = minerals;
	}

	@Override
	public String toString() {
		return "Comet [name=" + name + ", orbitalPeriod=" + orbitalPeriod + ", minerals=" + minerals + "]";
	}

}
