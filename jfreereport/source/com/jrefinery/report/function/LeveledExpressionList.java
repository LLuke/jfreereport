/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport;
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * --------------------
 * LeveledExpressionList.java
 * --------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: LeveledExpressionList.java,v 1.4 2002/12/02 17:29:18 taqua Exp $
 *
 * Changes
 * -------
 */
package com.jrefinery.report.function;

import com.jrefinery.report.DataRow;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.ReportInitialisationException;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.event.ReportListener;
import com.jrefinery.report.util.LevelList;
import com.jrefinery.report.util.Log;

import java.util.Hashtable;
import java.util.Iterator;

public class LeveledExpressionList implements ReportListener, Cloneable
{
  private LevelList expressionList;
  private Hashtable nameLookup;
  private int level;

  public LeveledExpressionList(ExpressionCollection ec, ExpressionCollection fc)
      throws ReportInitialisationException 

  {
    expressionList = new LevelList();
    nameLookup = new Hashtable();
    initializeExpressions(ec);
    initializeFunctions(fc);
  }

  /**
   * Receives notification that report generation has started.
   * <P>
   * The event carries a ReportState.Started state.  Use this to initialize the report.
   *
   * @param event The event.
   */
  public void reportStarted(ReportEvent event)
  {
    Iterator it = expressionList.getLevelsDescending();
    while (it.hasNext())
    {
      Integer level = (Integer) it.next();
      if (level.intValue() < getLevel())
      {
        break;
      }
      Iterator itLevel = expressionList.getElementsForLevel(level.intValue());
      while (itLevel.hasNext())
      {
        Expression e = (Expression) itLevel.next();
        if (e instanceof Function)
        {
          Function f = (Function) e;
          try
          {
            f.reportStarted(event);
          }
          catch (Exception ex)
          {
            Log.error ("Function made a boo!", ex);
          }
        }
        else
        {
          if (e.isActive())
            e.getValue();
        }
      }
    }

  }

  /**
   * Receives notification that report generation has finished (the last record is read and all
   * groups are closed).
   *
   * @param event The event.
   */
  public void reportFinished(ReportEvent event)
  {
    Iterator it = expressionList.getLevelsDescending();
    while (it.hasNext())
    {
      Integer level = (Integer) it.next();
      if (level.intValue() < getLevel())
      {
        break;
      }
      Iterator itLevel = expressionList.getElementsForLevel(level.intValue());
      while (itLevel.hasNext())
      {
        Expression e = (Expression) itLevel.next();
        if (e instanceof Function)
        {
          Function f = (Function) e;
          try
          {
            f.reportFinished(event);
          }
          catch (Exception ex)
          {
            Log.error ("Function made a boo!", ex);
          }
        }
        else
        {
          if (e.isActive())
            e.getValue();
        }
      }
    }
  }

  /**
   * Receives notification that a new page is being started.
   *
   * @param event The event.
   */
  public void pageStarted(ReportEvent event)
  {
    Iterator it = expressionList.getLevelsDescending();
    while (it.hasNext())
    {
      Integer level = (Integer) it.next();
      if (level.intValue() < getLevel())
      {
        break;
      }
      Iterator itLevel = expressionList.getElementsForLevel(level.intValue());
      while (itLevel.hasNext())
      {
        Expression e = (Expression) itLevel.next();
        if (e instanceof Function)
        {
          Function f = (Function) e;
          try
          {
            f.pageStarted(event);
          }
          catch (Exception ex)
          {
            Log.error ("Function made a boo!", ex);
          }
        }
        else
        {
          if (e.isActive())
            e.getValue();
        }
      }
    }
  }

  /**
   * Receives notification that a page is completed.
   *
   * @param event The event.
   */
  public void pageFinished(ReportEvent event)
  {
    Iterator it = expressionList.getLevelsDescending();
    while (it.hasNext())
    {
      Integer level = (Integer) it.next();
      if (level.intValue() < getLevel())
      {
        break;
      }
      Iterator itLevel = expressionList.getElementsForLevel(level.intValue());
      while (itLevel.hasNext())
      {
        Expression e = (Expression) itLevel.next();
        if (e instanceof Function)
        {
          Function f = (Function) e;
          try
          {
            f.pageFinished(event);
          }
          catch (Exception ex)
          {
            Log.error ("Function made a boo!", ex);
          }
        }
        else
        {
          if (e.isActive())
            e.getValue();
        }
      }
    }
  }

  /**
   * Receives notification that a new group has started.
   * <P>
   * The group can be determined by the report state's getCurrentGroup() function.
   *
   * @param event The event.
   */
  public void groupStarted(ReportEvent event)
  {
    Iterator it = expressionList.getLevelsDescending();
    while (it.hasNext())
    {
      Integer level = (Integer) it.next();
      if (level.intValue() < getLevel())
      {
        break;
      }
      Iterator itLevel = expressionList.getElementsForLevel(level.intValue());
      while (itLevel.hasNext())
      {
        Expression e = (Expression) itLevel.next();
        if (e instanceof Function)
        {
          Function f = (Function) e;
          try
          {
            f.groupStarted(event);
          }
          catch (Exception ex)
          {
            Log.error ("Function made a boo!", ex);
          }
        }
        else
        {
          if (e.isActive())
            e.getValue();
        }
      }
    }
  }

