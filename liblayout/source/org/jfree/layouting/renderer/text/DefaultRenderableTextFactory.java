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
 * $Id: DefaultRenderableTextFactory.java,v 1.9 2006/10/22 14:58:26 taqua Exp $
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
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.input.style.keys.text.TextStyleKeys;
import org.jfree.layouting.input.style.keys.text.TextWrap;
import org.jfree.layouting.input.style.keys.text.WhitespaceCollapse;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.FontSpecification;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.style.CSSValueResolverUtility;
import org.jfree.layouting.layouter.style.LayoutStyle;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.RenderableText;
import org.jfree.layouting.renderer.model.SpacerRenderNode;
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
import org.jfree.util.ObjectUtilities;

/**
 * For the sake of completeness, we would now also need a script-type classifier
 * and from there we would need a BaseLineInfo-factory.
 *
 * @author Thomas Morgner
 */
public class DefaultRenderableTextFactory implements RenderableTextFactory
{
  protected static class DefaultRenderableTextFactoryState implements State
  {
    private State clusterProducer;
    private boolean startText;
    private boolean produced;
    private State fontSizeProducer;
    private State kerningProducer;
    private State spacingProducer;
    private State breakOpportunityProducer;
    private State whitespaceFilter;
    private CSSValue whitespaceCollapseValue;
    private State classificationProducer;
    private State languageClassifier;

    private ArrayList words;
    private ArrayList glyphList;
    private long leadingMargin;
    private int lastLanguage;

    public DefaultRenderableTextFactoryState(DefaultRenderableTextFactory factory)
        throws StateException
    {
      this.lastLanguage = factory.lastLanguage;
      this.leadingMargin = factory.leadingMargin;
      this.glyphList = (ArrayList) factory.glyphList.clone();
      this.words = (ArrayList) factory.words.clone();
      this.languageClassifier = factory.languageClassifier.saveState();
      this.clusterProducer = factory.clusterProducer.saveState();
      this.produced = factory.produced;
      this.startText = factory.startText;

      if (factory.layoutContext != null)
      {
        this.classificationProducer = factory.classificationProducer.saveState();
        this.whitespaceCollapseValue = factory.whitespaceCollapseValue;
        this.whitespaceFilter = factory.whitespaceFilter.saveState();
        this.breakOpportunityProducer = factory.breakOpportunityProducer.saveState();
        this.spacingProducer = factory.spacingProducer.saveState();
        this.kerningProducer = factory.kerningProducer.saveState();
        this.fontSizeProducer = factory.fontSizeProducer.saveState();
      }
    }

    /**
     * Creates a restored instance of the saved component.
     * <p/>
     * By using this factory-like approach, we gain independence from having to
     * know the actual implementation. This makes things a lot easier.
     *
     * @param layoutProcess the layout process that controls it all
     * @return the saved state
     * @throws org.jfree.layouting.StateException
     *
     */
    public StatefullComponent restore(LayoutProcess layoutProcess)
        throws StateException
    {
      DefaultRenderableTextFactory factory =
          new DefaultRenderableTextFactory(layoutProcess, false);
      factory.dims = null;
      factory.lastLanguage = this.lastLanguage;
      factory.leadingMargin = this.leadingMargin;
      factory.glyphList = (ArrayList) this.glyphList.clone();
      factory.words = (ArrayList) this.words.clone();
      factory.languageClassifier = (LanguageClassifier)
          this.languageClassifier.restore(layoutProcess);
      factory.clusterProducer = (GraphemeClusterProducer)
          this.clusterProducer.restore(layoutProcess);
      factory.produced = this.produced;
      factory.startText = this.startText;

      if (factory.classificationProducer != null)
      {
        factory.classificationProducer = (GlyphClassificationProducer)
            this.classificationProducer.restore(layoutProcess);
        factory.whitespaceCollapseValue = this.whitespaceCollapseValue;
        factory.whitespaceFilter = (WhiteSpaceFilter)
            this.whitespaceFilter.restore(layoutProcess);

        factory.breakOpportunityProducer = (BreakOpportunityProducer)
            this.breakOpportunityProducer.restore(layoutProcess);
        factory.spacingProducer = (SpacingProducer)
            this.spacingProducer.restore(layoutProcess);
        factory.kerningProducer = (KerningProducer)
            this.kerningProducer.restore(layoutProcess);
        factory.fontSizeProducer = (FontSizeProducer)
            this.fontSizeProducer.restore(layoutProcess);
      }

      return factory;
    }
  }

