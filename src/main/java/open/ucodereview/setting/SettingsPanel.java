package open.ucodereview.setting;

import com.intellij.openapi.project.Project;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import open.ucodereview.data.ReviewDataService;
import open.ucodereview.setting.messages.MessageBundle;
import org.jetbrains.annotations.NotNull;

public class SettingsPanel extends JPanel {
  private final Project project;
  private final JBList filenameList;
  private final DefaultListModel<Developer> listModel;

  private boolean isModified = false;

  public SettingsPanel(Project project) {
    this.project = project;

    listModel = new DefaultListModel<Developer>();
    initializeListModel();
    filenameList = new JBList(listModel);
    filenameList.setCellRenderer(new DefaultListCellRenderer() {
      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value, int index,
          boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        setText(((Developer) value).getZhName());
        return this;

      }
    });
    setLayout(new BorderLayout());

    add(ToolbarDecorator.createDecorator(filenameList)
        .setAddAction(r -> addNew())
        .setRemoveAction(r -> removeSelected())
        .setEditAction(r -> editSelected())
        .disableUpAction()
        .disableDownAction()
        .createPanel());
  }

  public void apply() {
    List<Developer> newList = new ArrayList<Developer>(listModel.getSize());
    Enumeration<Developer> elements = listModel.elements();
    while (elements.hasMoreElements()) {
      newList.add(elements.nextElement());
    }

    ReviewDataService.getInstance(project).saveDevelopers(newList);
    isModified = false;
  }

  public void reset() {
    listModel.removeAllElements();
    initializeListModel();
    isModified = false;
  }

  private void initializeListModel() {
    for (Developer filename : ReviewDataService.getInstance(project).getDevelopers()) {
      listModel.add(listModel.size(), filename);
    }
  }

  private void addNew() {
    DeveloperEditorDialog dlg = createConfigurableFilenameEditorDialog(
        MessageBundle.message("dialog.add.title"));
    Developer filename = new Developer();
    dlg.setData(filename);
    if (dlg.showAndGet()) {
      insertNewConfigurableFilename(dlg.getData(), true);
    }
    filenameList.requestFocus();
    isModified = true;
  }

  private void removeSelected() {
    int selectedIndex = filenameList.getSelectedIndex();
    if (selectedIndex != -1) {
      listModel.remove(selectedIndex);
    }

    filenameList.requestFocus();
    isModified = true;
  }

  private void editSelected() {
    int selectedIndex = filenameList.getSelectedIndex();
    if (selectedIndex != -1) {
      Developer filename = listModel.getElementAt(selectedIndex);
      DeveloperEditorDialog dlg = createConfigurableFilenameEditorDialog(
          MessageBundle.message("dialog.edit.title"));
      dlg.setData(filename);
      if (dlg.showAndGet()) {
        listModel.set(selectedIndex, dlg.getData());
      }
    }

    filenameList.requestFocus();
    isModified = true;
  }

  private DeveloperEditorDialog createConfigurableFilenameEditorDialog(String title) {
    return new DeveloperEditorDialog(this, title);
  }

  private void insertNewConfigurableFilename(@NotNull Developer filename,
      boolean setSelected) {
    listModel.add(listModel.size(), filename);
    if (setSelected) {
      filenameList.setSelectedValue(filename, true);
    }
  }

  public boolean isModified() {
    return isModified;
  }
}