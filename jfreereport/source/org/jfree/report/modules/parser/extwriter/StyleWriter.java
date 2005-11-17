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
 * ----------------
 * StyleWriter.java
 * ----------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StyleWriter.java,v 1.14 2005/02/23 21:05:56 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */
package org.jfree.report.modules.parser.extwriter;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jfree.report.ShapeElement;
import org.jfree.report.modules.parser.base.CommentHintPath;
import org.jfree.report.style.BandDefaultStyleSheet;
import org.jfree.report.style.ElementDefaultStyleSheet;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.StyleKey;
import org.jfree.util.ObjectUtilities;
import org.jfree.xml.CommentHandler;
import org.jfree.xml.factory.objects.ClassFactoryCollector;
import org.jfree.xml.factory.objects.ObjectDescription;
import org.jfree.xml.factory.objects.ObjectFactoryException;
import org.jfree.xml.writer.AttributeList;

/**
 * A style writer. This class will write a single stylesheet into the writer.
 *
 * @author Thomas Morgner.
 */
public class StyleWriter extends AbstractXMLDefinitionWriter
{
  /**
   * The element style sheet.
   */
  private ElementStyleSheet elementStyleSheet;

//  /** The default style sheet. */
//  private ElementStyleSheet defaultStyleSheet;

  /**
   * The comment hint path is used to read xml comments from the report builder hints
   * collection.
   */
  private CommentHintPath commentPath;

  /**
   * Creates a new writer.
   *
   * @param reportWriter      the report writer.
   * @param elementStyleSheet the element style sheet (never null).
   * @param indentLevel       the current indention level.
   * @param commentPath       the path on where to search for ext-parser comments in the
   *                          report builder hints.
   */
  public StyleWriter (final ReportWriter reportWriter,
                      final ElementStyleSheet elementStyleSheet,
                      final int indentLevel,
                      final CommentHintPath commentPath)
  {
    super(reportWriter, indentLevel);
    if (elementStyleSheet == null)
    {
      throw new NullPointerException();
    }
    this.elementStyleSheet = elementStyleSheet;
    this.commentPath = commentPath.getInstance();
  }

  /**
   * Writes the style sheet.
   *
   * @param writer the character stream writer.
   * @throws IOException           if there is an I/O problem.
   * @throws ReportWriterException if there is a problem writing the report.
   */
  public void write (final Writer writer)
          throws IOException, ReportWriterException
  {
    final ElementStyleSheet[] parents = elementStyleSheet.getParents();
    // write the parents of the stylesheet ...
    for (int p = 0; p < parents.length; p++)
    {
      final ElementStyleSheet parent = parents[p];
      if (isDefaultStyleSheet(parent) == false)
      {
        final CommentHintPath extendsPath = commentPath.getInstance();
        extendsPath.addName(parent.getName());
        writeComment(writer, extendsPath, CommentHandler.OPEN_TAG_COMMENT);
        writeTag(writer, EXTENDS_TAG, "name", parent.getName(), CLOSE);
      }
    }

    // now write all defined properties of the stylesheet ...
    // this will not write ihnerited values, only the ones defined in this instance.
    final Iterator keys = elementStyleSheet.getDefinedPropertyNames();
    while (keys.hasNext())
    {
      final StyleKey key = (StyleKey) keys.next();
      if (key.isTransient() == false)
      {
        final Object value = elementStyleSheet.getStyleProperty(key);
        if (value != null)
        {
          writeKeyValue(writer, key, value);
        }
      }
    }
  }

  /**
   * Tries to find an object description suitable for the given stylekey type. If first
   * tries to find an implementation which matches the given object, if that fails, it
   * tries to find a description for the key types. If this also fails, the method starts
   * to search for super class descriptions for the key and the object.
   *
   * @param key the stylekey.
   * @param o   the stylekey value.
   * @return the found object description or null, if none was found.
   */
  private ObjectDescription findObjectDescription (final StyleKey key, final Object o)
  {
    final ClassFactoryCollector cc = getReportWriter().getClassFactoryCollector();
    // search an direct definition for the given object class ...
    ObjectDescription od = cc.getDescriptionForClass(o.getClass());
    if (od != null)
    {
      return od;
    }

    // now search an definition for the stylekey class ...
    od = cc.getDescriptionForClass(key.getValueType());
    // todo change here : removed return od if od <> null
    // and use this as best known result when searching for super class object
    // descriptions. ...

    // search the most suitable super class object description for the object
    // and the key ...
    od = cc.getSuperClassObjectDescription(o.getClass(), od);
    od = cc.getSuperClassObjectDescription(key.getValueType(), od);

    // if it is still null now, then we do not know anything about this object type.
    return od;
  }

