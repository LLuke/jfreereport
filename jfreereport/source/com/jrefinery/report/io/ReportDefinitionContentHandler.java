/* =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
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
 * -----------------------------------
 * ReportDefinitionContentHandler.java
 * -----------------------------------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id$
 *
 * Changes
 * -------
 * 01-Mar-2002 : Version 1, based on the XML format contributed by Thomas Morgner and modified
 *               by DG (DG);
 * 15-Apr-2002 : name element is now optional. Anonymous elements are generated as needed
 *               a default group is generated if no groups are defined.
 *               The parsing of numeric values has been secured against number-format exceptions
 *               
 */

package com.jrefinery.report.io;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.LabelElement;
import com.jrefinery.report.StringElement;
import com.jrefinery.report.NumberElement;
import com.jrefinery.report.DateElement;
import com.jrefinery.report.GeneralElement;
import com.jrefinery.report.ShapeElement;
import com.jrefinery.report.FunctionElement;
import com.jrefinery.report.StringFunctionElement;
import com.jrefinery.report.NumberFunctionElement;
import com.jrefinery.report.DateFunctionElement;
import com.jrefinery.report.ReportHeader;
import com.jrefinery.report.ReportFooter;
import com.jrefinery.report.PageHeader;
import com.jrefinery.report.PageFooter;
import com.jrefinery.report.GroupHeader;
import com.jrefinery.report.GroupFooter;
import com.jrefinery.report.ItemBand;
import com.jrefinery.report.ImageReference;
import com.jrefinery.report.ImageElement;
import com.jrefinery.report.Group;
import com.jrefinery.report.FunctionCollection;
import com.jrefinery.report.function.Function;

import java.awt.geom.Line2D;
import java.util.Properties;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.IOException;

/**
 * Used to construct a JFreeReport instance from a definition in XML format.
 *
 * This is the default implementation used by the JFreeReportDemo. This parser
 * is formed to use reports formed after the "report.dtd".
 */
public class ReportDefinitionContentHandler extends AbstractReportDefinitionHandler {

  private static final String REPORT_TAG = "report";

  private static final String REPORT_HEADER_TAG = "reportheader";

  private static final String REPORT_FOOTER_TAG = "reportfooter";

  private static final String PAGE_HEADER_TAG = "pageheader";

  private static final String PAGE_FOOTER_TAG = "pagefooter";
  
  /**
   * Group definition
   */

  private static final String GROUP_HEADER_TAG = "groupheader";

  private static final String GROUP_FOOTER_TAG = "groupfooter";

  private static final String GROUPS_TAG = "groups";

  private static final String GROUP_TAG = "group";

  private static final String FIELDS_TAG = "fields";

  private static final String FIELD_TAG = "field";

  /**
   * Items and data elements of the report
   */

  private static final String ITEMS_TAG = "items";

  private static final String LABEL_TAG = "label";

  private static final String IMAGEREF_TAG = "imageref";

  private static final String LINE_TAG = "line";

  private static final String GENERAL_FIELD_TAG = "general-field";

  private static final String STRING_FIELD_TAG = "string-field";

  private static final String NUMBER_FIELD_TAG = "number-field";

  private static final String DATE_FIELD_TAG = "date-field";
  
  private static final String MULTILINE_FIELD_TAG = "multi-line-field";

  private static final String GENERAL_FUNCTION_TAG = "general-function";

  private static final String STRING_FUNCTION_TAG = "string-function";

  private static final String NUMBER_FUNCTION_TAG = "number-function";

  private static final String DATE_FUNCTION_TAG = "date-function";

  private static final String MULTILINE_FUNCTION_TAG = "multi-line-function";

  private static final String FUNCTIONS_TAG = "functions";

  private static final String FUNCTION_TAG = "function";

  private static final String FONT_NAME_ATT = "fontname";
  private static final String FONT_STYLE_ATT = "fontstyle";
  private static final String FONT_SIZE_ATT = "fontsize";

