package ishango.models;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

public class UserVotes {
  @Getter private final List<Integer> candidateOrder;

  public UserVotes(List<Vote> votes) {
    this.candidateOrder = votesToCandidateOrder(votes);
  }

  private List<Integer> votesToCandidateOrder(final List<Vote> votes) {
    Collections.sort(votes, new Vote.UserVotesSortingComparator());
    return votes.stream().map(Vote::getCandidateId).collect(Collectors.toList());
  }
}
