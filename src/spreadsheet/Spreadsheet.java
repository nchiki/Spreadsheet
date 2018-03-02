package spreadsheet;

import common.api.CellLocation;
import common.api.ExpressionUtils;
import common.api.Tabular;
import common.api.value.InvalidValue;
import common.api.value.LoopValue;
import common.api.value.StringValue;
import common.api.value.Value;
import common.api.value.ValueEvaluator;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import sun.awt.image.ImageWatched.Link;

public class Spreadsheet implements Tabular {

  public HashMap<CellLocation, Cell> cells;
  public Set<Cell> recompCells = new HashSet<>();
  private Set<Cell> ignoreCells = new HashSet<>();
  Deque<CellLocation> queue = new ArrayDeque<>();

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
      return cells.get(location).getExpression();
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
    Iterator<Cell> iterator = recompCells.iterator();
    while (iterator.hasNext()) {
      Cell c = iterator.next();
      recomputeCell(c);
      if (!ignoreCells.contains(c)) {
        recomputeCell(c);
      }
      iterator.remove();
    }
    ignoreCells.clear();
  }

  public boolean isIn(Cell cell) {
    return recompCells.contains(cell);
  }

  private void recomputeCell(Cell c) {
    checkLoops(c, new LinkedHashSet<>());
    if (!queue.contains(c.getLocation())) {
      return;
    }

    for(Cell ref : c.references){
      if(queue.contains(ref.getLocation())){
        queue.remove(c.getLocation());
        queue.addLast(c.getLocation());
        return;
      }
    }

    calculateCellValue(c);
  }

  private void calculateCellValue(Cell c) {
    Map<CellLocation, Double> valueMap = new HashMap<>();
    SpreadsheetValueEvaluator eval = new SpreadsheetValueEvaluator();
    for (Cell d : c.references) {
      d.getValue().evaluate(eval);
      valueMap.put(d.getLocation(), eval.getDouble());
    }
    c.setValue(ExpressionUtils.computeValue(c.getExpression(), valueMap));
     queue.remove(c.getLocation());
  }

  class SpreadsheetValueEvaluator implements ValueEvaluator {

    private double doubleRes = 0;

    @Override
    public void evaluateDouble(double value) {
      doubleRes = value;
    }

    public double getDouble() {
      return doubleRes;
    }

    @Override
    public void evaluateInvalid(String expression) {

    }

    @Override
    public void evaluateLoop() {

    }

    @Override
    public void evaluateString(String expression) {

    }
  }

  private void checkLoops(Cell c, LinkedHashSet<Cell> cellsSeen) { //is correct and finished
    if (cellsSeen.contains(c)) {
      markAsValidatedLoop(c, cellsSeen);
    } else {
      cellsSeen.add(c);
      for (Cell cell : c.references) {
        checkLoops(cell, cellsSeen);
      }
      cellsSeen.remove(c);
    }
  }

  private void markAsValidatedLoop(Cell startCell, LinkedHashSet<Cell> cells) {
    ignoreCells.addAll(cells);
    boolean traverse = false;
    Iterator<Cell> iterator = cells.iterator();
    while (iterator.hasNext()) {
      Cell cell = iterator.next();
      if (cell.equals(startCell)) {
        traverse = true;
      }
      if (traverse) {
        cell.setValue(LoopValue.INSTANCE);
      }

    }
  }
    public boolean updateCell(CellLocation location){
      if(queue.contains(location)) {
        return false;
      }
      queue.addLast(location);
      return true;
    }
  }

