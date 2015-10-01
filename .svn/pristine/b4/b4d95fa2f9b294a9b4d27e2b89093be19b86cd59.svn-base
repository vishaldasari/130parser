package org.instructures.interp;

import org.instructures.interp.values.LexemeDatum;

/**
 * A subset of the tokens specified in the Revised^6 Report on the Algorithmic
 * Language Scheme (http://www.r6rs.org/final/r6rs.pdf), Section 4.2 (Lexical
 * Syntax).
 * 
 * The subset only allows for identifiers, booleans, characters, strings,
 * numbers (only decimal integers), parenthesis, square brackets, a single
 * quote, and the dot.
 * 
 * <pre>
 *   Lexeme ::= Identifier | Boolean | Number | Character | String | ( | ) | [ | ] | ' | .
 * </pre>
 * 
 * Not represented by lexemes are whitespace and comments. This subset
 * recognizes:
 * 
 * <pre>
 *   Whitespace ::= {{See below}}
 *   Comment ::= ; {{All subsequent characters up to a line ending}}
 * </pre>
 * 
 * Where whitespace is any character <code>t</code> such that
 * <code>java.lang.Character.isWhitespace(t)</code> returns true, and a line
 * ending is when the result of <code>java.lang.Character.getType(t)</code> is
 * <code>java.lang.Character.LINE_SEPARATOR</code>.
 */
public enum TokenType {
  /**
   * A symbol: a sequence of letters and other special characters to form an
   * identifier.
   * 
   * <pre>
   *   Identifier ::= Initial {Subsequent} | PeculiarIdentifier
   *   Initial ::= Constituent | SpecialInitial
   *   Constituent ::= Letter
   *   Letter ::= a | b | c | .. | z | A | B | C | .. | Z
   *   SpecialInitial ::= ! | $ | % | &amp; | * | / | : | &lt; | = | &gt; | ? | ^ | _ | ~
   *   Subsequent ::= Initial | Digit | SpecialSubsequent
   *   Digit ::= 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
   *   SpecialSubsequent ::= + | - | . | @
   *   PeculiarIdentifier ::= + | - | ... | ->{Subsequent}
   * </pre>
   */
  SYMBOL(true) {
    @Override
    public LexemeDatum.SymbolDatum getValueFrom(String lexeme) {
      return LexemeDatum.newSymbol(lexeme);
    }

    @Override
    public String getDescription() {
      return "an identifier";
    }
  },

  /**
   * A boolean literal represents either true or false.
   * 
   * <pre>
   *   Boolean ::= #t | #T | #f | #F
   * </pre>
   */
  BOOLEAN(true) {
    @Override
    public LexemeDatum.BooleanDatum getValueFrom(String lexeme) {
      return LexemeDatum.newBoolean(LexicalUtils.parseBooleanValue(lexeme));
    }

    @Override
    public String getDescription() {
      return "a Boolean value";
    }
  },

  /**
   * A single character literal.
   *
   * <pre>
   *   Character ::= #\{{Any Character}} | #\CharacterName
   *   CharacterName ::= nul | tab | newline | return | space
   * </pre>
   * 
   * Where:
   * <ul>
   * <li><code>#\nul</code> is <code>'\0'</code></li>
   * <li><code>#\tab</code> is <code>'\t'</code></li>
   * <li><code>#\newline</code> is <code>'\n'</code></li>
   * <li><code>#\return</code> is <code>'\r'</code></li>
   * <li><code>#\space</code> is <code>' '</code></li>
   * </ul>
   */
  CHARACTER(true) {
    @Override
    public LexemeDatum.CharacterDatum getValueFrom(String lexeme) {
      return LexemeDatum.newCharacter(LexicalUtils.parseCharacterValue(lexeme));
    }

    @Override
    public String getDescription() {
      return "a single character";
    }
  },

  /**
   * A string literal.
   *
   * <pre>
   *   String ::= " {StringElement} "
   *   StringElement ::= NonEscaped | \t | \n | \r | \" | \\
   *   NonEscaped ::= {{Any character other than " or \}}
   * </pre>
   */
  STRING(true) {
    @Override
    public LexemeDatum.StringDatum getValueFrom(String lexeme) {
      return LexemeDatum.newString(LexicalUtils.parseStringValue(lexeme));
    }

    @Override
    public String getDescription() {
      return "a string literal";
    }
  },

  /**
   * A numeric literal, limited to decimal integers for this subset.
   * 
   * <pre>
   *   Number ::= Sign Uinteger10
   *   Sign ::= Empty | + | -
   *   Uinteger10 ::= Digit {Digit}
   *   Digit ::= 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
   * </pre>
   */
  NUMBER(true) {
    @Override
    public LexemeDatum.NumberDatum getValueFrom(String lexeme) {
      return LexemeDatum.newNumber(lexeme);
    }

    @Override
    public String getDescription() {
      return "a number";
    }
  },

  /**
   * A left (open) parenthesis: '('.
   */
  LPAREN {
    @Override
    public TokenType getTerminator() {
      return RPAREN;
    }

    @Override
    public String getDescription() {
      return "\"(\"";
    }
  },

  /**
   * A right (closed) parenthesis: ')'.
   */
  RPAREN {
    @Override
    public String getDescription() {
      return "\")\"";
    }
  },

  /**
   * A left (open) square bracket: '['.
   */
  LBRACK {
    @Override
    public TokenType getTerminator() {
      return RBRACK;
    }

    @Override
    public String getDescription() {
      return "\"[\"";
    }
  },

  /**
   * A right (closed) square bracket: ']'.
   */
  RBRACK {
    @Override
    public String getDescription() {
      return "\"]\"";
    }
  },

  /**
   * A single quote: '\''.
   */
  SQUOTE {
    @Override
    public String getDescription() {
      return "\"'\"";
    }
  },

  /**
   * A dot: '.'.
   */
  DOT {
    @Override
    public String getDescription() {
      return "\".\"";
    }
  },

  /**
   * The value to return (with a helpful error message) for any character
   * sequence not defined by a token or whitespace.
   */
  ERROR {
    @Override
    public String getDescription() {
      return "error";
    }
  },

  /**
   * The special end-of-file sentinel.
   */
  EOF {
    @Override
    public String getDescription() {
      return "EOF";
    }
  };

  private boolean hasValue;

  TokenType() {
    this(false);
  }

  TokenType(boolean hasValue) {
    this.hasValue = hasValue;
  }

  /**
   * A token's lexeme has a value if it is one of the lexeme datum types, such
   * as literals and symbols.
   */
  public boolean hasValue() {
    return hasValue;
  }

  /**
   * Processes the given text into an instance of a suitable Java type. Returns
   * null for lexemes that don't have values.
   * 
   * @param lexeme The characters of text that comprises the token.
   */
  public LexemeDatum getValueFrom(String lexeme) {
    return null;
  }

  public TokenType getTerminator() {
    return EOF;
  }

  public abstract String getDescription();
}
