package gnu.bhresearch.pixie.wmf.records;

import gnu.bhresearch.pixie.wmf.MfRecord;
import gnu.bhresearch.pixie.wmf.WmfFile;

import java.awt.Rectangle;
import java.util.Hashtable;

/**
 * CHORD is not implemented.
 */
public abstract class MfCmd
{
  /** Writer function */
  //public abstract MfRecord getRecord ();

  /** reader function */
  public abstract void setRecord (MfRecord record);

  public abstract int getFunction ();

  public abstract MfCmd getInstance ();

  // to be abstract
  public abstract void replay (WmfFile metafile);

  private static Hashtable recordTypes;
  private static boolean isInitialized;

  private float scaleX;
  private float scaleY;

  public MfCmd ()
  {
    scaleX = 1;
    scaleY = 1;
  }

  public void setScale (float scaleX, float scaleY)
  {
    float oldScaleX = this.scaleX;
    float oldScaleY = this.scaleY;
    this.scaleX = scaleX;
    this.scaleY = scaleY;
    if (oldScaleX != scaleX)
    {
      scaleXChanged ();
    }
    if (oldScaleY != scaleY)
    {
      scaleYChanged ();
    }
  }

  protected abstract void scaleXChanged ();

  protected abstract void scaleYChanged ();

  public Rectangle scaleRect (Rectangle r)
  {
    Rectangle retval = new Rectangle ();
    retval.x = getScaledX (r.x);
    retval.y = getScaledY (r.y);

    retval.width = getScaledWidth (r.width);
    retval.height = getScaledHeight (r.height);
    return retval;
  }

  public int getScaledWidth (int length)
  {
    if (length == 0)
    {
      return 1;
    }
    length = (int) (length * scaleX + 0.5f);
    return (length == 0) ? 1 : length;
  }

  public int getScaledHeight (int length)
  {
    if (length == 0)
    {
      return 1;
    }
    length = (int) (length * scaleY + 0.5f);
    return (length == 0) ? 1 : length;
  }

  public int[] applyScaleX (int[] n, int[] dest)
  {
    if (dest == null)
      dest = new int[n.length];
    else if (dest.length < n.length)
      dest = new int[n.length];

    for (int i = 0; i < n.length; i++)
    {
      dest[i] = (int) (n[i] * scaleX + 0.5f);
    }
    return dest;
  }

  public int[] applyScaleY (int[] n, int[] dest)
  {
    if (dest == null)
      dest = new int[n.length];
    else if (dest.length < n.length)
      dest = new int[n.length];

    for (int i = 0; i < n.length; i++)
    {
      dest[i] = (int) (n[i] * scaleY + 0.5f);
    }
    return dest;
  }

  /**
   * Return integer scaled to output units.
   */
  public int getScaledY (int y)
  {
    return (int) (y * scaleY + 0.5f);
  }

  /**
   * Return integer scaled to output units.
   */
  public int getScaledX (int x)
  {
    return (int) (x * scaleX + 0.5f);
  }

