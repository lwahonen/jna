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
package com.sun.jna.platform.unix;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.NativeLong;
import com.sun.jna.Structure.FieldOrder;
import com.sun.jna.ptr.PointerByReference;

/**
 * Bindings for the CUPS (Common UNIX Printing System) API defined in
 * {@code <cups/cups.h>}.
 * <p>
 * CUPS provides a portable printing layer for UNIX-based operating systems. It
 * uses the Internet Printing Protocol (IPP) as the basis for managing print
 * jobs, queues, and printers.
 * <p>
 * Reference: <a href=
 * "https://openprinting.github.io/cups/doc/cupspm.html">CUPS Programming
 * Manual</a>
 *
 * @see <a href="https://openprinting.github.io/cups/">OpenPrinting CUPS</a>
 */
public interface Cups extends Library {

    Cups INSTANCE = Native.load("cups", Cups.class);

    // Printer state constants (IPP printer-state values)

    /** The printer is idle and ready to accept jobs. */
    int IPP_PRINTER_IDLE = 3;
    /** The printer is currently processing a job. */
    int IPP_PRINTER_PROCESSING = 4;
    /** The printer is stopped (paused or has an error). */
    int IPP_PRINTER_STOPPED = 5;

    // Printer type/capability bit constants (cups_ptype_t)

    /** Printer class (group of printers). */
    int CUPS_PRINTER_CLASS = 0x0001;
    /** Remote printer or class. */
    int CUPS_PRINTER_REMOTE = 0x0002;
    /** Can do B&amp;W printing. */
    int CUPS_PRINTER_BW = 0x0004;
    /** Can do color printing. */
    int CUPS_PRINTER_COLOR = 0x0008;
    /** Can do two-sided printing. */
    int CUPS_PRINTER_DUPLEX = 0x0010;
    /** Can staple output. */
    int CUPS_PRINTER_STAPLE = 0x0020;
    /** Can do copies in hardware. */
    int CUPS_PRINTER_COPIES = 0x0040;
    /** Can quickly collate copies. */
    int CUPS_PRINTER_COLLATE = 0x0080;
    /** Can punch output. */
    int CUPS_PRINTER_PUNCH = 0x0100;
    /** Can cover output. */
    int CUPS_PRINTER_COVER = 0x0200;
    /** Can bind output. */
    int CUPS_PRINTER_BIND = 0x0400;
    /** Can sort output. */
    int CUPS_PRINTER_SORT = 0x0800;
    /** Can print on Letter/Legal/A4-size media. */
    int CUPS_PRINTER_SMALL = 0x1000;
    /** Can print on Tabloid/B/C/A3/A2-size media. */
    int CUPS_PRINTER_MEDIUM = 0x2000;
    /** Can print on D/E/A1/A0-size media. */
    int CUPS_PRINTER_LARGE = 0x4000;
    /** Can print on rolls and custom-size media. */
    int CUPS_PRINTER_VARIABLE = 0x8000;
    /** Default printer on network. */
    int CUPS_PRINTER_DEFAULT = 0x20000;
    /** Fax queue. */
    int CUPS_PRINTER_FAX = 0x40000;
    /** Printer is rejecting jobs. */
    int CUPS_PRINTER_REJECTING = 0x80000;
    /** Printer is not shared. @since CUPS 1.2 */
    int CUPS_PRINTER_NOT_SHARED = 0x200000;
    /** Printer requires authentication. @since CUPS 1.2 */
    int CUPS_PRINTER_AUTHENTICATED = 0x400000;
    /** Printer supports maintenance commands. @since CUPS 1.2 */
    int CUPS_PRINTER_COMMANDS = 0x800000;
    /** Printer was discovered (not configured locally). @since CUPS 1.2 */
    int CUPS_PRINTER_DISCOVERED = 0x1000000;

    // Job state constants (ipp_jstate_t)

    /** Job is waiting to be printed. */
    int IPP_JSTATE_PENDING = 3;
    /** Job has been held for printing. */
    int IPP_JSTATE_HELD = 4;
    /** Job is currently printing. */
    int IPP_JSTATE_PROCESSING = 5;
    /** Job has been stopped. */
    int IPP_JSTATE_STOPPED = 6;
    /** Job has been canceled. */
    int IPP_JSTATE_CANCELED = 7;
    /** Job has been aborted due to an error. */
    int IPP_JSTATE_ABORTED = 8;
    /** Job has completed successfully. */
    int IPP_JSTATE_COMPLETED = 9;

