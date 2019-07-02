package open.ucodereview.view.component.action.callback;

import com.intellij.openapi.project.Project;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.plexus.util.FileUtils;
import open.ucodereview.setting.MailSend;
import open.ucodereview.setting.messages.MessageBundle;
import open.ucodereview.view.component.action.SendMailAction.SendCallBack;
import open.ucodereview.util.CommonTools;
import open.ucodereview.util.DateUtil;

@Slf4j
public class SendMailCallBack implements SendCallBack {

  private Project myProject;
  private JPanel contextPanel;

  public SendMailCallBack(Project myProject, JPanel contextPanel) {
    this.myProject = myProject;
    this.contextPanel = contextPanel;
  }

  @Override
  public void callback(String excelPath) {
    if (StringUtils.isBlank(excelPath)) {
      return;
    }
    String title = MessageBundle
        .message("msg.send.mail.title", myProject.getName(), DateUtil.getDateString());
    try {
      List<String> develperEmailList = CommonTools.getDevelperEmailList(myProject);
      int size = develperEmailList.size();
      if (size > 0) {
        int option = JOptionPane
            .showConfirmDialog(contextPanel,
                MessageBundle.message("alert.send.mail.content", develperEmailList, size),
                MessageBundle.message("alert.send.mail.title"), JOptionPane.YES_NO_OPTION);
        if (option == 0) { // O为YES, 1为NO
          MailSend.sendMail(title, develperEmailList, title, excelPath);
          JOptionPane.showMessageDialog(contextPanel, MessageBundle.message(
              "alert.send.mail.success"));
        }
      } else {
        JOptionPane.showMessageDialog(contextPanel,
            MessageBundle.message("alert.send.mail.emptylist"));
      }
    } catch (Exception e1) {
      String message = MessageBundle.message("alert.send.mail.fail", e1.getMessage());
      log.error(message, e1);
      JOptionPane.showMessageDialog(contextPanel,
          message);
    } finally {
      try {
        FileUtils.fileDelete(excelPath); // 发送完邮件自动删除附件
      } catch (Exception e) {
        log.error("删除文件失败", e);
      }
    }
  }
}
