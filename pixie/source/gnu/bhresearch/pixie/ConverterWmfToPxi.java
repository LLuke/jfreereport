//  // ConverterWmfToPxi.java - Parse Windows metafiles into PXI format.
//  //
//  // Copyright (c) 1997-1998 David R Harris.
//  // You can redistribute this work and/or modify it under the terms of the
//  // GNU Library General Public License version 2, as published by the Free
//  // Software Foundation. No warranty is implied. See lgpl.htm for details.
//
//package gnu.bhresearch.pixie;
//
//import java.io.InputStream;
//import java.io.IOException;
//import java.awt.Rectangle;
//import java.awt.Point;
//import java.awt.Font;
//import gnu.bhresearch.quant.Debug;
//import gnu.bhresearch.quant.Assert;
//import gnu.bhresearch.pixie.wmf.*;
//
///**
//	Convert Windows metafiles into PXI format.
//*/
//public class ConverterWmfToPxi extends ConverterToPxi {
//  private String inName;
//  private MfParser metafile;
//
//  public boolean canAppend()
//  {
//    return true;
//  }
//
//  public int canConvert( String inName, InputStream in )
//  {
//    switch (MfParser.isMetafile( inName, in ))
//    {
//    default: Assert.failed();
//    case MfParser.QUALITY_NO: return QUALITY_NO;
//    case MfParser.QUALITY_MAYBE: return QUALITY_MAYBE;
//    case MfParser.QUALITY_YES: return QUALITY_YES;
//    }
//  }
//
//  public void convert( String inName ) throws Exception
//  {
//    this.inName = inName;
//    metafile = new MfParser( inName );
//    parseHeader();
//    parseRecords();
//  }
//
//  private Transform transform = null;
//
//  /** Read the metafile header. */
//  protected void parseHeader() throws ConverterException, IOException
//  {
//    MfHeader header = metafile.readHeader();
//    //Debug.println( header.isPlaceable() ?
//    //        "Placeable" : "not placeable" );
//
//    if (!header.isValid())
//      throw new ConverterException( inName + "is not a real metafile" );
//  }
//
//  /** Flag to help postpone processing the header until the first
//  marking record is seen. Hopefully we will have info about the
//  mapping mode by then. */
//  private boolean doneHeader;
//
//  /** Read and interpret the body of the metafile. */
//  protected void parseRecords() throws IOException
//  {
//    MfRecord mf;
//    while ((mf = metafile.readNextRecord()) != null)
//      parseRecord( mf );
//    if (!isAppending())
//      out.endFrame();
//  }
//
//  /** Interpret a single metafile record. */
//  protected void parseRecord( MfRecord mf ) throws IOException
//  {
//    //Debug.println( mf.toString() );
//    if (!doneHeader && mf.doesMark())
//    {
//      doneHeader = true;
//      doHeader();
//    }
//
//    // Update mapping mode transform.
//    if (mf.isMappingMode())
//      transform = null;
//    if (mf.doesMark() && transform == null)
//      transform = calcTransform();
//
//    // Do the right thing for this record.
//    switch (mf.getType())
//    {
//    default:
//      if (mf.doesMark())
//        System.err.println(
//          "Warning: record type " +
//          mf.getName() + " [" + mf.getType() + "] was ignored" );
//      break;
//    case MfType.LINE_TO:
//      parseLineTo( mf );
//      break;
//    case MfType.RECTANGLE:
//      parseRectangle( mf );
//      break;
//    case MfType.ELLIPSE:
//      parseEllipse( mf );
//      break;
//    case MfType.POLY_POLYGON:
//      parsePolyPolygon( mf );
//      break;
//    case MfType.POLYGON:
//      parsePolygon( mf );
//      break;
//    case MfType.POLYLINE:
//      parsePolyline( mf );
//      break;
//    case MfType.EXT_TEXT_OUT:
//      parseExtTextOut( mf );
//      break;
//    case MfType.TEXT_OUT:
//      parseTextOut( mf );
//      break;
//    }
//
//    // Allow metafile to track DC state.
//    metafile.updateState( mf );
//  }
//
//  /** Process the header record. */
//  protected void doHeader() throws IOException
//  {
//    // Try to preserve the aspect ratio
//    int width, height;
//    MfHeader header = metafile.getHeader();
//    // Use true size if possible.
//    if (header.isPlaceable())
//    {
//      Rectangle bbox = header.getBBox();
//      width = bbox.width;
//      height = bbox.height;
//    }
//    else
//    {
//      // Use window extents.
//      MfDcState state = metafile.getState();
//      width = state.windowExtX;
//      height = state.windowExtY;
//    }
//
//    if (width < 0)
//      width = -width;
//    if (height < 0)
//      height = -height;
//
//    setDefaultSize( width, height );
//
//    if (!isAppending())
//    {
//      // Some comments.
//      out.outputComment( Constants.COMMENT_SOURCE,
//        inName );
//      out.outputComment( Constants.COMMENT_DATE_CREATED,
//        System.currentTimeMillis() );
//      out.outputComment( Constants.COMMENT_APPLICATION,
//        getClass().getName() );
//
//      out.setLogicalSize( getOutBox() );
//      out.startFrame(-1);
//    }
//  }
//
//  /** Treat lineTo as a curve. */
//  protected void parseLineTo( MfRecord mf ) throws IOException
//  {
//    Curve curve = new Curve(2);
//    MfDcState state = metafile.getState();
//    curve.add( state.curPosX, state.curPosY );
//    curve.add( mf.getParam(1), mf.getParam(0) );
//    curve = transformCurve( curve );
//    outputCurve( curve, false );
//  }
//
//  protected void parseRectangle( MfRecord mf ) throws IOException
//  {
//    outputBox( parseBox( mf, 0 ), false );
//  }
//
//  protected void parseEllipse( MfRecord mf ) throws IOException
//  {
//    outputBox( parseBox( mf, 0 ), true );
//  }
//
//  protected Rectangle parseBox( MfRecord mf, int offset ) throws IOException
//  {
//    int left = transform.applyToX( mf.getParam(offset+3) );
//    int top = transform.applyToY( mf.getParam(offset+2) );
//    int right = transform.applyToX( mf.getParam(offset+1) );
//    int bottom = transform.applyToY( mf.getParam(offset+0) );
//    return new Rectangle( left, top, right-left, bottom-top );
//  }
//
//  /** Output an ellipse or rectangle. */
//  protected void outputBox( Rectangle box, boolean isEllipse ) throws IOException
//  {
//    if (outputBrush())
//      if (isEllipse)
//        out.outputQuickEllipse( true, box );
//      else
//        out.outputQuickRectangle( true, box );
//
//      if (outputPen())
//        if (isEllipse)
//          out.outputQuickEllipse( false, box );
//        else
//          out.outputQuickRectangle( false, box );
//  }
//
//  /** Java doesn't have a polypolygon command, so we may need to convert it
//  into a polygon. */
//  protected void parsePolyPolygon( MfRecord mf ) throws IOException
//  {
//    int inPolyCnt = mf.getParam(0);
//    if (inPolyCnt == 0)
//      return;
//    Curve curves[] = new Curve[inPolyCnt];
//
//    // Parse the individual polygons.
//    int totalPointCnt = inPolyCnt-1;
//    int offset = inPolyCnt+1;
//    int polyCnt = 0;
//    for (int i = 0; i < inPolyCnt; i++)
//    {
//      int cnt = mf.getParam(i+1);
//      curves[polyCnt] = getPoly( mf, offset, cnt );
//      offset += 2*cnt;
//      if (curves[polyCnt].getSize() < 2)
//        continue;
//      totalPointCnt += curves[polyCnt].getSize();
//      polyCnt++;
//    }
//
//    // Seperate out any that don't overlap.
//    int polyCnt2 = 0;
//    for (int i = 0; i < polyCnt; i++)
//    {
//      Rectangle bBox = curves[i].getBBox();
//      boolean overlap = false;
//      for (int j = polyCnt-1; j >= 0; j--)
//        if (j != i)
//          if (bBox.intersects( curves[j].getBBox()))
//          {
//            overlap = true;
//            break;
//          }
//          if (overlap)
//            curves[polyCnt2++] = curves[i];
//          else
//            outputCurve( curves[i], true );
//    }
//    polyCnt = polyCnt2;
//
//    // Simplify.
//    for (int i = polyCnt-1; i >= 0; i--)
//      if (isEllipse( curves[i] ))
//        curves[i] = simplifyEllipse( curves[i] );
//      if (polyCnt == 0)
//        return;
//
//      // Fill remainder as one big polygon.
//      if (outputBrush())
//      {
//        Curve curve = new Curve(totalPointCnt);
//        curve.add( curves[0] );
//        Point start = curve.get( curve.getSize()-1 );
//        for (int i = 1; i < polyCnt; i++)
//        {
//          curve.add( curves[i] );
//          curve.add( start, false );
//        }
//        out.outputQuickCurve( true, curve );
//      }
//
//      // Outlines don't need PolyPoly; they can be separate polygons.
//      if (outputPen())
//        for (int i = 0; i < polyCnt; i++)
//        {
//          curves[i].addClose();
//          out.outputQuickCurve( false, curves[i] );
//        }
//  }
//
//  protected void parsePolygon( MfRecord mf ) throws IOException
//  {
//    int cnt = mf.getParam(0);
//    if (cnt > 0)
//      outputCurve( getPoly( mf, 1, cnt ), true );
//  }
//
//  protected void parsePolyline( MfRecord mf ) throws IOException
//  {
//    int cnt = mf.getParam(0);
//    if (cnt > 0)
//      outputCurve( getPoly( mf, 1, cnt ), false );
//  }
//
//  /** Parse a polygon and return it ready-transformed. */
//  protected Curve getPoly( MfRecord mf, int offset, int cnt )
//  {
//    Assert.assert( (offset+cnt+3)*2 <= mf.getLength() );
//    Curve curve = new Curve( cnt );
//    for (int i = 0; i < cnt; i++, offset += 2)
//    {
//      int x = mf.getParam( offset );
//      int y = mf.getParam( offset+1 );
//      x = transform.applyToX( x );
//      y = transform.applyToY( y );
//      curve.add( x, y, false );
//    }
//    curve.removeColinear();
//    return curve;
//  }
//
//  protected Curve transformCurve( Curve curve )
//  {
//    Curve result = new Curve( curve.getSize() );
//    for (int i = 0; i < curve.getSize(); i++)
//    {
//      int x = transform.applyToX( curve.getX(i) );
//      int y = transform.applyToY( curve.getY(i) );
//      result.add( x, y, curve.isBezier(i) );
//    }
//    result.removeColinear();
//    return result;
//  }
//
//  /** Output a curve. If it is roughly elliptical, output an ellipse instead
//  because that's much smaller. */
//  protected void outputCurve( Curve curve, boolean isFill ) throws IOException
//  {
//    if (curve.getSize() < 2)
//      return;
//    if (isFill)
//      curve.addClose();
//
//    boolean isEllipse = isEllipse( curve );
//    Rectangle box = curve.getBBox();
//
//    if (isFill && outputBrush())
//    {
//      if (isEllipse)
//        out.outputQuickEllipse( true, box );
//      else
//        out.outputQuickCurve( true, curve );
//    }
//
//    if (outputPen())
//    {
//      if (isEllipse && curve.isClosed())
//        out.outputQuickEllipse( false, box );
//      else
//        out.outputQuickCurve( false, curve );
//    }
//  }
//
//  /** True if the curve approximates an ellipse. */
//  protected boolean isEllipse( Curve curve )
//  {
//    if (curve.getSize() < 50)
//      return false;
//
//    // Use the formula x*x/a*a + y*y/b*b == 1.
//    Rectangle box = curve.getBBox();
//    float a = box.width / 2.0f;
//    float b = box.height / 2.0f;
//    if (a == 0 || b == 0)
//      return false;
//    float ox = box.x + a;
//    float oy = box.y + b;
//    a = a*a;
//    b = b*b;
//    float epsilon = 0.05f;
//
//    int maxLen = Math.max( box.width, box.height ) / 10;
//    int maxLen2 = maxLen*maxLen;
//
//    for (int i = 0; i < curve.getSize(); i++)
//    {
//      if (curve.isBezier(i))
//        return false;
//      float x = curve.getX(i)-ox;
//      float y = curve.getY(i)-oy;
//      float err = (x*x)/a + (y*y)/b - 1.0f;
//      if (err < -epsilon || err > epsilon)
//        return false;
//
//      // Are points evenly spaced?
//      int prevI = (i > 0) ? i-1 : curve.getSize()-1;
//      int dx = curve.getX(i) - curve.getX(prevI);
//      int dy = curve.getY(i) - curve.getY(prevI);
//      int len2 = dx*dx + dy*dy;
//      if (len2 > maxLen2)
//        return false;
//    }
//    return true;
//  }
//
//  /** Return an ellipse which is simpler than the given ellipse. */
//  protected Curve simplifyEllipse( Curve curve )
//  {
//    Curve ellipse = new Curve();
//    ellipse.addEllipse( curve.getBBox() );
//    return (ellipse.getSize() < curve.getSize()) ? ellipse : curve;
//  }
//
//  /** Recalculate transform based on the current DC state. */
//  protected Transform calcTransform()
//  {
//    MfDcState state = metafile.getState();
//    Rectangle src = new Rectangle(
//      state.windowOrgX,
//      state.windowOrgY,
//      state.windowExtX,
//      state.windowExtY );
//    //Debug.println( "transform src=" + src + ", out=" + getOutBox() );
//    return new Transform( getOutBox(), src );
//  }
//
//  // Only display warning messages once.
//  private boolean doneBrushStyleWarning;
//  private boolean donePenWidthWarning;
//  private boolean donePenStyleWarning;
//
//  /** Output current brush color. Return false if transparent. */
//  protected  boolean outputBrush() throws IOException
//  {
//    MfLogBrush brush = metafile.getState().logBrush;
//    if (brush == null)
//      brush = new MfLogBrush();
//    if (brush.getColor() == null)
//      return false;
//    if (!brush.isSimpleStyle())
//      if (!doneBrushStyleWarning)
//      {
//        doneBrushStyleWarning = true;
//        System.out.println( "Warning: brush style " +
//          brush.getStyle() + " was ignored" );
//      }
//
//      out.outputQuickSetColor( brush.getColor() );
//      return true;
//  }
//
//  /** Output current pen color. Return false if transparent. */
//  protected boolean outputPen() throws IOException
//  {
//    MfLogPen pen = metafile.getState().logPen;
//    if (pen == null)
//      pen = new MfLogPen();
//    if (pen.getColor() == null)
//      return false;
//    if (pen.getWidth() > 1)
//    {
//      if (!donePenWidthWarning)
//      {
//        donePenWidthWarning = true;
//        System.out.println( "Warning: pen width " +
//          pen.getWidth() + " was ignored" );
//      }
//    }
//    if (!pen.isSimpleStyle())
//    {
//      if (!donePenStyleWarning)
//      {
//        donePenStyleWarning = true;
//        System.out.println( "Warning: pen style " +
//          pen.getStyle() + " ignored" );
//      }
//    }
//    out.outputQuickSetColor( pen.getColor() );
//    return true;
//  }
//
//  protected void parseExtTextOut( MfRecord mf ) throws IOException
//  {
//    MfDcState state = metafile.getState();
//
//    int options = mf.getParam(3);
//    if ((options & 0x02) != 0)
//    {
//      // Fill the background rectangle.
//      int rx = mf.getParam(4);
//      int ry = mf.getParam(5);
//      int rw = mf.getParam(6) - rx;
//      int rh = mf.getParam(7) - ry;
//      Rectangle box = transform.applyTo( new Rectangle( rx, ry, rw, rh ) );
//      out.outputQuickSetColor( state.bkColor );
//      out.outputQuickRectangle( true, box );
//    }
//
//    // Number of characters.
//    int cnt = mf.getParam(2);
//    if (cnt == 0)
//      return;
//
//    int x = transform.applyToX( mf.getParam(1) );
//    int y = transform.applyToY( mf.getParam(0) );
//    String text = mf.getStringParam( (options == 0) ? 4 : 8, cnt );
//
//    outputText( text, x, y );
//  }
//
//  protected void parseTextOut( MfRecord mf ) throws IOException
//  {
//    // Number of characters.
//    int cnt = mf.getParam(0);
//    if (cnt == 0)
//      return;
//
//    String text = mf.getStringParam( 1, cnt );
//    int wordCnt = (cnt+1)/2;
//    int x = transform.applyToX( mf.getParam(wordCnt+2) );
//    int y = transform.applyToY( mf.getParam(wordCnt+1) );
//    outputText( text, x, y );
//  }
//
//  protected void outputText( String text, int x, int y ) throws IOException
//  {
//    // Convert the current font.
//    MfDcState state = metafile.getState();
//    MfLogFont logFont = state.logFont;
//    String face = logFont.getFace();
//    if (!out.isKnownFont( face ))
//      System.out.println("Warning: \"" + face + "\" is not a portable font name." );
//    face = out.mapFont( face );
//    int size = logFont.getSize();
//    if (size < 0)
//      size = -size;
//    else
//    {
//      // We must exclude internal leading, but we have no idea
//      // what that might be. Try 10%.
//      size -= size/10;
//    }
//    size = Math.abs( transform.applyToHeight( size ) );
//    out.outputQuickSetFont( new Font( face, logFont.getStyle(), size ) );
//
//    // Any remaining attributes.
//    int flags = 0;
//    if (logFont.isUnderline())
//      flags |= Constants.TEXT_UNDERLINE;
//    if ((state.textAlign & 0x06) == 0x02)
//      flags |= Constants.TEXT_RIGHT;
//    else if ((state.textAlign & 0x06) == 0x06)
//      flags |= Constants.TEXT_CENTER;
//
//    // Output the text in the right color.
//    out.outputQuickSetColor( state.fgColor );
//    out.outputQuickFillText( text, x, y, flags );
//  }
//}
