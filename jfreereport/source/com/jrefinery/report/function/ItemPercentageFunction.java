/**
 *
 *  Date: 23.06.2002
 *  ItemPercentageFunction.java
 *  ------------------------------
 *  23.06.2002 : ...
 */
package com.jrefinery.report.function;

import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.util.Log;
import com.jrefinery.report.filter.NumberFormatParser;
import com.jrefinery.report.filter.StaticDataSource;
import com.jrefinery.report.filter.DecimalFormatParser;

import javax.swing.table.TableModel;
import java.math.BigDecimal;
import java.util.Properties;

public class ItemPercentageFunction extends AbstractFunction
{
  private TotalGroupSumFunction totalSumFunction;
  private BigDecimal currentValue;
  private static final BigDecimal ZERO = new BigDecimal (0.0);

  /** The parser for performing data conversion */
  private NumberFormatParser parser;

  /** The datasource of the parser */
  private StaticDataSource datasource;


  public ItemPercentageFunction ()
  {
    totalSumFunction = new TotalGroupSumFunction();
    totalSumFunction.setName("total");

    datasource = new StaticDataSource();
    parser = new DecimalFormatParser();
    parser.setNullValue(ZERO);
    parser.setDataSource(datasource);
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
    super.initialize();
    totalSumFunction.initialize();
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
    totalSumFunction.groupStarted(event);
    TableModel data = event.getReport().getData ();
    int row = event.getState().getCurrentDisplayItem();

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
      currentValue = new BigDecimal(n.toString());
    }
    catch (Exception e)
    {
      Log.error ("ItemSumFunction.advanceItems(): problem adding number.");
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
    totalSumFunction.itemsAdvanced (event);
    TableModel data = event.getReport().getData ();
    int row = event.getState().getCurrentDisplayItem();

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
      currentValue = new BigDecimal(n.toString());
    }
    catch (Exception e)
    {
      Log.error ("ItemSumFunction.advanceItems(): problem adding number.");
    }

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
    totalSumFunction.reportStarted(event);
    currentValue = ZERO;
  }

  /**
   * Return the current function value.
   * <P>
   * Don not count on the correctness of this function until the preparerun has finished.
   *
   * @return The value of the function.
   */
  public Object getValue ()
  {
    BigDecimal total = (BigDecimal) totalSumFunction.getValue();

    if (total.longValue() == 0)
      return null;

    BigDecimal retval = currentValue.multiply(new BigDecimal(100)).divide(total, 4, BigDecimal.ROUND_HALF_UP);
    return retval;
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
   * Sets a property for the function.
   *
   * @param name The property name.
   * @param value The property value.
   */
  public void setProperty (String name, String value)
  {
    super.setProperty (name, value);
    totalSumFunction.setProperty (name, value);
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
    ItemPercentageFunction clone = (ItemPercentageFunction) super.clone ();
    clone.totalSumFunction = (TotalGroupSumFunction) totalSumFunction.clone();
    return clone;
  }
}