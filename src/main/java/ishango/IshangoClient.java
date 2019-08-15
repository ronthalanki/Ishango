package ishango;

import ishango.models.Ballot;
import ishango.models.Choice;
import ishango.models.User;
import ishango.models.Vote;
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

  public void addChoice(final String choiceName) throws SQLException {
    dbClient.addChoice(choiceName);
  }

  public List<Choice> listChoices() throws SQLException {
    return dbClient.listChoices();
  }

  public void addUser(final String userName) throws SQLException {
    dbClient.addUser(userName);
  }

  public List<User> listUsers() throws SQLException {
    return dbClient.listUsers();
  }

  public void addVotes(final Integer userId, final Ballot ballot) throws SQLException {
    final List<Integer> ballotList = ballot.getBallot();
    for (int i = 0; i < ballotList.size(); i++) {
      dbClient.addVote(userId, ballotList.get(i), i + 1);
    }
  }

  public List<Vote> listVotes() throws SQLException {
    return dbClient.listVotes();
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

      Integer minKey = null;
      Integer minVotes = Integer.MAX_VALUE;
      Boolean minTie = false;

      for (Integer choice : votesPerChoice.keySet()) {
        final Integer numVotes = votesPerChoice.get(choice);
        if (numVotes > maxVotes) {
          maxKey = choice;
          maxVotes = numVotes;
        }

        if (numVotes < minVotes) {
          minKey = choice;
          minVotes = numVotes;
          minTie = false;
        } else if (numVotes == minVotes) {
          minTie = true;
        }
      }

      if (maxKey > users.size() / 2) {
        excludedChoices.add(maxKey);
      } else {
        return dbClient.getChoiceById(maxKey);
      }
    }
  }

  private List<Ballot> getBallots(final List<User> users) throws SQLException {
    final List<Ballot> ballots = new ArrayList<>();
    for (final User user : users) {
      ballots.add(new Ballot(dbClient.getVotesByUser(user.getId())));
    }
    return ballots;
  }

  private Map<Integer, Integer> getVotesPerChoice(
      final List<Choice> choices, final List<Ballot> ballots, final List<Integer> excludedChoices)
      throws Exception {
    final Map<Integer, Integer> votesPerChoice = new HashMap<>();
    for (final Choice choice : choices) {
      votesPerChoice.put(choice.getId(), 0);
    }

    for (final Ballot ballot : ballots) {
      votesPerChoice.put(
          ballot.getHighestChoice(excludedChoices),
          votesPerChoice.get(ballot.getHighestChoice(excludedChoices)) + 1);
    }
    return votesPerChoice;
  }
}
