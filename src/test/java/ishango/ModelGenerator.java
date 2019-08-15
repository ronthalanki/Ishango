package ishango;

import ishango.models.Choice;
import ishango.models.User;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModelGenerator {
  private static final Random random = new Random();

  public static Choice newChoice() {
    final Integer randomInt = newInt();
    return new Choice(randomInt, String.format("test_choice%s", randomInt));
  }

  public static List<Choice> newChoices(final Integer limit) {
    return Stream.generate(() -> newChoice()).limit(limit).collect(Collectors.toList());
  }

  public static User newUser() {
    final Integer randomInt = random.nextInt();
    return new User(randomInt, String.format("test_user%s", randomInt));
  }

  public static List<User> newUsers(final Integer limit) {
    return Stream.generate(() -> newUser()).limit(limit).collect(Collectors.toList());
  }

  public static Integer newInt() {
    return random.nextInt(Integer.MAX_VALUE);
  }
}
