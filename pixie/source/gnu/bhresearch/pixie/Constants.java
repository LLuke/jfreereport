// Constants.java - Constants for wvi files.
//
// Copyright (c) 1997-1998 David R Harris.
// You can redistribute this work and/or modify it under the terms of the
// GNU Library General Public License version 2, as published by the Free
// Software Foundation. No warranty is implied. See lgpl.htm for details.

package gnu.bhresearch.pixie;

/**
	Constants for wvi files.
*/
public interface Constants {
    /** Magic number at start of file. */
    int MAGIC = 0x005daa749;

    /** Limit on range of logical X coords. */
    int MAX_LOG_WIDTH = 32*1024;
    /** Limit on range of logical Y coords. */
    int MAX_LOG_HEIGHT = 32*1024;
    /** Default resolution in X direction. */
	  int DEFAULT_LOG_WIDTH = 1000;
    /** Default resolution in Y direction. */
	  int DEFAULT_LOG_HEIGHT = 1000;

    // File record types. Common commands have IDs less than 8
	  // so that VInt can store them in a single nibble.

    int CMD_UNDEFINED = 0;	// To catch common errors.
    int CMD_MIN = 1;
    int CMD_SET_COLOR = 1;
    int CMD_FILL_RECTANGLE = 2;
    int CMD_FILL_ELLIPSE = 3;
    int CMD_FILL_POLYGON = 4;
    int CMD_STROKE_POLYLINE = 5;
    int CMD_USE_OBJECT = 6;
    int CMD_COMMENT = 7;
    int CMD_END = 8;
    int CMD_SET_FONT = 9;
    int CMD_FILL_TEXT = 10;
    int CMD_HOT_SPOT = 11;
    int CMD_STROKE_ELLIPSE = 12;
    int CMD_STROKE_RECTANGLE = 13;
    int CMD_STROKE_CURVE = 14;
    int CMD_FILL_CURVE = 15;
    int CMD_MAX = 16;

	// Control commands.
	int CTL_UNDEFINED = 0;
	int CTL_MIN = 1;
	int CTL_END = 1;
	int CTL_PAUSE = 2;
	int CTL_MAX = 3;

	/** Text formatting styles. */
    int TEXT_LEFT = 0x00;
    int TEXT_CENTER = 0x01;
    int TEXT_RIGHT = 0x02;
    int TEXT_JUSTIFY_MASK = 0x03;
    int TEXT_UNDERLINE = 0x04;
    int TEXT_STYLE_MASK = 0x07;

	/** Hotspot commands. Positive hotspot commands are frame numbers. */
    int HOT_SPOT_FIRST_FRAME = 0;
    int HOT_SPOT_URL = -1;
    int HOT_SPOT_STOP = -2;
    int HOT_SPOT_NEXT_FRAME = -3;
    int HOT_SPOT_PREVIOUS_FRAME = -4;
    int HOT_SPOT_LAST_FRAME = -5;
    int HOT_SPOT_TOGGLE_PAUSE = -6;
    int HOT_SPOT_NO_OP = -7;
    int HOT_SPOT_MIN = -7;

	/** Types of comments. */
    int COMMENT_MIN = 0;
    int COMMENT_UNKNOWN = 0;
    int COMMENT_SOURCE = 1;
    int COMMENT_DATE_CREATED = 2;
    int COMMENT_APPLICATION = 3;
    int COMMENT_MAX = 4;
}