    // Job filter constants for cupsGetJobs2

    /** Return all jobs regardless of state. */
    int CUPS_WHICHJOBS_ALL = -1;
    /** Return active (pending, processing, held) jobs. */
    int CUPS_WHICHJOBS_ACTIVE = 0;
    /** Return completed (stopped, canceled, aborted, completed) jobs. */
    int CUPS_WHICHJOBS_COMPLETED = 1;

    /**
     * CUPS option (name/value pair) structure.
     * <p>
     * Corresponds to {@code cups_option_t} in {@code <cups/cups.h>}.
     *
     * @see <a href=
     *      "https://openprinting.github.io/cups/doc/cupspm.html#cups_option_s">cups_option_s</a>
     */
    @FieldOrder({ "name", "value" })
    class CupsOption extends Structure {
        /** Name of option. */
        public String name;
        /** Value of option. */
        public String value;

        public CupsOption() {
            super();
        }

        public CupsOption(Pointer p) {
            super(p);
            read();
        }
    }

    /**
     * CUPS destination (printer or class) structure.
     * <p>
     * Corresponds to {@code cups_dest_t} in {@code <cups/cups.h>}. Each
     * destination represents a printer or printer class known to the CUPS
     * system.
     * <p>
     * Starting with CUPS 1.2, the options array includes attributes such as
     * "printer-info", "printer-is-accepting-jobs", "printer-is-shared",
     * "printer-make-and-model", "printer-state", "printer-state-change-time",
     * "printer-state-reasons", "printer-type", and "printer-uri-supported".
     *
     * @see <a href=
     *      "https://openprinting.github.io/cups/doc/cupspm.html#cups_dest_s">cups_dest_s</a>
     */
    @FieldOrder({ "name", "instance", "is_default", "num_options", "options" })
    class CupsDest extends Structure {
        /** Printer or class name. */
        public String name;
        /** Local instance name or {@code null} for the primary instance. */
        public String instance;
        /** Non-zero if this is the default destination. */
        public int is_default;
        /** Number of options. */
        public int num_options;
        /** Pointer to options array ({@code cups_option_t *}). */
        public Pointer options;

        public CupsDest() {
            super();
        }

        public CupsDest(Pointer p) {
            super(p);
            read();
        }
    }

    /**
     * CUPS job structure.
     * <p>
     * Corresponds to {@code cups_job_t} in {@code <cups/cups.h>}. Represents a
     * print job in the CUPS system.
     *
     * @see <a href=
     *      "https://openprinting.github.io/cups/doc/cupspm.html#cups_job_s">cups_job_s</a>
     */
    @FieldOrder({ "id", "dest", "title", "user", "format", "state", "size", "priority", "completed_time",
            "creation_time", "processing_time" })
    class CupsJob extends Structure {
        /** The job ID. */
        public int id;
        /** Printer or class name. */
        public String dest;
        /** Title of the job. */
        public String title;
        /** User who submitted the job. */
        public String user;
        /** Document format (MIME type). */
        public String format;
        /** Job state ({@code ipp_jstate_t}). */
        public int state;
        /** Size in kilobytes. */
        public int size;
        /** Priority (1-100). */
        public int priority;
        /** Time the job was completed ({@code time_t}, Unix epoch). */
        public NativeLong completed_time;
        /** Time the job was created ({@code time_t}, Unix epoch). */
        public NativeLong creation_time;
        /** Time the job began processing ({@code time_t}, Unix epoch). */
        public NativeLong processing_time;

        public CupsJob() {
            super();
        }

        public CupsJob(Pointer p) {
            super(p);
            read();
        }
    }

    /**
     * Gets all available destinations (printers and classes) from the default
     * server.
     * <p>
     * The returned list includes options containing printer attributes. Use
     * {@link #cupsGetOption} to retrieve specific attribute values from a
     * destination's options.
     * <p>
     * Use {@link #cupsFreeDests} to free the returned destination list.
     *
     * @param dests pointer to receive the destination array
     * @return the number of destinations
     * @see <a href=
     *      "https://openprinting.github.io/cups/doc/cupspm.html#cupsGetDests2">cupsGetDests2</a>
     */
    int cupsGetDests(PointerByReference dests);

