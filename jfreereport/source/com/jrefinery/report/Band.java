/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
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
 * ---------
 * Band.java
 * ---------
 * (C)opyright 2000-2002, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: Band.java,v 1.26 2002/12/02 18:23:57 taqua Exp $
 *
 * Changes (from 8-Feb-2002)
 * -------------------------
 * 08-Feb-2002 : Updated code to work with latest version of the JCommon class library (DG);
 * 18-Feb-2002 : Changed band height from Number --> float (DG);
 * 05-Mar-2002 : Changed the constructors from public --> protected (DG);
 * 10-May-2002 : Declared Abstract, Removed complex constructors
 * 11-May-2002 : Bug: when adding multiple data fields referencing to the same column in the
 *               data model only the first field was filled with data on populateElements.
 * 20-May-2002 : Changed to support new drawing scheme. The state of the OutputTarget is stored
 *               before any element starts to draw and restored afterwards. This will greatly
 *               reduce sideeffects from changed fonts or paints which are not restored by the
 *               element.
 * 26-May-2002 : Elements are now stored ordered. Updated drawing to reflect new element property
 *               "Visible".
 * 04-Jun-2002 : Public methods throw exceptions on illegal values. Documentation update.
 * 04-Jul-2002 : Serializable and Cloneable
 * 08-Aug-2002 : Band visibility support added. Bands can be hidden using the visible-property
 * 22-Aug-2002 : Height contains now a special value (-100) to adjust the band to fit the available
 *               pageheight.  This is a temporary fix and gets reomved with the next layout update.
 * 31-Aug-2002 : Removed separate stoage of Function and ReportDataSource elements. This stuff was
 *               no longer in use.
 * 05-Sep-2002 : Documentation
 */

package com.jrefinery.report;

import com.jrefinery.report.targets.FloatDimension;
import com.jrefinery.report.targets.style.BandDefaultStyleSheet;
import com.jrefinery.report.targets.style.BandStyleSheet;
import com.jrefinery.report.targets.style.ElementStyleSheet;

import java.awt.geom.Dimension2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A report band contains a list of elements to be displayed, and represents one section of a
 * report (the report header or footer, the page header or footer, the group header or footer,
 * or the items within a group).
 * <P>
 * The elements in a report band can contain fixed values, field values from the dataset, or
 * function values. The elements are not required to have unique names.
 * <p>
 * This implementation is not synchronized, to take care that you externally synchronize
 * it when using multiple threads.
 *
 * @author David Gilbert
 * @author Thomas Morgner
 */
public class Band extends Element implements Serializable, Cloneable
{
  /** All the elements for this band, stored by name. */
  private ArrayList allElements;

  /** The default style-sheet for the elements contained in the band. */
  private ElementStyleSheet bandDefaults;

  /**
   * Constructs a new band (initially empty).
   */
  public Band()
  {
    getStyle().addParent(BandDefaultStyleSheet.getBandDefaultStyle());

    setName("anonymousBand@" + hashCode());
    allElements = new ArrayList();

    // naming ??
    bandDefaults = new BandStyleSheet("default");
  }

  /**
   * Returns the default style-sheet for the band's elements.
   *
   * @return the default style-sheet.
   */
  public ElementStyleSheet getBandDefaults ()
  {
    return bandDefaults;
  }

  /**
   * Adds a report element to the band.
   *
   * @param element  the element (null not permitted).
   *
   * @throws NullPointerException if the Element is null or contains Null-Values.
   * @throws IllegalArgumentException if the element violates validity rules
   */
  public void addElement(Element element)
  {
    if (element == null)
    {
      throw new NullPointerException("Band.addElement(...): element is null.");
    }
    allElements.add(element);
    element.getStyle().addParent(getBandDefaults());
  }

  /**
   * Adds a collection of elements to the band.
   *
   * @param elements  the element collection.
   *
   * @throws NullPointerException if the collection given is <code>null</code>.
   */
  public void addElements(Collection elements)
  {
    if (elements == null)
    {
      throw new NullPointerException("Band.addElements(...): collection is null.");
    }

    Iterator iterator = elements.iterator();
    while (iterator.hasNext())
    {
      Element element = (Element) iterator.next();
      addElement(element);
    }
  }

  /**
   * Returns the first element in the list that is registered by the given name.
   *
   * @param name  the element name.
   *
   * @return the first element with the specified name, or <code>null</code> if there is no 
   *         such element.
   *
   * @throws NullPointerException if the given name is null.
   */
  public Element getElement(String name)
  {
    if (name == null)
    {
      throw new NullPointerException("Band.getElement(...): name is null.");
    }

    Iterator it = allElements.iterator();
    while (it.hasNext())
    {
      Element e = (Element) it.next();
      if (e.getName() != null)
      {
        if (e.getName().equals(name))
        {
          return e;
        }
      }
    }
    return null;
  }

  /**
   * Removes an element from the band.
   *
   * @param e  the element.
   */
  public void removeElement (Element e)
  {
    e.getStyle().removeParent(getBandDefaults());
    allElements.remove(e);
  }

  /**
   * Returns a list of the elements in the band.
   *
   * @return an immutable list of all registered elements for this band.
   */
  public List getElements()
  {
    return Collections.unmodifiableList(allElements);
  }

  /**
   * Returns an array of the elements in the band.
   *
   * @return the elements.
   */
  public Element[] getElementArray ()
  {
    Element[] elements = new Element[allElements.size()];
    elements = (Element[]) allElements.toArray(elements);
    return elements;
  }

  /**
   * Returns a string representation of the band and all the elements it contains, useful 
   * mainly for debugging purposes.
   *
   * @return a string.
   */
  public String toString()
  {
    StringBuffer b = new StringBuffer();
    b.append(this.getClass().getName());
    return b.toString();
  }

  /**
   * Clones this Band and all Elements contained in this band.
   *
   * @return the clone of this band.
   *
   * @throws CloneNotSupportedException if this band or an element contained in this band does not
   *                                    support cloning.
   */
  public Object clone() throws CloneNotSupportedException
  {
    Band b = (Band) super.clone();
    b.bandDefaults = (ElementStyleSheet) bandDefaults.clone();
    b.allElements = (ArrayList) allElements.clone();
    b.allElements.clear();
    for (int i = 0; i < allElements.size(); i++)
    {
      Element e = (Element) allElements.get(i);
      b.allElements.add (e.clone());
    }
    return b;
  }

  /** The content type. */
  public static final String CONTENT_TYPE = "X-container";

  /** 
   * Returns a string indicating the band's content type.
   *
   * @return the content type.
   */
  public String getContentType()
  {
    return CONTENT_TYPE;
  }

  /// DEPRECATED METHODS //////////////////////////////////////////////////////////////////////////
  
  /**
   * Sets the height of the band.
   *
   * @param height  the band height.
   *
   * @deprecated do not manipulate the element properties that way, use a stylesheet.
   */
  public void setHeight (float height)
  {
    getStyle().setStyleProperty(ElementStyleSheet.MINIMUMSIZE, new FloatDimension (0, height));
  }

  /**
   * Returns the band height.
   *
   * @return the band height.
   *
   * @deprecated do not manipulate the element properties that way, use a stylesheet and take care
   * of the layoutmanager reading this data.
   */
  public float getHeight ()
  {
    Dimension2D d = (Dimension2D) getStyle().getStyleProperty(ElementStyleSheet.MINIMUMSIZE, 
                                                              new FloatDimension(0, 0));
    return (float) d.getHeight();
  }
}
