//  // WviGenerator.java - generate Pxi a file.
//  //
//  // Copyright (c) 1997-1998 David R Harris.
//  // You can redistribute this work and/or modify it under the terms of the
//  // GNU Library General Public License version 2, as published by the Free
//  // Software Foundation. No warranty is implied. See lgpl.htm for details.
//
//
//package gnu.bhresearch.pixie;
//
//import java.io.DataOutputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.File;
//import java.awt.Color;
//import java.awt.Font;
//import java.awt.Rectangle;
//import java.awt.Dimension;
//import java.util.Vector;
//import java.util.Stack;
//import gnu.bhresearch.quant.Debug;
//import gnu.bhresearch.quant.Assert;
//import gnu.bhresearch.quant.Range;
//
///**
//	Generate a Pxi file. This class hides the details of the low level output
//	format from the main program.
//
//	<P>The "quick" versions of commands usually do some kind of optimisation.
//	Most coordinates are stored in relative form.
//*/
//public class PxiGenerator 
//{
//  public class PxiGeneratorState
//  {  // For defineObject.
//    public final ByteArrayOutputStream outByteArrayStream;
//    public final VIntOutputStream out;
//
//    public PxiGeneratorState(
//      ByteArrayOutputStream outByteArrayStream,
//      VIntOutputStream out )
//    {
//      this.outByteArrayStream = outByteArrayStream;
//      this.out = out;
//    }
//  }
//
//  private ByteArrayOutputStream outByteArrayStream = null;
//  private VIntOutputStream out = null;
//  private Vector frameOffsets = new Vector();
//  private Vector framePauses = new Vector();
//  private Vector objectOffsets = new Vector();
//  private Stack defineObjectStack = new Stack();
//  private int nextObjectId = 0;  // For defineObject.
//  private Vector pendingObjects = new Vector();
//  private int version = 0;
//  private int logWidth = Constants.DEFAULT_LOG_WIDTH;
//  private int logHeight = Constants.DEFAULT_LOG_HEIGHT;
//  private int[] recordCounts = new int[ Constants.CMD_MAX ];
//  private boolean inFrame = false;
//  private Font curFont = null;
//  private Color curColor = null;
//  private Font pendingFont = null;
//  private Color pendingColor = null;
//  private int prevX = 0;
//  private int prevY = 0;
//
//  /** Constructor. */
//  public PxiGenerator() throws IOException
//  {
//    outByteArrayStream = new ByteArrayOutputStream( 1024 );
//    out = new VIntOutputStream( outByteArrayStream );
//  }
//
//  /** Number of records written so far. */
//  public int getRecordCount()
//  {
//    int sum = 0;
//    for (int i = 0; i < recordCounts.length; i++)
//      sum += recordCounts[i];
//    return sum;
//  }
//
//  /** Number of records written so far. */
//  public int getRecordCount( int cmd )
//  {
//    return recordCounts[ cmd ];
//  }
//
//  /** Number of frames. */
//  public int getFrameCount()
//  {
//    return frameOffsets.size();
//  }
//
//  /** Current nesting level of defineObject commands. */
//  public int getDefineObjectLevel()
//  {
//    return defineObjectStack.size();
//  }
//
//  /** Size in pixels. */
//  public void setLogicalSize( Rectangle box )
//  {
//    setLogicalSize( box.width, box.height );
//  }
//
//  /** Size in pixels. */
//  public void setLogicalSize( int logWidth, int logHeight )
//  {
//    Assert.assert( Range.in( 0, logWidth, Constants.MAX_LOG_WIDTH ) );
//    Assert.assert( Range.in( 0, logHeight, Constants.MAX_LOG_HEIGHT ) );
//    this.logWidth = logWidth;
//    this.logHeight = logHeight;
//  }
//
//  /** Version number. */
//  public void setHeaderVersion( int version )
//  throws IOException
//  {
//    this.version = version;
//  }
//
//  /** Pause between given frame and the next. */
//  public void setFramePause( int frame, int pause )
//  {
//    framePauses.setElementAt( new Integer( pause ), frame );
//  }
//
//  /** Pause between given frame and the next. */
//  public int getFramePause( int frame )
//  {
//    return ((Integer)framePauses.elementAt( frame )).intValue();
//  }
//
//  /** Get current font. */
//  public Font getCurFont()
//  {
//    return pendingFont == null ? curFont : pendingFont;
//  }
//
//  /** Current color. */
//  public Color getCurColor()
//  {
//    return pendingColor == null ? curColor : pendingColor;
//  }
//
//  private void clearRenderState()
//  {
//    curFont = pendingFont = null;
//    curColor = pendingColor = null;
//    prevX = prevY = 0;
//  }
//
//  /** Save everything to file. */
//  public void writeTo( String outName ) throws IOException
//  {
//    VIntOutputStream file = null;
//	    try {
//			file =
//				new VIntOutputStream(
//					new FileOutputStream( outName ) );
//      writeHeaderTo( file );
//      writeBodyTo( file );
//      file.close();
//      }
//		catch (IOException e) {
//			if (file != null)
//				file.close();
//      new File( outName ).delete();
//      throw e;
//      }
//	}
//
//  /** Write the header. */
//  public void writeHeaderTo( VIntOutputStream file ) throws IOException
//  {
//    // Must have at least 1 frame.
//    Assert.assert( frameOffsets.size() > 0 );
//
//    // Frame data must keep in sync.
//    Assert.assert( frameOffsets.size() == framePauses.size() );
//
//    file.writeInt( Constants.MAGIC );
//
//    // Choose version 3.00 or 3.03 depending on features used.
//    int lowestVersion;
//    if (frameOffsets.size() > 1)
//      lowestVersion = 303;
//    else if (recordCounts[ Constants.CMD_USE_OBJECT ] != 0 ||
//      objectOffsets.size() > 0)
//      lowestVersion = 302;
//    else if (recordCounts[ Constants.CMD_HOT_SPOT ] != 0)
//      lowestVersion = 301;
//    else
//      lowestVersion = 300;
//
//    if (version == 0)  // Default to lowest.
//      version = lowestVersion;
//    Assert.assert( version >= lowestVersion );
//    file.writeShort( version );
//
//    file.writeUnsignedVInt( logWidth );
//    file.writeUnsignedVInt( logHeight );
//
//    // Spare fields for future expansion.
//    file.writeUnsignedVInt( 0);
//    file.writeUnsignedVInt( 0 );
//
//    writeFrameTable( file );
//    writeObjectsTable( file );
//
//    // Extensions table.
//    file.writeUnsignedVInt( 0 );
//
//    // A spare field for future expansion.
//    file.writeUnsignedVInt( 0 );
//  }
//
//  /** Frame table. */
//  void writeFrameTable( VIntOutputStream file ) throws IOException
//  {
//    int prevFrame = 0;
//    int prevPause = -1;
//    file.writeUnsignedVInt( frameOffsets.size() );
//    for (int i = 0; i < frameOffsets.size(); i++)
//    {
//      // Always write frame offset.
//      int offset = ((Integer)frameOffsets.elementAt(i)).intValue();
//      Assert.assert( offset >= prevFrame );
//      file.writeUnsignedVInt( offset-prevFrame );
//      prevFrame = offset;
//
//      // Generate pause if different to previous.
//      int pause = getFramePause( i );
//      if (pause != prevPause)
//      {
//        file.writeUnsignedVInt( Constants.CTL_PAUSE );
//        file.writeVInt( pause );
//        prevPause = pause;
//      }
//
//      file.writeUnsignedVInt( Constants.CTL_END );
//    }
//  }
//
//  /** Objects table */
//  void writeObjectsTable( VIntOutputStream file ) throws IOException
//  {
//    file.writeUnsignedVInt( objectOffsets.size() );
//    for (int i = 0, prev = 0; i < objectOffsets.size(); i++)
//    {
//      int offset = ((Integer)objectOffsets.elementAt(i)).intValue();
//      Assert.assert( offset >= prev );
//      file.writeUnsignedVInt( offset-prev );
//      prev = offset;
//    }
//  }
//
//  /** Write the body (including its length). */
//  public void writeBodyTo( VIntOutputStream file ) throws IOException
//  {
//    out.flush();
//    Assert.assert( out.size() == outByteArrayStream.size() );
//    file.writeUnsignedVInt( outByteArrayStream.size() );
//    file.flushVInt();
//    outByteArrayStream.writeTo( file );
//  }
//
//  /** Start a new frame. */
//  public void startFrame( int pause ) throws IOException
//  {
//    // Can't nest frames.
//    Assert.assert( !inFrame );
//    // Can't reuse whole frames.
//    Assert.assert( getDefineObjectLevel() == 0 );
//    out.flush();
//    frameOffsets.addElement( new Integer( out.size() ) );
//    framePauses.addElement( new Integer( pause ) );
//    clearRenderState();
//    inFrame = true;
//  }
//
//  /** Start a new frame. */
//  public void endFrame() throws IOException
//  {
//    // Ensure correct nesting.
//    Assert.assert( inFrame );
//    Assert.assert( getDefineObjectLevel() == 0 );
//    outputCommand( Constants.CMD_END );
//    inFrame = false;
//
//    // Output any outstanding object definitions in the space between
//    // frames. That way all the data for frame N is sent before frame
//    // N+1 starts.
//    outputPendingObjects();
//  }
//
//  /** Start recording a re-usable graphics object. */
//  public void startDefineObject() throws IOException
//  {
//    // Store current state.
//    defineObjectStack.push( new PxiGeneratorState(
//      outByteArrayStream, out ) );
//
//    // Start writing to new streams.
//    outByteArrayStream = new ByteArrayOutputStream( 1024 );
//    out = new VIntOutputStream( outByteArrayStream );
//    clearRenderState();
//  }
//
//  /** End recording a re-usable graphics object. */
//  public Object endDefineObject() throws IOException
//  {
//    Assert.assert( getDefineObjectLevel() > 0 );
//    Assert.assert( nextObjectId ==
//      objectOffsets.size()+pendingObjects.size() );
//
//    // End current stream.
//    outputCommand( Constants.CMD_END );
//
//    // Store current stream in pending tray.
//    out.flush();
//    int size = out.size();
//    Assert.assert( out.size() == outByteArrayStream.size() );
//    pendingObjects.addElement( outByteArrayStream );
//
//    // Fetch and restore old state.
//    PxiGeneratorState state = (PxiGeneratorState)defineObjectStack.pop();
//    outByteArrayStream = state.outByteArrayStream;
//    out = state.out;
//
//    if (size == 1)
//    {
//      // Empty object. Delete it.
//      pendingObjects.removeElementAt( pendingObjects.size()-1 );
//      return null;
//    }
//
//    // Return object ID.
//    return new Integer( nextObjectId++ );
//  }
//
//  /** Output any defined objects that are waiting. */
//  private void outputPendingObjects() throws IOException
//  {
//    Assert.assert( nextObjectId ==
//      objectOffsets.size()+pendingObjects.size() );
//    for (int i = 0; i < pendingObjects.size(); i++)
//    {
//
//      // Fetch the bytes of this object.
//      ByteArrayOutputStream object = (ByteArrayOutputStream)
//        pendingObjects.elementAt(i);
//      // Store offset to current position, for the header.
//      out.flush();
//      objectOffsets.addElement( new Integer( out.size() ) );
//      // Append the bytes.
//      object.writeTo( out );
//    }
//    pendingObjects.removeAllElements();
//
//    Assert.assert( pendingObjects.size() == 0 );
//    Assert.assert( nextObjectId == objectOffsets.size() );
//  }
//
//  /** Use a previously recorded graphics object. */
//  public void outputUseObject( Object idObj ) throws IOException
//  {
//    outputUseObject( idObj, new Rectangle(0,0,logWidth,logHeight) );
//  }
//
//  /** Use a previously recorded graphics object. */
//  public void outputUseObject( Object idObj, Rectangle box ) throws IOException
//  {
//    // Can only use objects that have been defined.
//    int id = ((Integer)idObj).intValue();
//    Assert.assert( Range.in( 0, id, nextObjectId ) );
//    Assert.assert( nextObjectId ==
//      objectOffsets.size()+pendingObjects.size() );
//
//    outputCommand( Constants.CMD_USE_OBJECT );
//    out.writeUnsignedVInt( id );
//    outputRectangle( box );
//    out.flushVInt();
//
//    // Render state is undefined after a USE_OBJECT, so that the
//    // renderer can cache objects as bitmaps.
//    clearRenderState();
//    report( "USE " + id );
//  }
//
//  /** Output a 64-bit comment. */
//  public void outputComment( int type, long comment ) throws IOException
//  {
//    byte bytes[] = new byte[ 8 ];
//    for (int i = 0; i < 8; i++)
//      bytes[i] = (byte)(comment >> ((7-i)*8));
//    outputComment( type, bytes );
//  }
//
//  /** Output a string comment. */
//  public void outputComment( int type, String comment ) throws IOException
//  {
//    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//    DataOutputStream dos = new DataOutputStream( baos );
//    dos.writeUTF( comment );
//    dos.close();
//    outputComment( type, baos.toByteArray() );
//  }
//
//  /** Output a byte-array comment. */
//  public void outputComment( int type, byte[] comment ) throws IOException
//  {
//    outputComment( type, comment, 0, comment.length );
//  }
//
//  /** Output a byte-array comment. */
//  public void outputComment( int type, byte[] comment,
//    int off, int len ) throws IOException
//  {
//    Assert.assert( Range.in( Constants.COMMENT_MIN, type,
//      Constants.COMMENT_MAX ) );
//
//    outputCommand( Constants.CMD_COMMENT );
//    out.writeUnsignedVInt( type );
//    out.writeUnsignedVInt( len );
//    out.flushVInt();
//    out.write( comment, off, len );
//    report( "COMMENT " + type + " " + len );
//  }
//
//  /** Set the current color. It won't be output until its needed, to avoid
//  spurious changes. */
//  public void outputQuickSetColor( int red, int green, int blue )
//  throws IOException
//  {
//    Assert.assert( Range.in( 0, red, 256 ) );
//    Assert.assert( Range.in( 0, green, 256 ) );
//    Assert.assert( Range.in( 0, blue, 256 ) );
//    outputQuickSetColor( new Color( red, green, blue ) );
//  }
//
//  /** Set the current color. It won't be output until its needed, to avoid
//  spurious changes. */
//  public void outputQuickSetColor( Color color ) throws IOException
//  {
//    pendingColor = color;
//  }
//
//  /** Resync output color with current. */
//  private void outputPendingColor() throws IOException
//  {
//    // If a color change is pending, and the new color is different
//    // to the last one output, output it now.
//    if (pendingColor != null)
//    {
//      if (curColor == null || !curColor.equals( pendingColor ))
//        outputSetColor( pendingColor );
//      pendingColor = null;
//    }
//  }
//
//  /** Output color. */
//  public void outputSetColor( Color color ) throws IOException
//  {
//    pendingColor = null;
//    curColor = color;
//    outputCommand( Constants.CMD_SET_COLOR );
//    out.writeUnsignedVInt( color.getRGB() & 0x0ffffff );
//    report( "SET_COLOR " + color );
//  }
//
//  /** Set the current font. It won't be output until its needed, to avoid
//  spurious changes. */
//  public void outputQuickSetFont( Font font ) throws IOException
//  {
//    Assert.assert( font.getSize() > 0 );
//    pendingFont = font;
//  }
//
//  /** Resync output font with current. */
//  private void outputPendingFont() throws IOException
//  {
//    if (pendingFont != null)
//    {
//      if (curFont == null || !curFont.equals( pendingFont ))
//        outputSetFont( pendingFont );
//      pendingFont = null;
//    }
//  }
//
//  /** Output a font change. */
//  public void outputSetFont( Font font )throws IOException
//  {
//    Assert.assert( font.getSize() > 0 );
//    pendingFont = null;
//    curFont = font;
//
//    outputCommand( Constants.CMD_SET_FONT );
//    outputString( font.getName() );
//    out.writeUnsignedVInt( font.getStyle() );
//    out.writeUnsignedVInt( font.getSize() );
//    prevY += font.getSize();
//    report( "SET_FONT " + font );
//  }
//
//  /** Output text if not empty. */
//  public void outputQuickFillText( String text, int x, int y, int flags )
//  throws IOException
//  {
//    if (text.length() == 0)
//      return;
//    outputFillText( text, x, y, flags );
//  }
//
//  /** Output text. */
//  public void outputFillText( String text, int x, int y, int flags )
//  throws IOException
//  {
//    Assert.assert( text.length() > 0 );
//    Assert.assert( Range.in( -Constants.MAX_LOG_WIDTH, x, Constants.MAX_LOG_WIDTH ) );
//    Assert.assert( Range.in( -Constants.MAX_LOG_HEIGHT, y, Constants.MAX_LOG_HEIGHT ) );
//    // Can't have both center and right justify.
//    Assert.assert( (flags & Constants.TEXT_JUSTIFY_MASK) != Constants.TEXT_JUSTIFY_MASK);
//    // Only bottom 3 bits of mask are used.
//    Assert.assert( (flags & ~Constants.TEXT_STYLE_MASK) == 0 );
//    Assert.assert( getCurFont() != null );
//    Assert.assert( getCurColor() != null );
//    outputPendingColor();
//    outputPendingFont();
//
//    outputCommand( Constants.CMD_FILL_TEXT );
//    outputString( text );
//    out.writeVInt( x-prevX );
//    out.writeVInt( y-prevY );
//    out.writeUnsignedVInt( flags );
//    prevX = x;
//    prevY = y;
//    report( "FILL_TEXT " + text + " p=" + x + ',' + y );
//  }
//
//  /** Output a hotspot. */
//  public void outputHotSpot( String arg, Rectangle r, int cmd )
//  throws IOException
//  {
//    outputHotSpot( arg, r.x, r.y, r.width, r.height, cmd );
//  }
//
//  /** Output a hotspot. */
//  public void outputHotSpot( String arg, int x, int y, int w, int h, int cmd )
//  throws IOException
//  {
//    Assert.assert( cmd >= Constants.HOT_SPOT_MIN );
//    out.writeUnsignedVInt( Constants.CMD_HOT_SPOT );
//    out.flushVInt();
//    out.writeUTF( arg );
//    outputRectangle( x, y, w, h );
//    out.writeVInt( cmd );
//    report( "HOT_SPOT " + arg );
//  }
//
//  /** Output an ellipse any way you like. Empty ones are ignored,
//  degenerate ones are drawn with boxes. */
//  public void outputQuickEllipse( boolean isFill, Rectangle box )
//  throws IOException
//  {
//    if (!outputQuickShape( isFill, box ))
//      outputEllipse( isFill, box );
//  }
//
//  /** Output a Rectangle any way you like. Empty ones are ignored,
//  degenerate ones are drawn with boxes. */
//  public void outputQuickRectangle( boolean isFill, Rectangle box )
//  throws IOException
//  {
//    if (!outputQuickShape( isFill, box ))
//      outputRectangle( isFill, box );
//  }
//
//  /** Output a degnerate box. */
//  private boolean outputQuickShape( boolean isFill, Rectangle box )
//  throws IOException
//  {
//    // It may be quicker to draw as a rectangle.
//    if (isFill)
//    {
//      if (box.width == 0 || box.height == 0)
//        return true;  // Nothing to draw.
//      else if (box.width == 1 || box.height == 1)
//      {
//        outputRectangle( true, box );
//        return true;
//      }
//      else
//        return false;
//    }
//    else
//    {
//      if (box.width == 0 || box.height == 0)
//      {
//        box.width++;
//        box.height++;
//        outputRectangle( true, box );
//        box.width--;
//        box.height--;
//        return true;
//      }
//      else
//        return false;
//    }
//  }
//
//  /** Output a curve any way you like. Trivial ones are ignored, degenerate
//  ones are replaced with boxes. */
//  public void outputQuickCurve( boolean isFill, Curve curve )
//  throws IOException
//  {
//    if (curve.getSize() < 2)
//      return;
//
//    Rectangle bBox = curve.getBBox();
//    if (curve.equals( bBox ))
//    {
//      outputQuickRectangle( isFill, bBox );
//      return;
//    }
//
//    // Filled curves are closed automatically.
//    if (isFill && curve.isClosed() &&
//      !curve.isBezier( curve.getSize()-1 ))
//    {
//      curve.setSize( curve.getSize() - 1 );
//      outputCurve( isFill, curve );
//      curve.addClose();
//      return;
//    }
//
//    outputCurve( isFill, curve );
//  }
//
//  /** Output an ellipse. */
//  public void outputEllipse( boolean isFill, Rectangle box )
//  throws IOException
//  {
//    Assert.assert( getCurColor() != null );
//    outputPendingColor();
//    outputCommand( isFill ?
//      Constants.CMD_FILL_ELLIPSE : Constants.CMD_STROKE_ELLIPSE );
//    outputRectangle( box );
//    report( "ELLIPSE " + isFill + " " + box );
//  }
//
//  /** Output a rectangle. */
//  public void outputRectangle( boolean isFill, Rectangle box )
//  throws IOException
//  {
//    Assert.assert( getCurColor() != null );
//    outputPendingColor();
//    outputCommand( isFill ?
//      Constants.CMD_FILL_RECTANGLE : Constants.CMD_STROKE_RECTANGLE );
//    outputRectangle( box );
//    report( "RECT " + isFill + " " + box );
//  }
//
//  /** Output a curve. */
//  public void outputCurve( boolean isFill, Curve curve ) throws IOException
//  {
//    curve.assertValid();
//    Assert.assert( curve.getSize() > 1 );
//    Assert.assert( getCurColor() != null );
//    boolean isCurve = (curve.getBezierCount() > 0);
//    int cmd = isFill ? 
//      (isCurve ? Constants.CMD_FILL_CURVE : Constants.CMD_FILL_POLYGON) :
//      (isCurve ? Constants.CMD_STROKE_CURVE : Constants.CMD_STROKE_POLYLINE);
//    outputPendingColor();
//
//    outputCommand( cmd );
//    outputCurveSegCount( curve );
//    if (isCurve)
//      outputBezierFlags( curve );
//    outputCoords( curve, false );
//    outputCoords( curve, true );
//    report( "CURVE " + isFill + " " + curve.getSize() );
//  }
//
//  /** Output the length of a curve. */
//  private void outputCurveSegCount( Curve curve ) throws IOException
//  {
//    int segCnt = 0;
//    for (int i = 0; i < curve.getSize(); i++)
//    {
//      segCnt++;
//      if (curve.isBezier(i)) // Bezier consume 2 extra points.
//        i += 2;
//    }
//    out.writeUnsignedVInt( segCnt );
//  }
//
//  /** Output the array of curve types. */
//  private void outputBezierFlags( Curve curve ) throws IOException
//  {
//    // Store this binary array using run length encoding.
//    int i = 0;
//    while (i < curve.getSize())
//    {
//      int runLen;
//      for (runLen = 0; i < curve.getSize(); runLen++, i++)
//        if (curve.isBezier(i))
//          break;
//        // First point of a curve is never a bezier.
//        Assert.assert( runLen > 0 );
//        out.writeUnsignedVInt( runLen );
//        if (i == curve.getSize())
//          break;
//
//        for (runLen = 0; i < curve.getSize(); runLen++, i++)
//          if (!curve.isBezier(i))
//            break;
//          Assert.assert( runLen > 0 );
//          // Beziers always come in multiples of 3 points.
//          Assert.assert( runLen % 3 == 0 );
//          out.writeUnsignedVInt( runLen/3 );
//    }
//  }
//
//  /** Output either the X or the Y coordinates of a curve. */
//  private void outputCoords( Curve curve, boolean isY ) throws IOException
//  {
//    Assert.assert( curve.getSize() > 1 );
//    out.writeVInt( curve.get(0,isY) - (isY ? prevY : prevX) );
//    for (int i = 1; i < curve.getSize(); )
//    {
//      if (curve.isBezier(i))
//      {
//        out.writeVInt( curve.get(i,isY) - curve.get(i-1,isY) );
//        out.writeVInt( curve.get(i+1,isY) - curve.get(i,isY) );
//        out.writeVInt( curve.get(i+2,isY) - curve.get(i+1,isY) );
//        i += 3;
//      }
//      else
//      {
//        out.writeVInt( curve.get(i,isY) - curve.get(i-1,isY) );
//        i++;
//      }
//    }
//    if (isY)
//      prevY = curve.get( curve.getSize()-1, isY );
//    else
//      prevX = curve.get( curve.getSize()-1, isY ); 
//  }
//
//  /** Utility to write a rectangle. */
//  private void outputRectangle( Rectangle box ) throws IOException
//  {
//    outputRectangle( box.x, box.y, box.width, box.height );
//  }
//
//  /** Utility to write a rectangle. */
//  private void outputRectangle( int x, int y, int w, int h ) throws IOException
//  {
//    Assert.assert( Range.in( -Constants.MAX_LOG_WIDTH, x, Constants.MAX_LOG_WIDTH ) );
//    Assert.assert( Range.in( -Constants.MAX_LOG_HEIGHT, y, Constants.MAX_LOG_HEIGHT ) );
//    Assert.assert( Range.in( 1, w, Constants.MAX_LOG_WIDTH ) );
//    Assert.assert( Range.in( 1, h, Constants.MAX_LOG_HEIGHT ) );
//    out.writeVInt( x-prevX );
//    out.writeVInt( y-prevY );
//
//    // A size of zero means use the whole image.
//    out.writeUnsignedVInt( w == logWidth ? 0 : w );
//    out.writeUnsignedVInt( h == logHeight ? 0 : h );
//    prevX = x+w;
//    prevY = y+h;
//  }
//
//  /** Utility to write a command. */
//  private void outputCommand( int cmd )throws IOException
//  {
//    out.writeUnsignedVInt( cmd );
//    recordCounts[ cmd ]++;
//  }
//
//  /** Utility to write a string. */
//  private void outputString( String str ) throws IOException
//  {
//    out.flushVInt();
//    out.writeUTF( str );
//  }
//
//  /** For debugging. */
//  private void report( String str )
//  {
//    Debug.println( str );
//  }
//
//  /** Return approx bounding box of the text. */
//  public Rectangle getTextBox( String text, int x, int y, boolean centered )
//  {
//    int size = curFont.getSize();
//    int width = text.length() * size / 2;
//    Rectangle box = new Rectangle();
//    box.x = x;
//    box.y = y - size;
//    box.width = width;
//    box.height = size + size/3;
//    if (centered)
//      box.x -= box.width/2;
//    return box;
//  }
//
//  private static String fontMap[] = {
//		// Windows fonts.
//		"Arial",				"Helvetica",
//		"Times New Roman",		"TimesRoman",
//		"Courier New",			"Courier",
//		"MS Sans Serif",		"Dialog",
//		"WingDings",			"ZapfDingBats",
//
//		// X fonts.
//		"adobe-helvetica",		"Helvetica",
//		"adobe-times",			"TimesRoman",
//		"adobe-courier",		"Courier",
//		"b&h-lucida",			"Dialog",
//		"b&h-lucidatypewriter",	"DialogInput",
//		"itc-zapfdingbats",		"ZapfDingBats"
//	};
//
//  /** Return the Java name of a font. */
//  public String mapFont( String face )
//  {
//    for (int i = 0; i < fontMap.length; i += 2)
//      if (fontMap[i].equalsIgnoreCase( face ))
//        return fontMap[i+1];
//      return face;
//  }
//
//  /** Is this a standard Java font? */
//  public boolean isKnownFont( String face )
//  {
//    for (int i = 0; i < fontMap.length; i++)
//      if (fontMap[i].equals( face ))
//        return true;
//      return false;
//  }
//}
