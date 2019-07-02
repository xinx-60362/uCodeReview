package test.csdn;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

//
//
//


/**
 * 本类为JTable一个简单应用，实现了JTable的行颜色设置及表格的打印功能 本文件与StyleTable.java文件相配合使用
 */
public class PlanetTable {

  public static void main(String[] args) {
    JFrame frame = new PlanetTableFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
}

class PlanetTableFrame extends JFrame {

  private Object[][] cells = {
      {"Mercury", new Double(2440.0), new Integer(0),
          new Boolean(false), Color.yellow},
      {"Venus", new Double(60520.0), new Integer(0), new Boolean(false),
          Color.yellow},
      {"Earth", new Double(6378.0), new Integer(1), new Boolean(false),
          Color.blue},
      {"Mars", new Double(3397.0), new Integer(2), new Boolean(false),
          Color.red},
      {"Jupiter", new Double(71492.0), new Integer(16),
          new Boolean(false), Color.orange}};

  private String[] columnNames = {"Planet", "Radius", "Moons", "Gaseous",
      "Color"};
  //
  private static final int DEFAULT_WIDTH = 400;
  private static final int DEFAULT_HEIGHT = 200;

  public PlanetTableFrame() {
    // 用于控制每一行颜色的数组
    String[] color = {"H", "A", "F", "E", "W"};

    setTitle("PlanetTable");
    setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    // 定义JTable,实例成自己扩展的JTable类，并传入用于设定颜色的数组
    //final JTable table = new StyleTable(cells, columnNames, color);
    // 下面这行代码可实现相邻两行颜色交替的效果,注意与上一行的区别
    final JTable table = new StyleTable(cells, columnNames);
    add(new JScrollPane(table), BorderLayout.CENTER);

    JButton printButton = new JButton("Print");
    printButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        try {
          table.print();
        } catch (java.awt.print.PrinterException e) {
          e.printStackTrace();
        }
      }
    });
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(printButton);
    add(buttonPanel, BorderLayout.SOUTH);
  }
}