/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * ------------
 * Element.java
 * ------------
 * (C)opyright 2000-2003, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: Element.java,v 1.13 2003/11/07 18:33:47 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 05-Mar-2002 : Integration of Thomas Morgner's code, plus PDF report generation via iText (DG);
 * 10-May-2002 : Removed complex-Constructor to support automatic generation of elements using
 *               generic parsers.
 * 16-May-2002 : Line delimiters adjusted
 *               paint now protected member
 * 20-May-2002 : Support for DataTarget interface added. The drawing scheme has changed to fit
 *               the new OutputTarget implementation
 * 26-May-2002 : Elements visible property controls whether an element is drawn by its band
 * 04-Jun-2002 : Documentation tags changed. A default name is generated, a default datasource
 *               is set. Elements paint is no longer protected, the paint is retrieved by the
 *               getPaint (Band) method. If neither band nor element have a paint declared, fail
 *               with and exception.
 * 04-Jul-2002 : Serializable and Cloneable
 * 05-Sep-2002 : Documentation
 * 06-Dec-2002 : Updated Javadocs (DG);
 * 06-Dec-2002 : Also updated the docs, declared setPaint(),getPaint deprecated, removed
 *               setStyleSheet
 * 13-Dec-2002 : Added support for parent property
 * 22-Jan-2003 : Paint parameter is now restricted to java.awt.Color
 * 27-Jan-2003 : Changed ParentRegistration, now the band is responsible for setting the parent
 *               property and the BandDefaultStyleSheet.
 * 04-Feb-2003 : removed equals() method. Element equality is no longer bound to names.
 */

package org.jfree.report;

import java.awt.geom.Dimension2D;
import java.io.Serializable;

import org.jfree.report.filter.DataSource;
import org.jfree.report.filter.DataTarget;
import org.jfree.report.filter.EmptyDataSource;
import org.jfree.report.filter.ReportConnectable;
import org.jfree.report.style.ElementDefaultStyleSheet;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.StyleSheetCarrier;
import org.jfree.report.util.InstanceID;

/**
 * Base class for all report elements (display items that can appear within a report band).
 * <p>
 * All elements have a non-null name and have a style sheet defined. The style sheet is
 * used to store and access all element properties that can be used to layout the
 * element or affect the elements appeareance in a ReportProcessor.
 *
 * @author David Gilbert
 * @author Thomas Morgner
 */
public abstract class Element implements DataTarget, Serializable, Cloneable
{
  protected static class InternalElementStyleSheet extends ElementStyleSheet
  {
    private Element element;
    private Band parent;

    public InternalElementStyleSheet (final Element element)
    {
      super(element.getName());
      this.parent = element.getParent();
      this.element = element;
      setDefaultStyleSheet(element.createGlobalDefaultStyle());
      setAllowCaching(true);
    }

    public Element getElement ()
    {
      return element;
    }

    /**
     * Creates and returns a copy of this object. After the cloning, the new StyleSheet is
     * no longer registered with its parents.
     *
     * @return a clone of this instance.
     *
     * @see Cloneable
     */
    public Object clone () throws CloneNotSupportedException
    {
      final InternalElementStyleSheet es = (InternalElementStyleSheet) super.clone();
      es.parent = null;
      es.element = null;
      return es;
    }

    public void parentChanged ()
    {
      if (parent != null)
      {
        setCascadeStyleSheet(null);
      }
      this.parent = element.getParent();
      if (parent != null)
      {
        setCascadeStyleSheet(parent.getStyle());
      }
    }

    protected StyleSheetCarrier createCarrier (final ElementStyleSheet styleSheet)
    {
      return null;
    }
  }

  protected ElementDefaultStyleSheet createGlobalDefaultStyle ()
  {
    return ElementDefaultStyleSheet.getDefaultStyle();
  }

  /** The internal constant to mark anonymous element names. */
  public static final String ANONYMOUS_ELEMENT_PREFIX = "anonymousElement@";

  /** A null datasource. */
  private static final DataSource NULL_DATASOURCE = new EmptyDataSource();

  /** The head of the data source chain. */
  private DataSource datasource;

  /** The name of the element. */
  private String name;

  /** The stylesheet defines global appearance for elements. */
  private InternalElementStyleSheet style;

  /** the parent for the element (the band where the element is contained in). */
  private Band parent;

  /** the tree lock to identify the element. */
  private final InstanceID treeLock;

  private ReportDefinition reportDefinition;

