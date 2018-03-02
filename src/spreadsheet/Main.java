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

// Hey Pani,
//something is wrong with my code (you probably already noticed), I always get a NullPointerException,
// and I think I got the logic wrong, but unfortunately, I can't find the mistake :((((
// the spec sheet was a bit too vague in my opinion, which is why I might have understood sth wrong
// or I might have implemented a method wrong cause I thought it was supposed to do sth. else