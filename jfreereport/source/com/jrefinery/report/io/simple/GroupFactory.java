/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * -----------------
 * GroupFactory.java
 * -----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: GroupFactory.java,v 1.1 2003/01/12 21:33:53 taqua Exp $
 *
 * Changes
 * -------
 * 10-May-2002 : Initial version
 * 10-Dec-2002 : Fixed errors reported by Checkstyle (DG);
 *
 */

package com.jrefinery.report.io.simple;

import com.jrefinery.report.Group;
import com.jrefinery.report.GroupFooter;
import com.jrefinery.report.GroupHeader;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.ParserUtil;
import com.jrefinery.report.targets.FloatDimension;
import com.jrefinery.report.targets.style.BandStyleSheet;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class is a SAX handler for reading groups from a report template file.
 *
 * @author Thomas Morgner
 */
public class GroupFactory extends AbstractReportDefinitionHandler implements ReportDefinitionTags
{
  /** The group under constructiom. */
  private Group currentGroup;

  /** The current text. */
  private StringBuffer currentText;

  /** A font handler. */
  private FontFactory fontFactory;

  /**
   * Creates a new handler.
   */
  public GroupFactory(Parser parser, String finishTag)
  {
    super(parser, finishTag);
    fontFactory = new FontFactory();
  }

