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
 * CompoundObjectReadHandler.java
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.xml.ElementDefinitionException;
import org.jfree.xml.factory.objects.ClassFactory;
import org.jfree.xml.factory.objects.ObjectDescription;
import org.jfree.xml.parser.XmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class CompoundObjectReadHandler extends BasicObjectReadHandler
{
  private HashMap basicObjects;
  private HashMap compoundObjects;

  public CompoundObjectReadHandler (final ObjectDescription objectDescription,
                                    final CommentHintPath commentPath)
  {
    super(objectDescription, commentPath);
    basicObjects = new HashMap();
    compoundObjects = new HashMap();
  }

  protected HashMap getBasicObjects ()
  {
    return basicObjects;
  }

  protected HashMap getCompoundObjects ()
  {
    return compoundObjects;
  }

  /**
   * Done parsing.
   *
   * @throws org.xml.sax.SAXException if there is a parsing error.
   * @throws org.jfree.xml.parser.XmlReaderException
   *                                  if there is a reader error.
   */
  protected void doneParsing ()
          throws SAXException, XmlReaderException
  {
    final ObjectDescription objectDescription = getObjectDescription();
    final Iterator basicObjectsEntries = basicObjects.entrySet().iterator();
    while (basicObjectsEntries.hasNext())
    {
      final Map.Entry entry = (Map.Entry) basicObjectsEntries.next();
      final String name = (String) entry.getKey();
      final BasicObjectReadHandler readHandler = (BasicObjectReadHandler) entry.getValue();
      objectDescription.setParameter(name, readHandler.getObject());
    }

    final Iterator compoundObjectsEntries = compoundObjects.entrySet().iterator();
    while (compoundObjectsEntries.hasNext())
    {
      final Map.Entry entry = (Map.Entry) compoundObjectsEntries.next();
      final String name = (String) entry.getKey();
      final CompoundObjectReadHandler readHandler = (CompoundObjectReadHandler) entry.getValue();
      objectDescription.setParameter(name, readHandler.getObject());
    }

    storeCloseComments();
    storeComments();
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
    if (tagName.equals("basic-object"))
    {
      return handleBasicObject(atts);
    }
    else if (tagName.equals("compound-object"))
    {
      return handleCompoundObject(atts);
    }
    return null;
  }

  protected XmlReadHandler handleBasicObject (final Attributes atts)
          throws ElementDefinitionException
  {
    final String name = atts.getValue("name");
    if (name == null)
    {
      throw new ElementDefinitionException
              ("Required attribute 'name' is missing.");
    }

    final ClassFactory fact = getClassFactory();
    final ObjectDescription objectDescription =
            ObjectFactoryUtility.findDescription
            (fact, getObjectDescription().getParameterDefinition(name));

    final CommentHintPath path = getCommentHintPath().getInstance();
    path.addName(name);
    final BasicObjectReadHandler readHandler =
            new BasicObjectReadHandler(objectDescription, path);
    basicObjects.put(name, readHandler);
    return readHandler;
  }

  protected XmlReadHandler handleCompoundObject (final Attributes atts)
          throws ElementDefinitionException
  {
    final String name = atts.getValue("name");
    if (name == null)
    {
      throw new ElementDefinitionException
              ("Required attribute 'name' is missing.");
    }

    final ClassFactory fact = getClassFactory();
    final ObjectDescription objectDescription =
            ObjectFactoryUtility.findDescription
            (fact, getObjectDescription().getParameterDefinition(name));

    final CommentHintPath path = getCommentHintPath().getInstance();
    path.addName(name);

    final CompoundObjectReadHandler readHandler =
            new CompoundObjectReadHandler(objectDescription, path);
    compoundObjects.put(name, readHandler);
    return readHandler;
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
    handleStartParsing(attrs);
  }
}
