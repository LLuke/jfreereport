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

import com.jrefinery.report.Group;
import com.jrefinery.report.GroupFooter;
import com.jrefinery.report.GroupHeader;
import com.jrefinery.report.JFreeReport;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GroupFactory extends DefaultHandler implements ReportDefinitionTags
{
  private JFreeReport report;
  private Group currentGroup;
  private StringBuffer currentText;
  private ReportDefinitionContentHandler handler;
  private FontFactory fontFactory;

  public GroupFactory (ReportFactory baseFactory)
  {
    this.report = baseFactory.getReport ();
    this.handler = baseFactory.getHandler ();
    fontFactory = handler.getFontFactory ();
  }

  protected ReportDefinitionContentHandler getHander ()
  {
    return handler;
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
      startFields (atts);
    }
    else if (elementName.equals (FIELD_TAG))
    {
      startField (atts);
    }
    else if (elementName.equals (GROUP_TAG))
    {
      startGroup (atts);
    }
    else
    {
      throw new SAXException ("Expected one of: group, groupfooter, groutheader, fields, field");
    }
  }

  protected void startFields (Attributes atts)
          throws SAXException
  {
  }

  protected void startField (Attributes atts)
          throws SAXException
  {
    currentText = new StringBuffer ();
  }


  protected void startGroup (Attributes attr)
          throws SAXException
  {
    Group group = new Group ();
    group.setName (handler.generateName (attr.getValue ("name")));
    setCurrentGroup (group);
  }

  protected void startGroupHeader (Attributes attr)
          throws SAXException

  {
    // get the height...
    float height = ParserUtil.parseFloat (attr.getValue ("height"), "Element height not specified");
    boolean pageBreak = ParserUtil.parseBoolean (attr.getValue ("pagebreak"), false);
    // create the group header...
    GroupHeader groupHeader = new GroupHeader ();
    groupHeader.setHeight (height);
    groupHeader.setPageBreakBeforePrint (pageBreak);
    groupHeader.setDefaultFont (fontFactory.createDefaultFont (attr));
    currentGroup.setHeader (groupHeader);

    handler.getReportFactory().setCurrentBand(groupHeader);
    ElementFactory factory = handler.createElementFactory();
    handler.setExpectedHandler (factory);
  }

  /**
   * Handles the start of a GROUPFOOTER element.
   */
  protected void startGroupFooter (Attributes attr) throws SAXException
  {
    // get the height...
    float height = ParserUtil.parseFloat (attr.getValue ("height"), "Element height not specified");

    // get the default font...
    // create the group footer...
    GroupFooter groupFooter = new GroupFooter ();
    groupFooter.setHeight (height);
    groupFooter.setDefaultFont (fontFactory.createDefaultFont (attr));
    currentGroup.setFooter (groupFooter);

    handler.getReportFactory().setCurrentBand(groupFooter);
    ElementFactory factory = handler.createElementFactory();
    handler.setExpectedHandler (factory);
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
      endGroupHeader ();
    }
    // *** GROUP FOOTER ***
    else if (elementName.equals (GROUP_FOOTER_TAG))
    {
      endGroupFooter ();
    }
    else if (elementName.equals (FIELDS_TAG))
    {
      endFields ();
    }
    else if (elementName.equals (GROUP_TAG))
    {
      endGroup ();
    }
    else if (elementName.equals (FIELD_TAG))
    {
      endField ();
    }
    else if (elementName.equals (GROUPS_TAG))
    {
      endGroups ();
    }
    else
    {
      throw new SAXException ("Expected one of: group, groupfooter, groutheader, fields, field - but found " + elementName);
    }
  }

  protected void endGroups ()
          throws SAXException
  {
    handler.finishedHandler ();
  }

  protected void endGroup ()
          throws SAXException
  {
    getReport ().addGroup (currentGroup);
    setCurrentGroup (null);
  }

  protected void endField ()
          throws SAXException
  {
    this.currentGroup.addField (this.currentText.toString ());
    currentText = null;
  }

  protected void endFields ()
          throws SAXException
  {
  }

  protected void endGroupFooter ()
          throws SAXException
  {
  }

  protected void endGroupHeader ()
          throws SAXException
  {
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
