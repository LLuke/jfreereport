package org.jfree.report;

import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.jfree.report.util.PageFormatFactory;
import org.jfree.report.util.SerializerHelper;

public class CustomPageDefinition implements PageDefinition
{
  private transient ArrayList pageBoundsList;
  private transient ArrayList pageFormatList;
  private float width;
  private float height;

  public CustomPageDefinition ()
  {
    pageBoundsList = new ArrayList();
    pageFormatList = new ArrayList();
  }

  public void addPageFormat (final PageFormat format, final float x, final float y)
  {
    width = Math.max(width, (float)(format.getImageableWidth() + x));
    height = Math.max(height, (float)(format.getImageableHeight() + y));
    final Rectangle2D bounds = new Rectangle2D.Double
            ( x, y,
             format.getImageableWidth(),
             format.getImageableHeight());
    pageBoundsList.add(bounds);
    pageFormatList.add(format.clone());
  }

  public int getPageCount ()
  {
    return pageBoundsList.size();
  }

  /**
   * Returns the page format for the given page number. The page format contains local
   * coordinates - that means that the point (0,0) denotes the upper left corner of this
   * returned page format and not global coordinates.
   *
   * @param pos the position of the pageformat within the page
   * @return the given pageformat.
   */
  public PageFormat getPageFormat (final int pos)
  {
    final PageFormat fmt = (PageFormat) pageFormatList.get(pos);
    return (PageFormat) fmt.clone();
  }

  /**
   * Describes the internal position of the given page within the logical page. The
   * logical page does not include any page margins, the printable area for a page starts
   * at (0,0).
   *
   * @param pos
   * @return
   */
  public Rectangle2D getPagePosition (final int pos)
  {
    final Rectangle2D rec = (Rectangle2D) pageBoundsList.get(pos);
    return rec.getBounds2D();
  }

  /**
   * Returns all page positions as array.
   *
   * @return the collected page positions
   *
   * @see PageDefinition#getPagePosition(int)
   */
  public Rectangle2D[] getPagePositions ()
  {
    final Rectangle2D[] rects =
            new Rectangle2D[pageBoundsList.size()];
    for (int i = 0; i < pageBoundsList.size(); i++)
    {
      final Rectangle2D rec = (Rectangle2D) pageBoundsList.get(i);
      rects[i] = rec.getBounds2D();
    }
    return rects;
  }

  public float getWidth ()
  {
    return width;
  }

  public float getHeight ()
  {
    return height;
  }

  public Object clone ()
          throws CloneNotSupportedException
  {
    final CustomPageDefinition def = (CustomPageDefinition) super.clone();
    def.pageBoundsList = (ArrayList) pageBoundsList.clone();
    def.pageFormatList = (ArrayList) pageFormatList.clone();
    return def;
  }


  /**
   * deserizalize the report and restore the pageformat.
   *
   * @param out the objectoutput stream
   * @throws java.io.IOException if errors occur
   */
  private void writeObject(final ObjectOutputStream out)
      throws IOException
  {
    out.defaultWriteObject();
    final SerializerHelper instance = SerializerHelper.getInstance();
    final Iterator pageBoundsIterator = pageBoundsList.iterator();
    while (pageBoundsIterator.hasNext())
    {
      instance.writeObject(pageBoundsIterator.next(), out);
    }
    instance.writeObject(null, out);
    final Iterator pageFormatIterator = pageFormatList.iterator();
    while (pageFormatIterator.hasNext())
    {
      instance.writeObject(pageFormatIterator.next(), out);
    }
    instance.writeObject(null, out);
  }

  /**
   * resolve the pageformat, as PageFormat is not serializable.
   *
   * @param in  the input stream.
   *
   * @throws java.io.IOException if there is an IO problem.
   * @throws ClassNotFoundException if there is a class problem.
   */
  private void readObject(final ObjectInputStream in)
      throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    final SerializerHelper instance = SerializerHelper.getInstance();
    pageBoundsList = new ArrayList();
    pageFormatList = new ArrayList();

    Object o = instance.readObject(in);
    while (o != null)
    {
      final Rectangle2D rect = (Rectangle2D) o;
      pageBoundsList.add(rect);
      o = instance.readObject(in);
    }

    o = instance.readObject(in);
    while (o != null)
    {
      final PageFormat format = (PageFormat) o;
      pageFormatList.add(format);
      o = instance.readObject(in);
    }
  }

  public boolean equals (final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof CustomPageDefinition))
    {
      return false;
    }

    final CustomPageDefinition customPageDefinition = (CustomPageDefinition) o;

    if (height != customPageDefinition.height)
    {
      return false;
    }
    if (width != customPageDefinition.width)
    {
      return false;
    }
    if (!pageBoundsList.equals(customPageDefinition.pageBoundsList))
    {
      return false;
    }

    if (pageFormatList.size() != customPageDefinition.pageFormatList.size())
    {
      return false;
    }

    for (int i = 0; i < pageFormatList.size(); i++)
    {
      final PageFormat pf = (PageFormat) pageFormatList.get(i);
      final PageFormat cpf = (PageFormat) customPageDefinition.pageFormatList.get(i);
      if (PageFormatFactory.isEqual(pf, cpf) == false)
      {
        return false;
      }
    }

    return true;
  }

  public int hashCode ()
  {
    int result;
    result = pageBoundsList.hashCode();
    result = 29 * result + pageFormatList.hashCode();
    result = 29 * result + width != +0.0f ? Float.floatToIntBits(width) : 0;
    result = 29 * result + height != +0.0f ? Float.floatToIntBits(height) : 0;
    return result;
  }
}
