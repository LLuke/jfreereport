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

  public void addTemplate (Template t)
  {
    templates.add (t);
  }

  public void print(PrintWriter writer, Context context) throws TemplateException
  {
    for (int i = 0; i < templates.size(); i++)
    {
      Template t = (Template) templates.get(i);
      t.print(writer, context);
    }
  }
}
