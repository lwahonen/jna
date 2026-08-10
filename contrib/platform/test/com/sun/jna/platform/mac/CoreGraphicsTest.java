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

import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.platform.mac.CoreFoundation.CFArrayRef;
import com.sun.jna.platform.mac.CoreFoundation.CFDictionaryRef;
import com.sun.jna.platform.mac.CoreFoundation.CFStringRef;
import com.sun.jna.ptr.IntByReference;

import junit.framework.TestCase;

/**
 * Exercise the {@link CoreGraphics} class.
 */
public class CoreGraphicsTest extends TestCase {

    public void testWindowList() {
        if (!Platform.isMac()) {
            return;
        }
        CFArrayRef windowList = CoreGraphics.INSTANCE.CGWindowListCopyWindowInfo(
                CoreGraphics.kCGWindowListOptionAll, CoreGraphics.kCGNullWindowID);
        try {
            assertNotNull("Window list should not be null", windowList);
            int count = windowList.getCount();
            assertTrue("Should have at least one window", count > 0);
            System.out.println("Total windows: " + count);

            // Read first window's info
            Pointer dictPtr = windowList.getValueAtIndex(0);
            assertNotNull("First window dict should not be null", dictPtr);
            CFDictionaryRef dict = new CFDictionaryRef(dictPtr);

            // Get window owner name
            CFStringRef key = CFStringRef.createCFString(CoreGraphics.kCGWindowOwnerName);
            try {
                Pointer val = CoreFoundation.INSTANCE.CFDictionaryGetValue(dict, key);
                if (val != null) {
                    CFStringRef nameRef = new CFStringRef(val);
                    System.out.println("First window owner: " + nameRef.stringValue());
                }
            } finally {
                key.release();
            }

            // Get window PID
            key = CFStringRef.createCFString(CoreGraphics.kCGWindowOwnerPID);
            try {
                Pointer val = CoreFoundation.INSTANCE.CFDictionaryGetValue(dict, key);
                if (val != null) {
                    CoreFoundation.CFNumberRef pidNum = new CoreFoundation.CFNumberRef(val);
                    System.out.println("First window PID: " + pidNum.intValue());
                }
            } finally {
                key.release();
            }
        } finally {
            windowList.release();
        }
    }

    public void testOnScreenWindows() {
        if (!Platform.isMac()) {
            return;
        }
        CFArrayRef windowList = CoreGraphics.INSTANCE.CGWindowListCopyWindowInfo(
                CoreGraphics.kCGWindowListOptionOnScreenOnly | CoreGraphics.kCGWindowListExcludeDesktopElements,
                CoreGraphics.kCGNullWindowID);
        try {
            assertNotNull("On-screen window list should not be null", windowList);
            int count = windowList.getCount();
            assertTrue("Should have at least one on-screen window", count > 0);
            System.out.println("On-screen windows (excluding desktop): " + count);
        } finally {
            windowList.release();
        }
    }

    public void testCGRectMakeWithDictionaryRepresentation() {
        if (!Platform.isMac()) {
            return;
        }
        // Get a window and parse its bounds
        CFArrayRef windowList = CoreGraphics.INSTANCE.CGWindowListCopyWindowInfo(
                CoreGraphics.kCGWindowListOptionOnScreenOnly, CoreGraphics.kCGNullWindowID);
        try {
            int count = windowList.getCount();
            if (count == 0) {
                return;
            }
            Pointer dictPtr = windowList.getValueAtIndex(0);
            CFDictionaryRef dict = new CFDictionaryRef(dictPtr);

            CFStringRef boundsKey = CFStringRef.createCFString(CoreGraphics.kCGWindowBounds);
            try {
                Pointer boundsPtr = CoreFoundation.INSTANCE.CFDictionaryGetValue(dict, boundsKey);
                if (boundsPtr != null) {
                    CFDictionaryRef boundsDict = new CFDictionaryRef(boundsPtr);
                    CoreGraphics.CGRect rect = new CoreGraphics.CGRect();
                    byte success = CoreGraphics.INSTANCE.CGRectMakeWithDictionaryRepresentation(
                            boundsDict, rect);
                    assertTrue("CGRectMakeWithDictionaryRepresentation should succeed", success != 0);
                    System.out.println("First window bounds: x=" + rect.origin.x
                            + " y=" + rect.origin.y
                            + " w=" + rect.size.width
                            + " h=" + rect.size.height);
                }
            } finally {
                boundsKey.release();
            }
        } finally {
            windowList.release();
        }
    }

    public void testMainDisplayID() {
        if (!Platform.isMac()) {
            return;
        }
        int mainDisplay = CoreGraphics.INSTANCE.CGMainDisplayID();
        assertTrue("Main display ID should be positive", mainDisplay > 0);
        System.out.println("Main display ID: " + mainDisplay);
    }

    public void testGetActiveDisplayList() {
        if (!Platform.isMac()) {
            return;
        }
        IntByReference countRef = new IntByReference();
        int err = CoreGraphics.INSTANCE.CGGetActiveDisplayList(0, null, countRef);
        assertEquals("CGGetActiveDisplayList should succeed", 0, err);
        int count = countRef.getValue();
        assertTrue("Should have at least one active display", count > 0);

        int[] displays = new int[count];
        err = CoreGraphics.INSTANCE.CGGetActiveDisplayList(count, displays, countRef);
        assertEquals("CGGetActiveDisplayList should succeed", 0, err);
        System.out.println("Active displays: " + count);

        for (int i = 0; i < count; i++) {
            long width = CoreGraphics.INSTANCE.CGDisplayPixelsWide(displays[i]).longValue();
            long height = CoreGraphics.INSTANCE.CGDisplayPixelsHigh(displays[i]).longValue();
            assertTrue("Display width should be positive", width > 0);
            assertTrue("Display height should be positive", height > 0);
            System.out.println("  Display " + displays[i] + ": " + width + "x" + height);
        }
    }

    public void testDisplayProperties() {
        if (!Platform.isMac()) {
            return;
        }
        int mainDisplay = CoreGraphics.INSTANCE.CGMainDisplayID();

        int isActive = CoreGraphics.INSTANCE.CGDisplayIsActive(mainDisplay);
        assertTrue("Main display should be active", isActive != 0);

        int isMain = CoreGraphics.INSTANCE.CGDisplayIsMain(mainDisplay);
        assertTrue("Main display should report as main", isMain != 0);

        double rotation = CoreGraphics.INSTANCE.CGDisplayRotation(mainDisplay);
        assertTrue("Rotation should be 0, 90, 180, or 270",
                rotation == 0 || rotation == 90 || rotation == 180 || rotation == 270);
        System.out.println("Main display rotation: " + rotation);

        CoreGraphics.CGRect.ByValue bounds = CoreGraphics.INSTANCE.CGDisplayBounds(mainDisplay);
        assertTrue("Display width should be positive", bounds.size.width > 0);
        assertTrue("Display height should be positive", bounds.size.height > 0);
        System.out.println("Main display bounds: " + bounds.size.width + "x" + bounds.size.height);
    }
}
