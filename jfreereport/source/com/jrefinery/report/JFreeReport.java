/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  David Gilbert (david.gilbert@jrefinery.com);
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: JFreeReport.java,v 1.5 2002/05/16 19:58:24 jaosch Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 04-Mar-2002 : Major changes to report engine to incorporate functions and different output
 *               targets (DG);
 * 24-Apr-2002 : ItemBand and Groups are Optional Elements, default Elements are created as needed
 * 01-May-2002 : Renamed addProperty to setProperty to create consistent naming among other property uses.
 * 07-May-2002 : Fixed bug where last row of data is left off the report if it is alone in a
 *               group, reported by Steven Feinstein (DG);
 * 10-May-2002 : Rewrote report-processing. All reportstate-changes are handled by ReportState
 *               Objects. Created AccessorMethods for Properties. (TM)
 * 11-May-2002 : All bands have to be initialized. Null is no longer allowed for pageHeader,pageFooter,
 *               reportHeader, reportFooter, itemBand, functionCollection
 *
 */

package com.jrefinery.report;

import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.function.Function;
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.ui.about.ProjectInfo;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.print.PageFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class co-ordinates the process of generating a report from a TableModel.  The report is
 * made up of 'bands', which are used repeatedly as necessary to generate small sections of the
 * report.
 */
public class JFreeReport implements JFreeReportConstants
{
  public static final ProjectInfo INFO = new JFreeReportInfo();

  /** The report name. */
  private String name;

  /** Storage for arbitrary properties that a user can assign to a report.*/
  private Map properties;

  /** An ordered list of report groups (each group defines its own header and footer). */
  private GroupList groups;

  /** Storage for the functions in the report. */
  private FunctionCollection _functions;

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

  /** The table model containing the data for the report. */
  private TableModel data;

  /** 
   * The page format used for the report (determines the page size, and therefore the report
   * width). */
  private PageFormat defaultPageFormat;

  private Group defaultGroup;

  /**
   * The default constructor. Creates an empty but fully initialized report.
   */
  public JFreeReport()
  {
    this.properties = new TreeMap();
    this.groups = new GroupList();

    setName(null);
    setReportHeader(new ReportHeader());
    setReportFooter(new ReportFooter());
    setPageHeader(new PageHeader());
    setPageFooter(new PageFooter());
    setData(new DefaultTableModel());
    setDefaultPageFormat(null);
    setItemBand(new ItemBand());
    setGroups(new GroupList());
    setFunctions(new FunctionCollection());
  }

  /**
   * Constructs a report with the specified attributes.
   * @param name the name of the report
   * @param reportHeader The report header <i>not null</i>.
   * @param reportFooter The report footer <i>not null</i>.
   * @param pageHeader The page header <i>not null</i>.
   * @param pageFooter The page footer <i>not null</i>.
   * @param itemBand The item band <i>not null</i>.
   * @param groups The report groups <i>not null</i>.
   * @param data The data for the report <i>not null</i>.
   * @param defaultPageFormat The default page format.
   * @throws NullPointerException if one of the <i>not null</i>-parameters is null.
   */
  public JFreeReport(
    String name,
    ReportHeader reportHeader,
    ReportFooter reportFooter,
    PageHeader pageHeader,
    PageFooter pageFooter,
    ItemBand itemBand,
    GroupList groups,
    Collection functions,
    TableModel data,
    PageFormat defaultPageFormat)
    throws FunctionInitializeException
  {
    this();
    setName(name);
    setReportHeader(reportHeader);
    setReportFooter(reportFooter);
    setPageHeader(pageHeader);
    setPageFooter(pageFooter);
    setData(data);
    setDefaultPageFormat(defaultPageFormat);

    setItemBand(itemBand);
    setGroups(groups);

    // store the functions in a Map using the function name as the key.

    setFunctions(new FunctionCollection(functions));
  }

  /**
   * Sets the item band for the report. If the ItemBand is null, an
   * empty itemband is created
   *
   * @param band The new item band.
   */
  public void setItemBand(ItemBand band)
  {
    if (band == null)
    {
      throw new NullPointerException("An Itemband must not be null");
    }
    this.itemBand = band;
  }

  /**
   * @return the name of the report.
   */
  public String getName()
  {
    Object name = getProperty(NAME_PROPERTY);
    return String.valueOf(name);
  }

  /**
   * Sets the name of the report. This defines the NAME_PROPERTY.
   *
   * @param name the name of the report. If name is null, the property will be removed.
   */
  public void setName(String name)
  {
    setProperty(NAME_PROPERTY, name);
  }

