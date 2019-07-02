/*
 *  Copyright (c) 2016 Bart Cremers
 *
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without
 *  restriction, including without limitation the rights to use,
 *  copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following
 *  conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *  OTHER DEALINGS IN THE SOFTWARE.
 */

package open.ucodereview.setting;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.util.ui.JBUI;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import open.ucodereview.setting.messages.MessageBundle;
import org.jetbrains.annotations.Nullable;

public class DeveloperEditorDialog extends DialogWrapper {

  private final JTextField zhNameField = new JTextField(6);
  private final JTextField enNameField = new JTextField(11);
  private final JTextField emailField = new JTextField(18);

  DeveloperEditorDialog(JComponent parent, String title) {
    super(parent, true);
    setTitle(title);
    init();
  }

  @Override
  public void show() {
    super.show();
    zhNameField.requestFocus();
  }

  public Developer getData() {
    Developer filename = new Developer();

    filename.setZhName(convertString(zhNameField.getText()));
    filename.setEnName(convertString(enNameField.getText()));
    filename.setEmail(convertString(emailField.getText()));

    return filename;
  }

  public void setData(Developer filename) {
    zhNameField.setText(filename.getZhName());
    enNameField.setText(filename.getEnName());
    emailField.setText(filename.getEmail());
  }

  @Nullable
  @Override
  protected JComponent createCenterPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints constr;

    // zhname
    constr = new GridBagConstraints();
    constr.gridx = 0;
    constr.gridy = 0;
    constr.anchor = GridBagConstraints.WEST;
    constr.insets = JBUI.insets(5, 0, 0, 0);
    panel.add(new JLabel(MessageBundle.message("dialog.zhname.label")), constr);

    constr = new GridBagConstraints();
    constr.gridx = 1;
    constr.gridy = 0;
    constr.weightx = 1;
    constr.insets = JBUI.insets(5, 10, 0, 10);
//        constr.fill = GridBagConstraints.HORIZONTAL;
    constr.anchor = GridBagConstraints.WEST;
    panel.add(zhNameField, constr);

    // enname
    constr = new GridBagConstraints();
    constr.gridx = 0;
    constr.gridy = 1;
    constr.anchor = GridBagConstraints.WEST;
    constr.insets = JBUI.insets(5, 0, 0, 0);
    panel.add(new JLabel(MessageBundle.message("dialog.enname.label")), constr);

    constr = new GridBagConstraints();
    constr.gridx = 1;
    constr.gridy = 1;
    constr.insets = JBUI.insets(5, 10, 0, 10);
//        constr.fill = GridBagConstraints.HORIZONTAL;
    constr.anchor = GridBagConstraints.WEST;
    panel.add(enNameField, constr);

    // email
    constr = new GridBagConstraints();
    constr.gridx = 0;
    constr.gridy = 2;
    constr.anchor = GridBagConstraints.WEST;
    constr.insets = JBUI.insets(5, 0, 0, 0);
    panel.add(new JLabel(MessageBundle.message("dialog.email.label")), constr);

    constr = new GridBagConstraints();
    constr.gridx = 1;
    constr.gridy = 2;
//        constr.weightx = 6;
    constr.insets = JBUI.insets(5, 10, 0, 0);
    constr.fill = GridBagConstraints.HORIZONTAL;
    constr.anchor = GridBagConstraints.EAST;
    panel.add(emailField, constr);

    return panel;
  }

  private String convertString(String s) {
    if (s != null && s.trim().isEmpty()) {
      return null;
    }
    return s;
  }
}
