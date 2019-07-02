package open.ucodereview.view.custom;


import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 * 自动完成器。自动找到最匹配的项目，并排在列表的最前面。
 *
 * @author Turbo Chen
 */
class AutoCompleter implements KeyListener, ItemListener {

  private JComboBox owner = null;
  private JTextField editor = null;
  private ComboBoxModel model = null;
  private final ComboBoxModel initDataModel;

  public AutoCompleter(JComboBox comboBox) {
    owner = comboBox;
    editor = (JTextField) comboBox.getEditor().getEditorComponent();
    editor.addKeyListener(this);
    model = comboBox.getModel();
    initDataModel = model;
    owner.addItemListener(this);
  }

  public void keyTyped(KeyEvent e) {
  }

  public void keyPressed(KeyEvent e) {
  }

  public void keyReleased(KeyEvent e) {
    int ch = e.getKeyCode();
    if (ch == KeyEvent.VK_UP || ch == KeyEvent.VK_DOWN || ch == KeyEvent.VK_LEFT
        || ch == KeyEvent.VK_RIGHT
        || ch == KeyEvent.VK_ENTER) {
      return;
    }

    int caretPosition = editor.getCaretPosition();
    String str = editor.getText();

    if (ch == KeyEvent.VK_DELETE || str.length() == 1) {
      model = initDataModel;
      owner.setModel(initDataModel);
    }

    autoComplete(str, caretPosition);
    editor.setText(str);
  }

  /**
   * 自动完成。根据输入的内容，在列表中找到相似的项目.
   */
  protected void autoComplete(String strf, int caretPosition) {
    Object[] opts;
    opts = getMatchingOptions(strf.substring(0, caretPosition));
    if (owner != null) {
      model = new DefaultComboBoxModel(opts);
      owner.setModel(model);
      if (opts.length > 0) {
        owner.showPopup();
        owner.setSelectedIndex(-1);
      }
    }
  }

  /**
   * 找到相似的项目, 并且将之排列到数组的最前面。
   *
   * @return 返回所有项目的列表。
   */
  protected Object[] getMatchingOptions(String str) {
    List v = new ArrayList();
    for (int k = 0; k < model.getSize(); k++) {
      Object itemObj = model.getElementAt(k);
      if (itemObj != null) {
        String item = itemObj.toString().toLowerCase();
        if (item.indexOf(str) > -1) {
          v.add(item);
        }
      }
    }
    return v.toArray();
  }

  public void itemStateChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      int caretPosition = editor.getCaretPosition();
      if (caretPosition != -1) {
        try {
          editor.moveCaretPosition(caretPosition);
        } catch (IllegalArgumentException ex) {
          ex.printStackTrace();
        }
      }
    }
  }

}