  /**
   * Receives notification that a group is finished.
   * <P>
   * The group can be determined by the report state's getCurrentGroup() function.
   *
   * @param event The event.
   */
  public void groupFinished(ReportEvent event)
  {
    Iterator it = expressionList.getLevelsDescending();
    while (it.hasNext())
    {
      Integer level = (Integer) it.next();
      if (level.intValue() < getLevel())
      {
        break;
      }
      Iterator itLevel = expressionList.getElementsForLevel(level.intValue());
      while (itLevel.hasNext())
      {
        Expression e = (Expression) itLevel.next();
        if (e instanceof Function)
        {
          Function f = (Function) e;
          try
          {
            f.groupFinished(event);
          }
          catch (Exception ex)
          {
            Log.error ("Function made a boo!", ex);
          }
        }
        else
        {
          if (e.isActive())
            e.getValue();
        }
      }
    }
  }

  /**
   * Receives notification that a group of item bands is about to be processed.
   * <P>
   * The next events will be itemsAdvanced events until the itemsFinished event is raised.
   *
   * @param event The event.
   */
  public void itemsStarted(ReportEvent event)
  {
    Iterator it = expressionList.getLevelsDescending();
    while (it.hasNext())
    {
      Integer level = (Integer) it.next();
      if (level.intValue() < getLevel())
      {
        break;
      }
      Iterator itLevel = expressionList.getElementsForLevel(level.intValue());
      while (itLevel.hasNext())
      {
        Expression e = (Expression) itLevel.next();
        if (e instanceof Function)
        {
          Function f = (Function) e;
          try
          {
            f.itemsStarted(event);
          }
          catch (Exception ex)
          {
            Log.error ("Function made a boo!", ex);
          }
        }
        else
        {
          if (e.isActive())
            e.getValue();
        }
      }
    }
  }

  /**
   * Receives notification that a group of item bands has been completed.
   * <P>
   * The itemBand is finished, the report starts to close open groups.
   *
   * @param event The event.
   */
  public void itemsFinished(ReportEvent event)
  {
    Iterator it = expressionList.getLevelsDescending();
    while (it.hasNext())
    {
      Integer level = (Integer) it.next();
      if (level.intValue() < getLevel())
      {
        break;
      }
      Iterator itLevel = expressionList.getElementsForLevel(level.intValue());
      while (itLevel.hasNext())
      {
        Expression e = (Expression) itLevel.next();
        if (e instanceof Function)
        {
          Function f = (Function) e;
          try
          {
            f.itemsFinished(event);
          }
          catch (Exception ex)
          {
            Log.error ("Function made a boo!", ex);
          }
        }
        else
        {
          if (e.isActive())
            e.getValue();
        }
      }
    }
  }

  /**
   * Receives notification that a new row has been read.
   * <P>
   * This event is raised before an ItemBand is printed.
   *
   * @param event The event.
   */
  public void itemsAdvanced(ReportEvent event)
  {
    Iterator it = expressionList.getLevelsDescending();
    while (it.hasNext())
    {
      Integer level = (Integer) it.next();
      if (level.intValue() < getLevel())
      {
        break;
      }
      Iterator itLevel = expressionList.getElementsForLevel(level.intValue());
      while (itLevel.hasNext())
      {
        Expression e = (Expression) itLevel.next();
        if (e instanceof Function)
        {
          Function f = (Function) e;
          try
          {
            f.itemsAdvanced(event);
          }
          catch (Exception ex)
          {
            Log.error ("Function made a boo!", ex);
          }
        }
        else
        {
          if (e.isActive())
            e.getValue();
        }
      }
    }
  }

  /**
   * Connects the given datarow to the expression collection and all expressions contained in
   * this collection.
   *
   * @param dr  the datarow to be connected (null not permitted).
   *
   * @throws IllegalStateException if there is a datarow already connected.
   * @throws NullPointerException if the given datarow is null.
   */
  public void connectDataRow(DataRow dr)
  {
    if (dr == null)
    {
      throw new NullPointerException();
    }
    for (int i = 0; i < expressionList.size(); i++)
    {
      Expression f = (Expression) expressionList.get(i);
      f.setDataRow(dr);
    }
  }

  /**
   * Disconnects the datarow from the expression.
   *
   * @param dr the datarow to be connected.
   * @throws NullPointerException if the given datarow is null.
   */
  public void disconnectDataRow(DataRow dr)
  {
    if (dr == null)
    {
      throw new NullPointerException("Null-DataRowBackend cannot be disconnected.");
    }
    for (int i = 0; i < expressionList.size(); i++)
    {
      Expression f = (Expression) expressionList.get(i);
      f.setDataRow(null);
    }
  }

