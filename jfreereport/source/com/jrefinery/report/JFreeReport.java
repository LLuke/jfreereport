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
 * $Id: JFreeReport.java,v 1.25 2002/08/19 22:06:02 taqua Exp $
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
 * 11-May-2002 : All bands have to be initialized. Null is no longer allowed for pageHeader,
 *               pageFooter, reportHeader, reportFooter, itemBand, functionCollection.
 * 17-May-2002 : Fixed reportPropertyInitialisation and checked if the report is proceeding on
 *               print.
 * 26-May-2002 : Changed repagination behaviour. Reports are repaginated before printed, so that
 *               global initialisations can be done.
 * 05-Jun-2002 : Updated Javadoc comments (DG);
 * 08-Jun-2002 : The defaultPageFormat is now always filled (and used in PreviewFrame)
 * 19-Jun-2002 : more documentation
 * 03-Jul-2002 : Serializable and cloneable, Removed JFreeReportInfo field, it disrupts the serializable process
 * 26-Jul-2002 : Removed method "isLastItemInHigherGroups()". The same functionality is implemented
 *               in Group.isLastItemInGroup()
 */

package com.jrefinery.report;

import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.function.Function;
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.report.function.Expression;
import com.jrefinery.report.targets.OutputTarget;
import com.jrefinery.report.targets.OutputTargetException;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.util.ReportProperties;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

/**
 * This class co-ordinates the process of generating a report from a TableModel.  The report is
 * made up of 'bands', which are used repeatedly as necessary to generate small sections of the
 * report.
 * <p>
 * Accessing the bands and the elements:
 * <p>
 * Elements on a band can be reached by using Band.getElement (String elementName) or
 * by retrieving all elements using Band.getElements() and performing a search on the returned list.
 * <p>
 * The different band can be accessed using the main report definition class (this JFreeReport-class).
 * <ul>
 * <li>PageHeader and -footer can be reached by using <code>getPageHeader()</code> and <code>getPageFooter()</code>
 * <li>ReportHeader and -footer can be reached by using <code>getReportHeader()</code> and <code>getReportFooter()</code>
 * <li>the ItemBand is reachable with getItemBand ()
 * <li>Groups can be queries using <code>getGroup(String groupName)</code>, groupheader and footer are accessible
 * through the group object, so use <code>getGroup(String groupName).getGroupHeader()<code> and
 * <code>getGroup(String groupName).getGroupFooter()<code>.
 * </ul>
 */
public class JFreeReport implements JFreeReportConstants, Cloneable, Serializable
{
  private static JFreeReportInfo INFO;

  /** The report name. */
  private String name;

  /** Storage for arbitrary properties that a user can assign to the report. */
  private ReportProperties properties;

  /** An ordered list of report groups (each group defines its own header and footer). */
  private GroupList groups;

  /** Storage for the functions in the report. */
  private FunctionCollection _functions;

  private ExpressionCollection expressions;

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

  /** The page format for the report (determines the page size, and therefore the report width). */
  private PageFormat defaultPageFormat;

  /**
   * The default constructor. Creates an empty but fully initialized report.
   */
  public JFreeReport ()
  {
    this.properties = new ReportProperties ();
    this.groups = new GroupList ();

    setName (null);
    setReportHeader (new ReportHeader ());
    setReportFooter (new ReportFooter ());
    setPageHeader (new PageHeader ());
    setPageFooter (new PageFooter ());
    setData (new DefaultTableModel ());
    setDefaultPageFormat (null);
    setItemBand (new ItemBand ());
    setGroups (new GroupList ());
    setFunctions (new FunctionCollection ());
    setExpressions(new ExpressionCollection());
  }

  /**
   * Constructs a report with the specified attributes.
   *
   * @param name The name of the report.
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
  public JFreeReport (
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
    this ();
    setName (name);
    setReportHeader (reportHeader);
    setReportFooter (reportFooter);
    setPageHeader (pageHeader);
    setPageFooter (pageFooter);
    setData (data);
    setDefaultPageFormat (defaultPageFormat);

    setItemBand (itemBand);
    setGroups (groups);

    // store the functions in a Map using the function name as the key.

    setFunctions (new FunctionCollection (functions));
  }

  /**
   * Constructs a report with the specified attributes.
   *
   * @param name The name of the report.
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
  public JFreeReport (
          String name,
          ReportHeader reportHeader,
          ReportFooter reportFooter,
          PageHeader pageHeader,
          PageFooter pageFooter,
          ItemBand itemBand,
          GroupList groups,
          Collection functions,
          TableModel data,
          PageFormat defaultPageFormat,
          ExpressionCollection expressions)
          throws FunctionInitializeException
  {
    this (name, reportHeader, reportFooter, pageHeader, pageFooter, itemBand,
          groups, functions, data, defaultPageFormat);
    setExpressions(expressions);
  }


  /**
   * Sets the item band for the report. If the ItemBand is null, an
   * empty itemband is created
   *
   * @param band The new item band.
   */
  public void setItemBand (ItemBand band)
  {
    if (band == null)
    {
      throw new NullPointerException ("An Itemband must not be null");
    }
    this.itemBand = band;
  }

