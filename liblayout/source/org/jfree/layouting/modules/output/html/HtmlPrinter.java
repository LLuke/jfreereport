/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/liblayout/
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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 */

package org.jfree.layouting.modules.output.html;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import org.jfree.layouting.DocumentContextUtility;
import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;
import org.jfree.layouting.input.style.keys.border.BorderStyle;
import org.jfree.layouting.input.style.keys.border.BorderStyleKeys;
import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.keys.box.DisplayRole;
import org.jfree.layouting.input.style.keys.color.ColorStyleKeys;
import org.jfree.layouting.input.style.keys.color.HtmlColors;
import org.jfree.layouting.input.style.keys.font.FontStyleKeys;
import org.jfree.layouting.input.style.keys.line.LineStyleKeys;
import org.jfree.layouting.input.style.keys.text.TextStyleKeys;
import org.jfree.layouting.input.style.keys.text.WhitespaceCollapse;
import org.jfree.layouting.input.style.values.CSSColorValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.DocumentContext;
import org.jfree.layouting.layouter.context.FontSpecification;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.context.LayoutStyle;
import org.jfree.layouting.namespace.Namespaces;
import org.jfree.layouting.renderer.model.BlockRenderBox;
import org.jfree.layouting.renderer.model.BoxLayoutProperties;
import org.jfree.layouting.renderer.model.InlineRenderBox;
import org.jfree.layouting.renderer.model.NodeLayoutProperties;
import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.RenderableText;
import org.jfree.layouting.renderer.model.StaticBoxLayoutProperties;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.model.table.TableCellRenderBox;
import org.jfree.layouting.renderer.model.table.TableRenderBox;
import org.jfree.layouting.renderer.model.table.TableRowRenderBox;
import org.jfree.layouting.renderer.model.table.TableSectionRenderBox;
import org.jfree.layouting.renderer.model.table.cols.TableColumn;
import org.jfree.layouting.renderer.model.table.cols.TableColumnModel;
import org.jfree.layouting.renderer.process.IterateStructuralProcessStep;
import org.jfree.layouting.util.geom.StrictGeomUtility;
import org.jfree.util.FastStack;
import org.jfree.util.StackableRuntimeException;
import org.jfree.xmlns.common.AttributeList;
import org.jfree.xmlns.writer.DefaultTagDescription;
import org.jfree.xmlns.writer.XmlWriter;

/**
 * Creation-Date: 25.11.2006, 18:17:57
 *
 * @author Thomas Morgner
 */
public class HtmlPrinter extends IterateStructuralProcessStep
{
  public static final String TAG_DEF_PREFIX = "org.jfree.layouting.modules.output.html.";

  private XmlWriter xmlWriter;
  private FastStack contexts;
  private DecimalFormat pointConverter;
  private NumberFormat pointIntConverter;

  public HtmlPrinter()
  {
    contexts = new FastStack();
    pointConverter = new DecimalFormat
        ("0.####", new DecimalFormatSymbols(Locale.US));
    pointIntConverter = new DecimalFormat
        ("0", new DecimalFormatSymbols(Locale.US));
  }

  public void generate(final LogicalPageBox box,
                       final OutputStream out,
                       final String encoding,
                       final DocumentContext documentContext)
      throws IOException
  {
    final DefaultTagDescription tagDescription = new DefaultTagDescription();
    tagDescription.configure
        (LibLayoutBoot.getInstance().getGlobalConfig(), HtmlPrinter.TAG_DEF_PREFIX);


    final OutputStreamWriter writer = new OutputStreamWriter(out, encoding);
    xmlWriter = new XmlWriter(writer, tagDescription);
    xmlWriter.setAlwaysAddNamespace(false);
    xmlWriter.setAssumeDefaultNamespace(true);
    xmlWriter.addNamespace(Namespaces.XHTML_NAMESPACE, "");

    contexts.clear();

    final LayoutStyle initialStyle =
        DocumentContextUtility.getInitialStyle(documentContext);

    final StyleBuilder inialBuilder = new StyleBuilder(false);
    StyleKey[] keys = StyleKeyRegistry.getRegistry().getKeys();
    for (int i = 0; i < keys.length; i++)
    {
      final StyleKey key = keys[i];
      if (key.isInherited())
      {
        inialBuilder.append(key, initialStyle.getValue(key));
      }
    }
    contexts.push(inialBuilder);

    startBlockBox(box);
    processBoxChilds(box);
    finishBlockBox(box);
    xmlWriter = null;
    writer.flush();
  }
  // Todo: Text height is not yet applied by the layouter ..

