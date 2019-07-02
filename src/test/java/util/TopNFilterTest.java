package util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;

public class TopNFilterTest {

  @Test
  public void filter() {
    TopNFilter topNFilter = new TopNFilter(1);
    List<String> list = Arrays.asList("1", "2", "3", "1", "2", "3");
    List<String> collect = list.stream().filter(topNFilter::filter)
        .collect(Collectors.toList());
    Assert.assertEquals(collect.toString(), Arrays.asList("1", "2", "3", "1", "2", "3").toString());
  }
}