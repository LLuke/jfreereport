package org.jfree.report.modules.parser.ext.readhandlers;

import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateCollection;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateDescription;
import org.jfree.xml.ParseException;
import org.jfree.xml.parser.RootXmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public class TemplateReadHandler extends CompoundObjectReadHandler
{
  private TemplateCollection templateCollection;
  private boolean nameRequired;

  public TemplateReadHandler (final boolean nameRequired,
                              final CommentHintPath commentHintPath)
  {
    super(null, commentHintPath);
    this.nameRequired = nameRequired;
  }


  /**
   * Initialises the handler.
   *
   * @param rootHandler the root handler.
   * @param tagName     the tag name.
   */
  public void init (final RootXmlReadHandler rootHandler,
                    final String tagName)
  {
    super.init(rootHandler, tagName);
    templateCollection = (TemplateCollection) rootHandler.getHelperObject
            (ReportDefinitionReadHandler.TEMPLATE_FACTORY_KEY);
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final PropertyAttributes attrs)
          throws SAXException, XmlReaderException
  {
    final String templateName = attrs.getValue("name");
    if (nameRequired && templateName == null)
    {
      throw new ParseException("The 'name' attribute is required for template definitions",
              getRootHandler().getLocator());
    }
    final String references = attrs.getValue("references");
    if (references == null)
    {
      throw new ParseException("The 'references' attribute is required for template definitions",
              getRootHandler().getLocator());
    }
    TemplateDescription template = templateCollection.getTemplate(references);
    if (template == null)
    {
      throw new ParseException("The template '" + references + "' is not defined",
              getRootHandler().getLocator());
    }

    // Clone the defined template ... we don't change the original ..
    template = (TemplateDescription) template.getInstance();
    if (templateName != null)
    {
      template.setName(templateName);
      templateCollection.addTemplate(template);
      // if this template is a global template, store it by its name..
      if (nameRequired)
      {
        getCommentHintPath().addName(templateName);
      }
    }
    setObjectDescription(template);
  }
}
