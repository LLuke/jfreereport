package org.jfree.report.modules.output.pageable.plaintext;

import java.awt.print.Paper;
import java.io.IOException;
import java.io.OutputStream;

import org.jfree.report.style.FontDefinition;
import org.jfree.report.util.PageFormatFactory;

public abstract class AbstractEpsonPrinterDriver implements PrinterDriver
{
  protected static class DriverState
  {
    private boolean bold;
    private boolean underline;
    private boolean italic;
    private boolean strikethrough;

    private byte font;
    private int manualLeftBorder;

    public DriverState ()
    {
    }

    public boolean isBold ()
    {
      return bold;
    }

    public void setBold (final boolean bold)
    {
      this.bold = bold;
    }

    public boolean isItalic ()
    {
      return italic;
    }

    public void setItalic (final boolean italic)
    {
      this.italic = italic;
    }

    public boolean isUnderline ()
    {
      return underline;
    }

    public void setUnderline (final boolean underline)
    {
      this.underline = underline;
    }

    public byte getFont ()
    {
      return font;
    }

    public void setFont (final byte font)
    {
      this.font = font;
    }

    public int getManualLeftBorder ()
    {
      return manualLeftBorder;
    }

    public void setManualLeftBorder (final int manualLeftBorder)
    {
      this.manualLeftBorder = manualLeftBorder;
    }

    public boolean isStrikethrough ()
    {
      return strikethrough;
    }

    public void setStrikethrough (final boolean strikethrough)
    {
      this.strikethrough = strikethrough;
    }

    public void reset ()
    {
      this.bold = false;
      this.italic = false;
      this.strikethrough = false;
      this.underline = false;
      this.font = PrinterDriver.SELECT_FONT_ROMAN;
      this.manualLeftBorder = 0;
    }
  }

  public static final String FONT_15_CPI = "Epson.Font-15CPI-available";

  private PrinterSpecification printerSpecification;
  private FontMapper fontMapper;

  private OutputStream out;
  private float charsPerInch;
  private float linesPerInch;
  private EncodingUtilities encodingUtilities;
  private boolean firstPage;

  private int borderTop;
  private int borderBottom;
  private DriverState driverState;

  protected AbstractEpsonPrinterDriver (final OutputStream out,
                                        final float charsPerInch, final float linesPerInch,
                                        final String printerModel)
  {
    this.out = out;
    this.charsPerInch = charsPerInch;
    this.linesPerInch = linesPerInch;
    this.printerSpecification = lookupPrinterSpecification(printerModel);
    this.fontMapper = new DefaultFontMapper();
    this.firstPage = true;
    this.driverState = new DriverState();

    //validate the CPI values
    if (isValidCPI(charsPerInch) == false)
    {
      throw new IllegalArgumentException
              ("The given CPI of '" + charsPerInch +
              "' is invalid for the selected printer model ('" + printerModel + "'.");
    }
    // we cannot influence the LPI, so we have to accept what the user gives in
  }

  private boolean isValidCPI (final float charsPerInch)
  {
    if (charsPerInch == CPI_10)
    {
      return true;
    }
    if (charsPerInch == CPI_12)
    {
      return true;
    }
    if (charsPerInch == CPI_17)
    {
      return true;
    }
    if (charsPerInch == CPI_20)
    {
      return true;
    }
    if (charsPerInch == CPI_15 &&
            getPrinterSpecification().isFeatureAvailable(FONT_15_CPI))
    {
      return true;
    }
    return false;
  }


  public DriverState getDriverState ()
  {
    return driverState;
  }

  public FontMapper getFontMapper ()
  {
    return fontMapper;
  }

  public void setFontMapper (final FontMapper fontMapper)
  {
    this.fontMapper = fontMapper;
  }

  protected OutputStream getOut ()
  {
    return out;
  }

  protected boolean isFirstPage ()
  {
    return firstPage;
  }

  public PrinterSpecification getPrinterSpecification ()
  {
    return printerSpecification;
  }

  /**
   * Ends a new line.
   *
   * @param overflow
   * @throws java.io.IOException if an IOError occures.
   */
  public void endLine (final boolean overflow)
          throws IOException
  {
    if (overflow == false)
    {
      out.write(PrinterDriver.CARRIAGE_RETURN);
      out.write(PrinterDriver.LINE_FEED);
    }
  }

  /**
   * Ends the current page. Should print empty lines or an FORM_FEED command.
   *
   * @param overflow
   * @throws java.io.IOException if there was an IOError while writing the command
   */
  public void endPage (final boolean overflow)
          throws IOException
  {
    if (overflow == false)
    {
      out.write(PrinterDriver.FORM_FEED);
    }
  }

