package ishango;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import ishango.models.Candidate;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DbClient {
  private static final String MYSQL_PROPERTIES = "src/main/resources/db.properties";
  final MysqlDataSource datasource;
  final Connection connection;

  public DbClient() throws IOException, SQLException {
    datasource = getMySQLDataSource();
    connection = datasource.getConnection();
  }

  private MysqlDataSource getMySQLDataSource() throws IOException {
    final Properties properties = new Properties();
    final FileInputStream fileInputStream = new FileInputStream(MYSQL_PROPERTIES);
    properties.load(fileInputStream);

    final MysqlDataSource ds = new MysqlDataSource();
    ds.setURL(properties.getProperty("mysql.url"));
    ds.setUser(properties.getProperty("mysql.username"));
    ds.setPassword(properties.getProperty("mysql.password"));

    return ds;
  }

  private List<Candidate> listCandidates() throws SQLException {
    final String query = "SELECT * FROM Candidates;";
    final ResultSet resultSet = getResultSet(query);

    final List<Candidate> candidates = new ArrayList<>();
    while (resultSet.next()) {
      candidates.add(new Candidate(resultSet.getInt(1), resultSet.getString(2)));
    }
    return candidates;
  }

  private ResultSet getResultSet(final String query) throws SQLException {
    PreparedStatement pst = connection.prepareStatement(query);
    pst.execute();
    return pst.getResultSet();
  }

  private void executeMultipleQueries(final String query) throws SQLException {
    PreparedStatement pst = connection.prepareStatement(query);
    boolean isResult = pst.execute();

    while (isResult) {
      final ResultSet rs = pst.getResultSet();

      while (rs.next()) {
        System.out.print("ID: " + rs.getInt(1) + "\t");
        System.out.println("Name: " + rs.getString(2));
      }

      isResult = pst.getMoreResults();
    }
  }

  public static void main(String[] args) throws IOException, SQLException {
    DbClient client = new DbClient();
    // client.executeMultipleQueries("SELECT * FROM Candidates;");
    System.out.println(client.listCandidates());
  }
}
