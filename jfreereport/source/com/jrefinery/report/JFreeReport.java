/* =============================================================
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
 * $Id: JFreeReport.java,v 1.1.1.1 2002/04/25 17:02:17 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 04-Mar-2002 : Major changes to report engine to incorporate functions and different output
 *               targets (DG);
 * 24-Apr-2002 : ItemBand and Groups are Optional Elements, default Elements are created as needed
 * 07-May-2002 : Fixed bug where last row of data is left off the report if it is alone in a
 *               group, reported by Steven Feinstein (DG);
 *
 */

package com.jrefinery.report;

import java.awt.Graphics2D;
import javax.swing.table.TableModel;
import java.awt.print.PageFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import com.jrefinery.JCommon;
import com.jrefinery.report.function.Function;
import com.jrefinery.ui.about.Licences;
import com.jrefinery.ui.about.Library;
import com.jrefinery.ui.about.Contributor;
import com.jrefinery.ui.about.ProjectInfo;

/**
 * This class co-ordinates the process of generating a report from a TableModel.  The report is
 * made up of 'bands', which are used repeatedly as necessary to generate small sections of the
 * report.
 */
public class JFreeReport implements JFreeReportConstants {

    public static ProjectInfo INFO = new JFreeReportInfo();

    /** The report name. */
    protected String name;

    /** Storage for arbitrary properties that a user can assign to a report.*/
    protected Map properties;

    /** An ordered list of report groups (each group defines its own header and footer). */
    protected List groups;

    /** Storage for the functions in the report. */
    protected FunctionCollection functions;

    /** The report header band (if not null, printed once at the start of the report). */
    protected ReportHeader reportHeader;

    /** The report footer band (if not null, printed once at the end of the report). */
    protected ReportFooter reportFooter;

    /** The page header band (if not null, printed at the start of every page). */
    protected PageHeader pageHeader;

    /** The page footer band (if not null, printed at the end of every page). */
    protected PageFooter pageFooter;

    /** The item band - used once for each row of data. */
    protected ItemBand itemBand;

    /** The table model containing the data for the report. */
    protected TableModel data;

    /** The page format used for the report (determines the page size, and therefore the report
        width). */
    protected PageFormat defaultPageFormat;

    /**
     * Constructs an empty report.
     */
    public JFreeReport() {
        this(null);
    }

    /**
     * Constructs a named empty report.
     */
    public JFreeReport(String name) {
        this(name,
             null, // report header
             null, // report footer
             null, // page header
             null, // page footer
             null, // item band
             null  // groups
        );
    }

    public JFreeReport(String name, TableModel data) {

        this(name, null, null, null, null, null, null, null, data, null);

    }

    /**
     * Constructs a report.
     */
    public JFreeReport(String name,
                       ReportHeader reportHeader,
                       ReportFooter reportFooter,
                       PageHeader pageHeader,
                       PageFooter pageFooter,
                       ItemBand items,
                       List groups) {

        this(name, reportHeader, reportFooter, pageHeader, pageFooter, items,  groups,
             null, null, null);

    }

