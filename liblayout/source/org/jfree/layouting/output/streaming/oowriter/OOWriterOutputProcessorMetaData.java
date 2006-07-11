package org.jfree.layouting.output.streaming.oowriter;

import org.jfree.fonts.registry.FontFamily;
import org.jfree.fonts.registry.FontStorage;
import org.jfree.layouting.input.style.keys.font.FontFamilyValues;
import org.jfree.layouting.output.AbstractOutputProcessorMetaData;

public class OOWriterOutputProcessorMetaData
        extends AbstractOutputProcessorMetaData
{
  public OOWriterOutputProcessorMetaData (final FontStorage fontStorage)
  {
    super(fontStorage);
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
