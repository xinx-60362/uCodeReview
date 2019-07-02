package open.ucodereview.view.component.action;

import com.intellij.openapi.project.Project;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import open.ucodereview.entity.ReviewInfo;

@Slf4j
public class SendMailAction implements Runnable {
  private Project myProject;
  private List<ReviewInfo> codeReviewInfoEntities;
  private SendCallBack callBack;

  public SendMailAction(Project myProject, List<ReviewInfo> codeReviewInfoEntities,
      SendCallBack callBack) {
    this.myProject = myProject;
    this.codeReviewInfoEntities = codeReviewInfoEntities;
    this.callBack = callBack;
  }

  @Override
  public void run() {
    ExportAction exportAction = new ExportAction(myProject, codeReviewInfoEntities, null);
    exportAction.run();
    String excelPath = exportAction.getExcelPath();
    callBack.callback(excelPath);
  }

  public interface SendCallBack {

    void callback(String path);
  }
}