  private static final String PROPERTY_TAG = "property";
  private static final String PROPERTIES_TAG = "properties";

  private int nameCounter;

  /** The report under construction. */
  protected JFreeReport report;

  /** The report header under construction. */
  protected ReportHeader reportHeader = null;

  /** The report footer under construction. */
  protected ReportFooter reportFooter = null;

  /** The page header under construction. */
  protected PageHeader pageHeader = null;

  /** The page footer under construction. */
  protected PageFooter pageFooter = null;

  /** The group header under construction. */
  protected GroupHeader groupHeader = null;

  /** The group footer under construction. */
  protected GroupFooter groupFooter = null;

  /** The item band under construction. */
  protected ItemBand items = null;

  /** The functions under construction. */
  protected FunctionCollection functions = null;

  /** Working storage for the text in the current element. */
  protected String elementText = "";

  /** Working reference to the current report band. */
  protected Band currentBand = null;

  /** Working reference to the current group. */
  protected Group currentGroup = null;
  protected boolean groupsDefined = false;
  /** Working reference to the current function. */
  protected Function currentFunction = null;

  /** Temporary storage for the element name. */
  protected String elementName;

  /** Temporary storage for the element X. */
  protected float elementX;

  /** Temporary storage for the element Y. */
  protected float elementY;

  /** Temporary storage for the element W. */
  protected float elementW;

  /** Temporary storage for the element H. */
  protected float elementH;

  /** Temporary storage for the element font. */
  protected Font elementFont;

  protected String elementFontName;

  protected int elementFontStyle;

  protected int elementFontSize;

  protected int elementAlignment;

  protected String elementFormat;

  protected String elementFunctionName;

  protected String elementFieldname;

  protected String elementSource;
    
  protected Properties properties;

  protected String currentProperty;
  
  protected URL contentBase;
  
  /**
  * Default constructor.
  */
  public ReportDefinitionContentHandler()
  {
  }

  public URL getContentBase ()
  {
    return contentBase;
  }

  public void setContentBase (URL url)
  {
    if (url == null)
      throw new NullPointerException ("URL for content pane must not be null");
    
    contentBase = url;  
  }
  
  /**
  * Returns the report for this content handler.  Be careful when you get this reference - the
  * report may be half-built.
  */
  public JFreeReport getReport()
  {
    return this.report;
  }

  /**
  * Receives notification of the start of the document.
  */
  public void startDocument()
  {
    // do nothing
  }

  /**
  * Receives notification of the end of the document.
  */
  public void endDocument()
  {
    // do nothing
  }

  protected String generateName (String name)
  {
    if (name == null)
    {
      nameCounter += 1;
      return "@anonymous" + Integer.toHexString (nameCounter);
    }
    return name;
  }

