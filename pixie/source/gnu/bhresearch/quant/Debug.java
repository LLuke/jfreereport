// Debug.java -- Simplish debugging "println"-like statements.
//
// Copyright (c) 1997-1998 David R Harris.
// You can redistribute this work and/or modify it under the terms of the
// GNU Library General Public License version 2, as published by the Free
// Software Foundation. No warranty is implied. See Lgpl.htm for details.

package gnu.bhresearch.quant;

import java.awt.Frame;
import java.awt.TextArea;
import java.awt.BorderLayout;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Hashtable;

/**
	Simplish debugging "println"-like statements. The output can be switched
	on or off, redirected to console, file or window, and tagged with thread
	and time info.

	<P>All members are static. Use as, eg<PRE>
    	Debug.println("Hello, world");
	</PRE>
*/
public class Debug {
  /** For setType(): sends output to System.out. */
  public static final int CONSOLE = 0;
  /** For setType(): sends output to a file. */
  public static final int FILE = 1;
  /** For setType(): sends output to a window. */
  public static final int WINDOW = 2;
  /** Largest known argument for setType(). */
  public static final int MAX_TYPE = 3;

  static private boolean isVerbose = true;
  static private int level = 0;
  static private int type = CONSOLE;
  static private Frame frame = null;
  static private TextArea textArea = null;
  static private String filename = "debug.out";
  static private boolean isAppend = false;
  static private PrintStream out = null;
  static private Hashtable threadTable = null;
  static private final long startTime = System.currentTimeMillis();
  static private final String eol = System.getProperty( "line.separator" );


  /** True if debugging output is switched on. */
  public static boolean isEnabled()
  {  
    return level > 0;
  }

  /** True if debugging output includes extra info. */
  public static boolean isVerbose()
  {  
    return isVerbose;
  }

  /** Control the verbosity and speed of debugging. The exact meaning
    	is undefined, except that values of 0 or less mean debug output
    	is switched off. */
  public static int getLevel()
  {
    return level;
  }

  /** Output type: CONSOLE, FILE or WINDOW. */
  public static int getType()
  {
    return type;
  }

  /** Name of output file. */
  public static String getFilename()
  {
    return filename;
  }

  /** Return string containing verbose info. This version includes
  thread and time data. */
  public static String getVerbosePrefix()
  {
    int t = getThreadId();
    String prefix = "[" + t + " " + getTime() + "] ";
    // Indent by thread.
    int indent = t % 5;
    for (int i = 0; i < indent; i++)
      prefix += "  ";
    return prefix;
  }

  /** Integer ID of current thread, starting from 0. */
  public static synchronized int getThreadId()
  {
    // Lazy initialisation in case we don't want it.
    if (threadTable == null)
      threadTable = new Hashtable();
    Thread current = Thread.currentThread();
    Integer value = (Integer) threadTable.get( current );
    if (value == null)
    {
      value = new Integer( threadTable.size() );
      threadTable.put( current, value );
    }
    return value.intValue();
  }

  /** Time since this class was initialised. */
  public static synchronized long getTime()
  {
    return System.currentTimeMillis() - startTime;
  }

  /** Set to true to switch debugging output on. */
  public static void setIsEnabled( boolean isEnabled )
  {
    setLevel( isEnabled ? 1 : 0 );
  }

  /** Control the verbosity and speed of debugging. The exact meaning
    	is undefined, except that values of 0 or less mean debug output
    	is switched off. */
  public static void setLevel( int _level )
  {
    level = _level;
  }


  /** Set to true to include extra info in debugging output. */
  public static void setIsVerbose( boolean _isVerbose )
  {
    isVerbose = _isVerbose;
  }

  /** Set type of output: CONSOLE, FILE or WINDOW. Implicitly enables
  output. */
  public static void setType( int _type )
  {
    Assert.assert( Range.in( 0, _type, MAX_TYPE ), "Debug.setType: " + type );
    type = _type;
    setIsEnabled( true );
  }

