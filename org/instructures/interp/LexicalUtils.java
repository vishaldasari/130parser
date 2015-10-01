package org.instructures.interp;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Stream;

public class LexicalUtils {
  private static final Map<String, Boolean> BOOLEAN_LITERAL = new HashMap<>();
  private static final Map<String, String> SPECIAL_CHARACTER_LITERAL = new HashMap<>();
  private static final Map<String, String> CHARACTER_LITERAL_LOOKUP = new HashMap<>();
  private static final int MAX_CHARACTER_LITERAL_LENGTH;
  private static final Map<String, String> ESCAPED_STRING_ELEMENT = new HashMap<>();
  private static final Map<String, String> STRING_ELEMENT_LOOKUP = new HashMap<>();
  private static final HashMap<String, TokenType> PUNCTUATION = new HashMap<>();
  private static final Set<String> SPECIAL_INITIAL = new HashSet<>();
  private static final Set<String> SPECIAL_SUBSEQUENT = new HashSet<>();

  static {
    BOOLEAN_LITERAL.put("#t", Boolean.TRUE);
    BOOLEAN_LITERAL.put("#T", Boolean.TRUE);
    BOOLEAN_LITERAL.put("#f", Boolean.FALSE);
    BOOLEAN_LITERAL.put("#F", Boolean.FALSE);

    SPECIAL_CHARACTER_LITERAL.put("#\\nul", "\0");
    SPECIAL_CHARACTER_LITERAL.put("#\\tab", "\t");
    SPECIAL_CHARACTER_LITERAL.put("#\\newline", "\n");
    SPECIAL_CHARACTER_LITERAL.put("#\\return", "\r");
    SPECIAL_CHARACTER_LITERAL.put("#\\space", " ");
    // reverse lookup for character literals
    for (Entry<String, String> entry: SPECIAL_CHARACTER_LITERAL.entrySet()) {
      CHARACTER_LITERAL_LOOKUP.put(entry.getValue(), entry.getKey());
    }    
    Stream<String> charNames = SPECIAL_CHARACTER_LITERAL.keySet().stream();
    MAX_CHARACTER_LITERAL_LENGTH = charNames.mapToInt(String::length).reduce(0, Integer::max);

    ESCAPED_STRING_ELEMENT.put("\\t", "\t");
    ESCAPED_STRING_ELEMENT.put("\\n", "\n");
    ESCAPED_STRING_ELEMENT.put("\\r", "\r");
    ESCAPED_STRING_ELEMENT.put("\\\"", "\"");
    ESCAPED_STRING_ELEMENT.put("\\\\", "\\");
    // reverse lookup for string literals
    for (Entry<String, String> entry: ESCAPED_STRING_ELEMENT.entrySet()) {
      STRING_ELEMENT_LOOKUP.put(entry.getValue(), entry.getKey());
    }  

    PUNCTUATION.put("(", TokenType.LPAREN);
    PUNCTUATION.put(")", TokenType.RPAREN);
    PUNCTUATION.put("[", TokenType.LBRACK);
    PUNCTUATION.put("]", TokenType.RBRACK);
    PUNCTUATION.put("'", TokenType.SQUOTE);

    String[] specialInitials = {
      "!", "$", "%", "&", "*", "/", ":", "<", "=", ">", "?", "^", "_", "~"
    };
    SPECIAL_INITIAL.addAll(Arrays.asList(specialInitials));

    String[] specialSubsequents = {
      "+", "-", ".", "@"
    };
    SPECIAL_SUBSEQUENT.addAll(Arrays.asList(specialSubsequents));
  }

  /**
   * Returns true if the given string is not empty and contains only digits.
   */
  public static boolean isDigit(String str) {
    return nonVacuousIsOnly(str, Character::isDigit);
  }

  /**
   * Returns true if the given string is not empty and contains only letters.
   */
  public static boolean isLetter(String str) {
    return nonVacuousIsOnly(str, Character::isLetter);
  }

  /**
   * @param tr A single-character string.
   */
  public static boolean isInitial(String tr) {
    return isLetter(tr) || SPECIAL_INITIAL.contains(tr);
  }

  public static boolean isPunctuation(String tr) {
    return PUNCTUATION.containsKey(tr);
  }

