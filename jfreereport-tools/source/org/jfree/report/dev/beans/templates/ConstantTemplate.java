package org.jfree.report.dev.beans.templates;

import java.io.PrintWriter;

public class ConstantTemplate implements Template
{
  private String text;

  public ConstantTemplate(final String text)
  {
    this.text = text;
  }

  public void print(final PrintWriter writer, final Context context)
  {
    writer.print(text);
  }
}
