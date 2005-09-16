/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * ----------------
 * JFreeReport.java
 * ----------------
 * (C)opyright 2000-2003, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: JFreeReport.java,v 1.25 2005/09/07 14:23:49 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 04-Mar-2002 : Major changes to report engine to incorporate functions and different output
 *               targets (DG);
 * 24-Apr-2002 : ItemBand and Groups are Optional Elements, default Elements are created as needed
 * 01-May-2002 : Renamed addProperty to setProperty to create consistent naming among other
 *               property uses.
 * 07-May-2002 : Fixed bug where last row of data is left off the report if it is alone in a
 *               group, reported by Steven Feinstein (DG);
 * 10-May-2002 : Rewrote report-processing. All reportstate-changes are handled by ReportState
 *               Objects. Created AccessorMethods for Properties. (TM)
 * 11-May-2002 : All bands have to be initialized. Null is no longer allowed for pageHeader,
 *               pageFooter, reportHeader, reportFooter, itemBand, functionCollection.
 * 17-May-2002 : Fixed reportPropertyInitialisation and checked if the report is proceeding on
 *               print.
 * 26-May-2002 : Changed repagination behaviour. Reports are repaginated before printed, so that
 *               global initialisations can be done.
 * 05-Jun-2002 : Updated Javadoc comments (DG);
 * 08-Jun-2002 : The defaultPageFormat is now always filled (and used in PreviewFrame)
 * 19-Jun-2002 : more documentation
 * 03-Jul-2002 : Serializable and cloneable, Removed JFreeReportInfo field, it disrupts the
 *               serializable process
 * 26-Jul-2002 : Removed method "isLastItemInHigherGroups()". The same functionality is implemented
 *               in Group.isLastItemInGroup()
 * 05-Dec-2002 : Updated Javadocs (DG);
 * 03-Jan-2003 : More Javadocs (DG);
 * 05-Feb-2003 : Fixed serialisation problem
 */

package org.jfree.report;

import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jfree.base.config.HierarchicalConfiguration;
import org.jfree.base.config.ModifiableConfiguration;
import org.jfree.report.function.Expression;
import org.jfree.report.function.ExpressionCollection;
import org.jfree.report.style.StyleSheetCollection;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.report.util.ReportProperties;

/**
 * A JFreeReport instance is used as report template to define the visual layout of a
 * report and to collect all data sources for the reporting. Possible data sources are the
 * {@link TableModel}, {@link Expression}s or {@link ReportProperties}. The report is made
 * up of 'bands', which are used repeatedly as necessary to generate small sections of the
 * report.
 * <p/>
 * <h2>Accessing the bands and the elements:</h2>
 * <p/>
 * The different bands can be accessed using the main report definition (this class):
 * <p/>
 * <ul> <li>the report header and footer can be reached by using
 * <code>getReportHeader()</code> and <code>getReportFooter()</code>
 * <p/>
 * <li>the page header and page footer can be reached by using
 * <code>getPageHeader()</code> and <code>getPageFooter()</code>
 * <p/>
 * <li>the item band is reachable with <code>getItemBand()</code>
 * <p/>
 * <li>the watermark band is reachable with <code>getWaterMark()</code> </ul>
 * <p/>
 * Groups can be queried using <code>getGroup(int groupLevel)</code>. The group header and
 * footer are accessible through the group object, so use <code>getGroup(int
 * groupLevel).getGroupHeader()<code> and <code>getGroup(int groupLevel).getGroupFooter()<code>.
 * <p/>
 * All report elements share the same stylesheet collection. Report elements cannot be
 * shared between two different report instances.
 * <p/>
 * For dynamic computation of content you can add {@link Expression}s and {@link
 * org.jfree.report.function.Function}s to the report.
 * <p/>
 * Static content can also be added by using the {@link ReportProperties}. Properties,
 * which have been marked using {@link JFreeReport#setPropertyMarked(String, boolean)} can
 * be accessed during the report processing as if they were static columns from the
 * tablemodel.
 * <p/>
 * Creating a new instance of JFreeReport seems to lock down the JDK on some Windows
 * Systems, where no printer driver is installed. To prevent that behaviour on these
 * systems, you can set the {@link ReportConfiguration} key "org.jfree.report.NoPrinterAvailable"
 * to "false" and JFreeReport will use a hardcoded default page format instead.
 *
 * @author David Gilbert
 * @author Thomas Morgner
 */
