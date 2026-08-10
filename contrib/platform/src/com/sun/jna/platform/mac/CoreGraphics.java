/* Copyright (c) 2026 Daniel Widdis, All Rights Reserved
 *
 * The contents of this file is dual-licensed under 2
 * alternative Open Source/Free licenses: LGPL 2.1 or later and
 * Apache License 2.0. (starting with JNA version 4.0.0).
 *
 * You can freely decide which license you want to apply to
 * the project.
 *
 * You may obtain a copy of the LGPL License at:
 *
 * http://www.gnu.org/licenses/licenses.html
 *
 * A copy is also included in the downloadable source code package
 * containing JNA, in file "LGPL2.1".
 *
 * You may obtain a copy of the Apache License at:
 *
 * http://www.apache.org/licenses/
 *
 * A copy is also included in the downloadable source code package
 * containing JNA, in file "AL2.0".
 */
package com.sun.jna.platform.mac;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;
import com.sun.jna.platform.mac.CoreFoundation.CFArrayRef;
import com.sun.jna.platform.mac.CoreFoundation.CFDictionaryRef;
import com.sun.jna.platform.mac.CoreFoundation.CFStringRef;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.unix.LibCAPI.size_t;

/**
 * Bindings for the macOS CoreGraphics framework, specifically the Quartz Window
 * Services and Quartz Display Services APIs.
 * <p>
 * CoreGraphics provides low-level 2D rendering and, on macOS, services for
 * working with display hardware, low-level user input events, and the windowing
 * system.
 *
 * @see <a href=
 *      "https://developer.apple.com/documentation/coregraphics/quartz_window_services">Quartz
 *      Window Services</a>
 * @see <a href=
 *      "https://developer.apple.com/documentation/coregraphics/quartz_display_services">Quartz
 *      Display Services</a>
 */
public interface CoreGraphics extends Library {

    CoreGraphics INSTANCE = Native.load("CoreGraphics", CoreGraphics.class);

    // CGWindowListOption constants

    /** List all windows, including off-screen windows. */
    int kCGWindowListOptionAll = 0;
    /** List only on-screen windows. */
    int kCGWindowListOptionOnScreenOnly = 1 << 0;
    /** List on-screen windows above the specified window. */
    int kCGWindowListOptionOnScreenAboveWindow = 1 << 1;
    /** List on-screen windows below the specified window. */
    int kCGWindowListOptionOnScreenBelowWindow = 1 << 2;
    /** Include the specified window. */
    int kCGWindowListOptionIncludingWindow = 1 << 3;
    /** Exclude desktop elements (wallpaper, icons). */
    int kCGWindowListExcludeDesktopElements = 1 << 4;

    /** A null window ID, used as the relativeToWindow parameter. */
    int kCGNullWindowID = 0;

    // CGWindowImageOption constants

    /** Default window image options. */
    int kCGWindowImageDefault = 0;
    /** Include window frame decorations in the image. */
    int kCGWindowImageBoundsIgnoreFraming = 1 << 0;
    /** Only include the specified window (not windows below it). */
    int kCGWindowImageShouldBeOpaque = 1 << 1;
    /** Only capture the shadow of the window. */
    int kCGWindowImageOnlyShadows = 1 << 2;
    /** Use best resolution regardless of display. @since macOS 10.9 */
    int kCGWindowImageBestResolution = 1 << 3;
    /** Use nominal resolution. @since macOS 10.9 */
    int kCGWindowImageNominalResolution = 1 << 4;

    // Window info dictionary keys (CFString constants)

