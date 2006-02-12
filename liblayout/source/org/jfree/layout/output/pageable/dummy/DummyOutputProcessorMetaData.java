package org.jfree.layouting.output.pageable.dummy;

import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontRegistry;
import org.jfree.fonts.registry.FontStorage;
import org.jfree.layouting.input.style.keys.font.FontFamilyValues;
import org.jfree.layouting.output.AbstractOutputProcessorMetaData;
import org.jfree.layouting.output.OutputProcessorFeature;

public class DummyOutputProcessorMetaData extends AbstractOutputProcessorMetaData
{
  private FontStorage fontStorage;

  public DummyOutputProcessorMetaData (FontStorage fontStorage)
  {
    super(fontStorage.getFontRegistry());
    this.fontStorage = fontStorage;

    setFamilyMapping(FontFamilyValues.CURSIVE, "sans-serif");
    setFamilyMapping(FontFamilyValues.FANTASY, "Verdana");
    setFamilyMapping(FontFamilyValues.MONOSPACE, "monospaced");
    setFamilyMapping(FontFamilyValues.SERIF, "serif");
    setFamilyMapping(FontFamilyValues.SANS_SERIF, "sans-serif");

    setNumericFeatureValue(OutputProcessorFeature.DEVICE_RESOLUTION, 300);
    setNumericFeatureValue(OutputProcessorFeature.FONT_SMOOTH_THRESHOLD, 8);

  }

  public FontFamily getDefaultFontFamily ()
  {
    final FontRegistry fontRegistry = fontStorage.getFontRegistry();
    final FontFamily ff = fontRegistry.getFontFamily("sans-serif");
    if (ff != null)
    {
      return ff;
    }
    final String[] families = fontRegistry.getRegisteredFamilies();
    if (families.length == 0)
    {
      return null;
    }
    return fontRegistry.getFontFamily(families[0]);
  }
}
