package ishango.models;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Ballot {
  private final List<Integer> ballot;

  public Ballot(final List<Vote> votes) {
    this.ballot = votesToBallot(votes);
  }

  public Integer getHighestChoice(final List<Integer> excludedChoices) throws Exception {
    for (Integer choice : ballot) {
      if (!excludedChoices.contains(choice)) {
        return choice;
      }
    }
    throw new Exception("Ballot getHighestChoice failed");
  }

  private List<Integer> votesToBallot(final List<Vote> votes) {
    Collections.sort(votes, new Vote.UserVotesSortingComparator());
    return votes.stream().map(Vote::getChoiceId).collect(Collectors.toList());
  }
}
