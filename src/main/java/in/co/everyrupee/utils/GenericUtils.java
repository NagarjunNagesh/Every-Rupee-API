package in.co.everyrupee.utils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Generic utility services
 *
 * @author Nagarjun
 */
public class GenericUtils {

  public static List<Integer> removeAll(List<Integer> list, int element) {
    return list.stream().filter(e -> !Objects.equals(e, element)).collect(Collectors.toList());
  }
}
