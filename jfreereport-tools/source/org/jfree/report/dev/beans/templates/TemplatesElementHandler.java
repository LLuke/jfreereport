package org.jfree.report.dev.beans.templates;

import org.jfree.xml.AbstractElementDefinitionHandler;
import org.jfree.xml.ParseException;
import org.jfree.xml.Parser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class TemplatesElementHandler extends AbstractElementDefinitionHandler
{
  private CompoundTemplate template;

  public TemplatesElementHandler(final Parser parser, final CompoundTemplate template)
  {
    super(parser);
    this.template = template;
  }

  public void startElement(final String tagName, final Attributes attrs) throws SAXException
  {
    if (tagName.equals("print"))
    {
      final String var= attrs.getValue("var");
      final String bean = attrs.getValue("bean");
      final String style = attrs.getValue("style");
      template.addTemplate(new PrintVarTemplate(bean, var, "quoted".equals(style)));

    }
    else if (tagName.equals("notEmpty"))
    {
      final String bean = attrs.getValue("bean");
      final String var= attrs.getValue("element");
      if (var == null)
      {
        throw new ParseException("Missing attribute: var");
      }
      final NotEmptyTemplate template = new NotEmptyTemplate(bean, var);
      this.template.addTemplate(template);
      getParser().pushFactory(new TemplatesElementHandler(getParser(), template));
    }
    else if (tagName.equals("foreach"))
    {
      final String bean = attrs.getValue("bean");
      final String asBean = attrs.getValue("as");
      final String var= attrs.getValue("element");
      if (var == null)
      {
        throw new ParseException("Missing attribute: var");
      }
      final ForEachTemplate template = new ForEachTemplate(bean, var, asBean);
      this.template.addTemplate(template);
      getParser().pushFactory(new TemplatesElementHandler(getParser(), template));
    }
    else if (tagName.equals("equals"))
    {
      final String bean = attrs.getValue("bean");
      final String value = attrs.getValue("value");
      if (value == null)
      {
        throw new ParseException("Missing attribute: value");
      }
      final String var= attrs.getValue("var");
      if (var == null)
      {
        throw new ParseException("Missing attribute: var");
      }
      final EqualsTemplate template = new EqualsTemplate(bean, var, value);
      this.template.addTemplate(template);
      getParser().pushFactory(new TemplatesElementHandler(getParser(), template));
    }
  }

  public void characters(final char[] ch, final int start, final int length) throws SAXException
  {
    template.addTemplate (new ConstantTemplate(new String (ch, start, length)));
  }

  public void endElement(final String tagName) throws SAXException
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
