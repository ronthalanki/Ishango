package ishango;

import static ishango.ModelGenerator.newChoice;
import static ishango.ModelGenerator.newChoices;
import static ishango.ModelGenerator.newInt;
import static ishango.ModelGenerator.newUsers;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ishango.models.Choice;
import ishango.models.User;
import ishango.models.Vote;
import java.util.List;
import org.junit.Test;

public class IshangoResourceTest {
  private static final DbClient dbClient = mock(DbClient.class);
  private static final IshangoResource ishango = new IshangoResource(dbClient);

  @Test
  public void testCalculateWinner_oneChoice_allVotes() throws Exception {
    final List<User> users = newUsers(4);
    final Choice choice = newChoice();

    when(dbClient.listChoices()).thenReturn(asList(choice));
    when(dbClient.listUsers()).thenReturn(users);
    when(dbClient.getChoiceById(choice.getId())).thenReturn(choice);
    for (final User user : users) {
      final List<Vote> votes = asList(new Vote(newInt(), user.getId(), choice.getId(), 1));
      when(dbClient.getVotesByUser(user.getId())).thenReturn(votes);
    }

    assertThat(ishango.calculateWinner()).isEqualTo(choice);
  }

  @Test
  public void testCalculateWinner_twoChoices_allVotes() throws Exception {
    final List<User> users = newUsers(4);
    final List<Choice> choices = newChoices(2);

    when(dbClient.listChoices()).thenReturn(choices);
    when(dbClient.listUsers()).thenReturn(users);
    when(dbClient.getChoiceById(choices.get(0).getId())).thenReturn(choices.get(0));
    for (final User user : users) {
      final List<Vote> votes =
          asList(
              new Vote(newInt(), user.getId(), choices.get(0).getId(), 1),
              new Vote(newInt(), user.getId(), choices.get(1).getId(), 2));
      when(dbClient.getVotesByUser(user.getId())).thenReturn(votes);
    }

    assertThat(ishango.calculateWinner()).isEqualTo(choices.get(0));
  }

  @Test
  public void testCalculateWinner_twoChoices_majorityVotes() throws Exception {
    final Integer numUsers = 4;
    final List<User> users = newUsers(numUsers);
    final List<Choice> choices = newChoices(2);

    when(dbClient.listChoices()).thenReturn(choices);
    when(dbClient.listUsers()).thenReturn(users);
    when(dbClient.getChoiceById(choices.get(1).getId())).thenReturn(choices.get(1));
    for (int i = 0; i < numUsers / 2 + 1; i++) {
      final List<Vote> votes =
          asList(
              new Vote(newInt(), users.get(i).getId(), choices.get(1).getId(), 1),
              new Vote(newInt(), users.get(i).getId(), choices.get(0).getId(), 2));
      when(dbClient.getVotesByUser(users.get(i).getId())).thenReturn(votes);
    }
    for (int i = numUsers / 2 + 1; i < numUsers; i++) {
      final List<Vote> votes =
          asList(
              new Vote(newInt(), users.get(i).getId(), choices.get(0).getId(), 1),
              new Vote(newInt(), users.get(i).getId(), choices.get(1).getId(), 2));
      when(dbClient.getVotesByUser(users.get(i).getId())).thenReturn(votes);
    }

    assertThat(ishango.calculateWinner()).isEqualTo(choices.get(1));
  }

  @Test
  public void testCalculateWinner_threeChoices_pluralityVotes() throws Exception {
    final Integer numUsers = 5;
    final List<User> users = newUsers(5);
    final List<Choice> choices = newChoices(3);

    when(dbClient.listChoices()).thenReturn(choices);
    when(dbClient.listUsers()).thenReturn(users);
    when(dbClient.getChoiceById(choices.get(0).getId())).thenReturn(choices.get(0));
    for (int i = 0; i < 2; i++) {
      final List<Vote> votes =
          asList(
              new Vote(newInt(), users.get(i).getId(), choices.get(0).getId(), 1),
              new Vote(newInt(), users.get(i).getId(), choices.get(1).getId(), 2),
              new Vote(newInt(), users.get(i).getId(), choices.get(2).getId(), 3));
      when(dbClient.getVotesByUser(users.get(i).getId())).thenReturn(votes);
    }
    for (int i = 2; i < 4; i++) {
      final List<Vote> votes =
          asList(
              new Vote(newInt(), users.get(i).getId(), choices.get(1).getId(), 1),
              new Vote(newInt(), users.get(i).getId(), choices.get(0).getId(), 2),
              new Vote(newInt(), users.get(i).getId(), choices.get(2).getId(), 3));
      when(dbClient.getVotesByUser(users.get(i).getId())).thenReturn(votes);
    }
    final List<Vote> votes =
        asList(
            new Vote(newInt(), users.get(4).getId(), choices.get(2).getId(), 1),
            new Vote(newInt(), users.get(4).getId(), choices.get(0).getId(), 2),
            new Vote(newInt(), users.get(4).getId(), choices.get(1).getId(), 3));
    when(dbClient.getVotesByUser(users.get(4).getId())).thenReturn(votes);

    assertThat(ishango.calculateWinner()).isEqualTo(choices.get(0));
  }
}
