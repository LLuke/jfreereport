package org.jfree.report.dev.beans.templates;

import java.io.IOException;
import java.net.URL;

import org.jfree.xml.ElementDefinitionException;
import org.jfree.xml.ParserFrontend;

public class TemplateParserFrontend extends ParserFrontend
{
  public static final String BEAN_DESCRIPTOR_KEY = "beanDescriptor";
  public static final String WRITER = "writer";
  private static final String TEMPLATE_NAME = "templates.xml";

  public TemplateParserFrontend()
  {
    super(new TemplateParser());
  }

  public Template performParsing ()
      throws IOException, ElementDefinitionException
  {
    URL url = getClass().getResource(TEMPLATE_NAME);
    return (Template) parse(url, url);
  }
}
