/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * ----------------------------
 * ElementDefinitionHandler.java
 * ----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TemplatesHandler.java,v 1.8 2003/08/24 15:08:20 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */

package org.jfree.report.modules.parser.ext;

import org.jfree.report.modules.parser.base.CommentHandler;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.ReportParser;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateCollector;
import org.jfree.report.modules.parser.ext.factory.templates.TemplateDescription;
import org.jfree.xml.ParseException;
import org.jfree.xml.factory.objects.ObjectDescription;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A templates handler. The Templates collection can be used to predefine
 * common datasource definitions for elements. This allows the definition of
 * global format strings, f.i. for Number or Date fields.
 *
 * @author Thomas Morgner.
 * @see org.jfree.report.filter.templates.Template
 */
public class TemplatesHandler extends AbstractExtReportParserHandler
{
  /** the predefined comment hint path for all template definitions. */
  private static final CommentHintPath TEMPLATES_PATH = new CommentHintPath(new String[]{
    ExtParserModuleInit.REPORT_DEFINITION_TAG,
    ExtReportHandler.REPORT_DESCRIPTION_TAG,
    ExtReportHandler.TEMPLATES_TAG
  });


  /** The template tag. */
  public static final String TEMPLATE_TAG = "template";

  /** A template collector. */
  private final TemplateCollector templateCollector;

  /** A template handler. */
  private TemplateHandler templateFactory;

  /**
   * Creates a new templates handler.
   *
   * @param parser  the parser.
   * @param finishTag  the finish tag.
   */
  public TemplatesHandler(final ReportParser parser, final String finishTag)
  {
    super(parser, finishTag);
    templateCollector = (TemplateCollector)
        getParser().getHelperObject(ParserConfigHandler.TEMPLATE_FACTORY_TAG);
    if (templateCollector == null)
    {
      throw new IllegalStateException("No template collector defined for this parser?");
    }

  }

  /**
   * Callback to indicate that an XML element start tag has been read by the parser.
   *
   * @param tagName  the tag name.
   * @param attrs  the attributes.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public void startElement(final String tagName, final Attributes attrs)
      throws SAXException
  {
    if (tagName.equals(TEMPLATE_TAG) == false)
    {
      throw new ParseException("Expected tag '" + TEMPLATE_TAG + "'",
          getParser().getLocator());
    }

    final String templateName = attrs.getValue("name");
    if (templateName == null)
    {
      throw new ParseException("The 'name' attribute is required for template definitions",
          getParser().getLocator());
    }
    final String references = attrs.getValue("references");
    if (references == null)
    {
      throw new ParseException("The 'references' attribute is required for template definitions",
          getParser().getLocator());
    }
    TemplateDescription template = templateCollector.getTemplate(references);
    if (template == null)
    {
      throw new ParseException("The template '" + references + "' is not defined",
          getParser().getLocator());
    }

    // Clone the defined template ... we don't change the original ..
    template = (TemplateDescription) template.getInstance();
    template.setName(templateName);
    final ObjectDescription unconfiguredTemplate = template.getUnconfiguredInstance();
    getParserHints().putHint(unconfiguredTemplate, "ext.parser.template-reference", references);
    final CommentHintPath path = createPath(unconfiguredTemplate);
    addComment(path, CommentHandler.OPEN_TAG_COMMENT);
    templateFactory = new TemplateHandler
        (getReportParser(), TEMPLATE_TAG, template, path);
    getParser().pushFactory(templateFactory);
  }

  /**
   * Creates a new comment hint path for the given name by appending
   * it to a copy of the current path.
   *
   * @param tdesc the name of the new path segment.
   * @return the new comment path.
   */
  private CommentHintPath createPath(final ObjectDescription tdesc)
  {
    final CommentHintPath path = TEMPLATES_PATH.getInstance();
    path.addName(tdesc);
    return path;
  }

  /**
   * Callback to indicate that some character data has been read.
   *
   * @param ch  the character array.
   * @param start  the start index for the characters.
   * @param length  the length of the character sequence.
   */
  public void characters(final char[] ch, final int start, final int length)
  {
    // ignore characters ...
  }

  /**
   * Callback to indicate that an XML element end tag has been read by the parser.
   *
   * @param tagName  the tag name.
   *
   * @throws SAXException if a parser error occurs or the validation failed.
   */
  public void endElement(final String tagName)
      throws SAXException
  {
    if (tagName.equals(TEMPLATE_TAG))
    {
      final TemplateDescription template = templateFactory.getTemplate();
      final ObjectDescription unconfiguredTemplate = template.getUnconfiguredInstance();
      getParserHints().addHintList
          (getReport(), "ext.parser.template-definition", template.getUnconfiguredInstance());
      final CommentHintPath path = createPath(unconfiguredTemplate);
      addComment(path, CommentHandler.CLOSE_TAG_COMMENT);
      templateCollector.addTemplate(template);
    }
    else if (tagName.equals(getFinishTag()))
    {
      getParser().popFactory().endElement(tagName);
    }
    else
    {
      throw new ParseException("Wrong tag, expected one of " + getFinishTag() + ","
          + TEMPLATE_TAG, getParser().getLocator());
    }
  }
}
