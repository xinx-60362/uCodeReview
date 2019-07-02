package open.ucodereview.enums;

/**
 * @Description 应该始终把ID放最后, 因为id要隐藏. 枚举名务必与ReviewInfo里的字段名保持一致,除了必要的下划线
 * @author nengcai.wang
 * @Date 2019/6/26
 */
public enum ReviewListColumnEnum {
  PROJECT_NAME(0, "项目名称"), FILE_DETAIL_PATH(1, "文件路径"), SELECTED_CONTEXT(2, "上下文"), LINE_NUMBER(3,
      "行号"),
  DEFECT_DESC(4, "缺陷说明", true), REVIES_DESC(5, "修订说明", true), DEFECT_TYPE(6, "缺陷类型", true), CHECK_USER(7, "检查人", true),
  TO_CHECK_USER(8, "被检查人", true), OPRA_TIME(9, "时间"), STATUS(10, "状态"), DEFECT_LEVEL(11, "优先级", true), ID(12, "ID");

  private int index;
  private String name;
  private boolean editable;

  ReviewListColumnEnum(int index, String name) {
    this(index, name, false);
  }

  ReviewListColumnEnum(int index, String name, boolean editable) {
    this.index = index;
    this.name = name;
    this.editable = editable;
  }

  public int getIndex() {
    return this.index;
  }

  public String getName() {
    return this.name;
  }

  public boolean isEditable() {
    return editable;
  }
}
