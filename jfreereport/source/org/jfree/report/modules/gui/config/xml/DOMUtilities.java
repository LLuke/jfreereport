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
 * DOMUtilities.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * 27.08.2003 : Initial version
 *  
 */

package org.jfree.report.modules.gui.config.xml;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.jfree.report.util.CharacterEntityParser;

public class DOMUtilities
{
  private static final CharacterEntityParser XML_ENTITIES =
    CharacterEntityParser.createXMLEntityParser();
  /**
   * Parses the given input stream to form a document.
   *
   * @return the parsed document or <code>null</code>, when an error occured
   */
  public static Document parseInputStream (InputStream instream)
    throws ParserConfigurationException, SAXException, IOException
  {
    DocumentBuilder db;
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    db = dbf.newDocumentBuilder();
    return db.parse(new InputSource(instream));
  }

  /**
   * extracts all text-elements of a particular element and returns
   * an single string containing the contents of all textelements and
   * all character entity nodes. If a node is not known to the parser,
   * its string value will be delivered as <code>&entityname;</code>.
   *
   * @param e the element which is direct parent of all to be extracted
   * textnodes.
   * @return the extracted String
   */
  public static String getText(Element e)
  {
    NodeList nl = e.getChildNodes();
    StringBuffer result = new StringBuffer();

    for (int i = 0; i < nl.getLength(); i++)
    {
      Node n = nl.item(i);
      if (n.getNodeType() == Node.TEXT_NODE)
      {
        Text text = (Text) n;

        result.append(text.getData());
      }
      else if (n.getNodeType() == Node.ENTITY_REFERENCE_NODE)
      {
        result.append("&" + n.getNodeName() + ";");
      }
    }
    return XML_ENTITIES.decodeEntities(result.toString());
  }

}
