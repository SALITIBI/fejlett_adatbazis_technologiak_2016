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
			"meanRadius",
			"orbitalPeriod",
			"rotationVelocity",
			"orbitalSpeed",
			"axialTilt",
			"moons"
		}
	)
public class Planet {
	@XmlAttribute
	private String name;
	@XmlElementWrapper(name="moons")
	@XmlElement(name="moon")	
	private List<Moon> moons;
	@XmlElement
	private Property meanRadius;
	@XmlElement
	private Property rotationVelocity;
	@XmlElement
	private Property orbitalSpeed;
	@XmlElement
	private Property orbitalPeriod;
	@XmlElement
	private Property axialTilt;
	
	
	
	
	public Planet() {
		super();
	}
	public Planet(String name, List<Moon> moons, Property meanRadius, Property rotationVelocity, Property orbitalSpeed,
			Property orbitalPeriod, Property axialTilt) {
		super();
		this.name = name;
		this.moons = moons;
		this.meanRadius = meanRadius;
		this.rotationVelocity = rotationVelocity;
		this.orbitalSpeed = orbitalSpeed;
		this.orbitalPeriod = orbitalPeriod;
		this.axialTilt = axialTilt;
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
	public Property getMeanRadius() {
		return meanRadius;
	}
	public void setMeanRadius(Property meanRadius) {
		this.meanRadius = meanRadius;
	}
	public Property getRotationVelocity() {
		return rotationVelocity;
	}
	public void setRotationVelocity(Property rotationVelocity) {
		this.rotationVelocity = rotationVelocity;
	}
	public Property getOrbitalSpeed() {
		return orbitalSpeed;
	}
	public void setOrbitalSpeed(Property orbitalSpeed) {
		this.orbitalSpeed = orbitalSpeed;
	}
	public Property getOrbitalPeriod() {
		return orbitalPeriod;
	}
	public void setOrbitalPeriod(Property orbitalPeriod) {
		this.orbitalPeriod = orbitalPeriod;
	}
	public Property getAxialTilt() {
		return axialTilt;
	}
	public void setAxialTilt(Property axialTilt) {
		this.axialTilt = axialTilt;
	}
	@Override
	public String toString() {
		return "Planet [name=" + name + ", moons=" + moons + ", meanRadius=" + meanRadius + ", rotationVelocity=" + rotationVelocity
				+ ", orbitalSpeed=" + orbitalSpeed + ", orbitalPeriod=" + orbitalPeriod + ", axialTilt=" + axialTilt + "]";
	}

	

}
