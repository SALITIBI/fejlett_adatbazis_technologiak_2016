package hu.unideb.inf.universe.main;

import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;

import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.XQuery;
import org.basex.io.serial.Serializer;
import org.basex.query.QueryException;
import org.basex.query.QueryProcessor;
import org.basex.query.iter.Iter;
import org.basex.query.value.Value;
import org.basex.query.value.item.Item;

import net.xqj.basex.BaseXXQDataSource;

public class Application {

	private static final Context context = new Context();

	public static void main(String[] args) throws Exception {
		// String query = "for $x in doc('src/main/resources/fejlett_adatbazis_hazi.xml')//universe return data($x)";
		//
		// query(query);
		// process(query);
		// serialize(query);
		// iterate(query);

		XQDataSource xqs = new BaseXXQDataSource();
		xqs.setProperty("serverName", "localhost");
		xqs.setProperty("port", "1984");

		// Change USERNAME and PASSWORD values
		XQConnection conn = xqs.getConnection("admin", "admin");

		XQPreparedExpression xqpe = conn.prepareExpression("declare variable $x as xs:string external; $x");

		xqpe.bindString(new QName("x"), "Hello World!", null);

		XQResultSequence rs = xqpe.executeQuery();

		while (rs.next()) {
			System.out.println(rs.getItemAsString(null));
		}
		
		xqpe = conn.prepareExpression("for $x in //universe return data($x)");
		rs = xqpe.executeQuery();
		while (rs.next()) {
			System.out.println(rs.getItemAsString(null));
		}
		
		
		System.out.println("Conn isClosed: " + conn.isClosed());
		conn.close();
		System.out.println("Conn isClosed: " + conn.isClosed());
	}

	static void query(final String query) throws BaseXException {
		System.out.println(new XQuery(query).execute(context));
	}

	static void process(final String query) throws QueryException {
		try (QueryProcessor proc = new QueryProcessor(query, context)) {
			Value result = proc.value();

			System.out.println(result);
		}
	}

	static void serialize(final String query) throws QueryException, IOException {
		try (QueryProcessor proc = new QueryProcessor(query, context)) {
			Iter iter = proc.iter();

			try (Serializer ser = proc.getSerializer(System.out)) {
				for (Item item; (item = iter.next()) != null;) {
					ser.serialize(item);
				}
			}
			System.out.println();
		}
	}

	static void iterate(final String query) throws QueryException {
		try (QueryProcessor proc = new QueryProcessor(query, context)) {
			Iter iter = proc.iter();

			for (Item item; (item = iter.next()) != null;) {
				System.out.println(item.toJava());
			}
		}
	}

}
