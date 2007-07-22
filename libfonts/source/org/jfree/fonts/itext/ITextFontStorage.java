package org.jfree.fonts.itext;

import com.lowagie.text.pdf.BaseFont;
import org.jfree.fonts.merge.CompoundFontIdentifier;
import org.jfree.fonts.registry.FontContext;
import org.jfree.fonts.registry.FontIdentifier;
import org.jfree.fonts.registry.FontMetrics;
import org.jfree.fonts.registry.FontRecord;
import org.jfree.fonts.registry.FontRegistry;
import org.jfree.fonts.registry.FontStorage;
import org.jfree.fonts.truetype.TrueTypeFontIdentifier;

/**
 * Creation-Date: 22.07.2007, 17:54:43
 *
 * @author Thomas Morgner
 */
public class ITextFontStorage implements FontStorage
{
  private ITextFontRegistry registry;
  private BaseFontSupport baseFontSupport;

  public ITextFontStorage()
  {
    this.registry = new ITextFontRegistry();
    this.registry.initialize();

    this.baseFontSupport = new BaseFontSupport(registry);
  }

  public ITextFontStorage(final ITextFontRegistry registry)
  {
    this.registry = registry;
    this.baseFontSupport = new BaseFontSupport(registry);
  }

  public ITextFontStorage(final ITextFontRegistry registry, final String encoding)
  {
    this.registry = registry;
    this.baseFontSupport = new BaseFontSupport(registry, encoding);
  }

  public FontRegistry getFontRegistry()
  {
    return registry;
  }

  public FontMetrics getFontMetrics(final FontIdentifier rawRecord, final FontContext context)
  {
    final FontIdentifier record;
    if (rawRecord instanceof CompoundFontIdentifier)
    {
      final CompoundFontIdentifier ci = (CompoundFontIdentifier) rawRecord;
      record = ci.getIdentifier();
    }
    else
    {
      record = rawRecord;
    }

    final String fontName;
    final boolean bold;
    final boolean italic;
    if (record instanceof FontRecord)
    {
      final FontRecord fontRecord = (FontRecord) record;
      fontName = fontRecord.getFamily().getFamilyName();
      bold = fontRecord.isBold();
      italic = fontRecord.isItalic();
    }
    else if (record instanceof TrueTypeFontIdentifier)
    {
      final TrueTypeFontIdentifier ttfFontRecord = (TrueTypeFontIdentifier) record;
      fontName = ttfFontRecord.getFontName();
      bold = false;
      italic = false;
    }
    else
    {
      throw new IllegalArgumentException("Unknown font-identifier type encountered.");
    }

    final BaseFont baseFont = baseFontSupport.createBaseFont
        (fontName, bold, italic, context.getEncoding(), context.isEmbedded());

    return new BaseFontFontMetrics(baseFont, (float) context.getFontSize());
  }

  public BaseFontSupport getBaseFontSupport()
  {
    return baseFontSupport;
  }
}
