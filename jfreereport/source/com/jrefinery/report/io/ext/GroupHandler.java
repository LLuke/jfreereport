/**
 * Date: Jan 22, 2003
 * Time: 3:45:29 PM
 *
 * $Id: GroupHandler.java,v 1.2 2003/01/23 18:07:44 taqua Exp $
 */
package com.jrefinery.report.io.ext;

import com.jrefinery.report.Band;
import com.jrefinery.report.Group;
import com.jrefinery.report.GroupFooter;
import com.jrefinery.report.GroupHeader;
import com.jrefinery.report.io.Parser;
import com.jrefinery.report.io.ReportDefinitionHandler;
import com.jrefinery.report.util.CharacterEntityParser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class GroupHandler implements ReportDefinitionHandler
{
  public static final String FIELDS_TAG = "fields";
  public static final String FIELD_TAG = "field";
  public static final String GROUP_HEADER_TAG = "group-header";
  public static final String GROUP_FOOTER_TAG = "group-footer";

  private Parser parser;
  private String finishTag;
  private Group group;
  private StringBuffer buffer;
  private BandHandler bandFactory;
  private CharacterEntityParser entityParser;

  public GroupHandler(Parser parser, String finishTag, Group group)
  {
    this.entityParser = CharacterEntityParser.createXMLEntityParser();
    this.parser = parser;
    this.finishTag = finishTag;
    this.group = group;
  }

  public void startElement(String tagName, Attributes attrs) throws SAXException
  {
    if (tagName.equals (GROUP_HEADER_TAG))
    {
      Band band = new GroupHeader();
      String name = attrs.getValue("name");
      if (name != null)
      {
        band.setName(name);
      }
      bandFactory = new BandHandler(getParser(), tagName, band);
      getParser().pushFactory(bandFactory);
    }
    else if (tagName.equals (GROUP_FOOTER_TAG))
    {
      Band band = new GroupFooter();
      String name = attrs.getValue("name");
      if (name != null)
      {
        band.setName(name);
      }
      bandFactory = new BandHandler(getParser(), tagName, band);
      getParser().pushFactory(bandFactory);
    }
    else if (tagName.equals (FIELDS_TAG))
    {
      // unused
    }
    else if (tagName.equals (FIELD_TAG))
    {
      buffer = new StringBuffer();
    }
    else
    {
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: " +
                              GROUP_HEADER_TAG + ", " +
                              GROUP_FOOTER_TAG + ", " +
                              FIELDS_TAG + ", " +
                              FIELD_TAG);
    }
  }

  public void characters(char ch[], int start, int length) throws SAXException
  {
    if (buffer != null)
    {
      buffer.append(ch, start, length);
    }
  }

  public void endElement(String tagName) throws SAXException
  {
    if (tagName.equals(finishTag))
    {
      getParser().popFactory().endElement(tagName);
    }
    else if (tagName.equals (GROUP_HEADER_TAG))
    {
      group.setHeader((GroupHeader) bandFactory.getElement());
    }
    else if (tagName.equals (GROUP_FOOTER_TAG))
    {
      group.setFooter((GroupFooter) bandFactory.getElement());
    }
    else if (tagName.equals (FIELDS_TAG))
    {
      // ignore ...
    }
    else if (tagName.equals (FIELD_TAG))
    {
      group.addField(entityParser.decodeEntities(buffer.toString()));
      buffer = null;
    }
    else
    {
      throw new SAXException ("Invalid TagName: " + tagName + ", expected one of: " +
                              GROUP_HEADER_TAG + ", " +
                              GROUP_FOOTER_TAG + ", " +
                              FIELDS_TAG + ", " +
                              FIELD_TAG + ", " +
                              finishTag);
    }
  }

  public Group getGroup()
  {
    return group;
  }

  public Parser getParser()
  {
    return parser;
  }
}
