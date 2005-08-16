package org.jfree.report.dev.beans.templates;

import java.io.IOException;
import java.net.URL;

import org.jfree.xml.ElementDefinitionException;
import org.jfree.xml.ParserFrontend;
import org.jfree.util.ObjectUtilities;

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
    final URL url = ObjectUtilities.getResourceRelative(TEMPLATE_NAME, TemplateParserFrontend.class);
    return (Template) parse(url, url);
  }
}
