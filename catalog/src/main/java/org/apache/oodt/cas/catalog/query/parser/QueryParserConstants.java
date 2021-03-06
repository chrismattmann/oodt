/* Generated By:JJTree&JavaCC: Do not edit this line. QueryParserConstants.java */
package org.apache.oodt.cas.catalog.query.parser;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface QueryParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int SPACE = 4;
  /** RegularExpression Id. */
  int AND = 5;
  /** RegularExpression Id. */
  int OR = 6;
  /** RegularExpression Id. */
  int QUOTE = 7;
  /** RegularExpression Id. */
  int EQUALS = 8;
  /** RegularExpression Id. */
  int SEMI_COLON = 9;
  /** RegularExpression Id. */
  int COMMA = 10;
  /** RegularExpression Id. */
  int OPEN_BRACES = 11;
  /** RegularExpression Id. */
  int CLOSE_BRACES = 12;
  /** RegularExpression Id. */
  int OPEN_PARENS = 13;
  /** RegularExpression Id. */
  int CLOSE_PARENS = 14;
  /** RegularExpression Id. */
  int BUCKET_NAME_KEY = 15;
  /** RegularExpression Id. */
  int CUSTOM_NAME_KEY = 16;
  /** RegularExpression Id. */
  int P_KEY = 17;
  /** RegularExpression Id. */
  int EQ = 18;
  /** RegularExpression Id. */
  int GE = 19;
  /** RegularExpression Id. */
  int GT = 20;
  /** RegularExpression Id. */
  int LE = 21;
  /** RegularExpression Id. */
  int LT = 22;
  /** RegularExpression Id. */
  int TERM = 23;
  /** RegularExpression Id. */
  int VALUE = 24;
  /** RegularExpression Id. */
  int NON_TERM = 25;
  /** RegularExpression Id. */
  int STRING_LITERAL = 26;
  /** RegularExpression Id. */
  int SPECIAL_CHARS = 27;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\"\\r\"",
    "\"\\n\"",
    "\"\\r\\n\"",
    "\" \"",
    "\" AND \"",
    "\" OR \"",
    "\"\\\'\"",
    "\"=\"",
    "\";\"",
    "\",\"",
    "\"{\"",
    "\"}\"",
    "\"(\"",
    "\")\"",
    "\"bucketNames\"",
    "\"name\"",
    "<P_KEY>",
    "\"==\"",
    "\">=\"",
    "\">\"",
    "\"<=\"",
    "\"<\"",
    "<TERM>",
    "<VALUE>",
    "<NON_TERM>",
    "<STRING_LITERAL>",
    "<SPECIAL_CHARS>",
  };

}