  /**
  * An element start tag has been reached.  We can read the attributes for the element, but
  * have to wait for the sub-elements and text...so store the attributes for later use.
  */
  public void startElement(String namespaceURI,
    String localName,
    String qName,
    Attributes atts) throws SAXException
  {

    String elementName = qName.toLowerCase().trim();

    this.elementText="";

    // *** REPORT ***
    if (elementName.equals(REPORT_TAG))
    {
      handleReportStart(atts);
    }

    // *** REPORT HEADER ***
    else if (elementName.equals(REPORT_HEADER_TAG))
    {
      handleReportHeaderStart(atts);
    }

    // *** REPORT FOOTER ***
    else if (elementName.equals(REPORT_FOOTER_TAG))
    {
      handleReportFooterStart(atts);
    }

    // *** PAGE HEADER ***
    else if (elementName.equals(PAGE_HEADER_TAG))
    {
      handlePageHeaderStart(atts);
    }

    // *** PAGE FOOTER ***
    else if (elementName.equals(PAGE_FOOTER_TAG))
    {
      handlePageFooterStart(atts);
    }

    // *** GROUP HEADER ***
    else if (elementName.equals(GROUP_HEADER_TAG))
    {
      handleGroupHeaderStart(atts);
    }

    // *** GROUP FOOTER ***
    else if (elementName.equals(GROUP_FOOTER_TAG))
    {
      handleGroupFooterStart(atts);

    }

    // *** GROUPS ***
    else if (elementName.equals(GROUPS_TAG))
    {
      // groups are defined, so don't add a default group.
      groupsDefined = true;
    }

    // *** GROUP ***
    else if (elementName.equals(GROUP_TAG))
    {

      String name = generateName (atts.getValue("name"));
      this.currentGroup = new Group(name);

    }

    // *** FIELDS ***
    else if (elementName.equals(FIELDS_TAG))
    {
      // nothing to do
    }

    // *** FIELD ***
    else if (elementName.equals(FIELD_TAG))
    {
      // nothing to do
    }

    // *** FUNCTIONS ***
    else if (elementName.equals(FUNCTIONS_TAG))
    {
      this.functions = new FunctionCollection();
    }

    // *** FUNCTION ***
    else if (elementName.equals(FUNCTION_TAG))
    {

      // get the name
      String name = generateName (atts.getValue("name"));

      String className = atts.getValue("class");
      if (className==null)
      {
        throw new SAXException("Function class not specified");
      }

      try
      {
        Class fnC = Class.forName(className);
        this.currentFunction = (Function)fnC.newInstance();
        this.currentFunction.setName(name);
      }
      catch (ClassNotFoundException e)
      {
        throw new SAXException ("Function " + name + " class=" + className + " is not valid: " + e.getMessage());
      }
      catch (IllegalAccessException e)
      {
        throw new SAXException ("Function " + name + " class=" + className + " is not valid: " + e.getMessage());
      }
      catch (InstantiationException e)
      {
        throw new SAXException ("Function " + name + " class=" + className + " is not valid: " + e.getMessage());
      }
      this.properties = null;
    }

    // *** ITEMS ***
    else if (elementName.equals(ITEMS_TAG))
    {
      this.handleItemsStart(atts);
    }

    // *** LABEL ***
    else if (elementName.equals(LABEL_TAG))
    {
      this.getElementAttributes(qName, atts);
      // CDATA is the literal text
    }
    else if (elementName.equals(IMAGEREF_TAG))
    {
      // Handle the image reference
      this.elementName = generateName (atts.getValue("name"));
      this.elementSource  = atts.getValue("src");
      if (elementSource == null)
         throw new SAXException ("ImageRef needs an source");
         
      getElementPosition (qName, atts);
    }
    // *** LINE ***
    else if (elementName.equals(LINE_TAG))
    {

      this.elementName = generateName (atts.getValue("name"));

      // x1
      float x1 = parseFloat (atts.getValue("x1"), "Element x1 not specified");
      // y1
      float y1 = parseFloat (atts.getValue("y1"), "Element y1 not specified");
      // x2
      float x2 = parseFloat (atts.getValue("x2"), "Element x2 not specified");
      // y2
      float y2 = parseFloat (atts.getValue("y2"), "Element y2 not specified");

      Line2D line = new Line2D.Float(x1, y1, x2, y2);
      this.currentBand.addElement(new ShapeElement(this.elementName, line, Color.blue));

    }

    // *** GENERAL / STRING / MULTILINE FIELDS ***
    else if (elementName.equals(GENERAL_FIELD_TAG) || elementName.equals(STRING_FIELD_TAG)
      || elementName.equals(MULTILINE_FIELD_TAG))
    {

      this.getElementAttributes(qName, atts);
      this.elementFieldname = atts.getValue("fieldname");
      if (this.elementFieldname == null)
      {
        throw new SAXException("Date/Number function: no fieldname specified.");
      }

    }

    // *** NUMBER / DATE FIELDS ***
    else if (elementName.equals(NUMBER_FIELD_TAG) || elementName.equals(DATE_FIELD_TAG))
    {

      this.getElementAttributes(qName, atts);
      this.elementFormat = atts.getValue("format");
      this.elementFieldname = atts.getValue("fieldname");
      if (this.elementFieldname == null)
      {
        throw new SAXException("Date/Number function: no fieldname specified.");
      }

    }

    // *** GENERAL / STRING FUNCTIONs ***
    else if (elementName.equals(STRING_FUNCTION_TAG) || elementName.equals(GENERAL_FUNCTION_TAG) || 
       elementName.equals(MULTILINE_FUNCTION_TAG) )
    {

      this.getElementAttributes(elementName, atts);
      this.elementFunctionName = atts.getValue("function");
      if (this.elementFunctionName==null)
      {
        throw new SAXException("String function: no function specified.");
      }

    }

    // *** NUMBER-FUNCTION / DATE-FUNCTION ***
    else if ((elementName.equals(NUMBER_FUNCTION_TAG)) || (elementName.equals(DATE_FUNCTION_TAG)))
    {

      this.getElementAttributes(elementName, atts);
      this.elementFunctionName = atts.getValue("function");
      if (this.elementFunctionName==null)
      {
        throw new SAXException("Number/date function: no function specified.");
      }
      this.elementFormat = atts.getValue("format");
      // CDATA not required

    }
    else if (elementName.equals (PROPERTIES_TAG))
    {
      this.properties = new Properties ();
    }
    else if (elementName.equals (PROPERTY_TAG))
    {
      this.currentProperty = atts.getValue ("name");
      if (this.currentProperty == null)
      {
        throw new SAXException("No property name specified.");
      }
    }
  }