  protected boolean startInlineBox(InlineRenderBox box)
  {
    try
    {
      final StyleBuilder builder = createStyleBuilder();
      contexts.push(builder);

      buildStyle(box, builder);

      final AttributeList attList = new AttributeList();
      if (builder.isEmpty() == false)
      {
        attList.setAttribute(Namespaces.XHTML_NAMESPACE, "style", builder.toString());
      }
      xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE, "span", attList, false);
      return true;
    }
    catch (IOException e)
    {
      throw new StackableRuntimeException("Failed", e);
    }
  }

  private void buildStyle(final RenderBox box, final StyleBuilder builder)
  {
    final LayoutContext layoutContext = box.getLayoutContext();
    if (layoutContext == null)
    {
      // this is either the logical page box or one of the direct anchestors
      // of said box.
      return;
    }

    final FontSpecification fs = layoutContext.getFontSpecification();
    final double fontSize = fs.getFontSize();
    builder.append(FontStyleKeys.FONT_SIZE, toPointString(fontSize), "pt");
    builder.append(FontStyleKeys.FONT_FAMILY, fs.getFontFamily());
    builder.append(FontStyleKeys.FONT_WEIGHT,
        layoutContext.getValue(FontStyleKeys.FONT_WEIGHT));
    builder.append(FontStyleKeys.FONT_STYLE,
        layoutContext.getValue(FontStyleKeys.FONT_STYLE));
    builder.append(TextStyleKeys.TEXT_ALIGN, layoutContext.getValue(TextStyleKeys.TEXT_ALIGN));
    builder.append(TextStyleKeys.TEXT_ALIGN_LAST, layoutContext.getValue(TextStyleKeys.TEXT_ALIGN_LAST));

    final NodeLayoutProperties nlp = box.getNodeLayoutProperties();
    //final BoxLayoutProperties blp = box.getBoxLayoutProperties();
    final StaticBoxLayoutProperties sblp = box.getStaticBoxLayoutProperties();
    builder.append(LineStyleKeys.VERTICAL_ALIGN, nlp.getVerticalAlignment());

    if (sblp.getPaddingTop() > 0 ||
        sblp.getPaddingLeft() > 0 ||
        sblp.getPaddingBottom() > 0 ||
        sblp.getPaddingRight() > 0)
    {
      builder.append(BoxStyleKeys.PADDING_TOP,
          toPointString(sblp.getPaddingTop()), "pt");
      builder.append(BoxStyleKeys.PADDING_LEFT,
          toPointString(sblp.getPaddingLeft()), "pt");
      builder.append(BoxStyleKeys.PADDING_BOTTOM,
          toPointString(sblp.getPaddingBottom()), "pt");
      builder.append(BoxStyleKeys.PADDING_RIGHT,
          toPointString(sblp.getPaddingRight()), "pt");
    }
    else
    {
      builder.append("padding", false, "0");
    }

    if (sblp.getMarginLeft() != 0 ||
        sblp.getMarginRight() != 0 ||
        sblp.getMarginTop() != 0 ||
        sblp.getMarginBottom() != 0)
    {
      builder.append(BoxStyleKeys.MARGIN_LEFT,
          toPointString(sblp.getMarginLeft()), "pt");
      builder.append(BoxStyleKeys.MARGIN_RIGHT,
          toPointString(sblp.getMarginRight()), "pt");
      builder.append(BoxStyleKeys.MARGIN_TOP,
          toPointString(sblp.getMarginTop()), "pt");
      builder.append(BoxStyleKeys.MARGIN_BOTTOM,
          toPointString(sblp.getMarginBottom()), "pt");
    }
    else
    {
      builder.append("margin", false, "0");
    }

    final String bgColor = toColorString(layoutContext.getValue
        (BorderStyleKeys.BACKGROUND_COLOR));
    if (bgColor != null)
    {
      builder.append(BorderStyleKeys.BACKGROUND_COLOR, bgColor);
    }
    final String fgColor = toColorString(layoutContext.getValue(ColorStyleKeys.COLOR));
    if (fgColor != null)
    {
      builder.append(ColorStyleKeys.COLOR, fgColor);
    }

    if (sblp.getBorderTop() > 0 || sblp.getBorderLeft() > 0 ||
        sblp.getBorderBottom() > 0 || sblp.getBorderRight() > 0)
    {
      if (sblp.getBorderTop() > 0)
      {
        builder.append(BorderStyleKeys.BORDER_TOP_COLOR, layoutContext.getValue(BorderStyleKeys.BORDER_TOP_COLOR));
        builder.append(BorderStyleKeys.BORDER_TOP_STYLE, layoutContext.getValue(BorderStyleKeys.BORDER_TOP_STYLE));
        builder.append(BorderStyleKeys.BORDER_TOP_WIDTH, toPointString(sblp.getBorderTop()), "pt");
      }
      else
      {
        builder.append(BorderStyleKeys.BORDER_TOP_STYLE, BorderStyle.NONE);
      }

      if (sblp.getBorderLeft() > 0)
      {
        builder.append(BorderStyleKeys.BORDER_LEFT_COLOR, layoutContext.getValue(BorderStyleKeys.BORDER_LEFT_COLOR));
        builder.append(BorderStyleKeys.BORDER_LEFT_STYLE, layoutContext.getValue(BorderStyleKeys.BORDER_LEFT_STYLE));
        builder.append(BorderStyleKeys.BORDER_LEFT_WIDTH, toPointString(sblp.getBorderLeft()), "pt");
      }
      else
      {
        builder.append(BorderStyleKeys.BORDER_LEFT_STYLE, BorderStyle.NONE);
      }

      if (sblp.getBorderBottom() > 0)
      {
        builder.append(BorderStyleKeys.BORDER_BOTTOM_COLOR, layoutContext.getValue(BorderStyleKeys.BORDER_BOTTOM_COLOR));
        builder.append(BorderStyleKeys.BORDER_BOTTOM_STYLE, layoutContext.getValue(BorderStyleKeys.BORDER_BOTTOM_STYLE));
        builder.append(BorderStyleKeys.BORDER_BOTTOM_WIDTH, toPointString(sblp.getBorderBottom()), "pt");
      }
      else
      {
        builder.append(BorderStyleKeys.BORDER_BOTTOM_STYLE, BorderStyle.NONE);
      }

      if (sblp.getBorderRight() > 0)
      {
        builder.append(BorderStyleKeys.BORDER_RIGHT_COLOR, layoutContext.getValue(BorderStyleKeys.BORDER_RIGHT_COLOR));
        builder.append(BorderStyleKeys.BORDER_RIGHT_STYLE, layoutContext.getValue(BorderStyleKeys.BORDER_RIGHT_STYLE));
        builder.append(BorderStyleKeys.BORDER_RIGHT_WIDTH, toPointString(sblp.getBorderRight()), "pt");
      }
      else
      {
        builder.append(BorderStyleKeys.BORDER_RIGHT_STYLE, BorderStyle.NONE);
      }
    }
    else
    {
      builder.append("border-style", true, "none");
    }
  }

  private StyleBuilder createStyleBuilder()
  {
    final StyleBuilder builder;
    if (contexts.isEmpty())
    {
      builder = new StyleBuilder(true);
    }
    else
    {
      builder = new StyleBuilder(true, (StyleBuilder) contexts.peek());
    }
    return builder;
  }

  private String toColorString(final CSSValue color)
  {
    if (color == null)
    {
      return null;
    }

    if (color instanceof CSSColorValue == false)
    {
      // This should not happen ..
      return color.getCSSText();
    }

    final CSSColorValue colorValue = (CSSColorValue) color;
    if (colorValue.getAlpha() == 0)
    {
      return null;
    }

    try
    {
      final Field[] fields = HtmlColors.class.getFields();
      for (int i = 0; i < fields.length; i++)
      {
        final Field f = fields[i];
        if (Modifier.isPublic(f.getModifiers())
            && Modifier.isFinal(f.getModifiers())
            && Modifier.isStatic(f.getModifiers()))
        {
          final String name = f.getName();
          final Object oColor = f.get(null);
          if (oColor instanceof Color)
          {
            if (color.equals(oColor))
            {
              return name.toLowerCase();
            }
          }
        }
      }
    }
    catch (Exception e)
    {
      //
    }

//    // no defined constant color, so this must be a user defined color
//    final String colorText = Integer.toHexString(colorValue.getRGB() & 0x00ffffff);
//    final StringBuffer retval = new StringBuffer(7);
//    retval.append("#");
//
//    final int fillUp = 6 - colorText.length();
//    for (int i = 0; i < fillUp; i++)
//    {
//      retval.append("0");
//    }
//
//    retval.append(colorText);
    return colorValue.getCSSText();
  }

  private String toPointString(final double value)
  {
    if (Math.floor(value) == value)
    {
      return pointIntConverter.format(value);
    }

    return pointConverter.format(value);
  }

  private String toPointString(final long value)
  {
    final int remainder = (int) value % 1000;
    if (remainder == 0)
    {
      final double d = StrictGeomUtility.toExternalValue(value);
      return pointIntConverter.format(d);
    }

    final double d = StrictGeomUtility.toExternalValue(value);
    return pointConverter.format(d);
  }

  protected void finishInlineBox(InlineRenderBox box)
  {
    try
    {
      contexts.pop();
      xmlWriter.writeCloseTag();
    }
    catch (IOException e)
    {
      throw new StackableRuntimeException("Failed", e);
    }
  }

  protected boolean startBlockBox(BlockRenderBox box)
  {
    try
    {
      final StyleBuilder builder = createStyleBuilder();
      contexts.push(builder);

      if (box instanceof TableRenderBox)
      {
        startTable((TableRenderBox) box, builder);
      }
      else if (box instanceof TableSectionRenderBox)
      {
        TableSectionRenderBox section = (TableSectionRenderBox) box;
        final CSSValue displayRole = section.getDisplayRole();
        if (DisplayRole.TABLE_HEADER_GROUP.equals(displayRole))
        {
          startTableHeader((TableSectionRenderBox) box, builder);
        }
        else if (DisplayRole.TABLE_FOOTER_GROUP.equals(displayRole))
        {
          startTableFooter((TableSectionRenderBox) box, builder);
        }
        else
        {
          startTableBody((TableSectionRenderBox) box, builder);
        }
      }
      else if (box instanceof TableRowRenderBox)
      {
        startTableRow((TableRowRenderBox) box, builder);
      }
      else if (box instanceof TableCellRenderBox)
      {
        // or a th, it depends ..
        startTableCell((TableCellRenderBox) box, builder);
      }
      else if (box instanceof ParagraphRenderBox)
      {
        startParagraph((ParagraphRenderBox) box, builder);
      }
      else if (box instanceof LogicalPageBox)
      {
        startPageBox(box, builder);
      }
      else
      {
        startOtherBlockBox(box, builder);
      }
      return true;
    }
    catch (IOException e)
    {
      throw new StackableRuntimeException("Failed", e);
    }
  }

  protected void startPageBox(final RenderBox box,
                              final StyleBuilder builder) throws IOException
  {
    buildStyle(box, builder);
    builder.append(BoxStyleKeys.WIDTH, toPointString(box.getWidth()), "pt");

    final AttributeList attList = new AttributeList();
    attList.setAttribute(Namespaces.XHTML_NAMESPACE, "style", builder.toString());
    xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE,
        "div", attList, XmlWriter.OPEN);
  }

  protected void finishPageBox(final RenderBox box) throws IOException
  {
    xmlWriter.writeCloseTag();
  }

  protected void startOtherBlockBox(BlockRenderBox box,
                                    final StyleBuilder builder)
      throws IOException
  {
    buildStyle(box, builder);
    final AttributeList attList = new AttributeList();
    if (builder.isEmpty() == false)
    {
      attList.setAttribute(Namespaces.XHTML_NAMESPACE, "style", builder.toString());
    }
    xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE,
        "div", attList, XmlWriter.OPEN);
  }

  protected void finishOtherBlockBox(final BlockRenderBox tableRenderBox)
      throws IOException
  {
    xmlWriter.writeCloseTag();
  }

  protected void startParagraph(final ParagraphRenderBox box,
                                final StyleBuilder builder) throws IOException
  {
    buildStyle(box, builder);
    final AttributeList attList = new AttributeList();
    if (builder.isEmpty() == false)
    {
      attList.setAttribute(Namespaces.XHTML_NAMESPACE, "style", builder.toString());
    }
    xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE,
        "p", attList, XmlWriter.OPEN);
  }

  protected void finishParagraph(final ParagraphRenderBox tableRenderBox)
      throws IOException
  {
    xmlWriter.writeCloseTag();
  }

  protected void startTableCell(final TableCellRenderBox box,
                                final StyleBuilder builder) throws IOException
  {
    final int colSpan = box.getColSpan();
    final int rowSpan = box.getRowSpan();

    final AttributeList attrList = new AttributeList();
    if (colSpan != 0)
    {
      attrList.setAttribute(Namespaces.XHTML_NAMESPACE, "colspan", String.valueOf(colSpan));
    }
    if (rowSpan != 0)
    {
      attrList.setAttribute(Namespaces.XHTML_NAMESPACE, "rowspan", String.valueOf(rowSpan));
    }

    buildStyle(box, builder);
    if (builder.isEmpty() == false)
    {
      attrList.setAttribute(Namespaces.XHTML_NAMESPACE, "style", builder.toString());
    }
    xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE, "td", attrList, XmlWriter.OPEN);
  }

  protected void finishTableCell(final TableCellRenderBox tableRenderBox)
      throws IOException
  {
    xmlWriter.writeCloseTag();
  }

  protected void startTableRow(final TableRowRenderBox box,
                               final StyleBuilder builder) throws IOException
  {
    buildStyle(box, builder);
    final AttributeList attList = new AttributeList();
    if (builder.isEmpty() == false)
    {
      attList.setAttribute(Namespaces.XHTML_NAMESPACE, "style", builder.toString());
    }
    xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE,
        "tr", attList, XmlWriter.OPEN);
  }

  protected void finishTableRow(final TableRowRenderBox tableRenderBox)
      throws IOException
  {
    xmlWriter.writeCloseTag();
  }

  protected void startTableHeader(final TableSectionRenderBox box,
                                  final StyleBuilder builder)
      throws IOException
  {
    buildStyle(box, builder);
    final AttributeList attList = new AttributeList();
    if (builder.isEmpty() == false)
    {
      attList.setAttribute(Namespaces.XHTML_NAMESPACE, "style", builder.toString());
    }
    xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE,
        "thead", attList, XmlWriter.OPEN);
  }

  protected void finishTableHeader(final TableSectionRenderBox tableRenderBox)
      throws IOException
  {
    xmlWriter.writeCloseTag();
  }

  protected void startTableBody(final TableSectionRenderBox box,
                                final StyleBuilder builder)
      throws IOException
  {
    buildStyle(box, builder);
    final AttributeList attList = new AttributeList();
    if (builder.isEmpty() == false)
    {
      attList.setAttribute(Namespaces.XHTML_NAMESPACE, "style", builder.toString());
    }
    xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE,
        "tbody", attList, XmlWriter.OPEN);
  }

  protected void finishTableBody(final TableSectionRenderBox tableRenderBox)
      throws IOException
  {
    xmlWriter.writeCloseTag();
  }

  protected void startTableFooter(final TableSectionRenderBox box,
                                  final StyleBuilder builder)
      throws IOException
  {
    buildStyle(box, builder);
    final AttributeList attList = new AttributeList();
    if (builder.isEmpty() == false)
    {
      attList.setAttribute(Namespaces.XHTML_NAMESPACE, "style", builder.toString());
    }
    xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE,
        "tfoot", attList, XmlWriter.OPEN);
  }

  protected void finishTableFooter(final TableSectionRenderBox tableRenderBox)
      throws IOException
  {
    xmlWriter.writeCloseTag();
  }

  protected void startTable(final TableRenderBox box,
                            final StyleBuilder builder)
      throws IOException
  {
    buildStyle(box, builder);


    final AttributeList attList = new AttributeList();
    attList.setAttribute(Namespaces.XHTML_NAMESPACE, "cellspacing", "0");
    attList.setAttribute(Namespaces.XHTML_NAMESPACE, "cellpadding", "0");

    if (builder.isEmpty() == false)
    {
      attList.setAttribute(Namespaces.XHTML_NAMESPACE, "style", builder.toString());
    }
    xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE,
        "table", attList, XmlWriter.OPEN);

    xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE, "colgroup", XmlWriter.OPEN);

    final TableColumnModel columnModel = box.getColumnModel();
    final int columnCount = columnModel.getColumnCount();
    for (int i = 0; i < columnCount; i++)
    {
      final TableColumn column = columnModel.getColumn(i);
      final long effectiveSize = column.getEffectiveSize();
      final StyleBuilder colbuilder = new StyleBuilder(true);
      colbuilder.append(BoxStyleKeys.WIDTH, toPointString(effectiveSize), "pt");
      xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE,
          "col", "style", colbuilder.toString(), XmlWriter.CLOSE);
    }
    xmlWriter.writeCloseTag();
  }

  protected void finishTable(final TableRenderBox tableRenderBox)
      throws IOException
  {
    xmlWriter.writeCloseTag();
  }


  protected void finishBlockBox(BlockRenderBox box)
  {
    try
    {
      if (box instanceof TableRenderBox)
      {
        finishTable((TableRenderBox) box);
      }
      else if (box instanceof TableSectionRenderBox)
      {
        TableSectionRenderBox section = (TableSectionRenderBox) box;
        final CSSValue displayRole = section.getDisplayRole();
        if (DisplayRole.TABLE_HEADER_GROUP.equals(displayRole))
        {
          finishTableHeader((TableSectionRenderBox) box);
        }
        else if (DisplayRole.TABLE_FOOTER_GROUP.equals(displayRole))
        {
          finishTableFooter((TableSectionRenderBox) box);
        }
        else
        {
          finishTableBody((TableSectionRenderBox) box);
        }
      }
      else if (box instanceof TableRowRenderBox)
      {
        finishTableRow((TableRowRenderBox) box);
      }
      else if (box instanceof TableCellRenderBox)
      {
        // or a th, it depends ..
        finishTableCell((TableCellRenderBox) box);
      }
      else if (box instanceof ParagraphRenderBox)
      {
        finishParagraph((ParagraphRenderBox) box);
      }
      else if (box instanceof LogicalPageBox)
      {
        finishPageBox(box);
      }
      else
      {
        finishOtherBlockBox(box);
      }

      contexts.pop();
    }
    catch (IOException e)
    {
      throw new StackableRuntimeException("Failed", e);
    }
  }


  protected void startOtherNode(RenderNode node)
  {
    try
    {
      if (node instanceof RenderableText)
      {
        final RenderableText text = (RenderableText) node;

        // in a sane world, we would now process the glyphs ..
        xmlWriter.writeText(text.getRawText());
        final LayoutContext layoutContext = text.getLayoutContext();
        final CSSValue wsCollapse =
            layoutContext.getValue(TextStyleKeys.WHITE_SPACE_COLLAPSE);
        if (WhitespaceCollapse.DISCARD.equals(wsCollapse) == false)
        {
          xmlWriter.writeText(" ");
        }
      }
//      else if (node instanceof TableColumnNode)
//      {
//        // maybe write that ..
//        xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE, "col", XmlWriter.CLOSE);
//      }
    }
    catch (IOException e)
    {
      throw new StackableRuntimeException("Failed", e);
    }
  }

  protected boolean startOtherBox(RenderBox box)
  {
//    try
//    {
//      if (box instanceof TableColumnGroupNode)
//      {
//        xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE, "colgroup", XmlWriter.OPEN);
//      }
//    }
//    catch (IOException e)
//    {
//      throw new StackableRuntimeException("Failed", e);
//    }
    return true;
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    RenderNode node = box.getFirstChild();
    while (node != null)
    {
      // A paragraph always has only its line-boxes as direct childs.
      processBoxChilds((RenderBox) node);
      node = node.getNext();
    }
  }


}
