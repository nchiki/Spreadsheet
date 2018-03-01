package spreadsheet;

import common.api.CellLocation;
import common.api.Tabular;
import common.api.value.StringValue;
import common.api.value.Value;
import common.api.value.ValueEvaluator;
import java.util.HashMap;
import java.util.HashSet;
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
    if(cells.containsKey(location)) {
      return cells.get(location).getValue();
    } else {
      return null;
    }
  }

  @Override
  public void recompute(){
  while(!recompCells.isEmpty()){  //no for loop cause length will change due to removal of cells
    Cell cell = recompCells.get(0);
    cell.setValue(new StringValue(cell.getExpression()));
    recompCells.remove(cell);
   }
 }
}
