// Range.java -- utilities to do with ranges,
//
// Copyright (c) 1997-1998 David R Harris.
// You can redistribute this work and/or modify it under the terms of the
// GNU Library General Public License version 2, as published by the Free
// Software Foundation. No warranty is implied. See Lgpl.htm for details.

package gnu.bhresearch.quant;

/**
	Utilities to do with ranges. The 'I' suffix means "inclusive"; both lo
	and hi are included in the range. No suffix means that lo is included
	but hi is excluded. This is the usual case for eg integer array indexes,
	but the implied "hi-1" does not make so much sense for floating point
	values.
*/
public class Range {
	/** Ensure lo <= mid && mid < hi */
	public static int clip( int lo, int mid, int hi ) {
		return (lo > mid) ? lo : ((hi <= mid) ? hi-1 : mid);
	}

	/** Ensure lo <= mid && mid <= hi */
	public static int clipI( int lo, int mid, int hi ) {
		return (lo > mid) ? lo : ((hi < mid) ? hi : mid);
	}

	/** Ensure lo <= mid && mid <= hi; NaN if mid is NaN. */
	public static double clipI( double lo, double mid, double hi ) {
		return (lo > mid) ? lo : ((hi < mid) ? hi : mid);
	}

	/** True if lo <= mid && mid < hi. */
	public static boolean in( int lo, int mid, int hi ) {
		return (lo <= mid) && (mid < hi);
	}

	/** True if lo <= mid && mid <= hi. */
	public static boolean inI( int lo, int mid, int hi ) {
		return (lo <= mid) && (mid <= hi);
	}

	/** True if lo <= mid && mid <= hi. */
	public static boolean inI( double lo, double mid, double hi ) {
		return (lo <= mid) && (mid <= hi);
	}
}