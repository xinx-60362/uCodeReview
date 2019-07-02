//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package open.ucodereview.view.component;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.table.JBTable;
import open.ucodereview.util.CommonTools;
import open.ucodereview.enums.ReviewListColumnEnum;
import open.ucodereview.entity.ReviewInfo;
import open.ucodereview.data.ReviewDataService;

import javax.swing.*;
import javax.swing.RowSorter.SortKey;
import javax.swing.event.ChangeEvent;
import javax.swing.table.*;
import java.awt.*;
import java.util.Collections;

public class ShowReviewInfoListView extends SimpleToolWindowPanel {

  private Project myProject;
  private ReviewInfoModel reviewInfoModel;
  private JBTable showReviewListTable;

  public ReviewInfoModel getReviewInfoModel() {
    return this.reviewInfoModel;
  }

  public ShowReviewInfoListView(Project project) {
    super(false);
    this.myProject = project;

    JBSplitter splitter = new JBSplitter(false, 0.07F);
    this.reviewInfoModel = new ReviewInfoModel();
    this.showReviewListTable = new MyTable(this.reviewInfoModel);
    DefaultTableCellRenderer tcr = new ColorTableCellRenderer();
    tcr.setHorizontalAlignment(SwingConstants.CENTER);
    this.showReviewListTable.setDefaultRenderer(Object.class, tcr);
    TableRowSorter<TableModel> rowSorter = new TableRowSorter(this.reviewInfoModel);
    rowSorter.setSortKeys(Collections
        .singletonList(new SortKey(ReviewListColumnEnum.ID.getIndex(), SortOrder.ASCENDING)));
    rowSorter.sort();
    this.showReviewListTable.setRowSorter(rowSorter);
    TableColumn tc = this.showReviewListTable.getColumnModel()
        .getColumn(ReviewListColumnEnum.ID.getIndex());
    showReviewListTable.removeColumn(tc);

    JComboBox typeCbx = new ComboBox(CommonTools.getDefectTypes());
    this.showReviewListTable.getColumnModel()
        .getColumn(ReviewListColumnEnum.DEFECT_TYPE.getIndex())
        .setCellEditor(new DefaultCellEditor(typeCbx));

    JComboBox levelCbx = new ComboBox(CommonTools.getDefectLevels());
    this.showReviewListTable.getColumnModel()
        .getColumn(ReviewListColumnEnum.DEFECT_LEVEL.getIndex())
        .setCellEditor(new DefaultCellEditor(levelCbx));

    Object[] checkUsers = CommonTools.getCheckUsers(project);
    this.showReviewListTable.getColumnModel()
        .getColumn(ReviewListColumnEnum.CHECK_USER.getIndex())
        .setCellEditor(new DefaultCellEditor(new ComboBox(checkUsers)));
    this.showReviewListTable.getColumnModel()
        .getColumn(ReviewListColumnEnum.TO_CHECK_USER.getIndex())
        .setCellEditor(new DefaultCellEditor(new ComboBox(checkUsers)));

    this.reviewInfoModel.updateAllReviewModel(
        ReviewDataService.getInstance(project).getAllResourse());

    JScrollPane detailsScrollPane = ScrollPaneFactory.createScrollPane(this.showReviewListTable);
    JScrollPane leftButtonsScrollPane = ScrollPaneFactory.createScrollPane(
        (new ButtonGroupComponent(project, this.showReviewListTable, this.reviewInfoModel))
            .getContextPanel());
    splitter.setFirstComponent(leftButtonsScrollPane);
    splitter.setSecondComponent(detailsScrollPane);
    this.setContent(splitter);
  }


  class ColorTableCellRenderer extends DefaultTableCellRenderer {

    public ColorTableCellRenderer() {
      super();
      setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table,
        Object value,
        boolean isSelected,
        boolean hasFocus,
        int row,
        int column) {
      if (isSelected) {
        setBackground(Color.green);
      } else {
        setBackground(Color.white);
      }
      setForeground(Color.black);

      TableModel model = table.getModel();
      int modelRow = table.getRowSorter().convertRowIndexToModel(row);
      int statusColumnIndex = ReviewListColumnEnum.STATUS.getIndex();
      String statusColumnValue = (String) model.getValueAt(modelRow, statusColumnIndex);

      if (statusColumnValue.equals(ButtonGroupComponent.STATUS_RESOLVED)) {
        if (isSelected) {
          setBackground(Color.green);
        } else {
          setBackground(Color.yellow);
        }
      }

      setText(value != null ? value.toString() : "");
      return this;
    }
  }

  public class MyTable extends JBTable {
    private ReviewDataService reviewDataService;

    public MyTable(ReviewInfoModel reviewInfoModel) {
      super(reviewInfoModel);
      this.reviewDataService = ReviewDataService.getInstance(myProject);
    }

    public void editingStopped(ChangeEvent changeevent) {
      int r = getEditingRow();
      int c = getEditingColumn();
//    String editValue = (String) getValueAt(r, c);
      TableCellEditor tablecelleditor = getCellEditor();
      if (tablecelleditor != null) {
        Object obj = tablecelleditor.getCellEditorValue();
        setValueAt(obj, editingRow, editingColumn);
        System.out.println("编辑后value:" + obj);
        // 获取对象, 修改对象, 存储对象
        String id = (String) getModel()
            .getValueAt(convertRowIndexToModel(r), ReviewListColumnEnum.ID.getIndex());
        ReviewInfo rvInfo = reviewDataService.getRvInfo(id);
        if (c == ReviewListColumnEnum.DEFECT_DESC.getIndex()) {
          rvInfo.setDefectDesc(String.valueOf(obj.toString()));
        } else if (c == ReviewListColumnEnum.REVIES_DESC.getIndex()) {
          rvInfo.setReviesDesc(String.valueOf(obj.toString()));
        } else if (c == ReviewListColumnEnum.DEFECT_TYPE.getIndex()) {
          rvInfo.setDefectType(String.valueOf(obj.toString()));
        } else if (c == ReviewListColumnEnum.DEFECT_LEVEL.getIndex()) {
          rvInfo.setDefectLevel(String.valueOf(obj.toString()));
        } else if (c == ReviewListColumnEnum.CHECK_USER.getIndex()) {
          rvInfo.setCheckUser(String.valueOf(obj.toString()));
        } else if (c == ReviewListColumnEnum.TO_CHECK_USER.getIndex()) {
          rvInfo.setToCheckUser(String.valueOf(obj.toString()));
        }
        reviewDataService.resourseUpdated(rvInfo);
        removeEditor();
      }
    }
  }
}