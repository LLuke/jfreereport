package org.jfree.report.dev.beans.templates;

import org.jfree.xml.Parser;

public class TemplateParser extends Parser
{
  private CompoundTemplate template;
  public TemplateParser()
  {
    template = new CompoundTemplate();
    setInitialFactory(new TemplatesElementHandler(this, template));
  }

  public Parser getInstance()
  {
    return new TemplateParser();
  }

  public Object getResult()
  {
    return template;
  }
}