  /**
   * Checks, whether this key object would use the default object description for this key
   * type. If this method returns true, the object class can be omitted in the xml
   * definition.
   *
   * @param key the style key that should be used as base
   * @param o   the value object for this key type.
   * @return true, of the object can be described using the default object description,
   *         false otherwise.
   */
  private boolean isUseKeyObjectDescription
          (final StyleKey key, final Object o)
  {
    final ClassFactoryCollector cc = getReportWriter().getClassFactoryCollector();
    ObjectDescription odObject = cc.getDescriptionForClass(o.getClass());
    ObjectDescription odKey = cc.getDescriptionForClass(key.getValueType());

    // search the most suitable super class object description ...
    if (odObject == null)
    {
      odObject = cc.getSuperClassObjectDescription(o.getClass(), odObject);
    }
    if (odKey == null)
    {
      odKey = cc.getSuperClassObjectDescription(key.getValueType(), odKey);
    }
    return ObjectUtilities.equal(odKey, odObject);
  }

  /**
   * Writes a stylekey.
   *
   * @param w   the character stream writer.
   * @param key the style key that should be written.
   * @param o   the object that was stored at that key.
   * @throws IOException           if there is an I/O problem.
   * @throws ReportWriterException if there is a problem writing the report.
   */
  private void writeKeyValue (final Writer w, final StyleKey key, final Object o)
          throws IOException, ReportWriterException
  {
    final ObjectDescription od = findObjectDescription(key, o);
    if (od == null)
    {
      throw new ReportWriterException("Unable to find object description for key: "
              + key.getName());
    }

    try
    {
      od.setParameterFromObject(o);
    }
    catch (ObjectFactoryException e)
    {
      throw new ReportWriterException("Unable to fill the parameters for key: "
              + key.getName(), e);
    }
    final StyleKey keyFromFactory = getReportWriter().getStyleKeyFactoryCollector()
            .getStyleKey(key.getName());
    if (keyFromFactory == null)
    {
      throw new ReportWriterException
              ("The stylekey " + key.getName() +
                      " has no corresponding key description.");
    }


    final AttributeList p = new AttributeList();
    p.setAttribute("name", key.getName());
    if (isUseKeyObjectDescription(key, o) == false)
    {
      p.setAttribute("class", o.getClass().getName());
    }

    final CommentHintPath styleKeyPath = commentPath.getInstance();
    styleKeyPath.addName(key);

    final List parameterNames = getParameterNames(od);
    if (isBasicKey(parameterNames, od))
    {
      writeComment(w, styleKeyPath, CommentHandler.OPEN_TAG_COMMENT);
      writeTag(w, BASIC_KEY_TAG, p, OPEN);
      w.write(normalize((String) od.getParameter("value")));
      writeComment(w, styleKeyPath, CommentHandler.CLOSE_TAG_COMMENT);
      writeCloseTag(w, BASIC_KEY_TAG);
    }
    else
    {
      writeComment(w, styleKeyPath, CommentHandler.OPEN_TAG_COMMENT);
      writeTag(w, COMPOUND_KEY_TAG, p, OPEN);
      final ObjectWriter objWriter = new ObjectWriter
              (getReportWriter(), o, od, getIndentLevel(), styleKeyPath);
      objWriter.write(w);
      writeComment(w, styleKeyPath, CommentHandler.CLOSE_TAG_COMMENT);
      writeCloseTag(w, COMPOUND_KEY_TAG);
    }
  }

  /**
   * Returns <code>true</code> if this is a basic key, and <code>false</code> otherwise.
   *
   * @param parameters the parameters.
   * @param od         the object description.
   * @return A boolean.
   */
  private boolean isBasicKey (final List parameters, final ObjectDescription od)
  {
    if (parameters.size() == 1)
    {
      final String param = (String) parameters.get(0);
      if (param.equals("value"))
      {
        if (od.getParameterDefinition("value").equals(String.class))
        {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Returns a list of parameter names.
   *
   * @param d the object description.
   * @return The list.
   */
  private ArrayList getParameterNames (final ObjectDescription d)
  {
    final ArrayList list = new ArrayList();
    final Iterator it = d.getParameterNames();
    while (it.hasNext())
    {
      final String name = (String) it.next();
      list.add(name);
    }
    return list;
  }

  /**
   * Returns <code>true</code> if the style sheet is the default, and <code>false</code>
   * otherwise.
   *
   * @param es the style sheet.
   * @return A boolean.
   */
  private boolean isDefaultStyleSheet (final ElementStyleSheet es)
  {
    if (es == BandDefaultStyleSheet.getBandDefaultStyle())
    {
      return true;
    }
    if (es == ElementDefaultStyleSheet.getDefaultStyle())
    {
      return true;
    }
    if (es == ShapeElement.getDefaultStyle())
    {
      return true;
    }
    return false;
  }
}
