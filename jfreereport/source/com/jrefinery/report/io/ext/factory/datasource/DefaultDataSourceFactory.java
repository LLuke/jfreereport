/**
 * Date: Jan 12, 2003
 * Time: 4:36:42 PM
 *
 * $Id: DefaultDataSourceFactory.java,v 1.3 2003/01/25 02:47:09 taqua Exp $
 */
package com.jrefinery.report.io.ext.factory.datasource;

import com.jrefinery.report.filter.DataRowDataSource;
import com.jrefinery.report.filter.DateFormatFilter;
import com.jrefinery.report.filter.DateFormatParser;
import com.jrefinery.report.filter.DecimalFormatFilter;
import com.jrefinery.report.filter.DecimalFormatParser;
import com.jrefinery.report.filter.EmptyDataSource;
import com.jrefinery.report.filter.FormatFilter;
import com.jrefinery.report.filter.FormatParser;
import com.jrefinery.report.filter.ImageLoadFilter;
import com.jrefinery.report.filter.ImageRefFilter;
import com.jrefinery.report.filter.NumberFormatFilter;
import com.jrefinery.report.filter.NumberFormatParser;
import com.jrefinery.report.filter.ResourceFileFilter;
import com.jrefinery.report.filter.SimpleDateFormatFilter;
import com.jrefinery.report.filter.SimpleDateFormatParser;
import com.jrefinery.report.filter.StaticDataSource;
import com.jrefinery.report.filter.StringFilter;
import com.jrefinery.report.filter.URLFilter;
import com.jrefinery.report.io.ext.factory.objects.BeanObjectDescription;
import com.jrefinery.report.io.ext.factory.templates.DateFieldTemplateDescription;
import com.jrefinery.report.io.ext.factory.templates.ImageFieldTemplateDescription;
import com.jrefinery.report.io.ext.factory.templates.ImageURLElementTemplateDescription;
import com.jrefinery.report.io.ext.factory.templates.ImageURLFieldTemplateDescription;
import com.jrefinery.report.io.ext.factory.templates.LabelTemplateDescription;
import com.jrefinery.report.io.ext.factory.templates.NumberFieldTemplateDescription;
import com.jrefinery.report.io.ext.factory.templates.ResourceFieldTemplateDescription;
import com.jrefinery.report.io.ext.factory.templates.ResourceLabelTemplateDescription;
import com.jrefinery.report.io.ext.factory.templates.StringFieldTemplateDescription;

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
    registerDataSources("ResourceFileFilter", new BeanObjectDescription(ResourceFileFilter.class));

    // templates are also datasources ...
    registerDataSources("DateFieldTemplate", new DateFieldTemplateDescription("date-field"));
    registerDataSources("ImageFieldTemplate", new ImageFieldTemplateDescription("image-field"));
    registerDataSources("ImageURLFieldTemplate", new ImageURLFieldTemplateDescription("image-url-field"));
    registerDataSources("ImageURLElementTemplate", new ImageURLElementTemplateDescription("image-url-element"));
    registerDataSources("LabelTemplate", new LabelTemplateDescription("label"));
    registerDataSources("NumberFieldTemplate", new NumberFieldTemplateDescription("number-field"));
    registerDataSources("StringFieldTemplate", new StringFieldTemplateDescription("string-field"));
    registerDataSources("ResourceFieldTemplate", new ResourceFieldTemplateDescription("resource-field"));
    registerDataSources("ResourceLabelTemplate", new ResourceLabelTemplateDescription("resource-label"));
  }
  

}
