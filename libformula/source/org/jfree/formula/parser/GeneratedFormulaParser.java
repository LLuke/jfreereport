/* Generated By:JavaCC: Do not edit this line. GeneratedFormulaParser.java */
package org.jfree.formula.parser;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.jfree.formula.lvalues.ContextLookup;
import org.jfree.formula.lvalues.FormulaFunction;
import org.jfree.formula.lvalues.LValue;
import org.jfree.formula.lvalues.PostfixTerm;
import org.jfree.formula.lvalues.PrefixTerm;
import org.jfree.formula.lvalues.StaticValue;
import org.jfree.formula.lvalues.Term;
import org.jfree.formula.operators.InfixOperator;
import org.jfree.formula.operators.OperatorFactory;
import org.jfree.formula.operators.PostfixOperator;
import org.jfree.formula.operators.PrefixOperator;

public abstract class GeneratedFormulaParser implements GeneratedFormulaParserConstants {

  protected GeneratedFormulaParser ()
  {
  }

  protected abstract OperatorFactory getOperatorFactory();

  final public LValue getExpression(int level) throws ParseException {
  LValue retval = null;
  Term ex = null;
  if (level == -1) level = -2;
    retval = getLValue(level + 1);
    label_1:
    while (true) {
      switch (jj_nt.kind) {
      case PLUS:
      case MINUS:
      case MULT:
      case DIV:
      case POW:
      case EQUALS:
      case NOT_EQUALS:
      case LT_EQUALS:
      case GT_EQUALS:
      case LT:
      case GT:
      case CONCAT:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      ex = startTail(retval, ex);
    }
    if (ex != null)
    {
      {if (true) return ex;}
    }
    if ((level == 0) && (retval instanceof Term))
    {
      ex = (Term) retval;
    }
    {if (true) return retval;}
    throw new Error("Missing return statement in function");
  }

  final public InfixOperator getInfixOperator() throws ParseException {
  InfixOperator op = null;
  Token value = null;
    switch (jj_nt.kind) {
    case PLUS:
      value = jj_consume_token(PLUS);
      break;
    case MINUS:
      value = jj_consume_token(MINUS);
      break;
    case MULT:
      value = jj_consume_token(MULT);
      break;
    case DIV:
      value = jj_consume_token(DIV);
      break;
    case POW:
      value = jj_consume_token(POW);
      break;
    case EQUALS:
      value = jj_consume_token(EQUALS);
      break;
    case NOT_EQUALS:
      value = jj_consume_token(NOT_EQUALS);
      break;
    case LT_EQUALS:
      value = jj_consume_token(LT_EQUALS);
      break;
    case GT_EQUALS:
      value = jj_consume_token(GT_EQUALS);
      break;
    case LT:
      value = jj_consume_token(LT);
      break;
    case GT:
      value = jj_consume_token(GT);
      break;
    case CONCAT:
      value = jj_consume_token(CONCAT);
      break;
    default:
      jj_la1[1] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    {if (true) return getOperatorFactory().createInfixOperator(value.image);}
    throw new Error("Missing return statement in function");
  }

  final public Term startTail(LValue retval, Term ex) throws ParseException {
  LValue val = null;
  InfixOperator op = null;
    op = getInfixOperator();
    val = getLValue(-1);
      if (ex == null)
      {
        if (retval instanceof Term)
        {
          ex = (Term) retval;
        }
        else
        {
          ex = new Term (retval);
        }
      }
      ex.add (op, val);

      {if (true) return ex;}
    throw new Error("Missing return statement in function");
  }

  final public LValue getLValue(int level) throws ParseException {
  Token value = null; LValue retval = null; LValue lval;
  PrefixOperator prefixOp = null;
  PostfixOperator postfixOp = null;
    switch (jj_nt.kind) {
    case PLUS:
    case MINUS:
      prefixOp = getPrefixOperator();
      break;
    default:
      jj_la1[2] = jj_gen;
      ;
    }
    switch (jj_nt.kind) {
    case COLUMN_LOOKUP:
      value = jj_consume_token(COLUMN_LOOKUP);
                                retval = new ContextLookup (ParserTools.stripQuote(value.image));
      break;
    case STRING_LITERAL:
      value = jj_consume_token(STRING_LITERAL);
                                 retval = new StaticValue (ParserTools.stripQuote(value.image));
      break;
    case UNSIGNED_NUMERIC_LITERAL:
      value = jj_consume_token(UNSIGNED_NUMERIC_LITERAL);
                                           retval = new StaticValue (new BigDecimal (value.image));
      break;
    case UNSIGNED_INTEGER:
      value = jj_consume_token(UNSIGNED_INTEGER);
                                   retval = new StaticValue (new BigDecimal (value.image));
      break;
    case NULL:
      value = jj_consume_token(NULL);
                       retval = new StaticValue (null);
      break;
    case IDENTIFIER:
      value = jj_consume_token(IDENTIFIER);
      jj_consume_token(L_PAREN);
      retval = parseFunction(value.image);
      jj_consume_token(R_PAREN);
      break;
    case L_PAREN:
      jj_consume_token(L_PAREN);
      retval = getExpression(level);
      jj_consume_token(R_PAREN);
        if (level != -1)
        {
          retval = new Term (retval);
        }
      break;
    default:
      jj_la1[3] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    switch (jj_nt.kind) {
    case PERCENT:
      postfixOp = getPostfixOperator();
      break;
    default:
      jj_la1[4] = jj_gen;
      ;
    }
    if (postfixOp != null)
    {
      retval = new PostfixTerm(retval, postfixOp);
    }
    if (prefixOp != null)
    {
      retval = new PrefixTerm(prefixOp, retval);
    }
    {if (true) return retval;}
    throw new Error("Missing return statement in function");
  }

  final public LValue parseFunction(String name) throws ParseException {
   ArrayList params = null;
   LValue parameter = null;
    switch (jj_nt.kind) {
    case UNSIGNED_INTEGER:
    case L_PAREN:
    case PLUS:
    case MINUS:
    case IDENTIFIER:
    case COLUMN_LOOKUP:
    case STRING_LITERAL:
    case UNSIGNED_NUMERIC_LITERAL:
    case NULL:
      parameter = getExpression(0);
        params = new ArrayList();
        params.add(parameter);
      label_2:
      while (true) {
        switch (jj_nt.kind) {
        case COMMA:
          ;
          break;
        default:
          jj_la1[5] = jj_gen;
          break label_2;
        }
        jj_consume_token(COMMA);
        parameter = getExpression(0);
          params.add(parameter);
      }
      break;
    default:
      jj_la1[6] = jj_gen;
      ;
    }
     if (params == null)
     {
       {if (true) return new FormulaFunction(name, new LValue[0]);}
     }

     LValue[] paramVals = (LValue[]) params.toArray(new LValue[params.size()]);
     {if (true) return new FormulaFunction(name, paramVals);}
    throw new Error("Missing return statement in function");
  }

  final public PrefixOperator getPrefixOperator() throws ParseException {
  Token value = null;
    switch (jj_nt.kind) {
    case PLUS:
      value = jj_consume_token(PLUS);
      break;
    case MINUS:
      value = jj_consume_token(MINUS);
      break;
    default:
      jj_la1[7] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
     {if (true) return getOperatorFactory().createPrefixOperator(value.image);}
    throw new Error("Missing return statement in function");
  }

  final public PostfixOperator getPostfixOperator() throws ParseException {
  Token value = null;
    value = jj_consume_token(PERCENT);
     {if (true) return getOperatorFactory().createPostfixOperator(value.image);}
    throw new Error("Missing return statement in function");
  }

  public GeneratedFormulaParserTokenManager token_source;
  JavaCharStream jj_input_stream;
  public Token token, jj_nt;
  private int jj_gen;
  final private int[] jj_la1 = new int[8];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_0();
      jj_la1_1();
   }
   private static void jj_la1_0() {
      jj_la1_0 = new int[] {0xff000000,0xff000000,0x3000000,0x80100,0x0,0x2000,0x3080100,0x3000000,};
   }
   private static void jj_la1_1() {
      jj_la1_1 = new int[] {0xf,0xf,0x0,0x21e0,0x10,0x0,0x21e0,0x0,};
   }

  public GeneratedFormulaParser(java.io.InputStream stream) {
    jj_input_stream = new JavaCharStream(stream, 1, 1);
    token_source = new GeneratedFormulaParserTokenManager(jj_input_stream);
    token = new Token();
    token.next = jj_nt = token_source.getNextToken();
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
  }

  public void ReInit(java.io.InputStream stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    token.next = jj_nt = token_source.getNextToken();
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
  }

  public GeneratedFormulaParser(java.io.Reader stream) {
    jj_input_stream = new JavaCharStream(stream, 1, 1);
    token_source = new GeneratedFormulaParserTokenManager(jj_input_stream);
    token = new Token();
    token.next = jj_nt = token_source.getNextToken();
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
  }

  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    token.next = jj_nt = token_source.getNextToken();
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
  }

  public GeneratedFormulaParser(GeneratedFormulaParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    token.next = jj_nt = token_source.getNextToken();
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
  }

  public void ReInit(GeneratedFormulaParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    token.next = jj_nt = token_source.getNextToken();
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
  }

  final private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken = token;
    if ((token = jj_nt).next != null) jj_nt = jj_nt.next;
    else jj_nt = jj_nt.next = token_source.getNextToken();
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    jj_nt = token;
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  final public Token getNextToken() {
    if ((token = jj_nt).next != null) jj_nt = jj_nt.next;
    else jj_nt = jj_nt.next = token_source.getNextToken();
    jj_gen++;
    return token;
  }

  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private java.util.Vector jj_expentries = new java.util.Vector();
  private int[] jj_expentry;
  private int jj_kind = -1;

  public ParseException generateParseException() {
    jj_expentries.removeAllElements();
    boolean[] la1tokens = new boolean[46];
    for (int i = 0; i < 46; i++) {
      la1tokens[i] = false;
    }
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 8; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 46; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.addElement(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.elementAt(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  final public void enable_tracing() {
  }

  final public void disable_tracing() {
  }

}
