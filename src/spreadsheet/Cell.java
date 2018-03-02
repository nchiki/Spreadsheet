package spreadsheet;

import common.api.CellLocation;
import common.api.ExpressionUtils;
import common.api.monitor.Tracker;
import common.api.value.InvalidValue;
import common.api.value.StringValue;
import common.api.value.Value;
import java.util.HashSet;
import java.util.Set;

public class Cell implements Tracker<Cell>{

  private Value value;
  private String expression;
  private Spreadsheet spreadsheet;
  private CellLocation location;
  public Set<Cell> references;
  public Set<Tracker<Cell>> trackCell;

  public Cell(CellLocation location, Spreadsheet s) {
    references = new HashSet<>();
    this.value = null;
    expression = ""; //StringValue or just String
    this.location = location;
    this.spreadsheet = s;
    this.trackCell = new HashSet<>();
  }

  void setValue(Value givenVal) {
    this.value = givenVal;
  }

  Value getValue() {
    return this.value;
  }


  void unsubscribe(){
   for(Cell ref : this.references){
     ref.trackCell.remove(this); //removes cell from list of cells that depend on it
   }
   this.references.clear(); //forgetting references
  }

  void subscribe(){ //adds this cell to Trackerset of references
    for(Cell ref: this.references){
      ref.trackCell.add(this);
    }
  }

  CellLocation getLocation(){
    return location;
  }

  void setExpression(String expr) {   //clears references and Tracker Set after changing cell's expression
    this.expression = expr;
    for(Cell ref : this.references){
      ref.update(this);   //updates all referenced cells
    }
    this.unsubscribe();
    Set<CellLocation> refLoc = ExpressionUtils.getReferencedLocations(expr);
    for (CellLocation loc : refLoc) {
      Cell ref = this.spreadsheet.cells.get(loc);
      this.references.add(ref); // creates the Set<Cell> with all the cells that are referenced by this cell
    }
    this.value = new InvalidValue(expr);
    this.subscribe();
    if (!this.spreadsheet.recompCells.contains(this)) {
      this.spreadsheet.recompCells.add(this);//adding Cell to the Set of Cells to be recomputed
    }
    for(Tracker<Cell> trackC : this.trackCell){
      trackC.update(this);
    }
  }

  String getExpression() {
    return this.expression;
  }

  @Override
  public void update(Cell changed) {
    value = new InvalidValue(expression);
    if(spreadsheet.updateCell(location)) {
      for (Tracker<Cell> t : trackCell) {
        t.update(this);
      }
    }
  }
}
