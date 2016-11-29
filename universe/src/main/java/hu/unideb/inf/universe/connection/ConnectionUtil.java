package hu.unideb.inf.universe.connection;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;

import net.xqj.basex.BaseXXQDataSource;

public class ConnectionUtil {

	public static XQConnection getConnection(String serverName, String port, String user, String password)
			throws XQException {

		if (serverName == null) {
			throw new NullPointerException("serverName is null");
		}
		if (port == null) {
			throw new NullPointerException("port is null");
		}
		if (user == null) {
			throw new NullPointerException("user is null");
		}
		if (password == null) {
			throw new NullPointerException("password is null");
		}

		XQDataSource ds = new BaseXXQDataSource();
		ds.setProperty("serverName", serverName);
		ds.setProperty("port", port);
		ds.setProperty("user", user);
		ds.setProperty("password", password);

		return ds.getConnection();
	}

}