  private void initializeExpressions(ExpressionCollection expressionCollection)
    throws ReportInitialisationException
  {
    int size = expressionCollection.size();
    for (int i = 0; i < size; i++)
    {
      Expression f = expressionCollection.getExpression(i);
      if (f != null)
      {
        expressionList.add(f);
        expressionList.setLevel(f, f.getDepencyLevel());
        addName(f);
      }
    }
  }

  private void initializeFunctions(ExpressionCollection functionCollection)
      throws ReportInitialisationException
  {
    int size = functionCollection.size();
    for (int i = 0; i < size; i++)
    {
      // Explicit cast to Function to test all contained elements to be Functions!
      Function f = (Function) functionCollection.getExpression(i);
      if (f != null)
      {
        expressionList.add(f);
        expressionList.setLevel(f, f.getDepencyLevel());
        addName(f);
      }
    }
  }

  private void addName(Expression ex) throws ReportInitialisationException
  {
    String name = ex.getName();
    if (nameLookup.containsKey(name))
      throw new ReportInitialisationException ("Duplicate Name found: " + name);

    nameLookup.put(name, ex);
  }

  /**
   * Size does not change, so it is cached.
   */
  public int size()
  {
    return expressionList.size();
  }

  /**
   * Creates and returns a copy of this object.  The precise meaning
   * of "copy" may depend on the class of the object. The general
   * intent is that, for any object <tt>x</tt>, the expression:
   * <blockquote>
   * <pre>
   * x.clone() != x</pre></blockquote>
   * will be true, and that the expression:
   * <blockquote>
   * <pre>
   * x.clone().getClass() == x.getClass()</pre></blockquote>
   * will be <tt>true</tt>, but these are not absolute requirements.
   * While it is typically the case that:
   * <blockquote>
   * <pre>
   * x.clone().equals(x)</pre></blockquote>
   * will be <tt>true</tt>, this is not an absolute requirement.
   * Copying an object will typically entail creating a new instance of
   * its class, but it also may require copying of internal data
   * structures as well.  No constructors are called.
   * <p>
   * The method <tt>clone</tt> for class <tt>Object</tt> performs a
   * specific cloning operation. First, if the class of this object does
   * not implement the interface <tt>Cloneable</tt>, then a
   * <tt>CloneNotSupportedException</tt> is thrown. Note that all arrays
   * are considered to implement the interface <tt>Cloneable</tT>.
   * Otherwise, this method creates a new instance of the class of this
   * object and initializes all its fields with exactly the contents of
   * the corresponding fields of this object, as if by assignment; the
   * contents of the fields are not themselves cloned. Thus, this method
   * performs a "shallow copy" of this object, not a "deep copy" operation.
   * <p>
   * The class <tt>Object</tt> does not itself implement the interface
   * <tt>Cloneable</tt>, so calling the <tt>clone</tt> method on an object
   * whose class is <tt>Object</tt> will result in throwing an
   * exception at run time. The <tt>clone</tt> method is implemented by
   * the class <tt>Object</tt> as a convenient, general utility for
   * subclasses that implement the interface <tt>Cloneable</tt>, possibly
   * also overriding the <tt>clone</tt> method, in which case the
   * overriding definition can refer to this utility definition by the
   * call:
   * <blockquote>
   * <pre>
   * super.clone()</pre></blockquote>
   *
   * @return     a clone of this instance.
   * @exception  CloneNotSupportedException  if the object's class does not
   *               support the <code>Cloneable</code> interface. Subclasses
   *               that override the <code>clone</code> method can also
   *               throw this exception to indicate that an instance cannot
   *               be cloned.
   * @exception  OutOfMemoryError            if there is not enough memory.
   * @see        java.lang.Cloneable
   */
  public Object clone() throws CloneNotSupportedException
  {
    LeveledExpressionList ft = (LeveledExpressionList) super.clone();
    ft.nameLookup = (Hashtable) nameLookup.clone();
    ft.expressionList = (LevelList) expressionList.clone();
    ft.expressionList.clear();

    int size = expressionList.size();
    for (int i = 0; i < size; i++)
    {
      Expression ex = (Expression) expressionList.get(i);
      if (ex instanceof Function)
      {
        Expression exClone = (Expression) ex.clone();
        ft.expressionList.add(exClone, expressionList.getLevel(i));
        ft.nameLookup.put(ex.getName(), exClone);
      }
      else
      {
        ft.expressionList.add (ex, expressionList.getLevel(i));
      }
    }
    return ft;
  }

  public void setLevel(int level)
  {
    this.level = level;
  }

  public int getLevel()
  {
    return level;
  }

  public Iterator getLevelsDescending()
  {
    return expressionList.getLevelsDescending();
  }

  public Iterator getLevelsAscending()
  {
    return expressionList.getLevelsAscending();
  }

  public Object getValue(int index)
  {
    return ((Expression) expressionList.get(index)).getValue();
  }

  public Expression getExpression(int index)
  {
    return ((Expression) expressionList.get(index));
  }

  public Object getValue(String name)
  {
    return ((Expression) nameLookup.get(name)).getValue();
  }

  public Expression getExpression(String name)
  {
    return (Expression) nameLookup.get(name);
  }
}
