package org.instructures.interp;

import static org.instructures.interp.LexicalUtils.*;
import java.io.*;
import java.util.function.Predicate;

public class TokenScanner {
  private final LineNumberReader reader;

  public TokenScanner(Reader in) {
    this.reader = new LineNumberReader(in);
    reader.setLineNumber(1);
  }

  /**
   * Driver for testing the scanner code.
   */
  public static void main(String[] args) {
    for (String filename: args) {
      try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
        TokenScanner scanner = new TokenScanner(in);
        Lexeme token;
        do {
          token = scanner.nextToken();
          System.out.printf("%s:%02d: %s%n", filename, token.getLineNumber(), token);
        } while (token.isNoneOf(TokenType.EOF));
      } catch (Exception e) {
        System.err.printf("TokenScanner: %s%n", e.getMessage());
        e.printStackTrace(System.err);
      }
    }
  }

  public Lexeme nextToken() throws IOException {
	  String tr = readNext(1);
	    final PrintStream out = System.out;
	    LexicalUtils checker = new LexicalUtils();

	    while (isWhitespace(tr) || tr.equals(";")) {
	      if (tr.equals(";")) {
	        reader.readLine();
	      }
	      tr = readNext(1);
	    }
	    if (tr.isEmpty()) {
	      return result(TokenType.EOF, "");
	    } else if (tr.equals(".")) {
	      String lookahead = peekNext(2);
	      if (lookahead.equals("..")) {
	        match(lookahead);
	        return result(TokenType.SYMBOL, "...");
	      }
	      return result(TokenType.DOT, ".");
	    } 
	    
	    String next = peekNext(1);  // peek next character
	    
	    // First if statement:  Handles  --> "(" , ")" , "[" , "]" , "'" 
	    
	    if (checker.isPunctuation(tr)){	  	  
	  	  return result(checker.parsePunctuation(tr), tr);
	    }
	   	 
	    // Second if statement: Handles  -->  "#" 
	    else  if (tr.equals("#")){   	
	    	  while(checker.isSubsequent(next) || checker.isEscapedStringElement("\\" + next)) {	  
	    		  tr = tr + next;
				  match(next);
				  next = peekNext(1);
			  }	
	    	  
	    	   if (checker.isBoolean(tr)){
	  			return result(TokenType.BOOLEAN, tr);  
	  		  }
	    	   else if (checker.getCharacterNames().contains(tr))
	    		  return result(TokenType.CHARACTER, tr);
	    	   else  {
	    		   if (tr.charAt(1) != '\\')
	    		   return error(tr, "Invalid character after \"#\": %s", tr.substring(0,2));
	    		   return result(TokenType.CHARACTER, tr);
	    	   }
	    }
	    
	    // Third if statement: Handles -->  "     note: double quotation

	    else   if (tr.equals( "\"" )) {
	    	
	    	if (next.equals("\"")){
	    		tr = tr + next;
	    		match(next);
	    		return result(TokenType.STRING, tr);
	    	}
	    	
	    	while (!next.equals("\"")){
	    	
	    		tr = tr + next;
	    		match(next);
	    		next = peekNext(1);
	    		
	    		
	    		if (next.equals("\\")){
	    			String validEscape = peekNext(2);
	    		
	    			if(!checker.isEscapedStringElement(validEscape))
					 return error(validEscape, "Invalid string escape: \"%s\"", validEscape);
	    			  			
	    				tr = tr + validEscape;
	    			match(validEscape);
	    			next = peekNext(1);	
	    		}	    		
	    		
	    		if(next.equals("\"")){
    				tr = tr + next;
    				match(next);
    				return result(TokenType.STRING, tr);
	    			
    			}
	    		
	    	}	    	
	    	
	    	return result(TokenType.STRING, tr);
	    	
	    }
	    
	    // Fourth if statement: Handles --> digits, letters, and all other symbols
		  else if (checker.isSubsequent(tr)){		  
			  while(checker.isSubsequent(next)) {
				  tr = tr + next;
				  match(next);
				  next = peekNext(1);
			  }		  
			  if (checker.isDigit(tr))
					  return result(TokenType.NUMBER, tr);
			  else if (tr.matches(".*[a-z].*") || !checker.isDigit(tr.substring(1)))
			  {
				  	  if(tr == "quote")
				  		  return result(TokenType.SQUOTE, tr);
				  	  else
				  		  return result(TokenType.SYMBOL, tr);
			  }
			  else return result(TokenType.NUMBER, tr);
					  
		  }
		  else{
			  if (tr.equals("{") || tr.equals("}") )
			  return error(tr, "Unexpected character: \"%s\"", tr);
			  
			  
			  return result(TokenType.EOF, ""); 
		  }    
  }

  private String appendRest(String lexeme, Predicate<String> byCharPredicate) throws IOException {
    return appendRest(lexeme, byCharPredicate, Integer.MAX_VALUE);
  }

  private String appendRest(String lexeme, Predicate<String> byCharPredicate, int limit) throws IOException {
    String lookahead = peekNext(1);
    while (byCharPredicate.test(lookahead) && limit-- > 0) {
      lexeme += lookahead;
      match(lookahead);
      lookahead = peekNext(1);
    }
    return lexeme;
  }

  // Peeks ahead and returns the next characters to be read. The length of the
  // string returned depends on how many characters are left in the input.
  private String peekNext(int nextCharsCount) throws IOException {
    reader.mark(nextCharsCount + 1);
    String result = readNext(nextCharsCount);
    reader.reset();
    return result;
  }

  // Returns a string containing as many of the given count of characters as
  // possible.
  private String readNext(int nextCharsCount) throws IOException {
    char[] buff = new char[nextCharsCount];
    int actualLength = reader.read(buff, 0, buff.length);
    return (actualLength >= 1) ? new String(buff, 0, actualLength) : "";
  }

  private void match(String charsToSkip) throws IOException {
    reader.skip(charsToSkip.length());
  }

  private Lexeme result(TokenType tokenType, String text) {
    int lineNumber = reader.getLineNumber();
    return new Lexeme(tokenType, text, lineNumber);
  }

  private Lexeme error(String text, String format, Object... args) throws IOException {
    String errorMessage = String.format(format, args);
    int lineNumber = reader.getLineNumber();
    // consume the rest of the line to reduce cascading errors
    reader.readLine();
    return new Lexeme(TokenType.ERROR, text, errorMessage, lineNumber);
  }
}
