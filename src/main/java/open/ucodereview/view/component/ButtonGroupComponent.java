//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package open.ucodereview.view.component;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.table.JBTable;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import open.ucodereview.data.ReviewDataService;
import open.ucodereview.entity.ReviewInfo;
import open.ucodereview.enums.ReviewListColumnEnum;
import open.ucodereview.setting.messages.MessageBundle;
import open.ucodereview.util.CommonTools;
import open.ucodereview.view.component.action.ClearAction;
import open.ucodereview.view.component.action.ExportAction;
import open.ucodereview.view.component.action.SendMailAction;
import open.ucodereview.view.component.action.callback.ExportCallBackImpl;
import open.ucodereview.view.component.action.callback.SendMailCallBack;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;

public class ButtonGroupComponent {

  public final static String STATUS_ALL = "全部";
  public final static String STATUS_RESOLVED = "已解决";
  public final static String STATUS_UNRESOLVED = "未解决";
  public static String status = STATUS_ALL;
  private final ReviewDataService reviewDataService;
  private Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
  private Project myProject;
  private JBTable showReviewListTable;
  private ReviewInfoModel reviewInfoModel;
  private JButton exportButton;
  private JButton clearButton;
  private JButton refreshButton;
  private JButton sendButton;
  private ComboBox rvStatusCbx;
  private JPanel contextPanel;
  private JPopupMenu resolveMenu;

  public ButtonGroupComponent(Project project, JBTable table, ReviewInfoModel model) {
    this.myProject = project;
    this.showReviewListTable = table;
    this.reviewInfoModel = model;

    reviewDataService = ReviewDataService.getInstance(myProject);
    reviewDataService.setLeftButtonsView(this);

    this.createUIComponents();
    this.initLinstener();
  }

  public JPanel getContextPanel() {
    return this.contextPanel;
  }

