package org.instructures.interp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Deque;
import java.util.LinkedList;
import org.instructures.interp.values.CompoundDatum;
import org.instructures.interp.values.Datum;
import org.instructures.interp.values.LexemeDatum;

/**
 * A parser for a subset of the data specified in the Revised^6 Report on the
 * Algorithmic Language Scheme (http://www.r6rs.org/final/r6rs.pdf), Section 4.3
 * (Datum Syntax).
 *
 * The subset only allows for the lexeme datum values specified in
 * {@link TokenType}, together with some of the list datum rules, including
 * dotted notation and basic quotes.
 * 
 * <pre>
 *   Datum ::= LexemeDatum | CompoundDatum
 *   LexemeDatum ::= Boolean | Number | Character | String | Symbol
 *   Symbol ::= Identifier
 *   CompoundDatum ::= List
 *   List ::= `(` {Datum} `)`
 *          | `[` {Datum} `]`
 *          | `(` [Datum] `.` Datum `)`
 *          | `[` [Datum] `.` Datum `]`
 *          | Abbreviation
 *   Abbreviation ::= `'` Datum
 * </pre>
 */
public class DatumParser {
  private final TokenScanner scanner;
  private Lexeme lookahead;

  /**
   * Constructs a DatumParser that consumes the given input source.
   */
  public DatumParser(BufferedReader in) throws IOException {
    this.scanner = new TokenScanner(in);
    readNextToken();
  }

  /**
   * Parses the given string as a single datum, assumed to be well formed.
   */
  public static Datum fromString(String datumText) throws IOException {
    DatumParser parser = new DatumParser(new BufferedReader(new StringReader(datumText)));
    Datum result = parser.nextDatum();
    if (parser.nextDatum() != null) {
      throw Problem.internalError("Invalid text given to parseDatum: %s", datumText);
    }
    return result;
  }
  
  private CompoundDatum parseList(TokenType start, TokenType end) throws IOException {
    Lexeme startingLocation = lookahead;
    match(start);
    Deque<Datum> elements = new LinkedList<>();
    while (lookahead.isNoneOf(end, TokenType.DOT, TokenType.EOF)) {
      elements.addLast(nextDatum());
    }
    if (lookahead.matches(TokenType.EOF)) {
      throw Problem.noMatch(startingLocation, "%s must be closed with %s",
        start.getDescription(), end.getDescription());
    }
    CompoundDatum result;
    if (lookahead.matches(TokenType.DOT)) {
      if (elements.isEmpty()) {
        throw Problem.noMatch(lookahead, "Expression expected before .");
      }
      Datum penultimate = elements.removeLast();
      match(TokenType.DOT);
      Datum last = nextDatum();
      result = CompoundDatum.newPair(penultimate, last);
    } else {
      result = CompoundDatum.newEmptyList();
    }
    while (!elements.isEmpty()) {
      result = CompoundDatum.newPair(elements.removeLast(), result);
    }
    match(end);
    return result;
  }

  /**
   * Driver for testing the parser. It parses all datums until an error is
   * found or when EOF is reached. If no errors are encountered, it outputs the
   * datums using list notation.
   */
  public static void main(String[] args) {
    for (String filename: args) {
      try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
        DatumParser parser = new DatumParser(in);
        Datum datum = parser.nextDatum();
        while (datum != null) {
          System.out.printf("%s%n", datum.toSyntaxString());
          datum = parser.nextDatum();
        }
      } catch (Problem e) {
        System.err.printf("%s:%s%n", filename, e.getMessage());
      } catch (Exception e) {
        e.printStackTrace(System.err);
      }
    }
  }

  /**
   * Returns the next datum read, or null when end-of-file is reached.
   */
  public Datum nextDatum() throws IOException {
	
	// These if statements take care of all LexemeDatum
	if(lookahead.getTokenType() == TokenType.BOOLEAN)
	{
		LexemeDatum data = lookahead.getValue();
		readNextToken();
		return data;
	}
	else if(lookahead.getTokenType() == TokenType.STRING)
	{
		LexemeDatum data = lookahead.getValue();
		readNextToken();
		return data;
	}
	else if(lookahead.getTokenType() == TokenType.CHARACTER)
	{
		LexemeDatum data = lookahead.getValue();
		readNextToken();
		return data;
	}
	else if(lookahead.getTokenType() == TokenType.NUMBER)
	{
		
		LexemeDatum data = lookahead.getValue();
		readNextToken();
		return data;
		
	}
	else if(lookahead.getTokenType() == TokenType.SYMBOL)
	{
		LexemeDatum data = lookahead.getValue();
		readNextToken();
		return data;
	}
	else if(lookahead.getTokenType() == TokenType.RPAREN)
	{
		LexemeDatum data = lookahead.getValue();
		return data;
	}
	else if(lookahead.getTokenType() == TokenType.RBRACK)
	{
		LexemeDatum data = lookahead.getValue();
		return data;
	}
	else if(lookahead.getTokenType() == TokenType.DOT)
	{
		LexemeDatum data = lookahead.getValue();
		return data;
	}
	
	// These cases will cover compoundDatum
	else if(lookahead.getTokenType() == TokenType.SQUOTE)
	{
		Lexeme current = lookahead;
		readNextToken();
		Datum sequel = nextDatum();
		Lexeme QLex = new Lexeme(TokenType.SYMBOL, "quote", current.getLineNumber());
		CompoundDatum data = CompoundDatum.newList(QLex.getValue(), sequel);
		return data;
	}
	else if(lookahead.getTokenType() == TokenType.LPAREN)
	{
		CompoundDatum data = parseList(TokenType.LPAREN, TokenType.RPAREN);
		return data;
	}
	else if(lookahead.getTokenType() == TokenType.LBRACK)
	{
		CompoundDatum data = parseList(TokenType.LBRACK, TokenType.RBRACK);
		return data;
	}
	// If it is none of these, then it should be the EOF
	else if(lookahead.getTokenType() == TokenType.EOF)
	{
		return null;
	}
	// return null if none of these happen
	readNextToken();
	return null;
	
	  
	
	 
	  

	//  CompoundDatum datass = parseList(TokenType.LPAREN, TokenType.RPAREN);

  }

  // Your code here//
  
  private void match(TokenType expected) throws IOException {
    if (lookahead.isNoneOf(expected)) {
      String found = (lookahead.matches(TokenType.EOF))
        ? "end of file" : String.format("\"%s\"", lookahead.getOriginalText());
      throw Problem.noMatch(lookahead, "Expected %s but found %s", expected, found);
    }
    readNextToken();
  }

  private void readNextToken() throws IOException {
    this.lookahead = scanner.nextToken();
    if (lookahead.hasMessage()) {
      throw Problem.invalidToken(lookahead);
    }
  }
}
