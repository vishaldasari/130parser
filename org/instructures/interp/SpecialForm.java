package org.instructures.interp;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import org.instructures.interp.values.*;
import org.instructures.interp.values.LexemeDatum.SymbolDatum;

class SpecialForm {
  private static final Map<SymbolDatum, SpecialForm> SPECIAL_FORMS = new ConcurrentHashMap<>();

  private BiFunction<LinkedList<Value>, Environment, Value> handler;

  private SpecialForm(BiFunction<LinkedList<Value>, Environment, Value> handler) {
    this.handler = handler;
  }

  public static void define(String symbolName, BiFunction<LinkedList<Value>, Environment, Value> handler) {
    SPECIAL_FORMS.put(LexemeDatum.newSymbol(symbolName), new SpecialForm(handler));
  }
  
  public static SpecialForm lookup(String symbolName) {
    return SPECIAL_FORMS.get(LexemeDatum.newSymbol(symbolName));
  }

  public Value evaluate(Datum sExpr, Environment environment) {
    LinkedList<Value> list = sExpr.asProperList();
    return handler.apply(list, environment);
  }
}