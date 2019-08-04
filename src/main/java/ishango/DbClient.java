package ishango;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import ishango.models.Choice;
import ishango.models.User;
import ishango.models.Vote;
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
    datasource = getMySqlDataSource();
    connection = datasource.getConnection();
  }

  private MysqlDataSource getMySqlDataSource() throws IOException {
    final Properties properties = new Properties();
    final FileInputStream fileInputStream = new FileInputStream(MYSQL_PROPERTIES);
    properties.load(fileInputStream);

    final MysqlDataSource ds = new MysqlDataSource();
    ds.setURL(properties.getProperty("mysql.url"));
    ds.setUser(properties.getProperty("mysql.username"));
    ds.setPassword(properties.getProperty("mysql.password"));

    return ds;
  }

  public Choice getChoiceById(final Integer id) throws SQLException {
    final String query = String.format("SELECT * FROM Choices WHERE Id = %s;", id);
    final ResultSet resultSet = getResultSet(query);

    if (resultSet.next()) {
      return choiceFromResultSet(resultSet);
    }
    return null;
  }

  public List<Vote> getVotesByUser(final Integer userId) throws SQLException {
    final String query =
        String.format("SELECT UserId, ChoiceId, Rank FROM Votes WHERE UserId = %s;", userId);
    final ResultSet resultSet = getResultSet(query);

    final List<Vote> votes = new ArrayList<>();
    while (resultSet.next()) {
      votes.add(voteFromResultSet(resultSet));
    }
    return votes;
  }

  public List<Choice> listChoices() throws SQLException {
    final String query = "SELECT * FROM Choices;";
    final ResultSet resultSet = getResultSet(query);

    final List<Choice> choices = new ArrayList<>();
    while (resultSet.next()) {
      choices.add(choiceFromResultSet(resultSet));
    }
    return choices;
  }

  public List<User> listUsers() throws SQLException {
    final String query = "SELECT * FROM Users;";
    final ResultSet resultSet = getResultSet(query);

    final List<User> users = new ArrayList<>();
    while (resultSet.next()) {
      users.add(userFromResultSet(resultSet));
    }
    return users;
  }

  public List<Vote> listVotes() throws SQLException {
    final String query = "SELECT * FROM Votes;";
    final ResultSet resultSet = getResultSet(query);

    final List<Vote> votes = new ArrayList<>();
    while (resultSet.next()) {
      votes.add(voteFromResultSet(resultSet));
    }
    return votes;
  }

  private ResultSet getResultSet(final String query) throws SQLException {
    PreparedStatement pst = connection.prepareStatement(query);
    pst.execute();
    return pst.getResultSet();
  }

  private Choice choiceFromResultSet(final ResultSet resultSet) throws SQLException {
    return new Choice(resultSet.getInt(1), resultSet.getString(2));
  }

  private User userFromResultSet(final ResultSet resultSet) throws SQLException {
    return new User(resultSet.getInt(1), resultSet.getString(2));
  }

  private Vote voteFromResultSet(final ResultSet resultSet) throws SQLException {
    return new Vote(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3));
  }
}
