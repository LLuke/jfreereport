package org.jfree.report;

import java.util.ArrayList;
import java.awt.print.PageFormat;
import java.awt.geom.Rectangle2D;

public class CustomPageDefinition implements PageDefinition
{
  private ArrayList pageBoundsList;
  private ArrayList pageFormatList;
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
    final Rectangle2D bounds = new Rectangle2D.Float
            ( x, y,
             (float) format.getImageableWidth(),
             (float) format.getImageableHeight());
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
            new Rectangle2D.Float[pageBoundsList.size()];
    for (int i = 0; i < pageBoundsList.size(); i++)
    {
      final Rectangle2D rec = (Rectangle2D) pageBoundsList.get(i);
      rects[i] = rec.getBounds2D();
    }
    return rects;
  }

  public float getWidth ()
  {
    // todo implement me
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
}