    /**
     * Gets all available destinations (printers and classes) from the specified
     * server.
     * <p>
     * Pass {@code null} for the {@code http} parameter to use the default
     * server connection (equivalent to {@code CUPS_HTTP_DEFAULT}).
     * <p>
     * Use {@link #cupsFreeDests} to free the returned destination list.
     *
     * @param http  connection to server or {@code null} for the default
     *              connection
     * @param dests pointer to receive the destination array
     * @return the number of destinations
     * @see <a href=
     *      "https://openprinting.github.io/cups/doc/cupspm.html#cupsGetDests2">cupsGetDests2</a>
     * @since CUPS 1.1.21
     */
    int cupsGetDests2(Pointer http, PointerByReference dests);

    /**
     * Frees the memory used by a destination array returned by
     * {@link #cupsGetDests} or {@link #cupsGetDests2}.
     *
     * @param num_dests number of destinations
     * @param dests     pointer to the destination array
     * @see <a href=
     *      "https://openprinting.github.io/cups/doc/cupspm.html#cupsFreeDests">cupsFreeDests</a>
     */
    void cupsFreeDests(int num_dests, Pointer dests);

    /**
     * Gets the named destination from a destination list.
     * <p>
     * Use {@link #cupsGetDests} or {@link #cupsGetDests2} to get the list, then
     * call this function to find a specific destination by name.
     *
     * @param name      destination name or {@code null} for the default
     *                  destination
     * @param instance  instance name or {@code null}
     * @param num_dests number of destinations in the list
     * @param dests     pointer to the destination array
     * @return pointer to the matching destination or {@code null} if not found
     * @see <a href=
     *      "https://openprinting.github.io/cups/doc/cupspm.html#cupsGetDest">cupsGetDest</a>
     */
    Pointer cupsGetDest(String name, String instance, int num_dests, Pointer dests);

    /**
     * Gets options for the named destination, optimized for retrieving a single
     * destination.
     * <p>
     * This function is preferred over {@link #cupsGetDests2} followed by
     * {@link #cupsGetDest} when you know the destination name or want the
     * default destination.
     * <p>
     * The returned destination must be freed using {@link #cupsFreeDests} with a
     * {@code num_dests} value of 1.
     *
     * @param http     connection to server or {@code null} for the default
     *                 connection
     * @param name     destination name or {@code null} for the default
     *                 destination
     * @param instance instance name or {@code null}
     * @return pointer to the destination or {@code null} if not found
     * @see <a href=
     *      "https://openprinting.github.io/cups/doc/cupspm.html#cupsGetNamedDest">cupsGetNamedDest</a>
     * @since CUPS 1.4
     */
    Pointer cupsGetNamedDest(Pointer http, String name, String instance);

    /**
     * Gets the default printer name.
     *
     * @return the default printer name or {@code null} if no default is set
     * @see <a href=
     *      "https://openprinting.github.io/cups/doc/cupspm.html#cupsGetDest">cupsGetDest</a>
     */
    String cupsGetDefault();

    /**
     * Gets an option value from a destination's options array.
     * <p>
     * Common option names include "printer-info", "printer-state",
     * "printer-make-and-model", "printer-type", "printer-state-reasons",
     * "printer-is-accepting-jobs", and "printer-uri-supported".
     *
     * @param name        option name to look up
     * @param num_options number of options in the array
     * @param options     pointer to the options array
     * @return the option value or {@code null} if not found
     * @see <a href=
     *      "https://openprinting.github.io/cups/doc/cupspm.html#cupsGetOption">cupsGetOption</a>
     */
    String cupsGetOption(String name, int num_options, Pointer options);

    /**
     * Adds an option to an options array.
     * <p>
     * If the named option already exists, its value is replaced.
     *
     * @param name        option name
     * @param value       option value
     * @param num_options current number of options
     * @param options     pointer to the options array (updated on return)
     * @return the new number of options
     * @see <a href=
     *      "https://openprinting.github.io/cups/doc/cupspm.html#cupsAddOption">cupsAddOption</a>
     */
    int cupsAddOption(String name, String value, int num_options, PointerByReference options);