  /**
   * Starts an element.
   *
   * @param qName  the element name.
   * @param atts  the element attributes.
   *
   * @throws org.xml.sax.SAXException if there is a problem parsing the XML.
   */
  public void startElement (String qName,
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
      throw new SAXException ("Expected one of: group, groupfooter, groutheader, fields, field. -> " + qName);
    }
  }

  /**
   * Starts the fields element.
   *
   * @param atts  the element attributes.
   *
   * @throws org.xml.sax.SAXException if there is a problem parsing the report template.
   */
  protected void startFields (Attributes atts)
          throws SAXException
  {
  }

  /**
   * Starts the field element.
   *
   * @param atts  the element attributes.
   *
   * @throws org.xml.sax.SAXException if there is a problem parsing the report template.
   */
  protected void startField (Attributes atts)
          throws SAXException
  {
    currentText = new StringBuffer ();
  }

  /**
   * Starts the group element.
   *
   * @param atts  the element attributes.
   *
   * @throws org.xml.sax.SAXException if there is a problem parsing the report template.
   */
  protected void startGroup (Attributes atts)
          throws SAXException
  {
    Group group = new Group ();
    group.setName (getNameGenerator().generateName (atts.getValue ("name")));
    setCurrentGroup (group);
  }

  /**
   * Starts the group header element.
   *
   * @param atts  the element attributes.
   *
   * @throws org.xml.sax.SAXException if there is a problem parsing the report template.
   */
  protected void startGroupHeader (Attributes atts)
          throws SAXException

  {
    // get the height...
    float height = ParserUtil.parseFloat (atts.getValue ("height"),
                                          "Element height not specified");
    boolean pageBreak = ParserUtil.parseBoolean (atts.getValue ("pagebreak"), false);
    boolean repeat = ParserUtil.parseBoolean (atts.getValue (REPEAT_HEADER), false);
    // create the group header...
    GroupHeader groupHeader = new GroupHeader ();
    groupHeader.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE,
                                            new FloatDimension(0, height));
    groupHeader.getStyle().setStyleProperty(BandStyleSheet.PAGEBREAK_BEFORE,
                                            new Boolean (pageBreak));
    groupHeader.getStyle().setStyleProperty(BandStyleSheet.REPEAT_HEADER, new Boolean (repeat));

    FontFactory.FontInformation fi = fontFactory.createFont(atts);
    FontFactory.applyFontInformation(groupHeader.getStyle(), fi);
    FontFactory.applyFontInformation(groupHeader.getBandDefaults(), fi);

    String valign = atts.getValue(VALIGNMENT_ATT);
    if (valign != null)
    {
      groupHeader.getBandDefaults().setStyleProperty(ElementStyleSheet.VALIGNMENT,
          ParserUtil.parseVerticalElementAlignment(valign));
    }
    String halign = atts.getValue(ALIGNMENT_ATT);
    if (halign != null)
    {
      groupHeader.getBandDefaults().setStyleProperty(ElementStyleSheet.ALIGNMENT,
          ParserUtil.parseHorizontalElementAlignment(halign));
    }

    currentGroup.setHeader (groupHeader);
    getParser().pushFactory(new ElementFactory(getParser(), GROUP_HEADER_TAG, groupHeader));
  }

  /**
   * Starts the group footer element.
   *
   * @param atts  the element attributes.
   *
   * @throws org.xml.sax.SAXException if there is a problem parsing the report template.
   */
  protected void startGroupFooter (Attributes atts) throws SAXException
  {
    // get the height...
    boolean pageBreak = ParserUtil.parseBoolean (atts.getValue ("pagebreak"), false);
    float height = ParserUtil.parseFloat (atts.getValue ("height"),
                                          "Element height not specified");

    // get the default font...
    // create the group footer...
    GroupFooter groupFooter = new GroupFooter ();
    groupFooter.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE,
                                            new FloatDimension(0, height));
    groupFooter.getStyle().setStyleProperty(BandStyleSheet.PAGEBREAK_BEFORE,
                                            new Boolean (pageBreak));

    FontFactory.FontInformation fi = fontFactory.createFont(atts);
    FontFactory.applyFontInformation(groupFooter.getStyle(), fi);
    FontFactory.applyFontInformation(groupFooter.getBandDefaults(), fi);

    String valign = atts.getValue(VALIGNMENT_ATT);
    if (valign != null)
    {
      groupFooter.getBandDefaults().setStyleProperty(ElementStyleSheet.VALIGNMENT,
          ParserUtil.parseVerticalElementAlignment(valign));
    }
    String halign = atts.getValue(ALIGNMENT_ATT);
    if (halign != null)
    {
      groupFooter.getBandDefaults().setStyleProperty(ElementStyleSheet.ALIGNMENT,
          ParserUtil.parseHorizontalElementAlignment(halign));
    }

    currentGroup.setFooter (groupFooter);
    getParser().pushFactory(new ElementFactory(getParser(), GROUP_FOOTER_TAG, groupFooter));
  }

  /**
   * Receives some (or all) of the text in the current element.
   *
   * @param ch  storage space for the characters.
   * @param start  the first valid character in the array.
   * @param length  the length of the valid data in the array.
   */
  public void characters (char[] ch, int start, int length)
  {
    if (currentText != null)
    {
      this.currentText.append (String.copyValueOf (ch, start, length));
    }
  }

  /**
   * Ends an element.
   *
   * @param qName  the element name.
   *
   * @throws org.xml.sax.SAXException if there is a problem parsing the XML.
   */
  public void endElement (String qName)
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
    else if (elementName.equals(getFinishTag()))
    {
      getParser().popFactory().endElement(qName);
    }
    else
    {
      throw new SAXException ("Expected one of: group, groupfooter, groutheader, fields, "
                            + "field - but found " + elementName);
    }
  }

  /**
   * Ends the groups element.
   *
   * @throws org.xml.sax.SAXException if there is a problem parsing the report template.
   */
  protected void endGroups ()
          throws SAXException
  {
    getParser().popFactory().endElement(GROUPS_TAG);
  }

  /**
   * Ends the group element.
   *
   * @throws org.xml.sax.SAXException if there is a problem parsing the report template.
   */
  protected void endGroup ()
          throws SAXException
  {
    getReport ().addGroup (currentGroup);
    setCurrentGroup (null);
  }

  /**
   * Ends the field element.
   *
   * @throws org.xml.sax.SAXException if there is a problem parsing the report template.
   */
  protected void endField ()
          throws SAXException
  {
    this.currentGroup.addField (this.currentText.toString ());
    currentText = null;
  }

  /**
   * Ends the fields element.
   *
   * @throws org.xml.sax.SAXException if there is a problem parsing the report template.
   */
  protected void endFields ()
          throws SAXException
  {
  }

  /**
   * Ends the group footer element.
   *
   * @throws org.xml.sax.SAXException if there is a problem parsing the report template.
   */
  protected void endGroupFooter ()
          throws SAXException
  {
  }

  /**
   * Ends the group header element.
   *
   * @throws org.xml.sax.SAXException if there is a problem parsing the report template.
   */
  protected void endGroupHeader ()
          throws SAXException
  {
  }

  /**
   * Sets the current group.
   *
   * @param currentGroup the current group.
   *
   * @throws org.xml.sax.SAXException if there is a problem parsing the report template.
   */
  public void setCurrentGroup (Group currentGroup)
          throws SAXException
  {
    if (currentGroup == null && this.currentGroup == null)
    {
      throw new SAXException ("Band end before band start?");
    }
    if (currentGroup != null && this.currentGroup != null)
    {
      throw new SAXException ("Unable to stack a band into an other band");
    }

    this.currentGroup = currentGroup;
  }
}