    /**
     * Key for the window ID ({@code CGWindowID}, a {@code CFNumber}).
     */
    String kCGWindowNumber = "kCGWindowNumber";
    /**
     * Key for the window's Core Graphics backing store type
     * ({@code CFNumber}).
     */
    String kCGWindowStoreType = "kCGWindowStoreType";
    /**
     * Key for the window layer ({@code CFNumber}). Windows with layer 0 are
     * normal windows.
     */
    String kCGWindowLayer = "kCGWindowLayer";
    /**
     * Key for the window bounds ({@code CFDictionary} with X, Y, Width,
     * Height). Use {@link #CGRectMakeWithDictionaryRepresentation} to parse.
     */
    String kCGWindowBounds = "kCGWindowBounds";
    /**
     * Key for the sharing state ({@code CFNumber}).
     */
    String kCGWindowSharingState = "kCGWindowSharingState";
    /**
     * Key for the window alpha/opacity ({@code CFNumber}, 0.0-1.0).
     */
    String kCGWindowAlpha = "kCGWindowAlpha";
    /**
     * Key for the owning process ID ({@code CFNumber}, a {@code pid_t}).
     */
    String kCGWindowOwnerPID = "kCGWindowOwnerPID";
    /**
     * Key for the memory usage of the window in bytes ({@code CFNumber}).
     */
    String kCGWindowMemoryUsage = "kCGWindowMemoryUsage";
    /**
     * Key for the window name/title ({@code CFString}). May be absent if the
     * window has no title or the caller lacks permissions.
     */
    String kCGWindowName = "kCGWindowName";
    /**
     * Key for the name of the process that owns the window
     * ({@code CFString}).
     */
    String kCGWindowOwnerName = "kCGWindowOwnerName";
    /**
     * Key for whether the window is on screen ({@code CFBoolean}).
     */
    String kCGWindowIsOnscreen = "kCGWindowIsOnscreen";
    /**
     * Key for the backing location type ({@code CFNumber}).
     */
    String kCGWindowBackingLocationVideoMemory = "kCGWindowBackingLocationVideoMemory";

    // CGWindowSharingType constants

    /** Window contents cannot be read. */
    int kCGWindowSharingNone = 0;
    /** Window contents can be read only by the owning process. */
    int kCGWindowSharingReadOnly = 1;
    /** Window contents can be read by any process. */
    int kCGWindowSharingReadWrite = 2;

    // CGWindowBackingType constants

    /** Retained backing store (deprecated). */
    int kCGBackingStoreRetained = 0;
    /** Non-retained backing store (deprecated). */
    int kCGBackingStoreNonretained = 1;
    /** Buffered backing store. */
    int kCGBackingStoreBuffered = 2;

    /**
     * A point in the Core Graphics coordinate system.
     *
     * @see <a href=
     *      "https://developer.apple.com/documentation/corefoundation/cgpoint">CGPoint</a>
     */
    @FieldOrder({ "x", "y" })
    class CGPoint extends Structure {
        /** The x-coordinate of the point. */
        public double x;
        /** The y-coordinate of the point. */
        public double y;
    }

    /**
     * A size in the Core Graphics coordinate system.
     *
     * @see <a href=
     *      "https://developer.apple.com/documentation/corefoundation/cgsize">CGSize</a>
     */
    @FieldOrder({ "width", "height" })
    class CGSize extends Structure {
        /** The width value. */
        public double width;
        /** The height value. */
        public double height;
    }

    /**
     * A rectangle in the Core Graphics coordinate system.
     *
     * @see <a href=
     *      "https://developer.apple.com/documentation/corefoundation/cgrect">CGRect</a>
     */
    @FieldOrder({ "origin", "size" })
    class CGRect extends Structure {
        /** The origin (top-left corner) of the rectangle. */
        public CGPoint origin;
        /** The size (width and height) of the rectangle. */
        public CGSize size;

        public static class ByValue extends CGRect implements Structure.ByValue {
        }
    }

    // Quartz Window Services functions

    /**
     * Returns information about the windows in the current user session.
     * <p>
     * Each element in the returned array is a {@code CFDictionary} containing
     * window properties keyed by the {@code kCGWindow*} constants.
     *
     * @param option            a combination of {@code kCGWindowListOption*}
     *                          constants specifying which windows to include
     * @param relativeToWindow  the window ID to use as a reference point for
     *                          above/below options, or
     *                          {@link #kCGNullWindowID} for all windows
     * @return a {@code CFArray} of {@code CFDictionary} objects describing each
     *         window, or {@code null} on failure. The caller is responsible for
     *         releasing the array.
     * @see <a href=
     *      "https://developer.apple.com/documentation/coregraphics/1455137-cgwindowlistcopywindowinfo">CGWindowListCopyWindowInfo</a>
     */
    CFArrayRef CGWindowListCopyWindowInfo(int option, int relativeToWindow);

    /**
     * Creates a {@link CGRect} from a dictionary representation (as returned in
     * the {@link #kCGWindowBounds} key of window info dictionaries).
     *
     * @param dict the dictionary containing X, Y, Width, and Height keys
     * @param rect a {@link CGRect} structure to populate
     * @return non-zero if the conversion was successful
     * @see <a href=
     *      "https://developer.apple.com/documentation/coregraphics/1456348-cgrectmakewithdictionaryrepresen">CGRectMakeWithDictionaryRepresentation</a>
     *
     */
    byte CGRectMakeWithDictionaryRepresentation(CFDictionaryRef dict, CGRect rect);

