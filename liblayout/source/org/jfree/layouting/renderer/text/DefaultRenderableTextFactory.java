/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * RenderableTextFactory.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.renderer.text;

import java.util.ArrayList;

import org.jfree.fonts.registry.FontMetrics;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.keys.text.TextStyleKeys;
import org.jfree.layouting.input.style.keys.text.TextWrap;
import org.jfree.layouting.input.style.keys.text.WhitespaceCollapse;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.FontSpecification;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.layouter.style.resolver.LengthResolverUtility;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.renderer.model.RenderableText;
import org.jfree.layouting.renderer.text.breaks.BreakOpportunityProducer;
import org.jfree.layouting.renderer.text.breaks.LineBreakProducer;
import org.jfree.layouting.renderer.text.breaks.WordBreakProducer;
import org.jfree.layouting.renderer.text.classifier.GlyphClassificationProducer;
import org.jfree.layouting.renderer.text.classifier.LinebreakClassificationProducer;
import org.jfree.layouting.renderer.text.classifier.WhitespaceClassificationProducer;
import org.jfree.layouting.renderer.text.font.FontSizeProducer;
import org.jfree.layouting.renderer.text.font.GlyphMetrics;
import org.jfree.layouting.renderer.text.font.KerningProducer;
import org.jfree.layouting.renderer.text.font.NoKerningProducer;
import org.jfree.layouting.renderer.text.font.VariableFontSizeProducer;
import org.jfree.layouting.renderer.text.whitespace.CollapseWhiteSpaceFilter;
import org.jfree.layouting.renderer.text.whitespace.DiscardWhiteSpaceFilter;
import org.jfree.layouting.renderer.text.whitespace.PreserveBreaksWhiteSpaceFilter;
import org.jfree.layouting.renderer.text.whitespace.PreserveWhiteSpaceFilter;
import org.jfree.layouting.renderer.text.whitespace.WhiteSpaceFilter;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 11.06.2006, 16:05:34
 *
 * @author Thomas Morgner
 */
public class DefaultRenderableTextFactory implements RenderableTextFactory
{
  private LayoutProcess layoutProcess;
  private GraphemeClusterProducer clusterProducer;
  private boolean startText;
  private FontSizeProducer fontSizeProducer;
  private KerningProducer kerningProducer;
  private SpacingProducer spacingProducer;
  private BreakOpportunityProducer breakOpportunityProducer;
  private WhiteSpaceFilter whitespaceFilter;
  private CSSValue whitespaceCollapseValue;
  private GlyphClassificationProducer classificationProducer;
  private LayoutContext layoutContext;

  public DefaultRenderableTextFactory(final LayoutProcess layoutProcess)
  {
    this.layoutProcess = layoutProcess;
    this.clusterProducer = new GraphemeClusterProducer();
    this.startText = true;
  }


  public RenderableText[] createText(final int[] text,
                                     final int offset,
                                     final int length,
                                     final LayoutContext layoutContext)
  {
    kerningProducer = createKerningProducer(layoutContext);
    fontSizeProducer = createFontSizeProducer(layoutContext);
    spacingProducer = createSpacingProducer(layoutContext);
    breakOpportunityProducer = createBreakProducer(layoutContext);
    whitespaceFilter = createWhitespaceFilter(layoutContext);
    classificationProducer = createGlyphClassifier(layoutContext);
    this.layoutContext = layoutContext;

    if (startText)
    {
      whitespaceFilter.filter(ClassificationProducer.START_OF_TEXT);
      breakOpportunityProducer.createBreakOpportunity(ClassificationProducer.START_OF_TEXT);
      kerningProducer.getKerning(ClassificationProducer.START_OF_TEXT);
      startText = false;
    }

    return processText(text, offset, length);
  }

