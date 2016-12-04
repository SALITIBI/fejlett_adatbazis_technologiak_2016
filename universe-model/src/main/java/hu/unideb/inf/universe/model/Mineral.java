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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((elementName == null) ? 0 : elementName.hashCode());
		result = prime * result + ((quantity == null) ? 0 : quantity.hashCode());
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
		Mineral other = (Mineral) obj;
		if (elementName == null) {
			if (other.elementName != null)
				return false;
		} else if (!elementName.equals(other.elementName))
			return false;
		if (quantity == null) {
			if (other.quantity != null)
				return false;
		} else if (!quantity.equals(other.quantity))
			return false;
		return true;
	}

	
}
