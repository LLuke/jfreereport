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
 * ----------------
 * JFreeReport.java
 * ----------------
 * (C)opyright 2000-2003, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: JFreeReport.java,v 1.43 2002/12/13 14:55:06 mungady Exp $
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
 * 03-Jan-2002 : More Javadocs (DG);
 *
 */

package com.jrefinery.report;

import com.jrefinery.report.function.Expression;
import com.jrefinery.report.function.ExpressionCollection;
import com.jrefinery.report.function.Function;
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.report.util.ReportConfiguration;
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
 * by retrieving all elements using Band.getElements() and performing a search on the returned
 * list.
 * <p>
 * The different band can be accessed using the main report definition class (this JFreeReport
 * class).
 * <ul>
 * <li>PageHeader and -footer can be reached by using
 *     <code>getPageHeader()</code> and <code>getPageFooter()</code>
 * <li>ReportHeader and -footer can be reached by using
 *     <code>getReportHeader()</code> and <code>getReportFooter()</code>
 * <li>the ItemBand is reachable with getItemBand ()
 * <li>Groups can be queries using <code>getGroup(String groupName)</code>, groupheader and
 *     footer are accessible through the group object, so use
 *     <code>getGroup(String groupName).getGroupHeader()<code> and
 *     <code>getGroup(String groupName).getGroupFooter()<code>.
 * </ul>
 *
 * @author David Gilbert
 * @author Thomas Morgner
 */
public class JFreeReport implements JFreeReportConstants, Cloneable, Serializable
{
  /** Information about the JFreeReport class library. */
  private static JFreeReportInfo info;

  /** The table model containing the data for the report. */
  private TableModel data;

  /** The page format for the report (determines the page size, and therefore the report width). */
  private PageFormat defaultPageFormat;

  /** Storage for arbitrary properties that a user can assign to the report. */
  private ReportProperties properties;

  /** Storage for the functions in the report. */
  private ExpressionCollection functions;

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

  /** The report configuration. */
  private ReportConfiguration reportConfiguration;

  /**
   * The default constructor. Creates an empty but fully initialized report.
   */
  public JFreeReport ()
  {
    this.reportConfiguration = new ReportConfiguration(ReportConfiguration.getGlobalConfig());
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
    setFunctions (new ExpressionCollection ());
    setExpressions(new ExpressionCollection());
  }

  /**
   * Constructs a report with the specified attributes.
   *
   * @param name  the name of the report.
   * @param reportHeader  the report header <i>not null</i>.
   * @param reportFooter  the report footer <i>not null</i>.
   * @param pageHeader  the page header <i>not null</i>.
   * @param pageFooter  the page footer <i>not null</i>.
   * @param itemBand  the item band <i>not null</i>.
   * @param groups  the report groups <i>not null</i>.
   * @param functions  the report functions.
   * @param data  the data for the report <i>not null</i>.
   * @param defaultPageFormat  the default page format.
   *
   * @throws NullPointerException if one of the <i>not null</i>-parameters is null.
   * @throws FunctionInitializeException if any of the functions cannot be initialized.
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

    setFunctions (new ExpressionCollection (functions));
  }

  /**
   * Constructs a report with the specified attributes.
   *
   * @param name  the name of the report.
   * @param reportHeader  the report header <i>not null</i>.
   * @param reportFooter  the report footer <i>not null</i>.
   * @param pageHeader  the page header <i>not null</i>.
   * @param pageFooter  the page footer <i>not null</i>.
   * @param itemBand  the item band <i>not null</i>.
   * @param groups  the report groups <i>not null</i>.
   * @param functions  the report functions.
   * @param expressions  the report expressions.
   * @param data  the data for the report <i>not null</i>.
   * @param defaultPageFormat  the default page format.
   *
   * @throws NullPointerException if one of the <i>not null</i>-parameters is null.
   * @throws FunctionInitializeException if any of the functions cannot be initialized.
   * <!-- changed expressions to fit functions parameter -->
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
          Collection expressions)
          throws FunctionInitializeException
  {
    this (name, reportHeader, reportFooter, pageHeader, pageFooter, itemBand,
          groups, functions, data, defaultPageFormat);
    setExpressions(new ExpressionCollection(expressions));
  }

  /**
   * Returns the name of the report.
   * <p>
   * You can reference the report name in your XML report template file using the key
   * '<code>report.name</code>'.
   *
   * @return the name.
   */
  public String getName ()
  {
    Object name = getProperty (NAME_PROPERTY);
    return String.valueOf (name);
  }

  /**
   * Sets the name of the report.
   * <P>
   * The report name is stored as a property (key: <code>NAME_PROPERTY = "report.name"</code>) of
   * the report.  If you supply <code>null</code> as the name, the property is removed.
   *
   * @param name  the name of the report.
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
   * @param key  the key.
   * @param value  the value.
   */
  public void setProperty (String key, Object value)
  {
    properties.put (key, value);
  }

  /**
   * Returns the report properties collection for this report. These properties are
   * inherited to all ReportStates generated for this report.
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
   * @param key  the key.
   *
   * @return the property value.
   */
  public Object getProperty (String key)
  {
    if (key == null)
    {
      throw new NullPointerException ();
    }
    return this.properties.get (key);
  }

  /**
   * Returns the value of the property with the specified key.
   *
   * @param key  the key.
   *
   * @return the property value.
   */
  public boolean isPropertyMarked (String key)
  {
    return this.properties.isMarked(key);
  }

