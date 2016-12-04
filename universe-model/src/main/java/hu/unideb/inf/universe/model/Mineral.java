package hu.unideb.inf.universe.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Mineral {

	@XmlAttribute
	private String elementName;
	@javax.xml.bind.annotation.XmlElement
	private Property quantity;

	public Mineral() {
	}

	public Mineral(String elementName, Property quantity) {
		this.elementName = elementName;
		this.quantity = quantity;
	}

	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public Property getQuantity() {
		return quantity;
	}

	public void setQuantity(Property quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "Mineral [elementName=" + elementName + ", quantity=" + quantity + "]";
	}

}
