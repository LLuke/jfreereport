package org.jfree.report.dev.beans.templates;

import java.util.ArrayList;
import java.io.PrintWriter;

public class CompoundTemplate implements Template
{
  private ArrayList templates;

  public CompoundTemplate()
  {
    templates = new ArrayList();
  }

  public void addTemplate (final Template t)
  {
    templates.add (t);
  }

  public void print(final PrintWriter writer, final Context context) throws TemplateException
  {
    for (int i = 0; i < templates.size(); i++)
    {
      final Template t = (Template) templates.get(i);
      t.print(writer, context);
    }
  }
}
