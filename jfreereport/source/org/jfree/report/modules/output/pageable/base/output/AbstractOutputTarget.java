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
 * -------------------------
 * AbstractOutputTarget.java
 * -------------------------
 * (C)opyright 2000-2003, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: AbstractOutputTarget.java,v 1.13 2005/02/05 18:35:18 taqua Exp $
 *
 * Changes
 * -------
 * 21-May-2002 : Initial version
 * 22-May-2002 : TextAlignment fixed
 * 23-May-2002 : Replaced System.out logging with Log-class
 * 30-May-2002 : Performance upgrade: One-line texts are not processed by linebreak-method
 * 08-Jun-2002 : Documentation
 * 17-Jul-2002 : Added NullPointer handling for drawText(). Whitespaces are now replaced by
 *               space (0x20) if the text to be printed fits on a single line
 * 20-Jul-2002 : created this changelog
 * 23-Aug-2002 : breakLines was broken, fixed and removed useless code ..
 * 23-Aug-2002 : removed the strictmode, the reserved literal is now always added
 * 26-Aug-2002 : Corrected Fontheight calculations.
 * 02-Oct-2002 : Bug: breakLines() got a corrected word breaking (Aleksandr Gekht)
 * 06-Nov-2002 : Bug: LineBreaking again: Handled multiple linebreaks and empty lines
 * 15-Feb-2004 : Complete Rewrite; Handles MetaPage mapping now.
 */
package org.jfree.report.modules.output.pageable.base.output;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.Properties;

import org.jfree.report.ElementAlignment;
import org.jfree.report.PageDefinition;
import org.jfree.report.ShapeElement;
import org.jfree.report.content.AnchorContentFactoryModule;
import org.jfree.report.content.Content;
import org.jfree.report.content.ContentFactory;
import org.jfree.report.content.ContentType;
import org.jfree.report.content.DefaultContentFactory;
import org.jfree.report.content.DrawableContent;
import org.jfree.report.content.DrawableContentFactoryModule;
import org.jfree.report.content.EmptyContent;
import org.jfree.report.content.ImageContent;
import org.jfree.report.content.ImageContentFactoryModule;
import org.jfree.report.content.MultipartContent;
import org.jfree.report.content.ShapeContent;
import org.jfree.report.content.ShapeContentFactoryModule;
import org.jfree.report.content.ShapeTransform;
import org.jfree.report.content.TextContentFactoryModule;
import org.jfree.report.content.TextLine;
import org.jfree.report.modules.output.meta.MetaBand;
import org.jfree.report.modules.output.meta.MetaElement;
import org.jfree.report.modules.output.meta.MetaPage;
import org.jfree.report.modules.output.pageable.base.OutputTarget;
import org.jfree.report.modules.output.pageable.base.OutputTargetException;
import org.jfree.report.modules.output.pageable.base.operations.AlignmentTools;
import org.jfree.report.modules.output.pageable.base.operations.HorizontalBoundsAlignment;
import org.jfree.report.modules.output.pageable.base.operations.VerticalBoundsAlignment;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.FontDefinition;
import org.jfree.report.util.Log;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.util.geom.StrictGeomUtility;

/**
 * The abstract OutputTarget implements base code for all pageable OutputTargets.
 * This implementation contains base functions to process the MetaPage objects
 * to create the real output.
 *
 * @author David Gilbert
 * @author Thomas Morgner
 */