  /**
  * Handles the start of a REPORT element.  We are ignoring the width and height attributes
  * for now.
  */
  private void handleReportStart(Attributes atts) throws SAXException
  {

    String name = generateName (atts.getValue("name"));
    this.report = new JFreeReport(name);

  }

  /**
  * Handles the start of a REPORTHEADER element.
  */
  private void handleReportHeaderStart(Attributes atts) throws SAXException
  {

    // get the height...
    float height = parseFloat (atts.getValue("height"), "Element height not specified");

    // get the own page attribute...
    boolean ownPage = false;
    String s = atts.getValue("ownpage");
    if (s!=null)
    {
      if (s.equals("true")) ownPage = true;
    }

    // get the default font...
    Font defaultFont = this.readFont(atts);

    // create the report header...
    this.reportHeader = new ReportHeader(height, ownPage);
    this.reportHeader.setDefaultFont(defaultFont);
    this.currentBand = this.reportHeader;

  }

  /**
  * Handles the start of a REPORTFOOTER element.
  */
  private void handleReportFooterStart(Attributes atts) throws SAXException
  {

    // get the height...
    float height = parseFloat (atts.getValue("height"), "Element height not specified");

    // get the own page attribute...
    boolean ownPage = false;
    String s = atts.getValue("ownpage");
    if (s!=null)
    {
      if (s.equals("true")) ownPage = true;
    }

    // get the default font...
    Font defaultFont = this.readFont(atts);


    // create the report footer...
    this.reportFooter = new ReportFooter(height, ownPage);
    this.reportFooter.setDefaultFont(defaultFont);
    this.currentBand = this.reportFooter;

  }

  /**
  * Handles the start of a PAGEHEADER element.
  */
  private void handlePageHeaderStart(Attributes atts) throws SAXException
  {

    // get the height...
    float height = parseFloat (atts.getValue("height"), "Element height not specified");

    // get the own page attribute...
    boolean firstPage = false;
    String s = atts.getValue("firstpage");
    if (s!=null)
    {
      if (s.equals("true")) firstPage = true;
    }

    // get the default font...
    Font defaultFont = this.readFont(atts);


    // create the page header...
    this.pageHeader = new PageHeader(height, firstPage);
    this.pageHeader.setDefaultFont(defaultFont);
    this.currentBand = this.pageHeader;

  }

