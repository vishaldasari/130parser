package org.instructures.interp.values;

import java.util.LinkedList;
import java.util.List;
import org.instructures.interp.Problem;

/**
 * An interface for a subset of the base library types in the Revised^6 Report
 * on the Algorithmic Language Scheme (http://www.r6rs.org/final/r6rs.pdf),
 * Section 11.1 (Base Types, p. 31).
 */
public interface Value {
  /**
   * Returns true only for an object that is a symbol. This corresponds to the
   * {@code symbol?} predicate.
   */
  default boolean isSymbol() {
    return false;
  }

  /**
   * Returns true only for an object that is a cons cell. This corresponds to
   * the {@code pair?} predicate.
   */
  default boolean isPair() {
    return false;
  }

  /**
   * Returns true only for an object that is the empty list. This corresponds to
   * the {@code null?} predicate and the {@code '()} value.
   */
  default boolean isEmptyList() {
    return false;
  }

  /**
   * Returns true if the value is either a cons cell or an empty list.
   */
  default boolean isList() {
    return isPair() || isEmptyList();
  }

  /**
   * Returns true only for an object that is a procedure. This corresponds to
   * the {@code procedure?} predicate.
   */
  default boolean isProcedure() {
    return false;
  }

  /**
   * Returns true only for an object that is a Boolean. This corresponds to the
   * {@code boolean?} predicate.
   */
  default boolean isBoolean() {
    return false;
  }

  /**
   * Returns {@code true} for everything except for the special #f object and
   * the "unspecified value".
   */
  default boolean isTrue() {
    return true;
  }

  /**
   * Returns true only for an object that is a number. This corresponds to the
   * {@code number?} predicate.
   */
  default boolean isNumber() {
    return false;
  }

  /**
   * Returns true only for an object that is a character. This corresponds to
   * the {@code char?} predicate.
   */
  default boolean isCharacter() {
    return false;
  }

  /**
   * Returns {@code true} for everything except for the "unspecified value".
   */
  default boolean isSpecified() {
    return true;
  }

  /**
   * Returns true only for an object that is a string. This corresponds to the
   * {@code string?} predicate.
   */
  default boolean isString() {
    return false;
  }

  default Value getCar() {
    throw Problem.typeError("Not a cons");
  }

  default Value getCdr() {
    throw Problem.typeError("Not a cons");
  }

  /**
   * Converts an S-Expression, assumed to have a syntactic form matching
   * <code>( {Datum} )</code> into a List composed of each datum, in order. A
   * syntax error is thrown if the S-Expression is not a regular list.
   */
  default LinkedList<Value> asProperList() {
    LinkedList<Value> result = new LinkedList<>();
    if (this.asImproperList(result)) {
      throw Problem.typeError("Not properly formed list");
    }
    return result;
  }

  /**
   * Returns true if the list was improper: that is, the last element in the
   * output list was the cdr.
   */
  default boolean asImproperList(List<Value> listOut) {
    throw Problem.typeError("Not a pair");
  }

  default Value apply(List<Value> args) {
    throw Problem.typeError("Not a function");
  }

  default String toSyntaxString() {
    return this.toString();
  }
}
