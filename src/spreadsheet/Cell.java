package spreadsheet;

import common.api.CellLocation;
import common.api.value.StringValue;
import common.api.value.Value;

public class Cell {
  private Value value;
  private StringValue expression;
  private Spreadsheet spreadsheet;
  private CellLocation location;

  public Cell(CellLocation location, Spreadsheet s){
    this.value = null;
    expression = new StringValue("");
    this.location = location;
    this.spreadsheet = s;
  }

  void setValue(Value givenVal){
   this.value = givenVal;
  }

  Value getValue(){
    return this.value;
  }

  void setExpression(String expr){
    this.expression = new StringValue(expr);
  }

  StringValue getExpression(){
    return this.expression;
  }

}