public class JFreeReport implements Cloneable, Serializable, ReportDefinition
{
  /**
   * An empty datarow implementation. This implementation denies all knowledge about any
   * column and always returns null if queried.
   */
  private static class EmptyDataRow implements DataRow, Serializable
  {
    /**
     * A default constructor.
     */
    public EmptyDataRow ()
    {
    }

    /**
     * Returns the column position of the column, expression or function with the given
     * name or -1 if the given name does not exist in this DataRow.
     *
     * @param name the item name.
     * @return the item index.
     */
    public int findColumn (final String name)
    {
      return -1;
    }

    /**
     * Returns the value of the function, expression or column in the tablemodel using the
     * column number. For functions and expressions, the <code>getValue()</code> method is
     * called and for columns from the tablemodel the tablemodel method
     * <code>getValueAt(row, column)</code> gets called.
     *
     * @param col the item index.
     * @return the value.
     */
    public Object get (final int col)
    {
      return null;
    }

    /**
     * Returns the value of the function, expression or column using its specific name.
     * The given name is translated into a valid column number and the the column is
     * queried. For functions and expressions, the <code>getValue()</code> method is
     * called and for columns from the tablemodel the tablemodel method
     * <code>getValueAt(row, column)</code> gets called.
     *
     * @param col the item index.
     * @return the value.
     *
     * @throws IllegalStateException if the datarow detected a deadlock.
     */
    public Object get (final String col)
            throws IllegalStateException
    {
      return null;
    }

    /**
     * Returns the number of columns, expressions and functions and marked
     * ReportProperties in the report.
     *
     * @return the item count.
     */
    public int getColumnCount ()
    {
      return 0;
    }

    /**
     * Returns the name of the column, expression or function. For columns from the
     * tablemodel, the tablemodels <code>getColumnName</code> method is called. For
     * functions, expressions and report properties the assigned name is returned.
     *
     * @param col the item index.
     * @return the name.
     */
    public String getColumnName (final int col)
    {
      return null;
    }
  }

  /**
   * Key for the 'report name' property.
   */
  public static final String NAME_PROPERTY = "report.name";

  /**
   * Key for the 'report date' property.
   */
  public static final String REPORT_DATE_PROPERTY = "report.date";

  /**
   * Key for the 'report page format' property.
   */
  public static final String REPORT_PAGEFORMAT_PROPERTY = "report.pageformat";

  /**
   * Key for the 'report prepare run' property.
   */
  public static final String REPORT_PREPARERUN_PROPERTY = "report.preparerun";

  /**
   * Key for the 'report definition source' property.
   */
  public static final String REPORT_DEFINITION_SOURCE = "report.definition.source";

  /**
   * Key for the 'report definition content base' property.
   */
  public static final String REPORT_DEFINITION_CONTENTBASE = "report.definition.contentbase";

  /**
   * A reference to the currently used layout support implementation. This can be used to
   * compute text sizes from within functions.
   */
  public static final String REPORT_LAYOUT_SUPPORT = "report.layout-support";
  public static final String REPORT_DATASOURCE_PROPERTY = "report.datasource";

  /**
   * Information about the JFreeReport class library.
   */
  private static JFreeReportInfo info;

  /**
   * The data row implementation for reports, which are not currently beeing processed.
   */
  private static EmptyDataRow emptyDataRow;

  /**
   * The table model containing the data for the report.
   */
  private TableModel data;

  /**
   * The page format for the report (determines the page size, and therefore the report
   * width).
   */
  private PageDefinition pageDefinition;

  /**
   * Storage for arbitrary properties that a user can assign to the report.
   */
  private ReportProperties properties;

  /**
   * Storage for the expressions in the report.
   */
  private ExpressionCollection expressions;

  /**
   * An ordered list of report groups (each group defines its own header and footer).
   */
  private GroupList groups;

  /**
   * The report header band (if not null, printed once at the start of the report).
   */
  private ReportHeader reportHeader;

  /**
   * The report footer band (if not null, printed once at the end of the report).
   */
  private ReportFooter reportFooter;

  /**
   * The page header band (if not null, printed at the start of every page).
   */
  private PageHeader pageHeader;

