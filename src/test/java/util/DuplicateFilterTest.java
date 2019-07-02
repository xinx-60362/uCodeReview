package util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;

public class DuplicateFilterTest {

  @Test
  public void filter() {
    DuplicateFilter duplicateFilter = new DuplicateFilter();
    List<String> list = Arrays.asList("3", "2", "3");
    List<String> collect = list.stream().distinct().sorted()
        .collect(Collectors.toList());
    Assert.assertEquals(collect.toString(),Arrays.asList("2", "3").toString());
  }

}