  public RenderableText[] processText (final int[] text,
                                       final int offset,
                                       final int length)
  {
    final ArrayList glyphList = new ArrayList(length);
    int lastCluster = -1;
    int clusterStart = offset;
    int cluster = -1;
    int width = 0;
    int height = 0;
    int kerning = 0;
    int breakweight = BreakOpportunityProducer.BREAK_NEVER;
    int glyphClassification = 0;
    int clusterStartCodePoint = -1;

    Spacing spacing = null;
    GlyphMetrics dims = null;

    ArrayList generatedLines = null;
    RenderableText firstGeneratedLine = null;

    final int maxLen = Math.min(length + offset, text.length);
    for (int i = offset; i < maxLen; i++)
    {
      final int codePoint = text[i];
      cluster = this.clusterProducer.createGraphemeCluster(codePoint);

      final int filteredCodePoint = whitespaceFilter.filter(codePoint);
      final boolean filterWhiteSpace =
              (filteredCodePoint == WhiteSpaceFilter.STRIP_WHITESPACE);

      // depending on whether we are inside a grapheme cluster right now,
      // we can skip and/or change some tests.

      if ((filterWhiteSpace == false) &&
              (cluster > 0 && cluster == lastCluster))
      {
        // we're inside a codepoint cluster ..
        dims = fontSizeProducer.getCharacterSize(codePoint, dims);
        width = Math.max(width, (dims.getWidth() & 0x7FFFFFFF));
        height = Math.max(height, (dims.getHeight() & 0x7FFFFFFF));
        glyphClassification = classificationProducer.getClassification(codePoint);
      }
      else
      {

        if (i > offset && clusterStart >= 0)
        {
          // finish the old cluster ..
          int[] extraChars = new int[i - clusterStart - 1];
          if (extraChars.length > 0)
          {
            System.arraycopy(text, clusterStart + 1, extraChars, 0, extraChars.length);
          }
          if (breakweight == BreakOpportunityProducer.BREAK_LINE)
          {
            Glyph[] glyphs = (Glyph[]) glyphList.toArray(new Glyph[glyphList.size()]);
            glyphList.clear();
            RenderableText textLine = new RenderableText
                    (layoutContext, glyphs, 0, glyphs.length, true, true);
            if (generatedLines == null)
            {
              if (firstGeneratedLine == null)
              {
                firstGeneratedLine = textLine;
              }
              else
              {
                generatedLines = new ArrayList();
                generatedLines.add(firstGeneratedLine);
                generatedLines.add(textLine);
                firstGeneratedLine = null;
              }
            }
            else
            {
              generatedLines.add(textLine);
            }
          }
          else
          {
            // we strip that character, it must be a linebreak indicator.
            // such characters dont get printed itself (at least here)
            // and can be safely removed as long
            Glyph glyph = new Glyph(clusterStartCodePoint, breakweight,
                    glyphClassification, spacing, width, height,
                    dims.getBaselinePosition(), kerning, extraChars);
            glyphList.add(glyph);
          }
        }

        if (filterWhiteSpace)
        {
          lastCluster = cluster;
          clusterStart = -1;
          clusterStartCodePoint = -1;
//          Log.debug (codePoint + " ~ " + whitespaceFilter);
        }
        else
        {
//          Log.debug (codePoint + " * " + whitespaceFilter);
          clusterStartCodePoint = filteredCodePoint;
          lastCluster = cluster;
          clusterStart = i;
          // not inside a codepoint cluster. Ordinary filtering happens ..
          // kerning:
          glyphClassification = classificationProducer.getClassification(codePoint);
          kerning = kerningProducer.getKerning(codePoint);
          breakweight = breakOpportunityProducer.createBreakOpportunity(codePoint);
          spacing = spacingProducer.createSpacing(codePoint);
          dims = fontSizeProducer.getCharacterSize(codePoint, dims);
          width = (dims.getWidth() & 0x7FFFFFFF);
          height = (dims.getHeight() & 0x7FFFFFFF);
        }
      }
    }

    // Copy&Paste from above: Process the last codepoint ..
    if (clusterStart >= 0)
    {
      // finish the old cluster ..
      int[] extraChars = new int[maxLen - clusterStart - 1];
      if (extraChars.length > 0)
      {
        System.arraycopy(text, clusterStart + 1, extraChars, 0, extraChars.length);
      }
      if (breakweight == BreakOpportunityProducer.BREAK_LINE)
      {
        Glyph[] glyphs = (Glyph[]) glyphList.toArray(new Glyph[glyphList.size()]);
        glyphList.clear();
        RenderableText textLine = new RenderableText
                (layoutContext, glyphs, 0, glyphs.length, true, true);
        if (generatedLines == null)
        {
          if (firstGeneratedLine == null)
          {
            firstGeneratedLine = textLine;
          }
          else
          {
            generatedLines = new ArrayList();
            generatedLines.add(firstGeneratedLine);
            generatedLines.add(textLine);
            firstGeneratedLine = null;
          }
        }
        else
        {
          generatedLines.add(textLine);
        }
      }
      else
      {
        // we strip that character, it must be a linebreak indicator.
        // such characters dont get printed itself (at least here)
        // and can be safely removed as long
        Glyph glyph = new Glyph(clusterStartCodePoint, breakweight,
                glyphClassification, spacing, width, height,
                dims.getBaselinePosition(), kerning, extraChars);
        glyphList.add(glyph);
      }
    }


    if (glyphList.isEmpty() == false)
    {
      Glyph[] glyphs = (Glyph[]) glyphList.toArray(new Glyph[glyphList.size()]);
      RenderableText textLine = new RenderableText
            (layoutContext, glyphs, 0, glyphs.length, true,
                    breakweight == BreakOpportunityProducer.BREAK_LINE);
      if (generatedLines == null)
      {
        if (firstGeneratedLine == null)
        {
          firstGeneratedLine = textLine;
        }
        else
        {
          generatedLines = new ArrayList();
          generatedLines.add(firstGeneratedLine);
          generatedLines.add(textLine);
          firstGeneratedLine = null;
        }
      }
      else
      {
        generatedLines.add(textLine);
      }
    }

    if (generatedLines != null)
    {
      return (RenderableText[]) generatedLines.toArray(new RenderableText[generatedLines.size()]);
    }
    else if (firstGeneratedLine != null)
    {
      return new RenderableText[]{ firstGeneratedLine };
    }
    else
    {
      // we did not produce any text. 
      return new RenderableText[0];
    }
  }