  private void initLinstener() {
    this.showReviewListTable.addMouseListener(new TableMouseListener());

    this.rvStatusCbx.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == 1) { // 事件类型 1为新值,2为旧值
          if (rvStatusCbx.getSelectedIndex() == -1) {
            return;
          }
          status = e.getItem().toString();
          List<ReviewInfo> codeReviewInfoEntities = reviewDataService
              .getResourseByStatus(status);
          reviewInfoModel.updateAllReviewModel(codeReviewInfoEntities);
        }
      }
    });

    this.refreshButton.addActionListener((e) -> {
      rvStatusCbx.setSelectedIndex(-1);
      rvStatusCbx.setSelectedIndex(0);
    });

    this.sendButton.addActionListener((e) -> {
      javax.swing.SwingUtilities.invokeLater(new SendMailAction(myProject, reviewDataService
          .getAllResourse(), new SendMailCallBack(myProject, contextPanel)));
    });

    this.clearButton.addActionListener((e) -> {
      int[] rows = this.showReviewListTable.getSelectedRows();
      if (rows.length != 0) {
        int option = JOptionPane
            .showConfirmDialog(contextPanel,
                MessageBundle.message("alert.clear.warning", rows.length),
                MessageBundle.message("alert.clear.title"), JOptionPane.YES_NO_OPTION);
        if (option == 0) { // O为YES, 1为NO
          // 转成model里的row
          int[] newRows = Arrays.stream(rows)
              .map(row -> CommonTools.convert2ModelRow(showReviewListTable, row)).toArray();
          javax.swing.SwingUtilities.invokeLater(
              new ClearAction(reviewInfoModel, newRows, reviewDataService,
                  () -> JOptionPane
                      .showMessageDialog(contextPanel,
                          MessageBundle.message("alert.clear.success", rows.length))));
        }
      } else {
        JOptionPane.showMessageDialog(contextPanel, MessageBundle.message("alert.clear.emptylist"));
      }
    });

    this.exportButton.addActionListener((e) -> {
      javax.swing.SwingUtilities.invokeLater(new ExportAction(myProject, reviewDataService
          .getAllResourse(), new ExportCallBackImpl(contextPanel)));
    });
  }

  private void createUIComponents() {
    this.contextPanel = new JPanel();
    this.contextPanel.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
    this.rvStatusCbx = new ComboBox(
        new DefaultComboBoxModel(new String[]{STATUS_ALL, STATUS_RESOLVED, STATUS_UNRESOLVED}));
    this.contextPanel.add(this.rvStatusCbx,
        new GridConstraints(0, 0, 1, 1, 0, 0, 3, 0, (Dimension) null, new Dimension(30, 10),
            (Dimension) null, 0, false));

    this.clearButton = new JButton();
    this.clearButton.setText("清除");
    this.clearButton.setToolTipText("清除所选的检视项");
    this.contextPanel.add(this.clearButton,
        new GridConstraints(1, 0, 1, 1, 0, 0, 3, 0, (Dimension) null, new Dimension(30, 10),
            (Dimension) null, 0, false));

    this.exportButton = new JButton();
    this.exportButton.setText("导出");
    this.exportButton.setToolTipText("导出所有检视项");
    this.contextPanel.add(this.exportButton,
        new GridConstraints(2, 0, 1, 1, 0, 0, 3, 0, (Dimension) null, new Dimension(30, 10),
            (Dimension) null, 0, false));

    sendButton = new JButton("发送");
    this.contextPanel.add(this.sendButton,
        new GridConstraints(3, 0, 1, 1, 0, 0, 3, 0, (Dimension) null, new Dimension(30, 10),
            (Dimension) null, 0, false));

    refreshButton = new JButton("刷新");
    this.contextPanel.add(this.refreshButton,
        new GridConstraints(4, 0, 1, 1, 0, 0, 3, 0, (Dimension) null, new Dimension(30, 10),
            (Dimension) null, 0, false));

    createPopupMenu();
  }

  public JComponent $$$getRootComponent$$$() {
    return this.contextPanel;
  }

  private void createPopupMenu() {
    resolveMenu = new JPopupMenu();

    JMenuItem statusElement = new JMenuItem();
    statusElement.setText(ButtonGroupComponent.STATUS_RESOLVED);
    statusElement.addActionListener((evt) -> changeStatus(1));
    resolveMenu.add(statusElement);

    ActionListener actionListener = getActionListener();
    JMenuItem copyMenItem = new JMenuItem();
    copyMenItem.setText(MessageBundle.message("table.menu.item.copycell"));
    copyMenItem.addActionListener(actionListener);
    resolveMenu.add(copyMenItem);
  }

  @NotNull
  private ActionListener getActionListener() {
    return (evt) -> {
      int[] rows = this.showReviewListTable.getSelectedRows();
      if (rows.length != 0 && showReviewListTable.getSelectedColumn() != -1) {
        Object valueAt = this.showReviewListTable
            .getValueAt(rows[0], showReviewListTable.getSelectedColumn());
        StringSelection stsel = new StringSelection(valueAt.toString());
        systemClipboard.setContents(stsel, stsel);
      } else {
        JOptionPane.showMessageDialog(contextPanel, MessageBundle.message(
            "alert.table.cell.leftclick"));
      }
    };
  }

  private void changeStatus(int status) {
    int[] rows = this.showReviewListTable.getSelectedRows();
    if (rows.length != 0) {
      int modelRow = CommonTools.convert2ModelRow(showReviewListTable, rows[0]);
      String id = (String) this.reviewInfoModel
          .getValueAt(modelRow, ReviewListColumnEnum.ID.getIndex());
      reviewDataService.changeResourseStatus(id, status);
      reviewInfoModel.setValueAt(
          status == 1 ? ButtonGroupComponent.STATUS_RESOLVED : ButtonGroupComponent.STATUS_UNRESOLVED, modelRow,
          ReviewListColumnEnum.STATUS.getIndex());

      //自动更新list
      refreshList();
    }
  }

  public void refreshList() {
    List<ReviewInfo> codeReviewInfoEntities = reviewDataService
        .getResourseByStatus(status);
    System.out.println(status + codeReviewInfoEntities);
    reviewInfoModel.updateAllReviewModel(codeReviewInfoEntities);
  }

  class TableMouseListener implements MouseListener {
    @Override
    public void mouseClicked(MouseEvent e) {
      if (!showReviewListTable.getSelectionModel().isSelectionEmpty()) {
        int modelRow = CommonTools
            .convert2ModelRow(showReviewListTable, showReviewListTable.getSelectedRow());
        String id = (String) reviewInfoModel.getValueAt(modelRow, ReviewListColumnEnum.ID.getIndex());
        ReviewInfo reviewInfo = reviewDataService.getResourse(id);
        if (null != reviewInfo) {
          VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(
              reviewInfo.getFileDetailPath());
          if (null != virtualFile) {
            FileEditorManager fileEditorManager = FileEditorManager.getInstance(myProject);
            Integer startLineNumber = reviewInfo.getStartLineNumber();
            fileEditorManager.openTextEditor(new OpenFileDescriptor(myProject,
                virtualFile, startLineNumber - 1, 0), true);
          }
        }
      }
      if (e.getButton() == java.awt.event.MouseEvent.BUTTON3) {
        //通过点击位置找到点击为表格中的行
        int focusedRowIndex = showReviewListTable.rowAtPoint(e.getPoint());
        if (focusedRowIndex == -1) {
          return;
        }
        //将表格所选项设为当前右键点击的行
        showReviewListTable.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
        //弹出菜单
        popMenu(e);
      }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
  }

  private void popMenu(MouseEvent e) {
    int[] rows = this.showReviewListTable.getSelectedRows();
    if (rows.length != 0) {
      int modelRow = CommonTools.convert2ModelRow(showReviewListTable, rows[0]);
      String id = (String) this.reviewInfoModel
          .getValueAt(modelRow, ReviewListColumnEnum.ID.getIndex());
      ReviewInfo rvInfo = reviewDataService
          .getRvInfo(id);

      JMenuItem statusElement = (JMenuItem) resolveMenu.getSubElements()[0];
      if (rvInfo.getStatus() == 0) {
        statusElement.setText(STATUS_RESOLVED);
        statusElement.addActionListener((evt) -> changeStatus(1));
      } else {
        statusElement.setText(STATUS_UNRESOLVED);
        statusElement.addActionListener((evt) -> changeStatus(0));
      }
      resolveMenu.show(showReviewListTable, e.getX(), e.getY());
    }
  }
}