  /**
   * Returns the name of the report.
   *
   * @return The name.
   */
  public String getName ()
  {
    Object name = getProperty (NAME_PROPERTY);
    return String.valueOf (name);
  }

  /**
   * Sets the name of the report.
   * <P>
   * The report name is stored as a property (NAME_PROPERTY = "report.name") of the report.  If you
   * supply null as the name, the property is removed.
   *
   * @param name The name of the report.
   */
  public void setName (String name)
  {
    setProperty (NAME_PROPERTY, name);
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
  public void setProperty (String key, Object value)
  {
    properties.put (key, value);
  }

  /**
   * returns the report properties collection for this report. These properties are
   * inherited to all ReportStates generated for this report.
   */
  public ReportProperties getProperties ()
  {
    return properties;
  }

  /**
   * Returns the value of the property with the specified key.
   *
   * @param key The key.
   * @return The property value.
   */
  public Object getProperty (String key)
  {
    if (key == null) throw new NullPointerException ();
    return this.properties.get (key);
  }

  /**
   * Sets the report header (null permitted).
   *
   * @param header The report header.
   */
  public void setReportHeader (ReportHeader header)
  {
    if (header == null)
      throw new NullPointerException ("ReportHeader must not be null");

    this.reportHeader = header;
  }

  /**
   * @return the report footer or null, if no report footer is defined.
   */
  public ReportHeader getReportHeader ()
  {
    return reportHeader;
  }

  /**
   * Sets the report footer (null permitted).
   *
   * @param footer The report footer.
   */
  public void setReportFooter (ReportFooter footer)
  {
    if (footer == null)
      throw new NullPointerException ("ReportFooter must not be null");

    this.reportFooter = footer;
  }

  /**
   * @return the report footer or null, if no report footer is defined.
   */
  public ReportFooter getReportFooter ()
  {
    return reportFooter;
  }

  /**
   * Sets the page header (null permitted).
   *
   * @param header The page header.
   */
  public void setPageHeader (PageHeader header)
  {
    if (header == null)
      throw new NullPointerException ("PageHeader must not be null");

    this.pageHeader = header;
  }

  /**
   * @return the page header or null, if no page header is defined.
   */
  public PageHeader getPageHeader ()
  {
    return pageHeader;
  }

  /**
   * Sets the page footer (null permitted).
   *
   * @param footer The page footer.
   */
  public void setPageFooter (PageFooter footer)
  {
    if (footer == null)
      throw new NullPointerException ("PageFooter must not be null");

    this.pageFooter = footer;
  }

  /**
   * @return the page footer or null, if no page footer is defined.
   */
  public PageFooter getPageFooter ()
  {
    return pageFooter;
  }

  /**
   * @return The item band for the report.
   */
  public ItemBand getItemBand ()
  {
    return this.itemBand;
  }

  /**
   * Adds a group to the report.
   *
   * @param group The group.
   */
  public void addGroup (Group group)
  {
    groups.add (group);
  }

  /**
   * Sets the groups for this report. If no list (null) or an
   * empty list is given, an default group is created. This default
   * group contains no elements and starts at the first record of the
   * data and ends on the last record.
   *
   * @param groupList The list of groups.
   */
  public void setGroups (GroupList groupList)
  {
    if (groupList == null)
    {
      throw new NullPointerException ("GroupList must not be null");
    }

    this.groups.clear ();
    Iterator it = groupList.iterator ();
    while (it.hasNext ())
    {
      addGroup ((Group) it.next ());
    }

    // if this was an empty group, fix it by adding an default group
    if (groups.size () == 0)
    {
      Group defaultGroup = new Group ();
      defaultGroup.setName ("default");
      addGroup (defaultGroup);
    }
  }

  /**
   * Returns the list of groups for the report.
   *
   * @return The group list.
   */
  public GroupList getGroups ()
  {
    return this.groups;
  }

  /**
   * Returns the number of groups in this report.
   * <P>
   * Every report has at least one group defined.
   *
   * @return The group count.
   */
  public int getGroupCount ()
  {
    return groups.size ();
  }

  /**
   * Returns the group at the specified index or null, if there is no such group.
   *
   * @param count The group index.
   *
   * @return the requested group.
   *
   * @throws IllegalArgumentException if the count is negative.
   * @throws IndexOutOfBoundsException if the count is greater than the number of defined groups.
   */
  public Group getGroup (int count)
  {
    if (count < 0)
      throw new IllegalArgumentException ("GroupCount must not be negative");

    if (count >= groups.size ())
      throw new IndexOutOfBoundsException (
              "No such group defined. " + count + " vs. " + groups.size ());

    return (Group) groups.get (count);
  }

  /**
   * Adds a function to the report's collection of functions.
   *
   * @param function The function.
   */
  public void addExpression (Expression function) throws FunctionInitializeException
  {
    expressions.add (function);
  }

  /**
   * Adds a function to the report's collection of functions.
   *
   * @param function The function.
   */
  public void addFunction (Function function) throws FunctionInitializeException
  {
    _functions.add (function);
  }

  /**
   * Returns the report's collection of functions.
   *
   * @return The function collection.
   */
  public FunctionCollection getFunctions ()
  {
    return _functions;
  }

  /**
   * Sets the function collection.
   *
   * @param functions The collection of functions.
   */
  public void setFunctions (FunctionCollection functions)
  {
    if (functions == null)
    {
      throw new NullPointerException ("Null-Function collection is not allowed!");
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
  public PageFormat getDefaultPageFormat ()
  {
    return defaultPageFormat;
  }

  /**
   * Defines the default page format for this report. The defaultPageFormat is a hint
   * to define at least one suitable format. If no format is defined the systems default
   * page format is used.
   *
   * @param formt the default format or null, if no such format has been specified.
   */
  public void setDefaultPageFormat (PageFormat format)
  {
    if (format == null)
    {
      format = PrinterJob.getPrinterJob ().defaultPage ();
    }
    defaultPageFormat = format;
  }

  /**
   * Sets the data for the report.
   * <P>
   * Reports are generated from a TableModel (as used by Swing's JTable). If you don't want to
   * give data to the report, use an empty TableModel instead of null.
   *
   * @param data The data for the report.
   *
   * @throws NullPointerException if the given data is null.
   */
  public void setData (TableModel data)
  {
    if (data == null)
      throw new NullPointerException ("Data must not be null");

    this.data = data;
  }

  /**
   * Returns the current data for this report.
   *
   * @return The data in form of a table model.
   */
  public TableModel getData ()
  {
    return data;
  }

  public ReportState processReport (OutputTarget target)
          throws ReportProcessingException
  {
    return JFreeReport.processReport(target, this);
  }

  /**
   * Sends the entire report to the specified target. The report is always drawn.
   *
   * @param target The output target.
   *
   * @return The last state of the report, usually ReportState.Finish
   *
   * @throws ReportProcessingException if the report did not proceed and got stuck.
   */
  public static ReportState processReport (OutputTarget target, JFreeReport report)
          throws ReportProcessingException
  {
    int page = 1;
    ReportState rs = new ReportState.Start (report);
    ReportProcessor prc = new ReportProcessor (target, true, rs.getReport().getPageFooter ());

    // To a repagination
    ReportStateList rl = repaginate (target, rs);
    rl.clear ();
    rs = rs.advance (prc);

    rs = processPage (target, rs, true);
    while (!rs.isFinish ())
    {
      ReportState nrs = processPage (target, rs, true);
      Log.error (String.valueOf (rs.getProperty (REPORT_DATE_PROPERTY)));
      if (nrs.isProceeding (rs) == false)
      {
        throw new ReportProcessingException ("Report is not proceeding");
      }
      rs = nrs;
    }
    return rs;
  }

  /**
   * Processes the entire report and records the state at the end of every page.
   *
   * @param output The output target.
   * @param state The report state.
   */
  public static ReportStateList repaginate (OutputTarget output, ReportState state)
          throws ReportProcessingException
  {
    if (state.isStart () != true) throw new ReportProcessingException ("Need a start state for repagination");
    ReportStateList pageStates = new ReportStateList (state.getReport(), output);

    ReportProcessor prc = new ReportProcessor (output, false, state.getReport().getPageFooter ());
    state = state.advance (prc);

    state.setProperty (REPORT_PREPARERUN_PROPERTY, Boolean.TRUE);
    pageStates.add (state);
    state = processPage (output, state, false);
    while (!state.isFinish ())
    {
      pageStates.add (state);
      ReportState oldstate = state;
      state = processPage (output, state, false);

      if (!state.isProceeding (oldstate))
      {
        throw new ReportProcessingException ("State did not proceed, bailing out!");
      }
    }
    state.setProperty (REPORT_PAGECOUNT_PROPERTY, new Integer (state.getCurrentPage () - 1));
    state.setProperty (REPORT_PAGEFORMAT_PROPERTY, output.getPageFormat ());
    state.setProperty (REPORT_PREPARERUN_PROPERTY, Boolean.FALSE);
    return pageStates;
  }

  /**
   * Draws a single page of the report to the specified graphics device, and returns state
   * information.  The caller should check the returned state to ensure that some progress has
   * been made, because on some small paper sizes the report may get stuck (particularly if the
   * header and footer are large).
   * <p>
   * To check the progress, use ReportState.isProceeding(oldstate).
   *
   * @param target The output target.
   * @param currPage The report state at the beginning of the current page.
   * @param draw A flag that indicates whether or not the report is being written to the target.
   *
   * @return The report state suitable for the next page or ReportState.Finish.
   *
   * @throws IllegalArgumentException if the given state is a start or a finish state.
   */
  public static ReportState processPage (
          OutputTarget target,
          final ReportState currPage,
          boolean draw) throws ReportProcessingException
  {
    if (currPage.isStart ())
    {
      throw new IllegalArgumentException ("No start state for processpage allowed");
    }
    if (currPage.isFinish ())
    {
      throw new IllegalArgumentException ("No finish state for processpage allowed");
    }
    ReportState state = (ReportState) currPage.clone ();
    try
    {
      if (draw) target.beginPage ();
    }
    catch (OutputTargetException e)
    {
      Log.error ("Failed to start page " + state.getCurrentPage (), e);
      throw new ReportProcessingException ("Failed to start page " + state.getCurrentPage ());
    }

    int page = state.getCurrentPage ();
    boolean pageDone = false;
    ReportProcessor prc = new ReportProcessor (target, draw, state.getReport().getPageFooter ());

    // Print the pageHeader before any other item.
    ReportEvent event = new ReportEvent (state);
    state.firePageStartedEvent (event);
    //getPageHeader ().populateElements (state);
    if (page == 1)
    {
      if (state.getReport().getPageHeader ().isDisplayOnFirstPage ())
      {
        prc.printPageHeader (state.getReport().getPageHeader ());
      }
    }
    else
    {
      prc.printPageHeader (state.getReport().getPageHeader ());
    }

    // Do some real work.  The report header and footer, and the page headers and footers are
    // just decorations, as far as the report state is concerned.  The state only changes in
    // the following code...
    while (!prc.isPageDone ())
    {
      state = state.advance (prc);
    }

    // All work is done for this page. Just print the pageFooter and then return.
    event = new ReportEvent (state);
    state.firePageFinishedEvent (event);

    //getPageFooter ().populateElements (state);
    if (page == 1)
    {
      if (state.getReport().getPageFooter ().isDisplayOnFirstPage ())
      {
        prc.printPageFooter (state.getReport().getPageFooter ());
      }
    }
    else
    {
      prc.printPageFooter (state.getReport().getPageFooter ());
    }

    // return the state at the end of the page...
    try
    {
      if (draw) target.endPage ();
    }
    catch (OutputTargetException e)
    {
      Log.error ("Failed to end page " + state.getCurrentPage (), e);
      throw new ReportProcessingException ("Failed to end page " + state.getCurrentPage ());
    }
    state.nextPage ();
    return state;
  }

  public Object clone () throws CloneNotSupportedException
  {
    JFreeReport report = (JFreeReport) super.clone ();
    report._functions = (FunctionCollection) _functions.clone ();
    report.data = data; // data is defined to be immutable, so don't clone the thing
    report.defaultPageFormat = (PageFormat) defaultPageFormat.clone ();
    report.groups = (GroupList) groups.clone ();
    report.itemBand = (ItemBand) itemBand.clone ();
    report.pageFooter = (PageFooter) pageFooter.clone ();
    report.pageHeader = (PageHeader) pageHeader.clone ();
    report.properties = (ReportProperties) properties.clone ();
    report.reportFooter = (ReportFooter) reportFooter.clone ();
    report.reportHeader = (ReportHeader) reportHeader.clone ();
    report.expressions = (ExpressionCollection) expressions.clone();
    return report;
  }

  private void readObject (java.io.ObjectInputStream in)
          throws IOException, ClassNotFoundException
  {
    in.defaultReadObject ();
  }

  public static final JFreeReportInfo getInfo ()
  {
    if (INFO == null)
    {
      INFO = new JFreeReportInfo ();
    }
    return INFO;
  }

  public ExpressionCollection getExpressions()
  {
    return expressions;
  }

  public void setExpressions(ExpressionCollection expressions)
  {
    if (expressions == null) throw new NullPointerException();
    this.expressions = expressions;
  }
}