  /**
   * Adds a property to the report. If a property with the given name
   * exist, the property will be replaced with the new value. If the
   * value is null, the property will be removed.
   * <P>
   * Developers are free to add any properties they want to a report.  Use a
   * ReportPropertyFunction to retrieve the property during report generation.
   *
   * @param key The key.
   * @param value The value.
   */
  public void setProperty(String key, Object value)
  {
    if (value == null)
    {
      this.properties.remove(key);
    }
    else
    {
      this.properties.put(key, value);
    }
  }

  /**
   * Returns the value of the property with the specified key.
   *
   * @param key The key.
   * @return The property value.
   */
  public Object getProperty(String key)
  {
    if (key == null)
      throw new NullPointerException();
    return this.properties.get(key);
  }

  /**
   * Sets the report header (null permitted).
   *
   * @param header The report header.
   */
  public void setReportHeader(ReportHeader header)
  {
    if (header == null)
      throw new NullPointerException("ReportHeader must not be null");

    this.reportHeader = header;
  }

  /**
   * @return the report footer or null, if no report footer is defined.
   */
  public ReportHeader getReportHeader()
  {
    return reportHeader;
  }

  /**
   * Sets the report footer (null permitted).
   *
   * @param footer The report footer.
   */
  public void setReportFooter(ReportFooter footer)
  {
    if (footer == null)
      throw new NullPointerException("ReportFooter must not be null");

    this.reportFooter = footer;
  }

  /**
   * @return the report footer or null, if no report footer is defined.
   */
  public ReportFooter getReportFooter()
  {
    return reportFooter;
  }

  /**
   * Sets the page header (null permitted).
   *
   * @param header The page header.
   */
  public void setPageHeader(PageHeader header)
  {
    if (header == null)
      throw new NullPointerException("PageHeader must not be null");

    this.pageHeader = header;
  }

  /**
   * @return the page header or null, if no page header is defined.
   */
  public PageHeader getPageHeader()
  {
    return pageHeader;
  }

  /**
   * Sets the page footer (null permitted).
   *
   * @param footer The page footer.
   */
  public void setPageFooter(PageFooter footer)
  {
    if (footer == null)
      throw new NullPointerException("PageFooter must not be null");

    this.pageFooter = footer;
  }

  /**
   * @return the page footer or null, if no page footer is defined.
   */
  public PageFooter getPageFooter()
  {
    return pageFooter;
  }

  /**
   * @return the item band for the report.
   */
  public ItemBand getItemBand()
  {
    return this.itemBand;
  }

  /**
   * Adds a group to the report.
   *
   * @param group The group.
   */
  public void addGroup(Group group)
  {
    groups.add(group);
  }

  /**
   * Sets the groups for this report. If no list (null) or an
   * empty list is given, an default group is created. This default
   * group contains no elements and starts at the first record of the
   * data and ends on the last record.
   */
  public void setGroups(GroupList groupList)
  {
    if (groupList == null)
    {
      throw new NullPointerException("GroupList must not be null");
    }

    this.groups.clear();
    Iterator it = groupList.iterator();
    while (it.hasNext())
    {
      addGroup((Group) it.next());
    }

    // if this was an empty group, fix it by adding an default group
    if (groups.size() == 0)
    {
      Group defaultGroup = new Group();
      defaultGroup.setName("default");
      addGroup(defaultGroup);
    }
  }

  /**
   * Returns the list of groups for the report.
   * @return The list of groups for the report.
   */
  public GroupList getGroups()
  {
    return this.groups;
  }

  /**
   * returns the number of groups in this report. Every report has at least one group
   * defined.
   *
   * @return the number of groups in the report.
   */
  public int getGroupCount()
  {
    return groups.size();
  }

  /**
   * Returns the group at the specified index or null, if there is no such group.
   *
   * @throws IllegalArgumentException if the count is negative.
   * @throws IndexOutOfBoundsException if the count is greater than the number of defined groups.
   * @return the requested group.
   */
  public Group getGroup(int count)
  {
    if (count < 0)
      throw new IllegalArgumentException("GroupCount must not be negative");

    if (count >= groups.size())
      throw new IndexOutOfBoundsException(
        "No such group defined. " + count + " vs. " + groups.size());

    return (Group) groups.get(count);
  }

  /**
   * Adds a function to the report's collection of functions.
   *
   * @param function The function.
   */
  public void addFunction(Function function) throws FunctionInitializeException
  {
    _functions.add(function);
  }

