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
 * -----------------
 * GroupFactory.java
 * -----------------
 * (C)opyright 2002, 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: GroupFactory.java,v 1.11 2005/01/25 00:22:10 taqua Exp $
 *
 * Changes
 * -------
 * 10-May-2002 : Initial version
 * 10-Dec-2002 : Fixed errors reported by Checkstyle (DG);
 *
 */

package org.jfree.report.modules.parser.simple;

import org.jfree.report.Group;
import org.jfree.report.GroupFooter;
import org.jfree.report.GroupHeader;
import org.jfree.report.modules.parser.base.ReportParser;
import org.jfree.report.modules.parser.base.ReportParserUtil;
import org.jfree.report.style.BandStyleKeys;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.util.CharacterEntityParser;
import org.jfree.ui.FloatDimension;
import org.jfree.xml.ParseException;
import org.jfree.xml.ParserUtil;
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

  /** A character entity parser. */
  private final CharacterEntityParser entityParser;

  /**
   * Creates a new handler.
   *
   * @param parser the used parser to coordinate the parsing process.
   * @param finishTag the finish tag, that should trigger the deactivation of this parser.
   * @throws NullPointerException if the finishTag or the parser are null.
   */
  public GroupFactory(final ReportParser parser, final String finishTag)
  {
    super(parser, finishTag);
    entityParser = CharacterEntityParser.createXMLEntityParser();
  }

  /**
   * Starts an element.
   *
   * @param qName  the element name.
   * @param atts  the element attributes.
   *
   * @throws SAXException if there is a problem parsing the XML.
   */
  public void startElement(final String qName,
                           final Attributes atts) throws SAXException
  {
    final String elementName = qName.toLowerCase().trim();

    // *** GROUP HEADER ***
    if (elementName.equals(GROUP_HEADER_TAG))
    {
      startGroupHeader(atts);
    }
    // *** GROUP FOOTER ***
    else if (elementName.equals(GROUP_FOOTER_TAG))
    {
      startGroupFooter(atts);
    }
    else if (elementName.equals(FIELDS_TAG))
    {
      // do nothing...
    }
    else if (elementName.equals(FIELD_TAG))
    {
      startField(atts);
    }
    else if (elementName.equals(GROUP_TAG))
    {
      startGroup(atts);
    }
    else
    {
      throw new SAXException("Expected one of: group, groupfooter, groutheader, fields, field. -> "
          + qName);
    }
  }

  /**
   * Starts the field element.
   *
   * @param atts  the element attributes.
   *
   * @throws SAXException if there is a problem parsing the report template.
   */
  protected void startField(final Attributes atts)
      throws SAXException
  {
    currentText = new StringBuffer();
  }

  /**
   * Starts the group element.
   *
   * @param atts  the element attributes.
   *
   * @throws SAXException if there is a problem parsing the report template.
   */
  protected void startGroup(final Attributes atts)
      throws SAXException
  {
    final String groupName = atts.getValue(NAME_ATT);
    if (groupName != null)
    {
      Group group = getReport().getGroupByName(groupName);
      if (group != null)
      {
        setCurrentGroup(group);
      }
      else
      {
        group = new Group();
        group.setName(groupName);
        setCurrentGroup(group);
      }
    }
    else
    {
      setCurrentGroup(new Group());
    }
  }

  /**
   * Starts the group header element.
   *
   * @param atts  the element attributes.
   *
   * @throws SAXException if there is a problem parsing the report template.
   */
  protected void startGroupHeader(final Attributes atts)
      throws SAXException

  {
    // create the group header...
    final GroupHeader groupHeader = currentGroup.getHeader();

    // get the height...
    final String heightAttr = atts.getValue("height");
    if (heightAttr != null)
    {
      final float height = ParserUtil.parseFloat(heightAttr, 0);
      groupHeader.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE,
          new FloatDimension(0, height));
    }

    final String fixedPos = atts.getValue("fixed-position");
    if (fixedPos != null)
    {
      final float fixedPosValue = ParserUtil.parseFloat
              (fixedPos, "FixedPosition is invalid!");
      groupHeader.getStyle().setStyleProperty(BandStyleKeys.FIXED_POSITION,
              new Float (fixedPosValue));
    }

    String pagebreakBeforeAttr = atts.getValue("pagebreak");
    if (pagebreakBeforeAttr == null)
    {
      pagebreakBeforeAttr = atts.getValue("pagebreak-before-print");
    }
    if (pagebreakBeforeAttr != null)
    {
      final boolean pageBreak = ParserUtil.parseBoolean(pagebreakBeforeAttr, false);
      groupHeader.getStyle().setBooleanStyleProperty
          (BandStyleKeys.PAGEBREAK_BEFORE, pageBreak);
    }

    final String pagebreakAfterAttr = atts.getValue("pagebreak-after-print");
    if (pagebreakAfterAttr != null)
    {
      final boolean pageBreakAfter = ParserUtil.parseBoolean(pagebreakAfterAttr, false);
      groupHeader.getStyle().setBooleanStyleProperty
          (BandStyleKeys.PAGEBREAK_AFTER, pageBreakAfter);
    }

    final String repeatAttr = atts.getValue(REPEAT_HEADER);
    if (repeatAttr != null)
    {
      final boolean repeat = ParserUtil.parseBoolean(repeatAttr, false);
      groupHeader.getStyle().setBooleanStyleProperty
          (BandStyleKeys.REPEAT_HEADER, repeat);
    }

    final FontFactory.FontInformation fi = FontFactory.createFont(atts);
    FontFactory.applyFontInformation(groupHeader.getStyle(), fi);

    final String valign = atts.getValue(VALIGNMENT_ATT);
    if (valign != null)
    {
      groupHeader.getStyle().setStyleProperty(ElementStyleSheet.VALIGNMENT,
          ReportParserUtil.parseVerticalElementAlignment(valign));
    }
    final String halign = atts.getValue(ALIGNMENT_ATT);
    if (halign != null)
    {
      groupHeader.getStyle().setStyleProperty(ElementStyleSheet.ALIGNMENT,
          ReportParserUtil.parseHorizontalElementAlignment(halign));
    }

    getParser().pushFactory(new ElementFactory(getReportParser(), GROUP_HEADER_TAG, groupHeader));
  }

  /**
   * Starts the group footer element.
   *
   * @param atts  the element attributes.
   *
   * @throws SAXException if there is a problem parsing the report template.
   */
  protected void startGroupFooter(final Attributes atts) throws SAXException
  {
    final GroupFooter groupFooter = currentGroup.getFooter();

    // get the height...
    final String heightAttr = atts.getValue("height");
    if (heightAttr != null)
    {
      final float height = ParserUtil.parseFloat(heightAttr, 0);
      groupFooter.getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE,
          new FloatDimension(0, height));
    }

    final String fixedPos = atts.getValue("fixed-position");
    if (fixedPos != null)
    {
      final float fixedPosValue = ParserUtil.parseFloat
              (fixedPos, "FixedPosition is invalid!");
      groupFooter.getStyle().setStyleProperty(BandStyleKeys.FIXED_POSITION,
              new Float (fixedPosValue));
    }

    String pagebreakBeforeAttr = atts.getValue("pagebreak");
    if (pagebreakBeforeAttr == null)
    {
      pagebreakBeforeAttr = atts.getValue("pagebreak-before-print");
    }
    if (pagebreakBeforeAttr != null)
    {
      final boolean pageBreak = ParserUtil.parseBoolean(pagebreakBeforeAttr, false);
      groupFooter.getStyle().setBooleanStyleProperty
          (BandStyleKeys.PAGEBREAK_BEFORE, pageBreak);
    }

    final String pagebreakAfterAttr = atts.getValue("pagebreak-after-print");
    if (pagebreakAfterAttr != null)
    {
      final boolean pageBreakAfter = ParserUtil.parseBoolean(pagebreakAfterAttr, false);
      groupFooter.getStyle().setBooleanStyleProperty
          (BandStyleKeys.PAGEBREAK_AFTER, pageBreakAfter);
    }

    // create the group footer...

    final FontFactory.FontInformation fi = FontFactory.createFont(atts);
    FontFactory.applyFontInformation(groupFooter.getStyle(), fi);

    final String valign = atts.getValue(VALIGNMENT_ATT);
    if (valign != null)
    {
      groupFooter.getStyle().setStyleProperty(ElementStyleSheet.VALIGNMENT,
          ReportParserUtil.parseVerticalElementAlignment(valign));
    }
    final String halign = atts.getValue(ALIGNMENT_ATT);
    if (halign != null)
    {
      groupFooter.getStyle().setStyleProperty(ElementStyleSheet.ALIGNMENT,
          ReportParserUtil.parseHorizontalElementAlignment(halign));
    }
    getParser().pushFactory(new ElementFactory(getReportParser(), GROUP_FOOTER_TAG, groupFooter));
  }

  /**
   * Receives some (or all) of the text in the current element.
   *
   * @param ch  storage space for the characters.
   * @param start  the first valid character in the array.
   * @param length  the length of the valid data in the array.
   */
  public void characters(final char[] ch, final int start, final int length)
  {
    if (currentText != null)
    {
      this.currentText.append(String.copyValueOf(ch, start, length));
    }
  }

  /**
   * Ends an element.
   *
   * @param qName  the element name.
   *
   * @throws SAXException if there is a problem parsing the XML.
   */
  public void endElement(final String qName)
      throws SAXException
  {
    final String elementName = qName.toLowerCase().trim();
    // *** GROUP HEADER ***
    if (elementName.equals(GROUP_HEADER_TAG))
    {
      // do nothing ..
    }
    // *** GROUP FOOTER ***
    else if (elementName.equals(GROUP_FOOTER_TAG))
    {
      // do nothing ..
    }
    else if (elementName.equals(FIELDS_TAG))
    {
      // do nothing ..
    }
    else if (elementName.equals(GROUP_TAG))
    {
      endGroup();
    }
    else if (elementName.equals(FIELD_TAG))
    {
      endField();
    }
    else if (elementName.equals(GROUPS_TAG))
    {
      endGroups();
    }
    else if (elementName.equals(getFinishTag()))
    {
      getParser().popFactory().endElement(qName);
    }
    else
    {
      throw new SAXException("Expected one of: group, groupfooter, groutheader, fields, "
          + "field - but found " + elementName);
    }
  }

  /**
   * Ends the groups element.
   *
   * @throws SAXException if there is a problem parsing the report template.
   */
  protected void endGroups()
      throws SAXException
  {
    getParser().popFactory().endElement(GROUPS_TAG);
  }

  /**
   * Ends the group element.
   *
   * @throws SAXException if there is a problem parsing the report template.
   */
  protected void endGroup()
      throws SAXException
  {
    getReport().addGroup(currentGroup);
    setCurrentGroup(null);
  }

  /**
   * Ends the field element.
   *
   * @throws SAXException if there is a problem parsing the report template.
   */
  protected void endField()
      throws SAXException
  {
    this.currentGroup.addField(entityParser.decodeEntities(currentText.toString()));
    currentText = null;
  }

  /**
   * Sets the current group.
   *
   * @param currentGroup the current group.
   *
   * @throws SAXException if there is a problem parsing the report template.
   */
  public void setCurrentGroup(final Group currentGroup)
      throws SAXException
  {
    if (currentGroup == null && this.currentGroup == null)
    {
      throw new ParseException("Band end before band start?", getLocator());
    }
    if (currentGroup != null && this.currentGroup != null)
    {
      throw new ParseException("Unable to stack a band into an other band", getLocator());
    }

    this.currentGroup = currentGroup;
  }
}
