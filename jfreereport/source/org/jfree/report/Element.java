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
 * $Id: Element.java,v 1.8 2003/09/12 18:46:02 taqua Exp $
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

import java.io.Serializable;

import org.jfree.report.filter.DataSource;
import org.jfree.report.filter.DataTarget;
import org.jfree.report.filter.EmptyDataSource;
import org.jfree.report.style.ElementDefaultStyleSheet;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.InvalidStyleSheetCollectionException;
import org.jfree.report.style.StyleSheetCollection;
import org.jfree.report.style.StyleSheetCollectionHelper;
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
  /** The internal constant to mark anonymous element names. */
  public static final String ANONYMOUS_ELEMENT_PREFIX = "anonymousElement@";

  /**
   * Internal helper class to handle the style sheet collection properly.
   */
  private static class ElementStyleSheetCollectionHelper extends StyleSheetCollectionHelper
  {
    /** The Element for which we handle the style sheet collection. */
    private Element element;

    /**
     * Creates a new ElementStyleSheetCollectionHelper for the given element.
     *
     * @param e the element.
     * @throws NullPointerException if the given element is null.
     */
    public ElementStyleSheetCollectionHelper(final Element e)
    {
      if (e == null)
      {
        throw new NullPointerException();
      }
      this.element = e;
    }

    /**
     * Handles the stylesheet collection registration.
     * Forwards the call to the element.
     */
    protected void handleRegisterStyleSheetCollection()
    {
      element.handleRegisterStyleSheetCollection();
    }

    /**
     * Handles the stylesheet collection removal.
     * Forwards the call to the element.
     */
    protected void handleUnregisterStyleSheetCollection()
    {
      element.handleUnregisterStyleSheetCollection();
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

  /**
   * The stylesheet collection helper is used to manage the StyleSheetCollection of
   * this element. The use of the stylesheet collection is evil voodo, so we dont
   * handle it in the element ...
   */
  private ElementStyleSheetCollectionHelper styleSheetCollectionHelper;

  /** the parent for the element (the band where the element is contained in). */
  private Band parent;

  /** the tree lock to synchronize the element. */
  private final InstanceID treeLock;

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
    treeLock = new InstanceID();
    setName(ANONYMOUS_ELEMENT_PREFIX + super.hashCode());
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
  protected final void setParent(final Band parent)
  {
    this.parent = parent;
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
    final Element e = (Element) super.clone();
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
  public abstract String getContentType();

  /**
   * Returns the stylesheet collection which is assigned with this element and
   * all stylesheets of this element.
   *
   * @return the element stylesheet collection or null, if no collection is assigned.
   */
  public StyleSheetCollection getStyleSheetCollection()
  {
    return styleSheetCollectionHelper.getStyleSheetCollection();
  }

  /**
   * Registers the given StyleSheet collection with this element. If there is already
   * another stylesheet collection registered, this method will throw an
   * <code>InvalidStyleSheetCollectionException</code>.
   *
   * @param styleSheetCollection the stylesheet collection that should be registered.
   * @throws InvalidStyleSheetCollectionException if there is already an other
   * stylesheet registered.
   * @throws NullPointerException if the given stylesheet collection is null.
   */
  public void registerStyleSheetCollection(final StyleSheetCollection styleSheetCollection)
      throws InvalidStyleSheetCollectionException
  {
    styleSheetCollectionHelper.registerStyleSheetCollection(styleSheetCollection);
  }

  /**
   * Unregisters the given stylesheet collection from this element. If this stylesheet
   * collection is not registered with this element, this method will throw an
   * <code>InvalidStyleSheetCollectionException</code>
   *
   * @param styleSheetCollection the stylesheet collection that should be unregistered.
   * @throws InvalidStyleSheetCollectionException  if there is already an other stylesheet
   * registered.
   * @throws NullPointerException if the given stylesheet collection is null.
   */
  public void unregisterStyleSheetCollection(final StyleSheetCollection styleSheetCollection)
      throws InvalidStyleSheetCollectionException
  {
    styleSheetCollectionHelper.unregisterStyleSheetCollection(styleSheetCollection);
  }

  /**
   * Handles the unregistration of the stylesheet collection.
   */
  protected void handleUnregisterStyleSheetCollection()
  {
    getStyle().unregisterStyleSheetCollection(getStyleSheetCollection());
  }

  /**
   * Handles the registration of the stylesheet collection.
   */
  protected void handleRegisterStyleSheetCollection()
  {
    getStyle().registerStyleSheetCollection(getStyleSheetCollection());

    /**
     * This is an assertation implementation ... leave it alive to be
     * sure that everything works as expected ...
     */
    if (getStyle().getStyleSheetCollection() != getStyleSheetCollection())
    {
      throw new IllegalStateException("HandleRegisterStyleSheetCollection failed: " +
          getStyle().getName() + " for element " + getName());
    }
  }

  /**
   * Updates the stylesheet collection for this element and all substylesheets.
   * This method must be called after the element was cloned, to make sure that
   * all stylesheets are registered properly.
   * <p>
   * If you don't call this function after cloning prepare to be doomed.
   * This method will replace all inherited stylesheets with clones from the stylesheet
   * collection.
   *
   * @param sc the stylesheet collection that contains the updated information and
   * that should be assigned with that element.
   * @throws NullPointerException if the given stylesheet collection is null.
   * @throws InvalidStyleSheetCollectionException if there is an other stylesheet
   * collection already registered with that element.
   */
  public void updateStyleSheetCollection(final StyleSheetCollection sc)
      throws InvalidStyleSheetCollectionException
  {
    if (sc == null)
    {
      throw new NullPointerException("StyleSheetCollection is null.");
    }
    if (getStyleSheetCollection() != null)
    {
      throw new InvalidStyleSheetCollectionException
          ("There is a stylesheet collection already registered.");
    }

    sc.updateStyleSheet(getStyle());

    registerStyleSheetCollection(sc);
  }

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
}
