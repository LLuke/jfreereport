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
 * StyleReadHandler.java
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

import java.util.ArrayList;
import java.util.Iterator;

import org.jfree.report.JFreeReport;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.report.modules.parser.ext.factory.stylekey.StyleKeyFactory;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.StyleKey;
import org.jfree.report.style.StyleSheetCollection;
import org.jfree.util.Configuration;
import org.jfree.xml.ElementDefinitionException;
import org.jfree.xml.factory.objects.ObjectDescription;
import org.jfree.xml.factory.objects.ObjectFactoryException;
import org.jfree.xml.parser.RootXmlReadHandler;
import org.jfree.xml.parser.XmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public class StyleReadHandler extends CompoundObjectReadHandler
{
  private static class ElementStyleSheetObjectDescription implements ObjectDescription
  {
    private StyleKeyFactory keyfactory;
    private ElementStyleSheet styleSheet;

    public ElementStyleSheetObjectDescription ()
    {
    }

    /**
     * Configures this factory. The configuration contains several keys and their defined
     * values. The given reference to the configuration object will remain valid until the
     * report parsing or writing ends.
     * <p/>
     * The configuration contents may change during the reporting.
     *
     * @param config the configuration, never null
     */
    public void configure (final Configuration config)
    {
    }

    public void init (final RootXmlReadHandler rootHandler,
                      final ElementStyleSheet styleSheet)
    {
      this.keyfactory = (StyleKeyFactory)
              rootHandler.getHelperObject(ReportDefinitionReadHandler.STYLE_FACTORY_KEY);
      this.styleSheet = styleSheet;

    }

    /**
     * Creates an object based on the description.
     *
     * @return The object.
     */
    public Object createObject ()
    {
      return styleSheet;
    }

    /**
     * Returns a cloned instance of the object description. The contents of the parameter
     * objects collection are cloned too, so that any already defined parameter value is
     * copied to the new instance.
     * <p/>
     * Parameter definitions are not cloned, as they are considered read-only.
     *
     * @return A cloned instance.
     */
    public ObjectDescription getInstance ()
    {
      throw new UnsupportedOperationException("This is a private factory, go away.");
    }

    /**
     * Returns the object class.
     *
     * @return The Class.
     */
    public Class getObjectClass ()
    {
      return ElementStyleSheet.class;
    }

    /**
     * Returns the value of a parameter.
     *
     * @param name the parameter name.
     * @return The value.
     */
    public Object getParameter (final String name)
    {
      final StyleKey key = keyfactory.getStyleKey(name);
      return styleSheet.getStyleProperty(key);
    }

    /**
     * Returns a parameter definition. If the parameter is invalid, this function returns
     * null.
     *
     * @param name the definition name.
     * @return The parameter class or null, if the parameter is not defined.
     */
    public Class getParameterDefinition (final String name)
    {
      final StyleKey key = keyfactory.getStyleKey(name);
      return key.getValueType();
    }

    /**
     * Returns an iterator the provides access to the parameter names. This returns all
     * _known_ parameter names, the object description may accept additional parameters.
     *
     * @return The iterator.
     */
    public Iterator getParameterNames ()
    {
      // don't say anything ...
      return new ArrayList().iterator();
    }

    /**
     * Returns a cloned instance of the object description. The contents of the parameter
     * objects collection are cloned too, so that any already defined parameter value is
     * copied to the new instance.
     * <p/>
     * Parameter definitions are not cloned, as they are considered read-only.
     * <p/>
     * The newly instantiated object description is not configured. If it need to be
     * configured, then you have to call configure on it.
     *
     * @return A cloned instance.
     */
    public ObjectDescription getUnconfiguredInstance ()
    {
      throw new UnsupportedOperationException("This is a private factory, go away.");
    }

    /**
     * Sets the value of a parameter.
     *
     * @param name  the parameter name.
     * @param value the parameter value.
     */
    public void setParameter (final String name, final Object value)
    {
      final StyleKey key = keyfactory.getStyleKey(name);
      styleSheet.setStyleProperty(key, value);
    }

    /**
     * Sets the parameters of this description object to match the supplied object.
     *
     * @param o the object.
     * @throws org.jfree.xml.factory.objects.ObjectFactoryException
     *          if there is a problem while reading the properties of the given object.
     */
    public void setParameterFromObject (final Object o)
            throws ObjectFactoryException
    {
      throw new UnsupportedOperationException("This is a private factory, go away.");
    }
  }

  private StyleSheetCollection styleSheetCollection;
  private ElementStyleSheet styleSheet;
  private boolean createStyle;

  public StyleReadHandler ()
  {
    this(null);
  }

  public StyleReadHandler (final ElementStyleSheet styleSheet)
  {
    super(new ElementStyleSheetObjectDescription(), new CommentHintPath());
    this.styleSheet = styleSheet;
    this.createStyle = (styleSheet == null);
  }

  /**
   * Initialises the handler.
   *
   * @param rootHandler the root handler.
   * @param tagName     the tag name.
   */
  public void init (final RootXmlReadHandler rootHandler, final String tagName)
  {
    super.init(rootHandler, tagName);
    final JFreeReport report = (JFreeReport)
            rootHandler.getHelperObject(ReportDefinitionReadHandler.REPORT_KEY);
    styleSheetCollection = report.getStyleSheetCollection();
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
    if (createStyle)
    {
      final String name = attrs.getValue("name");
      if (name == null)
      {
        throw new ElementDefinitionException
                ("Required attribute 'name' is missing.", getRootHandler().getLocator());
      }
      styleSheet = styleSheetCollection.createStyleSheet(name);
    }

    getCommentHintPath().addName(styleSheet);

    final ElementStyleSheetObjectDescription objectDescription =
            (ElementStyleSheetObjectDescription) getObjectDescription();
    objectDescription.init(getRootHandler(), styleSheet);
  }

  /**
   * Returns the handler for a child element.
   *
   * @param tagName the tag name.
   * @param atts    the attributes.
   * @return the handler or null, if the tagname is invalid.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   * @throws org.jfree.xml.parser.XmlReaderException
   *                                  if there is a reader error.
   */
  protected XmlReadHandler getHandlerForChild (final String tagName,
                                               final PropertyAttributes atts)
          throws XmlReaderException, SAXException
  {
    if (tagName.equals("extends"))
    {
      return new StyleExtendsReadHandler(styleSheetCollection, styleSheet);
    }
    else if (tagName.equals("basic-key"))
    {
      return handleBasicObject(atts);
    }
    else if (tagName.equals("compound-key"))
    {
      return handleCompoundObject(atts);
    }
    return null;
  }

  /**
   * Returns the object for this element or null, if this element does not create an
   * object.
   *
   * @return the object.
   *
   * @throws org.jfree.xml.parser.XmlReaderException
   *          if there is a parsing error.
   */
  public Object getObject ()
          throws XmlReaderException
  {
    return styleSheet;
  }
}
