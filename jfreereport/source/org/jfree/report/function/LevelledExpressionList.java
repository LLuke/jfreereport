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
 * $Id: LevelledExpressionList.java,v 1.16 2005/01/30 23:37:19 taqua Exp $
 *
 * Changes
 * -------
 * 05-Dec-2002 : Updates Javadocs (DG);
 * 06-Jul-2003 : Only PageEventListener will receive page events.
 */

package org.jfree.report.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jfree.report.DataRow;
import org.jfree.report.event.LayoutEvent;
import org.jfree.report.event.LayoutListener;
import org.jfree.report.event.PageEventListener;
import org.jfree.report.event.PrepareEventListener;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.event.ReportListener;
import org.jfree.report.util.IntList;
import org.jfree.report.util.LevelList;

/**
 * A list of expressions/functions and associated levels.  This class listens for report
 * events, then passes these events on to the expressions and functions in *descending*
 * level order.
 *
 * @author Thomas Morgner
 */
public final class LevelledExpressionList implements ReportListener,
                                                     Cloneable, LayoutListener,
                                                     PageEventListener
{
  private static class LevelStorage
  {
    private int levelNumber;
    private int[] activeExpressions;
    private int[] functions;
    private int[] pageEventListeners;
    private int[] prepareEventListeners;
    private int[] layoutListeners;
    private int[] prepareEventLayoutListeners;
    private int[] expressions;

    public LevelStorage (final int levelNumber,
                         final int[] expressions,
                         final int[] activeExpressions,
                         final int[] functions,
                         final int[] pageEventListeners,
                         final int[] prepareEventListeners,
                         final int[] prepareEventLayoutListeners,
                         final int[] layoutListeners)
    {
      this.levelNumber = levelNumber;
      this.activeExpressions = activeExpressions;
      this.functions = functions;
      this.pageEventListeners = pageEventListeners;
      this.prepareEventListeners = prepareEventListeners;
      this.layoutListeners = layoutListeners;
      this.expressions = expressions;
      this.prepareEventLayoutListeners = prepareEventLayoutListeners;
    }
  }

  /**
   * error list stores the errors that occur during the event dispatching.
   */
  private ArrayList errorList;

  /**
   * The current processing level.
   */
  private int level;

  /**
   * The dataRow for all functions.
   */
  private DataRow dataRow;

  /**
   * The expressions sorted by levels.
   */
  private LevelStorage[] levelData;

  /**
   * all data as flat list.
   */
  private Expression[] flatData;

  /**
   * The number of functions and expressions in this list.
   */
  private int size;

  protected LevelledExpressionList ()
  {
    errorList = new ArrayList();
  }

  /**
   * Creates a new list.
   *
   * @param ec the expressions from the report definition.
   */
  public LevelledExpressionList (final ExpressionCollection ec)
  {
    errorList = new ArrayList();
    initialize(ec);
  }

  /**
   * Receives notification that report generation has started. <P> The event carries a
   * ReportState.Started state. Use this to prepare the report header.
   *
   * @param event the event.
   */
  public void reportStarted (final ReportEvent event)
  {
    for (int levelIdx = 0; levelIdx < levelData.length; levelIdx++)
    {
      final int level = levelData[levelIdx].levelNumber;
      if (level < getLevel())
      {
        break;
      }
      final int[] functions = levelData[levelIdx].functions;
      for (int l = 0; l < functions.length; l++)
      {
        final Function e = (Function) flatData[functions[l]];
        try
        {
          e.reportStarted(event);
        }
        catch (Exception ex)
        {
          addError(ex);
        }
      }

      final int[] activeExpressions = levelData[levelIdx].activeExpressions;
      for (int l = 0; l < activeExpressions.length; l++)
      {
        final Expression e = flatData[activeExpressions[l]];
        try
        {
          e.getValue();
        }
        catch (Exception ex)
        {
          addError(ex);
        }
      }
    }
  }

  /**
   * Receives notification that report generation has started. <P> The event carries a
   * ReportState.Started state.  Use this to initialize the report.
   *
   * @param event the event.
   */
  public void reportInitialized (final ReportEvent event)
  {
    clearError(); // has no prepare event ...
    for (int levelIndex = 0; levelIndex < levelData.length; levelIndex++)
    {
      final int level = levelData[levelIndex].levelNumber;
      if (level < getLevel())
      {
        break;
      }
      final int[] functions = levelData[levelIndex].functions;
      for (int expressionIdx = 0; expressionIdx < functions.length; expressionIdx++)
      {
        final Function e = (Function) flatData[functions[expressionIdx]];
        try
        {
          e.reportInitialized(event);
        }
        catch (Exception ex)
        {
          addError(ex);
        }
      }

      final int[] activeExpressions = levelData[levelIndex].activeExpressions;
      for (int exprIdx = 0; exprIdx < activeExpressions.length; exprIdx++)
      {
        final Expression e = flatData[activeExpressions[exprIdx]];
        try
        {
          e.getValue();
        }
        catch (Exception ex)
        {
          addError(ex);
        }
      }
    }
  }

  /**
   * Receives notification that report generation has finished (the last record is read
   * and all groups are closed).
   *
   * @param event the event.
   */
  public void reportFinished (final ReportEvent event)
  {
    for (int levelIdx = 0; levelIdx < levelData.length; levelIdx++)
    {
      final int level = levelData[levelIdx].levelNumber;
      if (level < getLevel())
      {
        break;
      }
      final int[] functions = levelData[levelIdx].functions;
      for (int l = 0; l < functions.length; l++)
      {
        final Function e = (Function) flatData[functions[l]];
        try
        {
          e.reportFinished(event);
        }
        catch (Exception ex)
        {
          addError(ex);
        }
      }

      final int[] activeExpressions = levelData[levelIdx].activeExpressions;
      for (int l = 0; l < activeExpressions.length; l++)
      {
        final Expression e = flatData[activeExpressions[l]];
        try
        {
          e.getValue();
        }
        catch (Exception ex)
        {
          addError(ex);
        }
      }
    }
  }

  /**
   * Receives notification that a new page is being started.
   *
   * @param event the event.
   */
  public void pageStarted (final ReportEvent event)
  {
    for (int levelIdx = 0; levelIdx < levelData.length; levelIdx++)
    {
      final int level = levelData[levelIdx].levelNumber;
      if (level < getLevel())
      {
        break;
      }
      final int[] functions = levelData[levelIdx].pageEventListeners;
      for (int l = 0; l < functions.length; l++)
      {
        final PageEventListener e = (PageEventListener) flatData[functions[l]];
        try
        {
          e.pageStarted(event);
        }
        catch (Exception ex)
        {
          addError(ex);
        }
      }

      final int[] activeExpressions = levelData[levelIdx].activeExpressions;
      for (int l = 0; l < activeExpressions.length; l++)
      {
        final Expression e = flatData[activeExpressions[l]];
        try
        {
          e.getValue();
        }
        catch (Exception ex)
        {
          addError(ex);
        }
      }
    }
  }

  /**
   * Receives notification that a new page is being started.
   *
   * @param event the event.
   */
  public void pageCanceled (final ReportEvent event)
  {
    for (int levelIdx = 0; levelIdx < levelData.length; levelIdx++)
    {
      final int level = levelData[levelIdx].levelNumber;
      if (level < getLevel())
      {
        break;
      }
      final int[] functions = levelData[levelIdx].pageEventListeners;
      for (int l = 0; l < functions.length; l++)
      {
        final PageEventListener e = (PageEventListener) flatData[functions[l]];
        try
        {
          e.pageCanceled(event);
        }
        catch (Exception ex)
        {
          addError(ex);
        }
      }
    }
  }

  /**
   * Receives notification that a new page is being started.
   *
   * @param event the event.
   */
  public void pageRolledBack (final ReportEvent event)
  {
    for (int levelIdx = 0; levelIdx < levelData.length; levelIdx++)
    {
      final int level = levelData[levelIdx].levelNumber;
      if (level < getLevel())
      {
        break;
      }
      final int[] functions = levelData[levelIdx].pageEventListeners;
      for (int l = 0; l < functions.length; l++)
      {
        final PageEventListener e = (PageEventListener) flatData[functions[l]];
        try
        {
          e.pageRolledBack(event);
        }
        catch (Exception ex)
        {
          addError(ex);
        }
      }
    }
  }


  /**
   * Receives notification that a page is completed.
   *
   * @param event the event.
   */
  public void pageFinished (final ReportEvent event)
  {
    for (int levelIdx = 0; levelIdx < levelData.length; levelIdx++)
    {
      final int level = levelData[levelIdx].levelNumber;
      if (level < getLevel())
      {
        break;
      }
      final int[] functions = levelData[levelIdx].pageEventListeners;
      for (int l = 0; l < functions.length; l++)
      {
        final PageEventListener e = (PageEventListener) flatData[functions[l]];
        try
        {
          e.pageFinished(event);
        }
        catch (Exception ex)
        {
          addError(ex);
        }
      }

      final int[] activeExpressions = levelData[levelIdx].activeExpressions;
      for (int l = 0; l < activeExpressions.length; l++)
      {
        final Expression e = flatData[activeExpressions[l]];
        try
        {
          e.getValue();
        }
        catch (Exception ex)
        {
          addError(ex);
        }
      }
    }
  }

  /**
   * Receives notification that a new group has started. <P> The group can be determined
   * by the report state's getCurrentGroup() function.
   *
   * @param event the event.
   */
  public void groupStarted (final ReportEvent event)
  {
    for (int levelIdx = 0; levelIdx < levelData.length; levelIdx++)
    {
      final int level = levelData[levelIdx].levelNumber;
      if (level < getLevel())
      {
        break;
      }
      final int[] functions = levelData[levelIdx].functions;
      for (int l = 0; l < functions.length; l++)
      {
        final Function e = (Function) flatData[functions[l]];
        try
        {
          e.groupStarted(event);
        }
        catch (Exception ex)
        {
          addError(ex);
        }
      }

      final int[] activeExpressions = levelData[levelIdx].activeExpressions;
      for (int l = 0; l < activeExpressions.length; l++)
      {
        final Expression e = flatData[activeExpressions[l]];
        try
        {
          e.getValue();
        }
        catch (Exception ex)
        {
          addError(ex);
        }
      }
    }

  }

  /**
   * Receives notification that a group is finished. <P> The group can be determined by
   * the report state's getCurrentGroup() function.
   *
   * @param event the event.
   */
  public void groupFinished (final ReportEvent event)
  {
    for (int levelIdx = 0; levelIdx < levelData.length; levelIdx++)
    {
      final int level = levelData[levelIdx].levelNumber;
      if (level < getLevel())
      {
        break;
      }
      final int[] functions = levelData[levelIdx].functions;
      for (int l = 0; l < functions.length; l++)
      {
        final Function e = (Function) flatData[functions[l]];
        try
        {
          e.groupFinished(event);
        }
        catch (Exception ex)
        {
          addError(ex);
        }
      }

      final int[] activeExpressions = levelData[levelIdx].activeExpressions;
      for (int l = 0; l < activeExpressions.length; l++)
      {
        final Expression e = flatData[activeExpressions[l]];
        try
        {
          e.getValue();
        }
        catch (Exception ex)
        {
          addError(ex);
        }
      }
    }
  }

  /**
   * Receives notification that a group of item bands is about to be processed. <P> The
   * next events will be itemsAdvanced events until the itemsFinished event is raised.
   *
   * @param event the event.
   */
  public void itemsStarted (final ReportEvent event)
  {
    for (int levelIdx = 0; levelIdx < levelData.length; levelIdx++)
    {
      final int level = levelData[levelIdx].levelNumber;
      if (level < getLevel())
      {
        break;
      }
      final int[] functions = levelData[levelIdx].functions;
      for (int l = 0; l < functions.length; l++)
      {
        final Function e = (Function) flatData[functions[l]];
        try
        {
          e.itemsStarted(event);
        }
        catch (Exception ex)
        {
          addError(ex);
        }
      }

      final int[] activeExpressions = levelData[levelIdx].activeExpressions;
      for (int l = 0; l < activeExpressions.length; l++)
      {
        final Expression e = flatData[activeExpressions[l]];
        try
        {
          e.getValue();
        }
        catch (Exception ex)
        {
          addError(ex);
        }
      }
    }
  }

  /**
   * Receives notification that a group of item bands has been completed. <P> The itemBand
   * is finished, the report starts to close open groups.
   *
   * @param event the event.
   */
  public void itemsFinished (final ReportEvent event)
  {
    for (int levelIdx = 0; levelIdx < levelData.length; levelIdx++)
    {
      final int level = levelData[levelIdx].levelNumber;
      if (level < getLevel())
      {
        break;
      }
      final int[] functions = levelData[levelIdx].functions;
      for (int l = 0; l < functions.length; l++)
      {
        final Function e = (Function) flatData[functions[l]];
        try
        {
          e.itemsFinished(event);
        }
        catch (Exception ex)
        {
          addError(ex);
        }
      }

      final int[] activeExpressions = levelData[levelIdx].activeExpressions;
      for (int l = 0; l < activeExpressions.length; l++)
      {
        final Expression e = flatData[activeExpressions[l]];
        try
        {
          e.getValue();
        }
        catch (Exception ex)
        {
          addError(ex);
        }
      }
    }
  }

  /**
   * Receives notification that a new row has been read. <P> This event is raised before
   * an ItemBand is printed.
   *
   * @param event the event.
   */
  public void itemsAdvanced (final ReportEvent event)
  {
    for (int levelIdx = 0; levelIdx < levelData.length; levelIdx++)
    {
      final int level = levelData[levelIdx].levelNumber;
      if (level < getLevel())
      {
        break;
      }
      final int[] functions = levelData[levelIdx].functions;
      for (int l = 0; l < functions.length; l++)
      {
        final Function e = (Function) flatData[functions[l]];
        try
        {
          e.itemsAdvanced(event);
        }
        catch (Exception ex)
        {
          addError(ex);
        }
      }

      final int[] activeExpressions = levelData[levelIdx].activeExpressions;
      for (int l = 0; l < activeExpressions.length; l++)
      {
        final Expression e = flatData[activeExpressions[l]];
        try
        {
          e.getValue();
        }
        catch (Exception ex)
        {
          addError(ex);
        }
      }
    }
  }

  /**
   * Receives notification that the band layouting has completed. <P> The event carries
   * the current report state.
   *
   * @param event The event.
   */
  public void layoutComplete (final LayoutEvent event)
  {
    firePrepareEventLayoutListener(event);

    for (int levelIdx = 0; levelIdx < levelData.length; levelIdx++)
    {
      final int level = levelData[levelIdx].levelNumber;
      if (level < getLevel())
      {
        break;
      }
      final int[] functions = levelData[levelIdx].layoutListeners;
      for (int l = 0; l < functions.length; l++)
      {
        final LayoutListener e = (LayoutListener) flatData[functions[l]];
        try
        {
          e.layoutComplete(event);
        }
        catch (Exception ex)
        {
          addError(ex);
        }
      }

      final int[] activeExpressions = levelData[levelIdx].activeExpressions;
      for (int l = 0; l < activeExpressions.length; l++)
      {
        final Expression e = flatData[activeExpressions[l]];
        try
        {
          e.getValue();
        }
        catch (Exception ex)
        {
          addError(ex);
        }
      }
    }
  }

  /**
   * Receives notification that the band output has completed. <P> The event carries the
   * current report state.
   *
   * @param event The event.
   */
  public void outputComplete (final LayoutEvent event)
  {
    for (int levelIdx = 0; levelIdx < levelData.length; levelIdx++)
    {
      final int level = levelData[levelIdx].levelNumber;
      if (level < getLevel())
      {
        break;
      }
      final int[] functions = levelData[levelIdx].layoutListeners;
      for (int l = 0; l < functions.length; l++)
      {
        final LayoutListener e = (LayoutListener) flatData[functions[l]];
        try
        {
          e.layoutComplete(event);
        }
        catch (Exception ex)
        {
          addError(ex);
        }
      }
    }
  }

  /**
   * Receives notification that report generation has completed, the report footer was
   * printed, no more output is done. This is a helper event to shut down the output
   * service.
   *
   * @param event The event.
   */
  public void reportDone (final ReportEvent event)
  {
    for (int levelIdx = 0; levelIdx < levelData.length; levelIdx++)
    {
      final int level = levelData[levelIdx].levelNumber;
      if (level < getLevel())
      {
        break;
      }
      final int[] functions = levelData[levelIdx].functions;
      for (int l = 0; l < functions.length; l++)
      {
        final Function e = (Function) flatData[functions[l]];
        try
        {
          e.reportDone(event);
        }
        catch (Exception ex)
        {
          addError(ex);
        }
      }

      final int[] activeExpressions = levelData[levelIdx].activeExpressions;
      for (int l = 0; l < activeExpressions.length; l++)
      {
        final Expression e = flatData[activeExpressions[l]];
        try
        {
          e.getValue();
        }
        catch (Exception ex)
        {
          addError(ex);
        }
      }
    }
  }

  /**
   * Connects the given datarow to the expression collection and all expressions contained
   * in this collection.
   *
   * @param dr the datarow to be connected (null not permitted).
   * @throws IllegalStateException if there is a datarow already connected.
   * @throws NullPointerException  if the given datarow is null.
   */
  public void setDataRow (final DataRow dr)
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
   * Updates the datarow for all expressions. Does not perform validity checks, so use
   * this function with care.
   *
   * @param dr the datarow to be connected.
   * @throws NullPointerException  if the given datarow is null.
   * @throws IllegalStateException if there is no datarow connected.
   */
  public void updateDataRow (final DataRow dr)
  {
    dataRow = dr;
    for (int i = 0; i < flatData.length; i++)
    {
      final Expression f = flatData[i];
      f.setDataRow(dr);
    }
  }

  /**
   * Returns the currently connected dataRow.
   *
   * @return the dataRow.
   */
  public DataRow getDataRow ()
  {
    return dataRow;
  }

  /**
   * Initialises the expressions.
   *
   * @param expressionCollection the expression collection.
   */
  private void initialize (final ExpressionCollection expressionCollection)
  {
    final LevelList expressionList = new LevelList();

    final int size = expressionCollection.size();
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
    initializeFromLevelList(expressionList);
  }

  /**
   * Initializes the expression list from the given levellist.
   *
   * @param expressionList the list containing the expressions and functions.
   */
  private void initializeFromLevelList (final LevelList expressionList)
  {
    this.size = 0;
    final Integer[] levels = expressionList.getLevelsDescendingArray();
    this.levelData = new LevelStorage[levels.length];
    this.flatData = new Expression[expressionList.size()];

    final IntList expressions = new IntList(20);
    final IntList activeExpressions = new IntList(20);
    final IntList functions = new IntList(20);
    final IntList layoutListeners = new IntList(20);
    final IntList pageEventListeners = new IntList(20);
    final IntList prepareEventListeners = new IntList(20);
    final IntList prepareLayoutEventListeners = new IntList(20);

    for (int i = 0; i < levels.length; i++)
    {
      final int currentLevel = levels[i].intValue();
      final Expression[] data = (Expression[])
              expressionList.getElementArrayForLevel(currentLevel,
                      new Expression[expressionList.getElementCountForLevel(currentLevel)]);
      System.arraycopy(data, 0, this.flatData, this.size, data.length);
      for (int x = 0; x < data.length; x++)
      {
        final Expression ex = data[x];
        final int globalPosition = this.size + x;

        expressions.add(globalPosition);
        if (ex.isActive())
        {
          activeExpressions.add(globalPosition);
        }
        if (ex instanceof Function == false)
        {
          continue;
        }
        functions.add(globalPosition);
        if (ex instanceof PageEventListener)
        {
          pageEventListeners.add(globalPosition);
        }
        if (ex instanceof LayoutListener)
        {
          layoutListeners.add(globalPosition);
          if (ex instanceof PrepareEventListener)
          {
            prepareEventListeners.add(globalPosition);
            prepareLayoutEventListeners.add(globalPosition);
          }
        }
        else if (ex instanceof PrepareEventListener)
        {
          prepareEventListeners.add(globalPosition);
        }
      }

      final LevelStorage storage = new LevelStorage(currentLevel,
              expressions.toArray(), activeExpressions.toArray(),
              functions.toArray(), pageEventListeners.toArray(),
              prepareEventListeners.toArray(), prepareLayoutEventListeners.toArray(),
              layoutListeners.toArray());
      levelData[i] = storage;

      expressions.clear();
      activeExpressions.clear();
      functions.clear();
      pageEventListeners.clear();
      prepareEventListeners.clear();
      prepareLayoutEventListeners.clear();
      layoutListeners.clear();
      this.size += data.length;
    }
  }

  /**
   * Size does not change, so it is cached.
   *
   * @return the size.
   */
  public int size ()
  {
    return size;
  }

  /**
   * Creates and returns a copy of this object.  The precise meaning of "copy" may depend
   * on the class of the object.
   * <p/>
   * The cloned LevelledExpressionList will no longer be connected to a datarow.
   *
   * @return a clone of this instance.
   *
   * @throws CloneNotSupportedException if the object's class does not support the
   *                                    <code>Cloneable</code> interface.
   * @throws OutOfMemoryError           if there is not enough memory.
   * @see java.lang.Cloneable
   */
  public Object clone ()
          throws CloneNotSupportedException
  {
    final LevelledExpressionList ft = (LevelledExpressionList) super.clone();
    // ft.levels = levels; // no need to clone them ...
    // ft.size = size;     // already copied during cloning ...
    ft.dataRow = null;
    ft.errorList = (ArrayList) errorList.clone();
    ft.flatData = (Expression[]) ft.flatData.clone();

    for (int expression = 0; expression < flatData.length; expression++)
    {
      ft.flatData[expression] = (Expression) flatData[expression].clone();
      ft.flatData[expression].setDataRow(null);
    }
    return ft;
  }

  /**
   * Returns the preview instance of the levelled expression list. This list does no
   * longer contain any Function instances.
   *
   * @return the preview (expressions only) instance of the levelled expression list.
   */
  public LevelledExpressionList getPreviewInstance ()
  {
    final LevelList expressionList = new LevelList();

    for (int level = 0; level < levelData.length; level++)
    {
      final LevelStorage levelStorage = levelData[level];
      for (int i = 0; i < levelStorage.expressions.length; i++)
      {
        final int idx = levelStorage.expressions[i];
        final Expression ex = flatData[idx];
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
    final LevelledExpressionList ft = new LevelledExpressionList();
    ft.initializeFromLevelList(expressionList);
    return ft;
  }

  /**
   * Sets the level.
   *
   * @param level the level.
   */
  public void setLevel (final int level)
  {
    this.level = level;
  }

  /**
   * Gets the current level.
   *
   * @return the current level.
   */
  public int getLevel ()
  {
    return level;
  }

  /**
   * Returns an iterator that provides access to the levels in descending order.
   *
   * @return the iterator.
   */
  public Iterator getLevelsDescending ()
  {
    final Integer[] levelIntegers = new Integer[levelData.length];
    for (int i = 0; i < levelData.length; i++)
    {
      final LevelStorage levelStorage = levelData[i];
      levelIntegers[i] = new Integer(levelStorage.levelNumber);
    }
    return Collections.unmodifiableList(Arrays.asList(levelIntegers)).iterator();
  }

  /**
   * Returns an iterator that provides access to the levels in ascending order.
   *
   * @return the iterator.
   */
  public Iterator getLevelsAscending ()
  {
    final Integer[] levelIntegers = new Integer[levelData.length];
    for (int i = 0; i < levelData.length; i++)
    {
      levelIntegers[levelData.length - i - 1] = new Integer(levelData[i].levelNumber);
    }
    return Collections.unmodifiableList(Arrays.asList(levelIntegers)).iterator();
  }

  /**
   * Returns the values of an expression.
   *
   * @param index the function/expression index.
   * @return the value.
   */
  public Object getValue (final int index)
  {
    return flatData[index].getValue();
  }

  /**
   * Returns an expression.
   *
   * @param index the function/expression index.
   * @return the function/expression.
   */
  public Expression getExpression (final int index)
  {
    return flatData[index];
  }

  /**
   * Returns the list of errors, that occured during the last event handling.
   *
   * @return the list of errors.
   */
  public List getErrors ()
  {
    return Collections.unmodifiableList(errorList);
  }

  /**
   * Returns true, if this list has detected at least one error in the last operation.
   *
   * @return true, if there were errors, false otherwise.
   */
  public boolean hasErrors ()
  {
    return errorList.size() != 0;
  }

  /**
   * Adds the error to the current list of errors.
   *
   * @param e the new exception that occured during the event dispatching.
   */
  protected void addError (final Exception e)
  {
    errorList.add(e);
  }

  /**
   * Clears the error list.
   */
  protected void clearError ()
  {
    errorList.clear();
  }

  /**
   * Fires a prepare event.
   *
   * @param event the event.
   */
  public void firePrepareEvent (final ReportEvent event)
  {
    clearError();
    for (int levelIdx = 0; levelIdx < levelData.length; levelIdx++)
    {
      final int level = levelData[levelIdx].levelNumber;
      if (level < getLevel())
      {
        break;
      }
      final int[] functions = levelData[levelIdx].prepareEventListeners;
      for (int l = 0; l < functions.length; l++)
      {
        final PrepareEventListener e = (PrepareEventListener) flatData[functions[l]];
        try
        {
          e.prepareEvent(event);
        }
        catch (Exception ex)
        {
          addError(ex);
        }
      }
    }

  }

  /**
   * Fires a prepare event layout listeners.
   *
   * @param event the event.
   */
  protected void firePrepareEventLayoutListener (final ReportEvent event)
  {
    for (int levelIdx = 0; levelIdx < levelData.length; levelIdx++)
    {
      final int level = levelData[levelIdx].levelNumber;
      if (level < getLevel())
      {
        break;
      }
      final int[] functions = levelData[levelIdx].prepareEventLayoutListeners;
      for (int l = 0; l < functions.length; l++)
      {
        final PrepareEventListener e = (PrepareEventListener) flatData[functions[l]];
        try
        {
          e.prepareEvent(event);
        }
        catch (Exception ex)
        {
          addError(ex);
        }
      }

      final int[] activeExpressions = levelData[levelIdx].activeExpressions;
      for (int l = 0; l < activeExpressions.length; l++)
      {
        final Expression e = flatData[activeExpressions[l]];
        try
        {
          e.getValue();
        }
        catch (Exception ex)
        {
          addError(ex);
        }
      }
    }
  }
}
