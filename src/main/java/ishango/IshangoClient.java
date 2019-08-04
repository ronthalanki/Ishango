package ishango;

import ishango.models.Ballot;
import ishango.models.Choice;
import ishango.models.User;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IshangoClient {
  private final DbClient dbClient;

  public IshangoClient() throws IOException, SQLException {
    this.dbClient = new DbClient();
  }

  public Choice calculateWinner() throws SQLException {
    final List<User> users = dbClient.listUsers();
    final List<Choice> choices = dbClient.listChoices();

    final List<Ballot> ballots = getBallots(users);
    final List<Integer> excludedChoices = new ArrayList<>();
    final Map<Integer, Integer> votesPerChoice =
        getVotesPerChoice(choices, ballots, excludedChoices);

    final Integer key =
        Collections.max(votesPerChoice.entrySet(), Map.Entry.comparingByValue()).getKey();
    return dbClient.getChoiceById(key);
  }

  private List<Ballot> getBallots(final List<User> users) throws SQLException {
    final List<Ballot> ballots = new ArrayList<>();
    for (User user : users) {
      ballots.add(new Ballot(dbClient.getVotesByUser(user.getId())));
    }
    return ballots;
  }

  private Map<Integer, Integer> getVotesPerChoice(
      final List<Choice> choices, final List<Ballot> ballots, final List<Integer> excludedChoices) {
    final Map<Integer, Integer> votesPerChoice = new HashMap<>();
    for (Choice choice : choices) {
      votesPerChoice.put(choice.getId(), 0);
    }

    for (Ballot ballot : ballots) {
      votesPerChoice.put(
          ballot.getBallot().get(0), votesPerChoice.get(ballot.getBallot().get(0)) + 1);
    }
    return votesPerChoice;
  }

  public static void main(String[] args) throws IOException, SQLException {
    IshangoClient client = new IshangoClient();
    client.dbClient.addUser("Bar");
    client.dbClient.addVote(1, 1, 1);
  }
}
