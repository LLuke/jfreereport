package org.jfree.report.modules.output.table.html;

import org.jfree.util.StringUtils;

public class StyleBuilder
{
  private boolean compact;
  private StringBuffer style;
  private final static String INDENT = "    ";

  public StyleBuilder (final boolean compact)
  {
    this.compact = compact;
    this.style = new StringBuffer();
  }

  public void append (final String key, final String value)
  {
    if (compact == false)
    {
      if (style.length() != 0)
      {
        style.append(StringUtils.getLineSeparator());
      }
      style.append(INDENT);
    }

    style.append(key);
    style.append(": ");
    style.append(value);
    style.append(";");
  }

  public void append (final String key, final String value, final String unit)
  {
    if (compact == false)
    {
      if (style.length() != 0)
      {
        style.append(StringUtils.getLineSeparator());
      }
      style.append(INDENT);
    }

    style.append(key);
    style.append(": ");
    style.append(value);
    style.append(unit);
    style.append(";");
  }

  public String toString()
  {
    return style.toString();
  }
}
