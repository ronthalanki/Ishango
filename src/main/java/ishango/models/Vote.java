package ishango.models;

import java.util.Objects;

public class Vote {
  private final Integer id;
  private final Integer userId;
  private final Integer candidateId;
  private final Integer rank;

  public Vote(Integer id, Integer userId, Integer candidateId, Integer rank) {
    this.id = id;
    this.userId = userId;
    this.candidateId = candidateId;
    this.rank = rank;
  }

  public Integer getId() {
    return id;
  }

  public Integer getUserId() {
    return userId;
  }

  public Integer getCandidateId() {
    return candidateId;
  }

  public Integer getRank() {
    return rank;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Vote)) return false;
    Vote vote = (Vote) o;
    return getId().equals(vote.getId())
        && getUserId().equals(vote.getUserId())
        && getCandidateId().equals(vote.getCandidateId())
        && getRank().equals(vote.getRank());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getUserId(), getCandidateId(), getRank());
  }

  @Override
  public String toString() {
    return "Vote{"
        + "id="
        + id
        + ", userId="
        + userId
        + ", candidateId="
        + candidateId
        + ", rank="
        + rank
        + '}';
  }
}
