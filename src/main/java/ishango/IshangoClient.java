package ishango;

import ishango.models.Ballot;
import ishango.models.Choice;
import ishango.models.User;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IshangoClient {
  private final DbClient dbClient;

  public IshangoClient() throws IOException, SQLException {
    this.dbClient = new DbClient();
  }

  public Choice calculateWinner() throws Exception {
    final List<User> users = dbClient.listUsers();
    final List<Choice> choices = dbClient.listChoices();

    final List<Ballot> ballots = getBallots(users);
    final List<Integer> excludedChoices = new ArrayList<>();

    while (true) {
      final Map<Integer, Integer> votesPerChoice =
          getVotesPerChoice(choices, ballots, excludedChoices);

      Integer maxKey = null;
      Integer maxVotes = 0;
      Boolean maxTie = false;

      Integer minKey = null;
      Integer minVotes = Integer.MAX_VALUE;
      Boolean minTie = false;

      for (Integer choice : votesPerChoice.keySet()) {
        final Integer numVotes = votesPerChoice.get(choice);
        if (numVotes > maxVotes) {
          maxKey = choice;
          maxVotes = numVotes;
          maxTie = false;
        } else if (numVotes == maxVotes) {
          maxTie = true;
        }

        if (numVotes < minVotes) {
          minKey = choice;
          minVotes = numVotes;
          minTie = false;
        } else if (numVotes == minVotes) {
          minTie = true;
        }
      }

      if (maxTie) {
        excludedChoices.add(maxKey);
      } else {
        return dbClient.getChoiceById(maxKey);
      }
    }
  }

  private List<Ballot> getBallots(final List<User> users) throws SQLException {
    final List<Ballot> ballots = new ArrayList<>();
    for (User user : users) {
      ballots.add(new Ballot(dbClient.getVotesByUser(user.getId())));
    }
    return ballots;
  }

  private Map<Integer, Integer> getVotesPerChoice(
      final List<Choice> choices, final List<Ballot> ballots, final List<Integer> excludedChoices)
      throws Exception {
    final Map<Integer, Integer> votesPerChoice = new HashMap<>();
    for (Choice choice : choices) {
      votesPerChoice.put(choice.getId(), 0);
    }

    for (Ballot ballot : ballots) {
      votesPerChoice.put(
          ballot.getHighestChoice(excludedChoices),
          votesPerChoice.get(ballot.getHighestChoice(excludedChoices)) + 1);
    }
    return votesPerChoice;
  }

  public static void main(String[] args) throws Exception {
    IshangoClient client = new IshangoClient();
    client.dbClient.setupTables();
  }
}
