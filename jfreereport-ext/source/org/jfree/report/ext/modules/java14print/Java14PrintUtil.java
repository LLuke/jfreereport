package org.jfree.report.ext.modules.java14print;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.Size2DSyntax;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaName;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PageRanges;

import org.jfree.report.JFreeReport;
import org.jfree.report.PageDefinition;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.modules.gui.base.ReportPane;
import org.jfree.report.modules.gui.print.PrintUtil;
import org.jfree.util.Log;

/**
 * Creation-Date: 05.09.2005, 19:11:47
 *
 * @author: Thomas Morgner
 */
public class Java14PrintUtil
{
  public static final int CONFIGURATION_VALID = 0;
  public static final int CONFIGURATION_REPAGINATE = 1;
  public static final int CONFIGURATION_SHOW_DIALOG = 2;

  private Java14PrintUtil()
  {
  }

  /**
   * This tests, whether the given attribute set defines the same page
   * properties as the given JFreeReport object.
   * <p/>
   * While showing the print dialog, the user has the chance to alter the page
   * format of the print job. When that happens, we have to repaginate the whole
   * report, which may render the users page range input invalid. In that case,
   * we will have to redisplay the dialog.
   *
   * @param attributes
   * @param report
   * @return
   */
  public static int isValidConfiguration(final PrintRequestAttributeSet attributes,
                                         final JFreeReport report)
  {
    final PrintRequestAttributeSet reportAttributes =
            copyConfiguration(null, report);
    // now, compare that minimal set with the given attribute collection.

    final Attribute[] printAttribs = reportAttributes.toArray();
    boolean invalidConfig = false;
    for (int i = 0; i < printAttribs.length; i++)
    {
      Attribute attrib = printAttribs[i];
      if (attributes.containsValue(attrib) == false)
      {
        invalidConfig = true;
        break;
      }
    }

    if (invalidConfig == false)
    {
      return CONFIGURATION_VALID;
    }
    if (attributes.containsKey(PageRanges.class))
    {
      return CONFIGURATION_SHOW_DIALOG;
    }
    return CONFIGURATION_REPAGINATE;
  }

  /**
   * This method replaces the media definition from the given attribute set with
   * the one found in the report itself.
   * <p/>
   * If no JobName is set, a default jobname will be assigned.
   *
   * @param attributes
   * @param report
   * @return
   */
  public static PrintRequestAttributeSet copyConfiguration
          (PrintRequestAttributeSet attributes,
           final JFreeReport report)
  {
    if (attributes == null)
    {
      attributes = new HashPrintRequestAttributeSet();
    }

    // for now, be lazy, assume that the first page is the reference
    final PageDefinition pdef = report.getPageDefinition();
    final PageFormat format = pdef.getPageFormat(0);
    final Paper paper = format.getPaper();

    final Media media = MediaSize.findMedia((float) (paper.getWidth() / 72d),
            (float) (paper.getHeight() / 72d), Size2DSyntax.INCH);
    attributes.add(media);

    final MediaPrintableArea printableArea = new MediaPrintableArea
            ((float) (paper.getImageableX() / 72d),
                    (float) (paper.getImageableY() / 72),
                    (float) (paper.getImageableWidth() / 72),
                    (float) (paper.getImageableHeight() / 72),
                    Size2DSyntax.INCH);

    attributes.add(printableArea);
    attributes.add(mapOrientation(format.getOrientation()));

    return attributes;
  }

  public static PrintRequestAttributeSet copyAuxillaryAttributes
          (PrintRequestAttributeSet attributes,
           final JFreeReport report)
  {
    if (attributes == null)
    {
      attributes = new HashPrintRequestAttributeSet();
    }

    if (attributes.containsKey(JobName.class) == false)
    {
      final String jobName = report.getReportConfiguration().getConfigProperty
              (PrintUtil.PRINTER_JOB_NAME_KEY, report.getName());
      attributes.add(new JobName(jobName, null));
    }

    return attributes;
  }

  public static PageFormat extractPageFormat(final PrintRequestAttributeSet attributeSet)
  {
    final Media media = (Media) attributeSet.get(Media.class);
    final MediaPrintableArea printableArea =
            (MediaPrintableArea) attributeSet.get(MediaPrintableArea.class);
    final OrientationRequested orientationRequested =
            (OrientationRequested) attributeSet.get(OrientationRequested.class);

    MediaSize mediaSize = lookupMediaSize(media);
    if (mediaSize == null)
    {
      Log.warn("Unknown media encountered, unable to compute page sizes.");
    }

    final PageFormat pageFormat = new PageFormat();
    pageFormat.setPaper(createPaper(mediaSize, printableArea));
    if (OrientationRequested.PORTRAIT.equals(orientationRequested))
    {
      pageFormat.setOrientation(PageFormat.PORTRAIT);
    }
    else if (OrientationRequested.LANDSCAPE.equals(orientationRequested))
    {
      pageFormat.setOrientation(PageFormat.LANDSCAPE);
    }
    else if (OrientationRequested.REVERSE_LANDSCAPE.equals
            (orientationRequested))
    {
      pageFormat.setOrientation(PageFormat.REVERSE_LANDSCAPE);
    }
    else if (OrientationRequested.REVERSE_PORTRAIT.equals(orientationRequested))
    {
      pageFormat.setOrientation(PageFormat.PORTRAIT);
    }
    return pageFormat;
  }