  /**
   * Constructs an element.
   * <p>
   * The element inherits the DefaultElementStyleSheet. When the element is added
   * to the band, the bands default stylesheet is also added to the elements style.
   * <p>
   * A datasource is assigned with this element is set to a default source,
   * which always returns null.
   */
  protected Element()
  {
    setName(ANONYMOUS_ELEMENT_PREFIX + System.identityHashCode(this));
    treeLock = new InstanceID();
    datasource = NULL_DATASOURCE;
    style = new InternalElementStyleSheet(this);
  }

  /**
   * Return the parent of the element.
   * You can use this to explore the component tree.
   *
   * @return the parent of the element.
   */
  public final Band getParent()
  {
    return parent;
  }

  /**
   * defines the parent of the element. Only a band should call this method.
   *
   * @param parent (null allowed).
   */
  protected final void setParent(final Band parent)
  {
    this.parent = parent;
    this.style.parentChanged();
  }

  /**
   * Defines the name for this element. The name must not be empty,
   * or a NullPointerException is thrown.
   *
   * @param name  the name of this element (null not permitted)
   */
  public void setName(final String name)
  {
    if (name == null)
    {
      throw new NullPointerException("Element.setName(...): name is null.");
    }
    this.name = name;
  }

  /**
   * Returns the name of the element. The name of the element cannot be null.
   *
   * @return the name.
   */
  public String getName()
  {
    return this.name;
  }

  /**
   * Returns the datasource for this element. You cannot override this function as the
   * element needs always be the last consumer in the chain of filters. This function
   * must never return null.
   *
   * @return the assigned datasource.
   */
  public final DataSource getDataSource()
  {
    return datasource;
  }

  /**
   * Sets the data source for this element. This datasource is queried on populateElements(),
   * to fill in the values.
   *
   * @param ds  the datasource (<code>null</code> not permitted).
   */
  public void setDataSource(final DataSource ds)
  {
    if (ds == null)
    {
      throw new NullPointerException("Element.setDataSource(...) : null data source.");
    }
    this.datasource = ds;
  }

  /**
   * Queries this element's datasource for a value.
   *
   * @return the value of the datasource, which can be null.
   */
  public Object getValue()
  {
    final DataSource ds = getDataSource();
    return ds.getValue();
  }

  /**
   * Defines whether this element should be painted. The detailed implementation is
   * up to the outputtarget.
   *
   * @return the current visiblity state.
   */
  public boolean isVisible()
  {
    final Boolean b = (Boolean) getStyle().getStyleProperty
        (ElementStyleSheet.VISIBLE, Boolean.FALSE);
    return b.booleanValue();
  }

  /**
   * Defines, whether this element should be drawn.
   *
   * @param b the new visibility state
   */
  public void setVisible(final boolean b)
  {
    getStyle().setStyleProperty(ElementStyleSheet.VISIBLE, b ? Boolean.TRUE : Boolean.FALSE);
  }

  /**
   * Clones this Element, the datasource and the private stylesheet of this element.
   * The clone does no longer have a parent, as the old parent would not recognize
   * that new object.
   *
   * @return a clone of this element.
   * @throws CloneNotSupportedException should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    final Element e = (Element) super.clone();
    // stylesheet clone disconnects the parent stylessheets ..
    e.style = (InternalElementStyleSheet) style.getCopy();
    // dataSource clone disconnects the reportDefinition ..
    e.datasource = (DataSource) datasource.clone();
    e.parent = null;
    e.style.element = e;
    e.reportDefinition = null;
    return e;
  }

  /**
   * Returns this elements private stylesheet. This sheet can be used to override
   * the default values set in one of the parent-stylesheets.
   *
   * @return the element's stylesheet
   */
  public ElementStyleSheet getStyle()
  {
    return style;
  }

  /**
   * Defines the content-type for this element. The content-type is used as a hint
   * how to process the contents of this element. An element implementation should
   * restrict itself to the content-type set here, or the reportprocessing may fail
   * or the element may not be printed.
   * <p>
   * An element is not allowed to change its content-type after ther report processing
   * has started.
   * <p>
   * If an content-type is unknown to the output-target, the processor should ignore
   * the content or clearly document its internal reprocessing. Ignoring is preferred.
   *
   * @return the content-type as string.
   */
  public abstract String getContentType();

  /**
   * Returns the tree lock object for the element tree.
   *
   * @return the treelock object.
   */
  public final Object getTreeLock ()
  {
    if (getParent() != null)
    {
      return getParent().getTreeLock();
    }
    return treeLock;
  }

  /**
   * Checks, whether the layout manager should compute the size of this element
   * based on the current content.
   * 
   * @return true, if the element is dynamic, false otherwise.
   */
  public boolean isDynamicContent()
  {
    return getStyle().getBooleanStyleProperty(ElementStyleSheet.DYNAMIC_HEIGHT);
  }

