/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * -----------------------------
 * DefaultDataSourceFactory.java
 * -----------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DefaultDataSourceFactory.java,v 1.7 2003/03/07 17:02:40 taqua Exp $
 *
 * Changes (from 19-Feb-2003)
 * -------------------------
 * 19-Feb-2003 : Added standard header and Javadocs (DG);
 *
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
import com.jrefinery.report.filter.DrawableFilter;
import com.jrefinery.report.filter.ShapeFilter;
import com.jrefinery.xml.factory.objects.BeanObjectDescription;
import com.jrefinery.report.io.ext.factory.templates.DateFieldTemplateDescription;
import com.jrefinery.report.io.ext.factory.templates.ImageFieldTemplateDescription;
import com.jrefinery.report.io.ext.factory.templates.ImageURLElementTemplateDescription;
import com.jrefinery.report.io.ext.factory.templates.ImageURLFieldTemplateDescription;
import com.jrefinery.report.io.ext.factory.templates.LabelTemplateDescription;
import com.jrefinery.report.io.ext.factory.templates.NumberFieldTemplateDescription;
import com.jrefinery.report.io.ext.factory.templates.ResourceFieldTemplateDescription;
import com.jrefinery.report.io.ext.factory.templates.ResourceLabelTemplateDescription;
import com.jrefinery.report.io.ext.factory.templates.StringFieldTemplateDescription;
import com.jrefinery.report.io.ext.factory.templates.ShapeFieldTemplateDescription;

/**
 * A default implementation of the {@link DataSourceFactory} interface.
 *
 * @author Thomas Morgner
 */
public class DefaultDataSourceFactory extends AbstractDataSourceFactory
{
  /**
   * Creates a new factory.
   */
  public DefaultDataSourceFactory()
  {
    registerDataSources("DataRowDataSource", new BeanObjectDescription(DataRowDataSource.class));
    registerDataSources("DateFormatFilter", new BeanObjectDescription(DateFormatFilter.class));
    registerDataSources("DateFormatParser", new BeanObjectDescription(DateFormatParser.class));
    registerDataSources("DecimalFormatFilter",
                        new BeanObjectDescription(DecimalFormatFilter.class));
    registerDataSources("DecimalFormatParser",
                        new BeanObjectDescription(DecimalFormatParser.class));
    registerDataSources("EmptyDataSource", new BeanObjectDescription(EmptyDataSource.class));
    registerDataSources("FormatFilter", new BeanObjectDescription(FormatFilter.class));
    registerDataSources("FormatParser", new BeanObjectDescription(FormatParser.class));
    registerDataSources("ImageLoadFilter", new BeanObjectDescription(ImageLoadFilter.class));
    registerDataSources("ImageRefFilter", new BeanObjectDescription(ImageRefFilter.class));
    registerDataSources("NumberFormatFilter", new BeanObjectDescription(NumberFormatFilter.class));
    registerDataSources("NumberFormatParser", new BeanObjectDescription(NumberFormatParser.class));
    registerDataSources("SimpleDateFormatFilter",
                        new BeanObjectDescription(SimpleDateFormatFilter.class));
    registerDataSources("SimpleDateFormatParser",
                        new BeanObjectDescription(SimpleDateFormatParser.class));
    registerDataSources("StaticDataSource", new BeanObjectDescription(StaticDataSource.class));
    registerDataSources("StringFilter", new BeanObjectDescription(StringFilter.class));
    registerDataSources("URLFilter", new BeanObjectDescription(URLFilter.class));
    registerDataSources("ResourceFileFilter", new BeanObjectDescription(ResourceFileFilter.class));
    registerDataSources("DrawableFilter", new BeanObjectDescription(DrawableFilter.class));
    registerDataSources("ShapeFilter", new BeanObjectDescription(ShapeFilter.class));

    // templates are also datasources ...
    registerDataSources("DateFieldTemplate", new DateFieldTemplateDescription("date-field"));
    registerDataSources("ImageFieldTemplate", new ImageFieldTemplateDescription("image-field"));
    registerDataSources("ImageURLFieldTemplate",
                        new ImageURLFieldTemplateDescription("image-url-field"));
    registerDataSources("ImageURLElementTemplate",
                        new ImageURLElementTemplateDescription("image-url-element"));
    registerDataSources("LabelTemplate", new LabelTemplateDescription("label"));
    registerDataSources("NumberFieldTemplate", new NumberFieldTemplateDescription("number-field"));
    registerDataSources("StringFieldTemplate", new StringFieldTemplateDescription("string-field"));
    registerDataSources("ResourceFieldTemplate",
                        new ResourceFieldTemplateDescription("resource-field"));
    registerDataSources("ResourceLabelTemplate",
                        new ResourceLabelTemplateDescription("resource-label"));
    registerDataSources("ShapeFieldTemplate",
                        new ShapeFieldTemplateDescription("shape-field"));
  }

}