  private LayoutProcess layoutProcess;
  private GraphemeClusterProducer clusterProducer;
  private boolean startText;
  private boolean produced;
  private FontSizeProducer fontSizeProducer;
  private KerningProducer kerningProducer;
  private SpacingProducer spacingProducer;
  private BreakOpportunityProducer breakOpportunityProducer;
  private WhiteSpaceFilter whitespaceFilter;
  private CSSValue whitespaceCollapseValue;
  private GlyphClassificationProducer classificationProducer;
  private LayoutContext layoutContext;
  private LanguageClassifier languageClassifier;

  private transient GlyphMetrics dims;

  private ArrayList words;
  private ArrayList glyphList;
  private long leadingMargin;
  private int lastLanguage;

  // todo: This is part of a cheap hack.
  private transient FontMetrics fontMetrics;

  public DefaultRenderableTextFactory(final LayoutProcess layoutProcess)
  {
    this(layoutProcess, true);
  }

  protected DefaultRenderableTextFactory(final LayoutProcess layoutProcess,
                                         boolean init)
  {
    this.layoutProcess = layoutProcess;
    if (init)
    {
      this.clusterProducer = new GraphemeClusterProducer();
      this.languageClassifier = new DefaultLanguageClassifier();
      this.startText = true;
      this.words = new ArrayList();
      this.glyphList = new ArrayList();
      this.dims = new GlyphMetrics();
    }
  }


  public RenderNode[] createText(final int[] text,
                                 final int offset,
                                 final int length,
                                 final LayoutContext layoutContext)
  {
    if (layoutContext == null)
    {
      throw new NullPointerException();
    }

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
      produced = false;
    }

    // Todo: This is part of a cheap hack ..
    final FontSpecification fontSpecification =
        layoutContext.getFontSpecification();
    final OutputProcessorMetaData outputMetaData =
        layoutProcess.getOutputMetaData();
    fontMetrics = outputMetaData.getFontMetrics(fontSpecification);

