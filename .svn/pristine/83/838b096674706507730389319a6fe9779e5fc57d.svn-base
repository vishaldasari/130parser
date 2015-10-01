package org.instructures.interp;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import org.instructures.interp.values.*;
import org.instructures.interp.values.LexemeDatum.CharacterDatum;
import org.instructures.interp.values.LexemeDatum.NumberDatum;
import org.instructures.interp.values.RuntimeValue.Procedure;

public class Primitives {
  public static Environment newGlobalEnvironment() {
    Environment ge = Environment.newEmptyEnvironment();
    
    // apply
    add(ge, "apply", new Procedure(2, false) {
      @Override
      protected Value checkedApply(LinkedList<Value> args) {
        Procedure proc = checkArgType(Procedure.class, args.removeFirst());
        CompoundDatum requiredList = checkArgType(CompoundDatum.class, args.removeLast());
        LinkedList<Value> flattenedArgs = new LinkedList<>();
        flattenedArgs.addAll(args);
        flattenedArgs.addAll(requiredList.asProperList());
        Value result = proc.apply(flattenedArgs);
        return result;
      }
    });

    // predicates
    add(ge, "boolean?", newUnaryOp(Value.class, obj -> LexemeDatum.newBoolean(obj.isBoolean())));
    add(ge, "char?", newUnaryOp(Value.class, obj -> LexemeDatum.newBoolean(obj.isCharacter())));
    add(ge, "list?", newUnaryOp(Value.class, obj -> LexemeDatum.newBoolean(obj.isList())));
    add(ge, "null?", newUnaryOp(Value.class, obj -> LexemeDatum.newBoolean(obj.isEmptyList())));
    add(ge, "number?", newUnaryOp(Value.class, obj -> LexemeDatum.newBoolean(obj.isNumber())));
    add(ge, "pair?", newUnaryOp(Value.class, obj -> LexemeDatum.newBoolean(obj.isPair())));
    add(ge, "procedure?", newUnaryOp(Value.class, obj -> LexemeDatum.newBoolean(obj.isProcedure())));
    add(ge, "string?", newUnaryOp(Value.class, obj -> LexemeDatum.newBoolean(obj.isString())));
    add(ge, "symbol?", newUnaryOp(Value.class, obj -> LexemeDatum.newBoolean(obj.isSymbol())));

    // output functions
    add(ge, "newline", newNullaryOp(() -> {
      System.out.println();
      return RuntimeValue.newUnspecified();
    }));
    add(ge, "display", newUnaryOp(Value.class, obj -> {
      System.out.printf("%s", obj.toString());
      return RuntimeValue.newUnspecified();
    }));

    // unary logical functions
    add(ge, "not", newUnaryOp(Value.class, obj -> LexemeDatum.newBoolean(!obj.isTrue())));

    // list functions
    add(ge, "cons", newBinaryOp(Value.class, Value.class, (car, cdr) -> {
      return CompoundDatum.newPair(car, cdr);
    }));
    add(ge, "car", newUnaryOp(CompoundDatum.Pair.class, pair -> pair.getCar()));
    add(ge, "cdr", newUnaryOp(CompoundDatum.Pair.class, pair -> pair.getCdr()));
    
    // string functions
    add(ge, "string", newVarArgsOp(0, LexemeDatum.CharacterDatum.class, charsList -> {
      StringBuilder buff = new StringBuilder();
      for (CharacterDatum cval: charsList) {
        buff.append(cval.toString());
      }
      return LexemeDatum.newString(buff.toString());
    }));
    add(ge, "string-length", newUnaryOp(LexemeDatum.StringDatum.class, str -> {
      int strLength = str.toString().length();
      return LexemeDatum.newNumber(BigInteger.valueOf(strLength));
    }));
    add(ge, "string-ref", newBinaryOp(LexemeDatum.StringDatum.class, LexemeDatum.NumberDatum.class, (str, ref) -> {
      String string = str.toString();
      int index = ref.getValue().intValue();
      return LexemeDatum.newCharacter(string.charAt(index));
    }));

    // conversion functions
    add(ge, "list->string", newUnaryOp(CompoundDatum.class, lst -> {
      LinkedList<CharacterDatum> charsList = checkArgs(LexemeDatum.CharacterDatum.class, lst.asProperList());
      StringBuilder buff = new StringBuilder();
      for (CharacterDatum cval: charsList) {
        buff.append(cval.toString());
      }
      return LexemeDatum.newString(buff.toString());
    }));
    add(ge, "number->string", newUnaryOp(LexemeDatum.NumberDatum.class, num -> {
      return LexemeDatum.newString(num.getValue().toString());
    }));
    add(ge, "string->list", newUnaryOp(LexemeDatum.StringDatum.class, str -> {
      String chars = str.toString();
      LinkedList<CharacterDatum> charsList = new LinkedList<>();
      for (int i = 0; i < chars.length(); ++i) {
        charsList.add(LexemeDatum.newCharacter(chars.charAt(i)));
      }
      return CompoundDatum.newList(charsList);
    }));
    add(ge, "string->symbol", newUnaryOp(LexemeDatum.StringDatum.class, str -> {
      return LexemeDatum.newSymbol(str.toString());
    }));
    add(ge, "symbol->string", newUnaryOp(LexemeDatum.SymbolDatum.class, sym -> {
      return LexemeDatum.newString(sym.toString());
    }));

    // unary numeric functions
    final BigInteger TWO = BigInteger.ONE.add(BigInteger.ONE);
    add(ge, "abs", newUnaryOp(LexemeDatum.NumberDatum.class, num -> {
      return LexemeDatum.newNumber(num.getValue().abs());
    }));
    add(ge, "even?", newUnaryOp(LexemeDatum.NumberDatum.class, num -> {
      return LexemeDatum.newBoolean(num.getValue().mod(TWO).equals(BigInteger.ZERO));
    }));
    add(ge, "odd?", newUnaryOp(LexemeDatum.NumberDatum.class, num -> {
      return LexemeDatum.newBoolean(!num.getValue().mod(TWO).equals(BigInteger.ZERO));
    }));
    add(ge, "positive?", newUnaryOp(LexemeDatum.NumberDatum.class, num -> {
      return LexemeDatum.newBoolean(num.getValue().compareTo(BigInteger.ZERO) > 0);
    }));
    add(ge, "negative?", newUnaryOp(LexemeDatum.NumberDatum.class, num -> {
      return LexemeDatum.newBoolean(num.getValue().compareTo(BigInteger.ZERO) < 0);
    }));
    add(ge, "zero?", newUnaryOp(LexemeDatum.NumberDatum.class, num -> {
      return LexemeDatum.newBoolean(num.getValue().compareTo(BigInteger.ZERO) == 0);
    }));

    // vararg numeric functions
    add(ge, "*", newVarArgsOp(0, LexemeDatum.NumberDatum.class, numList -> {
      BigInteger product = BigInteger.ONE;
      for (NumberDatum number: numList) {
        product = product.multiply(number.getValue());
      }
      return LexemeDatum.newNumber(product);
    }));
    add(ge, "+", newVarArgsOp(0, LexemeDatum.NumberDatum.class, numList -> {
      BigInteger sum = BigInteger.ZERO;
      for (NumberDatum number: numList) {
        sum = sum.add(number.getValue());
      }
      return LexemeDatum.newNumber(sum);
    }));
    add(ge, "max", newVarArgsOp(1, LexemeDatum.NumberDatum.class, numList -> {
      BigInteger max = numList.removeFirst().getValue();
      for (NumberDatum number: numList) {
        max = max.max(number.getValue());
      }
      return LexemeDatum.newNumber(max);
    }));
    add(ge, "min", newVarArgsOp(1, LexemeDatum.NumberDatum.class, numList -> {
      BigInteger min = numList.removeFirst().getValue();
      for (NumberDatum number: numList) {
        min = min.min(number.getValue());
      }
      return LexemeDatum.newNumber(min);
    }));
    add(ge, "-", newVarArgsOp(1, LexemeDatum.NumberDatum.class, numList -> {
      BigInteger result = numList.removeFirst().getValue();
      if (numList.isEmpty()) {
        result = result.negate();
      } else {
        for (NumberDatum number: numList) {
          result = result.subtract(number.getValue());
        }
      }
      return LexemeDatum.newNumber(result);
    }));
    add(ge, "/", newVarArgsOp(1, LexemeDatum.NumberDatum.class, numList -> {
      BigInteger result = numList.removeFirst().getValue();
      if (numList.isEmpty()) {
        if (result.equals(BigInteger.ZERO)) {
          throw Problem.argumentError("Division by zero");
        }
        // With just integers to work with, reciprocal truncates to zero
      result = BigInteger.ZERO;
    } else {
      for (NumberDatum number: numList) {
        if (number.equals(BigInteger.ZERO)) {
          throw Problem.argumentError("Division by zero");
        }
        result = result.divide(number.getValue());
      }
    }
    return LexemeDatum.newNumber(result);
  } ));
    add(ge, "<", newVarArgsOp(2, LexemeDatum.NumberDatum.class, numList -> {
      BigInteger last = numList.removeFirst().getValue();
      while (!numList.isEmpty()) {
        BigInteger next = numList.removeFirst().getValue();
        if (last.compareTo(next) < 0) {
          last = next;
        } else {
          return LexemeDatum.newBoolean(false);
        }
      }
      return LexemeDatum.newBoolean(true);
    }));
    add(ge, "<=", newVarArgsOp(2, LexemeDatum.NumberDatum.class, numList -> {
      BigInteger last = numList.removeFirst().getValue();
      while (!numList.isEmpty()) {
        BigInteger next = numList.removeFirst().getValue();
        if (last.compareTo(next) <= 0) {
          last = next;
        } else {
          return LexemeDatum.newBoolean(false);
        }
      }
      return LexemeDatum.newBoolean(true);
    }));
    add(ge, ">", newVarArgsOp(2, LexemeDatum.NumberDatum.class, numList -> {
      BigInteger last = numList.removeFirst().getValue();
      while (!numList.isEmpty()) {
        BigInteger next = numList.removeFirst().getValue();
        if (last.compareTo(next) > 0) {
          last = next;
        } else {
          return LexemeDatum.newBoolean(false);
        }
      }
      return LexemeDatum.newBoolean(true);
    }));
    add(ge, ">=", newVarArgsOp(2, LexemeDatum.NumberDatum.class, numList -> {
      BigInteger last = numList.removeFirst().getValue();
      while (!numList.isEmpty()) {
        BigInteger next = numList.removeFirst().getValue();
        if (last.compareTo(next) >= 0) {
          last = next;
        } else {
          return LexemeDatum.newBoolean(false);
        }
      }
      return LexemeDatum.newBoolean(true);
    }));
    add(ge, "=", newVarArgsOp(2, LexemeDatum.NumberDatum.class, numList -> {
      BigInteger last = numList.removeFirst().getValue();
      while (!numList.isEmpty()) {
        BigInteger next = numList.removeFirst().getValue();
        if (last.compareTo(next) == 0) {
          last = next;
        } else {
          return LexemeDatum.newBoolean(false);
        }
      }
      return LexemeDatum.newBoolean(true);
    }));

    return ge;
  }

