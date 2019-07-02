package util;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 取topN个数
 * @author nengcai.wang
 * @Date 2019/6/26
 */
public class TopNFilter<T> {

  private List<T> list = new ArrayList<>();
  private int topN = 0;

  public TopNFilter(int topN) {
    this.topN = topN;
  }

  public boolean filter(T t) {
    list.add(t);
    return list.size() <= topN;
  }
}