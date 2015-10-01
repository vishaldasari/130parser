package org.instructures.interp.values;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.instructures.interp.LexicalUtils;

public abstract class LexemeDatum implements Datum {
  private static final Map<String, SymbolDatum> symbolPool = new ConcurrentHashMap<>();
  private static final Map<Boolean, BooleanDatum> booleanLiterals = new ConcurrentHashMap<>();
  private static final Map<String, CharacterDatum> characterPool = new ConcurrentHashMap<>();
  private static final Map<String, StringDatum> stringPool = new ConcurrentHashMap<>();
  private static final Map<BigInteger, NumberDatum> numberPool = new ConcurrentHashMap<>();

  public static SymbolDatum newSymbol(String symbolName) {
    return symbolPool.computeIfAbsent(symbolName, k -> new SymbolDatum(k));
  }

  public static BooleanDatum newBoolean(boolean value) {
    return booleanLiterals.computeIfAbsent(value, b -> new BooleanDatum(b));
  }

  public static CharacterDatum newCharacter(char character) {
    return newCharacter(String.valueOf(character));
  }

  public static CharacterDatum newCharacter(String value) {
    return characterPool.computeIfAbsent(value, k -> new CharacterDatum(k));
  }

  public static StringDatum newString(String content) {
    return stringPool.computeIfAbsent(content, k -> new StringDatum(k));
  }

  public static NumberDatum newNumber(String lexeme) {
    if (lexeme.startsWith("+")) {
      lexeme = lexeme.substring(1);
    }
    return newNumber(new BigInteger(lexeme));
  }

  public static NumberDatum newNumber(BigInteger value) {
    return numberPool.computeIfAbsent(value, v -> new NumberDatum(v));
  }

  public static class SymbolDatum extends LexemeDatum {
    private String symbolName;

    private SymbolDatum(String symbolName) {
      this.symbolName = symbolName;
    }

    @Override
    public String toString() {
      return symbolName;
    }

    @Override
    public boolean isSymbol() {
      return true;
    }
  }

  public static class BooleanDatum extends LexemeDatum {
    private boolean value;

    private BooleanDatum(boolean value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return (value) ? "#t" : "#f";
    }

    @Override
    public boolean isBoolean() {
      return true;
    }

    @Override
    public boolean isTrue() {
      return value;
    }
  }

  public static class CharacterDatum extends LexemeDatum {
    private String value;

    private CharacterDatum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return value;
    }

    @Override
    public String toSyntaxString() {
      return LexicalUtils.unparseCharacterValue(value);
    }

    @Override
    public boolean isCharacter() {
      return true;
    }
  }

  public static class StringDatum extends LexemeDatum {
    private String content;

    private StringDatum(String content) {
      this.content = content;
    }

    @Override
    public String toString() {
      return content;
    }

    @Override
    public String toSyntaxString() {
      return LexicalUtils.unparseStringValue(content);
    }

    @Override
    public boolean isString() {
      return true;
    }
  }

  public static class NumberDatum extends LexemeDatum {
    private BigInteger value;

    private NumberDatum(BigInteger value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return value.toString();
    }

    @Override
    public boolean isNumber() {
      return true;
    }

    public BigInteger getValue() {
      return value;
    }
  }
}