  /**
   * The page footer band (if not null, printed at the end of every page).
   */
  private PageFooter pageFooter;

  /**
   * The item band - used once for each row of data.
   */
  private ItemBand itemBand;

  /**
   * The watermark band.
   */
  private Watermark watermark;

  /**
   * The report configuration.
   */
  private final ModifiableConfiguration reportConfiguration;

  /**
   * The stylesheet collection used for this report.
   */
  private StyleSheetCollection styleSheetCollection;

  /**
   * The report builder hints support writer and other report definition processing tools
   * by spicing up the report object with specific properties. ReportBuilderHints do not
   * get cloned and are removed from the clone.
   * <p/>
   * ReportBuilderHints will not be written into serialized instances.
   */
  private transient ReportBuilderHints reportBuilderHints;

  /**
   * The resource bundle factory is used when generating localized reports.
   */
  private ResourceBundleFactory resourceBundleFactory;

  /**
   * The default constructor. Creates an empty but fully initialized report.
   */
  public JFreeReport ()
  {
    this.reportConfiguration = new HierarchicalConfiguration
            (JFreeReportBoot.getInstance().getGlobalConfig());
    this.properties = new ReportProperties();
    this.styleSheetCollection = new StyleSheetCollection();

    this.groups = new GroupList();
    this.reportHeader = new ReportHeader();
    this.reportFooter = new ReportFooter();
    this.pageHeader = new PageHeader();
    this.pageFooter = new PageFooter();
    this.itemBand = new ItemBand();
    this.watermark = new Watermark();

    this.data = new DefaultTableModel();
    this.expressions = new ExpressionCollection();
    this.resourceBundleFactory = new DefaultResourceBundleFactory();
    setPageDefinition(null);

    this.groups.setReportDefinition(this);
    this.reportHeader.setReportDefinition(this);
    this.reportFooter.setReportDefinition(this);
    this.pageHeader.setReportDefinition(this);
    this.pageFooter.setReportDefinition(this);
    this.itemBand.setReportDefinition(this);
    this.watermark.setReportDefinition(this);
  }

  /**
   * Returns the name of the report.
   * <p/>
   * You can reference the report name in your XML report template file using the key
   * '<code>report.name</code>'.
   *
   * @return the name.
   */
  public String getName ()
  {
    final Object name = getProperty(NAME_PROPERTY);
    return String.valueOf(name);
  }

  /**
   * Sets the name of the report.
   * <p/>
   * The report name is stored as a property (key {@link JFreeReport#NAME_PROPERTY})
   * inside the report properties.  If you supply <code>null</code> as the name, the
   * property is removed.
   *
   * @param name the name of the report.
   */
  public void setName (final String name)
  {
    setProperty(NAME_PROPERTY, name);
  }

  /**
   * Adds a property to the report.
   * <p/>
   * If a property with the given name already exists, the property will be updated with
   * the new value. If the supplied value is <code>null</code>, the property will be
   * removed.
   * <p/>
   * Developers are free to add any properties they want to a report, and then display
   * those properties in the report. For example, you might add a 'user.name' property, so
   * that you can display the username in the header of a report.
   *
   * @param key   the key.
   * @param value the value.
   */
  public void setProperty (final String key, final Object value)
  {
    this.properties.put(key, value);
  }

  /**
   * Returns the report properties collection for this report.
   * <p/>
   * These properties are inherited to all ReportStates generated for this report.
   *
   * @return the report properties.
   */
  public ReportProperties getProperties ()
  {
    return properties;
  }

  /**
   * Returns the value of the property with the specified key.
   *
   * @param key the key.
   * @return the property value.
   */
  public Object getProperty (final String key)
  {
    if (key == null)
    {
      throw new NullPointerException();
    }
    return this.properties.get(key);
  }

  /**
   * Returns the value of the property with the specified key.
   *
   * @param key the key.
   * @return the property value.
   */
  public boolean isPropertyMarked (final String key)
  {
    return this.properties.isMarked(key);
  }

  /**
   * Returns the value of the property with the specified key.
   *
   * @param key  the key.
   * @param mark the new marking flag
   */
  public void setPropertyMarked (final String key, final boolean mark)
  {
    this.properties.setMarked(key, mark);
  }

