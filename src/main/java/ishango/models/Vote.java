package ishango.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Vote {
  private final Integer id;
  private final Integer userId;
  private final Integer candidateId;
  private final Integer rank;
}
