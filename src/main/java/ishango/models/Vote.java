package ishango.models;

import java.util.Comparator;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Vote {
  @Getter private final Integer userId;
  @Getter private final Integer choiceId;
  @Getter private final Integer rank;

  static class UserVotesSortingComparator implements Comparator<Vote> {
    @Override
    public int compare(Vote o1, Vote o2) {
      return o1.getRank().compareTo(o2.getRank());
    }
  }
}
