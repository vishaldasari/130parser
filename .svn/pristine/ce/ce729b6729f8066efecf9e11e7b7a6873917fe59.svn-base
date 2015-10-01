package org.instructures.interp;

@SuppressWarnings("serial")
public abstract class Problem extends RuntimeException {
  protected Problem(String fmt, Object... args) {
    super(String.format(fmt, args));
  }

  /**
   * To be thrown when an error is due to a bug in the interpreter.
   */
  public static InternalError internalError(String fmt, Object... args) {
    return new InternalError("Internal error: " + fmt, args);
  }

  /**
   * To be thrown when the characters from the input cannot form a valid token.
   */
  public static EvaluationError invalidToken(Lexeme badToken) {
    return new SyntaxError("%d: %s", badToken.getLineNumber(), badToken.getMessage());
  }

  /**
   * To be thrown when an expression is not well-formed for its context.
   */
  public static EvaluationError invalidExpression(String fmt, Object... args) {
    return new SyntaxError(fmt, args);
  }

  /**
   * To be thrown when the current token was not expected.
   */
  public static EvaluationError noMatch(Lexeme errorLocation, String fmt, Object... args) {
    return new SyntaxError("%d: %s", errorLocation.getLineNumber(), String.format(fmt, args));
  }

  /**
   * To be thrown when an operation cannot be applied to an object.
   */
  public static EvaluationError typeError(String fmt, Object... args) {
    return new TypeError(fmt, args);
  }

  /**
   * To be thrown when an operand's value is invalid for the operation.
   */
  public static EvaluationError argumentError(String fmt, Object... args) {
    return new ArgumentError(fmt, args);
  }

  /**
   * To be thrown when a variable is referenced and it's not in scope.
   */
  public static EvaluationError unboundVariable(String variableName) {
    return new ReferenceError("%s is not defined", variableName);
  }

  public static final class InternalError extends Problem {
    private InternalError(String fmt, Object... args) {
      super(fmt, args);
    }
  }

  public abstract static class EvaluationError extends Problem {
    private EvaluationError(String fmt, Object... args) {
      super(fmt, args);
    }
  }

  private static final class SyntaxError extends EvaluationError {
    private SyntaxError(String fmt, Object... args) {
      super(fmt, args);
    }
  }

  private static final class TypeError extends EvaluationError {
    private TypeError(String fmt, Object... args) {
      super(fmt, args);
    }
  }

  private static final class ReferenceError extends EvaluationError {
    private ReferenceError(String fmt, Object... args) {
      super(fmt, args);
    }
  }

  private static final class ArgumentError extends EvaluationError {
    private ArgumentError(String fmt, Object... args) {
      super(fmt, args);
    }
  }
}
