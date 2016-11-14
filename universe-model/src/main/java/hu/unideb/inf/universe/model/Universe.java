package hu.unideb.inf.universe.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class Universe {
	@XmlElementWrapper(name="galaxies")
	@XmlElement(name="galaxy")	
	private List<Galaxy> galaxies;

	public Universe() {
		super();
	}

	public Universe(List<Galaxy> galaxies) {
		super();
		this.galaxies = galaxies;
	}

	public List<Galaxy> getGalaxies() {
		return galaxies;
	}

	public void setGalaxies(List<Galaxy> galaxies) {
		this.galaxies = galaxies;
	}

	@Override
	public String toString() {
		return "Universe [galaxies=" + galaxies + "]";
	}

	
	
}
