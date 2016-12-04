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
	@XmlElementWrapper(name = "minerals")
	@XmlElement(name = "mineral")
	private List<Mineral> minerals;

	public Comet() {
	}

	public Comet(String name, Property orbitalPeriod, List<Mineral> minerals) {
		this.name = name;
		this.orbitalPeriod = orbitalPeriod;
		this.minerals = minerals;
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((minerals == null) ? 0 : minerals.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((orbitalPeriod == null) ? 0 : orbitalPeriod.hashCode());
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
		Comet other = (Comet) obj;
		if (minerals == null) {
			if (other.minerals != null)
				return false;
		} else if (!minerals.equals(other.minerals))
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
		return true;
	}

	
}
