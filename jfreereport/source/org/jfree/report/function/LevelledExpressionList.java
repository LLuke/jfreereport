/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * --------------------------
 * LevelledExpressionList.java
 * --------------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: LevelledExpressionList.java,v 1.6 2003/09/11 22:17:09 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Updates Javadocs (DG);
 * 06-Jul-2003 : Only PageEventListener will receive page events.
 */

package org.jfree.report.function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Arrays;

import org.jfree.report.DataRow;
import org.jfree.report.event.LayoutEvent;
import org.jfree.report.event.LayoutListener;
import org.jfree.report.event.PageEventListener;
import org.jfree.report.event.PrepareEventListener;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.event.ReportListener;
import org.jfree.report.util.LevelList;

/**
 * A list of expressions/functions and associated levels.  This class listens for report events,
 * then passes these events on to the expressions and functions in *descending* level order.
 *
 * @author Thomas Morgner
 */
public final class LevelledExpressionList implements ReportListener,
    Cloneable, LayoutListener, PageEventListener
{
  /** error list stores the errors that occur during the event dispatching. */
  private ArrayList errorList;

  /** The current processing level. */
  private int level;

  /** The levels (in descending order) */
  private int[] levels;

  /** The dataRow for all functions. */
  private DataRow dataRow;

  /** The expressions sorted by levels. */
  private Expression[][] levelData;
  /** all data as flat list */
  private Expression[] flatData;

  /** The number of functions and expressions in this list. */
  private int size;

  /**
   * DefaultConstructor.
   */
  protected LevelledExpressionList()
  {
    errorList = new ArrayList();
    levels = new int[0];
  }

  /**
   * Creates a new list.
   *
   * @param ec  the expressions from the report definition.
   * @param fc  the functions from the report definition.
   */
  public LevelledExpressionList(final ExpressionCollection ec, final ExpressionCollection fc)
  {
    this();
    initialize(ec, fc);
  }

  /**
   * Builds the list of all levels. This is done once after the initialisation,
   * as the functions level is not expected to change after the function was
   * initialized.
   * 
   * @param expressionList the level list from where to build the data.
   * @return the function levels.
   */
  private int[] buildLevels(LevelList expressionList)
  {
    // copy all levels from the collections to the cache ...
    // we assume, that the collections do not change anymore!
    final ArrayList al = new ArrayList();
    final Iterator it = expressionList.getLevelsDescending();
    while (it.hasNext())
    {
      final Integer level = (Integer) it.next();
      al.add(level);
    }
    final int[] levels = new int[al.size()];
    for (int i = 0; i < levels.length; i++)
    {
      final Integer level = (Integer) al.get(i);
      levels[i] = level.intValue();
    }
    return levels;
  }

  /**
   * Receives notification that report generation has started.
   * <P>
   * The event carries a ReportState.Started state.
   * Use this to prepare the report header.
   *
   * @param event  the event.
   */
  public void reportStarted(final ReportEvent event)
  {
//    clearError(); is done in the prepare event ...

    for (int i = 0; i < levels.length; i++)
    {
      final int level = levels[i];
      if (level < getLevel())
      {
        break;
      }
      final Object[] itLevel = levelData[i];
      for (int l = 0; l < itLevel.length; l++)
      {
        final Expression e = (Expression) itLevel[l];
        if (e instanceof Function)
        {
          final Function f = (Function) e;
          try
          {
            f.reportStarted(event);
          }
          catch (Exception ex)
          {
            addError(ex);
          }
        }
        else
        {
          try
          {
            if (e.isActive())
            {
              e.getValue();
            }
          }
          catch (Exception ex)
          {
            addError(ex);
          }
        }
      }
    }

  }

  /**
   * Receives notification that report generation has started.
   * <P>
   * The event carries a ReportState.Started state.  Use this to initialize the report.
   *
   * @param event  the event.
   */
  public void reportInitialized(final ReportEvent event)
  {
    clearError(); // has no prepare event ...

    for (int i = 0; i < levels.length; i++)
    {
      final int level = levels[i];
      if (level < getLevel())
      {
        break;
      }
      final Object[] itLevel = levelData[i];
      for (int l = 0; l < itLevel.length; l++)
      {
        final Expression e = (Expression) itLevel[l];
        if (e instanceof Function)
        {
          final Function f = (Function) e;
          try
          {
            f.reportInitialized(event);
          }
          catch (Exception ex)
          {
            addError(ex);
          }
        }
        else
        {
          try
          {
            if (e.isActive())
            {
              e.getValue();
            }
          }
          catch (Exception ex)
          {
            addError(ex);
          }
        }
      }
    }
  }

  /**
   * Receives notification that report generation has finished (the last record is read and all
   * groups are closed).
   *
   * @param event  the event.
   */
  public void reportFinished(final ReportEvent event)
  {
    // clearError(); done in the prepare event ...

    for (int i = 0; i < levels.length; i++)
    {
      final int level = levels[i];
      if (level < getLevel())
      {
        break;
      }
      final Object[] itLevel = levelData[i];
      for (int l = 0; l < itLevel.length; l++)
      {
        final Expression e = (Expression) itLevel[l];
        if (e instanceof Function)
        {
          final Function f = (Function) e;
          try
          {
            f.reportFinished(event);
          }
          catch (Exception ex)
          {
            addError(ex);
          }
        }
        else
        {
          try
          {
            if (e.isActive())
            {
              e.getValue();
            }
          }
          catch (Exception ex)
          {
            addError(ex);
          }
        }
      }
    }
  }

  /**
   * Receives notification that a new page is being started.
   *
   * @param event  the event.
   */
  public void pageStarted(final ReportEvent event)
  {
    // this is an internal event, don't fire prepare or clear the errors.
    for (int i = 0; i < levels.length; i++)
    {
      final int level = levels[i];
      if (level < getLevel())
      {
        break;
      }
      final Object[] itLevel = levelData[i];
      for (int l = 0; l < itLevel.length; l++)
      {
        final Expression e = (Expression) itLevel[l];
        if (e instanceof Function)
        {
          if (e instanceof PageEventListener)
          {
            final PageEventListener f = (PageEventListener) e;
            try
            {
              f.pageStarted(event);
            }
            catch (Exception ex)
            {
              addError(ex);
            }
          }
        }
        else
        {
          try
          {
            if (e.isActive())
            {
              e.getValue();
            }
          }
          catch (Exception ex)
          {
            addError(ex);
          }
        }
      }
    }
  }

  /**
   * Receives notification that a new page is being started.
   *
   * @param event  the event.
   */
  public void pageCanceled(final ReportEvent event)
  {
    // this is an internal event, don't fire prepare or clear the errors.
    for (int i = 0; i < levels.length; i++)
    {
      final int level = levels[i];
      if (level < getLevel())
      {
        break;
      }
      final Object[] itLevel = levelData[i];
      for (int l = 0; l < itLevel.length; l++)
      {
        final Expression e = (Expression) itLevel[l];
        if (e instanceof PageEventListener)
        {
          final PageEventListener f = (PageEventListener) e;
          try
          {
            f.pageCanceled(event);
          }
          catch (Exception ex)
          {
            addError(ex);
          }
        }
      }
    }
  }


  /**
   * Receives notification that a page is completed.
   *
   * @param event  the event.
   */
  public void pageFinished(final ReportEvent event)
  {
    // this is an internal event, don't fire prepare or clear the errors.
    for (int i = 0; i < levels.length; i++)
    {
      final int level = levels[i];
      if (level < getLevel())
      {
        break;
      }
      final Object[] itLevel = levelData[i];
      for (int l = 0; l < itLevel.length; l++)
      {
        final Expression e = (Expression) itLevel[l];
        if (e instanceof Function)
        {
          if (e instanceof PageEventListener)
          {
            final PageEventListener f = (PageEventListener) e;
            try
            {
              f.pageFinished(event);
            }
            catch (Exception ex)
            {
              addError(ex);
            }
          }
        }
        else
        {
          try
          {
            if (e.isActive())
            {
              e.getValue();
            }
          }
          catch (Exception ex)
          {
            addError(ex);
          }
        }
      }
    }
  }

  /**
   * Receives notification that a new group has started.
   * <P>
   * The group can be determined by the report state's getCurrentGroup() function.
   *
   * @param event  the event.
   */
  public void groupStarted(final ReportEvent event)
  {
    // clearError(); done in the prepare event ...

    for (int i = 0; i < levels.length; i++)
    {
      final int level = levels[i];
      if (level < getLevel())
      {
        break;
      }
      final Object[] itLevel = levelData[i];
      for (int l = 0; l < itLevel.length; l++)
      {
        final Expression e = (Expression) itLevel[l];
        if (e instanceof Function)
        {
          final Function f = (Function) e;
          try
          {
            f.groupStarted(event);
          }
          catch (Exception ex)
          {
            addError(ex);
          }
        }
        else
        {
          try
          {
            if (e.isActive())
            {
              e.getValue();
            }
          }
          catch (Exception ex)
          {
            addError(ex);
          }
        }
      }
    }
  }

  /**
   * Receives notification that a group is finished.
   * <P>
   * The group can be determined by the report state's getCurrentGroup() function.
   *
   * @param event  the event.
   */
  public void groupFinished(final ReportEvent event)
  {
    // clearError(); done in the prepare event ...

    for (int i = 0; i < levels.length; i++)
    {
      final int level = levels[i];
      if (level < getLevel())
      {
        break;
      }
      final Object[] itLevel = levelData[i];
      for (int l = 0; l < itLevel.length; l++)
      {
        final Expression e = (Expression) itLevel[l];
        if (e instanceof Function)
        {
          final Function f = (Function) e;
          try
          {
            f.groupFinished(event);
          }
          catch (Exception ex)
          {
            addError(ex);
          }
        }
        else
        {
          try
          {
            if (e.isActive())
            {
              e.getValue();
            }
          }
          catch (Exception ex)
          {
            addError(ex);
          }
        }
      }
    }
  }

  /**
   * Receives notification that a group of item bands is about to be processed.
   * <P>
   * The next events will be itemsAdvanced events until the itemsFinished event is raised.
   *
   * @param event  the event.
   */
  public void itemsStarted(final ReportEvent event)
  {
    // clearError(); done in the prepare event ...

    for (int i = 0; i < levels.length; i++)
    {
      final int level = levels[i];
      if (level < getLevel())
      {
        break;
      }
      final Object[] itLevel = levelData[i];
      for (int l = 0; l < itLevel.length; l++)
      {
        final Expression e = (Expression) itLevel[l];
        if (e instanceof Function)
        {
          final Function f = (Function) e;
          try
          {
            f.itemsStarted(event);
          }
          catch (Exception ex)
          {
            addError(ex);
          }
        }
        else
        {
          try
          {
            if (e.isActive())
            {
              e.getValue();
            }
          }
          catch (Exception ex)
          {
            addError(ex);
          }
        }
      }
    }
  }

  /**
   * Receives notification that a group of item bands has been completed.
   * <P>
   * The itemBand is finished, the report starts to close open groups.
   *
   * @param event  the event.
   */
  public void itemsFinished(final ReportEvent event)
  {
    // clearError(); done in the prepare event ...

    for (int i = 0; i < levels.length; i++)
    {
      final int level = levels[i];
      if (level < getLevel())
      {
        break;
      }
      final Object[] itLevel = levelData[i];
      for (int l = 0; l < itLevel.length; l++)
      {
        final Expression e = (Expression) itLevel[l];
        if (e instanceof Function)
        {
          final Function f = (Function) e;
          try
          {
            f.itemsFinished(event);
          }
          catch (Exception ex)
          {
            addError(ex);
          }
        }
        else
        {
          try
          {
            if (e.isActive())
            {
              e.getValue();
            }
          }
          catch (Exception ex)
          {
            addError(ex);
          }
        }
      }
    }
  }

  /**
   * Receives notification that a new row has been read.
   * <P>
   * This event is raised before an ItemBand is printed.
   *
   * @param event  the event.
   */
  public void itemsAdvanced(final ReportEvent event)
  {
    // clearError(); done in the prepare event ...

    for (int i = 0; i < levels.length; i++)
    {
      final int level = levels[i];
      if (level < getLevel())
      {
        break;
      }
      final Object[] itLevel = levelData[i];
      for (int l = 0; l < itLevel.length; l++)
      {
        final Expression e = (Expression) itLevel[l];
        if (e instanceof Function)
        {
          final Function f = (Function) e;
          try
          {
            f.itemsAdvanced(event);
          }
          catch (Exception ex)
          {
            addError(ex);
          }
        }
        else
        {
          try
          {
            if (e.isActive())
            {
              e.getValue();
            }
          }
          catch (Exception ex)
          {
            addError(ex);
          }
        }
      }
    }
  }

  /**
   * Receives notification that the band layouting has completed.
   * <P>
   * The event carries the current report state.
   *
   * @param event The event.
   */
  public void layoutComplete(final LayoutEvent event)
  {
    // this is an internal event, no need to handle prepare outside ..
    // clearError();
    firePrepareEventLayoutListener(event);

    for (int i = 0; i < levels.length; i++)
    {
      final int level = levels[i];
      if (level < getLevel())
      {
        break;
      }
      final Object[] itLevel = levelData[i];
      for (int l = 0; l < itLevel.length; l++)
      {
        final Expression e = (Expression) itLevel[l];
        if (e instanceof LayoutListener && e instanceof Function)
        {
          final LayoutListener f = (LayoutListener) e;
          try
          {
            f.layoutComplete(event);
          }
          catch (Exception ex)
          {
            addError(ex);
          }
        }
      }
    }
  }

  /**
   * Receives notification that report generation has completed, the report footer was printed,
   * no more output is done. This is a helper event to shut down the output service.
   *
   * @param event The event.
   */
  public void reportDone(final ReportEvent event)
  {
    // clearError(); done in the prepare event ...

    for (int i = 0; i < levels.length; i++)
    {
      final int level = levels[i];
      if (level < getLevel())
      {
        break;
      }
      final Object[] itLevel = levelData[i];
      for (int l = 0; l < itLevel.length; l++)
      {
        final Expression e = (Expression) itLevel[l];
        if (e instanceof Function)
        {
          final Function f = (Function) e;
          try
          {
            f.reportDone(event);
          }
          catch (Exception ex)
          {
            addError(ex);
          }
        }
        else
        {
          try
          {
            if (e.isActive())
            {
              e.getValue();
            }
          }
          catch (Exception ex)
          {
            addError(ex);
          }
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
  public void setDataRow(final DataRow dr)
  {
    if (dr != null && dataRow != null)
    {
      // be paranoid and make sure that we dont replace the datarow
      // by accident
      throw new IllegalStateException
          ("Paranoia: Update calls must be done using the updateDataRow method.");
    }
    updateDataRow(dr);
  }

  /**
   * Updates the datarow for all expressions. Does not perform validity
   * checks, so use this function with care.
   *
   * @param dr  the datarow to be connected.
   *
   * @throws NullPointerException if the given datarow is null.
   * @throws IllegalStateException if there is no datarow connected.
   */
  public void updateDataRow(final DataRow dr)
  {
    dataRow = dr;
    for (int i = 0; i < levelData.length; i++)
    {
      for (int j = 0; j < levelData[i].length; j++)
      {
        final Expression f = levelData[i][j];
        f.setDataRow(dr);
      }
    }
  }

  /**
   * Returns the currently connected dataRow.
   *
   * @return the dataRow.
   */
  public DataRow getDataRow()
  {
    return dataRow;
  }

  /**
   * Initialises the expressions.
   *
   * @param expressionCollection  the expression collection.
   * @param functionCollection the function collection.
   */
  private void initialize(final ExpressionCollection expressionCollection,
                          final ExpressionCollection functionCollection)
  {
    LevelList expressionList = new LevelList();

    int size = expressionCollection.size();
    for (int i = 0; i < size; i++)
    {
      Expression f = expressionCollection.getExpression(i);
      if (f != null)
      {
        f = f.getInstance();
        expressionList.add(f);
        expressionList.setLevel(f, f.getDependencyLevel());
      }
    }
    if (functionCollection != null)
    {
      size = functionCollection.size();
      for (int i = 0; i < size; i++)
      {
        // Explicit cast to Function to test all contained elements to be Functions!
        // this may be just paranoid.
        // todo 090 next redesign should unify functions and expressions in the
        // report object.
        Function f = (Function) functionCollection.getExpression(i);
        if (f != null)
        {
          f = (Function) f.getInstance();
          expressionList.add(f);
          expressionList.setLevel(f, f.getDependencyLevel());
        }
      }
    }
    initializeFromLevelList(expressionList);
  }

  /**
   * Initializes the expression list from the given levellist.
   * 
   * @param expressionList the list containing the expressions and 
   * functions.
   */
  private void initializeFromLevelList (LevelList expressionList)
  {
    this.size = 0;
    this.levels = buildLevels(expressionList);
    this.levelData = new Expression[levels.length][];
    this.flatData = new Expression[expressionList.size()];

    for (int i = 0; i < levels.length; i++)
    {
      final int currentLevel = levels[i];
      Expression[] data = (Expression[])
          expressionList.getElementArrayForLevel(currentLevel,
          new Expression[expressionList.getElementCountForLevel(currentLevel)]);

      this.levelData[i] = data;
      System.arraycopy(data, 0, this.flatData, this.size, data.length);
      this.size += data.length;
    }
  }

  /**
   * Size does not change, so it is cached.
   *
   * @return the size.
   */
  public int size()
  {
    return size;
  }

  /**
   * Creates and returns a copy of this object.  The precise meaning
   * of "copy" may depend on the class of the object.
   * <p>
   * The cloned LevelledExpressionList will no longer be connected to
   * a datarow.
   *
   * @return     a clone of this instance.
   * @exception  CloneNotSupportedException  if the object's class does not
   *               support the <code>Cloneable</code> interface.
   * @exception  OutOfMemoryError            if there is not enough memory.
   * @see        java.lang.Cloneable
   */
  public Object clone() throws CloneNotSupportedException
  {
    final LevelledExpressionList ft = (LevelledExpressionList) super.clone();
    // ft.levels = levels; // no need to clone them ...
    // ft.size = size;     // already copied during cloning ...
    ft.dataRow = null;
    ft.errorList = (ArrayList) errorList.clone();
    ft.levelData = new Expression[levelData.length][];
    ft.flatData = new Expression[flatData.length];

    int flatDataPos = 0;
    for (int level = 0; level < levelData.length; level++)
    {
      ft.levelData[level] = new Expression[levelData[level].length];
      for (int i = 0; i < levelData[level].length; i++)
      {
        final Expression ex = levelData[level][i];
        if (ex instanceof Function)
        {
          final Expression exClone = (Expression) ex.clone();
          exClone.setDataRow(null);
          ft.levelData[level][i] = exClone;
          ft.flatData[flatDataPos] = exClone;
        }
        else
        {
          final Expression exClone = ex.getInstance();
          exClone.setDataRow(null);
          ft.levelData[level][i] = exClone;
          ft.flatData[flatDataPos] = exClone;
        }
        flatDataPos++;
      }
    }
    return ft;
  }

  /**
   * Returns the preview instance of the levelled expression list. This list
   * does no longer contain any Function instances.
   *  
   * @return the preview (expressions only) instance of the levelled expression list.
   */
  public LevelledExpressionList getPreviewInstance()
  {
    final LevelledExpressionList ft = new LevelledExpressionList();
    ft.errorList = new ArrayList();
    final LevelList expressionList = new LevelList();

    for (int level = 0; level < levelData.length; level++)
    {
      for (int i = 0; i < levelData[level].length; i++)
      {
        final Expression ex = levelData[level][i];
        if (ex instanceof Function)
        {
          // ignore it, functions are state dependent and cannot be used
          // to compute group changes ...
        }
        else
        {
          expressionList.add(ex.getInstance(), level);
        }
      }
    }
    ft.initializeFromLevelList(expressionList);
    return ft;
  }

  /**
   * Sets the level.
   *
   * @param level  the level.
   */
  public void setLevel(final int level)
  {
    this.level = level;
  }

  /**
   * Gets the current level.
   *
   * @return the current level.
   */
  public int getLevel()
  {
    return level;
  }

  /**
   * Returns an iterator that provides access to the levels in descending order.
   *
   * @return the iterator.
   */
  public Iterator getLevelsDescending()
  {
    Integer[] levelIntegers = new Integer[levels.length];
    for (int i = 0; i < levels.length; i++)
    {
      levelIntegers[i] = new Integer(levels[i]);
    }
    return Collections.unmodifiableList(Arrays.asList(levelIntegers)).iterator();
  }

  /**
   * Returns an iterator that provides access to the levels in ascending order.
   *
   * @return the iterator.
   */
  public Iterator getLevelsAscending()
  {
    Integer[] levelIntegers = new Integer[levels.length];
    for (int i = 0; i < levels.length; i++)
    {
      levelIntegers[levels.length - i - 1] = new Integer(levels[i]);
    }
    return Collections.unmodifiableList(Arrays.asList(levelIntegers)).iterator();
  }

  /**
   * Returns the values of an expression.
   *
   * @param index  the function/expression index.
   *
   * @return the value.
   */
  public Object getValue(final int index)
  {
    return flatData[index].getValue();
  }

  /**
   * Returns an expression.
   *
   * @param index  the function/expression index.
   *
   * @return the function/expression.
   */
  public Expression getExpression(final int index)
  {
    return flatData[index];
  }

  /**
   * Returns the list of errors, that occured during the last event handling.
   *
   * @return the list of errors.
   */
  public List getErrors()
  {
    return Collections.unmodifiableList(errorList);
  }

  /**
   * Returns true, if this list has detected at least one error in the last operation.
   *
   * @return true, if there were errors, false otherwise.
   */
  public boolean hasErrors()
  {
    return errorList.size() != 0;
  }

  /**
   * Adds the error to the current list of errors.
   *
   * @param e the new exception that occured during the event dispatching.
   */
  protected void addError(final Exception e)
  {
    errorList.add(e);
  }

  /**
   * Clears the error list.
   */
  protected void clearError()
  {
    errorList.clear();
  }

  /**
   * Fires a prepare event.
   *
   * @param event  the event.
   */
  public void firePrepareEvent(final ReportEvent event)
  {
    clearError();

    for (int i = 0; i < levels.length; i++)
    {
      final int level = levels[i];
      if (level < getLevel())
      {
        break;
      }
      final Object[] itLevel = levelData[i];
      for (int l = 0; l < itLevel.length; l++)
      {
        final Expression e = (Expression) itLevel[l];
        if (e instanceof Function && e instanceof PrepareEventListener)
        {
          final PrepareEventListener f = (PrepareEventListener) e;
          try
          {
            f.prepareEvent(event);
          }
          catch (Exception ex)
          {
            addError(ex);
          }
        }
      }
    }
  }

  /**
   * Fires a prepare event layout listeners.
   *
   * @param event  the event.
   */
  protected void firePrepareEventLayoutListener(final ReportEvent event)
  {
    // no clear error here ...
    for (int i = 0; i < levels.length; i++)
    {
      final int level = levels[i];
      if (level < getLevel())
      {
        break;
      }
      final Object[] itLevel = levelData[i];
      for (int l = 0; l < itLevel.length; l++)
      {
        final Expression e = (Expression) itLevel[l];
        if (e instanceof Function && e instanceof PrepareEventListener
            && e instanceof LayoutListener)
        {
          final PrepareEventListener f = (PrepareEventListener) e;
          try
          {
            f.prepareEvent(event);
          }
          catch (Exception ex)
          {
            addError(ex);
          }
        }
      }
    }
  }
}