  private static void add(Environment env, String name, Procedure proc) {
    env.defineVariable(name, proc);
  }

  private static Procedure newNullaryOp(Supplier<Value> fn) {
    return new Procedure(0, false) {
      @Override
      protected Value checkedApply(LinkedList<Value> args) {
        return fn.get();
      }
    };
  }

  private static <A extends Value> Procedure newUnaryOp(Class<A> paramType, Function<A, Value> fn) {
    return new Procedure(1, false) {
      @Override
      protected Value checkedApply(LinkedList<Value> args) {
        A arg = checkArgType(paramType, args.removeFirst());
        return fn.apply(arg);
      }
    };
  }

  /**
   * Create a new variable-argument length procedure with the given number of
   * minimum required arguments. All arguments are passed in as a single Java
   * List as if there were no named required parameters.
   */
  private static <A extends Value> Procedure newVarArgsOp(
    int minArgs, Class<A> paramType, Function<LinkedList<A>, Value> fn) {
    return new Procedure(minArgs, true) {
      @Override
      protected Value checkedApply(LinkedList<Value> args) {
        LinkedList<A> checkedArgs = new LinkedList<>();
        for (int i = 0; i < minArgs; ++i) {
          checkedArgs.add(checkArgType(paramType, args.removeFirst()));
        }
        checkedArgs.addAll(checkArgs(paramType, args.removeFirst().asProperList()));
        return fn.apply(checkedArgs);
      }
    };
  }

  private static <A1 extends Value, A2 extends Value> Procedure newBinaryOp(
    Class<A1> param1Type, Class<A2> param2Type, BiFunction<A1, A2, Value> fn) {
    return new Procedure(2, false) {
      @Override
      protected Value checkedApply(LinkedList<Value> args) {
        A1 arg1 = checkArgType(param1Type, args.removeFirst());
        A2 arg2 = checkArgType(param2Type, args.removeFirst());
        return fn.apply(arg1, arg2);
      }
    };
  }

  private static <T extends Value> T checkArgType(Class<T> valueType, Value value) {
    if (!valueType.isInstance(value)) {
      throw Problem.typeError("Invalid argument type: %s", value);
    }
    return valueType.cast(value);
  }

  private static <T extends Value> LinkedList<T> checkArgs(Class<T> argType, List<? extends Value> argsList) {
    LinkedList<T> result = new LinkedList<>();
    for (Value arg: argsList) {
      result.add(checkArgType(argType, arg));
    }
    return result;
  }
}
