//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package open.ucodereview.view.component;

import open.ucodereview.enums.ReviewListColumnEnum;
import open.ucodereview.entity.ReviewInfo;

import javax.swing.table.DefaultTableModel;
import java.util.Iterator;
import java.util.List;

public class ReviewInfoModel extends DefaultTableModel {

  public Class<?> getColumnClass(int columnIndex) {
    return String.class;
  }

  public int getColumnCount() {
    return ReviewListColumnEnum.values().length;
  }

  public String getColumnName(int column) {
    for (ReviewListColumnEnum entity : ReviewListColumnEnum.values()) {
      if (entity.getIndex() == column) {
        return entity.getName();
      }
    }
    return super.getColumnName(column);
  }

  public boolean isCellEditable(int row, int column) {
    for (ReviewListColumnEnum entity : ReviewListColumnEnum.values()) {
      if (entity.getIndex() == column) {
        return entity.isEditable();
      }
    }
    return false;
  }

  public void updateReviewModel(List<ReviewInfo> codeReviewInfoEntities) {
    if (null != codeReviewInfoEntities && codeReviewInfoEntities.size() != 0) {
      Iterator var2 = codeReviewInfoEntities.iterator();

      while (var2.hasNext()) {
        ReviewInfo entity = (ReviewInfo) var2.next();
        this.addRow(new Object[]{entity.getProjectName(),
            entity.getFileDetailPath(), entity.getSelectedContext(),
            entity.getStartLineNumber() + "-" + entity.getEndLineNumber(),
            entity.getDefectDesc(), entity.getReviesDesc(), entity.getDefectType(),
            entity.getCheckUser(), entity.getToCheckUser(),
            entity.getOpraTime(), entity.getStatusName(), entity.getDefectLevel(), entity.getId()});
      }
    }
  }

  public void updateAllReviewModel(List<ReviewInfo> codeReviewInfoEntities) {
    if (null != codeReviewInfoEntities) {
      setRowCount(0); // 清除所有数据
      updateReviewModel(codeReviewInfoEntities);
    }
  }
}