  /**
   * Gets the default character width in CPI.
   *
   * @return the default character width in CPI.
   */
  public float getCharactersPerInch ()
  {
    return charsPerInch;
  }

  /**
   * Gets the default line height.
   *
   * @return the default line height.
   */
  public float getLinesPerInch ()
  {
    return linesPerInch;
  }

  /**
   * Flushes the output stream.
   *
   * @throws java.io.IOException if an IOError occured.
   */
  public void flush ()
          throws IOException
  {
    out.flush();
  }

  /**
   * Prints a single text chunk at the given position on the current line. The chunk
   * should not be printed, if an previous chunk overlays this chunk.
   *
   * @param chunk the chunk that should be written
   * @throws java.io.IOException if an IO error occured.
   */
  public void printChunk (final PlaintextDataChunk chunk)
          throws IOException
  {
    final String encoding = chunk.getEncoding();
    final String text = chunk.getText().substring(0, chunk.getWidth());
    final FontDefinition fd = chunk.getFont();

    sendDefineFont(fontMapper.getPrinterFont(fd));
    sendFontStyle(fd.isBold(), fd.isItalic(), fd.isUnderline(), fd.isStrikeThrough());
    getEncodingUtilities(encoding).writeEncodedText(text, out);
  }

  protected abstract void sendFontStyle (boolean bold, boolean italic,
                                         boolean underline, boolean strikeTrough)
          throws IOException;

  protected void sendDefineFont (final byte printerFont)
          throws IOException
  {
    if (getDriverState().getFont() != printerFont)
    {
      getOut().write(0x1b);
      getOut().write(0x6b);
      getOut().write(printerFont);
      getDriverState().setFont(printerFont);
    }
  }

  /**
   * Prints an empty chunk. This is called for all undefined chunk-cells. The last defined
   * font is used to print that empty text.
   *
   * @throws java.io.IOException if an IOError occured.
   */
  public void printEmptyChunk (final int count)
          throws IOException
  {
    for (int i = 0; i < count; i++)
    {
      out.write(PrinterDriver.SPACE);
    }
  }

  /**
   * Prints some raw content. This content is not processed in any way, so be very
   * carefull.
   *
   * @param raw the content that should be printed.
   */
  public void printRaw (final byte[] raw)
          throws IOException
  {
    out.write(raw);
  }

  /**
   * Starts a new line.
   *
   * @throws java.io.IOException if an IOError occures.
   */
  public void startLine ()
          throws IOException
  {
    final int manualLeftBorder = getDriverState().getManualLeftBorder();
    for (int i = 0; i < manualLeftBorder; i++)
    {
      out.write(PrinterDriver.SPACE);
    }
  }

  /**
   * Resets the printer and starts a new page. Prints the top border lines (if
   * necessary).
   *
   * @throws java.io.IOException if there was an IOError while writing the command
   */
  public void startPage (final Paper paper, final String encoding)
          throws IOException
  {
    final float lineHeightPoints = 72f / getLinesPerInch();
    final float charWidthPoints = 72f / getCharactersPerInch();

    // Quoted from the Epson Reference Manual page R-4:

    // 1. Send an ESC @ to initialize the printer
    sendResetPrinter();
    driverState.reset();

    // 2. Set the unit of line spacing to the minimum vertical increment necessary
    sendDefineLineSpacing(lineHeightPoints);

    // 3. Set the printing area
    final int lines = (int) ((paper.getHeight() / 72f) * getLinesPerInch());
    sendDefinePageLengthInLines(lines);

    sendDefineCharacterWidth(getCharactersPerInch());

    final PageFormatFactory fact = PageFormatFactory.getInstance();
    final int borderLeft = (int) (fact.getLeftBorder(paper) / charWidthPoints);
    final int borderRight = (int) (fact.getRightBorder(paper) / charWidthPoints);
    sendDefineHorizontalBorders(borderLeft, borderRight);

    borderTop = (int) (fact.getTopBorder(paper) / lineHeightPoints);
    borderBottom = (int) (fact.getBottomBorder(paper) / lineHeightPoints);

    // print the top margin ..
    for (int i = 0; i < borderTop; i++)
    {
      startLine();
      endLine(false);
    }

    // 4. Assign character tables to each of the four active tables as
    // necessary. (ESC/P2 printers only)
    //
    // this is done before we start printing the text (and may change mid-page).
    // sendDefineCodepage(encoding);

    // 5. Define any user-defined characters.
    //
    // this is done before we start printing the text (and may change mid-page).
    // sendDefineUserCharacters();
  }