  public static void registerAllKnownTypes ()
  {
    if (isInitialized)
      return;

    registerCommand (new MfCmdAnimatePalette ());
    registerCommand (new MfCmdArc ());
    registerCommand (new MfCmdBitBlt ());
    registerCommand (new MfCmdChord ());
    registerCommand (new MfCmdCreateBrush ());
    registerCommand (new MfCmdCreateDibPatternBrush ());
    registerCommand (new MfCmdCreateFont ());
    registerCommand (new MfCmdCreatePen ());
    registerCommand (new MfCmdCreatePalette ());
    registerCommand (new MfCmdCreatePatternBrush ());
    registerCommand (new MfCmdCreateRegion ());
    registerCommand (new MfCmdDeleteObject ());
    registerCommand (new MfCmdEllipse ());
    registerCommand (new MfCmdEscape ());
    registerCommand (new MfCmdExcludeClipRect ());
    registerCommand (new MfCmdExtFloodFill ());
    registerCommand (new MfCmdExtTextOut ());
    registerCommand (new MfCmdFillRegion ());
    registerCommand (new MfCmdFrameRegion ());
    registerCommand (new MfCmdFloodFill ());
    registerCommand (new MfCmdInvertRegion ());
    registerCommand (new MfCmdIntersectClipRect ());
    registerCommand (new MfCmdLineTo ());
    registerCommand (new MfCmdMoveTo ());
    registerCommand (new MfCmdOffsetClipRgn ());
    registerCommand (new MfCmdOffsetViewportOrg ());
    registerCommand (new MfCmdOffsetWindowOrg ());
    registerCommand (new MfCmdOldBitBlt ());
    registerCommand (new MfCmdOldStrechBlt ());
    registerCommand (new MfCmdPatBlt ());
    registerCommand (new MfCmdPaintRgn ());
    registerCommand (new MfCmdPie ());
    registerCommand (new MfCmdPolyPolygon ());
    registerCommand (new MfCmdPolygon ());
    registerCommand (new MfCmdPolyline ());
    registerCommand (new MfCmdRealisePalette ());
    registerCommand (new MfCmdRectangle ());
    registerCommand (new MfCmdRestoreDc ());
    registerCommand (new MfCmdResizePalette ());
    registerCommand (new MfCmdRoundRect ());
    registerCommand (new MfCmdSaveDc ());
    registerCommand (new MfCmdScaleWindowExt ());
    registerCommand (new MfCmdScaleViewportExt ());
    registerCommand (new MfCmdSelectClipRegion ());
    registerCommand (new MfCmdSelectObject ());
    registerCommand (new MfCmdSelectPalette ());
    registerCommand (new MfCmdSetBkMode ());
    registerCommand (new MfCmdSetBkColor ());
    registerCommand (new MfCmdSetDibitsToDevice ());
    registerCommand (new MfCmdSetMapperFlags ());
    registerCommand (new MfCmdSetMapMode ());
    registerCommand (new MfCmdSetPaletteEntries ());
    registerCommand (new MfCmdSetPolyFillMode ());
    registerCommand (new MfCmdSetPixel ());
    registerCommand (new MfCmdSetRop2 ());
    registerCommand (new MfCmdSetStretchBltMode ());
    registerCommand (new MfCmdSetTextCharExtra ());
    registerCommand (new MfCmdSetTextAlign ());
    registerCommand (new MfCmdSetTextColor ());
    registerCommand (new MfCmdSetTextJustification ());
    registerCommand (new MfCmdSetViewPortExt ());
    registerCommand (new MfCmdSetViewPortOrg ());
    registerCommand (new MfCmdSetWindowExt ());
    registerCommand (new MfCmdSetWindowOrg ());
    registerCommand (new MfCmdStretchBlt ());
    registerCommand (new MfCmdStretchDibits ());
    registerCommand (new MfCmdTextOut ());
    isInitialized = true;
  }

  public static void registerCommand (MfCmd command)
  {
    if (recordTypes == null)
      recordTypes = new Hashtable ();

    if (recordTypes.get (new Integer (command.getFunction ())) != null)
    {
      System.out.println (recordTypes.get (new Integer (command.getFunction ())).getClass ().getName ());
      throw new IllegalArgumentException ("Already registered");
    }

    recordTypes.put (new Integer (command.getFunction ()), command);
  }

  public static MfCmd getCommand (int function)
  {
    if (recordTypes == null)
      recordTypes = new Hashtable ();
    MfCmd cmd = (MfCmd) recordTypes.get (new Integer (function));
    if (cmd == null)
    {
      MfCmdUnknownCommand ucmd = new MfCmdUnknownCommand ();
      ucmd.setFunction (function);
      return ucmd;
    }
    return cmd.getInstance ();
  }
}
