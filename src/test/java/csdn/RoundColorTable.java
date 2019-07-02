package test.csdn;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class RoundColorTable extends JFrame {

  private String[] colname = {"第1列", "第2列", "第3列", "第4列", "第5列"}; //表头信息
  private String[][] data = new String[10][5]; //表内容
  private JTable table;

  public RoundColorTable() {
    //表内容赋值
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 5; j++) {
        data[i][j] = "( " + (j + 1) + ", " + (i + 1) + " )";
      }
    }
    table = new JTable(new DefaultTableModel(data, colname));
    TableCellRenderer tcr = new ColorTableCellRenderer();
    table.setDefaultRenderer(Object.class, tcr);//为JTable增加渲染器，因为是针对于表格中的所有单元格，所有用Object.class
    add(new JScrollPane(table), BorderLayout.CENTER);
    setVisible(true);
    setSize(500, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public static void main(String args[]) {
    new RoundColorTable();
  }
}

class ColorTableCellRenderer extends DefaultTableCellRenderer {

  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
    if (table.getModel().getValueAt(row, 4).toString().contains("1")) {
      setBackground(Color.BLUE);
    } else {
      setBackground(Color.WHITE);
    }
    //调用基类方法
    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
  }
}