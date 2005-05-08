package org.jfree.report.util;

import java.io.Serializable;

import org.jfree.report.util.beans.ConverterRegistry;
import org.jfree.report.util.beans.BeanException;

public abstract class PropertyLookupParser implements Serializable
{
  private static final int EXPECT_DOLLAR = 0;
  private static final int EXPECT_OPEN_BRACE = 1;
  private static final int EXPECT_CLOSE_BRACE = 2;
  private char markerChar;
  private char closingBraceChar;
  private char openingBraceChar;
  private char escapeChar;

  protected PropertyLookupParser ()
  {
    markerChar = '$';
    closingBraceChar = '}';
    openingBraceChar = '{';
    escapeChar = '\\';
  }

  public char getClosingBraceChar ()
  {
    return closingBraceChar;
  }

  public void setClosingBraceChar (final char closingBraceChar)
  {
    this.closingBraceChar = closingBraceChar;
  }

  public char getEscapeChar ()
  {
    return escapeChar;
  }

  public void setEscapeChar (final char escapeChar)
  {
    this.escapeChar = escapeChar;
  }

  public char getOpeningBraceChar ()
  {
    return openingBraceChar;
  }

  public void setOpeningBraceChar (final char openingBraceChar)
  {
    this.openingBraceChar = openingBraceChar;
  }

  public char getMarkerChar ()
  {
    return markerChar;
  }

  public void setMarkerChar (final char markerChar)
  {
    this.markerChar = markerChar;
  }

  public String translateAndLookup (final String value)
  {
    if (value == null)
    {
      return null;
    }

    final char[] chars = value.toCharArray();
    final StringBuffer result = new StringBuffer(chars.length);
    boolean haveEscape = false;
    int state = EXPECT_DOLLAR;
    final StringBuffer propertyName = new StringBuffer();

    for (int i = 0; i < chars.length; i++)
    {
      final char c = chars[i];

      if (haveEscape)
      {
        haveEscape = false;
        if (state == EXPECT_CLOSE_BRACE)
        {
          propertyName.append(c);
        }
        else
        {
          result.append(c);
        }
        continue;
      }

      if (state == EXPECT_DOLLAR && c == markerChar)
      {
        state = EXPECT_OPEN_BRACE;
        continue;
      }
      if (state == EXPECT_OPEN_BRACE)
      {
        if (c == openingBraceChar)
        {
          state = EXPECT_CLOSE_BRACE;
          continue;
        }
        else
        {
          result.append(markerChar);
          state = 0;
        }
      }
      if (state == EXPECT_CLOSE_BRACE && c == closingBraceChar)
      {
        final String s = lookupVariable(propertyName.toString());
        if (s == null)
        {
          result.append(markerChar);
          result.append(openingBraceChar);
          result.append(propertyName);
          result.append(closingBraceChar);
        }
        else
        {
          result.append(s);
        }
        propertyName.delete(0, propertyName.length());
        state = 0;
        continue;
      }

      if (c == escapeChar)
      {
        haveEscape = true;
        continue;
      }

      if (state == EXPECT_CLOSE_BRACE)
      {
        propertyName.append(c);
      }
      else
      {
        result.append(c);
      }
    }

    if (state >= EXPECT_OPEN_BRACE)
    {
      result.append(markerChar);
      if (state >= EXPECT_CLOSE_BRACE)
      {
        result.append(openingBraceChar);
        result.append(propertyName);
      }
    }
    return result.toString();
  }

  protected abstract Object performInitialLookup (String name);

  protected String lookupVariable (final String entity)
  {
    // first, split the entity into separate strings (separator is '.').

    final CSVTokenizer tokenizer = new CSVTokenizer(entity, ".");
    if (tokenizer.hasMoreTokens())
    {
      final String name = tokenizer.nextToken();
      final Object base = performInitialLookup(name);
      try
      {
        if (tokenizer.hasMoreTokens())
        {
          return continueLookupVariable(tokenizer, base);
        }
        else
        {
          return ConverterRegistry.toAttributeValue(base);
        }
      }
      catch (BeanException e)
      {
        return entity;
      }
    }
    return entity;
  }

  private static String continueLookupVariable (final CSVTokenizer tokenizer,
                                                final Object parent)
          throws BeanException
  {
    if (tokenizer.hasMoreTokens())
    {
      final String name = tokenizer.nextToken();
      final Object base = ConverterRegistry.toPropertyValue(name, parent.getClass());
      if (tokenizer.hasMoreTokens())
      {
        return continueLookupVariable(tokenizer, base);
      }
      else
      {
        return ConverterRegistry.toAttributeValue(base);
      }
    }
    return null;
  }

}
