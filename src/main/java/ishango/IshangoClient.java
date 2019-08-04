package ishango;

import ishango.models.Choice;
import ishango.models.User;
import ishango.models.UserVotes;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IshangoClient {
  private DbClient dbClient;

  public IshangoClient() throws IOException, SQLException {
    this.dbClient = new DbClient();
  }

  public Choice calculateWinner() throws SQLException {
    final List<User> users = dbClient.listUsers();

    final List<UserVotes> userVotes = new ArrayList<>();
    for (User user : users) {
      userVotes.add(new UserVotes(dbClient.getVotesByUser(user.getId())));
    }

    final List<Choice> choices = dbClient.listChoices();
    final Map<Integer, Integer> numVotes = new HashMap<>();

    for (Choice choice : choices) {
      numVotes.put(choice.getId(), 0);
    }

    for (UserVotes uv : userVotes) {
      numVotes.put(uv.getCandidateOrder().get(0), numVotes.get(uv.getCandidateOrder().get(0)) + 1);
    }

    Map.Entry<Integer, Integer> maxEntry = null;

    for (Map.Entry<Integer, Integer> entry : numVotes.entrySet()) {
      if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
        maxEntry = entry;
      }
    }

    return dbClient.getChoiceById(maxEntry.getKey());
  }

  public static void main(String[] args) throws IOException, SQLException {
    IshangoClient client = new IshangoClient();
    System.out.println(client.calculateWinner());
  }
}
