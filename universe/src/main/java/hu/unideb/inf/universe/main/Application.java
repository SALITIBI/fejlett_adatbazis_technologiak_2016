package hu.unideb.inf.universe.main;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQResultSequence;

import net.xqj.basex.BaseXXQDataSource;

public class Application {

	public static boolean validate(XQConnection xqc) {
		try {
			XQExpression xqe = xqc.createExpression();
			XQResultSequence rs = xqe.executeQuery("for $doc in db:open('universe') return validate:xsd($doc, 'C:\\Egyetem\\xmldb\\universe.xsd')");
			return true;
		} catch (XQException ex) {
			return false;
		}
	}
	
	public static void main(String[] args) throws Exception {
		// String query = "for $x in doc('src/main/resources/fejlett_adatbazis_hazi.xml')//universe return data($x)";

		XQDataSource ds = new BaseXXQDataSource();
		ds.setProperty("serverName", "localhost");
		ds.setProperty("port", "1984");
		ds.setProperty("user", "admin");
		ds.setProperty("password", "admin");

		XQConnection xqc = ds.getConnection();
		XQExpression xqe = xqc.createExpression();
		XQResultSequence rs = xqe.executeQuery("for $x in db:open('universe')//* return data($x)");
		rs.writeSequence(System.out, null);
		System.out.println(validate(xqc));
		xqc.close();
	}

}
