/**
 * Date: Jan 12, 2003
 * Time: 4:36:42 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.factory.datasource;

import com.jrefinery.report.io.ext.factory.objects.BeanObjectDescription;
import com.jrefinery.report.io.ext.factory.datasource.AbstractDataSourceFactory;
import com.jrefinery.report.filter.DataRowDataSource;
import com.jrefinery.report.filter.DateFormatFilter;
import com.jrefinery.report.filter.DateFormatParser;
import com.jrefinery.report.filter.FormatParser;
import com.jrefinery.report.filter.FormatFilter;
import com.jrefinery.report.filter.EmptyDataSource;
import com.jrefinery.report.filter.DecimalFormatParser;
import com.jrefinery.report.filter.DecimalFormatFilter;
import com.jrefinery.report.filter.ImageLoadFilter;
import com.jrefinery.report.filter.ImageRefFilter;
import com.jrefinery.report.filter.NumberFormatFilter;
import com.jrefinery.report.filter.NumberFormatParser;
import com.jrefinery.report.filter.SimpleDateFormatFilter;
import com.jrefinery.report.filter.SimpleDateFormatParser;
import com.jrefinery.report.filter.StaticDataSource;
import com.jrefinery.report.filter.StringFilter;
import com.jrefinery.report.filter.URLFilter;

public class DefaultDataSourceFactory extends AbstractDataSourceFactory
{
  public DefaultDataSourceFactory()
  {
    registerDataSources("DataRowDataSource", new BeanObjectDescription(DataRowDataSource.class));
    registerDataSources("DateFormatFilter", new BeanObjectDescription(DateFormatFilter.class));
    registerDataSources("DateFormatParser", new BeanObjectDescription(DateFormatParser.class));
    registerDataSources("DecimalFormatFilter", new BeanObjectDescription(DecimalFormatFilter.class));
    registerDataSources("DecimalFormatParser", new BeanObjectDescription(DecimalFormatParser.class));
    registerDataSources("EmptyDataSource", new BeanObjectDescription(EmptyDataSource.class));
    registerDataSources("FormatFilter", new BeanObjectDescription(FormatFilter.class));
    registerDataSources("FormatParser", new BeanObjectDescription(FormatParser.class));
    registerDataSources("ImageLoadFilter", new BeanObjectDescription(ImageLoadFilter.class));
    registerDataSources("ImageRefFilter", new BeanObjectDescription(ImageRefFilter.class));
    registerDataSources("NumberFormatFilter", new BeanObjectDescription(NumberFormatFilter.class));
    registerDataSources("NumberFormatParser", new BeanObjectDescription(NumberFormatParser.class));
    registerDataSources("SimpleDateFormatFilter", new BeanObjectDescription(SimpleDateFormatFilter.class));
    registerDataSources("SimpleDateFormatParser", new BeanObjectDescription(SimpleDateFormatParser.class));
    registerDataSources("StaticDataSource", new BeanObjectDescription(StaticDataSource.class));
    registerDataSources("StringFilter", new BeanObjectDescription(StringFilter.class));
    registerDataSources("URLFilter", new BeanObjectDescription(URLFilter.class));
  }
}