  /**
   * Sets the report header.
   *
   * @param header the report header (<code>null</code> not permitted).
   */
  public void setReportHeader (final ReportHeader header)
  {
    if (header == null)
    {
      throw new NullPointerException("JFreeReport.setReportHeader(...) : null not permitted.");
    }
    this.reportHeader.setReportDefinition(null);
    this.reportHeader = header;
    this.reportHeader.setReportDefinition(this);
  }

  /**
   * Returns the report header.
   *
   * @return the report header (never <code>null</code>).
   */
  public ReportHeader getReportHeader ()
  {
    return reportHeader;
  }

  /**
   * Sets the report footer.
   *
   * @param footer the report footer (<code>null</code> not permitted).
   */
  public void setReportFooter (final ReportFooter footer)
  {
    if (footer == null)
    {
      throw new NullPointerException("JFreeReport.setReportFooter(...) : null not permitted.");
    }
    this.reportFooter.setReportDefinition(null);
    this.reportFooter = footer;
    this.reportFooter.setReportDefinition(this);
  }

  /**
   * Returns the page footer.
   *
   * @return the report footer (never <code>null</code>).
   */
  public ReportFooter getReportFooter ()
  {
    return reportFooter;
  }

  /**
   * Sets the page header.
   *
   * @param header the page header (<code>null</code> not permitted).
   */
  public void setPageHeader (final PageHeader header)
  {
    if (header == null)
    {
      throw new NullPointerException("JFreeReport.setPageHeader(...) : null not permitted.");
    }
    this.pageHeader.setReportDefinition(null);
    this.pageHeader = header;
    this.pageHeader.setReportDefinition(this);
  }

  /**
   * Returns the page header.
   *
   * @return the page header (never <code>null</code>).
   */
  public PageHeader getPageHeader ()
  {
    return pageHeader;
  }

  /**
   * Sets the page footer.
   *
   * @param footer the page footer (<code>null</code> not permitted).
   */
  public void setPageFooter (final PageFooter footer)
  {
    if (footer == null)
    {
      throw new NullPointerException("JFreeReport.setPageFooter(...) : null not permitted.");
    }
    this.pageFooter.setReportDefinition(null);
    this.pageFooter = footer;
    this.pageFooter.setReportDefinition(this);
  }

  /**
   * Returns the page footer.
   *
   * @return the page footer (never <code>null</code>).
   */
  public PageFooter getPageFooter ()
  {
    return pageFooter;
  }

  /**
   * Sets the watermark band for the report.
   *
   * @param band the new watermark band (<code>null</code> not permitted).
   */
  public void setWatermark (final Watermark band)
  {
    if (band == null)
    {
      throw new NullPointerException("JFreeReport.setWatermark(...) : null not permitted.");
    }
    this.watermark.setReportDefinition(null);
    this.watermark = band;
    this.watermark.setReportDefinition(this);
  }

  /**
   * Returns the report's watermark band.
   *
   * @return the watermark band (never <code>null</code>).
   */
  public Watermark getWatermark ()
  {
    return this.watermark;
  }

  /**
   * Sets the item band for the report.
   *
   * @param band the new item band (<code>null</code> not permitted).
   */
  public void setItemBand (final ItemBand band)
  {
    if (band == null)
    {
      throw new NullPointerException("JFreeReport.setItemBand(...) : null not permitted.");
    }
    this.itemBand.setReportDefinition(null);
    this.itemBand = band;
    this.itemBand.setReportDefinition(this);
  }

  /**
   * Returns the report's item band.
   *
   * @return the item band (never <code>null</code>).
   */
  public ItemBand getItemBand ()
  {
    return this.itemBand;
  }

  /**
   * Adds a group to the report.
   *
   * @param group the group.
   */
  public void addGroup (final Group group)
  {
    groups.add(group);
  }

  /**
   * Sets the groups for this report. If no list (null) or an empty list is given, an
   * default group is created. This default group contains no elements and starts at the
   * first record of the data and ends on the last record.
   *
   * @param groupList the list of groups.
   */
  public void setGroups (final GroupList groupList)
  {
    if (groupList == null)
    {
      throw new NullPointerException("GroupList must not be null");
    }
    this.groups.setReportDefinition(null);
    this.groups = groupList;
    this.groups.setReportDefinition(this);
  }

  /**
   * Returns a clone of the list of groups for the report.
   *
   * @return the group list.
   */
  public GroupList getGroups ()
  {
    return this.groups;
  }