public abstract strictfp class AbstractOutputTarget implements OutputTarget
{
  /** Storage for the output target properties. */
  private final Properties properties;

  /** The content factory used to create content for this output-target. */
  private final ContentFactory contentFactory;

  private StrictBounds operationBounds;
  private StrictBounds pageBounds;

  protected AbstractOutputTarget ()
  {
    properties = new Properties();
    contentFactory = createContentFactory();
    operationBounds = new StrictBounds();
    pageBounds = new StrictBounds();
  }

  /**
   * Defines a property for this output target. Properties are the standard way of configuring
   * an output target.
   *
   * @param property  the name of the property to set (<code>null</code> not permitted).
   * @param value  the value of the property.  If the value is <code>null</code>, the property is
   * removed from the output target.
   */
  public void setProperty(final String property, final String value)
  {
    if (property == null)
    {
      throw new NullPointerException();
    }

    if (value == null)
    {
      properties.remove(property);
    }
    else
    {
      properties.setProperty(property, value);
    }
  }

  /**
   * Queries the property named with <code>property</code>. If the property is not found, <code>
   * null</code> is returned.
   *
   * @param property the name of the property to be queried
   *
   * @return the value stored under the given property name
   *
   * @throws java.lang.NullPointerException if <code>property</code> is null
   */
  public String getProperty(final String property)
  {
    return getProperty(property, null);
  }

  /**
   * Queries the property named with <code>property</code>. If the property is not found, the
   * default value is returned.
   *
   * @param property the name of the property to be queried
   * @param defaultValue the defaultvalue returned if there is no such property
   *
   * @return the value stored under the given property name
   *
   * @throws java.lang.NullPointerException if <code>property</code> is null
   */
  public String getProperty(final String property, final String defaultValue)
  {
    if (property == null)
    {
      throw new NullPointerException();
    }

    final String retval = properties.getProperty(property);
    if (retval == null)
    {
      return defaultValue;
    }
    return retval;
  }

  /**
   * Returns an enumeration of the property names.
   *
   * @return the enumeration.
   */
  protected Iterator getPropertyNames()
  {
    return properties.keySet().iterator();
  }

  /**
   * Returns the element alignment. Elements will be layouted aligned to this
   * border, so that <code>mod(X, horizontalAlignment) == 0</code> and
   * <code>mod(Y, verticalAlignment) == 0</code>
   *
   * @return the vertical alignment grid boundry
   */
  public float getHorizontalAlignmentBorder()
  {
    return 0;
  }

  /**
   * Returns the element alignment. Elements will be layouted aligned to this
   * border, so that <code>mod(X, horizontalAlignment) == 0</code> and
   * <code>mod(Y, verticalAlignment) == 0</code>
   *
   * @return the vertical alignment grid boundry
   */
  public float getVerticalAlignmentBorder()
  {
    return 0;
  }

  /**
   * Returns the assigned content factory for the target.
   *
   * @return the content factory.
   */
  public ContentFactory getContentFactory()
  {
    return contentFactory;
  }

  /**
   * Creates a default content factory, which supports all known content types.
   * Override this method to supply an own implementation of the ContentFactory.
   *
   * @return a default content factory.
   */
  protected ContentFactory createContentFactory()
  {
    final DefaultContentFactory contentFactory = new DefaultContentFactory();
    contentFactory.addModule(new TextContentFactoryModule());
    contentFactory.addModule(new ImageContentFactoryModule());
    contentFactory.addModule(new ShapeContentFactoryModule());
    contentFactory.addModule(new DrawableContentFactoryModule());
    contentFactory.addModule(new AnchorContentFactoryModule());
    return contentFactory;
  }

  protected abstract void beginPage (PageDefinition page, int index) throws OutputTargetException;

  protected abstract void endPage () throws OutputTargetException;

  public void printPage(final MetaPage content, final PageDefinition page, final int index)
   throws OutputTargetException
  {
    setPageBounds(page.getPagePosition(index));
    beginPage(page, index);
    final StrictBounds pageBounds = getInternalPageBounds();

    // for all stored bands
    final MetaBand[] bands = content.getBands();
    for (int i = 0; i < bands.length; i++)
    {
      final MetaBand b = bands[i];
      final StrictBounds bounds = b.getBounds();
      // check if bounds are within the specified page bounds
      if (StrictBounds.intersects(bounds, pageBounds))
      {
        // if so, then print
        printBand(b, pageBounds.createIntersection(bounds));
      }
      else
      {
      // else ignore
        Log.debug ("Ignoring: PageBounds:" + pageBounds + " vs . BandBounds: " + bounds);
      }
    }
    endPage();
  }


  /**
   * Prints all elements of the band, which are within the given bounds.
   * The bounds are global bounds, so they define a position on the meta
   * page, not the physical page.
   *
   * @param band the band that should be printed
   * @param bounds the bounds for that band.
   */
  protected void printBand (final MetaBand band, final StrictBounds bounds)
    throws OutputTargetException
  {
    printElement(band, bounds);
    final MetaElement[] elements = band.toArray();
    for (int i = 0; i < elements.length; i++)
    {
      final MetaElement e = elements[i];
      final StrictBounds elementBounds = e.getBounds();
      // check if bounds are within the specified page bounds
      if (StrictBounds.intersects(bounds, elementBounds))
      {
        if (e instanceof MetaBand)
        {
          printBand((MetaBand) e, bounds.createIntersection(elementBounds));
        }
        else
        {
          printElement(e, bounds.createIntersection(elementBounds));
        }
      }
    }
  }

  protected void printHRefTarget (final MetaElement element, final String target)
  {
  }

  protected void printElement (final MetaElement element, final StrictBounds bounds)
    throws OutputTargetException
  {
    final String hrefTarget = (String) element.getProperty(ElementStyleSheet.HREF_TARGET);
    if (hrefTarget != null)
    {
      printHRefTarget(element, hrefTarget);
    }

    final Content content = element.getContent().getContentForBounds(bounds);
    if (content instanceof EmptyContent)
    {
      return;
    }

    final ElementAlignment va
        = (ElementAlignment) element.getProperty(ElementStyleSheet.VALIGNMENT);
    if (va != null)
    {
      final VerticalBoundsAlignment vba = AlignmentTools.getVerticalLayout(va, bounds);
      // calculate the horizontal shift ... is applied later
      final StrictBounds cBounds = content.getMinimumContentSize();
      final StrictBounds alignedBounds = vba.align((StrictBounds) cBounds.clone());
      final long vbaShift = (alignedBounds.getY() - cBounds.getY());
      printContent(element, content, vbaShift);
    }
    else
    {
      printContent(element, content, 0);
    }
  }

  protected void printContent
          (final MetaElement element, final Content content, final long vbaShift)
          throws OutputTargetException
  {
    if (content.getContentType().equals(ContentType.TEXT))
    {
      printTextContent(element, content, vbaShift);
    }
    else if (element.getContent().getContentType().equals(ContentType.SHAPE))
    {
      printShapeContent(element, content);
    }
    else if (element.getContent().getContentType().equals(ContentType.IMAGE))
    {
      printImageContent(element, content);
    }
    else if (element.getContent().getContentType().equals(ContentType.DRAWABLE))
    {
      printDrawableContent(element, content);
    }
    else if (element.getContent().getContentType().equals(ContentType.CONTAINER))
    {
      printContainerContent(element, content);
    }
    else if (element.getContent().getContentType().equals(ContentType.ANCHOR))
    {
      printAnchorContent(element, content);
    }
    else
    {
      Log.warn ("Unknown content");
    }

    // Warning: Cheap Hack: Only working with text content, everything else will fail!
    if (content instanceof MultipartContent)
    {
      final MultipartContent mc = (MultipartContent) content;
      for (int i = 0; i < mc.getContentPartCount(); i++)
      {
        printContent (element, mc.getContentPart(i), vbaShift);
      }

    }
  }

  protected void printTextContent
          (final MetaElement element, final Content content,
           final long vbaShift)
    throws OutputTargetException
  {
    if (element == null)
    {
      throw new NullPointerException("element is null");
    }

    // we assume here, that the content bounds are also defined for the global
    // range, or we have to 'adjust' them now.
    //final Rectangle2D bounds = element.getBounds();
    if (content instanceof TextLine == false)
    {
      return;
    }
    // Font
    final FontDefinition font = element.getFontDefinitionProperty();
    updateFont(font);

    // Paint
    final Paint extpaint = (Paint) element.getProperty(ElementStyleSheet.EXTPAINT);
    if (extpaint != null && isPaintSupported(extpaint))
    {
      updatePaint(extpaint);
    }
    else
    {
      final Color paint = (Color) element.getProperty(ElementStyleSheet.PAINT);
      updatePaint(paint);
    }

    final Stroke stroke = (Stroke) element.getProperty(ElementStyleSheet.STROKE);
    updateStroke(stroke);

    final ElementAlignment ha
        = (ElementAlignment) element.getProperty(ElementStyleSheet.ALIGNMENT);

    final HorizontalBoundsAlignment hba =
            AlignmentTools.getHorizontalLayout(ha, element.getBounds());
    printTextLine((TextLine) content, hba, vbaShift);
  }

  protected void printShapeContent (final MetaElement element, final Content content)
      throws OutputTargetException
  {
    if (content instanceof ShapeContent == false)
    {
      return;
    }
    //final Rectangle2D bounds = element.getBounds();

    final boolean shouldDraw = element.getBooleanProperty(ShapeElement.DRAW_SHAPE);
    final boolean shouldFill = element.getBooleanProperty(ShapeElement.FILL_SHAPE);

    if (shouldFill == false && shouldDraw == false)
    {
      return;
    }

    final StrictBounds operationBounds = AlignmentTools.computeAlignmentBounds(element);
    setInternalOperationBounds(operationBounds);

    final Stroke stroke = (Stroke) element.getProperty(ElementStyleSheet.STROKE);
    updateStroke(stroke);
    // Paint
    final Paint extpaint = (Paint) element.getProperty(ElementStyleSheet.EXTPAINT);
    if (isPaintSupported(extpaint))
    {
      updatePaint(extpaint);
    }
    else
    {
      final Color paint = (Color) element.getProperty(ElementStyleSheet.PAINT);
      updatePaint(paint);
    }

    final ShapeContent sc = (ShapeContent) content;
    // todo: Is this correct?
    final Rectangle2D clipBounds = getOperationBounds();
    final Shape s = ShapeTransform.performCliping(sc.getShape(), clipBounds);
    //final Shape s = sc.getShape();
    if (shouldDraw == true)
    {
      drawShape(s);
    }

    if (shouldFill == true)
    {
      fillShape(s);
    }
  }

  protected void printImageContent (final MetaElement element, final Content content)
      throws OutputTargetException
  {
    if (content instanceof ImageContent == false)
    {
      return;
    }
    final ImageContent ic = (ImageContent) content;
    setInternalOperationBounds(AlignmentTools.computeAlignmentBounds(element));
    drawImage(ic);
  }


  protected void printDrawableContent (final MetaElement element, final Content content)
      throws OutputTargetException
  {
    if (content instanceof DrawableContent == false)
    {
      return;
    }
    final DrawableContent drawableContent = (DrawableContent) content;
    setInternalOperationBounds(content.getBounds());
    drawDrawable (drawableContent);
  }

  protected void printContainerContent
          (final MetaElement element, final Content content)
          throws OutputTargetException
  {
  }

  protected void printAnchorContent
          (final MetaElement element, final Content content)
          throws OutputTargetException
  {
  }

  protected Rectangle2D getOperationBounds()
  {
    return StrictGeomUtility.createAWTRectangle
            (operationBounds.getX(), operationBounds.getY(),
                    operationBounds.getWidth(), operationBounds.getHeight());
  }

  protected StrictBounds getInternalOperationBounds()
  {
    return operationBounds;
  }

  /**
   * Correct the given bounds to fit on the page. The operation bounds
   * are valid within the global context, we have to adjust them to the
   * page local context.
   *
   * @param operationBounds the operation bounds
   */
  protected void setInternalOperationBounds(final StrictBounds operationBounds)
  {
    this.operationBounds.setRect(operationBounds.getX(), operationBounds.getY(),
            operationBounds.getWidth(), operationBounds.getHeight());
  }

  protected void setPageBounds(final Rectangle2D pageBounds)
  {
    final long x = StrictGeomUtility.toInternalValue(pageBounds.getX());
    final long y = StrictGeomUtility.toInternalValue(pageBounds.getY());
    final long w = StrictGeomUtility.toInternalValue(pageBounds.getWidth());
    final long h = StrictGeomUtility.toInternalValue(pageBounds.getHeight());
    this.pageBounds.setRect(x, y, w, h);
  }

  protected Rectangle2D getPageBounds()
  {
    return StrictGeomUtility.createAWTRectangle
            (pageBounds.getX(), pageBounds.getY(), pageBounds.getWidth(), pageBounds.getHeight());
  }

  protected StrictBounds getInternalPageBounds ()
  {
    return pageBounds;
  }
  /**
   * Add a single content junk (in most cases a single line or a line fragment) to
   * the list of PhysicalOperations. This method is called recursivly for all contentparts.
   *
   * @param c  the content.
   * @param hba  the bounds.
   * @param vbaShift  the vertical bounds alignment shifting.
   */
  protected void printTextLine (final TextLine c, final HorizontalBoundsAlignment hba,
                                  final long vbaShift)
  {
    final String value = c.getContent();
    final StrictBounds abounds = hba.align(c.getBounds());
    abounds.setRect(abounds.getX(), abounds.getY() + vbaShift,
        abounds.getWidth(), abounds.getHeight());
    setInternalOperationBounds(abounds);
    printText(value);
  }

  protected abstract void printText (String text);

  protected void updateFont (final FontDefinition f)
    throws OutputTargetException
  {
    if (f == null)
    {
      throw new NullPointerException("Font must not be null.");
    }
    if (f.equals(getFont()) == false)
    {
      setFont(f);
    }
  }

  protected abstract void setFont (FontDefinition f) throws OutputTargetException;
  protected abstract FontDefinition getFont ();

  protected void updatePaint (final Paint paint)
  {
    if (paint.equals(getPaint()) == false)
    {
      setPaint(paint);
    }
  }

  protected abstract void setPaint (Paint p);
  protected abstract Paint getPaint ();
  protected abstract boolean isPaintSupported (Paint p);


  protected void updateStroke(final Stroke stroke)
      throws OutputTargetException
  {
    if (stroke == null)
    {
      throw new NullPointerException("Stroke must not be null.");
    }
    if (stroke.equals(getStroke()) == false)
    {
      setStroke(stroke);
    }
  }

  protected abstract void setStroke (Stroke s)
      throws OutputTargetException;
  protected abstract Stroke getStroke ();
  protected abstract void drawShape (Shape s);
  protected abstract void fillShape (Shape s);

  protected abstract void drawDrawable (DrawableContent d)
      throws OutputTargetException;
  protected abstract void drawImage(ImageContent content)
      throws OutputTargetException;
}
