package ishango.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Vote {
  @Getter private final Integer userId;
  @Getter private final Integer candidateId;
  @Getter private final Integer rank;
}