  /** Set name of output file. Implicitly sets type to FILE. */
  public static void setFilename( String filename )
  {
    setFilename( filename, false );
  }

  /** Set name of output file. Implicitly sets type to FILE. */
  public static void setFilename( String _filename, boolean _isAppend )
  {
    filename = _filename;
    isAppend = _isAppend;
    setType( FILE );
    close();
  }

  /** Output a debug string followed by a newline. */
  public static void println( String message )
  {
    if (level > 0)
      print( message + eol );
  }

  /** Main print routine. */
  public static void print( String message )
  {
    if (level <= 0)
      return;
    if (isVerbose)
      message = getVerbosePrefix() + message;
    init();

    try
    {
      out.print( message );
      if (out.checkError())
        throw( new IOException() );
    }
    catch (IOException e)
    {
      if (out != null && out != System.out)
        out.close();
      setType( CONSOLE );
      System.out.println( "IOException in Debug" );
    }
  }


  /** Print exceptions somewhere we can see them. */
  public synchronized static void dump( Throwable e )
  {
    if (level <= 0)
      return;
    println( "Dumping..." ); // Forces init();
    e.printStackTrace( out );
    println( "" );
  }

  /** Dump a stack trace. */
  public synchronized static void dumpStack()
  {
    if (level > 0)
      Debug.dump( new Throwable( "dumpStack" ) );
  }

  /** Dump filename and line number of caller. */
  public synchronized static void here()
  {
    if (level > 0)
      println( "Here: " + getSourceLine( 1 ) );
  }

  /** Return the filename and line number of caller. This is extracted
  from the output of printStackTrace(), making various assumptions
  about its format.
  param level how far up the stack to walk to find the caller. 0 means
  the immediate caller. */
  public static String getSourceLine( int level )
  {
    if (level > 0)
    {
      ByteArrayOutputStream t = new ByteArrayOutputStream();
      new Throwable( "Here" ).printStackTrace(
        new PrintStream( t ) );
      String trace = t.toString();
      int i = 0;
      level += 2;
      for (int line = 0; line < level && i >= 0; line++)
        i = trace.indexOf( '\n', i+1 );
      if (i > 0)
      {
        int start = trace.indexOf( '(', i );
        int end = trace.indexOf( ')', i );
        if (start > 0 && end > 0)
          return trace.substring( start+1, end );
      }
    }
    return "";
  }

  /** Return the debug stream. Can be used for generating more complex
  reports, eg with more control over verbose info, or in a context
  where a PrintStream is required (eg Throwable.printStackTrace()). */
  public static PrintStream getPrintStream()
  {
    init();
    return out;
  }

  /** Initialise for printing. Mainly selects the output type. */
  private static void init()
  {
    if (level <= 0 || out != null)
      return;

    switch (type)
    {
    default:
    case CONSOLE:
      out = System.out;
      break;
//    case WINDOW:
//      if (frame == null)
//      {
//        frame = new Frame();
//        frame.reshape( 0,0, 200,200 );
//        frame.setTitle( "Debug" );
//        frame.show();
//
//        frame.setLayout( new BorderLayout() );
//        textArea = new TextArea( 10, 50 );
//        textArea.setEditable(false);
//        frame.add( "Center", textArea );
//        frame.layout();
//        out = new PrintStream(
//          new TextAreaOutputStream( textArea ), true );
//      }
//      break;
    case FILE:
      try
      {
        FileOutputStream fileStream = new FileOutputStream(
          filename, isAppend );
        out = new PrintStream( fileStream, true );
      }
      catch (IOException e)
      {
        out = System.out;
        setType( CONSOLE );
        println( "Debug.setFilename( \" + filename + \" ) failed" );
      }
      break;
    }
  }

  /** Force another init(). */
  private static void close()
  {
    if (out == null)
      return;
    switch (type)
    {
    case WINDOW:
      frame.hide();
      frame.dispose();
      frame = null;
      textArea = null;
      break;
    case FILE:
      out.close();
      break;
    }
    out = null;
  }

  /** No instances of this class allowed. */
  private Debug()
  {
  }
}
