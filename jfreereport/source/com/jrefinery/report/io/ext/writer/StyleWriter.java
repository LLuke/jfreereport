/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * $Id: StyleWriter.java,v 1.16 2003/05/30 16:57:53 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2003 : Added standard header and Javadocs (DG);
 *
 */
package com.jrefinery.report.io.ext.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.jrefinery.report.ShapeElement;
import com.jrefinery.report.io.ext.StyleSheetHandler;
import com.jrefinery.report.targets.style.BandDefaultStyleSheet;
import com.jrefinery.report.targets.style.ElementDefaultStyleSheet;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.targets.style.StyleKey;
import org.jfree.xml.factory.objects.ClassFactoryCollector;
import org.jfree.xml.factory.objects.ObjectDescription;
import org.jfree.xml.factory.objects.ObjectFactoryException;

/**
 * A style writer.
 *
 * @author Thomas Morgner.
 */
public class StyleWriter extends AbstractXMLDefinitionWriter
{
  /** The element style sheet. */
  private ElementStyleSheet elementStyleSheet;

  /** The default style sheet. */
  private ElementStyleSheet defaultStyleSheet;

  /**
   * Creates a new writer.
   *
   * @param reportWriter  the report writer.
   * @param elementStyleSheet  the element style sheet.
   * @param defaultStyleSheet  the default style sheet.
   * @param indentLevel the current indention level.
   */
  public StyleWriter(ReportWriter reportWriter,
                     ElementStyleSheet elementStyleSheet,
                     ElementStyleSheet defaultStyleSheet,
                     int indentLevel)
  {
    super(reportWriter, indentLevel);
    this.elementStyleSheet = elementStyleSheet;
    this.defaultStyleSheet = defaultStyleSheet;
  }

  /**
   * Writes the style.
   *
   * @param writer  the character stream writer.
   *
   * @throws IOException if there is an I/O problem.
   * @throws ReportWriterException if there is a problem writing the report.
   */
  public void write(Writer writer) throws IOException, ReportWriterException
  {
    List parents = elementStyleSheet.getParents();
    for (int p = 0; p < parents.size(); p++)
    {
      ElementStyleSheet parent = (ElementStyleSheet) parents.get(p);
      if (isDefaultStyleSheet(parent) == false)
      {
        writeTag(writer, StyleSheetHandler.EXTENDS_TAG, "name", parent.getName(), CLOSE);
      }
    }

    Iterator keys = elementStyleSheet.getDefinedPropertyNames();
    while (keys.hasNext())
    {
      StyleKey key = (StyleKey) keys.next();
      Object value = elementStyleSheet.getStyleProperty(key);
      if (value != null)
      {
        writeKeyValue(writer, key, value);
      }
    }
  }

  /**
   * Tries to find an object description suitable for the given stylekey type.
   * If first tries to find an implementation which matches the given object,
   * if that fails, it tries to find a description for the key types.
   * If this also fails, the method starts to search for super class descriptions
   * for the key and the object.
   *
   * @param key the stylekey.
   * @param o the stylekey value.
   * @return the found object description or null, if none was found.
   */
  private ObjectDescription findObjectDescription (StyleKey key, Object o)
  {
    ClassFactoryCollector cc = getReportWriter().getClassFactoryCollector();
    ObjectDescription od = cc.getDescriptionForClass(o.getClass());
    if (od != null)
    {
      return od;
    }
/*
    Log.debug
        ("Unable to find object description for implemented style key class: " + o.getClass());
*/
    od = cc.getDescriptionForClass(key.getValueType());
    if (od != null)
    {
      return od;
    }
/*
    Log.debug
        ("Unable to find object description for native style key class: " + key.getValueType());
*/
    // search the most suitable super class object description ...
    od = cc.getSuperClassObjectDescription(o.getClass(), od);
    od = cc.getSuperClassObjectDescription(key.getValueType(), od);
//    Log.debug ("Object description result: " + od);
    return od;
  }

  /**
   * Writes a stylekey.
   *
   * @param w  the character stream writer.
   * @param key  the key.
   * @param o  the object.
   *
   * @throws IOException if there is an I/O problem.
   * @throws ReportWriterException if there is a problem writing the report.
   */
  private void writeKeyValue (Writer w, StyleKey key, Object o)
    throws IOException, ReportWriterException
  {
    ObjectDescription od = findObjectDescription(key, o);
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
      throw new ReportWriterException ("Unable to fill the parameters for key: "
                                       + key.getName(), e);
    }

    Properties p = new Properties();
    p.setProperty("name", key.getName());
    if ((key.getValueType().equals(o.getClass())) == false)
    {
      p.setProperty("class", o.getClass().getName());
    }

    List parameterNames = getParameterNames(od);
    if (isBasicKey(parameterNames, od))
    {
      writeTag(w, StyleSheetHandler.BASIC_KEY_TAG, p, OPEN);
      w.write(normalize((String) od.getParameter("value")));
      writeCloseTag(w, StyleSheetHandler.BASIC_KEY_TAG);
    }
    else
    {
      writeTag(w, StyleSheetHandler.COMPOUND_KEY_TAG, p, OPEN);
      ObjectWriter objWriter = new ObjectWriter(getReportWriter(), o, od, getIndentLevel());
      objWriter.write(w);
      writeCloseTag(w, StyleSheetHandler.COMPOUND_KEY_TAG);
    }
  }

  /**
   * Returns <code>true</code> if this is a basic key, and <code>false</code> otherwise.
   *
   * @param parameters  the parameters.
   * @param od  the object description.
   *
   * @return A boolean.
   */
  private boolean isBasicKey(List parameters, ObjectDescription od)
  {
    if (parameters.size() == 1)
    {
      String param = (String) parameters.get(0);
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
   * @param d  the object description.
   *
   * @return The list.
   */
  private ArrayList getParameterNames (ObjectDescription d)
  {
    ArrayList list = new ArrayList();
    Iterator it = d.getParameterNames();
    while (it.hasNext())
    {
      String name = (String) it.next();
      list.add(name);
    }
    return list;
  }

  /**
   * Returns <code>true</code> if the style sheet is the default, and <code>false</code> otherwise.
   *
   * @param es  the style sheet.
   *
   * @return A boolean.
   */
  private boolean isDefaultStyleSheet (ElementStyleSheet es)
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
    if (es == defaultStyleSheet)
    {
      return true;
    }
    return false;
  }
}
