package ishango.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Comparator;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Vote {
  @Getter private final Integer userId;
  @Getter private final Integer candidateId;
  @Getter private final Integer rank;

  static class UserVotesSortingComparator implements Comparator<Vote> {
    @Override
    public int compare(Vote o1, Vote o2) {
      return o1.getRank().compareTo(o2.getRank());
    }
  }
}
