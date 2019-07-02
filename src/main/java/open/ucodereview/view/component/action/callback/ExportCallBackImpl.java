package open.ucodereview.view.component.action.callback;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import open.ucodereview.view.component.action.ExportAction.ExportCallBack;
import org.apache.commons.lang.StringUtils;
import open.ucodereview.setting.messages.MessageBundle;

public class ExportCallBackImpl implements ExportCallBack {
  private JPanel contextPanel;
  public ExportCallBackImpl(JPanel contextPanel) {
    this.contextPanel = contextPanel;
  }

  @Override
  public void callback(String path) {
    String message1 = MessageBundle.message("alert.export.success", path);
    String message2 = MessageBundle.message("alert.export.fail");
    String message = StringUtils.isNotBlank(path) ? message1 : message2;
    JOptionPane.showMessageDialog(contextPanel, message);
  }
}
