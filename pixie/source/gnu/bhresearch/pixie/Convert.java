//  // Convert.java - Convert various Pixie file types.
//  //
//  // Copyright (c) 1997-1998 David R Harris.
//  // You can redistribute this work and/or modify it under the terms of the
//  // GNU Library General Public License version 2, as published by the Free
//  // Software Foundation. No warranty is implied. See lgpl.htm for details.
//
//package gnu.bhresearch.pixie;
//
//import java.awt.Rectangle;
//import java.io.IOException;
//import java.io.FilenameFilter;
//import java.io.File;
//import java.util.Vector;
//import java.util.StringTokenizer;
//import gnu.bhresearch.quant.Debug;
//import gnu.bhresearch.quant.Assert;
//
///**
//	Convert various Pixie file types. Main entry point.
//*/
//public class Convert {
//	public static final String version =
//"pixie.Convert: Convert graphics files to .PXI format.\n" +
//"Version V1.13 (30th Mar 1998)\n" +
//"Copyright (c) 1997-1998 David R Harris.\n" +
//"You can redistribute this work and/or modify it under the terms of the\n" +
//"GNU Library General Public License version 2, as published by the Free\n" +
//"Software Foundation. No warranty is implied. See lgpl.htm for details\n.";
//
//	public static final String usage = 
//"Usage: gnu.bhresearch.pixie.Convert [-hv] [-r 999 [999]] infile outfile.pxi";
//
//	/** Main entry point. */
//    public static void main( String args[] ) {
//		new Convert().main2( args );
//	}
//
//	static {
//		// Register some default converters.
//		ConverterToPxi.registerConverter( new ConverterWmfToPxi() );
//		ConverterToPxi.registerConverter( new ConverterPxsToPxi() );
//	}
//
//    public static final String pxiExt = ".pxi";
//
//	private boolean isVerbose = false;
//	private String inName = null;
//	private String outName = null;
//	private Rectangle outBox = new Rectangle();
//
//	/** Main entry point for the object. */
//	public void main2( String args[] ) {
//		try {
//			Debug.setIsEnabled( false );
//			Assert.setLevel( 0 );
//			parseArgs( args );
//
//			boolean usedDefaultSize = outBox.width <= 0 || outBox.height <= 0;
//			if (inName.equalsIgnoreCase( outName ))
//				throw new Exception( "Input and output must be different" );
//
//			ConverterToPxi converter = ConverterToPxi.newConverterFor( inName, false );
//			converter.convert( outName, outBox, inName );
//
//			if (usedDefaultSize) {
//				outBox = converter.getOutBox();
//				System.out.println( "Size used for " +
//					outName + ": " +
//					outBox.width + "x" + outBox.height + " pixels" );
//			}
//
//			if (isVerbose)
//				System.out.println( "Done" );
//			System.exit( 0 );
//		}
//		catch (Exception e) {
//			String name = e.getClass().getName();
//			if (isVerbose)
//				e.printStackTrace( System.out );
//			else if (name.equals( "java.lang.Exception" ) ||
//					name.equals( "gnu.bhresearch.pixie.ConverterException" ) ) {
//				if (e.getMessage() != null)
//					System.out.println( "Error: " + e.getMessage() );
//			}
//			else {
//				String report = e.toString();
//				if (report.startsWith( name )) {
//					// Strip off package name and "Exception".
//					int start = name.lastIndexOf( '.' );
//					int end = name.lastIndexOf( "Exception" );
//					if (end < 0)
//					    end = name.length();
//					report = name.substring( start+1, end ) +
//					        report.substring( name.length() );
//				}
//				System.out.println( report );
//			}
//			System.exit( 1 );
//		}
//    }
//
//	/** Process commandline arguments. */
//	protected void parseArgs( String[] args ) throws Exception {
//		int argP = 0;
//
//		while (argP < args.length) {
//			String arg = args[argP++];
//			if (arg.charAt(0) != '-') {
//				if (inName == null)
//				    inName = arg;
//				else if (outName == null)
//					outName = arg;
//				else
//					throw new Exception( usage );
//			}
//			else {
//				int charP = 1;
//				while (charP < arg.length()) {
//					char option = arg.charAt( charP++ );
//					switch (option) {
//					default: throw new Exception( usage );
//					case 'h':
//						help();
//						throw new Exception();
//					case 'v':
//						isVerbose = true;
//						System.out.println( version );
//						Assert.setLevel( 2 );
//						break;
//					case 'r':
//						argP = parseResolution( args, argP );
//						break;
//					case 'd':
//						Debug.setIsEnabled( true );
//						Debug.setType( Debug.CONSOLE );
//						if (Assert.getLevel() == 0)
//							Assert.setLevel(1);
//						break;
//					}
//				}
//			}
//		}
//
//		if (inName == null)
//			promptForArgs();
//		if (outName == null && inName != null)
//			outName = setExt( inName, pxiExt );
//	}
//
//	/** Print help message. */
//	protected void help() {
//		System.out.println( version );
//		System.out.println( usage );
//		String text[] = {
//			"h  Help - this message.",
//			"v  Verbose - include a stack trace in error messages.",
//			"r  Resolution - width and height of output image, in pixels.",
//		};
//		for (int i = 0; i < text.length; i++)
//			System.out.println( text[i] );
//	}
//
//	/** Parse resolution argument. */
//	protected int parseResolution( String[] args, int argP ) throws Exception {
//		// Parse optional width and height.
//		if (argP >= args.length)
//			throw new Exception( usage );
//
//		StringTokenizer tokenizer = new StringTokenizer( args[argP++], ",:;\n\r\t" );
//		outBox.width = Integer.parseInt( tokenizer.nextToken() );
//
//		if (tokenizer.hasMoreTokens()) {
//			// Number following a comma.
//			String token = tokenizer.nextToken();
//			outBox.height = Integer.parseInt( token );
//		}
//		else if (argP < args.length) {
//			// See if there's a number in the next argument.
//			try {
//				outBox.height = Integer.parseInt( args[argP++] );
//			}
//			catch (NumberFormatException e) {
//				// Evidently not. Backup.
//				argP--;
//			}
//		}
//		return argP;
//	}
//
//	/** Fetch args from the console. */
//	protected void promptForArgs() throws Exception {
//		inName = readLine( "Input filename? " ).trim();
//		if (inName.length() == 0)
//			throw new Exception();
//
//		String defaultOutName = setExt( inName, pxiExt );
//		outName = readLine( "Output filename [" + defaultOutName + "]? " ).trim();
//		if (outName.length() == 0)
//			outName = defaultOutName;
//		else if (!hasExt( outName ))
//			outName = setExt( outName, pxiExt );
//
//		if (outBox.width == 0) {
//			int width = 0;
//			try {
//				String prompt = 
//						"Width of output in pixels [" +
//						Constants.DEFAULT_LOG_WIDTH +
//						"]? ";
//				String widthName = readLine( prompt ).trim();
//				width = Integer.parseInt( widthName );
//			}
//			catch (NumberFormatException e) {
//			};
//
//			int height = 0;
//			try {
//				String prompt = "Height of output in pixels [preserve aspect ratio]? ";
//				String heightName = readLine( prompt ).trim();
//				height = Integer.parseInt( heightName );
//			}
//			catch (NumberFormatException e) {
//			};
//
//			outBox = new Rectangle( 0, 0, width, height );
//		}
//	}
//
//	/** Prompt and read a line of input from the console. */
//	protected String readLine( String prompt ) throws IOException {
//		System.out.print( prompt );
//		StringBuffer buf = new StringBuffer( 80 );
//		int ch;
//		for (ch = readChar(); ch != '\n' && ch != '\r'; ch = readChar())
//			buf.append( (char) ch );
//		return buf.toString();
//	}
//
//	private int lastChar = 0;
//
//	/** Read a single character from the console. Process carriage return
//		linefeed pairs without apparently hanging. */
//	protected int readChar() throws IOException {
//		int ch = System.in.read();
//		if ((ch == '\n' && lastChar == '\r') ||
//				(ch == '\r' && lastChar == '\n'))
//			ch = System.in.read();
//		lastChar = ch;
//		return ch;
//	}
//
//	/** True if string has a file extension. */
//    public static boolean hasExt( String src ) {
//		return getExtIndex( src ) >= 0;
//	}
//
//	/** Return new string with the given file extension. */
//    public static String setExt( String src, String newExt ) {
//		int dotPos = getExtIndex( src );
//		if (dotPos < 0)
//			return src + newExt;
//		else
//			return src.substring( 0, dotPos ) + newExt;
//    }
//
//	/** Return index of file extension. */
//    private static int getExtIndex( String src ) {
//		int dotPos = src.lastIndexOf( '.' );
//		return
//			(dotPos >= 0 &&
//			src.indexOf( '/', dotPos ) < 0 &&
//			src.indexOf( '\\', dotPos ) < 0 &&
//			src.indexOf( ':', dotPos ) < 0) ? dotPos : -1;
//	}
//}
