//  // ConverterPxsToPxi.java - Parse Pixie script file and generate Pxi.
//  //
//  // Copyright (c) 1997-1998 David R Harris.
//  // You can redistribute this work and/or modify it under the terms of the
//  // GNU Library General Public License version 2, as published by the Free
//  // Software Foundation. No warranty is implied. See lgpl.htm for details.
//
//package gnu.bhresearch.pixie;
//
//import java.io.StreamTokenizer;
//import java.io.BufferedInputStream;
//import java.io.InputStream;
//import java.io.ByteArrayInputStream;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.FileNotFoundException;
//import java.io.File;
//import java.awt.Point;
//import java.awt.Color;
//import java.awt.Font;
//import java.awt.Rectangle;
//import java.util.Vector;
//import java.util.Hashtable;
//import java.lang.Math;
//import gnu.bhresearch.quant.Range;
//import gnu.bhresearch.quant.Debug;
//import gnu.bhresearch.quant.Assert;
//
///**
//	Parse Pixie script file and generate Pxi. The bulk of this class are
//	methods to parse individual contructs. The general approach is
//	recursive descent.
//*/
//public class ConverterPxsToPxi extends ConverterToPxi {
//  private String inName;
//  private StreamTokenizer tokenizer = null;
//  private final static char QUOTE_CHAR = '"';
//
//  public boolean canAppend()
//  {
//    return false;
//  }
//
//  /** True we recognise the input. */
//  public int canConvert( String inName, InputStream in )
//  {
//    if (in != null)
//    {
//      // Look for "PixieVersion" in first few bytes.
//      // Use a separate stream to avoid over-reading if not found.
//      int maxLen = 128;
//      in.mark( maxLen );
//      try
//      {		
//        byte[] buf = new byte[ maxLen ];
//        int len = in.read( buf );
//        setTokenizer( inName, new ByteArrayInputStream( buf, 0, len ) );
//        skip( "PixieVersion" );
//        // If we got here without error it worked.
//        tokenizer = null;
//        return QUALITY_YES;
//      }
//      catch (ConverterException e)
//      {
//        // Failed somehow.
//        return QUALITY_NO;
//      }
//      catch (IOException e)
//      {
//        // File too short?
//        return QUALITY_NO;
//      }
//      finally
//      {
//        try
//        {
//          in.reset();
//        }
//        catch (IOException e)
//        {
//        }
//      }
//    }
//    return matchesExt( inName, ".pxs" ) ? QUALITY_MAYBE : QUALITY_NO;
//  }
//
//  /** Convert the named file. */
//  public void convert( String inName ) throws Exception
//  {
//    setInBox( new Rectangle( 0, 0,
//      Constants.DEFAULT_LOG_WIDTH, Constants.DEFAULT_LOG_HEIGHT ) );
//
//    setTokenizer( inName );
//    parsePixieVersion();
//    parseHeader();
//    parseBody();
//  }
//
//  /** Initialise the tokenizer from the given file. */
//  protected void setTokenizer( String inName ) throws IOException, ConverterException
//  {
//    setTokenizer( inName,
//      new BufferedInputStream(
//      new FileInputStream( inName ) ) );
//  }
//
//  /** Initialise the tokenizer from the given stream. */
//  protected void setTokenizer( String inName, InputStream in )
//  throws IOException, ConverterException
//  {
//    this.inName = inName;
//    tokenizer = new StreamTokenizer( in );
//    // Switch off defaults that we don't like.
//    tokenizer.ordinaryChar( '/' );
//    tokenizer.ordinaryChar( '\'' );
//    tokenizer.ordinaryChar( '"' );
//    // Switch on behaviour we do like.
//    tokenizer.slashSlashComments( true );
//    tokenizer.slashStarComments( true );
//    tokenizer.quoteChar( QUOTE_CHAR );
//    nextToken();
//  }
//
//  /** PixieVersion( 1.0 ) */
//  protected void parsePixieVersion() throws Exception
//  {
//    skip( "PixieVersion" );
//    skip( '(' );
//    double version = parseNumber( 0.0, 100.0 );
//    skip( ')' );
//
//    if (version != 1)
//      parseError("I can only handle a PixieVersion number of 1.0" );
//  }
//
//  /** Header( <header commands> ) */
//  protected void parseHeader() throws Exception
//  {
//    skip( "Header" );
//    skip( '(' );
//    while (tokenizer.ttype != ')')
//    {
//      String command = parseWord( "<header command>" );
//
//      if (command.equals( "Comment" ))
//        parseComment();
//      else if (command.equals( "DefaultPause" ))
//        parsePausePerSlide();
//      else if (command.equals( "Size" ))
//        parseSize();
//      else if (command.equals( "Loop" ))
//        parseLoop();
//      else if (command.equals( "Banner" ))
//        parseBanner();
//      else
//        expected( "<header command>", command );
//    }
//    skip( ')' );
//  }
//
//  /** Comment( "Some text to be ignored" ) */
//  protected void parseComment() throws IOException, ConverterException
//  {
//    skip( '(' );
//    String comment = parseString();
//    skip( ')' );
//
//    out.outputComment( Constants.COMMENT_UNKNOWN, comment );
//  }
//
//  private int pausePerSlide = 1000;
//
//  /** DefaultPause( seconds|Forever ) */
//  protected void parsePausePerSlide() throws IOException, ConverterException
//  {
//    skip( '(' );
//    if (tokenizer.ttype == tokenizer.TT_NUMBER)
//      pausePerSlide = Math.round( (float) (1000 * parseNumber( 0.0, 100000.0 )) );
//    else
//    {
//      skip( "Forever" );
//      pausePerSlide = -1;
//    }
//    skip( ')' );
//  }
//
//  /** Size( width [height] ) */
//  protected void parseSize() throws IOException, ConverterException
//  {
//    // Size must come before any output commands.
//    Assert.assert( out.getRecordCount() == 0 );
//    skip( '(' );
//    int width = parseNumber( 1, Constants.MAX_LOG_WIDTH );
//    int height = width;
//    if (tokenizer.ttype == tokenizer.TT_NUMBER)
//      height = parseNumber( 1, Constants.MAX_LOG_HEIGHT );
//    skip( ')' );
//
//    setDefaultSize( width, height );
//    setInBox( new Rectangle( 0, 0, width, height ) );
//  }
//
//  private boolean isLoop = false;
//
//  /** Loop( True|False ) */
//  protected void parseLoop() throws IOException, ConverterException
//  {
//    skip( '(' );
//    isLoop = parseBoolean();
//    skip( ')' );
//  }
//
//  private boolean haveBannerFrame = false;
//
//  /** Banner( True|False ) */
//  protected void parseBanner() throws Exception
//  {
//    skip( '(' );
//    haveBannerFrame = parseBoolean();
//    skip( ')' );
//  }
//
//  private Rectangle inBox = null;
//  private Transform transform = null;
//
//  /** Define input coordinate system, and hence mapping to
//  output coordinate system. */
//  protected void setInBox( Rectangle inBox )
//  {
//    this.inBox = inBox;
//    out.setLogicalSize( getOutBox() );
//    transform = new Transform( getOutBox(), inBox );
//  }
//
//  /** Body( <body commands> ) */
//  protected void parseBody() throws Exception
//  {
//    skip( "Body" );
//    skip( '(' );
//    while (tokenizer.ttype != ')')
//    {
//      String command = parseWord( "<body command>" );
//
//      if (command.equals( "Comment" ))
//        parseComment();
//      else if (command.equals( "Pause" ))
//        parsePause();
//      else if (command.equals( "Define" ))
//        parseDefineObject();
//      else if (command.equals( "Background" ))
//        parseBackground();
//      else if (command.equals( "Foreground" ))
//        parseForeground();
//      else if (command.equals( "Slide" ))
//        parseSlide();
//      else
//        expected( "<body command>", command );
//    }
//    skip( ')' );
//
//    if (out.getFrameCount() == 0)
//      parseError( "No slide command" );
//
//    // Pause on last Slide unless told otherwise.
//    if (!isLoop)
//      out.setFramePause( out.getFrameCount()-1, -1 );
//
//    // Pause on first frame if and only if it is a banner.
//    if (out.getFrameCount() < 2)
//      haveBannerFrame = false;
//    if (haveBannerFrame)
//      out.setFramePause( 0, 0 );
//    else if (out.getFramePause( 0 ) == 0)
//      out.setFramePause( 0, 1 );
//  }
//
//  /** Pause( seconds|Forever ) */
//  protected void parsePause() throws IOException, ConverterException
//  {
//    if (out.getFrameCount() <= 0)
//      parseError( "No Slide to pause" );
//
//    skip( '(' );
//    float pause;
//    if (tokenizer.ttype == tokenizer.TT_NUMBER)
//      pause = (float) (1000 * parseNumber( 0.0, 100000.0 ));
//    else
//    {
//      skip( "Forever" );
//      pause = -1;
//    }
//    skip( ')' );
//
//    out.setFramePause( out.getFrameCount()-1, Math.round(pause) );
//  }
//
//  private Object backgroundId = null;
//
//  /** A slideBody which is a standard background. Ie a defined object
//  which will be played behind other slides. */
//  protected void parseBackground() throws Exception
//  {
//    out.startDefineObject();
//    parseSlideBody();
//    backgroundId = out.endDefineObject();
//  }
//
//  private Object foregroundId = null;
//
//  /** A slideBody which is a standard foreground. Ie a defined object
//  which will be played on top of other slides. */
//  protected void parseForeground() throws Exception
//  {
//    out.startDefineObject();
//    parseSlideBody();
//    foregroundId = out.endDefineObject();
//  }
//
//  /** A slideBody that defines one slide. We wrap it with the background
//  and foreground objects. */
//  protected void parseSlide() throws Exception
//  {
//    if (out.getFrameCount() == 0)
//      beforeFirstSlide();
//    out.startFrame( pausePerSlide );
//    if (backgroundId != null)
//      out.outputUseObject( backgroundId );
//    parseSlideBody();
//    if (foregroundId != null)
//      out.outputUseObject( foregroundId );
//    out.endFrame();
//  }
//
//  /** Some comments that get output as late as possible, but before the
//  first slide. */
//  protected void beforeFirstSlide() throws IOException
//  {
//    out.outputComment( Constants.COMMENT_SOURCE,
//      inName );
//    out.outputComment( Constants.COMMENT_DATE_CREATED,
//      System.currentTimeMillis() );
//    out.outputComment( Constants.COMMENT_APPLICATION,
//      getClass().getName() );
//  }
//
//  /** A slide body - bracketed list of slide commands. */
//  protected void parseSlideBody() throws Exception
//  {
//    skip( '(' );
//    while (tokenizer.ttype != ')')
//    {
//      String command = parseWord( "<Slide command>" );
//
//      if (isNamedObject( command ))
//        parseUseObject( command );
//      else if (command.equals( "Comment" ))
//        parseComment();
//      else if (command.equals( "Import" ))
//        parseImport();
//      else if (command.equals( "HotSpot" ))
//        parseHotSpot();
//      else if (command.equals( "FillText" ))
//        parseFillText();
//      else if (command.equals( "FillShape" ))
//        parseShape( true );
//      else if (command.equals( "StrokeShape" ))
//        parseShape( false );
//      else if (command.equals( "FillPolygon" ))
//        parsePoly( true, true );
//      else if (command.equals( "StrokePolygon" ))
//        parsePoly( false, true );
//      else if (command.equals( "StrokePolyline" ))
//        parsePoly( false, false );
//      else if (command.equals( "FillRect" ))
//        parseRect( true, false );
//      else if (command.equals( "StrokeRect" ))
//        parseRect( false, false );
//      else if (command.equals( "FillEllipse" ))
//        parseRect( true, true );
//      else if (command.equals( "StrokeEllipse" ))
//        parseRect( false, true );
//      //else if (command.equals( "FillRegular" ))
//      //    parseRegular( true );
//      //else if (command.equals( "StrokeRegular" ))
//      //    parseRegular( false );
//      else if (command.equals( "Button" ))
//        parseButton();
//      else if (command.equals( "VcrButtons" ))
//        parseVcrButtons();
//      else if (command.equals( "BlendRect" ))
//        parseBlend( false );
//      else if (command.equals( "BlendEllipse" ))
//        parseBlend( true );
//      else
//        expected( "<Slide command>", command );
//    }
//
//    skip( ')' );
//  }
//
//  /** Import( "name" [ x y width [ height] ] ). The imported file is
//  	embeded in the output. */
//  protected void parseImport() throws Exception
//  {
//    skip( '(' );
//    String name = parseString();
//    Rectangle box = getOutBox();
//    if (tokenizer.ttype != ')')
//      box = parseRectangle();
//    skip( ')' );
//
//    File fname = new File( name );
//    if (!fname.isAbsolute())
//    {
//      // Try relative to source file.
//      String parent = new File( inName ).getParent();
//      if (parent != null)
//        name = new File( parent, name ).toString();
//    }
//
//    ConverterToPxi converter = ConverterToPxi.newConverterFor( name, true );
//    converter.convert( out, box, name );
//  }
//
//  private String defineContext = "";
//  private Hashtable namedObjects = new Hashtable();
//
//  /** Define( name [w [h]] ( <body> ) ). Record an object for later play
//  back. */
//  protected void parseDefineObject() throws Exception
//  {
//    String oldDefineContext = defineContext;
//    Rectangle oldInBox = inBox;
//
//    skip( '(' );
//    String name = parseWord( "<name>" );
//    int width = oldInBox.width;
//    int height = oldInBox.height;
//    if (tokenizer.ttype == tokenizer.TT_NUMBER)
//    {
//      height = width = parseLogicalX();
//      if (tokenizer.ttype == tokenizer.TT_NUMBER)
//        height = parseLogicalY();
//    }
//
//    setInBox( new Rectangle( 0, 0, width, height ) );
//    defineContext = defineContext + QUOTE_CHAR + name;
//    out.startDefineObject();
//
//    parseSlideBody();
//
//    skip( ')' );
//    Object id = out.endDefineObject();
//
//    namedObjects.put( defineContext, id );
//    defineContext = oldDefineContext;
//    setInBox( oldInBox );
//  }
//
//  /** True if name is defined. */
//  protected boolean isNamedObject( String name ) throws IOException, ConverterException
//  {
//    // We use QUOTE_CHAR because that can't be part of a TT_WORD.
//    String fullName = defineContext + QUOTE_CHAR + name;
//    return namedObjects.get( fullName ) != null;
//  }
//
//  /** name ( [x y [ width [height] ] ). Play back a previously recorded
//  object. */
//  protected void parseUseObject( String name ) throws IOException, ConverterException
//  {
//    skip( '(' );
//    Rectangle box = getOutBox();
//    if (tokenizer.ttype == tokenizer.TT_NUMBER)
//    {
//      box.x = transform.applyToX( parseLogicalX() );
//      box.y = transform.applyToY( parseLogicalY() );
//      if (tokenizer.ttype == tokenizer.TT_NUMBER)
//      {
//        box.width = parseNumber( 1, Constants.MAX_LOG_WIDTH );
//        box.height = box.width;
//        if (tokenizer.ttype == tokenizer.TT_NUMBER)
//          box.height = parseNumber( 1, Constants.MAX_LOG_HEIGHT );
//        box.width = transform.applyToWidth( box.width );
//        box.width = Math.max( box.width, 1 );
//        box.height = transform.applyToHeight( box.height );
//        box.height = Math.max( box.height, 1 );
//      }
//    }
//    skip( ')' );
//
//    String fullName = defineContext + QUOTE_CHAR + name;
//    Object id = namedObjects.get( fullName );
//    out.outputUseObject( id, box );
//  }
//
//  private int hotSpotCount = 0;
//
//  /** HotSpot( type [ "arg" ] x y width [height] ). These are rectangles
//  you can click on. */
//  protected void parseHotSpot() throws IOException, ConverterException
//  {
//    skip( '(' );
//    int cmd = 0;
//    String type = parseWord( "<hotspot type>" );
//
//    if (type.equals( "First" ))
//      cmd = Constants.HOT_SPOT_FIRST_FRAME;
//    else if (type.equals( "Previous" ))
//      cmd = Constants.HOT_SPOT_PREVIOUS_FRAME;
//    else if (type.equals( "Stop" ))
//      cmd = Constants.HOT_SPOT_STOP;
//    else if (type.equals( "Next" ))
//      cmd = Constants.HOT_SPOT_NEXT_FRAME;
//    else if (type.equals( "Last" ))
//      cmd = Constants.HOT_SPOT_LAST_FRAME;
//    else if (type.equals( "NoOp" ))
//      cmd = Constants.HOT_SPOT_NO_OP;
//    else if (type.equals( "Toggle" ))
//      cmd = Constants.HOT_SPOT_TOGGLE_PAUSE;
//    else if (type.equals( "Url" ))
//      cmd = Constants.HOT_SPOT_URL;
//    else
//      expected( "<hotspot type>" );
//    String arg = "";
//    if (tokenizer.ttype == QUOTE_CHAR)
//      arg = parseString();
//    else switch (cmd)
//      {
//      case Constants.HOT_SPOT_FIRST_FRAME: arg = "|<"; break;
//      case Constants.HOT_SPOT_PREVIOUS_FRAME: arg = "<"; break;
//      case Constants.HOT_SPOT_STOP: arg = "o"; break;
//      case Constants.HOT_SPOT_NEXT_FRAME: arg = ">"; break;
//      case Constants.HOT_SPOT_LAST_FRAME: arg = ">|"; break;
//      case Constants.HOT_SPOT_URL: arg = "HRef" + ++hotSpotCount; break;
//      }
//
//      Rectangle box = parseRectangle();
//      skip( ')' );
//
//      out.outputHotSpot( arg, box, cmd );
//  }
//
//  /** Button( "text" x y width [height] ). Draw a button */
//  protected void parseButton() throws IOException, ConverterException
//  {
//    skip( '(' );
//    String text = parseString();
//    Rectangle box = parseRectangle();
//    skip( ')' );
//
//    outputButton( text, box );
//  }
//
//  /** VcrButtons( [height] ). Output the standard slide control buttons. */
//  protected void parseVcrButtons() throws IOException, ConverterException
//  {
//    skip( '(' );
//    Rectangle box = getOutBox();
//    int buttonHeight;
//    if (tokenizer.ttype == tokenizer.TT_NUMBER)
//    {
//      buttonHeight = parseNumber( 1, Constants.MAX_LOG_HEIGHT );
//      buttonHeight = transform.applyToHeight( buttonHeight );
//    }
//    else
//      buttonHeight = Math.min( box.height/10, box.width/10 );
//    skip( ')' );
//
//    int buttonWidth = buttonHeight * 2;
//    box.x = box.x + (box.width - buttonWidth*5)/2;
//    box.y += box.height - buttonHeight;
//    box.width = buttonWidth;
//    box.height = buttonHeight;
//
//    outputButton( "|<", box );
//    out.outputHotSpot( "Start", box, Constants.HOT_SPOT_FIRST_FRAME );
//    box.x += buttonWidth;
//
//    outputButton( "<", box );
//    out.outputHotSpot( "Back", box, Constants.HOT_SPOT_PREVIOUS_FRAME );
//    box.x += buttonWidth;
//
//    outputButton( "o", box );
//    out.outputHotSpot( "Stop", box, Constants.HOT_SPOT_STOP );
//    box.x += buttonWidth;
//
//    outputButton( ">", box );
//    out.outputHotSpot( "Continue", box, Constants.HOT_SPOT_NEXT_FRAME );
//    box.x += buttonWidth;
//
//    outputButton( ">|", box );
//    out.outputHotSpot( "End", box, Constants.HOT_SPOT_LAST_FRAME );
//    box.x += buttonWidth;
//  }
//
//  /** Output a button. */
//  protected void outputButton( String text, Rectangle box ) throws IOException
//  {
//    // Face.
//    out.outputQuickSetColor( Color.lightGray );
//    out.outputQuickRectangle( true, box );
//
//    // top,left
//    out.outputQuickSetColor( Color.white );
//    Curve curve = new Curve();
//    curve.add( box.x, box.y+box.height-2 );
//    curve.add( box.x, box.y );
//    curve.add( box.x+box.width-2, box.y );
//    out.outputQuickCurve( false, curve );
//
//    // bottom right outer.
//    out.outputQuickSetColor( Color.black );
//    curve.setSize(0);
//    curve.add( box.x+box.width-1, box.y );
//    curve.add( box.x+box.width-1, box.y+box.height-2 );
//    curve.add( box.x, box.y+box.height-2 );
//    out.outputQuickCurve( false, curve );
//
//    // Bottom right inner.
//    out.outputQuickSetColor( Color.gray );
//    curve.setSize(0);
//    curve.add( box.x+box.width-2, box.y+1 );
//    curve.add( box.x+box.width-2, box.y+box.height-3 );
//    curve.add( box.x+1, box.y+box.height-3 );
//    out.outputQuickCurve( false, curve );
//
//    if (text != null && text.length() != 0)
//    {
//      out.outputQuickSetColor( Color.black );
//      out.outputQuickSetFont( new Font( "Helvetica", 0, (3*box.height/4)-4 ) );
//      out.outputQuickFillText( text, box.x+box.width/2, box.y+3*box.height/4,
//        Constants.TEXT_CENTER );
//    }
//  }
//
//  /** FillText( <Color> "face" size [Bold] [Italic] [Underline]
//        [Left|Center|Right] x y "text" ) */
//  protected void parseFillText() throws IOException, ConverterException
//  {
//    skip( '(' );
//    Color color = parseColor();
//    String face = parseString();
//    int size = parseNumber( 0, Constants.MAX_LOG_HEIGHT );
//
//    int flags = 0;
//    int style = Font.PLAIN;
//    int alignCount = 0;
//    while (tokenizer.ttype == tokenizer.TT_WORD)
//    {
//      String str = tokenizer.sval;
//      if (str.equals( "Bold" ))
//        style |= Font.BOLD;
//      else if (str.equals( "Italic" ))
//        style |= Font.ITALIC;
//      else if (str.equals( "Underline" ))
//        flags |= Constants.TEXT_UNDERLINE;
//      else if (str.equals( "Left" ))
//      {
//        flags |= Constants.TEXT_LEFT;
//        alignCount++;
//      }
//      else if (str.equals( "Center" ))
//      {
//        flags |= Constants.TEXT_CENTER;
//        alignCount++;
//      }
//      else if (str.equals( "Right" ))
//      {
//        flags |= Constants.TEXT_RIGHT;
//        alignCount++;
//      }
//      else
//        expected( "Left, Center, Right, Bold, Italic or Underline" );
//      nextToken();
//    }
//    if (alignCount > 1)
//      parseError( "Only one of Left, Center, Right may be specified" );
//
//    int x = parseLogicalX();
//    int y = parseLogicalY();
//    String text = parseString();
//    skip( ')' );
//
//
//    x = transform.applyToX( x );
//    y = transform.applyToY( y );
//    if (!out.isKnownFont( face ))
//      System.out.println(
//        inName + " " + tokenizer.lineno() +
//        ": Warning: \"" + face +
//        "\" is not a portable font name." );
//    face = out.mapFont( face );
//    size = transform.applyToHeight( size );
//
//    if (text.length() == 0)
//      return;
//
//    out.outputQuickSetColor( color );
//    out.outputQuickSetFont( new Font( face, style, size ) );
//    out.outputQuickFillText( text, x, y, flags );
//  }
//
//
//  /** FillShape( Color() x0 y0 L(x1 y1) C(x1 y1 x2 y2 x3y3) ... )<BR>
//  	StrokeShape( Color() x0 y0 L(x1 y1) C(x1 y1 x2 y2 x3y3) ... ) */
//  protected void parseShape( boolean isFill ) throws IOException, ConverterException
//  {
//    skip( '(' );
//    Color color = parseColor();
//
//    Curve curve = new Curve();
//    int x = parseLogicalX();
//    int y = parseLogicalY();
//    curve.add( x, y, false );
//
//    while (tokenizer.ttype != ')')
//    {
//      if (tokenizer.ttype != tokenizer.TT_WORD)
//        expected( "line type L or C" );
//      String type = tokenizer.sval;
//      nextToken();
//      if (type.equals( "L" ))
//      {
//        // Line.
//        skip('(');
//        x = parseLogicalX();
//        y = parseLogicalY();
//        skip( ')' );
//        curve.add( x, y, false );
//      }
//      else if (type.equals( "C" ))
//      {
//        // Bezier curve.
//        skip('(');
//        for (int i = 0; i < 3; i++)
//        {
//          x = parseLogicalX();
//          y = parseLogicalY();
//          curve.add( x, y, true );
//        }
//        skip(')');
//      }
//      else
//        expected( "curve type L or C", type );
//    }
//    skip( ')' );
//
//    if (curve.getSize() < 2)
//      return;
//
//    curve = transform.applyTo( curve );
//    out.outputQuickSetColor( color );
//    out.outputQuickCurve( isFill, curve );
//  }
//
//  /** FillPolygon( Color() x1 y1  x2 y2  x3 y3 ... )<BR>
//  	StrokePolygon( Color() x1 y1  x2 y2  x3 y3 ... )<BR>
//  StrokePolyline( Color() x1 y1  x2 y2  x3 y3 ... ) */
//  protected void parsePoly( boolean isFill, boolean isClosed )
//  throws IOException, ConverterException
//  {
//    skip( '(' );
//    Color color = parseColor();
//
//    Curve curve = new Curve();
//    while (tokenizer.ttype != ')')
//    {
//      int x = parseLogicalX();
//      int y = parseLogicalY();
//      curve.add( x, y, false );
//    }
//    skip( ')' );
//
//    // If it needs to be closed, close it.
//    if (isClosed || isFill)
//      curve.addClose();
//    curve = transform.applyTo( curve );
//    curve.removeColinear();
//    if (curve.getSize() < 2)
//      return;
//    out.outputQuickSetColor( color );
//    out.outputQuickCurve( isFill, curve );
//  }
//
//  /** FillRect( Color() left top w [ h] )<BR>
//  StrokeRect( Color() left top w [ h] )<BR>
//  FillEllipse( Color() left top w [ h] )<BR>
//  StrokeEllipse( Color() left top w [ h] ) */
//  protected void parseRect( boolean isFill, boolean isEllipse )
//  throws IOException, ConverterException
//  {
//    skip( '(' );
//    Color color = parseColor();
//    Rectangle box = parseRectangle();
//    skip( ')' );
//
//    out.outputQuickSetColor( color );
//    if (isEllipse)
//      out.outputQuickEllipse( isFill, box );
//    else
//      out.outputQuickRectangle( isFill, box );
//  }
//
//  /** BlendRect( steps Color() left top w [ h ]
//  			[Color() [ left top w [ h ] ]] )<BR>
//  	BlendEllipse( steps Color() left top w [ h ]
//  			[Color() [ left top w [ h ] ]] ) */
//  protected void parseBlend( boolean isEllipse )
//  throws IOException, ConverterException
//  {
//    skip( '(' );
//    int steps = parseNumber( 1, 1000 );
//    Color color0 = parseColor();
//    Rectangle box0 = parseRectangle();
//    Color color1;
//    if (tokenizer.ttype == ')')
//      color1 = Color.white;
//    else
//      color1 = parseColor();
//    Rectangle box1;
//    if (tokenizer.ttype == tokenizer.TT_NUMBER)
//      box1 = parseRectangle();
//    else
//      box1 = new Rectangle( // Blend to middle.
//        box0.x + box0.width/2,
//        box0.y + box0.height/2,
//        0, 0 );
//    skip( ')' );
//
//    outputBlend( isEllipse, steps, color0, box0, color1, box1 );
//  }
//
//  /** Output a blend between 2 shapes and colors. */
//  protected void outputBlend( boolean isEllipse, int steps,
//    Color color0, Rectangle box0,
//    Color color1, Rectangle box1 ) throws IOException
//  {
//    Rectangle curBox = new Rectangle();
//    for (int i = 0; i <= steps; i++)
//    {
//      curBox.x = blendInt( box0.x, box1.x, i, steps );
//      curBox.y = blendInt( box0.y, box1.y, i, steps );
//      curBox.width = blendInt( box0.width, box1.width, i, steps );
//      curBox.height = blendInt( box0.height, box1.height, i, steps );
//      if (curBox.width <= 0 || curBox.height <= 0)
//        continue;
//
//      int r = blendInt( color0.getRed(), color1.getRed(), i, steps );
//      int g = blendInt( color0.getGreen(), color1.getGreen(), i, steps );
//      int b = blendInt( color0.getBlue(), color1.getBlue(), i, steps );
//      Color curColor = new Color( r, g, b );
//
//      out.outputQuickSetColor( curColor );
//      if (isEllipse)
//        out.outputQuickEllipse( true, curBox );
//      else
//        out.outputQuickRectangle( true, curBox );
//    }
//  }
//
//  final static int blendInt( int num0, int num1, int i, int steps )
//  {
//    return num0 + (num1-num0) * i / steps;
//  }
//
//
//  /** ParseRegular( Color() sides angle left top width [height ] )<BR>
//  	StrokeRegular( Color() sides angle left top width [height ] ) */
//  protected void parseRegular( boolean isFill )
//  throws IOException, ConverterException
//  {
//    skip( '(' );
//    Color color = parseColor();
//    int sides = parseNumber( 3, Constants.MAX_LOG_WIDTH );
//    double startAngle = parseNumber( 0, 360 ) * Math.PI / 180.0;
//    Rectangle box = parseRectangle();
//    skip( ')' );
//
//    Curve curve = new Curve( sides+1 );
//    curve.addPolygon( sides, box, startAngle );
//    out.outputQuickSetColor( color );
//    out.outputQuickCurve( isFill, curve );
//  }
//
//  /** name | Color(r g b) */
//  protected Color parseColor() throws IOException, ConverterException
//  {
//    int red, green, blue;
//    String name = parseWord( "<color>" );
//
//    if (name.equals( "Color" ))
//    {
//      skip( '(' );
//      red = parseNumber( 0, 256 );
//      green = parseNumber( 0, 256 );
//      blue = parseNumber( 0, 256 );
//      skip( ')' );
//    }
//    else
//    {
//      Color color = (Color) getColorTable().get( name );
//      if (color == null)
//        parseError("Unrecognised color name " + name );
//      red = color.getRed();
//      green = color.getGreen();
//      blue = color.getBlue();
//    }
//
//    return new Color( red, green, blue );
//  }
//
//
//  /** Return a rectangle: x y width [height] */
//  protected Rectangle parseRectangle() throws IOException, ConverterException
//  {
//    Rectangle box = new Rectangle();
//    box.x = parseLogicalX();
//    box.y = parseLogicalY();
//    box.width = parseNumber( 1, Constants.MAX_LOG_WIDTH );
//    box.height = box.width;
//    if (tokenizer.ttype == tokenizer.TT_NUMBER)
//      box.height = parseNumber( 1, Constants.MAX_LOG_HEIGHT );
//
//    box = transform.applyTo( box );
//    box.width = Math.max( box.width, 1 );
//    box.height = Math.max( box.height, 1 );
//    return box;
//  }
//
//  protected int parseLogicalX() throws IOException, ConverterException
//  {
//    return parseNumber( -Constants.MAX_LOG_WIDTH, Constants.MAX_LOG_WIDTH );
//  }
//
//  protected int parseLogicalY() throws IOException, ConverterException
//  {
//    return parseNumber( -Constants.MAX_LOG_HEIGHT, Constants.MAX_LOG_HEIGHT );
//  }
//
//  /** Return true/false. */
//  protected boolean parseBoolean() throws IOException, ConverterException
//  {
//    String word = parseWord( "True or False" );
//    if (word.equals( "True" ))
//      return true;
//    if (word.equals( "False" ))
//      return false;
//    expected( "True or False" );
//    return false; // Not reached.
//  }
//
//  /** Return a number. */
//  protected int parseNumber( int min, int max ) throws IOException, ConverterException
//  {
//    if (tokenizer.ttype != tokenizer.TT_NUMBER)
//      expected( "<number>" );
//    int result = (int) tokenizer.nval;
//    if (result < min || result >= max)
//      parseError( "Number " + result + " not in range " +
//        min + ".." + max );
//    nextToken();
//    return result;
//  }
//
//  /** Return a number. */
//  protected double parseNumber( double min, double max )
//  throws IOException, ConverterException
//  {
//    if (tokenizer.ttype != tokenizer.TT_NUMBER)
//      expected( "<number>" );
//    double result = tokenizer.nval;
//    if (result < min || result >= max)
//      parseError( "Number " + result + " not in range " +
//        min + ".." + max );
//    nextToken();
//    return result;
//  }
//
//  /** Return a string. */
//  protected String parseString() throws IOException, ConverterException
//  {
//    if (tokenizer.ttype != QUOTE_CHAR)
//      expected( "<string>" );
//    String result = tokenizer.sval;
//    nextToken();
//    return result;
//  }
//
//  /** Return a word. */
//  protected String parseWord( String type ) throws IOException, ConverterException
//  {
//    if (tokenizer.ttype != tokenizer.TT_WORD)
//      expected( type );
//    String result = tokenizer.sval;
//    nextToken();
//    return result;
//  }
//
//  /** Move onto next token, skipping comments. */
//  protected void nextToken() throws IOException, ConverterException
//  {
//    tokenizer.nextToken();
//  }
//
//  /** Skip an expected word. */
//  protected void skip( String word ) throws IOException, ConverterException
//  {
//    if (tokenizer.ttype != tokenizer.TT_WORD ||
//      !tokenizer.sval.equals( word ))
//      expected( "'" + word + "'" );
//    nextToken();
//  }
//
//  /** Skip an expected character, eg punctuation. */
//  protected void skip( char ch ) throws IOException, ConverterException
//  {
//    if (tokenizer.ttype != ch)
//      expected( "'" + String.valueOf(ch) + "'" );
//    nextToken();
//  }
//
//  /** Report an unexpected symbol error. */
//  void expected( String symbol ) throws ConverterException
//  {
//    String got;
//    if (tokenizer.ttype == tokenizer.TT_EOF)
//      got = "<EOF>";
//    else  
//      if (tokenizer.ttype == tokenizer.TT_EOL)
//      got = "<EOL>";
//    else  
//      if (tokenizer.ttype == tokenizer.TT_NUMBER)
//      got = Double.toString(tokenizer.nval);
//    else  
//      if (tokenizer.ttype == tokenizer.TT_WORD)
//      got = tokenizer.sval;
//    else  
//      got = String.valueOf( (char) tokenizer.ttype );
//    expected( symbol, got );
//  }
//
//  /** Report an unexpected symbol error. */
//  protected void expected( String symbol, String got ) throws ConverterException
//  {
//    parseError( "Expected " + symbol + " got \"" + got + "\"" );
//  }
//
//  /** Report a parser error. */
//  protected void parseError( String message ) throws ConverterException
//  {
//    String errorMessage = inName + " " + tokenizer.lineno() + ": ";
//    errorMessage += message;
//    throw new ConverterException( errorMessage );
//  }
//
//  /** Map named colors to values. */
//  private Hashtable colorTable = null;
//  protected Hashtable getColorTable()
//  {
//    if (colorTable == null)
//    {
//      colorTable = new Hashtable();
//      colorTable.put( "Red", Color.red );
//      colorTable.put( "Black", Color.black );
//      colorTable.put( "Green", Color.green );
//      colorTable.put( "Blue", Color.blue );
//      colorTable.put( "Cyan", Color.cyan );
//      colorTable.put( "DarkGray", Color.darkGray );
//      colorTable.put( "Gray", Color.gray );
//      colorTable.put( "LightGray", Color.lightGray );
//      colorTable.put( "Magenta", Color.magenta );
//      colorTable.put( "Orange", Color.orange );
//      colorTable.put( "Pink", Color.pink );
//      colorTable.put( "White", Color.white );
//      colorTable.put( "Yellow", Color.yellow );
//    }
//    return colorTable;
//  }
//}
//
