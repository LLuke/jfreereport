package org.jfree.report.ext.junit.base.functionality;

import javax.swing.table.DefaultTableModel;

import junit.framework.TestCase;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.modules.output.pageable.base.OutputTargetException;
import org.jfree.report.modules.output.pageable.base.PageableReportProcessor;
import org.jfree.report.util.geom.StrictBounds;
import org.jfree.ui.FloatDimension;
import org.jfree.util.Log;

public class AlignmentTest extends TestCase
{
  private class VerifyOutputTarget extends DebugOutputTarget
  {
    private int counter;
    private static final long STRICT_ALIGN = 5020;

    /**
     * Creates a new output target.  Both the logical page size and the physical page size
     * will be the same.
     */
    public VerifyOutputTarget ()
    {
      super(STRICT_ALIGN / 1000f, STRICT_ALIGN / 1000f);
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
      return STRICT_ALIGN;
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
      return STRICT_ALIGN;
    }

    /**
     * Opens the target.
     *
     * @throws org.jfree.report.modules.output.pageable.base.OutputTargetException
     *          if there is some problem opening the target.
     */
    public void open ()
            throws OutputTargetException
    {
      super.open();
      counter = 0;
    }

    /**
     * Draws a string at the current cursor position.
     *
     * @param text the text.
     */
    public void printText (final String text)
    {
      if (text.equals("Johnny hates Jazz"))
      {
        counter += 1;
      }
      final StrictBounds sb = getInternalOperationBounds();
      final StrictBounds pb = getInternalPageBounds();

      final long x = sb.getX() - pb.getX();
      final long y = sb.getY() - pb.getY();

      Log.debug (sb);

//      assertEquals(sb.getWidth(), ((sb.getWidth() / STRICT_ALIGN) * STRICT_ALIGN));
//      assertEquals(sb.getHeight(), ((sb.getHeight() / STRICT_ALIGN) * STRICT_ALIGN));
      assertEquals(x, ((x / STRICT_ALIGN) * STRICT_ALIGN));
      assertEquals(y, ((y / STRICT_ALIGN) * STRICT_ALIGN));

    }

    /**
     * Closes the target.
     */
    public void close ()
    {
      assertEquals(14, counter);
    }
  }
  public AlignmentTest (final String s)
  {
    super(s);
    JFreeReportBoot.getInstance().start();
  }

  public void testAlignedRootBands ()
          throws ReportProcessingException, OutputTargetException
  {
    final LabelElementFactory le = new LabelElementFactory();
    le.setMinimumSize(new FloatDimension(100, 15));
    le.setText("Johnny hates Jazz");

    final JFreeReport report = new JFreeReport();
    report.getPageFooter().addElement(le.createElement());
    report.getPageHeader().addElement(le.createElement());
    report.getReportHeader().addElement(le.createElement());
    report.getReportFooter().addElement(le.createElement());
    report.getItemBand().addElement(le.createElement());

    report.setData(new DefaultTableModel (10, 10));

    final PageableReportProcessor pr = new PageableReportProcessor(report);
    pr.setOutputTarget(new VerifyOutputTarget());
    pr.getOutputTarget().open();
    pr.processReport();
    pr.getOutputTarget().close();
  }
}
