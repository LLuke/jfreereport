package org.jfree.report.util;

import org.jfree.report.util.beans.ConverterRegistry;

public abstract class PropertyLookupParser
{
  private static final int EXPECT_DOLLAR = 0;
  private static final int EXPECT_OPEN_BRACE = 1;
  private static final int EXPECT_CLOSE_BRACE = 2;

  protected PropertyLookupParser ()
  {
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

      if (state == EXPECT_DOLLAR && c == '$')
      {
        state = EXPECT_OPEN_BRACE;
        continue;
      }
      if (state == EXPECT_OPEN_BRACE)
      {
        if (c == '{')
        {
          state = EXPECT_CLOSE_BRACE;
          continue;
        }
        else
        {
          result.append('$');
          state = 0;
        }
      }
      if (state == EXPECT_CLOSE_BRACE && c == '}')
      {
        final String s = lookupVariable(propertyName.toString());
        if (s == null)
        {
          result.append('$');
          result.append('{');
          result.append(propertyName);
          result.append('}');
        }
        else
        {
          result.append(s);
        }
        propertyName.delete(0, propertyName.length());
        state = 0;
        continue;
      }

      if (c == '\\')
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
      result.append('$');
      if (state >= EXPECT_CLOSE_BRACE)
      {
        result.append('{');
        result.append(propertyName);
      }
    }
    return result.toString();
  }

  protected abstract Object performInitialLookup (String name);

  private String lookupVariable (final String entity)
  {
    // first, split the entity into separate strings (separator is '.').

    final CSVTokenizer tokenizer = new CSVTokenizer(entity, ".");
    if (tokenizer.hasMoreTokens())
    {
      final String name = tokenizer.nextToken();
      final Object base = performInitialLookup(name);
      if (tokenizer.hasMoreTokens())
      {
        return continueLookupVariable(tokenizer, base);
      }
      else
      {
        return ConverterRegistry.toAttributeValue(base);
      }
    }
    return entity;
  }

  private static String continueLookupVariable (final CSVTokenizer tokenizer,
                                                final Object parent)
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
