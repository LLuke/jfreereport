package org.jfree.report.modules.parser.ext.readhandlers;

import org.jfree.xml.parser.AbstractXmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.jfree.xml.ElementDefinitionException;
import org.jfree.report.style.StyleSheetCollection;
import org.jfree.report.style.ElementStyleSheet;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class StyleExtendsReadHandler extends AbstractXmlReadHandler
{
  private StyleSheetCollection styleSheetCollection;
  private ElementStyleSheet styleSheet;

  public StyleExtendsReadHandler (final StyleSheetCollection styleSheetCollection,
                                  final ElementStyleSheet styleSheet)
  {
    this.styleSheetCollection = styleSheetCollection;
    this.styleSheet = styleSheet;
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final Attributes attrs)
          throws SAXException, XmlReaderException
  {
    final String name = attrs.getValue("name");
    if (name == null)
    {
      throw new ElementDefinitionException
              ("Required attribute 'name' is missing.", getRootHandler().getLocator());
    }
    final ElementStyleSheet parent = styleSheetCollection.getStyleSheet(name);
    if (parent == null)
    {
      throw new ElementDefinitionException
              ("Specified parent stylesheet is not defined.", getRootHandler().getLocator());
    }
    styleSheet.addParent(parent);
  }

  /**
   * Returns the object for this element or null, if this element does not create an
   * object.
   *
   * @return the object.
   *
   * @throws org.jfree.xml.parser.XmlReaderException
   *          if there is a parsing error.
   */
  public Object getObject ()
          throws XmlReaderException
  {
    return null;
  }
}
