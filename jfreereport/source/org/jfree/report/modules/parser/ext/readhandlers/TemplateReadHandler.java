/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * TemplateReadHandler.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
 *
 * Changes
 * -------
 *
 *
 */
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
