package org.jfree.report.ext.junit.base.functionality;

import junit.framework.TestCase;
import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.Band;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.report.content.ContentFactory;
import org.jfree.report.content.DefaultContentFactory;
import org.jfree.report.content.TextContentFactoryModule;
import org.jfree.report.style.FontDefinition;
import org.jfree.report.layout.BandLayoutManagerUtil;
import org.jfree.report.layout.LayoutSupport;
import org.jfree.report.layout.SizeCalculator;
import org.jfree.report.layout.SizeCalculatorException;
import org.jfree.report.layout.LayoutManagerCache;
import org.jfree.ui.FloatDimension;

public class AlignLayoutTest extends TestCase
{
  private static final long ALIGNMENT_BORDER = 5020;
  private static final float ALIGNMENT = 5.020f;

  private static class SimpleLayoutSupport implements LayoutSupport, SizeCalculator
  {
    private DefaultContentFactory contentFactory;
    private LayoutManagerCache cache;

    public SimpleLayoutSupport ()
    {
      contentFactory = new DefaultContentFactory();
      contentFactory.addModule(new TextContentFactoryModule());
      cache = new LayoutManagerCache();
    }

    public LayoutManagerCache getCache ()
    {
      return cache;
    }

    /**
     * Returns the line height.  This includes the font's ascent, descent and leading.
     *
     * @return the line height.
     */
    public float getLineHeight ()
    {
      return 12;
    }

    /**
     * Calculates the width of a <code>String<code> in the current <code>Graphics</code>
     * context.
     *
     * @param text         the text.
     * @param lineStartPos the start position of the substring to be measured.
     * @param endPos       the position of the last character to be measured.
     * @return the width of the string in Java2D units.
     */
    public float getStringWidth (final String text, final int lineStartPos,
                                 final int endPos)
    {
      return (endPos - lineStartPos) * 10;
    }

    /**
     * Creates a size calculator for the current state of the output target.  The calculator
     * is used to calculate the string width and line height and later maybe more...
     *
     * @param font the font.
     * @return the size calculator.
     *
     * @throws org.jfree.report.layout.SizeCalculatorException
     *          if there is a problem with the output target.
     */
    public SizeCalculator createTextSizeCalculator (final FontDefinition font)
            throws SizeCalculatorException
    {
      return this;
    }

    /**
     * Returns the assigned content factory for the target.
     *
     * @return the content factory.
     */
    public ContentFactory getContentFactory ()
    {
      return contentFactory;
    }

    /**
     * Returns the element alignment. Elements will be layouted aligned to this border, so
     * that <code>mod(X, horizontalAlignment) == 0</code> and <code>mod(Y,
     * verticalAlignment) == 0</code>. Returning 0 will disable the alignment.
     *
     * @return the vertical alignment grid boundry
     */
    public float getHorizontalAlignmentBorder ()
    {
      return ALIGNMENT;
    }

    /**
     * Returns the element alignment. Elements will be layouted aligned to this border, so
     * that <code>mod(X, horizontalAlignment) == 0</code> and <code>mod(Y,
     * verticalAlignment) == 0</code>. Returning 0 will disable the alignment.
     * <p/>
     * Q&D Hack: Save some cycles of processor time by computing that thing only once.
     *
     * @return the vertical alignment grid boundry
     */
    public long getInternalHorizontalAlignmentBorder ()
    {
      return ALIGNMENT_BORDER;
    }

    /**
     * Returns the element alignment. Elements will be layouted aligned to this border, so
     * that <code>mod(X, horizontalAlignment) == 0</code> and <code>mod(Y,
     * verticalAlignment) == 0</code>. Returning 0 will disable the alignment.
     * <p/>
     * Q&D Hack: Save some cycles of processor time by computing that thing only once.
     *
     * @return the vertical alignment grid boundry
     */
    public long getInternalVerticalAlignmentBorder ()
    {
      return ALIGNMENT_BORDER;
    }

    /**
     * Returns the element alignment. Elements will be layouted aligned to this border, so
     * that <code>mod(X, horizontalAlignment) == 0</code> and <code>mod(Y,
     * verticalAlignment) == 0</code>. Returning 0 will disable the alignment.
     *
     * @return the vertical alignment grid boundry
     */
    public float getVerticalAlignmentBorder ()
    {
      return ALIGNMENT;
    }
  }

  public AlignLayoutTest (final String s)
  {
    super(s);
  }

  public void testAlignedLayout ()
  {
    final LabelElementFactory le = new LabelElementFactory();
    le.setMinimumSize(new FloatDimension(100, 15));
    le.setText("Johnny hates Jazz");

    final Band band = new Band();
    band.addElement(le.createElement());

    final StrictBounds b = BandLayoutManagerUtil.doLayout
            (band, new SimpleLayoutSupport(), 1234000, 1234000);
    assertAligned(b);
  }

  public void assertAligned (final StrictBounds bounds)
  {
    assertEquals(bounds.getWidth(), ((bounds.getWidth() / ALIGNMENT_BORDER) * ALIGNMENT_BORDER));
    assertEquals(bounds.getHeight(), ((bounds.getHeight() / ALIGNMENT_BORDER) * ALIGNMENT_BORDER));
    assertEquals(bounds.getX(), ((bounds.getX() / ALIGNMENT_BORDER) * ALIGNMENT_BORDER));
    assertEquals(bounds.getY(), ((bounds.getY() / ALIGNMENT_BORDER) * ALIGNMENT_BORDER));

  }
}
