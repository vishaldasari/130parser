package org.instructures.interp;

import org.instructures.interp.values.LexemeDatum;

/**
 * A sequence of characters composing a valid Scheme token. A lexeme is a
 * specific instance of a token and has a line number.
 */
public class Lexeme {
  private final TokenType tokenType;
  private final String text;
  private final String message;
  private final int lineNumber;

  public Lexeme(TokenType tokenType, String text, int lineNumber) {
    this(tokenType, text, "", lineNumber);
  }

  public Lexeme(TokenType tokenType, String text, String message, int lineNumber) {
    this.tokenType = tokenType;
    this.text = text;
    this.message = message;
    this.lineNumber = lineNumber;
  }

  /**
   * A lexeme represents a value if it is one of the lexeme datum types, such
   * as literals and symbols.
   */
  public boolean hasValue() {
    return tokenType.hasValue();
  }

  public LexemeDatum getValue() {
    if (tokenType.hasValue()) {
      return tokenType.getValueFrom(text);
    }
    return null;
  }

  public int getLineNumber() {
    return lineNumber;
  }
  
  public boolean hasMessage() {
    return !message.isEmpty();
  }

  public String getMessage() {
    return message;
  }

  public boolean isNoneOf(TokenType... tokenTypes) {
    for (TokenType tokenType: tokenTypes) {
      if (matches(tokenType)) {
        return false;
      }
    }
    return true;
  }

  public boolean matches(TokenType tokenType) {
    return (this.tokenType == tokenType);
  }
  
  public TokenType getTokenType() {
    return tokenType;
  }
  
  public String getOriginalText() {
    return text;
  }

  @Override
  public String toString() {
    if (hasMessage()) {
      return String.format("Error: %s", message);
    } else if (tokenType.hasValue()) {
      return String.format("%s <%s>", tokenType, text);
    } else {
      return tokenType.toString();
    }
  }
}
