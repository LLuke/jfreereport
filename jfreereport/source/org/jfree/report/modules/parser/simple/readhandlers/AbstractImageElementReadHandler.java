package org.jfree.report.modules.parser.simple.readhandlers;

import org.jfree.report.elementfactory.ImageElementFactory;
import org.jfree.report.modules.parser.base.PropertyAttributes;
import org.jfree.xml.ParserUtil;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.SAXException;

public abstract class AbstractImageElementReadHandler extends AbstractElementReadHandler
{
  private static final String SCALE_ATT = "scale";
  private static final String KEEP_ASPECT_RATIO_ATT = "keepAspectRatio";

  protected AbstractImageElementReadHandler ()
  {
  }

  /**
   * Starts parsing.
   *
   * @param atts the attributes.
   * @throws org.xml.sax.SAXException if there is a parsing error.
   */
  protected void startParsing (final PropertyAttributes atts)
          throws SAXException, XmlReaderException
  {
    super.startParsing(atts);
    handleScale(atts);
    handleKeepAspectRatio(atts);
  }

  protected void handleScale (final PropertyAttributes atts)
  {
    final String booleanValue = atts.getValue(SCALE_ATT);
    if (booleanValue != null)
    {
      final ImageElementFactory elementFactory = (ImageElementFactory) getElementFactory();
      elementFactory.setScale(new Boolean(ParserUtil.parseBoolean(booleanValue, false)));
    }
  }

  protected void handleKeepAspectRatio (final PropertyAttributes atts)
  {
    final String booleanValue = atts.getValue(KEEP_ASPECT_RATIO_ATT);
    if (booleanValue != null)
    {
      final ImageElementFactory elementFactory = (ImageElementFactory) getElementFactory();
      elementFactory.setKeepAspectRatio(new Boolean(ParserUtil.parseBoolean(booleanValue, false)));
    }
  }
}