  /**
   * Returns the report's collection of functions.
   *
   * @return The function collection.
   */
  public FunctionCollection getFunctions()
  {
    return _functions;
  }

  /**
   * Sets the function collection.
   *
   * @param functions The collection of functions.
   */
  public void setFunctions(FunctionCollection functions)
  {
    if (functions == null)
    {
      throw new NullPointerException("Null-Function collection is not allowed!");
    }
    else
    {
      _functions = functions;
    }
  }

  /**
   * Returns the page format that will be used to output the report.
   *
   * @return The current page format.
   */
  public PageFormat getDefaultPageFormat()
  {
    return defaultPageFormat;
  }

  /**
   * Defines the default page format for this report. The defaultPageFormat is a hint
   * to define at least one suitable format.
   *
   * @return the default format or null, if no such format has been specified.
   */
  public void setDefaultPageFormat(PageFormat format)
  {
    defaultPageFormat = format;
  }

  /**
   * Sets the data for the report.  Reports are generated from a TableModel (as used by Swing's
   * JTable). If you don't want to give data to the report, use an empty TableModel instead of null.
   *
   * @param data The data for the report.
   * @throws NullPointerException if the given data is null.
   */
  public void setData(TableModel data)
  {
    if (data == null)
      throw new NullPointerException("Data must not be null");

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
   * Sends the entire report to the specified target.
   *
   * @param target The output target.
   * @return The last state of the report, usually ReportState.Finish
   * @throws ReportProcessingException if the report did not proceed and got stuck.
   */
  public ReportState processReport(OutputTarget target, boolean draw)
    throws ReportProcessingException
  {

    int page = 1;
    ReportState rs = new ReportState.Start(this);

    rs = processPage(target, rs, draw);
    target.endPage();

    while (!(rs instanceof ReportState.Finish))
    {
      rs = processPage(target, rs, draw);
      target.endPage();
    }
    return rs;
  }

  /**
   * Draws a single page of the report to the specified graphics device, and returns state
   * information.  The caller should check the returned state to ensure that some progress has
   * been made, because on some small paper sizes the report may get stuck (particularly if the
   * header and footer are large).
   * <p>
   * To check the progress, use ReportState.isProceeding(oldstate).
   *
   * @param target The graphics device on which the report is being drawn.
   * @param currPage The report state at the beginning of the current page.
   * @param draw A flag that indicates whether or not we are actually drawing to the graphics
   *             device.
   * @return The report state suitable for the next page or ReportState.Finish.
   */
  public ReportState processPage(
    OutputTarget target,
    final ReportState currPage,
    boolean draw)
  {
    ReportState state = (ReportState) currPage.clone();
    int page = state.getCurrentPage();
    boolean pageDone = false;
    ReportProcessor prc = new ReportProcessor(target, draw, pageFooter);

    FunctionCollection functions = state.getFunctions();
    ReportEvent event = new ReportEvent(this, state);
    state.firePageStartedEvent(event);

    // print the page header (if there is one) and measure the page footer (if there is one)...
    // on reportheader start

    pageHeader.populateElements(state);
    if (page == 1)
    {
      if (pageHeader.isDisplayOnFirstPage())
      {
        prc.printPageHeader(pageHeader);
      }
    }
    else
    {
      prc.printPageHeader(pageHeader);
    }

    // Do some real work.  The report header and footer, and the page headers and footers are
    // just decorations, as far as the report state is concerned.  The state only changes in
    // the following code...
    while (!prc.isPageDone())
    {
      state = state.advance(prc);
    }

    // All work is done for this page. Just print the pageFooter and then return.
    event = new ReportEvent(this, state);
    state.firePageFinishedEvent(event);

    pageFooter.populateElements(state);
    if (page == 1)
    {
      if (pageFooter.isDisplayOnFirstPage())
      {
        prc.printPageFooter(pageFooter);
      }
    }
    else
    {
      prc.printPageFooter(pageFooter);
    }

    // return the state at the end of the page...
    state.nextPage();
    return state;
  }

  /**
   * Returns true if the current row is the end of a group.
   * <p>
   * To work as expected, this method assumes that a lower group
   * includes all fields from an higher group.
   */
  public boolean isLastItemInHigherGroups(int row, int groupIndex)
  {
    if (row == data.getRowCount() - 1)
      return true;

    boolean result = false;
    for (int g = groupIndex; g > ReportState.BEFORE_FIRST_GROUP; g--)
    {
      Group group = getGroup(g);
      if (group.isLastItemInGroup(getData(), row))
      {
        return true;
      }
    }
    return false;
  }
}