/*
 * Created by IntelliJ IDEA.
 * User: user
 * Date: 20.08.2002
 * Time: 21:34:34
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package com.jrefinery.report.function;

import com.jrefinery.report.util.PropertiesIterator;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Iterator;

public class TextFormatExpression extends AbstractExpression
{
  public static final String PATTERN_PROPERTY = "pattern";

  private ArrayList fieldList;

  public TextFormatExpression()
  {
    fieldList = new ArrayList();
  }

  public Object getValue()
  {
    return MessageFormat.format(getPattern(), collectValues());
  }

  private Object[] collectValues ()
  {
    Object[] retval = new Object[fieldList.size()];
    for (int i = 0; i < fieldList.size(); i++)
    {
      String field = (String) fieldList.get(i);
      retval [i] = getDataRow().get(field);
    }
    return retval;
  }

  public void initialize() throws FunctionInitializeException
  {
    super.initialize();
    fieldList.clear();
    Iterator textFormatIterator = new PropertiesIterator (getProperties());
    while (textFormatIterator.hasNext())
    {
      fieldList.add (textFormatIterator.next());
    }
  }

  public String getPattern()
  {
    return getProperty(PATTERN_PROPERTY);
  }

  public void setPattern(String pattern)
  {
    setProperty(PATTERN_PROPERTY, pattern);
  }

  public Object clone() throws CloneNotSupportedException
  {
    TextFormatExpression tex = (TextFormatExpression) super.clone();
    tex.fieldList = new ArrayList(fieldList);
    return tex;
  }
}