  /**
   * Returns the value of the property with the specified key.
   *
   * @param key  the key.
   * @param mark the new marking flag
   */
  public void setPropertyMarked (String key, boolean mark)
  {
    this.properties.setMarked(key, mark);
  }

  /**
   * Sets the report header.
   *
   * @param header  the report header (<code>null</code> not permitted).
   */
  public void setReportHeader (ReportHeader header)
  {
    if (header == null)
    {
      throw new NullPointerException ("JFreeReport.setReportHeader(...) : null not permitted.");
    }

    this.reportHeader = header;
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
   * @param footer  the report footer (<code>null</code> not permitted).
   */
  public void setReportFooter (ReportFooter footer)
  {
    if (footer == null)
    {
      throw new NullPointerException ("JFreeReport.setReportFooter(...) : null not permitted.");
    }

    this.reportFooter = footer;
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
   * @param header  the page header (<code>null</code> not permitted).
   */
  public void setPageHeader (PageHeader header)
  {
    if (header == null)
    {
      throw new NullPointerException ("JFreeReport.setPageHeader(...) : null not permitted.");
    }

    this.pageHeader = header;
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
   * @param footer  the page footer (<code>null</code> not permitted).
   */
  public void setPageFooter (PageFooter footer)
  {
    if (footer == null)
    {
      throw new NullPointerException ("JFreeReport.setPageFooter(...) : null not permitted.");
    }

    this.pageFooter = footer;
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
   * Sets the item band for the report.
   *
   * @param band  the new item band (<code>null</code> not permitted).
   */
  public void setItemBand (ItemBand band)
  {
    if (band == null)
    {
      throw new NullPointerException ("JFreeReport.setItemBand(...) : null not permitted.");
    }
    this.itemBand = band;
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
   * @param group  the group.
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
   * @param groupList  the list of groups.
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
   * @return the group list.
   */
  public GroupList getGroups ()
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
  public int getGroupCount ()
  {
    return groups.size ();
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
  public Group getGroup (int count)
  {
    if (count < 0)
    {
      throw new IllegalArgumentException ("GroupCount must not be negative");
    }

    if (count >= groups.size ())
    {
      throw new IndexOutOfBoundsException ("No such group defined. " + count + " vs. "
                                           + groups.size ());
    }

    return groups.get (count);
  }

  /**
   * Adds a function to the report's collection of functions.
   *
   * @param function  the function.
   *
   * @throws FunctionInitializeException if any of the functions cannot be initialized.
   */
  public void addExpression (Expression function) throws FunctionInitializeException
  {
    expressions.add (function);
  }

  /**
   * Adds a function to the report's collection of functions.
   *
   * @param function  the function.
   *
   * @throws FunctionInitializeException if any of the functions cannot be initialized.
   */
  public void addFunction (Function function) throws FunctionInitializeException
  {
    this.functions.add (function);
  }

  /**
   * Returns the report's collection of functions.
   *
   * @return the function collection.
   */
  public ExpressionCollection getFunctions ()
  {
    return this.functions;
  }

  /**
   * Sets the function collection.
   *
   * @param functions  the collection of functions.
   */
  public void setFunctions (ExpressionCollection functions)
  {
    if (functions == null)
    {
      throw new NullPointerException ("JFreeReport.setFunctions(...) : null not permitted.");
    }
    else
    {
      this.functions = functions;
    }
  }

  /**
   * Returns the default page format.
   *
   * @return the page format.
   */
  public PageFormat getDefaultPageFormat ()
  {
    return defaultPageFormat;
  }

  /**
   * Defines the default page format for this report. The default is a hint
   * to define at least one suitable format. If no format is defined the system's default
   * page format is used.
   *
   * @param format  the default format (<code>null</code> permitted).
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
   * Reports are generated from a {@link TableModel} (as used by Swing's {@link JTable}). If you 
   * don't want to give data to the report, use an empty {@link TableModel} instead of 
   * <code>null</code>.
   *
   * @param data  the data for the report (<code>null</code> not permitted).
   */
  public void setData (TableModel data)
  {
    if (data == null)
    {
      throw new NullPointerException ("JFreeReport.setData(...) : null not permitted.");
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
  public Object clone () throws CloneNotSupportedException
  {
    JFreeReport report = (JFreeReport) super.clone ();
    report.data = data; // data is defined to be immutable, so don't clone the thing
    report.defaultPageFormat = (PageFormat) defaultPageFormat.clone ();
    report.groups = (GroupList) groups.clone ();
    report.itemBand = (ItemBand) itemBand.clone ();
    report.pageFooter = (PageFooter) pageFooter.clone ();
    report.pageHeader = (PageHeader) pageHeader.clone ();
    report.properties = (ReportProperties) properties.clone ();
    report.reportFooter = (ReportFooter) reportFooter.clone ();
    report.reportHeader = (ReportHeader) reportHeader.clone ();
    report.functions = (ExpressionCollection) this.functions.clone ();
    report.expressions = (ExpressionCollection) expressions.clone();
    return report;
  }

  /**
   * Reads an object.
   *
   * @param in  the input stream.
   *
   * @throws IOException if there is an IO problem.
   * @throws ClassNotFoundException if there is a class problem.
   */
  private void readObject (java.io.ObjectInputStream in)
          throws IOException, ClassNotFoundException
  {
    in.defaultReadObject ();
  }

  /**
   * Returns information about the JFreeReport class library.
   *
   * @return the information.
   */
  public static final JFreeReportInfo getInfo ()
  {
    if (JFreeReport.info == null)
    {
      JFreeReport.info = new JFreeReportInfo ();
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
  public void setExpressions(ExpressionCollection expressions)
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
}
