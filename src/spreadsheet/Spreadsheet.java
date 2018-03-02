package spreadsheet;

import common.api.CellLocation;
import common.api.Tabular;
import common.api.value.InvalidValue;
import common.api.value.LoopValue;
import common.api.value.StringValue;
import common.api.value.Value;
import common.api.value.ValueEvaluator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Spreadsheet implements Tabular {

  public HashMap<CellLocation, Cell> cells;
  List<Cell> recompCells;

  @Override
  public void setExpression(CellLocation location, String expression) {
    if (cells.containsKey(location)) {
      cells.get(location).setExpression(expression);
      cells.get(location).setValue(new StringValue(expression));
    } else {
      cells.put(location, new Cell(location, this));
      cells.get(location).setExpression(expression);
      cells.get(location).setValue(new StringValue(expression));
    }
  }

  @Override
  public String getExpression(CellLocation location) {
    if (cells.containsKey(location)) {
      return cells.get(location).getExpression().toString();
    } else {
      return "";
    }
  }

  @Override
  public Value getValue(CellLocation location) {
    if (cells.containsKey(location)) {
      return cells.get(location).getValue();
    } else {
      return null;
    }
  }

  @Override
  public void recompute() {
    while (!recompCells.isEmpty()) {  //no for loop cause length will change due to removal of cells
      Cell cell = recompCells.get(0);
      cell.setValue(new StringValue(cell.getExpression()));
      recompCells.remove(cell);
      //include call to recomputeCell
    }
  }

  private void recomputeCell(Cell c) {
    //call to checkLoops to check if there are any loops inside
  }

  private void checkLoops(Cell c, LinkedHashSet<Cell> cellsSeen) {
    if (cellsSeen.contains(c)) {
      //call to markAsValidatedLoop
    }
  }

  //markAsValidatedLoop has to run recursively on the cells the startCell depends on

































/* private void markAsValidatedLoop(Cell startCell, LinkedHashSet<Cell> cells) { //noch verbessern
   Set<Cell> beforeStart = new HashSet<>();
   Set<Cell> afterStart = new HashSet<>();
   for (Cell c : cells) {
     if (c == startCell) {
          break;
     } else {
       beforeStart.add(c);
       c.setValue(new InvalidValue(c.getExpression()));
       cells.remove(c);
     }
   }
   for (Cell c : cells) {
     // c.setValue(new LoopValue()); //restlichen Elemente in cells sind alle ab startcell
     afterStart.add(c);
   }
   cells.clear();
   for(Cell c : beforeStart){
     cells.add(c);
   }
   for(Cell c : afterStart){
     cells.add(c);
   }

 }
*/
}