  /**
   * Returns the number of groups in this report. <P> Every report has at least one group
   * defined.
   *
   * @return the group count.
   */
  public int getGroupCount ()
  {
    return groups.size();
  }

  /**
   * Returns the group at the specified index or null, if there is no such group.
   *
   * @param count the group index.
   * @return the requested group.
   *
   * @throws IllegalArgumentException  if the count is negative.
   * @throws IndexOutOfBoundsException if the count is greater than the number of defined
   *                                   groups.
   */
  public Group getGroup (final int count)
  {
    if (count < 0)
    {
      throw new IllegalArgumentException("GroupCount must not be negative");
    }

    if (count >= groups.size())
    {
      throw new IndexOutOfBoundsException("No such group defined. " + count + " vs. "
              + groups.size());
    }

    return groups.get(count);
  }

  /**
   * Searches a group by its defined name. This method returns null, if the group was not
   * found.
   *
   * @param name the name of the group.
   * @return the group or null if not found.
   *
   * @see GroupList#getGroupByName
   */
  public Group getGroupByName (final String name)
  {
    return groups.getGroupByName(name);
  }

  /**
   * Adds a function to the report's collection of expressions.
   *
   * @param function the function.
   */
  public void addExpression (final Expression function)
  {
    expressions.add(function);
  }

  /**
   * Returns the logical page definition for this report.
   *
   * @return the page definition.
   */
  public PageDefinition getPageDefinition ()
  {
    return pageDefinition;
  }

  /**
   * Defines the logical page definition for this report. If no format is defined the
   * system's default page format is used.
   * <p/>
   * If there is no printer available and the JDK blocks during the printer discovery, you
   * can set the {@link ReportConfiguration} key "org.jfree.report.NoPrinterAvailable" to
   * "false" and JFreeReport will use a hardcoded default page format instead.
   *
   * @param format the default format (<code>null</code> permitted).
   */
  public void setPageDefinition (PageDefinition format)
  {
    if (format == null)
    {
      if (JFreeReportBoot.getInstance().getExtendedConfig().getBoolProperty
              (JFreeReportCoreModule.NO_PRINTER_AVAILABLE_KEY))
      {
        format = new SimplePageDefinition(new PageFormat());
      }
      else
      {
        format = new SimplePageDefinition(PrinterJob.getPrinterJob().defaultPage());
      }
    }
    pageDefinition = format;
  }

  /**
   * Sets the data for the report.
   * <p/>
   * Reports are generated from a {@link TableModel} (as used by Swing's {@link
   * javax.swing.JTable}). If you don't want to give data to the report, use an empty
   * {@link TableModel} instead of <code>null</code>.
   *
   * @param data the data for the report (<code>null</code> not permitted).
   */
  public void setData (final TableModel data)
  {
    if (data == null)
    {
      throw new NullPointerException("JFreeReport.setData(...) : null not permitted.");
    }
    this.data = data;
  }

  /**
   * Returns the current data for this report.
   *
   * @return the data in form of a table model.
   */
  public TableModel getData ()
  {
    return data;
  }

  /**
   * Clones the report.
   *
   * @return the clone.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    final JFreeReport report = (JFreeReport) super.clone();
    report.data = data; // data is defined to be immutable, so don't clone the thing
    report.pageDefinition = (PageDefinition) pageDefinition.clone();
    report.groups = (GroupList) groups.clone();
    report.watermark = (Watermark) watermark.clone();
    report.itemBand = (ItemBand) itemBand.clone();
    report.pageFooter = (PageFooter) pageFooter.clone();
    report.pageHeader = (PageHeader) pageHeader.clone();
    report.properties = (ReportProperties) properties.clone();
    report.reportFooter = (ReportFooter) reportFooter.clone();
    report.reportHeader = (ReportHeader) reportHeader.clone();
    report.expressions = (ExpressionCollection) expressions.clone();
    report.styleSheetCollection = (StyleSheetCollection) styleSheetCollection.clone();
    // we replace the reportbuilder-hints with an empty set here
    // as only the original report definition will be hinted.
    report.reportBuilderHints = new ReportBuilderHints();

    report.groups.setReportDefinition(report);
    report.reportHeader.setReportDefinition(report);
    report.reportFooter.setReportDefinition(report);
    report.pageHeader.setReportDefinition(report);
    report.pageFooter.setReportDefinition(report);
    report.itemBand.setReportDefinition(report);
    report.watermark.setReportDefinition(report);
    return report;
  }

  /**
   * Returns information about the JFreeReport class library.
   *
   * @return the information.
   */
  public synchronized static JFreeReportInfo getInfo ()
  {
    if (JFreeReport.info == null)
    {
      JFreeReport.info = new JFreeReportInfo();
    }
    return JFreeReport.info;
  }

