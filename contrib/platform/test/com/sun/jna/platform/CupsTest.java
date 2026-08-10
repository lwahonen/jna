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
package com.sun.jna.platform;

import com.sun.jna.Platform;
import com.sun.jna.platform.unix.Cups;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * Exercise the {@link Cups} class.
 */
public class CupsTest extends TestCase {

    private boolean cupsAvailable;

    @Override
    protected void setUp() {
        if (Platform.isWindows()) {
            cupsAvailable = false;
            return;
        }
        try {
            assertNotNull(Cups.INSTANCE);
            cupsAvailable = true;
        } catch (UnsatisfiedLinkError e) {
            // CUPS may not be installed on all systems
            System.out.println("Cups not available: " + e.getMessage());
            cupsAvailable = false;
        }
    }

    public void testCupsServer() {
        if (!cupsAvailable) {
            return;
        }
        String server = Cups.INSTANCE.cupsServer();
        assertNotNull("CUPS server should not be null", server);
        assertTrue("CUPS server should not be empty", server.length() > 0);
        System.out.println("CUPS server: " + server);
    }

    public void testCupsUser() {
        if (!cupsAvailable) {
            return;
        }
        String user = Cups.INSTANCE.cupsUser();
        assertNotNull("CUPS user should not be null", user);
        assertTrue("CUPS user should not be empty", user.length() > 0);
        System.out.println("CUPS user: " + user);
    }

    public void testGetDests() {
        if (!cupsAvailable) {
            return;
        }
        PointerByReference destsRef = new PointerByReference();
        int numDests = Cups.INSTANCE.cupsGetDests(destsRef);
        try {
            assertTrue("Number of destinations should be non-negative", numDests >= 0);
            System.out.println("Number of CUPS destinations: " + numDests);

            if (numDests > 0) {
                Pointer destsPtr = destsRef.getValue();
                assertNotNull("Destinations pointer should not be null", destsPtr);

                // Read the first destination
                Cups.CupsDest[] dests = (Cups.CupsDest[]) new Cups.CupsDest(destsPtr).toArray(numDests);
                Cups.CupsDest dest = dests[0];
                assertNotNull("First destination name should not be null", dest.name);
                System.out.println("First destination: " + dest.name
                        + (dest.is_default != 0 ? " (default)" : ""));

                // Test cupsGetOption on the first destination
                if (dest.num_options > 0) {
                    String printerInfo = Cups.INSTANCE.cupsGetOption(
                            "printer-info", dest.num_options, dest.options);
                    // printer-info may be null if not set, that's OK
                    System.out.println("  printer-info: " + printerInfo);

                    String printerState = Cups.INSTANCE.cupsGetOption(
                            "printer-state", dest.num_options, dest.options);
                    System.out.println("  printer-state: " + printerState);

                    String printerType = Cups.INSTANCE.cupsGetOption(
                            "printer-type", dest.num_options, dest.options);
                    System.out.println("  printer-type: " + printerType + " " + mapPrinterTypeEnumNames(printerType).toString());
                }
            }
        } finally {
            if (numDests > 0) {
                Cups.INSTANCE.cupsFreeDests(numDests, destsRef.getValue());
            }
        }
    }

    public List<String> mapPrinterTypeEnumNames(String printerType) throws RuntimeException, NumberFormatException {
        try {
            List<String> result = new ArrayList<>();

            int printerTypeInt = Integer.parseInt(printerType);

            for (Field f : Cups.class.getFields()) {
                String fieldName = f.getName();
                if (fieldName.startsWith("CUPS_PRINTER_")) {
                    int val = f.getInt(null);
                    if (((printerTypeInt & val) == val)) {
                        result.add(fieldName);
                    }
                }
            }

            return result;
        } catch (IllegalAccessException | IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void testGetDefault() {
        if (!cupsAvailable) {
            return;
        }
        // cupsGetDefault may return null if no default printer is configured
        String defaultPrinter = Cups.INSTANCE.cupsGetDefault();
        System.out.println("Default printer: " + defaultPrinter);
    }

    public void testGetJobs() {
        if (!cupsAvailable) {
            return;
        }
        PointerByReference jobsRef = new PointerByReference();
        int numJobs = Cups.INSTANCE.cupsGetJobs2(null, jobsRef, null, 0,
                Cups.CUPS_WHICHJOBS_ALL);
        try {
            // cupsGetJobs2 may return -1 if the CUPS scheduler is not running
            if (numJobs < 0) {
                System.out.println("CUPS scheduler not running, skipping job test");
                return;
            }
            System.out.println("Number of CUPS jobs: " + numJobs);

            if (numJobs > 0) {
                Pointer jobsPtr = jobsRef.getValue();
                assertNotNull("Jobs pointer should not be null", jobsPtr);

                Cups.CupsJob[] jobs = (Cups.CupsJob[]) new Cups.CupsJob(jobsPtr).toArray(numJobs);
                Cups.CupsJob job = jobs[0];
                assertTrue("Job ID should be positive", job.id > 0);
                assertNotNull("Job destination should not be null", job.dest);
                System.out.println("First job: id=" + job.id + " dest=" + job.dest
                        + " title=" + job.title + " state=" + job.state);
            }
        } finally {
            if (numJobs > 0) {
                Cups.INSTANCE.cupsFreeJobs(numJobs, jobsRef.getValue());
            }
        }
    }

    public void testSetAndRestoreServer() {
        if (!cupsAvailable) {
            return;
        }
        String originalServer = Cups.INSTANCE.cupsServer();
        try {
            Cups.INSTANCE.cupsSetServer("localhost");
            String newServer = Cups.INSTANCE.cupsServer();
            assertEquals("Server should be updated", "localhost", newServer);
        } finally {
            // Restore original
            Cups.INSTANCE.cupsSetServer(null);
        }
    }

    public void testGetNamedDest() {
        if (!cupsAvailable) {
            return;
        }
        // Get the default destination (may be null if none configured)
        Pointer destPtr = Cups.INSTANCE.cupsGetNamedDest(null, null, null);
        if (destPtr != null) {
            try {
                Cups.CupsDest dest = new Cups.CupsDest(destPtr);
                assertNotNull("Named dest name should not be null", dest.name);
                System.out.println("Default named dest: " + dest.name);
            } finally {
                Cups.INSTANCE.cupsFreeDests(1, destPtr);
            }
        } else {
            System.out.println("No default destination configured");
        }
    }

    public void testLastError() {
        if (!cupsAvailable) {
            return;
        }
        // Just verify these don't crash; the error state depends on prior calls
        int errorCode = Cups.INSTANCE.cupsLastError();
        assertTrue("Error code should be non-negative", errorCode >= 0);
        String errorString = Cups.INSTANCE.cupsLastErrorString();
        assertNotNull("Error string should not be null", errorString);
    }
}
