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
	private Property radius;
	
	public Moon() {
		super();
	}
	public Moon(String name, Property radius) {
		super();
		this.name = name;
		this.radius = radius;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Property getRadius() {
		return radius;
	}
	public void setRadius(Property radius) {
		this.radius = radius;
	}

	@Override
	public String toString() {
		return "Moon{" + "name=" + name + ", radius=" + radius + '}';
	}
	
}
