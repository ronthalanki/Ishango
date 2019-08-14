package ishango;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import ishango.models.Ballot;
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
import java.util.Map;
import java.util.Properties;

public class DbClient {
  private static final String MYSQL_PROPERTIES = "src/main/resources/db.properties";
  private final MysqlDataSource datasource;
  private final Connection connection;

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

  public void addChoice(final String choiceName) throws SQLException {
    final String query = String.format("INSERT INTO Choices (`Name`) VALUES ('%s');", choiceName);
    executeSql(query);
  }

  public Choice getChoiceById(final Integer id) throws SQLException {
    final String query = String.format("SELECT * FROM Choices WHERE Id = %s;", id);
    final ResultSet resultSet = executeSqlGetResult(query);

    if (resultSet.next()) {
      return choiceFromResultSet(resultSet);
    }
    return null;
  }

  public List<Choice> listChoices() throws SQLException {
    final String query = "SELECT * FROM Choices;";
    final ResultSet resultSet = executeSqlGetResult(query);

    final List<Choice> choices = new ArrayList<>();
    while (resultSet.next()) {
      choices.add(choiceFromResultSet(resultSet));
    }
    return choices;
  }

  public void addUser(final String userName) throws SQLException {
    final String query = String.format("INSERT INTO Users (`Name`) VALUES ('%s');", userName);
    executeSql(query);
  }

  public List<User> listUsers() throws SQLException {
    final String query = "SELECT * FROM Users;";
    final ResultSet resultSet = executeSqlGetResult(query);

    final List<User> users = new ArrayList<>();
    while (resultSet.next()) {
      users.add(userFromResultSet(resultSet));
    }
    return users;
  }

  public void addVote(final Integer userId, final Integer choiceId, final Integer rank)
      throws SQLException {
    final String query =
        String.format(
            "INSERT INTO Votes (`UserId`,`ChoiceId`,`Rank`) VALUES (%s,%s,%s);",
            userId, choiceId, rank);
    executeSql(query);
  }

  public List<Vote> getVotesByUser(final Integer userId) throws SQLException {
    final String query = String.format("SELECT * FROM Votes WHERE UserId = %s;", userId);
    final ResultSet resultSet = executeSqlGetResult(query);

    final List<Vote> votes = new ArrayList<>();
    while (resultSet.next()) {
      votes.add(voteFromResultSet(resultSet));
    }
    return votes;
  }

  public List<Vote> listVotes() throws SQLException {
    final String query = "SELECT * FROM Votes;";
    final ResultSet resultSet = executeSqlGetResult(query);

    final List<Vote> votes = new ArrayList<>();
    while (resultSet.next()) {
      votes.add(voteFromResultSet(resultSet));
    }
    return votes;
  }

  public void setupTables() throws SQLException {
    executeSql("DROP TABLE IF EXISTS Votes, Users, Choices;");
    executeSql("CREATE TABLE Choices(Id BIGINT PRIMARY KEY AUTO_INCREMENT, Name VARCHAR(100));");
    executeSql("CREATE TABLE Users(Id BIGINT PRIMARY KEY AUTO_INCREMENT, Name VARCHAR(100));");
    executeSql(
        "CREATE TABLE Votes(Id BIGINT PRIMARY KEY AUTO_INCREMENT, UserId BIGINT, FOREIGN "
            + "KEY(UserId) REFERENCES Users(Id) ON DELETE CASCADE, ChoiceId BIGINT, Rank BIGINT, "
            + "FOREIGN KEY(ChoiceId) REFERENCES Choices(Id) ON DELETE CASCADE);");
  }

  public void fillTables(
      final List<String> choices, final List<String> users, final Map<Integer, Ballot> ballots)
      throws SQLException {
    for (final String choice : choices) {
      addChoice(choice);
    }

    for (final String user : users) {
      addUser(user);
    }

    for (final Integer user : ballots.keySet()) {
      final List<Integer> ballot = ballots.get(user).getBallot();
      for (int i = 0; i < ballot.size(); i++) {
        addVote(user, ballot.get(i), i + 1);
      }
    }
  }

  private void executeSql(final String query) throws SQLException {
    final PreparedStatement pst = connection.prepareStatement(query);
    pst.execute();
  }

  private ResultSet executeSqlGetResult(final String query) throws SQLException {
    final PreparedStatement pst = connection.prepareStatement(query);
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
    return new Vote(
        resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4));
  }
}
