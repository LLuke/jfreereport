/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * ------------------------------
 * ConfigDescriptionModel.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ConfigDescriptionModel.java,v 1.1 2003/08/27 20:19:53 taqua Exp $
 *
 * Changes
 * -------------------------
 * 26.08.2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Properties;
import javax.swing.AbstractListModel;
import javax.xml.parsers.ParserConfigurationException;

import org.jfree.report.modules.gui.config.xml.DOMUtilities;
import org.jfree.report.modules.gui.config.xml.DOMWriter;
import org.jfree.report.util.CharacterEntityParser;
import org.jfree.report.util.ReportConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConfigDescriptionModel extends AbstractListModel
{
  private static class ConfigEntryComparator implements Comparator
  {
    /**
     * Compares its two arguments for order.  Returns a negative integer,
     * zero, or a positive integer as the first argument is less than, equal
     * to, or greater than the second.<p>
     *
     */
    public int compare(Object o1, Object o2)
    {
      if (o1 == null && o2 == null)
      {
        return 0;
      }
      if (o1 == null)
      {
        return -1;
      }
      if (o2 == null)
      {
        return 1;
      }
      ConfigDescriptionEntry e1 = (ConfigDescriptionEntry) o1;
      ConfigDescriptionEntry e2 = (ConfigDescriptionEntry) o2;
      return e1.getKeyName().compareTo(e2.getKeyName());
    }
  }

  private ArrayList content;

  public ConfigDescriptionModel()
  {
    content = new ArrayList();
  }

  public void add (ConfigDescriptionEntry entry)
  {
    content.add (entry);
    updated();
  }

  public void remove (ConfigDescriptionEntry entry)
  {
    content.remove(entry);
    updated();
  }

  public ConfigDescriptionEntry get (int pos)
  {
    return (ConfigDescriptionEntry) content.get(pos);
  }

  public void updated ()
  {
    fireContentsChanged(this, 0, getSize());
  }

  public int indexOf (ConfigDescriptionEntry entry)
  {
    return content.indexOf(entry);
  }

  public boolean contains (ConfigDescriptionEntry entry)
  {
    return content.contains(entry);
  }

  public void sort ()
  {
    Collections.sort(content, new ConfigEntryComparator());
  }

  /**
   * Returns the length of the list.
   * @return the length of the list
   */
  public int getSize()
  {
    return content.size();
  }

  /**
   * Returns the value at the specified index.
   * @param index the requested index
   * @return the value at <code>index</code>
   */
  public Object getElementAt(int index)
  {
    ConfigDescriptionEntry entry = get (index);
    if (entry == null)
    {
      return null;
    }
    return entry.getKeyName();
  }

  public void importFromConfig (ReportConfiguration config)
  {
    Iterator it = config.findPropertyKeys("");
    while (it.hasNext())
    {
      String keyname = (String) it.next();
      TextConfigDescriptionEntry entry = new TextConfigDescriptionEntry(keyname);
      if (contains(entry) == false)
      {
        add(entry);
      }
    }
  }

  public void load (InputStream in)
      throws IOException, SAXException, ParserConfigurationException
  {
    Document doc = DOMUtilities.parseInputStream(in);
    Element e = doc.getDocumentElement();
    NodeList list = e.getElementsByTagName("key");
    for (int i = 0; i < list.getLength(); i++)
    {
      Element keyElement = (Element) list.item(i);
      String keyName = keyElement.getAttribute("name");
      boolean keyGlobal = parseBoolean (keyElement.getAttribute("global"), false);
      String descr = getDescription(keyElement);

      NodeList enumNodes = keyElement.getElementsByTagName("enum");
      if (enumNodes.getLength() != 0)
      {
        String[] alteratives = collectEnumEntries((Element) enumNodes.item(0));
        EnumConfigDescriptionEntry en = new EnumConfigDescriptionEntry(keyName);
        en.setDescription(descr);
        en.setGlobal(keyGlobal);
        en.setOptions(alteratives);
        add(en);
        continue;
      }

      NodeList classNodes = keyElement.getElementsByTagName("class");
      if (classNodes.getLength() != 0)
      {
        Element classElement = (Element) classNodes.item(0);
        String className = classElement.getAttribute("instanceof");
        if (className == null)
        {
          throw new SAXException("class element: instanceof attribute missing.");
        }
        Class baseClass;
        try
        {
          baseClass = this.getClass().getClassLoader().loadClass(className);
        }
        catch (Exception ex)
        {
          throw new SAXException("Failed to load base class", ex);
        }
        ClassConfigDescriptionEntry ce = new ClassConfigDescriptionEntry(keyName);
        ce.setBaseClass(baseClass);
        ce.setDescription(descr);
        ce.setGlobal(keyGlobal);
        add(ce);
        continue;
      }

      NodeList textNodes = keyElement.getElementsByTagName("text");
      if (textNodes.getLength() != 0)
      {
        TextConfigDescriptionEntry textEntry = new TextConfigDescriptionEntry(keyName);
        textEntry.setDescription(descr);
        textEntry.setGlobal(keyGlobal);
        add(textEntry);
        continue;
      }
    }
  }

  private String[] collectEnumEntries(Element element)
  {
    NodeList nl = element.getElementsByTagName("text");
    String[] retval = new String[nl.getLength()];
    for (int i = 0; i < nl.getLength(); i++)
    {
      retval[i] = DOMUtilities.getText((Element) nl.item(i));
    }
    return retval;
  }

  private String getDescription (Element e)
  {
    NodeList descr = e.getElementsByTagName("description");
    if (descr.getLength() == 0)
    {
      return "";
    }
    return DOMUtilities.getText((Element) descr.item(0));
  }
  private boolean parseBoolean (String attribute, boolean defaultValue)
  {
    if (attribute == null)
    {
      return defaultValue;
    }
    if (attribute.equals("true"))
    {
      return true;
    }
    return false;
  }

  public void save (OutputStream out, String encoding) throws IOException
  {
    PrintWriter writer = new PrintWriter(new OutputStreamWriter (out, encoding));
    writer.println("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>");
    writer.println("<!DOCTYPE config-description [");
    writer.println("<!ELEMENT config-description  (key*)>");
    writer.println("<!ELEMENT key                 (description?, (class | enum | text))>");
    writer.println("<!ATTLIST key");
    writer.println("   name   CDATA #REQUIRED");
    writer.println("   global CDATA #IMPLIED");
    writer.println(" >");
    writer.println();
    writer.println("<!ELEMENT description         (#PCDATA)>");
    writer.println("<!ELEMENT class               (EMPTY)>");
    writer.println("<!ATTLIST class");
    writer.println("  instanceof CDATA #REQUIRED");
    writer.println(" >");
    writer.println();
    writer.println("<!ELEMENT enum               (text)*>");
    writer.println("<!ELEMENT text               (#PCDATA)>");
    writer.println(" ]>");
    DOMWriter dwriter = new DOMWriter();
    dwriter.writeTag(writer, "config-description");

    CharacterEntityParser parser = CharacterEntityParser.createXMLEntityParser();
    for (int i = 0; i < getSize(); i++)
    {
      ConfigDescriptionEntry entry = get(i);
      Properties p = new Properties();
      p.setProperty("name", entry.getKeyName());
      p.setProperty("global", String.valueOf(entry.isGlobal()));
      dwriter.writeTag(writer, "key", p, false);
      if (entry.getDescription() != null)
      {
        dwriter.writeTag(writer, "description");
        writer.write(parser.encodeEntities(entry.getDescription()));
        dwriter.writeCloseTag(writer, "description");
      }
      if (entry instanceof ClassConfigDescriptionEntry)
      {
        ClassConfigDescriptionEntry ce = (ClassConfigDescriptionEntry) entry;
        if (ce.getBaseClass() != null)
        {
          dwriter.writeTag
            (writer, "class", "instanceof", ce.getBaseClass().getName(), DOMWriter.CLOSE);
        }
        else
        {
          dwriter.writeTag
            (writer, "class", "instanceof", "java.lang.Object", DOMWriter.CLOSE);
        }
      }
      else if (entry instanceof TextConfigDescriptionEntry)
      {
        dwriter.writeTag(writer, "text", new Properties(), DOMWriter.CLOSE);
      }
      else if (entry instanceof EnumConfigDescriptionEntry)
      {
        EnumConfigDescriptionEntry en = (EnumConfigDescriptionEntry) entry;
        dwriter.writeTag(writer, "enum");

        String[] alts = en.getOptions();
        if (alts != null)
        {
          for (int optCount = 0; optCount < alts.length; optCount++)
          {
            dwriter.writeTag (writer, "text");
            writer.write(parser.encodeEntities(alts[optCount]));
            dwriter.writeCloseTag(writer, "text");
          }
        }
        dwriter.writeCloseTag(writer, "enum");
      }
      dwriter.writeCloseTag(writer, "key");
    }
    dwriter.writeCloseTag(writer, "config-description");
    writer.flush();
  }
}