  /**
   * Returns true if the given string is not empty and contains only characters
   * that are valid Subsequent characters.
   * 
   * <pre>
   *   Subsequent ::= Initial | Digit | SpecialSubsequent
   * </pre>
   */
  public static boolean isSubsequent(String str) {
    return isInitial(str) || isDigit(str) || isSpecialSubsequent(str);
  }

  /**
   * @param tr A single-character string.
   */
  public static boolean isSpecialSubsequent(String tr) {
    return SPECIAL_SUBSEQUENT.contains(tr);
  }

  /**
   * Returns true if the given string is not empty and contains only whitespace
   * characters.
   */
  public static boolean isWhitespace(String tr) {
    return nonVacuousIsOnly(tr, Character::isWhitespace);
  }

  /**
   * Returns true if the given string represents an escape sequence in a Scheme
   * string.
   */
  public static boolean isEscapedStringElement(String tr) {
    return ESCAPED_STRING_ELEMENT.containsKey(tr);
  }

  public static boolean isBoolean(String lexeme) {
    return BOOLEAN_LITERAL.containsKey(lexeme);
  }

  public static boolean parseBooleanValue(String lexeme) {
    if (isBoolean(lexeme)) {
      return BOOLEAN_LITERAL.get(lexeme);
    }
    throw Problem.internalError("Invalid boolean literal: %s", lexeme);
  }

  private static final String CHARACTER_LITERAL_PREFIX = "#\\";

  public static String parseCharacterValue(String lexeme) {
    if (SPECIAL_CHARACTER_LITERAL.containsKey(lexeme)) {
      return SPECIAL_CHARACTER_LITERAL.get(lexeme);
    }
    if (lexeme.length() == CHARACTER_LITERAL_PREFIX.length() + 1) {
      return lexeme.substring(CHARACTER_LITERAL_PREFIX.length());
    }
    throw Problem.internalError("Invalid character literal: %s", lexeme);
  }

  public static String unparseCharacterValue(String charValue) {
    if (CHARACTER_LITERAL_LOOKUP.containsKey(charValue)) {
      return CHARACTER_LITERAL_LOOKUP.get(charValue);
    }
    return CHARACTER_LITERAL_PREFIX + charValue;
  }

  public static TokenType parsePunctuation(String tr) {
    return PUNCTUATION.get(tr);
  }

  public static String parseStringValue(String lexeme) {
    StringBuilder content = new StringBuilder();
    // skip the double quotes at the start and end
    for (int i = 1; i < lexeme.length() - 1; ++i) {
      char cr = lexeme.charAt(i);
      if (cr == '\\') {
        // convert escape sequences
        String seq = "\\" + lexeme.charAt(++i);
        if (ESCAPED_STRING_ELEMENT.containsKey(seq)) {
          content.append(ESCAPED_STRING_ELEMENT.get(seq));
        } else {
          throw Problem.internalError("Invalid escape sequence: %s", seq);
        }
      } else {
        content.append(cr);
      }
    }
    return content.toString();
  }
  
  public static String unparseStringValue(String stringValue) {
    StringBuilder buff = new StringBuilder();
    buff.append('"');
    for (int i = 0; i < stringValue.length(); ++i) {
      String tr = String.valueOf(stringValue.charAt(i));
      if (STRING_ELEMENT_LOOKUP.containsKey(tr)) {
        buff.append(STRING_ELEMENT_LOOKUP.get(tr));
      } else {
        buff.append(tr);
      }
    }
    buff.append('"');
    return buff.toString();
  }

  public static Set<String> getCharacterNames() {
    return SPECIAL_CHARACTER_LITERAL.keySet();
  }

  public static int getMaxCharacterLiteralLength() {
    return MAX_CHARACTER_LITERAL_LENGTH;
  }

  /**
   * Returns true if the given string is not empty and contains only characters
   * that pass the given predicate.
   * 
   * @param str The string to test.
   * @param testPredicate The predicate to satisfy non-vacuously for all
   *        characters in the string.
   */
  private static boolean nonVacuousIsOnly(String str, Function<Character, Boolean> testPredicate) {
    if (str.isEmpty()) {
      return false;
    }
    for (int i = 0; i < str.length(); ++i) {
      if (!testPredicate.apply(str.charAt(i))) {
        return false;
      }
    }
    return true;
  }
}