  /**
  * Handles the start of a PAGEFOOTER element.
  */
  private void handlePageFooterStart(Attributes atts) throws SAXException
  {

    // get the height...
    float height = parseFloat (atts.getValue("height"), "Element height not specified");

    // get the first page attribute...
    boolean firstPage = false;
    String s = atts.getValue("firstpage");
    if (s!=null)
    {
      if (s.equals("true")) firstPage = true;
    }

    // get the last page attribute...
    boolean lastPage = false;
    s = atts.getValue("lastpage");
    if (s!=null)
    {
      if (s.equals("true")) lastPage = true;
    }

    // get the default font...
    Font defaultFont = this.readFont(atts);

    // create the page footer...
    this.pageFooter = new PageFooter(height, firstPage, lastPage);
    this.pageFooter.setDefaultFont(defaultFont);
    this.currentBand = this.pageFooter;

  }

  /**
  * Handles the start of a GROUPHEADER element.
  */
  private void handleGroupHeaderStart(Attributes atts) throws SAXException
  {

    // get the height...
    float height = parseFloat (atts.getValue("height"), "Element height not specified");

    // get the default font...
    Font defaultFont = this.readFont(atts);


    // create the group header...
    this.groupHeader = new GroupHeader(height);
    this.groupHeader.setDefaultFont(defaultFont);
    this.currentBand = this.groupHeader;

  }

  /**
  * Handles the start of a GROUPFOOTER element.
  */
  private void handleGroupFooterStart(Attributes atts) throws SAXException
  {

    // get the height...
    float height = parseFloat (atts.getValue("height"), "Element height not specified");

    // get the default font...
    Font defaultFont = this.readFont(atts);


    // create the group footer...
    this.groupFooter = new GroupFooter(height);
    this.groupFooter.setDefaultFont(defaultFont);
    this.currentBand = this.groupFooter;

  }

  /**
  * Handles the start of an ITEMS element.
  */
  private void handleItemsStart(Attributes atts) throws SAXException
  {

    // get the height...
    float height = parseFloat (atts.getValue("height"), "Element height not specified");

    // get the default font...
    Font defaultFont = this.readFont(atts);


    // create the items...
    this.items = new ItemBand(height);
    this.items.setDefaultFont(defaultFont);
    this.currentBand = this.items;

  }

  /**
  * Constructs a font from a set of element attributes.
  *
  * @param atts The attributes.
  * @param name The default font name.
  * @param style The default font style.
  * @param size The default font size.
  */
  private Font readFont(Attributes atts) throws SAXException
  {
    // get the font name...
    elementFontName = atts.getValue(FONT_NAME_ATT);

    // get the font style...
    String fontStyle = atts.getValue(FONT_STYLE_ATT);
    elementFontStyle = -1;
    if (fontStyle != null)
    {
      if (fontStyle.equals("plain")) 
        elementFontStyle = Font.PLAIN;
      else if (fontStyle.equals("bold")) 
        elementFontStyle = Font.BOLD;
      else if (fontStyle.equals("italic")) 
        elementFontStyle = Font.ITALIC;
      else if (fontStyle.equals("bold-italic")) 
        elementFontStyle = Font.BOLD+Font.ITALIC;
    }
    
    int elementFontSize = -1;
    // get the font size...
    String fontSize = atts.getValue(FONT_SIZE_ATT);
    if (fontSize!=null)
    {
      try
      {
        int s = Integer.parseInt(fontSize);
        elementFontSize = s;
      }
      catch (NumberFormatException e)
      {
        // OK, take size argument as the default
      }
    }

    if (elementFontName == null || elementFontStyle == -1 || elementFontSize == -1) 
      return null;
      
    // return the font...
    return new Font(elementFontName, elementFontStyle, elementFontSize);

  }