    return processText(text, offset, length);
  }

  protected RenderNode[] processText(final int[] text,
                                     final int offset,
                                     final int length)
  {
    int clusterStartIdx = -1;
    final int maxLen = Math.min(length + offset, text.length);
    for (int i = offset; i < maxLen; i++)
    {
      final int codePoint = text[i];
      final boolean clusterStarted =
          this.clusterProducer.createGraphemeCluster(codePoint);
      // ignore the first cluster start; we need to see the whole cluster.
      if (clusterStarted)
      {
        if (i > offset)
        {
          int[] extraChars = new int[i - clusterStartIdx - 1];
          if (extraChars.length > 0)
          {
            System.arraycopy(text, clusterStartIdx + 1, extraChars, 0, extraChars.length);
          }
          addGlyph(text[clusterStartIdx], extraChars);
        }

        clusterStartIdx = i;
      }
    }

    // Process the last cluster ...
    if (clusterStartIdx >= offset)
    {
      int[] extraChars = new int[maxLen - clusterStartIdx - 1];
      if (extraChars.length > 0)
      {
        System.arraycopy(text, clusterStartIdx + 1, extraChars, 0, extraChars.length);
      }
      addGlyph(text[clusterStartIdx], extraChars);
    }

    if (words.isEmpty() == false)
    {
      final RenderNode[] renderableTexts = (RenderNode[])
          words.toArray(new RenderNode[words.size()]);
      words.clear();
      produced = true;
      return renderableTexts;
    }
    else
    {
      // we did not produce any text.
      return new RenderNode[0];
    }
  }

  protected void addGlyph(int rawCodePoint, int[] extraChars)
  {
    //  Log.debug ("Processing " + rawCodePoint);

    if (rawCodePoint == ClassificationProducer.END_OF_TEXT)
    {
      whitespaceFilter.filter(rawCodePoint);
      classificationProducer.getClassification(rawCodePoint);
      kerningProducer.getKerning(rawCodePoint);
      breakOpportunityProducer.createBreakOpportunity(rawCodePoint);
      spacingProducer.createSpacing(rawCodePoint);
      fontSizeProducer.getCharacterSize(rawCodePoint, dims);

      if (leadingMargin != 0 || glyphList.isEmpty() == false)
      {
        addWord(false);
      }
      else
      {
        // finish up ..
        glyphList.clear();
        leadingMargin = 0;
      }
      return;
    }

    int codePoint = whitespaceFilter.filter(rawCodePoint);
    boolean stripWhitespaces;

    // No matter whether we will ignore the result, we have to keep our
    // factories up and running. These beasts need to see all data, no
    // matter what get printed later.
    if (codePoint == WhiteSpaceFilter.STRIP_WHITESPACE)
    {
      // if we dont have extra-chars, ignore the thing ..
      if (extraChars.length == 0)
      {
        stripWhitespaces = true;
      }
      else
      {
        // convert it into a space. This might be invalid, but will work for now.
        codePoint = ' ';
        stripWhitespaces = false;
      }
    }
    else
    {
      stripWhitespaces = false;
    }

    int glyphClassification = classificationProducer.getClassification(codePoint);
    int kerning = kerningProducer.getKerning(codePoint);
    int breakweight = breakOpportunityProducer.createBreakOpportunity(codePoint);
    Spacing spacing = spacingProducer.createSpacing(codePoint);
    dims = fontSizeProducer.getCharacterSize(codePoint, dims);
    int width = (dims.getWidth() & 0x7FFFFFFF);
    int height = (dims.getHeight() & 0x7FFFFFFF);
    lastLanguage = languageClassifier.getScript(codePoint);

    for (int i = 0; i < extraChars.length; i++)
    {
      int extraChar = extraChars[i];
      dims = fontSizeProducer.getCharacterSize(extraChar, dims);
      width = Math.max(width, (dims.getWidth() & 0x7FFFFFFF));
      height = Math.max(height, (dims.getHeight() & 0x7FFFFFFF));
      breakweight = breakOpportunityProducer.createBreakOpportunity(extraChar);
      glyphClassification = classificationProducer.getClassification(extraChar);
    }

    if (stripWhitespaces)
    {
      //  Log.debug ("Stripping whitespaces");
      return;
    }

    if (Glyph.SPACE_CHAR == glyphClassification && isWordBreak(breakweight))
    {

      // Finish the current word ...
      final boolean forceLinebreak = breakweight == BreakOpportunityProducer.BREAK_LINE;
      if (glyphList.isEmpty() == false || forceLinebreak)
      {
        addWord(forceLinebreak);
      }

      // This character can be stripped. We increase the leading margin of the
      // next word by the character's width.
      leadingMargin += width;
      //   Log.debug ("Increasing Margin");
      return;
    }

    Glyph glyph = new Glyph(codePoint, breakweight,
        glyphClassification, spacing, width, height,
        dims.getBaselinePosition(), kerning, extraChars);
    glyphList.add(glyph);
    // Log.debug ("Adding Glyph");

    // does this finish a word? Check it!
    if (isWordBreak(breakweight) && glyphList.isEmpty() == false)
    {
      final boolean forceLinebreak = breakweight == BreakOpportunityProducer.BREAK_LINE;
      addWord(forceLinebreak);
    }
  }

  protected void addWord(boolean forceLinebreak)
  {
    if (glyphList.isEmpty())
    {
      // create an trailing margin element. This way, it can collapse with
      // the next element.
      if (forceLinebreak)
      {
        words.add(new RenderableText
            (layoutContext, TextUtility.createBaselineInfo('\n', fontMetrics), new Glyph[0], 0,
                0, lastLanguage, forceLinebreak));
      }
      else if (produced == true)
      {
        words.add(new SpacerRenderNode(leadingMargin, 0, false));
      }
    }
    else
    {
      // ok, it does.
      Glyph[] glyphs = (Glyph[]) glyphList.toArray(new Glyph[glyphList.size()]);
      if (leadingMargin > 0)// && words.isEmpty() == false)
      {
        words.add(new SpacerRenderNode(leadingMargin, 0, false));
      }

      // todo: this is cheating ..
      final int codePoint = glyphs[0].getCodepoint();

      words.add(new RenderableText
          (layoutContext, TextUtility.createBaselineInfo(codePoint, fontMetrics), glyphs, 0,
              glyphs.length, lastLanguage, forceLinebreak));
      glyphList.clear();
    }
    leadingMargin = 0;
  }

  private boolean isWordBreak(int breakOp)
  {
    if (BreakOpportunityProducer.BREAK_WORD == breakOp ||
        BreakOpportunityProducer.BREAK_LINE == breakOp)
    {
      return true;
    }
    return false;
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

    final int minIntVal = (int) CSSValueResolverUtility.convertLengthToDouble
        (minValue, layoutContext, outputMetaData);
    final int optIntVal = (int) CSSValueResolverUtility.convertLengthToDouble
        (optValue, layoutContext, outputMetaData);
    final int maxIntVal = (int) CSSValueResolverUtility.convertLengthToDouble
        (maxValue, layoutContext, outputMetaData);
    final Spacing spacing = new Spacing(minIntVal, optIntVal, maxIntVal);
//    Log.debug("Using some static spacing: " + spacing);
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

  public RenderNode[] finishText()
  {
    if (layoutContext == null)
    {
      return new RenderableText[0];
    }

    RenderNode[] text = processText
        (new int[]{ClassificationProducer.END_OF_TEXT}, 0, 1);
    layoutContext = null;
    classificationProducer = null;
    kerningProducer = null;
    fontSizeProducer = null;
    spacingProducer = null;
    breakOpportunityProducer = null;

    return text;
  }

  public void startText()
  {
    startText = true;
  }

  public State saveState() throws StateException
  {
    return new DefaultRenderableTextFactoryState(this);
  }
}
