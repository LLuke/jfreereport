/**
 *
 *  Date: 23.06.2002
 *  TotalGroupSumFunction.java
 *  ------------------------------
 *  23.06.2002 : ...
 */
package com.jrefinery.report.function;

import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportState;
import com.jrefinery.report.Group;
import com.jrefinery.report.filter.NumberFormatParser;
import com.jrefinery.report.filter.StaticDataSource;
import com.jrefinery.report.filter.DecimalFormatParser;
import com.jrefinery.report.util.Log;

import javax.swing.table.TableModel;
import java.math.BigDecimal;
import java.util.ArrayList;

public class TotalGroupSumFunction extends AbstractFunction
{
  private static class GroupSum
  {
    private BigDecimal result;

    public GroupSum ()
    {
      result = new BigDecimal(0);
    }

    public void add (Number n)
    {
      result = result.add (new BigDecimal(n.toString()));
    }

    public BigDecimal getResult ()
    {
      return result;
    }
  }

  private static final BigDecimal ZERO = new BigDecimal (0.0);

  /** The parser for performing data conversion */
  private NumberFormatParser parser;

  /** The datasource of the parser */
  private StaticDataSource datasource;

  private GroupSum groupResult;
  private ArrayList results;
  private int currentIndex;
  /**
   * Constructs a new function.
   * <P>
   * Initially the function has no name...be sure to assign one before using the function.
   */
  public TotalGroupSumFunction ()
  {
    groupResult = new GroupSum();
    datasource = new StaticDataSource();
    parser = new DecimalFormatParser();
    parser.setNullValue(ZERO);
    parser.setDataSource(datasource);
    results = new ArrayList();
  }

  /**
   * Receives notification that the report has started.
   * <P>
   * Maps the reportStarted-method to the legacy function startReport ().
   *
   * @param event Information about the event.
   */
  public void reportStarted (ReportEvent event)
  {
    if (event.getState().isPrepareRun() == false)
    {
      currentIndex = -1;
      return;
    }

    currentIndex = -1;
    results.clear();
    groupResult = new GroupSum();
  }

  /**
   * Receives notification that a group has started.
   * <P>
   * Maps the groupStarted-method to the legacy function startGroup (int).
   *
   * @param event Information about the event.
   */
  public void groupStarted (ReportEvent event)
  {

    if (getGroup () != null)
    {
      JFreeReport report = event.getReport ();
      ReportState state = event.getState ();
      Group group = report.getGroup (state.getCurrentGroupIndex ());
      if (getGroup ().equals (group.getName ()))
      {
        if (event.getState().isPrepareRun() == false)
        {
          // Activate the current group, which was filled in the prepare run.
          currentIndex += 1;
          groupResult = (GroupSum) results.get (currentIndex);
        }
        else
        {
          groupResult = new GroupSum();
          results.add (groupResult);
        }
      }
    }
  }


  /**
   * Receives notification that a row of data is being processed.
   * <P>
   * Maps the itemsAdvanced-method to the legacy function advanceItems (int).
   *
   * @param event Information about the event.
   */
  public void itemsAdvanced (ReportEvent event)
  {
    if (event.getState().isPrepareRun() == false)
    {
      return;
    }

    TableModel data = event.getReport().getData ();
    int row = event.getState().getCurrentDataItem();

    // Handle the case when the tablemodel contains no rows
    if (data.getRowCount() == 0) return;

    Object fieldValue = null;
    for (int c = 0; c < data.getColumnCount (); c++)
    {
      if (getField().equals (data.getColumnName (c)))
      {
        fieldValue = data.getValueAt (row, c);
      }
    }

    if (fieldValue == null)
    {
      // No add, field is null
      return;
    }
    try
    {
      datasource.setValue(fieldValue);
      Number n = (Number) parser.getValue();
      groupResult.add (n);
    }
    catch (Exception e)
    {
      Log.error ("ItemSumFunction.advanceItems(): problem adding number.");
    }
  }

  /**
   * Returns a clone of the function.
   * <P>
   * Be aware, this does not create a deep copy. If you have complex
   * strucures contained in objects, you have to overwrite this function.
   *
   * @return A clone of the function.
   */
  public Object clone () throws CloneNotSupportedException
  {
    return super.clone ();
  }

  /**
   * Returns the name of the group to be counted.
   */
  public String getGroup ()
  {
    return (String) getProperty("group");
  }

  /**
   * defines the name of the group to be counted.
   * If the name is null, all groups are counted.
   */
  public void setGroup (String group)
  {
    setProperty ("group", group);
  }

  /**
   * Return the current function value.
   * <P>
   * The value depends (obviously) on the function implementation.   For example, a page counting
   * function will return the current page number.
   *
   * @return The value of the function.
   */
  public Object getValue ()
  {
    return groupResult.getResult();
  }

  /**
   * Returns the field used by the function.
   * <P>
   * The field name corresponds to a column name in the report's TableModel.
   *
   * @return The field name.
   */
  public String getField ()
  {
    return getProperty("field");
  }

  /**
   * Sets the field name for the function.
   * <P>
   * The field name corresponds to a column name in the report's TableModel.
   *
   * @param The field name (null not permitted).
   */
  public void setField (String field)
  {
    if (field == null)
      throw new NullPointerException ();
    setProperty ("field", field);
  }

  /**
   * Checks that the function has been correctly initialized.  If there is a problem, this method
   * throws a FunctionInitializeException.
   * <P>
   * The default implementation checks that the function name is not null, and calls the
   * isInitialized() method (now deprecated).
   *
   * @throws FunctionInitializeException if the function name is not set or the call to
   * isInitialized returns false.
   */
  public void initialize () throws FunctionInitializeException
  {
    super.initialize ();
    if (getProperty("field") == null) throw new FunctionInitializeException("Field is required");
  }
}