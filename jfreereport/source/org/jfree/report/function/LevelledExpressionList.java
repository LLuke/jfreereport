/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * --------------------------
 * LevelledExpressionList.java
 * --------------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: LevelledExpressionList.java,v 1.18 2003/06/29 16:59:25 taqua Exp $
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
public class LevelledExpressionList implements ReportListener,
    Cloneable, LayoutListener, PageEventListener
{
  /** A list of expressions and associated levels. */
  private LevelList expressionList;

  /** error list stores the errors that occur during the event dispatching. */
  private ArrayList errorList;

  /** The level. */
  private int level;

  /** The levels. */
  private int[] levels;

  /**
   * Creates a new list.
   *
   * @param ec  the expressions.
   * @param fc  the functions.
   */
  public LevelledExpressionList(final ExpressionCollection ec, final ExpressionCollection fc)
  {
    expressionList = new LevelList();
    errorList = new ArrayList();
    initializeExpressions(ec);
    initializeFunctions(fc);

    // copy all levels from the collections to the cache ...
    // we assume, that the collections do not change anymore!
    final ArrayList al = new ArrayList();
    final Iterator it = expressionList.getLevelsDescending();
    while (it.hasNext())
    {
      final Integer level = (Integer) it.next();
      al.add(level);
    }
    levels = new int[al.size()];
    for (int i = 0; i < levels.length; i++)
    {
      final Integer level = (Integer) al.get(i);
      levels[i] = level.intValue();
    }
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
      final Iterator itLevel = expressionList.getElementsForLevel(level);
      while (itLevel.hasNext())
      {
        final Expression e = (Expression) itLevel.next();
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
      final Iterator itLevel = expressionList.getElementsForLevel(level);
      while (itLevel.hasNext())
      {
        Object next = itLevel.next();
        final Expression e = (Expression) next;
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
      final Iterator itLevel = expressionList.getElementsForLevel(level);
      while (itLevel.hasNext())
      {
        final Expression e = (Expression) itLevel.next();
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
      final Iterator itLevel = expressionList.getElementsForLevel(level);
      while (itLevel.hasNext())
      {
        final Expression e = (Expression) itLevel.next();
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
      final Iterator itLevel = expressionList.getElementsForLevel(level);
      while (itLevel.hasNext())
      {
        final Expression e = (Expression) itLevel.next();
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
      final Iterator itLevel = expressionList.getElementsForLevel(level);
      while (itLevel.hasNext())
      {
        final Expression e = (Expression) itLevel.next();
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
      final Iterator itLevel = expressionList.getElementsForLevel(level);
      while (itLevel.hasNext())
      {
        final Expression e = (Expression) itLevel.next();
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
      final Iterator itLevel = expressionList.getElementsForLevel(level);
      while (itLevel.hasNext())
      {
        final Expression e = (Expression) itLevel.next();
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
      final Iterator itLevel = expressionList.getElementsForLevel(level);
      while (itLevel.hasNext())
      {
        final Expression e = (Expression) itLevel.next();
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
      final Iterator itLevel = expressionList.getElementsForLevel(level);
      while (itLevel.hasNext())
      {
        final Expression e = (Expression) itLevel.next();
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
      final Iterator itLevel = expressionList.getElementsForLevel(level);
      while (itLevel.hasNext())
      {
        final Expression e = (Expression) itLevel.next();
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
      final Iterator itLevel = expressionList.getElementsForLevel(level);
      while (itLevel.hasNext())
      {
        final Expression e = (Expression) itLevel.next();
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
      final Iterator itLevel = expressionList.getElementsForLevel(level);
      while (itLevel.hasNext())
      {
        final Expression e = (Expression) itLevel.next();
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
  public void connectDataRow(final DataRow dr)
  {
    if (dr == null)
    {
      throw new NullPointerException();
    }
    for (int i = 0; i < expressionList.size(); i++)
    {
      final Expression f = (Expression) expressionList.get(i);
      f.setDataRow(dr);
    }
  }

  /**
   * Disconnects the datarow from the expression.
   *
   * @param dr  the datarow to be connected.
   *
   * @throws NullPointerException if the given datarow is null.
   */
  public void disconnectDataRow(final DataRow dr)
  {
    if (dr == null)
    {
      throw new NullPointerException("Null-DataRowBackend cannot be disconnected.");
    }
    for (int i = 0; i < expressionList.size(); i++)
    {
      final Expression f = (Expression) expressionList.get(i);
      f.setDataRow(null);
    }
  }

  /**
   * Initialises the expressions.
   *
   * @param expressionCollection  the expression collection.
   */
  private void initializeExpressions(final ExpressionCollection expressionCollection)
  {
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
  }

  /**
   * Initialises the functions.
   *
   * @param functionCollection  the function collection.
   */
  private void initializeFunctions(final ExpressionCollection functionCollection)
  {
    final int size = functionCollection.size();
    for (int i = 0; i < size; i++)
    {
      // Explicit cast to Function to test all contained elements to be Functions!
      Function f = (Function) functionCollection.getExpression(i);
      if (f != null)
      {
        f = (Function) f.getInstance();
        expressionList.add(f);
        expressionList.setLevel(f, f.getDependencyLevel());
      }
    }
  }

  /**
   * Size does not change, so it is cached.
   *
   * @return the size.
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
    final LevelledExpressionList ft = (LevelledExpressionList) super.clone();
    ft.expressionList = new LevelList(); // dont clone, too expensive ...
    ft.levels = levels;
    ft.errorList = (ArrayList) errorList.clone();

    final int size = expressionList.size();
    for (int i = 0; i < size; i++)
    {
      final Expression ex = (Expression) expressionList.get(i);
      if (ex instanceof Function)
      {
        final Expression exClone = (Expression) ex.clone();
        ft.expressionList.add(exClone, expressionList.getLevel(i));
      }
      else
      {
        ft.expressionList.add(ex, expressionList.getLevel(i));
      }
    }
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
    return expressionList.getLevelsDescending();
  }

  /**
   * Returns an iterator that provides access to the levels in ascending order.
   *
   * @return the iterator.
   */
  public Iterator getLevelsAscending()
  {
    return expressionList.getLevelsAscending();
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
    return ((Expression) expressionList.get(index)).getValue();
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
    return ((Expression) expressionList.get(index));
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
      final Iterator itLevel = expressionList.getElementsForLevel(level);
      while (itLevel.hasNext())
      {
        final Expression e = (Expression) itLevel.next();
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
      final Iterator itLevel = expressionList.getElementsForLevel(level);
      while (itLevel.hasNext())
      {
        final Expression e = (Expression) itLevel.next();
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