  /**
   * Defines the stylesheet property for the dynamic attribute. Calling this
   * function with either parameter will override any previously defined value
   * for the dynamic attribute. The value can no longer be inherited from parent
   * stylesheets.
   * 
   * @param dynamicContent the new state of the dynamic flag. 
   */
  public void setDynamicContent(final boolean dynamicContent)
  {
    getStyle().setBooleanStyleProperty
        (ElementStyleSheet.DYNAMIC_HEIGHT, dynamicContent);
    if (dynamicContent == true)
    {
      setLayoutCacheable(false);
    }
  }


  /**
   * Returns whether the layout of this element is cacheable.
   * 
   * @return true, if the layout is cacheable, false otherwise.
   */
  public boolean isLayoutCacheable()
  {
    return getStyle().getBooleanStyleProperty
        (ElementStyleSheet.ELEMENT_LAYOUT_CACHEABLE);
  }

  /**
   * Defines whether the layout of this element can be cached. 
   * <p>
   * Calling this function with either parameter will override any 
   * previously defined value for the layoutcachable attribute. 
   * The value can no longer be inherited from parent stylesheets.
   * 
   * @param layoutCacheable true, if the layout is cacheable, false otherwise. 
   */
  public void setLayoutCacheable(final boolean layoutCacheable)
  {
    getStyle().setBooleanStyleProperty
        (ElementStyleSheet.ELEMENT_LAYOUT_CACHEABLE, layoutCacheable);
  }


  /**
   * Returns the minimum size of this element, if defined.
   * Warning: The returned object is not immutable and should
   * not be changed.
   * 
   * @return the minimum size
   */
  public Dimension2D getMinimumSize()
  {
    return (Dimension2D) getStyle().getStyleProperty
        (ElementStyleSheet.MINIMUMSIZE);
  }

  /**
   * Defines the stylesheet property for the minimum element size. 
   * 
   * @param minimumSize the new minimum size or null, if the value should
   * be inherited.
   */
  public void setMinimumSize(final Dimension2D minimumSize)
  {
    getStyle().setStyleProperty (ElementStyleSheet.MINIMUMSIZE,
        minimumSize);
  }

  /**
   * Returns the maximum size of this element, if defined.
   * Warning: The returned object is not immutable and should
   * not be changed.
   * 
   * @return the maximum size
   */
  public Dimension2D getMaximumSize()
  {
    return (Dimension2D) getStyle().getStyleProperty
        (ElementStyleSheet.MAXIMUMSIZE);
  }

  /**
   * Defines the stylesheet property for the maximum element size. 
   * 
   * @param maximumSize the new maximum size or null, if the value should
   * be inherited.
   */
  public void setMaximumSize(final Dimension2D maximumSize)
  {
    getStyle().setStyleProperty (ElementStyleSheet.MAXIMUMSIZE,
        maximumSize);
  }

  /**
   * Returns the preferred size of this element, if defined.
   * Warning: The returned object is not immutable and should
   * not be changed.
   * 
   * @return the preferred size
   */
  public Dimension2D getPreferredSize()
  {
    return (Dimension2D) getStyle().getStyleProperty
        (ElementStyleSheet.PREFERREDSIZE);
  }

  /**
   * Defines the stylesheet property for the preferred element size. 
   * 
   * @param preferredSize the new preferred size or null, if the value should
   * be inherited.
   */
  public void setPreferredSize(final Dimension2D preferredSize)
  {
    getStyle().setStyleProperty (ElementStyleSheet.PREFERREDSIZE,
        preferredSize);
  }

  protected void setReportDefinition (final ReportDefinition reportDefinition)
  {
    if (this.reportDefinition != null)
    {
      disconnectDataSource(getDataSource());
    }
    this.reportDefinition = reportDefinition;
    if (this.reportDefinition != null)
    {
      connectDataSource(getDataSource());
    }
  }

  public ReportDefinition getReportDefinition ()
  {
    return reportDefinition;
  }

  protected final void connectDataSource (final DataSource ds)
  {
    if (ds instanceof ReportConnectable)
    {
      final ReportConnectable rc = (ReportConnectable) ds;
      rc.registerReportDefinition(reportDefinition);
    }
    if (ds instanceof DataTarget)
    {
      final DataTarget dt = (DataTarget) ds;
      connectDataSource(dt.getDataSource());
    }
  }

  protected final void disconnectDataSource (final DataSource ds)
  {
    if (ds instanceof ReportConnectable)
    {
      final ReportConnectable rc = (ReportConnectable) ds;
      rc.unregisterReportDefinition(reportDefinition);
    }
    if (ds instanceof DataTarget)
    {
      final DataTarget dt = (DataTarget) ds;
      disconnectDataSource(dt.getDataSource());
    }
  }

}