    /**
     * Constructs a report with the specified attributes.
     * @param header The report header (optional).
     * @param pageHeader The page header (optional).
     * @param items The item band (required).
     * @param pageFooter The page footer (optional).
     * @param groups The report groups.
     * @param data The data for the report (required).
     * @param defaultPageFormat The default page format.
     */
    public JFreeReport(String name,
                       ReportHeader reportHeader,
                       ReportFooter reportFooter,
                       PageHeader pageHeader,
                       PageFooter pageFooter,
                       ItemBand itemBand,
                       List groups,
                       Collection functions,
                       TableModel data,
                       PageFormat defaultPageFormat) {

        this.name = name;
        this.reportHeader = reportHeader;
        this.reportFooter = reportFooter;
        this.pageHeader = pageHeader;
        this.pageFooter = pageFooter;
        this.data = data;
        this.defaultPageFormat = defaultPageFormat;

        setItemBand (itemBand);
        setGroups (groups);

        // store the functions in a Map using the function name as the key.
        this.functions = new FunctionCollection(functions);
        this.properties = new TreeMap();

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
        this.itemBand = new ItemBand (0f);
      }
      else
      {
        this.itemBand = band;
      }
    }

    /**
     * Sets the groups for this report. If no list (null) or an
     * empty list is given, an default group is created. This default
     * group contains no elements and starts at the first record of the
     * data and ends on the last record.
     */
    protected void setGroups (List groupList)
    {
      this.groups = new ArrayList ();
      if (groupList != null)
      {
        groups.addAll (groupList);
      }
      if (groups.size() == 0)
      {
        groups.add (new Group ("default"));
      }
    }

    /**
     * Returns the name of the report.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the report.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Adds a property to the report.
     * <P>
     * Developers are free to add any properties they want to a report.  Use a
     * ReportPropertyFunction to retrieve the property during report generation.
     *
     * @param key The key.
     * @param value The value.
     */
    public void addProperty(String key, Object value) {
        this.properties.put(key, value);
    }

    /**
     * Returns the value of the property with the specified key.
     *
     * @param key The key.
     * @result The property value.
     */
    public Object getProperty(String key)
    {
      if (key == null)
         throw new NullPointerException ();
       return this.properties.get(key);
    }

    /**
     * Sets the report header (null permitted).
     *
     * @param header The report header.
     */
    public void setReportHeader(ReportHeader header) {
        this.reportHeader = header;
    }

    /**
     * Sets the report footer (null permitted).
     *
     * @param footer The report footer.
     */
    public void setReportFooter(ReportFooter footer) {
        this.reportFooter = footer;
    }

    /**
     * Sets the page header (null permitted).
     *
     * @param header The page header.
     */
    public void setPageHeader(PageHeader header) {
        this.pageHeader = header;
    }

    /**
     * Sets the page footer (null permitted).
     *
     * @param footer The page footer.
     */
    public void setPageFooter(PageFooter footer) {
        this.pageFooter = footer;
    }

    /**
     * Returns the item band for the report.
     */
    public ItemBand getItemBand() {
        return this.itemBand;
    }

    /**
     * Adds a group to the report.
     *
     * @param group The group.
     */
    public void addGroup(Group group) {

       if (this.groups==null) this.groups = new ArrayList();
       groups.add(group);

    }

    /**
     * Returns the list of groups for the report.
     * @return The list of groups for the report.
     */
    public List getGroups() {
        return this.groups;
    }

    /**
     * Adds a function to the report's collection of functions.
     *
     * @param function The function.
     */
    public void addFunction(Function function) {
        this.functions.add(function);
    }

    /**
     * Returns the report's collection of functions.
     *
     * @return The function collection.
     */
    public FunctionCollection getFunctions() {
        return this.functions;
    }

    /**
     * Sets the function collection.
     *
     * @param functions The collection of functions.
     */
    public void setFunctions(FunctionCollection functions) {
        this.functions = functions;
    }

    /**
     * Returns the page format that will be used to output the report.
     *
     * @return The current page format.
     */
    public PageFormat getDefaultPageFormat() {
        return defaultPageFormat;
    }

    /**
     * Sets the data for the report.  Reports are generated from a TableModel (as used by Swing's
     * JTable).
     *
     * @param data The data for the report.
     */
    public void setData(TableModel data) {
        this.data = data;
    }

    /**
     * Sends the entire report to the specified target.
     *
     * @param target The output target.
     */
    public void processReport(OutputTarget target, boolean draw) {

        int page = 1;
        ReportState rs = new ReportState(this);

        rs = processPage(page, target, rs, draw);
        target.endPage();

        while (!(rs.getState()==ReportState.FINISH)) {
            page++;
            rs.setCurrentPage(page);
            rs = processPage(page, target, rs, draw);
            target.endPage();
        }

    }

    /**
     * Draws a single page of the report to the specified graphics device, and returns state
     * information.  The caller should check the returned state to ensure that some progress has
     * been made, because on some small paper sizes the report may get stuck (particularly if the
     * header and footer are large).
     *
     * @param page The page to process.
     * @param previous The report state at the end of the previous page.
     * @param g2 The graphics device on which the report is being drawn.
     * @param pf The page format.
     * @param draw A flag that indicates whether or not we are actually drawing to the graphics
     *             device.
     * @return The report state as at the end of the page.
     */
    public ReportState processPage(int page,
                                   OutputTarget target, ReportState previous, boolean draw) {

        ReportState state = (ReportState)previous.clone();
        this.setFunctions(state.getFunctions());

        if (this.functions!=null) {
            this.functions.startPage(page);
        }

        boolean pageDone = false;
        Cursor cursor = new Cursor();
        cursor.pageLeft = target.getUsableX();
        cursor.pageBottom = target.getUsableY()+target.getUsableHeight();
        cursor.y = target.getUsableY();

        // if this is the first page and there is a report header, then print it...
        if (page==1) {

            if (reportHeader!=null) {

                //reportHeader.populateElements(data, state.getCurrentItem(), functions);
                if (draw) reportHeader.draw(target, cursor.pageLeft, cursor.y);
                cursor.advance(reportHeader.getHeight());
                pageDone = reportHeader.getOwnPage();

            }

        }

        // print the page header (if there is one) and measure the page footer (if there is one)...
        if (!pageDone) {

            if (pageHeader!=null) {

                if (page==1) {

                    if (pageHeader.displayOnFirstPage()) {
                        if (draw) pageHeader.draw(target, cursor.pageLeft, cursor.y);
                        cursor.advance(pageHeader.getHeight());
                    }

                }

                else {

                    if (draw) pageHeader.draw(target, cursor.pageLeft, cursor.y);
                    cursor.advance(pageHeader.getHeight());

                }

            }

            if (pageFooter!=null) {

                cursor.pageBottom = target.getUsableY()+target.getUsableHeight()
                                    -pageFooter.getHeight();
                if (page==1) {
                    if (!pageFooter.displayOnFirstPage()) {
                        cursor.pageBottom = target.getUsableY()+target.getUsableHeight();
                    }
                }

            }

        }

        // Do some real work.  The report header and footer, and the page headers and footers are
        // just decorations, as far as the report state is concerned.  The state only changes in
        // the following code...
        while (!pageDone) {

            pageDone = advanceState(target, state, cursor, draw);

        }

        if (this.functions!=null) {
            this.functions.endPage(page);
        }

        // print the page footer, if there is one...
        if (pageFooter!=null) {

            pageFooter.populateElements(this.data, state.getCurrentItem(), functions);
            if (page==1) {
                if (pageFooter.displayOnFirstPage()) {
                    if (draw) pageFooter.draw(target, cursor.pageLeft, cursor.pageBottom);
                }
            }
            else {
                if (draw) pageFooter.draw(target, cursor.pageLeft, cursor.pageBottom);
            }

        }

        // return the state at the end of the page...
        if (this.functions!=null) {
            state.setFunctions((FunctionCollection)this.functions.clone());
        }
        return state;

    }

    /**
     * Advance the state of the parent group by one step, returning true if the current page is
     * full.
     * @param g2 The graphics device on which the report is being drawn.
     * @param rs The current report state.
     * @param pf The page format.
     * @param cursor The cursor that marks the current position.
     * @param draw A flag that indicates whether or not we are actually drawing to the graphics
     *             device.
     */
    public boolean advanceState(OutputTarget target, ReportState rs, Cursor cursor, boolean draw) {

        int state = rs.getState();

        Group group = (Group)groups.get(rs.getCurrentGroupIndex());

        // *** START ***
        if (state==ReportState.START) {

            properties.put("Report Date", new Date());
            if (functions!=null) {
                functions.startReport(this);
            }
            rs.setState(ReportState.GROUP_START);
            return PAGE_NOT_FULL;

        }

        // *** GROUP START ***
        else if (state==ReportState.GROUP_START) {

            int g = rs.getCurrentGroupIndex();
            if (g<this.groups.size()-1) {
                rs.setState(ReportState.PRE_GROUP_HEADER);
            }
            else {
                rs.setState(ReportState.PRE_ITEM_GROUP_HEADER);
            }
            return PAGE_NOT_FULL;

        }

        // *** PRE_GROUP_HEADER ***
        else if (state==ReportState.PRE_GROUP_HEADER) {

            if (functions!=null) {
                functions.startGroup(group);
            }
            // if there is a header (and room to print it) print it and set state=POST_GROUP_HEADER
            GroupHeader header = group.getHeader();

            if (header!=null) {

                if (cursor.spaceFor(header.getHeight())) {

                    if (draw) {
                        header.populateElements(data, rs.getCurrentItem(), functions);
                        header.draw(target, cursor.pageLeft, cursor.y);
                    }
                    cursor.advance(header.getHeight());
                    rs.setState(ReportState.POST_GROUP_HEADER);
                    return PAGE_NOT_FULL;

                }
                else return PAGE_FULL;

            }

            else {

                // no header to print...
                rs.setState(ReportState.POST_GROUP_HEADER);
                return PAGE_NOT_FULL;

            }

        }

        // *** POST_GROUP_HEADER ***
        else if (state==ReportState.POST_GROUP_HEADER) {

            // step down to PREHEADER for the subgroup
            int g = rs.getCurrentGroupIndex();
            rs.setCurrentGroupIndex(g+1);
            if (g+1<groups.size()-1) {
                rs.setState(ReportState.PRE_GROUP_HEADER);
            }
            else {
                rs.setState(ReportState.PRE_ITEM_GROUP_HEADER);
            }

        }

        // *** PRE_ITEM_GROUP_HEADER ***
        else if (state==ReportState.PRE_ITEM_GROUP_HEADER) {

            if (functions!=null) {
                functions.startGroup(group);
            }
            GroupHeader header=group.getHeader();

            if (header!=null) {
                if (cursor.spaceFor(header.getHeight())) {  // room to print header?
                    if (draw) {
                        header.populateElements(data, rs.getCurrentItem(), functions);
                        header.draw(target, cursor.pageLeft, cursor.y);
                    }
                    cursor.advance(header.getHeight());
                    rs.setState(ReportState.POST_ITEM_GROUP_HEADER);
                    return PAGE_NOT_FULL;
                }
                else return PAGE_FULL;
            }

            else {
                rs.setState(ReportState.POST_ITEM_GROUP_HEADER);
                return PAGE_NOT_FULL;
            }

        }

        // *** POST_ITEM_GROUP_HEADER or IN_GROUP ***
        else if ((rs.getState()==ReportState.POST_ITEM_GROUP_HEADER)
              || (rs.getState()==ReportState.IN_ITEM_GROUP)) {

            if (group.lastItemInGroup(data, rs.getCurrentItem())) {

                if (cursor.spaceFor(itemBand.getHeight())) {

                    if (functions!=null) {
                        functions.advanceItems(data, rs.getCurrentItem());
                    }
                    if (draw) {
                        itemBand.populateElements(data, rs.getCurrentItem(), functions);
                        itemBand.draw(target, cursor.pageLeft, cursor.y);
                    }
                    rs.setCurrentItem(rs.getCurrentItem()+1);
                    cursor.advance(itemBand.getHeight());
                    rs.setState(ReportState.PRE_ITEM_GROUP_FOOTER);
                    return PAGE_NOT_FULL;

                }
                else return PAGE_FULL;

            }

            else if (this.isLastItemInHigherGroups(data, rs.getCurrentItem(), rs.getCurrentGroupIndex()-1)) {
                if (cursor.spaceFor(itemBand.getHeight())) {

                    if (functions!=null) {
                        functions.advanceItems(data, rs.getCurrentItem());
                    }
                    if (draw) {
                        itemBand.populateElements(data, rs.getCurrentItem(), functions);
                        itemBand.draw(target, cursor.pageLeft, cursor.y);
                    }
                    rs.setCurrentItem(rs.getCurrentItem()+1);
                    cursor.advance(itemBand.getHeight());
                    rs.setState(ReportState.PRE_ITEM_GROUP_FOOTER);
                    return PAGE_NOT_FULL;
                }
                else return PAGE_FULL;
            }

            else {

                if (cursor.spaceFor(itemBand.getHeight())) {

                    if (functions!=null) {
                        functions.advanceItems(data, rs.getCurrentItem());
                    }
                    itemBand.populateElements(data, rs.getCurrentItem(), functions);
                    if (draw) itemBand.draw(target, cursor.pageLeft, cursor.y);
                    rs.setCurrentItem(rs.getCurrentItem()+1);
                    cursor.advance(itemBand.getHeight());
                    rs.setState(ReportState.IN_ITEM_GROUP);
                    return PAGE_NOT_FULL;

                }
                else return PAGE_FULL;

            }

        }

        // *** PRE_ITEM_GROUP_FOOTER ***
        else if (state==ReportState.PRE_ITEM_GROUP_FOOTER) {

            if (functions!=null) {
                functions.endGroup(group);
            }
            GroupFooter footer=group.getFooter();

            if (footer!=null) {
                if (cursor.spaceFor(footer.getHeight())) {

                    if (draw) {
                        footer.populateElements(data, rs.getCurrentItem()-1, functions);
                        footer.draw(target, cursor.pageLeft, cursor.y);
                    }
                    cursor.advance(footer.getHeight());
                    rs.setState(ReportState.POST_ITEM_GROUP_FOOTER);
                    return PAGE_NOT_FULL;

                }
                else return PAGE_FULL;
            }

            else {
                rs.setState(ReportState.POST_ITEM_GROUP_FOOTER);
                return PAGE_NOT_FULL;
            }

        }

        // *** POST_ITEM_GROUP_FOOTER ***
        else if (state==ReportState.POST_ITEM_GROUP_FOOTER) {

            // IS THERE A PARENT GROUP
            // YES:
            //     IS THERE ANY MORE DATA?
            //     YES:  is it a group change for the parent?
            //           YES: --> PRE_GROUP_HEADER for parent group
            //           NO:  --> PRE_ITEM_GROUP_HEADER for this group
            //     NO:   --> PRE_GROUP_FOOTER for parent group


            // NO:
            //     IS THERE ANY MORE DATA?
            //     YES:  --> PRE_ITEM_GROUP_HEADER
            //     NO:   --> END

            int g = rs.getCurrentGroupIndex();
            if (g>0) {

                if (rs.getCurrentItem()<(data.getRowCount())) {

                    //Group group2 = (Group)groups.get(g-1);
                    //if (group2.lastItemInGroup(data, rs.getCurrentItem()-1)) {
                    if (this.isLastItemInHigherGroups(data, rs.getCurrentItem()-1, rs.getCurrentGroupIndex()-1)) {
                        rs.setCurrentGroupIndex(g-1);
                        rs.setState(ReportState.PRE_GROUP_FOOTER);
                        return PAGE_NOT_FULL;
                    }
                    else {
                        rs.setState(ReportState.PRE_ITEM_GROUP_HEADER);
                        return PAGE_NOT_FULL;
                    }

                }
                else {
                    rs.setCurrentGroupIndex(g-1);
                    rs.setState(ReportState.PRE_GROUP_FOOTER);
                    return PAGE_NOT_FULL;
                }
            }

            else {  // group finished, if there is more data start a new group...

                if (rs.getCurrentItem()<(data.getRowCount())) {
                    rs.setState(ReportState.PRE_ITEM_GROUP_HEADER);
                    return PAGE_NOT_FULL;
                }

                else {
                    rs.setState(ReportState.PRE_REPORT_FOOTER);
                    return PAGE_NOT_FULL;
                }

            }

        }

        // *** PRE_GROUP_FOOTER ***
        else if (state==ReportState.PRE_GROUP_FOOTER) {

            if (functions!=null) {
                functions.endGroup(group);
            }
            GroupFooter footer=group.getFooter();

            if (footer!=null) {

                if (cursor.spaceFor(footer.getHeight())) {

                    if (draw) {
                        footer.populateElements(data, rs.getCurrentItem()-1, functions);
                        footer.draw(target, cursor.pageLeft, cursor.y);
                    }
                    cursor.advance(footer.getHeight());
                    rs.setState(ReportState.POST_GROUP_FOOTER);
                    return PAGE_NOT_FULL;

                }
                else return PAGE_FULL;

            }

            else {
                rs.setState(ReportState.POST_GROUP_FOOTER);
                return PAGE_NOT_FULL;
            }

        }

        // *** POST_GROUP_FOOTER ***
        else if (rs.getState()==ReportState.POST_GROUP_FOOTER) {

            // IS THERE A PARENT GROUP
            // YES:
            //     IS THERE ANY MORE DATA?
            //     YES:  is it a group change for the parent?
            //           YES: --> PRE_GROUP_HEADER for parent group
            //           NO:  --> PRE_GROUP_HEADER for this group
            //     NO:   --> PRE_GROUP_FOOTER for parent group


            // NO:
            //     IS THERE ANY MORE DATA?
            //     YES:  --> PRE_GROUP_HEADER
            //     NO:   --> END

            int g = rs.getCurrentGroupIndex();
            if (g>0) {

                if (rs.getCurrentItem()<(data.getRowCount()-1)) {

                    Group group3 = (Group)groups.get(g-1);
                    if (group3.lastItemInGroup(data, rs.getCurrentItem()-1)) {
                        rs.setCurrentGroupIndex(g-1);
                        rs.setState(ReportState.PRE_GROUP_HEADER);
                        return PAGE_NOT_FULL;
                    }
                    else {
                        rs.setState(ReportState.PRE_GROUP_HEADER);
                        return PAGE_NOT_FULL;
                    }

                }
                else {
                    rs.setCurrentGroupIndex(g-1);
                    rs.setState(ReportState.PRE_GROUP_FOOTER);
                    return PAGE_NOT_FULL;
                }
            }

            else {  // group finished, if there is more data start a new group...

                if (rs.getCurrentItem()<(data.getRowCount()-1)) {
                    rs.setState(ReportState.PRE_GROUP_HEADER);
                    return PAGE_NOT_FULL;
                }

                else {
                    rs.setState(ReportState.PRE_REPORT_FOOTER);
                    return PAGE_NOT_FULL;
                }

            }

        }
        // *** POST_GROUP_FOOTER ***
        else if (rs.getState()==ReportState.PRE_REPORT_FOOTER) {

            if (reportFooter!=null) {
                if (cursor.spaceFor(reportFooter.getHeight())) {

                    if (draw) {
                        reportFooter.populateElements(data, rs.getCurrentItem()-1, functions);
                        reportFooter.draw(target, cursor.pageLeft, cursor.y);
                    }
                    cursor.advance(reportFooter.getHeight());
                    rs.setState(ReportState.FINISH);
                    return PAGE_FULL;

                }
                else return PAGE_FULL;
            }
            else {
                rs.setState(ReportState.FINISH);
                return PAGE_FULL;
            }

        }

        return PAGE_NOT_FULL;

    }

    /**
     * Returns true if the current row is the end of a group.
     */
    protected boolean isLastItemInHigherGroups(TableModel data, int row, int groupIndex) {

        boolean result = false;

        for (int g=0; g<=groupIndex; g++) {
            Group group = (Group)this.groups.get(g);
            if (group.lastItemInGroup(data, row)) {
                result = true;
            }
        }

        return result;

    }

}

