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
 * $Id: JFreeReport.java,v 1.7.2.1.2.1 2004/12/30 14:46:09 taqua Exp $
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
import java.util.Iterator;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jfree.report.function.Expression;
import org.jfree.report.function.ExpressionCollection;
import org.jfree.report.function.Function;
import org.jfree.report.function.FunctionInitializeException;
import org.jfree.report.style.StyleSheetCollection;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.report.util.ReportProperties;

/**
 * This class co-ordinates the process of generating a report from a <code>TableModel</code>.
 * The report is made up of 'bands', which are used repeatedly as necessary to generate small
 * sections of the report.
 * <p>
 * Accessing the bands and the elements:
 * <p>
 * The different bands can be accessed using the main report definition (this class):
 * <ul>
 * <li>the report header and footer can be reached by using
 *     <code>getReportHeader()</code> and <code>getReportFooter()</code>
 * <li>the page header and page footer can be reached by using
 *     <code>getPageHeader()</code> and <code>getPageFooter()</code>
 * <li>the item band is reachable with <code>getItemBand()</code>
 * </ul>
 *
 * Groups can be queried using <code>getGroup(int groupLevel)</code>.  The group header and
 * footer are accessible through the group object, so use
 * <code>getGroup(int groupLevel).getGroupHeader()<code> and
 * <code>getGroup(int groupLevel).getGroupFooter()<code>.
 * <p>
 * Elements on a band can be reached by using <code>Band.getElement (String elementName)</code>
 * or by retrieving all elements using <code>Band.getElements()</code> and performing a search on
 * the returned list.
 * <p>
 * All report elements share the same stylesheet collection. Report elements
 * cannot be shared between two different report instances.
 *
 * @author David Gilbert
 * @author Thomas Morgner
 */
public class JFreeReport implements Cloneable, Serializable
{

  /** Key for the 'report name' property. */
  public static final String NAME_PROPERTY = "report.name";

  /** Key for the 'report date' property. */
  public static final String REPORT_DATE_PROPERTY = "report.date";

  /** Key for the 'report page format' property. */
  public static final String REPORT_PAGEFORMAT_PROPERTY = "report.pageformat";

  /** Key for the 'report prepare run' property. */
  public static final String REPORT_PREPARERUN_PROPERTY = "report.preparerun";

  /** Key for the 'report definition source' property. */
  public static final String REPORT_DEFINITION_SOURCE = "report.definition.source";

  /** Key for the 'report definition content base' property. */
  public static final String REPORT_DEFINITION_CONTENTBASE = "report.definition.contentbase";

  /** Information about the JFreeReport class library. */
  private static JFreeReportInfo info;

  /** The table model containing the data for the report. */
  private TableModel data;

  /** The page format for the report (determines the page size, and therefore the report width). */
  private PageDefinition pageDefinition;

  /** Storage for arbitrary properties that a user can assign to the report. */
  private ReportProperties properties;

  /** Storage for the expressions in the report. */
  private ExpressionCollection expressions;

  /** An ordered list of report groups (each group defines its own header and footer). */
  private GroupList groups;

  /** The report header band (if not null, printed once at the start of the report). */
  private ReportHeader reportHeader;

  /** The report footer band (if not null, printed once at the end of the report). */
  private ReportFooter reportFooter;

  /** The page header band (if not null, printed at the start of every page). */
  private PageHeader pageHeader;

  /** The page footer band (if not null, printed at the end of every page). */
  private PageFooter pageFooter;

  /** The item band - used once for each row of data. */
  private ItemBand itemBand;

  /** The watermark band. */
  private Watermark watermark;

  /** The report configuration. */
  private final ReportConfiguration reportConfiguration;

  /** The stylesheet collection used for this report. */
  private StyleSheetCollection styleSheetCollection;

  /**
   * The report builder hints support writer and other report definition
   * processing tools by spicing up the report object with specific properties.
   * ReportBuilderHints do not get cloned and are removed from the clone.
   */
  private ReportBuilderHints reportBuilderHints;

