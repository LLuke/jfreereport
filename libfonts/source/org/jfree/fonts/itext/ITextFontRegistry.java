package org.jfree.fonts.itext;

import org.jfree.fonts.merge.CompoundFontRegistry;
import org.jfree.fonts.truetype.TrueTypeFontRegistry;
import org.jfree.fonts.afm.AfmFontRegistry;
import org.jfree.fonts.pfm.PfmFontRegistry;

/**
 * This class provides access to the iText font system. The IText registry does not actually use iText to
 * register the fonts (as iText does not provide all information we need for that task). 
 *
 * @author Thomas Morgner
 */
public class ITextFontRegistry extends CompoundFontRegistry
{
  public ITextFontRegistry()
  {
    addRegistry(new ITextBuiltInFontRegistry());
    addRegistry(new TrueTypeFontRegistry());
    addRegistry(new AfmFontRegistry());
    addRegistry(new PfmFontRegistry());
  }
}