/**
 * Details about the JFreeReport project.
 */
class JFreeReportInfo extends ProjectInfo {

    /**
     * Constructs an object containing information about the JFreeReport project.
     * <p>
     * Uses a resource bundle to localise some of the information.
     */
    public JFreeReportInfo() {

        // get a locale-specific resource bundle...
        String baseResourceClass = "com.jrefinery.report.resources.JFreeReportResources";
        ResourceBundle resources = ResourceBundle.getBundle(baseResourceClass);

        this.name = resources.getString("project.name");
        this.version = resources.getString("project.version");
        this.info = resources.getString("project.info");
        this.copyright = resources.getString("project.copyright");
        this.licenceText = Licences.LGPL;

        this.contributors = Arrays.asList(
            new Contributor[] {
                new Contributor("David Gilbert", "david.gilbert@object-refinery.com"),
                new Contributor("Thomas Morgner", "-")
            }
        );

        this.libraries = Arrays.asList(
            new Library[] {
                new Library(JCommon.INFO),
                new Library("iText", "0.92", "LGPL", "http://www.lowagie.com/iText/index.html"),
                new Library("GNU JAXP", "1.0beta1", "GPL with library exception",
                            "http://www.gnu.org/software/classpathx/jaxp/")
            }
        );

    }

}
