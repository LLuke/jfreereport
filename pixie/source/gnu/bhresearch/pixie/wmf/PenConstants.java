package gnu.bhresearch.pixie.wmf;

public interface PenConstants
{
  /* Pen Styles */
  public static final int PS_SOLID = 0;
  public static final int PS_DASH = 1;       /* -------  */
  public static final int PS_DOT = 2;       /* .......  */
  public static final int PS_DASHDOT = 3;       /* _._._._  */
  public static final int PS_DASHDOTDOT = 4;       /* _.._.._  */
  public static final int PS_NULL = 5;
  public static final int PS_INSIDEFRAME = 6;
  public static final int PS_USERSTYLE = 7;
  public static final int PS_ALTERNATE = 8;
  public static final int PS_STYLE_MASK = 0x0000000F;

  public static final int PS_ENDCAP_ROUND = 0x00000000;
  public static final int PS_ENDCAP_SQUARE = 0x00000100;
  public static final int PS_ENDCAP_FLAT = 0x00000200;
  public static final int PS_ENDCAP_MASK = 0x00000F00;

  public static final int PS_JOIN_ROUND = 0x00000000;
  public static final int PS_JOIN_BEVEL = 0x00001000;
  public static final int PS_JOIN_MITER = 0x00002000;
  public static final int PS_JOIN_MASK = 0x0000F000;

  public static final int PS_COSMETIC = 0x00000000;
  public static final int PS_GEOMETRIC = 0x00010000;
  public static final int PS_TYPE_MASK = 0x000F0000;

  /**
   * ___ ___ ___
   */
  public static final float[] DASH_DASH =
          {
            6f, 2f
          };

  /**
   * _ _ _ _ _ _
   */
  public static final float[] DASH_DOT =
          {
            2f, 2f
          };

  /**
   * ___ _ ___ _
   */
  public static final float[] DASH_DASHDOT =
          {
            6f, 2f, 2f, 2f
          };

  /**
   * ___ _ _ ___
   */
  public static final float[] DASH_DASHDOTDOT =
          {
            6f, 2f, 2f, 2f, 2f, 2f
          };

}