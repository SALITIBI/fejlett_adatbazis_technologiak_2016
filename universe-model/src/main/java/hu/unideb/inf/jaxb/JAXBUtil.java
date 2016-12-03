package hu.unideb.inf.jaxb;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class JAXBUtil {

	/**
	 * Serializes an object to XML. The output document is written in UTF-8 encoding.
	 *
	 * @param o the object to serialize
	 * @param os the {@link OutputStream} to write to
	 * @throws JAXBException on any error
	 */
	public static <T> void toXML(T t, OutputStream os) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(t.getClass());
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.marshal(t, os);
	}
	
	public static <T> String toXMLFragment(T t) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(t.getClass());
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		
		Writer writer = new StringWriter();
		marshaller.marshal(t, writer);
		return writer.toString();
	}
	
	/**
	 * Deserializes an object from XML.
	 *
	 * @param clazz the class of the object
	 * @param is the {@link InputStream} to read from
	 * @return the resulting object
	 * @throws JAXBException on any error
	 */
	public static <T> T fromXML(Class<T> clazz, InputStream is) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(clazz);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return clazz.cast(unmarshaller.unmarshal(is));
	}
	
	public static <T> T fromXML(Class<T> clazz, String xml) throws JAXBException {
		return fromXML(clazz, xml, StandardCharsets.UTF_8);
	}
	
	public static <T> T fromXML(Class<T> clazz, String xml, Charset charset) throws JAXBException {
		InputStream is = new ByteArrayInputStream(xml.getBytes(charset));
		return fromXML(clazz, is);
	}

}
