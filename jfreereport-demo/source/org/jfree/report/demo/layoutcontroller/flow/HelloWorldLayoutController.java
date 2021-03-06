/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
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
 * HelloWorldLayoutController.java
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.report.demo.layoutcontroller.flow;

import org.jfree.layouting.util.AttributeMap;
import org.jfree.report.DataSourceException;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.demo.layoutcontroller.HelloWorldElement;
import org.jfree.report.flow.FlowController;
import org.jfree.report.flow.ReportTarget;
import org.jfree.report.flow.layoutprocessor.ElementLayoutController;
import org.jfree.report.flow.layoutprocessor.LayoutController;

/**
 * Creation-Date: Dec 10, 2006, 11:09:49 PM
 *
 * @author Thomas Morgner
 */
public class HelloWorldLayoutController extends ElementLayoutController
{
  private AttributeMap attributeMap;

  public HelloWorldLayoutController()
  {
  }

  protected LayoutController startElement(final ReportTarget target)
      throws DataSourceException, ReportProcessingException, ReportDataFactoryException
  {

    final HelloWorldElement element = (HelloWorldElement) getElement();
    // Create a copy of the attributes.
    // In this example, we do not evaluate any style- or attribute expressions
    // as we want to keep it simple ..
    final AttributeMap attributeMap = new AttributeMap(element.getAttributeMap());
    target.startElement(attributeMap);

    final HelloWorldLayoutController derived = (HelloWorldLayoutController) clone();
    derived.setProcessingState(ElementLayoutController.OPENED);
    derived.attributeMap = attributeMap;
    return derived;
  }

  protected LayoutController processContent(final ReportTarget target)
      throws DataSourceException, ReportProcessingException, ReportDataFactoryException
  {

    final HelloWorldElement element = (HelloWorldElement) getElement();
    target.processText("Hello World :) ");
    target.processText("And here comes your text: '");
    target.processText(element.getText());
    target.processText("'");

    final HelloWorldLayoutController derived = (HelloWorldLayoutController) clone();
    derived.setProcessingState(ElementLayoutController.FINISHING);
    return derived;
  }

  /**
   * Finishes the processing of this element. This method is called when the
   * processing state is 'FINISHING'. The element should be closed now and all
   * privatly owned resources should be freed. If the element has a parent, it
   * would be time to join up with the parent now, else the processing state
   * should be set to 'FINISHED'.
   *
   * @param target the report target that receives generated events.
   * @return the new layout controller instance representing the new state.
   *
   * @throws DataSourceException       if there was a problem reading data from
   *                                   the datasource.
   * @throws ReportProcessingException if there was a general problem during the
   *                                   report processing.
   * @throws ReportDataFactoryException if there was an error trying query data.
   */
  protected LayoutController finishElement(final ReportTarget target)
      throws ReportProcessingException, DataSourceException,
      ReportDataFactoryException
  {

    target.endElement(attributeMap);

    final LayoutController parent = getParent();
    if (parent != null)
    {
      final FlowController fc = getFlowController();
      return parent.join(fc);
    }

    final HelloWorldLayoutController derived = (HelloWorldLayoutController) clone();
    derived.setProcessingState(ElementLayoutController.FINISHED);
    return derived;
  }

  /**
   * Joins with a delegated process flow. This is generally called from a child
   * flow and should *not* (I mean it!) be called from outside. If you do,
   * you'll suffer.
   *
   * @param flowController the flow controller of the parent.
   * @return the joined layout controller that incorperates all changes from
   * the delegate.
   */
  public LayoutController join(final FlowController flowController)
  {
    // We do not expect any calls here, so ..
    throw new UnsupportedOperationException();

    // In case we have delegated the call ..
//    HelloWorldLayoutController derived = (HelloWorldLayoutController) clone();
//    // Of course, you may have to adjust the state information ...
//    derived.setProcessingState(FINISHING);
//    derived.setFlowController(flowController);
//    return derived;

  }
}