  private static Paper createPaper(final MediaSize mediaSize,
                                   final MediaPrintableArea printableArea)
  {
    final Paper paper = new Paper();
    if (mediaSize != null)
    {
      paper.setSize(mediaSize.getX(Size2DSyntax.INCH) * 72d,
              mediaSize.getY(Size2DSyntax.INCH) * 72d);
    }
    if (printableArea != null)
    {
      paper.setImageableArea
              (printableArea.getX(Size2DSyntax.INCH) * 72d,
                      printableArea.getY(Size2DSyntax.INCH) * 72d,
                      printableArea.getWidth(Size2DSyntax.INCH) * 72d,
                      printableArea.getHeight(Size2DSyntax.INCH) * 72d);
    }
    return paper;
  }

  private static MediaSize lookupMediaSize(final Media media)
  {

    if (media instanceof MediaSizeName)
    {
      return MediaSize.getMediaSizeForName((MediaSizeName) media);
    }
    else if (media instanceof MediaName)
    {
      if (media.equals(MediaName.ISO_A4_TRANSPARENT) ||
              media.equals(MediaName.ISO_A4_WHITE))
      {
        return MediaSize.getMediaSizeForName(MediaSizeName.ISO_A4);
      }
      else if (media.equals(MediaName.NA_LETTER_TRANSPARENT) ||
              media.equals(MediaName.NA_LETTER_WHITE))
      {
        return MediaSize.getMediaSizeForName(MediaSizeName.NA_LETTER);
      }
    }
    return null;
  }

  private static OrientationRequested mapOrientation(final int orientation)
  {
    switch (orientation)
    {
      case PageFormat.LANDSCAPE:
        return OrientationRequested.LANDSCAPE;
      case PageFormat.REVERSE_LANDSCAPE:
        return OrientationRequested.REVERSE_LANDSCAPE;
      case PageFormat.PORTRAIT:
        return OrientationRequested.PORTRAIT;
      default:
        throw new IllegalArgumentException
                ("The given value is no valid PageFormat orientation.");
    }
  }

  public static void printDirectly (final JFreeReport report,
                                    PrintService printService)
          throws PrintException, ReportProcessingException
  {
    // with that method we do not use the PrintService UI ..
    // it is up to the user to supply a valid print service that
    // supports the Pageable printing.
    if (printService == null)
    {
      PrintService[] services = PrintServiceLookup.lookupPrintServices(
                                 DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);
      if (services.length == 0)
      {
        throw new PrintException
                ("Unable to find a matching print service implementation.");
      }
      printService = services[0];
    }
    else
    {
      if (printService.isDocFlavorSupported(DocFlavor.SERVICE_FORMATTED.PAGEABLE) == false)
      {
        throw new PrintException
                ("The print service implementation does not support the Pageable Flavor.");
      }
    }

    PrintRequestAttributeSet attributes =
            Java14PrintUtil.copyConfiguration(null, report);
    attributes = Java14PrintUtil.copyAuxillaryAttributes(attributes, report);

    ReportPane reportPane = new ReportPane(report);
    DocPrintJob job = printService.createPrintJob();
    SimpleDoc document = new SimpleDoc
      (reportPane, DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);

    job.print(document, attributes);

  }

  public static boolean print (final JFreeReport report)
          throws PrintException, ReportProcessingException
  {
    PrintService[] services = PrintServiceLookup.lookupPrintServices(
                               DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);
    if (services.length == 0)
    {
      throw new PrintException
              ("Unable to find a matching print service implementation.");
    }
    PrintRequestAttributeSet attributes =
            Java14PrintUtil.copyConfiguration(null, report);
    attributes = Java14PrintUtil.copyAuxillaryAttributes(attributes, report);

    PrintService service = ServiceUI.printDialog
        (null, 50, 50, services, services[0],
            DocFlavor.SERVICE_FORMATTED.PAGEABLE, attributes);
    if (service == null)
    {
      return false;
    }

    ReportPane reportPane = new ReportPane(report);
    DocPrintJob job = service.createPrintJob();
    SimpleDoc document = new SimpleDoc
      (reportPane, DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);

    job.print(document, attributes);
    return true;
  }
}
