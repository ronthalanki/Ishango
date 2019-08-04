package ishango.models;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

public class Ballot {
  @Getter private final List<Integer> ballot;

  public Ballot(List<Vote> votes) {
    this.ballot = votesToBallot(votes);
  }

  private List<Integer> votesToBallot(final List<Vote> votes) {
    Collections.sort(votes, new Vote.UserVotesSortingComparator());
    return votes.stream().map(Vote::getChoiceId).collect(Collectors.toList());
  }
}