  /**
   * The default constructor. Creates an empty but fully initialized report.
   */
  public JFreeReport()
  {
    this.reportConfiguration = new ReportConfiguration(ReportConfiguration.getGlobalConfig());
    this.properties = new ReportProperties();
    this.styleSheetCollection = new StyleSheetCollection();
    this.reportBuilderHints = new ReportBuilderHints();

    this.groups = new GroupList();
    checkGroups();
    this.groups.registerStyleSheetCollection(this.styleSheetCollection);

    this.reportHeader = new ReportHeader();
    this.reportHeader.registerStyleSheetCollection(this.styleSheetCollection);
    this.reportFooter = new ReportFooter();
    this.reportFooter.registerStyleSheetCollection(this.styleSheetCollection);
    this.pageHeader = new PageHeader();
    this.pageHeader.registerStyleSheetCollection(this.styleSheetCollection);
    this.pageFooter = new PageFooter();
    this.pageFooter.registerStyleSheetCollection(this.styleSheetCollection);
    this.itemBand = new ItemBand();
    this.itemBand.registerStyleSheetCollection(this.styleSheetCollection);
    this.watermark = new Watermark();
    this.watermark.registerStyleSheetCollection(this.styleSheetCollection);

    this.data = new DefaultTableModel();
    this.expressions = new ExpressionCollection();
    setPageDefinition(null);
  }

  /**
   * Returns the name of the report.
   * <p>
   * You can reference the report name in your XML report template file using the key
   * '<code>report.name</code>'.
   *
   * @return the name.
   */
  public String getName()
  {
    final Object name = getProperty(NAME_PROPERTY);
    return String.valueOf(name);
  }

  /**
   * Sets the name of the report.
   * <P>
   * The report name is stored as a property (key: <code>NAME_PROPERTY = "report.name"</code>) of
   * the report.  If you supply <code>null</code> as the name, the property is removed.
   *
   * @param name  the name of the report.
   */
  public void setName(final String name)
  {
    setProperty(NAME_PROPERTY, name);
  }

  /**
   * Adds a property to the report. If a property with the given name
   * already exists, the property will be updated with the new value. If the
   * supplied value is <code>null</code>, the property will be removed.
   * <P>
   * Developers are free to add any properties they want to a report, and then display those
   * properties in the report.  For example, you might add a 'user.name' property, so that you
   * can display the username in the header of a report.
   *
   * @param key  the key.
   * @param value  the value.
   */
  public void setProperty(final String key, final Object value)
  {
    this.properties.put(key, value);
  }

  /**
   * Returns the report properties collection for this report.
   * <p>
   * These properties are inherited to all ReportStates generated for this report.
   *
   * @return the report properties.
   */
  public ReportProperties getProperties()
  {
    return properties;
  }

  /**
   * Returns the value of the property with the specified key.
   *
   * @param key  the key.
   *
   * @return the property value.
   */
  public Object getProperty(final String key)
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
   * @param key  the key.
   *
   * @return the property value.
   */
  public boolean isPropertyMarked(final String key)
  {
    return this.properties.isMarked(key);
  }

  /**
   * Returns the value of the property with the specified key.
   *
   * @param key  the key.
   * @param mark the new marking flag
   */
  public void setPropertyMarked(final String key, final boolean mark)
  {
    this.properties.setMarked(key, mark);
  }

  /**
   * Sets the report header.
   *
   * @param header  the report header (<code>null</code> not permitted).
   */
  public void setReportHeader(final ReportHeader header)
  {
    if (header == null)
    {
      throw new NullPointerException("JFreeReport.setReportHeader(...) : null not permitted.");
    }

    this.reportHeader.unregisterStyleSheetCollection(getStyleSheetCollection());
    this.reportHeader = header;
    this.reportHeader.registerStyleSheetCollection(getStyleSheetCollection());
  }

  /**
   * Returns the report header.
   *
   * @return the report header (never <code>null</code>).
   */
  public ReportHeader getReportHeader()
  {
    return reportHeader;
  }

  /**
   * Sets the report footer.
   *
   * @param footer  the report footer (<code>null</code> not permitted).
   */
  public void setReportFooter(final ReportFooter footer)
  {
    if (footer == null)
    {
      throw new NullPointerException("JFreeReport.setReportFooter(...) : null not permitted.");
    }

    this.reportFooter.unregisterStyleSheetCollection(getStyleSheetCollection());
    this.reportFooter = footer;
    this.reportFooter.registerStyleSheetCollection(getStyleSheetCollection());
  }

  /**
   * Returns the page footer.
   *
   * @return the report footer (never <code>null</code>).
   */
  public ReportFooter getReportFooter()
  {
    return reportFooter;
  }