    /**
     * Frees the memory used by an options array.
     *
     * @param num_options number of options
     * @param options     pointer to the options array
     * @see <a href=
     *      "https://openprinting.github.io/cups/doc/cupspm.html#cupsFreeOptions">cupsFreeOptions</a>
     */
    void cupsFreeOptions(int num_options, Pointer options);

    /**
     * Gets the jobs from the specified server.
     * <p>
     * Use {@link #cupsFreeJobs} to free the returned job array.
     *
     * @param http      connection to server or {@code null} for the default
     *                  connection
     * @param jobs      pointer to receive the job array
     * @param name      destination name or {@code null} for all destinations
     * @param myjobs    0 for all users' jobs, 1 for only the current user's
     *                  jobs
     * @param whichjobs one of {@link #CUPS_WHICHJOBS_ALL},
     *                  {@link #CUPS_WHICHJOBS_ACTIVE}, or
     *                  {@link #CUPS_WHICHJOBS_COMPLETED}
     * @return the number of jobs
     * @see <a href=
     *      "https://openprinting.github.io/cups/doc/cupspm.html#cupsGetJobs2">cupsGetJobs2</a>
     * @since CUPS 1.1.21
     */
    int cupsGetJobs2(Pointer http, PointerByReference jobs, String name, int myjobs, int whichjobs);

    /**
     * Frees the memory used by a job array returned by {@link #cupsGetJobs2}.
     *
     * @param num_jobs number of jobs
     * @param jobs     pointer to the job array
     * @see <a href=
     *      "https://openprinting.github.io/cups/doc/cupspm.html#cupsFreeJobs">cupsFreeJobs</a>
     */
    void cupsFreeJobs(int num_jobs, Pointer jobs);

    /**
     * Cancels a job on a destination.
     *
     * @param http   connection to server or {@code null} for the default
     *               connection
     * @param name   destination name
     * @param job_id the job ID to cancel
     * @return 0 on success, non-zero on failure
     * @see <a href=
     *      "https://openprinting.github.io/cups/doc/cupspm.html#cupsCancelDestJob">cupsCancelDestJob</a>
     */
    int cupsCancelJob(String name, int job_id);

    /**
     * Returns the hostname or address of the current CUPS server.
     * <p>
     * The default server comes from the {@code CUPS_SERVER} environment
     * variable, then {@code ~/.cups/client.conf}, and finally
     * {@code /etc/cups/client.conf}. If not set, the default is
     * "localhost" or a domain socket path.
     *
     * @return the server name
     * @see <a href=
     *      "https://openprinting.github.io/cups/doc/cupspm.html#cupsServer">cupsServer</a>
     */
    String cupsServer();

    /**
     * Sets the default CUPS server name and port.
     * <p>
     * The server string can be a fully-qualified hostname, a numeric IPv4 or
     * IPv6 address, or a domain socket pathname. Hostnames and numeric IP
     * addresses can optionally be followed by a colon and port number. Pass
     * {@code null} to restore the default server.
     *
     * @param server server name or {@code null} to restore default
     * @see <a href=
     *      "https://openprinting.github.io/cups/doc/cupspm.html#cupsSetServer">cupsSetServer</a>
     */
    void cupsSetServer(String server);

    /**
     * Returns the current user's name as known to CUPS.
     * <p>
     * Note: The current user name is tracked separately for each thread.
     *
     * @return the user name
     * @see <a href=
     *      "https://openprinting.github.io/cups/doc/cupspm.html#cupsUser">cupsUser</a>
     */
    String cupsUser();

    /**
     * Sets the default user name for CUPS operations.
     * <p>
     * Pass {@code null} to restore the default user name. Note: The user name
     * is tracked per-thread.
     *
     * @param user user name or {@code null} to restore default
     * @see <a href=
     *      "https://openprinting.github.io/cups/doc/cupspm.html#cupsSetUser">cupsSetUser</a>
     */
    void cupsSetUser(String user);

    /**
     * Returns the last IPP status code from a CUPS operation.
     *
     * @return the IPP status code
     */
    int cupsLastError();

    /**
     * Returns a human-readable message for the last IPP error.
     *
     * @return the error message string
     */
    String cupsLastErrorString();
}
