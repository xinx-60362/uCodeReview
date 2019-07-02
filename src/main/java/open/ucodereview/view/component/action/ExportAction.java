package open.ucodereview.view.component.action;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import open.ucodereview.entity.ReviewInfo;
import open.ucodereview.util.DateUtil;
import open.ucodereview.enums.ReviewListColumnEnum;
import open.ucodereview.setting.messages.MessageBundle;

@Slf4j
public class ExportAction implements Runnable{
  private Project myProject;
  private List<ReviewInfo> codeReviewInfoEntities;
  private ExportCallBack callBack;
  private String excelPath = null;

  public ExportAction(Project myProject, List<ReviewInfo> codeReviewInfoEntities, ExportCallBack callBack){
    this.myProject = myProject;
    this.codeReviewInfoEntities = codeReviewInfoEntities;
    this.callBack = callBack;
  }

  @Override
  public void run() {
    VirtualFile file = myProject.getBaseDir();
    if (null != file) {
      StringBuilder sbody = new StringBuilder();
      sbody.append("<?xml version=\"1.0\"?>\n");
      sbody.append("<?mso-application progid=\"Excel.Sheet\"?>\n");
      sbody.append("<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\"\n");
      sbody.append("xmlns:o=\"urn:schemas-microsoft-com:office:office\"\n");
      sbody.append("xmlns:x=\"urn:schemas-microsoft-com:office:excel\"\n");
      sbody.append("xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\"\n");
      sbody.append("xmlns:html=\"http:\\www.w3.org\\TR\\REC-html40\">\n");
      sbody.append("<Styles>\n");
      sbody.append("<Style ss:ID=\"s50\">");
      sbody.append("<Font ss:Bold=\"1\" ss:Size=\"10\" x:CharSet=\"134\" ss:FontName=\"宋体\"/>");
      sbody.append("</Style>");
      sbody.append("</Styles>\n");
      sbody.append("<Worksheet ss:Name=\"review info\"><Table>");
      sbody.append("<Column ss:Index=\"1\" ss:Width=\"50\"/>");
      sbody.append("<Column ss:Index=\"2\" ss:Width=\"300\"/>");
      sbody.append("<Column ss:Index=\"3\" ss:Width=\"200\"/>");
      sbody.append("<Column ss:Index=\"4\" ss:Width=\"50\"/>");
      sbody.append("<Column ss:Index=\"5\" ss:Width=\"100\"/>");
      sbody.append("<Column ss:Index=\"6\" ss:Width=\"100\"/>");
      sbody.append("<Column ss:Index=\"7\" ss:Width=\"50\"/>");
      sbody.append("<Column ss:Index=\"8\" ss:Width=\"50\"/>");
      sbody.append("<Column ss:Index=\"9\" ss:Width=\"50\"/>");
      sbody.append("<Column ss:Index=\"10\" ss:Width=\"125\"/>");
      Collections.sort(codeReviewInfoEntities);
      if (codeReviewInfoEntities != null && codeReviewInfoEntities.size() > 0) {
        sbody.append("<Row>");
        sbody.append("<Cell ss:StyleID=\"s50\"><Data ss:Type=\"String\">"
            + ReviewListColumnEnum.PROJECT_NAME.getName() + "</Data></Cell>");
        sbody.append("<Cell ss:StyleID=\"s50\"><Data ss:Type=\"String\">"
            + ReviewListColumnEnum.FILE_DETAIL_PATH.getName() + "</Data></Cell>");
        sbody.append("<Cell ss:StyleID=\"s50\"><Data ss:Type=\"String\">"
            + ReviewListColumnEnum.SELECTED_CONTEXT.getName() + "</Data></Cell>");
        sbody.append("<Cell ss:StyleID=\"s50\"><Data ss:Type=\"String\">"
            + ReviewListColumnEnum.LINE_NUMBER.getName() + "</Data></Cell>");
        sbody.append("<Cell ss:StyleID=\"s50\"><Data ss:Type=\"String\">"
            + ReviewListColumnEnum.DEFECT_DESC.getName() + "</Data></Cell>");
        sbody.append("<Cell ss:StyleID=\"s50\"><Data ss:Type=\"String\">"
            + ReviewListColumnEnum.REVIES_DESC.getName() + "</Data></Cell>");
        sbody.append("<Cell ss:StyleID=\"s50\"><Data ss:Type=\"String\">"
            + ReviewListColumnEnum.DEFECT_TYPE.getName() + "</Data></Cell>");
        sbody.append("<Cell ss:StyleID=\"s50\"><Data ss:Type=\"String\">"
            + ReviewListColumnEnum.CHECK_USER.getName() + "</Data></Cell>");
        sbody.append("<Cell ss:StyleID=\"s50\"><Data ss:Type=\"String\">"
            + ReviewListColumnEnum.TO_CHECK_USER.getName() + "</Data></Cell>");
        sbody.append("<Cell ss:StyleID=\"s50\"><Data ss:Type=\"String\">"
            + ReviewListColumnEnum.OPRA_TIME.getName() + "</Data></Cell>");
        sbody.append(
            "<Cell ss:StyleID=\"s50\"><Data ss:Type=\"String\">" + ReviewListColumnEnum.STATUS
                .getName() + "</Data></Cell>");
        sbody.append("</Row>");
        Iterator var6 = codeReviewInfoEntities.iterator();

        while (var6.hasNext()) {
          ReviewInfo reviewInfo = (ReviewInfo) var6.next();
          sbody.append("<Row>");
          sbody.append(
              "<Cell><Data ss:Type=\"String\">" + reviewInfo.getProjectName() + "</Data></Cell>");
          sbody.append("<Cell><Data ss:Type=\"String\">" + reviewInfo.getFileDetailPath()
              + "</Data></Cell>");
          sbody.append("<Cell><Data ss:Type=\"String\">" + StringUtil
              .escapeXml(reviewInfo.getSelectedContext()) + "</Data></Cell>");
          sbody.append("<Cell><Data ss:Type=\"String\">" + reviewInfo.getStartLineNumber() + "-"
              + reviewInfo.getEndLineNumber() + "</Data></Cell>");
          sbody.append(
              "<Cell><Data ss:Type=\"String\">" + StringUtil.escapeXml(reviewInfo.getDefectDesc())
                  + "</Data></Cell>");
          sbody.append(
              "<Cell><Data ss:Type=\"String\">" + StringUtil.escapeXml(reviewInfo.getReviesDesc())
                  + "</Data></Cell>");
          sbody.append(
              "<Cell><Data ss:Type=\"String\">" + reviewInfo.getDefectType() + "</Data></Cell>");
          sbody.append(
              "<Cell><Data ss:Type=\"String\">" + reviewInfo.getCheckUser() + "</Data></Cell>");
          sbody.append(
              "<Cell><Data ss:Type=\"String\">" + reviewInfo.getToCheckUser() + "</Data></Cell>");
          sbody.append(
              "<Cell><Data ss:Type=\"String\">" + reviewInfo.getOpraTime() + "</Data></Cell>");
          sbody.append(
              "<Cell><Data ss:Type=\"String\">" + reviewInfo.getStatusName() + "</Data></Cell>");
          sbody.append("</Row>");
        }
      }

      sbody.append("</Table></Worksheet>");
      sbody.append("</Workbook>\n");
      String fileName = "CodeReview" + DateUtil.getTimeStamp() + ".xls";
      FileOutputStream fileOutputStream = null;

      try {
        byte[] data = sbody.toString().getBytes("utf-8");
        File file1 = new File(file.getPath().concat("/").concat(fileName));
        if (!file1.exists()) {
          file1.createNewFile();
        }

        fileOutputStream = new FileOutputStream(file1);
        fileOutputStream.write(data);
        excelPath = file1.getPath();
      } catch (Exception e) {
        log.error(MessageBundle.message("msg.export.fail"), e);
      } finally {
        if (null != fileOutputStream) {
          try {
            fileOutputStream.close();
          } catch (IOException e) {
            log.error("io error", e);
          }
        }

        if(callBack != null){
          callBack.callback(excelPath);
        }
      }
    }
  }

  public String getExcelPath() {
    return excelPath;
  }

  public interface ExportCallBack {
    void callback(String path);
  }
}
