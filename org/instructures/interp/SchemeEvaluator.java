package org.instructures.interp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.instructures.interp.Problem.EvaluationError;
import org.instructures.interp.values.*;
import org.instructures.interp.values.CompoundDatum.Pair;
import org.instructures.interp.values.LexemeDatum.SymbolDatum;

public class SchemeEvaluator {
  private final Environment environment;

  public SchemeEvaluator() {
    this.environment = Primitives.newGlobalEnvironment();
  }

  /**
   * Driver for testing the evaluator. All of the files input for evaluation are
   * executed in the same environment, as if they were all concatenated together
   * and evaluated as a single file.
   */
  public static void main(String[] args) {
    SchemeEvaluator evaluator = new SchemeEvaluator();
    for (String filename: args) {
      try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
        DatumParser parser = new DatumParser(in);
        for (;;) {
          try {
            Datum datum = parser.nextDatum();
            if (datum == null) {
              break;
            }
            Value value = evaluator.evaluate(datum);
            if (value.isSpecified()) {
              System.out.printf("%s%n", value.toSyntaxString());
            }
          } catch (EvaluationError e) {
            System.err.printf("%s:%s%n", filename, e.getMessage());
          }
        }
      } catch (Exception e) {
        System.err.printf("%s:%s%n", filename, e.getMessage());
      }
    }
  }

  /**
   * Evaluates the S-Expression specified by the given datum. If the expression
   * does not have a defined value then {@code null} is returned. In the event
   * of a syntax or type error, a Problem will be thrown.
   */
  public Value evaluate(Datum sExpr) throws Problem {
    return evaluate(sExpr, environment);
  }

  private static Value evaluate(Datum sExpr, Environment environment) {
    // YOUR CODE HERE //
    // EXAMPLE FROM CLASS:
        if (sExpr.isSymbol()) {
          return environment.lookupVariable(sExpr.toString());
        } else if (sExpr.isEmptyList()) {
          throw Problem.invalidExpression("Application operator must have a procedure");
        } else if (sExpr.isPair()) {
          return evaluateNonEmptyListForm(sExpr, environment);
        } else {
          // then just assume it's self-evaluating
          return sExpr;
        }
  }

  static{
    SpecialForm.define("quote", SchemeEvaluator::evaluateQuote);
    SpecialForm.define("set!", SchemeEvaluator::evaluateAssignment);
    SpecialForm.define("define", SchemeEvaluator::evaluateDefine);
    SpecialForm.define("let", SchemeEvaluator::evaluateLet);
    SpecialForm.define("lambda", SchemeEvaluator::evaluateLambda);
    SpecialForm.define("if", SchemeEvaluator::evaluateIf);
    SpecialForm.define("cond", SchemeEvaluator::evaluateCond);
    SpecialForm.define("begin", SchemeEvaluator::evaluateBegin);
  }

  private static Value evaluateQuote(LinkedList<Value> body, Environment environment){
    matchSymbol(body, "quote");
    Value textOfQuote = next(body);
    noExtras(body, "quote");
    return textOfQuote;
  }

  private static Value evaluateAssignment(LinkedList<Value> body, Environment environment){
    matchSymbol(body, "set!");
    Value assignee = next(body);
    Datum exp = (Datum)next(body);
    Value newValue = evaluate(exp, environment);
    noExtras(body, "set!");
    environment.setVariable(assignee.toString(), newValue);
    return RuntimeValue.newUnspecified();
  }

  private static Value evaluateDefine(LinkedList<Value> body, Environment environment){
    matchSymbol(body, "define");
    Value assignee = next(body);
    Datum exp = (Datum)next(body);
    Value newValue = evaluate(exp, environment);
    noExtras(body, "define");
    environment.defineVariable(assignee.toString(), newValue);
    return RuntimeValue.newUnspecified();
  }

  private static Value evaluateLet(LinkedList<Value> body, Environment environment){
    matchSymbol(body, "let");
    noExtras(body, "let");
    return null;
  }

  private static Value evaluateLambda(LinkedList<Value> body, Environment environment){
    matchSymbol(body, "lambda");
    /*
    List<Value> params = next(body);
    List<String> strParams = new ArrayList<String>();
    for( Value v : params){
        strParams.add(v.toString());
    }
    List<Datum> bodyExp = (List<Datum>)next(body);
    
    Lambda newLambda = new Lambda(strParams, false, bodyExp, environment);
    return newLambda;
    */
    return null;
  }
  private static Value evaluateIf(LinkedList<Value> body, Environment environment){
    matchSymbol(body, "if");
    Datum predicate = (Datum)next(body);
    Datum consequent = (Datum)next(body);
    Datum alternative = (Datum)next(body);
    boolean evaled = helperEvalIf(predicate, environment);

    if(evaled)
        return evaluate(consequent, environment);
    else{
        if(alternative.isSpecified())
            return evaluate(alternative, environment);
        else
            return RuntimeValue.newUnspecified();
    }
  }

  private static boolean helperEvalIf(Datum predicate, Environment environment){
      return evaluate(predicate, environment).isTrue();
  }


  private static Value evaluateCond(LinkedList<Value> body, Environment environment){
    matchSymbol(body, "cond");
    for( Value v : body ){
        Datum predicate = (Datum)v.getCar();
        Datum consequent = (Datum)v.getCdr();

        if( predicate.toString().equals("else") || helperEvalIf(predicate, environment) ){
            for( ; !consequent.getCdr().isEmptyList() ; consequent = (Datum)consequent.getCdr() ){
                evaluate((Datum)consequent.getCar(), environment);
            }
            return evaluate((Datum)consequent.getCar(),  environment);
        }
    }

    return RuntimeValue.newUnspecified();
  }
  private static Value evaluateBegin(LinkedList<Value> body, Environment environment){
    matchSymbol(body, "begin");
    Value last = RuntimeValue.newUnspecified();
    for( Value v : body ){
        last = evaluate((Datum)v, environment);
    }
    return last;
  }

  private static Value evaluateNonEmptyListForm(Datum sExpr, Environment environment){
    Value listHead = sExpr.getCar();
    if(listHead.isSymbol()){
        SpecialForm specialForm = SpecialForm.lookup(listHead.toString());
        if(specialForm != null){
            return specialForm.evaluate(sExpr, environment);
        }
    }

    return evaluateApplication(sExpr, environment);
  }

  private static Value evaluateApplication(Datum sExpr, Environment environment){
      // Assuming that the listHead is always a symbol...
      Value theProcedure = environment.lookupVariable(sExpr.getCar().toString());
      List<Value> theRest = sExpr.getCdr().asProperList();
      List<Value> finished = new ArrayList<Value>();

      for( Value element : theRest ){
          Datum asDatum = (Datum)element;
          finished.add(evaluate(asDatum, environment));
      }

      return theProcedure.apply(finished);

      //return environment.lookupVariable(sExpr.getCar().toString()).apply(sExpr.getCdr().asProperList());
  }

  // YOUR CODE HERE //
  // EXAMPLE CODE:
    private static class Lambda extends RuntimeValue.Procedure {
      private List<String> formals;
      private List<Datum> body;
      private Environment scope;
  
      public Lambda(List<String> formals, boolean isVarArg, List<Datum> body, Environment scope) {
        super(formals.size() - (isVarArg ? 1 : 0), isVarArg);
        this.formals = formals;
        this.body = body;
        this.scope = scope;
      }
  
      @Override
      public Value checkedApply(LinkedList<Value> args) {
        Environment extendedEnvironment = scope.extend(formals, args);
         return evaluateSequence(body, extendedEnvironment);
       }
    }

  private static Value evaluateSequence(List<Datum> body, Environment extended){
    Value last = RuntimeValue.newUnspecified();
    for( Datum d : body ){
        last = evaluate(d, extended);
    }
    return last;
  }

  // Code from Prof Mac.
  private static void matchSymbol(LinkedList<Value> queue, String symbolName){
      if (queue.isEmpty()) 
          throw Problem.invalidExpression("Incomplete expression");
      
      assertCondition(testSymbol(queue.remove(), symbolName), "Expected %s", symbolName);
  }

  // Code from Prof Mac.
  private static boolean testSymbol(Value value, String symbolName) {
      SymbolDatum expected = LexemeDatum.newSymbol(symbolName);
      return (value == expected);
  }

  // Code kinda from Prof Mac.
  private static Value next(LinkedList<Value> queue){
      if (queue.isEmpty()) {
          throw Problem.invalidExpression("Incomplete expression");
      }
      return queue.remove();
  }

  // Code from Prof Mac.
  private static void noExtras(LinkedList<Value> queue, String context){
      assertCondition(queue.isEmpty(), "Extra expression in %s", context);
  }

  private static void assertCondition(boolean condition, String fmt, Object... args) {
    if (!condition) {
      throw Problem.invalidExpression(fmt, args);
    }
  }
}
