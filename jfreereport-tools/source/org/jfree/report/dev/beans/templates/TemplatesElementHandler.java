package org.jfree.report.dev.beans.templates;

import org.jfree.xml.AbstractElementDefinitionHandler;
import org.jfree.xml.ParseException;
import org.jfree.xml.Parser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class TemplatesElementHandler extends AbstractElementDefinitionHandler
{
  private CompoundTemplate template;

  public TemplatesElementHandler(final Parser parser, CompoundTemplate template)
  {
    super(parser);
    this.template = template;
  }

  public void startElement(String tagName, Attributes attrs) throws SAXException
  {
    if (tagName.equals("print"))
    {
      String var= attrs.getValue("var");
      String bean = attrs.getValue("bean");
      String style = attrs.getValue("style");
      template.addTemplate(new PrintVarTemplate(bean, var, "quoted".equals(style)));

    }
    else if (tagName.equals("notEmpty"))
    {
      String bean = attrs.getValue("bean");
      String var= attrs.getValue("element");
      if (var == null)
      {
        throw new ParseException("Missing attribute: var");
      }
      NotEmptyTemplate template = new NotEmptyTemplate(bean, var);
      this.template.addTemplate(template);
      getParser().pushFactory(new TemplatesElementHandler(getParser(), template));
    }
    else if (tagName.equals("foreach"))
    {
      String bean = attrs.getValue("bean");
      String asBean = attrs.getValue("as");
      String var= attrs.getValue("element");
      if (var == null)
      {
        throw new ParseException("Missing attribute: var");
      }
      ForEachTemplate template = new ForEachTemplate(bean, var, asBean);
      this.template.addTemplate(template);
      getParser().pushFactory(new TemplatesElementHandler(getParser(), template));
    }
    else if (tagName.equals("equals"))
    {
      String bean = attrs.getValue("bean");
      String value = attrs.getValue("value");
      if (value == null)
      {
        throw new ParseException("Missing attribute: value");
      }
      String var= attrs.getValue("var");
      if (var == null)
      {
        throw new ParseException("Missing attribute: var");
      }
      EqualsTemplate template = new EqualsTemplate(bean, var, value);
      this.template.addTemplate(template);
      getParser().pushFactory(new TemplatesElementHandler(getParser(), template));
    }
  }

  public void characters(char[] ch, int start, int length) throws SAXException
  {
    template.addTemplate (new ConstantTemplate(new String (ch, start, length)));
  }

  public void endElement(String tagName) throws SAXException
  {
    if (tagName.equals("notEmpty"))
    {
      getParser().popFactory();
    }
    else if (tagName.equals("foreach"))
    {
      getParser().popFactory();
    }
    else if (tagName.equals("equals"))
    {
      getParser().popFactory();
    }
  }

}
