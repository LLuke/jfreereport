/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * ------------
 * Element.java
 * ------------
 * (C)opyright 2000-2003, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: Element.java,v 1.31 2003/06/15 19:05:41 taqua Exp $
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

package com.jrefinery.report;

import java.awt.Color;
import java.awt.Paint;
import java.io.Serializable;

import com.jrefinery.report.filter.DataSource;
import com.jrefinery.report.filter.DataTarget;
import com.jrefinery.report.filter.EmptyDataSource;
import com.jrefinery.report.targets.style.ElementDefaultStyleSheet;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.targets.style.StyleSheetCollection;
import com.jrefinery.report.targets.style.InvalidStyleSheetCollectionException;
import com.jrefinery.report.targets.style.StyleSheetCollectionHelper;

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
  private static class ElementStyleSheetCollectionHelper extends StyleSheetCollectionHelper
  {
    private Element element;

    public ElementStyleSheetCollectionHelper(Element e)
    {
      if (e == null)
      {
        throw new NullPointerException();
      }
      this.element = e;
    }

    protected void registerStyleSheetCollection()
    {
      element.registerStyleSheetCollection();
    }

    protected void unregisterStyleSheetCollection()
    {
      element.unregisterStyleSheetCollection();
    }
  }

  /** A null datasource. */
  private static final DataSource NULL_DATASOURCE = new EmptyDataSource();

  /** The head of the data source chain. */
  private DataSource datasource;

  /** The name of the element. */
  private String name;

  /** The stylesheet defines global appearance for elements. */
  private ElementStyleSheet style;

  private ElementStyleSheetCollectionHelper styleSheetCollectionHelper;

  /**
   * ElementStyle constant for horizontal left alignment.
   * For StyleSheet definition use the ElementAlignment-Objects.
   *
   * @deprecated use ElementAlignment objects instead.
   */
  public static final int LEFT = ElementAlignment.LEFT.getOldAlignment();
  /**
   * ElementStyle constant for horizontal right alignment.
   * For StyleSheet definition use the ElementAlignment-Objects.
   *
   * @deprecated use ElementAlignment objects instead.
   */
  public static final int RIGHT = ElementAlignment.RIGHT.getOldAlignment();
  /**
   * ElementStyle constant for horizontal center alignment.
   * For StyleSheet definition use the ElementAlignment-Objects.
   *
   * @deprecated use ElementAlignment objects instead.
   */
  public static final int CENTER = ElementAlignment.CENTER.getOldAlignment();

  /**
   * ElementStyle constant for vertical top alignment.
   * For StyleSheet definition use the ElementAlignment-Objects.
   *
   * @deprecated use ElementAlignment objects instead.
   */
  public static final int TOP = ElementAlignment.TOP.getOldAlignment();
  /**
   * ElementStyle constant for vertical middle alignment.
   * For StyleSheet definition use the ElementAlignment-Objects.
   *
   * @deprecated use ElementAlignment objects instead.
   */
  public static final int MIDDLE = ElementAlignment.MIDDLE.getOldAlignment();
  /**
   * ElementStyle constant for vertical bottom alignment.
   * For StyleSheet definition use the ElementAlignment-Objects.
   *
   * @deprecated use ElementAlignment objects instead.
   */
  public static final int BOTTOM = ElementAlignment.BOTTOM.getOldAlignment();

  /** the parent for the element (the band where the element is contained in). */
  private Band parent;

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
    setName("anonymousElement@" + super.hashCode());
    datasource = NULL_DATASOURCE;
    style = new ElementStyleSheet(getName());
    style.setAllowCaching(true);
    style.addDefaultParent(ElementDefaultStyleSheet.getDefaultStyle());
    styleSheetCollectionHelper = new ElementStyleSheetCollectionHelper(this);
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
  protected final void setParent(Band parent)
  {
    this.parent = parent;
  }

  /**
   * Defines the name for this element. The name must not be empty,
   * or a NullPointerException is thrown.
   *
   * @param name  the name of this element (null not permitted)
   */
  public void setName(String name)
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
  public void setDataSource(DataSource ds)
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
    DataSource ds = getDataSource();
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
    Boolean b = (Boolean) getStyle().getStyleProperty(ElementStyleSheet.VISIBLE, Boolean.FALSE);
    return b.booleanValue();
  }

  /**
   * Defines, whether this element should be drawn.
   *
   * @param b the new visibility state
   */
  public void setVisible(boolean b)
  {
    getStyle().setStyleProperty(ElementStyleSheet.VISIBLE, b ? Boolean.TRUE : Boolean.FALSE);
  }

  /**
   * Clones this Element, the datasource and the private stylesheet of this element.
   * If this element was previously assigned with an stylesheet collection, then the
   * clone is no longer assiged with that collection. You will have to register the
   * element manually again.
   *
   * @return a clone of this element.
   *
   * @throws CloneNotSupportedException should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    Element e = (Element) super.clone();
    e.style = style.getCopy();
    e.datasource = (DataSource) datasource.clone();
    e.styleSheetCollectionHelper = new ElementStyleSheetCollectionHelper(e);
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
  public abstract String getContentType ();


  public StyleSheetCollection getStyleSheetCollection()
  {
    return styleSheetCollectionHelper.getStyleSheetCollection();
  }

  public void setStyleSheetCollection(StyleSheetCollection styleSheetCollection)
  {
    styleSheetCollectionHelper.setStyleSheetCollection(styleSheetCollection);
  }

  protected void unregisterStyleSheetCollection ()
  {
    getStyleSheetCollection().remove(getStyle());
  }

  protected void registerStyleSheetCollection ()
  {
    getStyleSheetCollection().addStyleSheet(getStyle());
  }

  /// DEPRECATED METHODS //////////////////////////////////////////////////////////////////////////

  /**
   * Returns the paint used to draw this element. There is alway a paint defined
   * for an element.
   *
   * @deprecated don't store and access the paint directly, use the stylesheet.
   * @return The paint.
   */
  public Paint getPaint()
  {
    Paint retval = (Paint) getStyle().getStyleProperty(ElementStyleSheet.PAINT);
    if (retval == null)
    {
      // assertation
      throw new IllegalStateException("Element.getPaint(): no Paint defined.");
    }
    return retval;
  }

  /**
   * Sets the paint for this element. The paint can be null, in this case the
   * default paint of the band used to draw this element is used.
   * <p>
   * The paint object must be an instance of color. Generic paints are not permitted.
   *
   * @param p  the paint for this element (null permitted).
   * 
   * @deprecated use a stylesheet to define the paint. The paint object must be an
   * instance of color.
   */
  public void setPaint(Paint p)
  {
    if (p == null)
    {
      getStyle().setStyleProperty(ElementStyleSheet.PAINT, null);
    }
    else
    if (p instanceof Color)
    {
      getStyle().setStyleProperty(ElementStyleSheet.PAINT, p);
    }
    else
    {
      throw new IllegalArgumentException("This style-key requires a Color object");
    }
  }
}
