package org.jfree.report.dev.beans.templates;

import java.io.PrintWriter;

public interface Template
{
  public void print (PrintWriter writer, Context context) throws TemplateException;
}
