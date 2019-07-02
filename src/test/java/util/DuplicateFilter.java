package util;

import java.util.HashSet;
import java.util.Set;

/**
 * @Description 对集合元素进行去重, stream中已提供distinct方法
 * @author nengcai.wang
 * @Date 2019/6/26
 */
public class DuplicateFilter<T> {
  private Set<T> set = new HashSet<>();
  public boolean filter(T t){
    return set.add(t);
  }
}