  /**
   * Sets the page header.
   *
   * @param header  the page header (<code>null</code> not permitted).
   */
  public void setPageHeader(final PageHeader header)
  {
    if (header == null)
    {
      throw new NullPointerException("JFreeReport.setPageHeader(...) : null not permitted.");
    }

    this.pageHeader.unregisterStyleSheetCollection(getStyleSheetCollection());
    this.pageHeader = header;
    this.pageHeader.registerStyleSheetCollection(getStyleSheetCollection());
  }

  /**
   * Returns the page header.
   *
   * @return the page header (never <code>null</code>).
   */
  public PageHeader getPageHeader()
  {
    return pageHeader;
  }

  /**
   * Sets the page footer.
   *
   * @param footer  the page footer (<code>null</code> not permitted).
   */
  public void setPageFooter(final PageFooter footer)
  {
    if (footer == null)
    {
      throw new NullPointerException("JFreeReport.setPageFooter(...) : null not permitted.");
    }

    this.pageFooter.unregisterStyleSheetCollection(getStyleSheetCollection());
    this.pageFooter = footer;
    this.pageFooter.registerStyleSheetCollection(getStyleSheetCollection());
  }

  /**
   * Returns the page footer.
   *
   * @return the page footer (never <code>null</code>).
   */
  public PageFooter getPageFooter()
  {
    return pageFooter;
  }

  /**
   * Sets the watermark band for the report.
   *
   * @param band  the new watermark band (<code>null</code> not permitted).
   */
  public void setWatermark(final Watermark band)
  {
    if (band == null)
    {
      throw new NullPointerException("JFreeReport.setWatermark(...) : null not permitted.");
    }

    this.watermark.unregisterStyleSheetCollection(getStyleSheetCollection());
    this.watermark = band;
    this.watermark.registerStyleSheetCollection(getStyleSheetCollection());
  }

  /**
   * Returns the report's watermark band.
   *
   * @return the watermark band (never <code>null</code>).
   */
  public Watermark getWatermark()
  {
    return this.watermark;
  }

  /**
   * Sets the item band for the report.
   *
   * @param band  the new item band (<code>null</code> not permitted).
   */
  public void setItemBand(final ItemBand band)
  {
    if (band == null)
    {
      throw new NullPointerException("JFreeReport.setItemBand(...) : null not permitted.");
    }

    this.itemBand.unregisterStyleSheetCollection(getStyleSheetCollection());
    this.itemBand = band;
    this.itemBand.registerStyleSheetCollection(getStyleSheetCollection());
  }

  /**
   * Returns the report's item band.
   *
   * @return the item band (never <code>null</code>).
   */
  public ItemBand getItemBand()
  {
    return this.itemBand;
  }

  /**
   * Adds a group to the report.
   *
   * @param group  the group.
   */
  public void addGroup(final Group group)
  {
    groups.add(group);
  }

  /**
   * Sets the groups for this report. If no list (null) or an
   * empty list is given, an default group is created. This default
   * group contains no elements and starts at the first record of the
   * data and ends on the last record.
   *
   * @param groupList  the list of groups.
   */
  public void setGroups(final GroupList groupList)
  {
    if (groupList == null)
    {
      throw new NullPointerException("GroupList must not be null");
    }

    this.groups.clear();
    final Iterator it = groupList.iterator();
    while (it.hasNext())
    {
      addGroup((Group) it.next());
    }
    checkGroups();
  }

  /**
   * Verifies the group list and adds the default group to the list if necessary.
   */
  protected void checkGroups()
  {
    // if this was an empty group, fix it by adding an default group
    if (groups.size() == 0)
    {
      final Group defaultGroup = new Group();
      defaultGroup.setName("default");
      addGroup(defaultGroup);
    }
  }

  /**
   * Returns a clone of the list of groups for the report.
   *
   * @return the group list.
   */
  public GroupList getGroups()
  {
    return (GroupList) this.groups.clone();
  }

  /**
   * Returns the number of groups in this report.
   * <P>
   * Every report has at least one group defined.
   *
   * @return the group count.
   */
  public int getGroupCount()
  {
    return groups.size();
  }

