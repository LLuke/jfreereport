/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2004, by Simba Management Limited and Contributors.
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
 * ------------------------------
 * PageOfPagesFunction.java
 * ------------------------------
 * (C)opyright 2004, by J&ouml;rg Schaible and Contributors.
 *
 * Original Author:  J&ouml;rg Schaible;
 *
 * $Id: PageOfPagesFunction.java,v 1.3 2005/05/08 15:41:15 taqua Exp $
 *
 * Changes
 * -------------------------
 * 15-Dec-2004 : Initial version
 *
 */
package org.jfree.report.function;

import java.text.MessageFormat;
import java.util.Locale;

import org.jfree.report.DataRow;
import org.jfree.report.event.ReportEvent;


/**
 * A report function that combines {@link PageFunction}and {@link PageTotalFunction}.
 * Restrictions for both classes apply to this one also.
 *
 * @author J&ouml;rg Schaible
 */
public class PageOfPagesFunction extends PageFunction
{
  private PageTotalFunction pageTotalFunction;
  private String format;
  private transient Locale locale;

  /**
   * Constructs an unnamed function. <P> This constructor is intended for use by the SAX
   * handler class only.
   */
  public PageOfPagesFunction ()
  {
    pageTotalFunction = new PageTotalFunction();
    pageTotalFunction.setName("__internally_used_only");
    this.format = "{0} / {1}";
  }

  /**
   * Constructs a named function.
   *
   * @param name the function name.
   */
  public PageOfPagesFunction (final String name)
  {
    this();
    setName(name);
  }

  /**
   * Returns the format used to print the value. The default format is &quot;{0} /
   * {1}&quot;.
   *
   * @return the format
   *
   * @see MessageFormat
   */
  public String getFormat ()
  {
    return format;
  }

  /**
   * Set the format of the value. The format should follow the rules of {@link
   * MessageFormat}. The first parameter is filled with the current page, the second with
   * the total number of pages.
   *
   * @param format the format string.
   */
  public void setFormat (final String format)
  {
    if (format == null)
    {
      throw new NullPointerException("Format must not be null.");
    }
    this.format = format;
  }

  public void reportInitialized (final ReportEvent event)
  {
    super.reportInitialized(event);
    pageTotalFunction.reportInitialized(event);
    locale = event.getReport().getResourceBundleFactory().getLocale();
  }

  public void pageStarted (final ReportEvent event)
  {
    super.pageStarted(event);
    pageTotalFunction.pageStarted(event);
    locale = event.getReport().getResourceBundleFactory().getLocale();
  }

  public void pageCanceled (final ReportEvent event)
  {
    super.pageCanceled(event);
    pageTotalFunction.pageCanceled(event);
    locale = event.getReport().getResourceBundleFactory().getLocale();
  }

  public void pageFinished (final ReportEvent event)
  {
    super.pageFinished(event);
    pageTotalFunction.pageFinished(event);
    locale = event.getReport().getResourceBundleFactory().getLocale();
  }

  public void groupStarted (final ReportEvent event)
  {
    super.groupStarted(event);
    pageTotalFunction.groupStarted(event);
    locale = event.getReport().getResourceBundleFactory().getLocale();
  }

  /**
   * Return the value of this {@link Function}. The method uses the format definition from
   * the properties and adds the current page and the total number of pages as parameter.
   *
   * @return the formatted value with current page and total number of pages.
   */
  public Object getValue ()
  {
    final Object page = super.getValue();
    final Object pages = pageTotalFunction.getValue();
    if (locale == null)
    {
      locale = Locale.getDefault();
    }
    final MessageFormat compiledFormat = new MessageFormat(getFormat(), locale);
    return compiledFormat.format(new Object[]{page, pages});
  }

  public void setGroup (final String group)
  {
    super.setGroup(group);
    pageTotalFunction.setGroup(group);
  }

  public void setStartPage (final int startPage)
  {
    super.setStartPage(startPage);
    pageTotalFunction.setStartPage(startPage);
  }

  public void setDependencyLevel (final int level)
  {
    super.setDependencyLevel(level);
    pageTotalFunction.setDependencyLevel(level);
  }

  public void setDataRow (final DataRow dataRow)
  {
    super.setDataRow(dataRow);
    pageTotalFunction.setDataRow(dataRow);
  }

  public Expression getInstance ()
  {
    final PageOfPagesFunction function = (PageOfPagesFunction) super.getInstance();
    function.pageTotalFunction = (PageTotalFunction) pageTotalFunction.getInstance();
    return function;
  }
}
