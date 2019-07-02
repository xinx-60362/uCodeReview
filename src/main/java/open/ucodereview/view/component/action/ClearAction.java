package open.ucodereview.view.component.action;

import open.ucodereview.view.component.ReviewInfoModel;
import open.ucodereview.enums.ReviewListColumnEnum;
import open.ucodereview.data.ReviewDataService;

public class ClearAction implements Runnable {

  private ReviewInfoModel reviewInfoModel;
  private int[] rows;
  private ReviewDataService reviewDataService;
  private CommonCallback callBack;

  public ClearAction(ReviewInfoModel reviewInfoModel, int[] rows,
      ReviewDataService reviewDataService, CommonCallback callBack) {
    this.reviewInfoModel = reviewInfoModel;
    this.rows = rows;
    this.reviewDataService = reviewDataService;
    this.callBack = callBack;
  }

  @Override
  public void run() {
    for (int i = rows.length; i > 0; --i) {
      int newRow = rows[i - 1];
      String id = (String) this.reviewInfoModel
          .getValueAt(newRow, ReviewListColumnEnum.ID.getIndex());
      reviewDataService.removeResourse(id);
      this.reviewInfoModel.removeRow(newRow);
    }
    if (callBack != null) {
      callBack.callback();
    }
  }

  public interface CommonCallback {
    void callback();
  }
}