  /**
  * An element ending tag has been reached.  This is a chance to make use of the element
  * text that has been accumulated.
  */
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException
  {
    // *** REPORT ***
    if (qName.equals(REPORT_TAG))
    {

      this.report.setReportHeader(this.reportHeader);
      this.report.setReportFooter(this.reportFooter);
      this.report.setPageHeader(this.pageHeader);
      this.report.setPageFooter(this.pageFooter);
      this.report.setItemBand(this.items);
      this.report.setFunctions(this.functions);
      
    }
    // *** GROUP ***
    else if (qName.equals(GROUP_TAG))
    {

      this.report.addGroup(this.currentGroup);
      this.currentGroup = null;

    }

    // *** GROUP HEADER ***
    else if (qName.equals(GROUP_HEADER_TAG))
    {

      this.currentGroup.setHeader(this.groupHeader);

    }

    // *** GROUP FOOTER ***
    else if (qName.equals(GROUP_FOOTER_TAG))
    {

      this.currentGroup.setFooter(this.groupFooter);

    }

    // *** FUNCTION ***
    else if (qName.equals(FUNCTION_TAG))
    {
      if (this.properties != null)
      {
        currentFunction.setProperties (this.properties);
      }
      if (currentFunction.isInitialized() == false)
      {
        throw new SAXException ("Function " + currentFunction + " is not initialized");
      }
      this.functions.add(currentFunction);
    }

    // *** FIELD ***
    else if (qName.equals(FIELD_TAG))
    {
      this.currentGroup.addField(this.elementText);
    }

    // *** LABEL ***
    else if (qName.equals(LABEL_TAG))
    {

      LabelElement label = new LabelElement(this.elementName,
        this.elementX,
        this.elementY,
        this.elementW,
        this.elementH,
        Color.black,
        this.elementFont,
        this.elementFontName,
        this.elementFontStyle,
        this.elementFontSize,
        this.elementAlignment,
        this.elementText);
      this.currentBand.addElement(label);
    }

    // *** GENERAL ***
    else if (qName.equals(GENERAL_FIELD_TAG))
    {

      GeneralElement general = new GeneralElement(this.elementName,
        this.elementX,
        this.elementY,
        this.elementW,
        this.elementH,
        Color.black,
        this.elementFont,
        this.elementFontName,
        this.elementFontStyle,
        this.elementFontSize,
        this.elementAlignment,
        this.elementFieldname);
      this.currentBand.addElement(general);

    }

    // *** STRING ***
    else if (qName.equals(STRING_FIELD_TAG))
    {

      StringElement string = new StringElement(this.elementName,
        this.elementX,
        this.elementY,
        this.elementW,
        this.elementH,
        Color.black,
        this.elementFont,
        this.elementFontName,
        this.elementFontStyle,
        this.elementFontSize,
        this.elementAlignment,
        this.elementFieldname);
      this.currentBand.addElement(string);

    }

    // *** STRING-FUNCTION ***
    else if (qName.equals(STRING_FUNCTION_TAG))
    {

      StringFunctionElement string =
        new StringFunctionElement(this.elementName,
        this.elementX,
        this.elementY,
        this.elementW,
        this.elementH,
        Color.black,
        this.elementFont,
        this.elementFontName,
        this.elementFontStyle,
        this.elementFontSize,
        this.elementAlignment,
        this.elementFunctionName);
      this.currentBand.addElement(string);

    }

    // *** NUMBER ***
    else if (qName.equals(NUMBER_FIELD_TAG))
    {

      NumberElement number = new NumberElement(this.elementName,
        this.elementX,
        this.elementY,
        this.elementW,
        this.elementH,
        Color.black,
        this.elementFont,
        this.elementFontName,
        this.elementFontStyle,
        this.elementFontSize,
        this.elementAlignment,
        this.elementFieldname,
        this.elementFormat);
      this.currentBand.addElement(number);

    }

    // *** NUMBER FUNCTION ***
    else if (qName.equals(NUMBER_FUNCTION_TAG))
    {

      NumberFunctionElement number = new NumberFunctionElement(this.elementName,
        this.elementX,
        this.elementY,
        this.elementW,
        this.elementH,
        Color.black,
        this.elementFont,
        this.elementFontName,
        this.elementFontStyle,
        this.elementFontSize,
        this.elementAlignment,
        this.elementFunctionName,
        this.elementFormat);
      this.currentBand.addElement(number);

    }

    // *** DATE FIELD ***
    else if (qName.equals(DATE_FIELD_TAG))
    {

      DateElement date = new DateElement(this.elementName,
        this.elementX,
        this.elementY,
        this.elementW,
        this.elementH,
        Color.black,
        this.elementFont,
        this.elementFontName,
        this.elementFontStyle,
        this.elementFontSize,
        this.elementAlignment,
        this.elementFieldname,
        this.elementFormat);
      this.currentBand.addElement(date);

    }

    // *** DATE ***
    else if (qName.equals(DATE_FUNCTION_TAG))
    {

      DateFunctionElement date = new DateFunctionElement(this.elementName,
        this.elementX,
        this.elementY,
        this.elementW,
        this.elementH,
        this.elementFont,
        this.elementAlignment,
        this.elementFunctionName,
        this.elementFormat);
      this.currentBand.addElement(date);

    }
    else if (qName.equals (IMAGEREF_TAG))
    {
      try
      {
        ImageReference imgRef = new ImageReference (new URL (getContentBase(), elementSource), elementX, elementY, elementW, elementH);
        this.currentBand.addElement (new ImageElement (elementName, imgRef));
      }
      catch (MalformedURLException uel)
      {
        throw new SAXException ("Failed to create the image reference");
      }
      catch (IOException ieo)
      {
        throw new SAXException ("Failed to create the image reference (2)");
      }
    }
    else if (qName.equals (PROPERTIES_TAG))
    {
      // do nothing
    }
    else if (qName.equals (PROPERTY_TAG))
    {
      if (this.properties == null || currentProperty == null)
         throw new SAXException ("Illegal Property tag encountered");
         
      this.properties.setProperty (currentProperty, elementText);
    }

    elementText = "";

  }

