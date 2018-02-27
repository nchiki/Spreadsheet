package spreadsheet;

import common.gui.SpreadsheetGUI;

public class Main {

  private static final int DEFAULT_NUM_ROWS = 5000;
  private static final int DEFAULT_NUM_COLUMNS = 5000;

  public static void main(String[] args) {
    Spreadsheet newss = new Spreadsheet();
    if(args[0].equals(null)){
      SpreadsheetGUI newS = new SpreadsheetGUI(newss, DEFAULT_NUM_ROWS, DEFAULT_NUM_COLUMNS);
    } else {
      int row = Integer.parseInt(args[0]);
      int column = Integer.parseInt(args[1]);
      SpreadsheetGUI indS = new SpreadsheetGUI(newss, row, column);
    }

  }

}
