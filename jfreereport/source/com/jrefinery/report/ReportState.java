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
 * ReportState.java
 * ----------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morger;
 *
 * $Id$
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 19-Feb-2002 : Moved constants into the ReportStateConstants interface (DG);
 * 18-Apr-2002 : Added detection whether a report did proceed.
 */

package com.jrefinery.report;

/**
 * Captures state information for a report while it is in the process of being displayed or
 * printed.  In most cases, we are interested in the report state at the end of a page, so that
 * we can begin the next page in the right manner.
 */
public class ReportState implements ReportStateConstants, Cloneable {

    /** The report that the state belongs to. */
    protected JFreeReport report;

    /** The current state. */
    protected int state;

    /** The current item. */
    protected int currentItem;

    /** The page that this state applies to. */
    protected int currentPage;

    /** The current group. */
    protected int currentGroupIndex;

    /** The functions. */
    protected FunctionCollection functions;

    /** Band with an pending page break */
    protected Band pband;
    /**
     * Constructs a ReportState for the specified report.
     *
     * @param report The report.
     */
    public ReportState(JFreeReport report) {

        this.report = report;
        this.state = START;
        this.currentItem = 0;
        this.currentPage = 1;
        this.currentGroupIndex = 0;
        this.functions = report.getFunctions();

    }

    /**
     * Returns the state (one of: FINISHED, PREHEADER, POSTHEADER, INGROUP, PREFOOTER, POSTFOOTER,
     * POST_SUBGROUP_FOOTER).
     * @return The current state.
     */
    public int getState() {
        return this.state;
    }

    /**
     * Sets the state.
     * @param state The new state (FINISHED, PREHEADER, POSTHEADER, INGROUP, PREFOOTER, POSTFOOTER,
     *              POST_SUBGROUP_FOOTER);
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * Returns the current item (that is, the current row of the data in the TableModel).
     * @return The current item index (corresponds to a row in the TableModel).
     */
    public int getCurrentItem() {
        return this.currentItem;
    }

    /**
     * Sets the current item index (corresponds to a row in the TableModel).
     * @param itemIndex The new item index.
     */
    public void setCurrentItem(int itemIndex) {
        this.currentItem=itemIndex;
    }

    /**
     * Returns the current page.
     * @return The page that this state refers to.
     */
    public int getCurrentPage() {
        return this.currentPage;
    }

    /**
     * Sets the current page.
     * @param page The new page number.
     */
    public void setCurrentPage(int page) {
        this.currentPage=page;
    }

    /**
     * Returns the current group index.
     * @return The current group index.
     */
    public int getCurrentGroupIndex() {
        return currentGroupIndex;
    }

    /**
     * Sets the current group index (zero is the item group).
     * @param index The new group index.
     */
    public void setCurrentGroupIndex(int index) {
        this.currentGroupIndex=index;
    }

    /**
     * Returns the function collection.
     */
    public FunctionCollection getFunctions() {
        return this.functions;
    }

    /**
     * Sets the function collection.
     */
    public void setFunctions(FunctionCollection functions) {
        this.functions = functions;
    }

    /**
     * Clones the report state.
     */
    public Object clone() {

        ReportState result = null;

        try {
            result = (ReportState)super.clone();
            if (this.functions!=null) {
                FunctionCollection copyOfFunctions = (FunctionCollection)this.functions.clone();
                result.setFunctions(copyOfFunctions);
            }
        }
        catch (CloneNotSupportedException e) {
            // this should never happen...
            System.err.println("ReportState: clone not supported");
        }

        return result;

    }

  /**
   * This is a helper function used to detect infinite loops on report 
   * processing. Returns true, if the report did proceed over at least one
   * element.
   */
	public boolean isProceeding (ReportState oldstate)
	{
  	if (currentGroupIndex != oldstate.currentGroupIndex ||
    	  currentItem != oldstate.currentItem ||
        state != oldstate.state ||
        currentPage != oldstate.currentPage ||
        pband != oldstate.pband) 
    {
    	return true;
    }
    return false;
	}

  /**
   * Activate an pagebreak on next printing for this Band. This is
   * a helper function used to detect infinite loops on report processing.
   */  
  public void setPendingPageBreak (Band band)
  {
  	pband = band;
  }
  
  /**
   * This is a helper function used to detect infinite loops on report 
   * processing. Returns true, if the report will do a page break on the
   * next band processing. This is
   * a helper function used to detect infinite loops on report processing.
   */
  public boolean isPendingPageBreak (Band band)
  {
  	if (pband == band)
    {
    	pband = null;
      return true;
    }
    return false;
  }
}