  protected WhiteSpaceFilter createWhitespaceFilter(final LayoutContext layoutContext)
  {
    final LayoutStyle style = layoutContext.getStyle();
    final CSSValue wsColl = style.getValue(TextStyleKeys.WHITE_SPACE_COLLAPSE);

    if (ObjectUtilities.equal(whitespaceCollapseValue, wsColl))
    {
      if (whitespaceFilter != null)
      {
        return whitespaceFilter;
      }
    }

    whitespaceCollapseValue = wsColl;

    if (WhitespaceCollapse.DISCARD.equals(wsColl))
    {
      return new DiscardWhiteSpaceFilter();
    }
    if (WhitespaceCollapse.PRESERVE.equals(wsColl))
    {
      return new PreserveWhiteSpaceFilter();
    }
    if (WhitespaceCollapse.PRESERVE_BREAKS.equals(wsColl))
    {
      return new PreserveBreaksWhiteSpaceFilter();
    }
    return new CollapseWhiteSpaceFilter();
  }

  protected GlyphClassificationProducer createGlyphClassifier(final LayoutContext layoutContext)
  {
    final LayoutStyle style = layoutContext.getStyle();
    final CSSValue wsColl = style.getValue(TextStyleKeys.WHITE_SPACE_COLLAPSE);
    if (WhitespaceCollapse.PRESERVE.equals(wsColl))
    {
      return new LinebreakClassificationProducer();
    }
    return new WhitespaceClassificationProducer();
  }

  protected BreakOpportunityProducer createBreakProducer
          (final LayoutContext layoutContext)
  {
    final LayoutStyle style = layoutContext.getStyle();
    final CSSValue wordBreak = style.getValue(TextStyleKeys.TEXT_WRAP);
    if (TextWrap.NONE.equals(wordBreak))
    {
      // surpress all but the linebreaks. This equals the 'pre' mode of HTML
      return new LineBreakProducer();
    }

    // allow other breaks as well. The wordbreak producer does not perform
    // advanced break-detection (like syllable based breaks).
    return new WordBreakProducer();
  }

  protected SpacingProducer createSpacingProducer
          (final LayoutContext layoutContext)
  {

    final LayoutStyle style = layoutContext.getStyle();
    final CSSValue minValue = style.getValue(TextStyleKeys.X_MIN_LETTER_SPACING);
    final CSSValue optValue = style.getValue(TextStyleKeys.X_OPTIMUM_LETTER_SPACING);
    final CSSValue maxValue = style.getValue(TextStyleKeys.X_MAX_LETTER_SPACING);

    final OutputProcessorMetaData outputMetaData =
            layoutProcess.getOutputMetaData();

    final int minIntVal = (int) LengthResolverUtility.convertLengthToDouble
            (minValue, layoutContext, outputMetaData);
    final int optIntVal = (int) LengthResolverUtility.convertLengthToDouble
            (optValue, layoutContext, outputMetaData);
    final int maxIntVal = (int) LengthResolverUtility.convertLengthToDouble
            (maxValue, layoutContext, outputMetaData);
    final Spacing spacing = new Spacing(minIntVal, optIntVal, maxIntVal);
    Log.debug("Using some static spacing: " + spacing);
    return new StaticSpacingProducer(spacing);
  }

  protected FontSizeProducer createFontSizeProducer
          (final LayoutContext layoutContext)
  {
    final FontSpecification fontSpecification =
            layoutContext.getFontSpecification();
    final OutputProcessorMetaData outputMetaData =
            layoutProcess.getOutputMetaData();
    final FontMetrics fontMetrics =
            outputMetaData.getFontMetrics(fontSpecification);
    return new VariableFontSizeProducer(fontMetrics);
//    return new StaticFontSizeProducer(5000, 5000, 4000);
  }

  protected KerningProducer createKerningProducer(final LayoutContext layoutContext)
  {

//    final LayoutStyle style = layoutContext.getStyle();
//    if (KerningMode.NONE.equals(style.getValue(TextStyleKeys.KERNING_MODE)))
//    {
    return new NoKerningProducer();
//    }
//
//    final FontSpecification fontSpecification =
//            layoutContext.getFontSpecification();
//    final OutputProcessorMetaData outputMetaData =
//            layoutProcess.getOutputMetaData();
//    final FontMetrics fontMetrics =
//            outputMetaData.getFontMetrics(fontSpecification);
//    return new DefaultKerningProducer(fontMetrics);
  }

  public void startText()
  {
    startText = true;
  }

  public RenderableText[] finishText()
  {
    if (layoutContext == null)
    {
      return new RenderableText[0];
    }

    RenderableText[] text = processText
            (new int[]{ ClassificationProducer.END_OF_TEXT}, 0, 1);
    layoutContext = null;
    classificationProducer = null;
    kerningProducer = null;
    fontSizeProducer = null;
    spacingProducer = null;
    breakOpportunityProducer = null;
    return text;
  }
}
