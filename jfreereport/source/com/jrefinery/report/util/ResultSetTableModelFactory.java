package com.jrefinery.report.util;

import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Creates an TableModel which is backed up by an ResultSet.
 * If the resultset is scrollable, a ScrollableResultSetTableModel is
 * created, else all data is copied from the resultSet into an 
 * DefaultTableModel.
 *
 * The creation of default table models can be forces if the system property
 * "com.jrefinery.report.TableFactoryMode" is set to "simple".
 */
public class ResultSetTableModelFactory
{
	public TableModel createTableModel (ResultSet rs)
  	throws SQLException
	{
    // Allow for override, some jdbc drivers are buggy :(
    if (System.getProperty ("com.jrefinery.report.TableFactoryMode", "").equalsIgnoreCase("simple"))
    {
      return generateDefaultTableModel (rs);
    }
  	if (rs.getType () == ResultSet.TYPE_FORWARD_ONLY)
    {
    	return generateDefaultTableModel (rs);
    }
    else
    {
    	return new ScrollableResultSetTableModel (rs);
    }
	}
  
  public DefaultTableModel generateDefaultTableModel (ResultSet rs)
    throws SQLException
  {
  	ResultSetMetaData rsmd = rs.getMetaData ();
    int colcount = rsmd.getColumnCount ();
    Vector header = new Vector (colcount);
		for (int i = 0; i < colcount; i++)
		{
    	String name = rsmd.getColumnName (i + 1);
      header.add (name);
		}
    Vector rows  = new Vector ();
    while (rs.next ())
    {
    	Vector column = new Vector (colcount);
	    for (int i = 0; i < colcount; i++)
	    {
	    	Object val = rs.getObject (i + 1);
        if (val == null)
        {
        	val = getNullValue();
        }
	      header.add (val);
	    }
	    rows.add (column);
    }
    DefaultTableModel model = new DefaultTableModel (rows, header);
    return model;
  }
  
  private Object nullValue;
  
  public void setNullValue (Object nullval)
  {
    nullValue = nullval;
  }
  
  public Object getNullValue ()
  {
    return nullValue;
  }
  
  private static ResultSetTableModelFactory defaultInstance;
  
  public static ResultSetTableModelFactory getInstance ()
  {
    if (defaultInstance == null)
    {
      defaultInstance = new ResultSetTableModelFactory ();
    }
    return defaultInstance;
  }
}
