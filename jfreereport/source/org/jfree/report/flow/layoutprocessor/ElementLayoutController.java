/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * $Id: ElementLayoutController.java,v 1.6 2006/12/09 21:19:04 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.flow.layoutprocessor;

import org.jfree.report.DataSourceException;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.expressions.Expression;
import org.jfree.report.data.GlobalMasterRow;
import org.jfree.report.data.PrecomputeNode;
import org.jfree.report.data.PrecomputeNodeKey;
import org.jfree.report.data.PrecomputedValueRegistry;
import org.jfree.report.data.StaticExpressionRuntimeData;
import org.jfree.report.data.ExpressionSlot;
import org.jfree.report.data.PrecomputedExpressionSlot;
import org.jfree.report.data.RunningExpressionSlot;
import org.jfree.report.flow.EmptyReportTarget;
import org.jfree.report.flow.FlowController;
import org.jfree.report.flow.ReportJob;
import org.jfree.report.flow.ReportTarget;
import org.jfree.report.flow.FlowControlOperation;
import org.jfree.report.structure.Element;
import org.jfree.util.Log;

/**
 * Creation-Date: 24.11.2006, 13:56:30
 *
 * @author Thomas Morgner
 */
public abstract class ElementLayoutController
    implements LayoutController, Cloneable
{
  protected static class ElementPrecomputeKey implements PrecomputeNodeKey
  {
    private String name;
    private String id;
    private String namespace;
    private String tagName;

    public ElementPrecomputeKey(Element element)
    {
      this.name = element.getName();
      this.tagName = element.getType();
      this.namespace = element.getNamespace();
      this.id = element.getId();
    }

    public boolean equals(final Object o)
    {
      if (this == o)
      {
        return true;
      }
      if (o == null || getClass() != o.getClass())
      {
        return false;
      }

      final ElementPrecomputeKey that = (ElementPrecomputeKey) o;

      if (id != null ? !id.equals(that.id) : that.id != null)
      {
        return false;
      }
      if (name != null ? !name.equals(that.name) : that.name != null)
      {
        return false;
      }
      if (namespace != null ? !namespace.equals(that.namespace) : that.namespace != null)
      {
        return false;
      }
      if (tagName != null ? !tagName.equals(that.tagName) : that.tagName != null)
      {
        return false;
      }

      return true;
    }

    public int hashCode()
    {
      int result;
      result = (name != null ? name.hashCode() : 0);
      result = 29 * result + (id != null ? id.hashCode() : 0);
      result = 29 * result + (namespace != null ? namespace.hashCode() : 0);
      result = 29 * result + (tagName != null ? tagName.hashCode() : 0);
      return result;
    }

    public boolean equals(PrecomputeNodeKey otherKey)
    {
      return false;
    }
  }

  public static final int NOT_STARTED = 0;
  public static final int OPENED = 1;
  public static final int WAITING_FOR_JOIN = 2;
  public static final int FINISHING = 3;
  public static final int JOINING = 4;
  public static final int FINISHED = 5;

  private int processingState;
  private FlowController flowController;
  private Element element;
  private LayoutController parent;
  private boolean precomputing;

  protected ElementLayoutController()
  {
    this.processingState = NOT_STARTED;
  }

  public LayoutController getParent()
  {
    return parent;
  }

  /**
   * Calling initialize after the first advance must result in a
   * IllegalStateException.
   *
   * @param flowController
   * @param initialNode
   * @throws org.jfree.report.DataSourceException
   *
   * @throws org.jfree.report.ReportDataFactoryException
   *
   * @throws org.jfree.report.ReportProcessingException
   *
   */
  public void initialize(final Object node,
                         final FlowController flowController,
                         final LayoutController parent)
      throws DataSourceException, ReportDataFactoryException, ReportProcessingException
  {

    if (processingState != NOT_STARTED)
    {
      throw new IllegalStateException();
    }

    this.element = (Element) node;
    this.flowController = flowController;
    this.parent = parent;
  }

  public final LayoutController advance(ReportTarget target)
      throws DataSourceException, ReportProcessingException, ReportDataFactoryException
  {
    final int processingState = getProcessingState();
    switch (processingState)
    {
      case NOT_STARTED:
        return startElement(target);
      case OPENED:
        return processContent(target);
      case FINISHING:
        return finishElement(target);
      case JOINING:
        return joinWithParent();
      default:
        throw new IllegalStateException();
    }
  }

  protected abstract LayoutController startElement(final ReportTarget target)
      throws DataSourceException, ReportProcessingException, ReportDataFactoryException;
  protected abstract LayoutController processContent(final ReportTarget target)
      throws DataSourceException, ReportProcessingException, ReportDataFactoryException;
  protected abstract LayoutController finishElement(final ReportTarget target)
      throws ReportProcessingException, DataSourceException;

  protected LayoutController joinWithParent()
  {
    final LayoutController parent = getParent();
    if (parent == null)
    {
      // skip to the next step ..
      throw new IllegalStateException("There is no parent to join with. " +
              "This should not happen in a sane environment!");
    }

    return parent.join(getFlowController());
  }

  public boolean isAdvanceable()
  {
    return processingState != FINISHED;
  }

  public Element getElement()
  {
    return element;
  }

  public FlowController getFlowController()
  {
    return flowController;
  }

  public int getProcessingState()
  {
    return processingState;
  }

  public void setProcessingState(final int processingState)
  {
    this.processingState = processingState;
  }

  public void setFlowController(final FlowController flowController)
  {
    this.flowController = flowController;
  }

  public void setParent(final LayoutController parent)
  {
    this.parent = parent;
  }

  public Object clone ()
  {
    try
    {
      return super.clone();
    }
    catch (CloneNotSupportedException e)
    {
      Log.error("Clone not supported: " , e);
      throw new IllegalStateException("Clone must be supported.");
    }
  }

  protected StaticExpressionRuntimeData createRuntimeData(final FlowController fc)
  {
    final GlobalMasterRow dataRow = fc.getMasterRow();
    final ReportJob reportJob = fc.getReportJob();
    final StaticExpressionRuntimeData sdd = new StaticExpressionRuntimeData();
    sdd.setData(dataRow.getReportDataRow().getReportData());
    sdd.setDeclaringParent(getElement());
    sdd.setConfiguration(reportJob.getConfiguration());
    sdd.setReportContext(fc.getReportContext());
    return sdd;
  }

  public boolean isPrecomputing()
  {
    return precomputing;
  }

  protected void setPrecomputing(final boolean precomputing)
  {
    this.precomputing = precomputing;
  }


  protected Object precompute(final int expressionPosition)
      throws ReportProcessingException, ReportDataFactoryException, DataSourceException
  {
    final FlowController fc = getFlowController().createPrecomputeInstance();
    final PrecomputedValueRegistry pcvr =
        fc.getPrecomputedValueRegistry();

    final Element element = (Element) getElement();
    pcvr.startElementPrecomputation(new ElementPrecomputeKey(element));

    final ElementLayoutController layoutController =
        (ElementLayoutController) this.clone();
    layoutController.setPrecomputing(true);
    layoutController.setFlowController(fc);
    layoutController.setParent(null);

    final ReportTarget target =
        new EmptyReportTarget(fc.getReportJob(), fc.getExportDescriptor());

    LayoutController lc = layoutController;
    while (lc.isAdvanceable())
    {
      lc = lc.advance(target);
    }

    final PrecomputeNode precomputeNode = pcvr.currentNode();
    final Object functionResult = precomputeNode.getFunctionResult(expressionPosition);
    pcvr.finishElementPrecomputation(new ElementPrecomputeKey(element));
    return functionResult;
  }


  protected FlowController performElementPrecomputation(final Expression[] expressions,
                                                        FlowController fc)
      throws ReportProcessingException, ReportDataFactoryException, DataSourceException
  {
    final PrecomputedValueRegistry pcvr = fc.getPrecomputedValueRegistry();
    if (isPrecomputing() == false)
    {
      final Element e = (Element) getElement();
      pcvr.startElement(new ElementPrecomputeKey(e));
    }

    if (expressions.length > 0)
    {
      final ExpressionSlot[] slots = new ExpressionSlot[expressions.length];
      final StaticExpressionRuntimeData runtimeData = createRuntimeData(fc);

      for (int i = 0; i < expressions.length; i++)
      {
        final Expression expression = expressions[i];
        if (isPrecomputing() == false && expression.isPrecompute())
        {
          // ok, we have to precompute the expression's value. For that
          // we fork a new layout process, compute the value and then come
          // back with the result.
          Object value = precompute (i);
          slots[i] = new PrecomputedExpressionSlot(expression.getName(), value, expression.isPreserve());
        }
        else
        {
          // thats a bit easier; we dont have to do anything special ..
          slots[i] = new RunningExpressionSlot(expression, runtimeData, pcvr.currentNode());
        }
      }

      fc = fc.activateExpressions(slots);
    }
    return fc;
  }


  protected FlowController tryRepeatingCommit (final FlowController fc)
      throws DataSourceException
  {
    if (isPrecomputing() == false)
    {
      // ok, the user wanted us to repeat. So we repeat if the group in which
      // we are in, is not closed (and at least one advance has been fired
      // since the last repeat request [to prevent infinite loops]) ...
      final boolean advanceRequested = fc.isAdvanceRequested();
      final boolean advanceable = fc.getMasterRow().isAdvanceable();
      if (advanceable && advanceRequested)
      {
        // we check against the commited target; But we will not use the
        // commited target if the group is no longer active...
        final FlowController cfc =
            fc.performOperation(FlowControlOperation.COMMIT);
        final boolean groupFinished =
            LayoutControllerUtil.isGroupFinished(cfc, getElement());
        if (groupFinished == false)
        {
          return cfc;
        }
      }
    }
    return null;
  }
}