  private void sendDefineCharacterWidth (final float charactersPerInch)
          throws IOException
  {
    if (charactersPerInch == CPI_10)
    {
      getOut().write(0x12); // disable condensed printing
      getOut().write(0x1b);
      getOut().write(0x50); // select 10 CPI
    }
    else if (charactersPerInch == CPI_12)
    {
      getOut().write(0x12); // disable condensed printing
      getOut().write(0x1b);
      getOut().write(0x4d); // select 12 CPI
    }
    else if (charactersPerInch == CPI_15)
    {
      // All ESC/P2 and 24Pin ESC/P printers support that mode
      // Additionally, the 9Pin printer models FX-2170 and DFX-5000+
      // support that character width.
      getOut().write(0x12); // disable condensed printing
      getOut().write(0x1b);
      getOut().write(0x67);
    }
    else if (charactersPerInch == CPI_17)
    {
      getOut().write(0x0f); // enable condensed printing
      getOut().write(0x1b);
      getOut().write(0x50); // select 10 CPI (-> 17.14 cpi because of condensed printing)
    }
    else if (charactersPerInch == CPI_20)
    {
      getOut().write(0x0f); // enable condensed printing
      getOut().write(0x1b);
      getOut().write(0x4d); // select 12 CPI (-> 20 cpi because of condensed printing)
    }
    else
    {
      throw new IllegalArgumentException("The given character width is invalid");
    }
  }


  protected void sendResetPrinter ()
          throws IOException
  {
    out.write(0x1b);
    out.write(0x40);
  }

  protected abstract void sendDefineLineSpacing (float lineHeightInPoints)
          throws IOException;

  protected void sendDefinePageLengthInLines (final int paperSizeInLines)
          throws IOException
  {
    // SideEffects: Setting the page size will mark the current position
    //              as TopOfForm position
    //
    // All printers support that command.
    getOut().write(0x1b); // ESC
    getOut().write(0x67); // C
    getOut().write(paperSizeInLines);
  }

  protected void sendDefineHorizontalBorders (final int left, final int right)
          throws IOException
  {
    if (left < 256)
    {
      // depends on the pitch to be defined correctly.
      // In that implementation we can assume that this is ok.
      getOut().write(0x1b); // ESC
      getOut().write(0x6c); // l
      getOut().write(left);
      getDriverState().setManualLeftBorder(0);
    }
    else
    {
      getOut().write(0x1b); // ESC
      getOut().write(0x6c); // l
      getOut().write(255);
      getDriverState().setManualLeftBorder(left - 255);
    }
    // Compatibility: All printers support that command.

    // don't care about the right border, the PlainTextPage makes sure that
    // we do not violate that constraint.
  }


  protected void sendDefineCodepage (final String encoding, final int characterTable)
          throws IOException
  {
    final byte[] cp = getPrinterSpecification().getEncoding(encoding).getCode();
    out.write(0x1b); // ESC
    out.write(0x28); // (
    out.write(0x74); // t
    out.write(0x03); // const: 3
    out.write(0x00); // const: 0
    out.write(characterTable); // Define charset; (0 works on all printers)
    out.write(cp);   // the codepage
    out.write(0x1b); // ESC
    out.write(0x74); // t
    out.write(0x00); // Select charset 0 (works on all printers)
  }

  protected void sendDefineUserCharacters ()
  {
  }

  protected int getSelectedCharacterTable ()
  {
    return 0;
  }

  protected EncodingUtilities getEncodingUtilities (final String encoding)
          throws IOException
  {
    if (encodingUtilities != null &&
            encodingUtilities.getEncoding().equals(encoding))
    {
      return encodingUtilities;
    }

    encodingUtilities = new EncodingUtilities(encoding);
    sendDefineCodepage(encoding, 0);
    return encodingUtilities;
  }

  protected abstract PrinterSpecificationManager getPrinterSpecificationManager ();

  private PrinterSpecification lookupPrinterSpecification (final String model)
  {
    final PrinterSpecificationManager printerSpecificationManager =
            getPrinterSpecificationManager();
    if (model == null)
    {
      return PrinterSpecificationManager.getGenericPrinter();
    }

    final PrinterSpecification printerModel =
            printerSpecificationManager.getPrinter(model);
    if (printerModel == null)
    {
      throw new IllegalArgumentException("The printer model is not supported.");
    }
    return printerModel;
  }


}
