/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ------------------------
 * CSVReader.java
 * ------------------------
 * (C)opyright 2000-2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Mimil;
 *
 * $Id: CSVReader.java,v 1.2 2004/08/07 17:45:47 mimil Exp $
 *
 * $Log: CSVReader.java,v $
 * Revision 1.2  2004/08/07 17:45:47  mimil
 * Some JavaDocs
 *
 * Revision 1.1  2004/08/07 14:35:14  mimil
 * Initial version
 *
 */

package org.jfree.report.ext.input;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.table.TableModel;

import org.jfree.report.util.CSVTokenizer;

/**
 * Creates a <code>TableModel</code> using a file formated in CSV for input. The
 * separation can be what ever you want (as it is an understandable regexp). The default
 * separator is a <code>;</code>.
 *
 * @author Mimil
 */
public class CSVReader
{
  private BufferedReader reader;
  private String separator = ";";
  private CSVTableModel tableModel;
  private boolean columnNameFirst = false;

  public CSVReader (final InputStream in)
  {
    if (in == null)
    {
      throw new NullPointerException("The input stream must not be null");
    }
    this.reader = new BufferedReader(new InputStreamReader(in));
  }

  public CSVReader (final String filename)
    throws FileNotFoundException
  {
    this.reader = new BufferedReader(new FileReader(filename));
  }

  public CSVReader (final BufferedReader r)
  {
    if (r == null)
    {
      throw new NullPointerException("The input stream must not be null");
    }
    this.reader = r;
  }

  /**
   * Parses the input and stores data in a TableModel.
   *
   * @see this.getTableModel()
   */
  public synchronized TableModel parse () throws IOException
  {
    if (tableModel != null)
    {
      return tableModel;
    }

    this.tableModel = new CSVTableModel();

    if (this.columnNameFirst == true)
    {   //read the fisrt line
      final String first = this.reader.readLine();

      if (first == null)
      {
        // after the end of the file it makes no sense to read anything.
        // so we can safely return ..
        return tableModel;
      }
      this.tableModel.setColumnNames(splitLine(first));
    }

    final ArrayList data = new ArrayList();
    String line;
    int maxLength = 0;
    while ((line = this.reader.readLine()) != null)
    {
      final String[] o = splitLine(line);
      if (o.length > maxLength)
      {
        maxLength = o.length;
      }
      data.add(o);
    }

    final Object[][] array = new Object[data.size()][];
    data.toArray(array);
    this.tableModel.setData(array);
    return tableModel;
  }

  private String[] splitLine (final String line)
  {
    final ArrayList row = new ArrayList();
    final CSVTokenizer tokenizer = new CSVTokenizer(line, getSeparator());
    while (tokenizer.hasMoreElements())
    {
      row.add(tokenizer.nextElement());
    }
    return (String[]) row.toArray(new String[row.size()]);
  }

  /**
   * Returns the current separator used to parse the input.
   *
   * @return a regexp
   */
  public String getSeparator ()
  {
    return separator;
  }

  /**
   * Sets the separator for parsing the input. It can be a regexp as we use the function
   * <code>String.split()</code>. The default separator is a <code>;</code>.
   *
   * @param separator a regexp
   */
  public void setSeparator (final String separator)
  {
    this.separator = separator;
  }

  /**
   * Creates the corrspondant TableModel of the input.
   *
   * @return the new TableModel
   */
  public TableModel getTableModel () throws IOException
  {
    return this.parse();
  }

  /**
   * Tells if the first line of the input was column names.
   *
   * @return boolean
   */
  public boolean isColumnNameFirstLine ()
  {
    return columnNameFirst;
  }

  /**
   * Set if the first line of the input is column names or not.
   *
   * @param columnNameFirst boolean
   */
  public void setColumnNameFirstLine (final boolean columnNameFirst)
  {
    this.columnNameFirst = columnNameFirst;
  }

}
