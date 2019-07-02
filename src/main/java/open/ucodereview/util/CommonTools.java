package open.ucodereview.util;

import com.intellij.openapi.project.Project;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JTable;
import org.apache.commons.lang.StringUtils;
import open.ucodereview.data.ReviewDataService;

public class CommonTools {

  public static Object[] getCheckUsers(Project project) {
    List<String> checkUsers = ReviewDataService.getInstance(project).getCheckUsers();
    Collections.sort(checkUsers);
    return checkUsers.toArray();
  }

  public static String[] getDefectTypes() {
    return new String[]{"编码样式", "异常捕获", "代码重构", "性能优化", "补充日志", "补充注释", "补充测试用例", "其它"};
  }

  public static String[] getDefectLevels() {
    return new String[]{"紧急", "重要", "一般"};
  }

  public static List<String> getDevelperEmailList(Project project) {
    return ReviewDataService.getInstance(project).getDevelopers().stream()
        .map(d -> d.getEmail()).filter(
            StringUtils::isNotBlank).distinct().sorted().collect(Collectors.toList());
  }

  /**
   * @Description list中的行转成model中的行
   * @Date 14:09 2019/6/27
   * @Param [project]
   * @return int
  */
  public static int convert2ModelRow(JTable jTable, int row) {
    return jTable.convertRowIndexToModel(row);
  }
}