  /**
   * Returns the expressions for the report.
   *
   * @return the expressions.
   */
  public ExpressionCollection getExpressions ()
  {
    return expressions;
  }

  /**
   * Sets the expressions for the report.
   *
   * @param expressions the expressions (<code>null</code> not permitted).
   */
  public void setExpressions (final ExpressionCollection expressions)
  {
    if (expressions == null)
    {
      throw new NullPointerException("JFreeReport.setExpressions(...) : null not permitted.");
    }
    this.expressions = expressions;
  }

  /**
   * Returns the report configuration.
   * <p/>
   * The report configuration is automatically set up when the report is first created,
   * and uses the global JFreeReport configuration as its parent.
   *
   * @return the report configuration.
   */
  public ModifiableConfiguration getReportConfiguration ()
  {
    return reportConfiguration;
  }

  /**
   * Returns the stylesheet collection of this report. The stylesheet collection is fixed
   * for the report and all elements of the report. When a band or group is added to the
   * report it will get registered with this stylesheet collection and cannot be used in
   * an different report.
   *
   * @return the stylesheet collection of the report, never null.
   */
  public StyleSheetCollection getStyleSheetCollection ()
  {
    return styleSheetCollection;
  }

  /**
   * Returns the report builder hint collection, that is assigned with this report. Be
   * aware that these hints are not cloned and that during the cloning all references to
   * this ReportBuilderHints instance get replaced by an newly created instance.
   * <p/>
   * The report builder hints are used by the report writer to preserve the original
   * layout of the XML files.
   *
   * @return the report builder hints.
   */
  public ReportBuilderHints getReportBuilderHints ()
  {
    return reportBuilderHints;
  }

  /**
   * Redefines the report builder hints. If null is given, the old hints get removed
   * without a replacement.
   *
   * @param reportBuilderHints the new report builder hints.
   */
  public void setReportBuilderHints (final ReportBuilderHints reportBuilderHints)
  {
    this.reportBuilderHints = reportBuilderHints;
  }

  /**
   * Returns the resource bundle factory for this report definition. The {@link
   * ResourceBundleFactory} is used in internationalized reports to create the
   * resourcebundles holding the localized resources.
   *
   * @return the assigned resource bundle factory.
   */
  public ResourceBundleFactory getResourceBundleFactory ()
  {
    return resourceBundleFactory;
  }

  /**
   * Redefines the resource bundle factory for the report.
   *
   * @param resourceBundleFactory the new resource bundle factory, never null.
   * @throws NullPointerException if the given ResourceBundleFactory is null.
   */
  public void setResourceBundleFactory (
          final ResourceBundleFactory resourceBundleFactory)
  {
    if (resourceBundleFactory == null)
    {
      throw new NullPointerException("ResourceBundleFactory must not be null");
    }
    this.resourceBundleFactory = resourceBundleFactory;
  }

  /**
   * Queries the current resource bundle factory for the resource bundle specified by the
   * given key.
   *
   * @param key the base name of the resource bundle.
   * @return the resource bundle
   *
   * @throws java.util.MissingResourceException
   *          if no resource bundle for the specified base name can be found
   */
  public ResourceBundle getResourceBundle (final String key)
  {
    return resourceBundleFactory.getResourceBundle(key);
  }

  /**
   * Returns the current datarow assigned to this report definition. JFreeReport objects
   * do not hold a working DataRow, as the final contents of the data cannot be known,
   * until the reporting has started.
   *
   * @return the default implementation for non-processed reports.
   */
  public DataRow getDataRow ()
  {
    synchronized (JFreeReport.class)
    {
      if (emptyDataRow == null)
      {
        emptyDataRow = new EmptyDataRow();
      }
      return emptyDataRow;
    }
  }
}
