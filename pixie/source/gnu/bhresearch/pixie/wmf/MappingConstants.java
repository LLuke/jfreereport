package gnu.bhresearch.pixie.wmf;

public interface MappingConstants
{
  public static final int MM_TEXT = 1;
  public static final int MM_LOMETRIC = 2;
  public static final int MM_HIMETRIC = 3;
  public static final int MM_LOENGLISH = 4;
  public static final int MM_HIENGLISH = 5;
  public static final int MM_TWIPS = 6;
  public static final int MM_ISOTROPIC = 7;
  public static final int MM_ANISOTROPIC = 8;

  /* Min and Max Mapping Mode values */
  public static final int MM_MIN = MM_TEXT;
  public static final int MM_MAX = MM_ANISOTROPIC;
  public static final int MM_MAX_FIXEDSCALE = MM_TWIPS;
}