  /**
   * Returns the group at the specified index or null, if there is no such group.
   *
   * @param count  the group index.
   *
   * @return the requested group.
   *
   * @throws IllegalArgumentException if the count is negative.
   * @throws IndexOutOfBoundsException if the count is greater than the number of defined groups.
   */
  public Group getGroup(final int count)
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
   * Searches a group by its defined name. This method returns null, if the
   * group was not found.
   *
   * @param name the name of the group.
   * @return the group or null if not found.
   * @see GroupList#getGroupByName
   */
  public Group getGroupByName(final String name)
  {
    return groups.getGroupByName(name);
  }

  /**
   * Adds a function to the report's collection of functions.
   *
   * @param function  the function.
   *
   * @throws FunctionInitializeException if any of the functions cannot be initialized.
   */
  public void addExpression(final Expression function) throws FunctionInitializeException
  {
    expressions.add(function);
  }

  /**
   * Adds a function to the report's collection of functions.
   *
   * @param function  the function.
   *
   * @throws FunctionInitializeException if any of the functions cannot be initialized.
   * @deprecated use addExpression instead.
   */
  public void addFunction(final Function function) throws FunctionInitializeException
  {
    expressions.add(function);
  }

  /**
   * Returns the default page format.
   *
   * @return the page definition.
   */
  public PageDefinition getPageDefinition()
  {
    return pageDefinition;
  }

  /**
   * Defines the default page format for this report. The default is a hint
   * to define at least one suitable format. If no format is defined the system's default
   * page format is used.
   *
   * @param format  the default format (<code>null</code> permitted).
   */
  public void setPageDefinition(PageDefinition format)
  {
    if (format == null)
    {
      if (ReportConfiguration.getGlobalConfig().getConfigProperty
          (ReportConfiguration.NO_PRINTER_AVAILABLE, "false").equals("true"))
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
   * <P>
   * Reports are generated from a {@link TableModel} (as used by Swing's
   * {@link javax.swing.JTable}). If you don't want to give data to the report, use an empty
   * {@link TableModel} instead of <code>null</code>.
   *
   * @param data  the data for the report (<code>null</code> not permitted).
   */
  public void setData(final TableModel data)
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
  public TableModel getData()
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
  public Object clone() throws CloneNotSupportedException
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
    report.groups.updateStyleSheetCollection(report.styleSheetCollection);
    report.itemBand.updateStyleSheetCollection(report.styleSheetCollection);
    report.watermark.updateStyleSheetCollection(report.styleSheetCollection);
    report.reportFooter.updateStyleSheetCollection(report.styleSheetCollection);
    report.reportHeader.updateStyleSheetCollection(report.styleSheetCollection);
    report.pageFooter.updateStyleSheetCollection(report.styleSheetCollection);
    report.pageHeader.updateStyleSheetCollection(report.styleSheetCollection);
    report.reportBuilderHints = new ReportBuilderHints();
    return report;
  }

  /**
   * Returns information about the JFreeReport class library.
   *
   * @return the information.
   */
  public static final JFreeReportInfo getInfo()
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
  public ExpressionCollection getExpressions()
  {
    return expressions;
  }

  /**
   * Sets the expressions for the report.
   *
   * @param expressions  the expressions (<code>null</code> not permitted).
   */
  public void setExpressions(final ExpressionCollection expressions)
  {
    if (expressions == null)
    {
      throw new NullPointerException("JFreeReport.setExpressions(...) : null not permitted.");
    }
    this.expressions = expressions;
  }

  /**
   * Returns the report configuration.
   * <p>
   * The report configuration is automatically set up when the report is first created, and uses
   * the global JFreeReport configuration as its parent.
   *
   * @return the report configuration.
   */
  public ReportConfiguration getReportConfiguration()
  {
    return reportConfiguration;
  }

  /**
   * Returns the stylesheet collection of this report. The stylesheet collection
   * is fixed for the report and all elements of the report. When a band or
   * group is added to the report it will get registered with this stylesheet
   * collection and cannot be used in an different report.
   *
   * @return the stylesheet collection of the report, never null.
   */
  public StyleSheetCollection getStyleSheetCollection()
  {
    return styleSheetCollection;
  }

  /**
   * Returns the report builder hints collection assigned with this report.
   * Be aware that these hints are not cloned and that during the cloning all
   * references to this ReportBuilderHints instance get replaced by an newly
   * created instance.
   *
   * @return the report builder hints.
   */
  public ReportBuilderHints getReportBuilderHints()
  {
    return reportBuilderHints;
  }
}
