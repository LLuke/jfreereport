/**
 * Date: Jan 13, 2003
 * Time: 2:08:56 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.writer;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.Group;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ShapeElement;
import com.jrefinery.report.io.ext.ExtReportHandler;
import com.jrefinery.report.targets.style.BandDefaultStyleSheet;
import com.jrefinery.report.targets.style.ElementDefaultStyleSheet;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.util.Log;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.TreeSet;

public class StylesWriter extends AbstractXMLDefinitionWriter
{
  private ArrayList reportStyles;

  public StylesWriter(ReportWriter reportWriter)
  {
    super(reportWriter);
    reportStyles = new ArrayList();
  }

  public void write(Writer writer) throws IOException, ReportWriterException
  {
    Object[] styles = collectStyles();
    writeTag(writer, ExtReportHandler.STYLES_TAG);

    for (int i = 0; i < styles.length; i++)
    {
      ElementStyleSheet style = (ElementStyleSheet) styles[i];
      StyleWriter stW = new StyleWriter(getReportWriter(), false, style);
      stW.write(writer);
    }
    writeCloseTag(writer, ExtReportHandler.STYLES_TAG);
  }


  private Object[] collectStyles ()
  {
    JFreeReport report = getReport();
    collectStylesFromBand(report.getReportHeader());
    collectStylesFromBand(report.getReportFooter());
    collectStylesFromBand(report.getPageHeader());
    collectStylesFromBand(report.getPageFooter());
    collectStylesFromBand(report.getItemBand());
    for (int i = 0; i < report.getGroupCount(); i++)
    {
      Group g = report.getGroup(i);
      collectStylesFromBand(g.getHeader());
      collectStylesFromBand(g.getFooter());
    }

    Hashtable collectedStyles = new Hashtable();

    for (int i = 0; i < reportStyles.size(); i++)
    {
      ElementStyleSheet es = (ElementStyleSheet) reportStyles.get(i);
      if (collectedStyles.containsKey(es.getName()))
      {
        Log.warn ("Duplciate stylesheet-template name: " + es.getName());
      }
      else
      {
        collectedStyles.put(es.getName(), es);
      }
    }

    reportStyles.clear();

    //now sort the elements ...
    TreeSet sortedSet = new TreeSet(new StylesComparator());
    Enumeration keys = collectedStyles.keys();
    while (keys.hasMoreElements())
    {
      String key = (String) keys.nextElement();
      Object value = collectedStyles.get(key);
      sortedSet.add(value);
    }
    Object[] styles = sortedSet.toArray();
    return styles;
  }

  private void collectStylesFromBand (Band band)
  {
    ElementStyleSheet bandDefaults = band.getBandDefaults();

    List parents = bandDefaults.getParents();
    for (int i = 0; i < parents.size(); i++)
    {
      ElementStyleSheet es = (ElementStyleSheet) parents.get(i);
      collectStyleSheet(es);
    }
    collectStylesFromElement(band);
  }

  private void collectStylesFromElement (Element element)
  {
    ElementStyleSheet elementSheet = element.getStyle();

    List parents = elementSheet.getParents();
    for (int i = 0; i < parents.size(); i++)
    {
      ElementStyleSheet es = (ElementStyleSheet) parents.get(i);
      collectStyleSheet(es);
    }
  }

  private void collectStyleSheet (ElementStyleSheet es)
  {
    if (es == BandDefaultStyleSheet.getBandDefaultStyle())
      return;

    if (es == ElementDefaultStyleSheet.getDefaultStyle())
      return;

    if (es == ShapeElement.getDefaultStyle())
      return;

    if (reportStyles.contains(es) == false)
      reportStyles.add(es);
  }
}