  /**
  * Receives some (or all) of the text in the current element.
  */
  public void characters(char[] ch, int start, int length)
  {
    // @todo: Parse the default entities 
    
    // accumulate the characters in case the text is split into several chunks...
    this.elementText = elementText + String.copyValueOf(ch, start, length);


  }


  protected int parseInt (String text, String message)
    throws SAXException
  {
    if (text == null)
      throw new SAXException (message);
      
    try
    {
      return Integer.parseInt(text);
    }
    catch (NumberFormatException nfe)
    {
      throw new SAXException ("NumberFormatError: " + message);
    }
  }

  protected float parseFloat (String text, String message)
    throws SAXException
  {
    if (text == null)
      throw new SAXException (message);
    try
    {
      return Float.parseFloat(text);
    }
    catch (NumberFormatException nfe)
    {
      throw new SAXException ("NumberFormatError: " + message);
    }
  }

  /**
   * Reads the attributes that are common for all band-elements, as
   * name, x, y, width, height, font, fontstyle, fontsize and alignment
   */
  protected void getElementAttributes(String qName, Attributes atts) throws SAXException
  {
    this.elementName = generateName (atts.getValue("name"));

    getElementPosition (qName, atts);
        
    this.elementFont = readFont(atts);

    // alignment
    this.elementAlignment = Element.LEFT;
    String s = atts.getValue("alignment");
    if (s!=null)
    {
      if (s.equals("left")) this.elementAlignment = Element.LEFT;
      if (s.equals("center")) this.elementAlignment = Element.CENTER;
      if (s.equals("right")) this.elementAlignment = Element.RIGHT;
    }

  }

  protected void getElementPosition (String qName, Attributes atts) throws SAXException
  {
    // x
    this.elementX = parseFloat (atts.getValue("x"), "Element x not specified");
    this.elementY = parseFloat (atts.getValue("y"), "Element y not specified");
    this.elementW = parseFloat (atts.getValue("width"), "Element width not specified");
    this.elementH = parseFloat (atts.getValue("height"), "Element height not specified");
  }
}
