package open.ucodereview.view;

import com.google.common.collect.Lists;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.PsiFile;
import com.intellij.ui.JBColor;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import open.ucodereview.view.component.ShowReviewInfoListView;
import open.ucodereview.util.CommonTools;
import open.ucodereview.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import open.ucodereview.entity.ReviewInfo;
import open.ucodereview.data.ReviewDataService;
import open.ucodereview.view.custom.JAutoCompleteComboBox;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AddCodeReviewView
    extends DialogWrapper {

  private Project project;
  private Editor editor;
  private PsiFile mFile;
  private JPanel contentPane;
  private JAutoCompleteComboBox reviewUser;
  private JAutoCompleteComboBox toReviewUser;
  private JComboBox defectTypeList;
  private JComboBox defectLevelList;
  private JTextArea defectDescription;
  private JTextArea reviesDescription;
  private ReviewDataService reviewDataService;

  private void createUIComponents() {
  }

  @Nullable
  protected JComponent createCenterPanel() {
    return this.contentPane;
  }

  public AddCodeReviewView(Project project, Editor editor, PsiFile mFile) {
    super(true);
    this.project = project;
    this.editor = editor;
    this.mFile = mFile;
    reviewDataService = ReviewDataService.getInstance(project);

    createComponents();
    setTitle("Add Code Review");
    setSize(368, 400);
    init();

    ReviewInfo reviewInfo = reviewDataService.getResourseFromCache();
    if (null != reviewInfo) {
      this.reviewUser.setText(reviewInfo.getCheckUser());
      this.toReviewUser.setText(reviewInfo.getToCheckUser());
      this.defectTypeList.setSelectedItem(reviewInfo.getDefectType());
      this.defectLevelList.setSelectedItem(reviewInfo.getDefectLevel());
    }
  }

  protected void doOKAction() {
    String checkUser = this.reviewUser.getText();
    if(StringUtils.isBlank(checkUser)){
      JOptionPane.showMessageDialog(contentPane, "请先选择检查人!");
      return ;
    }
    String toCheckUser = this.toReviewUser.getText();
    if(StringUtils.isBlank(toCheckUser)){
      JOptionPane.showMessageDialog(contentPane, "请先选择被检查人!");
      return ;
    }
    String defectType = String.valueOf(this.defectTypeList.getSelectedItem());
    if(StringUtils.isBlank(defectType)|| defectType.equalsIgnoreCase("null")){
      JOptionPane.showMessageDialog(contentPane, "请先选择缺陷类型!");
      return ;
    }
    String defectLevel = String.valueOf(this.defectLevelList.getSelectedItem());
    if(StringUtils.isBlank(defectLevel) || defectLevel.equalsIgnoreCase("null")){
      JOptionPane.showMessageDialog(contentPane, "请先选择优先级!");
      return ;
    }

    ReviewInfo reviewInfo = new ReviewInfo();
    reviewInfo.setDefectType(defectType);
    reviewInfo.setDefectLevel(defectLevel);
    reviewInfo.setDefectDesc(this.defectDescription.getText());
    reviewInfo.setCheckUser(checkUser);
    reviewInfo.setFileDetailPath(this.mFile.getVirtualFile().getCanonicalPath());
    reviewInfo.setOpraTime(DateUtil.getDateString());
    reviewInfo.setProjectName(this.project.getName());
    reviewInfo.setReviesDesc(this.reviesDescription.getText());
    reviewInfo.setToCheckUser(toCheckUser);
    if (null != this.editor.getSelectionModel()) {
      Document document = this.editor.getDocument();
      CaretModel caretModel = this.editor.getCaretModel();
      int lineNum = document.getLineNumber(caretModel.getOffset()) + 1;
      int startPosition = this.editor.getSelectionModel().getSelectionStartPosition().line;

      reviewInfo.setStartLineNumber(Integer.valueOf(lineNum));
      reviewInfo.setEndLineNumber(Integer.valueOf(
          this.editor.getSelectionModel().getSelectionEndPosition().line + lineNum
              - startPosition));
      reviewInfo.setSelectedContext(this.editor.getSelectionModel().getSelectedText());
      reviewInfo.setReviesDesc(reviewInfo.getReviesDesc());
    }
    reviewInfo.setId(DateUtil.getTimeStamp());
    reviewDataService.resourseUpdated(reviewInfo);
    reviewDataService.cacheUpdated(reviewInfo);
    List<ReviewInfo> codeReviewInfoEntities = Lists.newArrayList();
    codeReviewInfoEntities.add(reviewInfo);
    ToolWindow toolWindow = ToolWindowManager.getInstance(this.project)
        .getToolWindow("UCodeReview");
    ContentManager contentManager = toolWindow.getContentManager();
    Content content = contentManager.findContent("Show Review");
    if ((content.getComponent() instanceof ShowReviewInfoListView)) {
      ((ShowReviewInfoListView) content.getComponent()).getReviewInfoModel()
          .updateReviewModel(codeReviewInfoEntities);
    }
    super.doOKAction();
  }

  private void createComponents() {
    this.contentPane = new JPanel();
    this.contentPane.setLayout(new GridBagLayout());
    this.contentPane.setBackground(JBColor.GRAY);
    this.contentPane.setEnabled(true);
    this.contentPane.setForeground(JBColor.BLACK);
    this.contentPane.setInheritsPopupMenu(true);
    this.contentPane.setMinimumSize(new Dimension(368, 400));
    this.contentPane.setPreferredSize(new Dimension(368, 400));
    JPanel panel1 = new JPanel();
    panel1.setLayout(new GridLayoutManager(6, 4, new Insets(0, 0, 0, 0), -1, -1));
    panel1.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
    panel1.setPreferredSize(new Dimension(280, 400));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 1.0D;
    gbc.weighty = 1.0D;
    gbc.fill = 1;
    this.contentPane.add(panel1, gbc);
    Spacer spacer1 = new Spacer();
    panel1.add(spacer1,
        new GridConstraints(1, 1, 1, 1, 0, 1, 4, 1, null, null,
            null, 0, false));


    Object[] objects = CommonTools.getCheckUsers(project);
    this.reviewUser = new JAutoCompleteComboBox(new DefaultComboBoxModel(objects));
    this.toReviewUser = new JAutoCompleteComboBox(new DefaultComboBoxModel(objects));

    JLabel label1 = new JLabel();
    label1.setText("检查人");
    panel1.add(label1,
        new GridConstraints(0, 0, 1, 1, 8, 0, 0, 0, null, null,
            null, 0, false));
    panel1.add(this.reviewUser,
        new GridConstraints(0, 1, 1, 3, 8, 1, 4, 0, null, new Dimension(150, -1),
            null, 0, false));
    JLabel label2 = new JLabel();
    label2.setText("被检查人");
    panel1.add(label2,
        new GridConstraints(2, 0, 1, 1, 8, 0, 0, 0, null, null,
            null, 0, false));
    panel1.add(this.toReviewUser,
        new GridConstraints(2, 1, 1, 3, 8, 1, 4, 0, null, new Dimension(150, -1),
            null, 0, false));

    JLabel label3 = new JLabel();
    label3.setText("缺陷说明");
    panel1.add(label3,
        new GridConstraints(3, 0, 1, 1, 8, 0, 0, 0, null, null,
            null, 0, false));
    this.defectDescription = new JTextArea();
    this.defectDescription.setLineWrap(true);
    this.defectDescription.setWrapStyleWord(true);
    JScrollPane scrollPane1 = new JScrollPane();
    scrollPane1.setViewportView(this.defectDescription);
    panel1.add(scrollPane1,
        new GridConstraints(3, 1, 1, 3, 0, 3, 5, 5, null, null,
            null, 0, false));

    JLabel label4 = new JLabel();
    label4.setText("缺陷类型");
    panel1.add(label4,
        new GridConstraints(4, 0, 1, 1, 0, 1, 0, 0, null, null,
            null, 0, false));
    this.defectTypeList = new ComboBox();
    DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel(CommonTools.getDefectTypes());
    this.defectTypeList.setModel(defaultComboBoxModel1);
    panel1.add(this.defectTypeList,
        new GridConstraints(4, 1, 1, 1, 0, 1, 0, 0, null, null,
            null, 0, false));
    panel1.add(new JLabel("优先级"),
        new GridConstraints(4, 2, 1, 1, 0, 1, 0, 0, null, null,
            null, 0, false));
    defectLevelList = new ComboBox();
    DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel(CommonTools.getDefectLevels());
    defectLevelList.setModel(defaultComboBoxModel2);
    panel1.add(defectLevelList,
        new GridConstraints(4, 3, 1, 1, 8, 1, 6, 0, null, null,
            null, 0, false));

    JLabel label5 = new JLabel();
    label5.setText("修订说明");
    panel1.add(label5,
        new GridConstraints(5, 0, 1, 1, 8, 0, 0, 0, null, null,
            null, 0, false));

    this.reviesDescription = new JTextArea();
    this.reviesDescription.setLineWrap(true);
    this.reviesDescription.setWrapStyleWord(true);
    JScrollPane scrollPane2 = new JScrollPane();
    scrollPane2.setViewportView(this.reviesDescription);
    panel1.add(scrollPane2,
        new GridConstraints(5, 1, 1, 3, 0, 3, 5, 5, null, null,
            null, 0, false));
  }

  public JComponent $$$getRootComponent$$$() {
    return this.contentPane;
  }
}
