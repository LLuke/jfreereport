/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * -----------------------
 * GroupFactory.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * 10-May-2002 : Initial version
 */
package com.jrefinery.report.io;

import com.jrefinery.report.Band;
import com.jrefinery.report.Group;
import com.jrefinery.report.GroupFooter;
import com.jrefinery.report.GroupHeader;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.util.Log;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GroupFactory extends AbstractReportDefinitionHandler implements ReportDefinitionTags
{
  private JFreeReport report;
  private Group currentGroup;
  private StringBuffer currentText;
  private ReportDefinitionContentHandler handler;
  private FontFactory fontFactory;

  public GroupFactory (JFreeReport report, ReportDefinitionContentHandler handler)
  {
    this.report = report;
    this.handler = handler;
    fontFactory = handler.getFontFactory();
  }

  public void startElement (String namespaceURI,
                            String localName,
                            String qName,
                            Attributes atts) throws SAXException
  {
    String elementName = qName.toLowerCase ().trim ();
    // *** GROUP HEADER ***
    if (elementName.equals (GROUP_HEADER_TAG))
    {
      startGroupHeader (atts);
    }
    // *** GROUP FOOTER ***
    else if (elementName.equals (GROUP_FOOTER_TAG))
    {
      startGroupFooter (atts);
    }
    else if (elementName.equals (FIELDS_TAG))
    {
      // ignore me!
    }
    else if (elementName.equals (FIELD_TAG))
    {
      currentText = new StringBuffer();
    }
    else if (elementName.equals (GROUP_TAG))
    {
      startGroup(atts);
    }
    else
    {
      throw new SAXException ("Expected one of: group, groupfooter, groutheader, fields, field");
    }
  }

  /**
   * Receives some (or all) of the text in the current element.
   */
  public void characters (char[] ch, int start, int length)
  {
    if (currentText != null)
      this.currentText.append (String.copyValueOf (ch, start, length));
  }

  public void endElement (String namespaceURI, String localName, String qName)
          throws SAXException
  {
    String elementName = qName.toLowerCase ().trim ();
    // *** GROUP HEADER ***
    if (elementName.equals (GROUP_HEADER_TAG))
    {
    }
    // *** GROUP FOOTER ***
    else if (elementName.equals (GROUP_FOOTER_TAG))
    {
    }
    else if (elementName.equals (FIELDS_TAG))
    {
      // ignore me!
    }
    else if (elementName.equals (GROUP_TAG))
    {
      Log.debug ("Add Group: " + currentGroup.getName());
      Log.debug ("GroupHeader: " + currentGroup.getName() + " " + currentGroup.getHeader());
      Log.debug ("GroupFooter: " + currentGroup.getName() + " " + currentGroup.getFooter());
      report.addGroup(currentGroup);
      setCurrentGroup(null);
    }
    else if (elementName.equals (FIELD_TAG))
    {
      this.currentGroup.addField (this.currentText.toString());
      currentText = null;
    }
    else if (elementName.equals (GROUPS_TAG))
    {
      handler.finishedHandler ();
    }
    else
    {
      throw new SAXException ("Expected one of: group, groupfooter, groutheader, fields, field - but found " + elementName);
    }
  }

  public void startGroup (Attributes attr)
    throws SAXException
  {
    Group group = new Group ();
    group.setName (generateName (attr.getValue ("name")));
    setCurrentGroup(group);
  }

  public void startGroupHeader (Attributes attr)
          throws SAXException

  {
    // get the height...
    float height = parseFloat (attr.getValue ("height"), "Element height not specified");
    boolean pageBreak = parseBoolean(attr.getValue ("pagebreak"), false);
    // create the group header...
    GroupHeader groupHeader = new GroupHeader ();
    groupHeader.setHeight (height);
    groupHeader.setPageBreakBeforePrint(pageBreak);
    groupHeader.setDefaultFont (fontFactory.createDefaultFont (attr));
    currentGroup.setHeader (groupHeader);
    handler.setExpectedHandler(new ElementFactory(groupHeader, handler));
  }

  /**
   * Handles the start of a GROUPFOOTER element.
   */
  private void startGroupFooter (Attributes attr) throws SAXException
  {
    // get the height...
    float height = parseFloat (attr.getValue ("height"), "Element height not specified");

    // get the default font...
    // create the group footer...
    GroupFooter groupFooter = new GroupFooter ();
    groupFooter.setHeight (height);
    groupFooter.setDefaultFont (fontFactory.createDefaultFont (attr));
    currentGroup.setFooter (groupFooter);
    handler.setExpectedHandler(new ElementFactory(groupFooter, handler));
  }

  public void setCurrentGroup (Group currentGroup)
          throws SAXException
  {
    if (currentGroup == null && this.currentGroup == null)
      throw new SAXException ("Band end before band start?");
    if (currentGroup != null && this.currentGroup != null)
      throw new SAXException ("Unable to stack a band into an other band");

    this.currentGroup = currentGroup;
  }

  public JFreeReport getReport ()
  {
    return report;
  }
}
