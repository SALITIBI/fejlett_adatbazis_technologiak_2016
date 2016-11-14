package hu.unideb.inf.universe.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Moon{
	@XmlAttribute
	private String name;
	@XmlElement
	private Property meanRadius;
	
	public Moon() {
		super();
	}
	public Moon(String name, Property meanRadius) {
		super();
		this.name = name;
		this.meanRadius = meanRadius;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Property getMeanRadius() {
		return meanRadius;
	}
	public void setMeanRadius(Property meanRadius) {
		this.meanRadius = meanRadius;
	}
	@Override
	public String toString() {
		return "Moon [name=" + name + ", meanRadius=" + meanRadius + "]";
	}
	
}