    // Quartz Display Services functions

    /**
     * Returns the display ID of the main display.
     * <p>
     * The main display is the display with its screen location at (0,0) in the
     * global display coordinate space. In a system without display mirroring,
     * the display with the menu bar is typically the main display.
     *
     * @return the main display ID
     * @see <a href=
     *      "https://developer.apple.com/documentation/coregraphics/1456599-cgmaindisplayid">CGMainDisplayID</a>
     */
    int CGMainDisplayID();

    /**
     * Returns the bounds of a display in the global display coordinate space.
     *
     * @param display the display ID
     * @return a {@link CGRect} describing the display bounds
     * @see <a href=
     *      "https://developer.apple.com/documentation/coregraphics/1456395-cgdisplaybounds">CGDisplayBounds</a>
     */
    CGRect.ByValue CGDisplayBounds(int display);

    /**
     * Returns the width in pixels of a display.
     *
     * @param display the display ID
     * @return the width in pixels
     * @see <a href=
     *      "https://developer.apple.com/documentation/coregraphics/1456348-cgrectmakewithdictionaryrepresen">CGRectMakeWithDictionaryRepresentation</a>
     *
     */
    size_t CGDisplayPixelsWide(int display);

    /**
     * Returns the height in pixels of a display.
     *
     * @param display the display ID
     * @return the height in pixels
     * @see <a href=
     *      "https://developer.apple.com/documentation/coregraphics/1456
     *      395-cgdisplaypixelshigh">CGDisplayPixelsHigh</a>
     */
    size_t CGDisplayPixelsHigh(int display);

    /**
     * Provides a list of displays that are active (or drawable).
     *
     * @param maxDisplays maximum number of displays to return
     * @param activeDisplays an array to fill with display IDs
     * @param displayCount receives the actual number of displays
     * @return 0 ({@code kCGErrorSuccess}) on success
     * @see <a href=
     *      "https://developer.apple.com/documentation/coregraphics/1454603-cggetactivedisplaylist">CGGetActiveDisplayList</a>
     */
    int CGGetActiveDisplayList(int maxDisplays, int[] activeDisplays, IntByReference displayCount);

    /**
     * Provides a list of online displays (active or mirrored).
     *
     * @param maxDisplays maximum number of displays to return
     * @param onlineDisplays an array to fill with display IDs
     * @param displayCount receives the actual number of displays
     * @return 0 ({@code kCGErrorSuccess}) on success
     * @see <a href=
     *      "https://developer.apple.com/documentation/coregraphics/1454964-cggetonlinedisplaylist">CGGetOnlineDisplayList</a>
     */
    int CGGetOnlineDisplayList(int maxDisplays, int[] onlineDisplays, IntByReference displayCount);

    /**
     * Returns the rotation angle of a display in degrees.
     *
     * @param display the display ID
     * @return the rotation angle (0, 90, 180, or 270)
     * @see <a href=
     *      "https://developer.apple.com/documentation/coregraphics/1454084-cgdisplayrotation">CGDisplayRotation</a>
     */
    double CGDisplayRotation(int display);

    /**
     * Returns whether a display is active.
     *
     * @param display the display ID
     * @return non-zero if the display is active
     */
    int CGDisplayIsActive(int display);

    /**
     * Returns whether a display is the main display.
     *
     * @param display the display ID
     * @return non-zero if the display is the main display
     */
    int CGDisplayIsMain(int display);

    /**
     * Returns whether a display is built-in (e.g., a laptop screen).
     *
     * @param display the display ID
     * @return non-zero if the display is built-in
     */
    int CGDisplayIsBuiltin(int display);

    /**
     * Returns the vendor number for a display.
     *
     * @param display the display ID
     * @return the vendor number
     */
    int CGDisplayVendorNumber(int display);

    /**
     * Returns the model number for a display.
     *
     * @param display the display ID
     * @return the model number
     */
    int CGDisplayModelNumber(int display);

    /**
     * Returns the serial number for a display.
     *
     * @param display the display ID
     * @return the serial number
     */
    int CGDisplaySerialNumber(int display);
}
