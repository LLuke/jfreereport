/**
 * Date: Jan 13, 2003
 * Time: 1:34:28 PM
 *
 * $Id: ReportConfigWriter.java,v 1.3 2003/01/23 18:07:46 taqua Exp $
 */
package com.jrefinery.report.io.ext.writer;

import com.jrefinery.report.io.ext.ExtReportHandler;
import com.jrefinery.report.io.ext.ReportConfigHandler;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.PageFormatFactory;
import com.jrefinery.report.util.ReportConfiguration;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.Properties;

public class ReportConfigWriter extends AbstractXMLDefinitionWriter
{
  public ReportConfigWriter(ReportWriter reportWriter)
  {
    super(reportWriter);
  }

  public void write(Writer writer) throws IOException
  {
    writeTag(writer, ExtReportHandler.REPORT_CONFIG_TAG);
    writeTag(writer, ReportConfigHandler.DEFAULT_PAGEFORMAT_TAG, buildPageFormatProperties(), CLOSE);
    writeTag(writer, ReportConfigHandler.CONFIGURATION_TAG);
    ReportConfiguration config = getReport().getReportConfiguration();
    Enumeration enum = config.getConfigProperties();
    while (enum.hasMoreElements())
    {
      String key = (String) enum.nextElement();
      String value = config.getConfigProperty(key);
      if (value != null)
      {
        writeTag(writer, "property", "name", key, OPEN);
        writer.write(normalize(value));
        writeCloseTag(writer, "property");
      }
    }

    writeCloseTag(writer, ReportConfigHandler.CONFIGURATION_TAG);
    writeCloseTag(writer, ExtReportHandler.REPORT_CONFIG_TAG);
  }

  private Properties buildPageFormatProperties ()
  {
    Properties retval = new Properties();
    PageFormat fmt = getReport().getDefaultPageFormat();
    int[] borders = getBorders(fmt.getPaper());

    if (fmt.getOrientation() == PageFormat.LANDSCAPE)
    {
      retval.setProperty(ReportConfigHandler.ORIENTATION_ATT, ReportConfigHandler.ORIENTATION_LANDSCAPE_VAL);
      retval.setProperty(ReportConfigHandler.TOPMARGIN_ATT, String.valueOf(borders[RIGHT_BORDER]));
      retval.setProperty(ReportConfigHandler.LEFTMARGIN_ATT, String.valueOf(borders[TOP_BORDER]));
      retval.setProperty(ReportConfigHandler.BOTTOMMARGIN_ATT, String.valueOf(borders[LEFT_BORDER]));
      retval.setProperty(ReportConfigHandler.RIGHTMARGIN_ATT, String.valueOf(borders[BOTTOM_BORDER]));
    }
    else if (fmt.getOrientation() == PageFormat.PORTRAIT)
    {
      retval.setProperty(ReportConfigHandler.ORIENTATION_ATT, ReportConfigHandler.ORIENTATION_PORTRAIT_VAL);
      retval.setProperty(ReportConfigHandler.TOPMARGIN_ATT, String.valueOf(borders[TOP_BORDER]));
      retval.setProperty(ReportConfigHandler.LEFTMARGIN_ATT, String.valueOf(borders[LEFT_BORDER]));
      retval.setProperty(ReportConfigHandler.BOTTOMMARGIN_ATT, String.valueOf(borders[BOTTOM_BORDER]));
      retval.setProperty(ReportConfigHandler.RIGHTMARGIN_ATT, String.valueOf(borders[RIGHT_BORDER]));
    }
    else
    {
      retval.setProperty(ReportConfigHandler.ORIENTATION_ATT, ReportConfigHandler.ORIENTATION_REVERSE_LANDSCAPE_VAL);
      retval.setProperty(ReportConfigHandler.TOPMARGIN_ATT, String.valueOf(borders[LEFT_BORDER]));
      retval.setProperty(ReportConfigHandler.LEFTMARGIN_ATT, String.valueOf(borders[BOTTOM_BORDER]));
      retval.setProperty(ReportConfigHandler.BOTTOMMARGIN_ATT, String.valueOf(borders[RIGHT_BORDER]));
      retval.setProperty(ReportConfigHandler.RIGHTMARGIN_ATT, String.valueOf(borders[TOP_BORDER]));
    }

    int w = (int) fmt.getPaper().getWidth();
    int h = (int) fmt.getPaper().getHeight();

    String pageDefinition = lookupPageDefinition(w, h);
    if (pageDefinition != null)
    {
      retval.setProperty(ReportConfigHandler.PAGEFORMAT_ATT, pageDefinition);
    }
    else
    {
      retval.setProperty(ReportConfigHandler.WIDTH_ATT, String.valueOf(w));
      retval.setProperty(ReportConfigHandler.HEIGHT_ATT, String.valueOf(h));
    }
    return retval;
  }

  private static final int TOP_BORDER = 0;
  private static final int LEFT_BORDER = 1;
  private static final int BOTTOM_BORDER = 2;
  private static final int RIGHT_BORDER = 3;

  private int[] getBorders (Paper p)
  {
    int [] retval = new int[4];

    retval[0] = (int) p.getImageableY();
    retval[1] = (int) p.getImageableX();
    retval[2] = (int) (p.getHeight() - (p.getImageableY() + p.getImageableHeight()));
    retval[3] = (int) (p.getWidth() - (p.getImageableX() + p.getImageableWidth()));
    return retval;
  }

  public String lookupPageDefinition (int w, int h)
  {
    try
    {
      Field[] fields = PageFormatFactory.class.getFields();
      for (int i = 0; i < fields.length; i++)
      {
        Field f = fields[i];
        if (Modifier.isPublic(f.getModifiers()) && Modifier.isStatic(f.getModifiers()))
        {
          Object o = f.get(PageFormatFactory.getInstance());
          if (o instanceof int[])
          {
            int[] pageDef = (int[]) o;
            if (pageDef[0] == w && pageDef[1] == h)
            {
              return f.getName();
            }
          }
        }
      }
    }
    catch (Exception e)
    {
      Log.debug ("Unable to translate the page size");
    }
    return null;
  }
}
