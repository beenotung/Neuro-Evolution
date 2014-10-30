package myutils.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;

/**
 * @author beenotung
 */
public class MyUtils {

	public static String getString(PreparedStatement preparedStatement) {
		String tmp = preparedStatement.toString();
		return tmp.substring(tmp.indexOf(':') + 1);
	}

	public static Vector<String> TO_VECTOR(String string, int count) {
		Vector<String> vector = new Vector<String>();
		for (int i = 0; i < count; i++)
			vector.add(new String(string));
		return vector;
	}

	public static Vector<PreparedStatement> TO_VECTOR(
			PreparedStatement preparedStatement, int count) throws SQLException {
		Vector<PreparedStatement> vector = new Vector<PreparedStatement>();
		Connection connection = preparedStatement.getConnection();
		String sql = getString(preparedStatement);
		for (int i = 0; i < count; i++)
			vector.add(connection.prepareStatement(sql));
		return vector;
	}
}
