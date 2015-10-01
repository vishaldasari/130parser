package org.instructures.interp.values;

import java.util.List;

public abstract class CompoundDatum implements Datum {
  private static final EmptyList EMPTY_LIST = new EmptyList();
  
  public static EmptyList newEmptyList() {
    return EMPTY_LIST;
  }

  public static Pair newPair(Value car, Value cdr) {
    return new Pair(car, cdr);
  }

  public static CompoundDatum newList(Value... values) {
    CompoundDatum result = newEmptyList();
    for (int i = values.length - 1; i >= 0; --i) {
      result = new Pair(values[i], result);
    }
    return result;
  }

  public static CompoundDatum newList(List<? extends Value> values) {
    return newList(values.toArray(new Value[0]));
  }

  @Override
  public boolean asImproperList(List<Value> listOut) {
    Value curr = this;
    while (curr != null && !curr.isEmptyList()) {
      if (curr.isPair()) {
        listOut.add(curr.getCar());
        curr = curr.getCdr();
      } else {
        listOut.add(curr);
        return true;
      }
    }
    return false;
  }

  public static class EmptyList extends CompoundDatum {
    private EmptyList() {
    }

    @Override
    public String toString() {
      return "()";
    }

    @Override
    public boolean isEmptyList() {
      return true;
    }
  }

  public static class Pair extends CompoundDatum {
    private Value car;
    private Value cdr;

    private Pair(Value car, Value cdr) {
      this.car = car;
      this.cdr = cdr;
    }

    @Override
    public Value getCar() {
      return car;
    }

    @Override
    public Value getCdr() {
      return cdr;
    }

    @Override
    public String toString() {
      StringBuilder buff = new StringBuilder();
      buff.append("(");
      Value curr = this;
      for (;;) {
        buff.append(curr.getCar().toSyntaxString());
        curr = curr.getCdr();
        if (!curr.isPair())
          break;
        buff.append(" ");
      }
      if (!curr.isEmptyList()) {
        buff.append(" . ");
        buff.append(curr.toSyntaxString());
      }
      buff.append(")");
      return buff.toString();
    }

    @Override
    public boolean isPair() {
      return true;
    }
  }
}
