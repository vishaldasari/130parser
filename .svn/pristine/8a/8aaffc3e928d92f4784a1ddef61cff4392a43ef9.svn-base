package org.instructures.interp;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.instructures.interp.values.Value;

public abstract class Environment {
  /**
   * Creates an empty environment where new bindings can be added to it.
   */
  public static Environment newEmptyEnvironment() {
    return new NullEnvironment().extend(Collections.emptyList(), Collections.emptyList());
  }

  public abstract void defineVariable(String variable, Value value);

  public abstract Value lookupVariable(String variable);

  public abstract void setVariable(String variable, Value newValue);

  /**
   * Creates a new environment by extending the given one. Any bindings not
   * found in the extended environment will be checked in the base environment.
   */
  public Environment extend(List<String> varNames, List<Value> values) {
    Environment extended = new ExtendedEnvironment(this);
    if (varNames.size() != values.size()) {
      throw Problem.internalError("binding names=%d but binding values=%d!", varNames.size(), values.size());
    }
    for (int i = 0; i < varNames.size(); ++i) {
      extended.defineVariable(varNames.get(i), values.get(i));
    }
    return extended;
  }

  private static class NullEnvironment extends Environment {
    @Override
    public void defineVariable(String variable, Value value) {
      throw Problem.internalError("Attempted to set a value in the null environment");
    }

    @Override
    public Value lookupVariable(String variable) {
      throw Problem.unboundVariable(variable);
    }

    @Override
    public void setVariable(String variable, Value newValue) {
      throw Problem.unboundVariable(variable);
    }
  }

  private static class ExtendedEnvironment extends Environment {
    private final Map<String, Value> frame = new HashMap<>();
    private final Environment baseEnvironment;

    private ExtendedEnvironment(Environment baseEnvironment) {
      this.baseEnvironment = baseEnvironment;
    }

    @Override
    public void defineVariable(String variable, Value value) {
      frame.put(variable, value);
    }

    @Override
    public Value lookupVariable(String variable) {
      if (frame.containsKey(variable)) {
        return frame.get(variable);
      }
      return baseEnvironment.lookupVariable(variable);
    }

    @Override
    public void setVariable(String variable, Value newValue) {
      if (frame.containsKey(variable)) {
        frame.put(variable, newValue);
      } else {
        baseEnvironment.setVariable(variable, newValue);
      }
    }
  }
}
