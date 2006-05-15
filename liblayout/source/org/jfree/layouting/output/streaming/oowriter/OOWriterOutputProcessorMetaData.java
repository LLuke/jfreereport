package org.jfree.layouting.output.streaming.oowriter;

import org.jfree.layouting.output.AbstractOutputProcessorMetaData;
import org.jfree.layouting.input.style.keys.font.FontFamilyValues;
import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontRegistry;

public class OOWriterOutputProcessorMetaData
        extends AbstractOutputProcessorMetaData
{
  public OOWriterOutputProcessorMetaData (final FontRegistry fontRegistry)
  {
    super(fontRegistry);
    setFamilyMapping(FontFamilyValues.CURSIVE, "sans-serif");
    setFamilyMapping(FontFamilyValues.FANTASY, "fantasy");
    setFamilyMapping(FontFamilyValues.MONOSPACE, "monospace");
    setFamilyMapping(FontFamilyValues.SERIF, "serif");
    setFamilyMapping(FontFamilyValues.SANS_SERIF, "sans-serif");
  }

  public FontFamily getDefaultFontFamily ()
  {
    return getFontRegistry().getFontFamily("sans-serif");
  }

  public String getExportDescriptor ()
  {
    return "streaming/oowriter";
  }
}
