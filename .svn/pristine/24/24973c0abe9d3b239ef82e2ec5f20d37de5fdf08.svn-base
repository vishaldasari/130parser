package org.instructures.interp.values;

import java.util.LinkedList;
import java.util.List;
import org.instructures.interp.Problem;

public abstract class RuntimeValue implements Value {
  public abstract static class Procedure extends RuntimeValue {
    private final int numRequiredArgs;
    private final boolean isVarArg;

    protected Procedure(int requiredArgs, boolean isVarArg) {
      this.numRequiredArgs = requiredArgs;
      this.isVarArg = isVarArg;
    }

    @Override
    public boolean isProcedure() {
      return true;
    }

    @Override
    public Value apply(List<Value> args) {
      LinkedList<Value> argsQueue = new LinkedList<>();
      if (isVarArg) {
        if (args.size() < numRequiredArgs) {
          throw Problem.typeError("Too few arguments: %d (expected at least %d)", args.size(), numRequiredArgs);
        }
        List<Value> requiredArgs = args.subList(0, numRequiredArgs);
        List<Value> restArgs = args.subList(numRequiredArgs, args.size());
        argsQueue.addAll(requiredArgs);
        argsQueue.add(CompoundDatum.newList(restArgs));
      } else {
        if (args.size() != numRequiredArgs) {
          throw Problem.typeError("Incorrect number of arguments: %d (expected %d)", args.size(), numRequiredArgs);
        }
        argsQueue.addAll(args);
      }
      Value result = checkedApply(argsQueue);
      return result;
    }

    protected abstract Value checkedApply(LinkedList<Value> args);

    @Override
    public String toString() {
      return "<procedure>";
    }
  }

  /**
   * Unspecified is the "value" to return for when the language does not specify
   * what a result should be.
   */
  public static RuntimeValue newUnspecified() {
    return UnspecifiedInstance.INSTANCE.instance;
  }

  private static class Unspecified extends RuntimeValue {
    @Override
    public boolean isTrue() {
      return false;
    }

    @Override
    public boolean isSpecified() {
      return false;
    }

    @Override
    public String toString() {
      throw Problem.internalError("Unspecified value should never be output");
    }
  }

  private enum UnspecifiedInstance {
    INSTANCE;
    Unspecified instance;

    UnspecifiedInstance() {
      this.instance = new Unspecified();
    }
  }
}
