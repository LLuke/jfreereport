package org.jfree.report.dev.beans.templates;

import java.io.PrintWriter;

public class ConstantTemplate implements Template
{
  private String text;

  public ConstantTemplate(String text)
  {
    this.text = text;
  }

  public void print(PrintWriter writer, Context context)
  {
    writer.print(text);
  }
}
