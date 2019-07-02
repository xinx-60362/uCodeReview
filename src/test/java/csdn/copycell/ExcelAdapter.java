package test.csdn.copycell;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.StringTokenizer;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;


/**
 * ExcelAdapter enables Copy-Paste Clipboard functionality on JTables. The clipboard data format
 * used by the adapter is compatible with the clipboard format used by Excel. This provides for
 * clipboard interoperability between enabled JTables and Excel.
 */
public class ExcelAdapter implements ActionListener {


  private String rowstring, value;
  private Clipboard clipboard;
  private StringSelection stsel;
  private JTable jTable1;


  /**
   * The Excel Adapter is constructed with a JTable on which it enables Copy-Paste and acts as a
   * Clipboard listener.
   */
  public ExcelAdapter(JTable myJTable) {

    jTable1 = myJTable;
    final KeyStroke copy = KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK, false);
    // Identifying the copy KeyStroke user can modify this
    // to copy on some other Key combination.
    final KeyStroke paste = KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK, false);
    // Identifying the Paste KeyStroke user can modify this
    //to copy on some other Key combination.
    jTable1.registerKeyboardAction(this, "Copy", copy, JComponent.WHEN_FOCUSED);
    jTable1.registerKeyboardAction(this, "Paste", paste, JComponent.WHEN_FOCUSED);
    clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
  }


  /**
   * Public Accessor methods for the Table on which this adapter acts.
   */
  public JTable getJTable() {

    return jTable1;
  }


  public void setJTable(JTable jTable1) {

    this.jTable1 = jTable1;
  }


  /**
   * This method is activated on the Keystrokes we are listening to in this implementation. Here it
   * listens for Copy and Paste ActionCommands. Selections comprising non-adjacent cells result in
   * invalid selection and then copy action cannot be performed. Paste is done by aligning the upper
   * left corner of the selection with the 1st element in the current selection of the JTable.
   */
  @Override
  public void actionPerformed(ActionEvent e) {

    final String actionCommand = e.getActionCommand();

    if (actionCommand.equals("Copy")) {

      StringBuilder sbf = new StringBuilder();
      // Check to ensure we have selected only a contiguous block of cells.
      final int numcols = jTable1.getSelectedColumnCount();
      final int numrows = jTable1.getSelectedRowCount();
      final int[] rowsselected = jTable1.getSelectedRows();
      final int[] colsselected = jTable1.getSelectedColumns();

      if (!((numrows - 1 == rowsselected[rowsselected.length - 1] - rowsselected[0] &&
          numrows == rowsselected.length) &&
          (numcols - 1 == colsselected[colsselected.length - 1] - colsselected[0] &&
              numcols == colsselected.length))) {
        JOptionPane.showMessageDialog(null, "Invalid Copy Selection",
            "Invalid Copy Selection",
            JOptionPane.ERROR_MESSAGE);
        return;
      }
      for (int i = 0; i < numrows; i++) {
        for (int j = 0; j < numcols; j++) {
          sbf.append(jTable1.getValueAt(rowsselected[i], colsselected[j]));
          if (j < numcols - 1) {
            sbf.append('\t');
          }
        }
        sbf.append('\n');
      }
      stsel = new StringSelection(sbf.toString());
      clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      clipboard.setContents(stsel, stsel);

    } else if (actionCommand.equals("Paste")) {

      System.out.println("Trying to Paste");
      final int startRow = (jTable1.getSelectedRows())[0];
      final int startCol = (jTable1.getSelectedColumns())[0];
      try {
        final String trString = (String) (clipboard.getContents(this)
            .getTransferData(DataFlavor.stringFlavor));
        System.out.println("String is:" + trString);
        final StringTokenizer st1 = new StringTokenizer(trString, "\n");
        for (int i = 0; st1.hasMoreTokens(); i++) {
          rowstring = st1.nextToken();
          StringTokenizer st2 = new StringTokenizer(rowstring, "\t");
          for (int j = 0; st2.hasMoreTokens(); j++) {
            value = (String) st2.nextToken();
            if (startRow + i < jTable1.getRowCount() &&
                startCol + j < jTable1.getColumnCount()) {
              jTable1.setValueAt(value, startRow + i, startCol + j);
            }
            System.out.println(
                "Putting " + value + "at row = " + startRow + i + " column = " + startCol + j);
          }
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }

    }

  }

}