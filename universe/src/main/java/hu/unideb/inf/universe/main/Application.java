package hu.unideb.inf.universe.main;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQResultSequence;

import hu.unideb.inf.jaxb.JAXBUtil;
import hu.unideb.inf.universe.model.Galaxy;
import net.xqj.basex.BaseXXQDataSource;

public class Application {

	public static void main(String[] args) throws Exception {
		XQDataSource ds = new BaseXXQDataSource();
		ds.setProperty("serverName", "localhost");
		ds.setProperty("port", "1984");
		ds.setProperty("user", "admin");
		ds.setProperty("password", "admin");

		XQConnection xqc = ds.getConnection();

		// doSomething(xqc);
		System.out.println(getGalaxies(xqc));
		System.out.println("validate: " + validate(xqc));

		xqc.close();
	}

	public static boolean validate(XQConnection xqc) {
		try {
			XQExpression xqe = xqc.createExpression();
			String universeXsdPath = Application.class.getClassLoader().getResource("universe.xsd").getPath();
			xqe.executeQuery("for $doc in db:open('universe') return validate:xsd($doc, '" + universeXsdPath + "')");
			return true;
		} catch (XQException ex) {
			return false;
		}
	}

	public static List<Galaxy> getGalaxies(XQConnection xqc) throws XQException, JAXBException {
		List<Galaxy> galaxies = new LinkedList<>();

		XQExpression xqe = xqc.createExpression();
		XQResultSequence rs = xqe.executeQuery("for $galaxy in db:open('universe')//galaxies/* return $galaxy");
		while (rs.next()) {
			Galaxy galaxy = JAXBUtil.fromXML(Galaxy.class, rs.getItemAsString(null));
			galaxies.add(galaxy);
		}

		return galaxies;
	}

	private static void doSomething(XQConnection xqc) throws XQException {
		XQExpression xqe = xqc.createExpression();
		XQResultSequence rs = xqe.executeQuery("for $x in db:open('universe')//* return data($x)");
		rs.writeSequence(System.out, null);
		System.out.println();
	}

}
