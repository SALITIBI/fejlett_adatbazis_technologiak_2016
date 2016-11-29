package hu.unideb.inf.universe.main;

import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQExpression;
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

		XQDataSource ds = new BaseXXQDataSource();
		ds.setProperty("serverName", "localhost");
		ds.setProperty("port", "1984");
		ds.setProperty("user", "admin");
		ds.setProperty("password", "admin");
		
		XQConnection xqc = ds.getConnection();
		XQExpression xqe = xqc.createExpression();
		XQResultSequence rs = xqe.executeQuery("for $x in db:open('universe')//* return data($x)");
		rs.writeSequence(System.out, null);
		xqc.close();
		
//		ds.setLogWriter(new PrintWriter(System.out));
//
//		// Change USERNAME and PASSWORD values
//		XQConnection conn = ds.getConnection("admin", "admin");
////
////		XQPreparedExpression xqpe = conn.prepareExpression("declare variable $x as xs:string external; $x");
////
////		xqpe.bindString(new QName("x"), "Hello World!", null);
////
////		XQResultSequence rs = xqpe.executeQuery();
////
////		while (rs.next()) {
////			System.out.println(rs.getItemAsString(null));
////		}
////		
////		XQExpression createExpression = conn.createExpression();
////		XQResultSequence executeQuery = createExpression.executeQuery("for $x in //galaxy return data($x)");
////		executeQuery.writeSequence(System.out, null);
////		
////		System.out.println("Conn isClosed: " + conn.isClosed());
////		conn.close();
////		System.out.println("Conn isClosed: " + conn.isClosed());
//		
//		
////		XQConnection xqc = ds.getConnection();
//	    XQExpression xqe = conn.createExpression();
//
////	    xqe.executeCommand("SHOW DATABASES");
//	    xqe.executeCommand("SHOW SESSIONS");
//	    xqe.executeCommand("SHOW USERS");
//	    xqe.executeCommand("SHOW EVENTS");
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
