package com.jrefinery.report.util;

import javax.swing.table.*;
import java.sql.*;

public class ScrollableResultSetTableModel extends AbstractTableModel 
{
	/**
 	 * The scrollable ResultSet source.
 	 */
	protected ResultSet resultset;

	/**
	* The ResultSetMetaData object for this result set.
	*/
	protected ResultSetMetaData dbmd;

	/**
	* The number of rows in the result set.
	*/
	private int rowCount;

	/**
	* Constructs the model.
	*/
	public ScrollableResultSetTableModel (ResultSet resultset)
    throws SQLException
	{
		if (resultset != null)
		{
			updateResultSet(resultset);
		}
		else
		{
			clear();
		}
	}

	/**
	* DefaultConstructor.
	*/
	protected ScrollableResultSetTableModel ()
	{
	}


	/**
 	 * Updates the result set in this model with the given ResultSet object.
 	 */
	public void updateResultSet(ResultSet resultset)
    throws SQLException
	{
		if (this.resultset != null)
		{
			clear();
		}

		this.resultset = resultset;
		this.dbmd = resultset.getMetaData();

		if (resultset.last())
		{
			rowCount = resultset.getRow();
		}
		else
		{
			rowCount = 0;
		}

		fireTableStructureChanged();
	}

	/**
	* Clears the model of the current result set.
	*/
	public void clear()
	{
		// Close the old result set if needed.
		if (resultset != null)
		{
			try
			{
				resultset.close();
			}
			catch (SQLException e)
			{
				// Just in case the JDBC driver can't close a result set twice.
				//				e.printStackTrace();
			}
		}
		resultset = null;
		dbmd = null;
		rowCount = 0;
		fireTableStructureChanged();
	}

  /**
   * get an rowCount. This can be a very expensive operation on large
   * datasets. Returns -1 if the total amount of rows is not known.
   */
	public int getRowCount()
	{
    if (resultset == null)
      return 0;
      
  	try
  	{
			if (resultset.last())
			{
				rowCount = resultset.getRow();
        if (rowCount == -1)
        {
          rowCount = 0;
        }
			}
			else
			{
				rowCount = 0;
			}
  	}
    catch (SQLException sqle)
    {
    	return 0;
    }
		return rowCount;
	}

	public int getColumnCount()
	{
		if (resultset == null)
		  return 0;
		  
		if (dbmd != null)
		{
			try
			{
				return dbmd.getColumnCount();
			}
			catch (SQLException e)
			{
        e.printStackTrace ();
			}
		}
		return 0;
	}

	public String getColumnName(int column)
	{
		if (dbmd != null)
		{
			try
			{
				return dbmd.getColumnLabel(column + 1);
			}
			catch (SQLException e)
			{
			}
		}
    return null;
	}

	public Object getValueAt(int row, int column)
	{
		if (resultset != null)
		{
			try
			{
				resultset.absolute(row + 1);
				return resultset.getObject(column + 1);
			}
			catch (SQLException e)
			{
        e.printStackTrace ();
			}
		}
    return null;
	}
  
  public Class getColumnClass (int column)
  {
	  if (dbmd != null)
	  {
	  	try
	  	{
        return Class.forName (getColumnClassName (column));
	  	}
      catch (Exception e)
      {
      	e.printStackTrace ();
      }
	  }
    return Object.class;
  }
  
  public String getColumnClassName (int column)
  {
	  if (dbmd != null)
	  {
	  	try
	  	{
	  		return mckoiDBFixClassName (dbmd.getColumnClassName (column + 1));
	  	}
	  	catch (SQLException e)
	  	{
	  		e.printStackTrace ();
	  	}
	  }
    return Object.class.getName();
  }
  
  /**
   * Just removes the word class from the start of the classname string
   * McKoiDB version 0.92 was not able to properly return classnames of
   * resultset elements.
   */
  private String mckoiDBFixClassName (String classname)
  {
  	if (classname.startsWith ("class "))
    {
    	return classname.substring (6).trim ();
    }
    return classname;
  }
  
  
}
