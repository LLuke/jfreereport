/*
 * Simple editor for Wvi files.
 *
 * Copyright (c) 1997-1998 David R Harris.
 * You can redistribute this work and/or modify it under the terms of the
 * GNU Library General Public License version 2, as published by the Free
 * Software Foundation. No warranty is implied. See lgpl.htm for details.
 */
 
//
//package gnu.bhresearch.pixie;
//
//import java.io.InputStream;
//import java.io.BufferedInputStream;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.awt.Color;
//import java.awt.Font;
//import java.awt.FontMetrics;
//import java.awt.Rectangle;
//import java.awt.Toolkit;
//import java.util.Vector;
//import gnu.bhresearch.quant.Debug;
//import gnu.bhresearch.quant.Assert;
//import gnu.bhresearch.quant.Range;
//
///**
//	Base converter class. It defines a protocol used by the concrete
//	converter subclasses, and provides some static methods for choosing
//	one.
//*/
//public abstract class ConverterToPxi {
//	// Conversion quality levels.
//	/** Can't convert. */
//	public static final int QUALITY_NO = 0;
//	/** Might be able to convert. */
//	public static final int QUALITY_MAYBE = 1;
//	/** Can definitely convert. */
//	public static final int QUALITY_YES = 2;
//
//	/** All registered converters. */
//	private static Vector converters = new Vector();
//
//	/** Register a converter. */
//	public static void registerConverter( ConverterToPxi converter ) {
//		converters.addElement( converter );
//	}
//
//	/** Return a new converter which can handle the given file. */
//	public static ConverterToPxi newConverterFor( String inName, boolean isAppend )
//			throws ConverterException {
//		// Provide the opened file so that converters can use the first
//		// few bytes to decide whether they can manage it.
//		InputStream in = getMarkableStream( inName );
//
//		try {
//			int bestQuality = QUALITY_NO;
//			ConverterToPxi bestConverter = null;
//			// Try each converter in turn. Choose best one.
//			for (int i = 0; i < converters.size(); i++) {
//				ConverterToPxi converter = (ConverterToPxi) converters.elementAt(i);
//				int quality = (isAppend && !converter.canAppend()) ?
//							QUALITY_NO : converter.canConvert( inName, in );
//				if (quality > bestQuality) {
//					bestQuality = quality;
//					bestConverter = converter;
//				}
//			}
//			if (bestConverter != null) {
//				Assert.assert( bestQuality != QUALITY_NO );
//				try {
//					return (ConverterToPxi) bestConverter.getClass().newInstance();
//				}
//				catch (InstantiationException e) {
//					Assert.assert(false); // Can't happen.
//				}
//				catch (IllegalAccessException e) {
//					Assert.assert(false); // Can't happen.
//				}
//			}
//		}
//		finally {
//			if (in != null)
//				try {
//					in.close();
//				}
//				catch (IOException e) {
//				}
//		}
//
//		throw new ConverterException(
//				"Don't know how to convert " + inName );
//    }
//
//	private static InputStream getMarkableStream( String inName ) {
//		try {
//			InputStream in = new BufferedInputStream(
//					new FileInputStream( inName ) );
//			if (in.markSupported()) 
//				return in;
//			in.close();
//			return null;
//		}
//		catch (IOException e) {
//			return null;
//		}
//	}
//
//	/** True if the name has the same file extension. We ignore case. */
//    public static boolean matchesExt( String src, String ext ) {
//        return src.regionMatches( true, src.length()-4,
//                ext, 0, 4 );
//    }
//
//	/** The output file. */
//    protected PxiGenerator out = null;
//	private Rectangle outBox = null;
//	private boolean isAppending = false;
//
//	/** Convert into a whole new file. */
//    public void convert( String outName, Rectangle outBox, String inName )
//    		throws Exception {
//		this.out = new PxiGenerator();
//		this.outBox = outBox;
//		this.isAppending = false;
//		convert( inName );
//        this.out.writeTo( outName );
//    }
//
//	/** Convert and append to an existing file. Used, eg by #include
//		facilities. */
//    public void convert( PxiGenerator out, Rectangle outBox, String inName )
//    		throws Exception {
//		Assert.assert( canAppend() );
//		this.out = out;
//		this.outBox = outBox;
//		this.isAppending = true;
//		convert( inName );
//    }
//
//	/** True if we can convert the given file. */
//	public abstract int canConvert( String inName, InputStream in );
//
//	/** Convert the given file. */
//    public abstract void convert( String inName ) throws Exception;
//
//	/** True if we can append. */
//	public abstract boolean canAppend();
//
//	/** Set the default size of the output drawing area. */
//	protected void setDefaultSize( int width, int height ) {
//		outBox = setDefaultSizeFor( outBox, width, height );
//	}
//
//	/** Return the output drawing area. */
//	public Rectangle getOutBox() {
//		return setDefaultSizeFor( outBox,
//				Constants.DEFAULT_LOG_WIDTH,
//				Constants.DEFAULT_LOG_HEIGHT );
//	}
//
//	/** If no size given, we use default but with the original aspect ratio. */
//	private Rectangle setDefaultSizeFor( Rectangle in, int width, int height ) {
//		Assert.assert( width > 0 && height > 0 );
//		Rectangle out = new Rectangle( in.x, in.y, in.width, in.height );
//
//		if (out.width == 0 && out.height == 0)
//			if (width > height)
//				out.width = Constants.DEFAULT_LOG_WIDTH;
//			else
//				out.height = Constants.DEFAULT_LOG_HEIGHT;
//
//		if (out.width == 0)
//			out.width = (out.height * width) / height;
//		if (out.height == 0)
//			out.height = (out.width * height) / width;
//		Assert.assert( out.width > 0 && out.height > 0 );
//		return out;
//	}
//
//	/** True if we are in append mode. */
//	public boolean isAppending() {
//		return isAppending;
//	} 
//}
