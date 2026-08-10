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
package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;
import com.sun.jna.Union;
import com.sun.jna.platform.win32.WinBase.SYSTEMTIME;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinNT.HANDLEByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

/**
 * Provides mappings for the Windows Bluetooth API functions from {@code BluetoothApis.dll}.
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/api/bluetoothapis/">BluetoothApis.h</a>
 */
public interface BluetoothApis extends StdCallLibrary {

    /** Instance of BluetoothApis. */
    BluetoothApis INSTANCE = Native.load("BluetoothApis", BluetoothApis.class, W32APIOptions.UNICODE_OPTIONS);

    /** Maximum Bluetooth device name length. */
    int BLUETOOTH_MAX_NAME_SIZE = 248;

    /**
     * The {@code BLUETOOTH_ADDRESS} structure provides the address of a Bluetooth device. The address is stored as
     * either a {@code BTH_ADDR} (unsigned 64-bit integer) or a 6-byte array.
     *
     * @see <a href=
     *      "https://learn.microsoft.com/en-us/windows/win32/api/bluetoothapis/ns-bluetoothapis-bluetooth_address_struct">BLUETOOTH_ADDRESS</a>
     */
    class BLUETOOTH_ADDRESS extends Union {
        /** The Bluetooth address as a 64-bit unsigned integer (only lower 48 bits used). */
        public long ullLong;
        /** The Bluetooth address as a 6-byte array in network byte order. */
        public byte[] rgBytes = new byte[6];

        /**
         * Gets the address as a long.
         *
         * @return the address
         */
        public long getAddress() {
            setType("ullLong");
            read();
            return ullLong;
        }

        /**
         * Gets the address as a 6-byte array.
         *
         * @return the address bytes
         */
        public byte[] getBytes() {
            setType("rgBytes");
            read();
            return rgBytes;
        }
    }

    /**
     * The {@code BLUETOOTH_DEVICE_INFO} structure provides information about a Bluetooth device.
     *
     * @see <a href=
     *      "https://learn.microsoft.com/en-us/windows/win32/api/bluetoothapis/ns-bluetoothapis-bluetooth_device_info_struct">BLUETOOTH_DEVICE_INFO</a>
     */
    @FieldOrder({ "dwSize", "Address", "ulClassofDevice", "fConnected", "fRemembered", "fAuthenticated", "stLastSeen",
            "stLastUsed", "szName" })
    class BLUETOOTH_DEVICE_INFO extends Structure {
        /** Size of the structure, in bytes. Must be set before calling any function. */
        public int dwSize;
        /** Address of the device. */
        public BLUETOOTH_ADDRESS Address;
        /** Class of Device (CoD) of the device. */
        public int ulClassofDevice;
        /** Whether the device is connected. */
        public boolean fConnected;
        /** Whether the device is a remembered device. Not all remembered devices are authenticated. */
        public boolean fRemembered;
        /** Whether the device is authenticated, paired, or bonded. All authenticated devices are remembered. */
        public boolean fAuthenticated;
        /** Last time the device was seen. */
        public SYSTEMTIME stLastSeen;
        /** Last time the device was used. */
        public SYSTEMTIME stLastUsed;
        /** Name of the device. */
        public char[] szName = new char[BLUETOOTH_MAX_NAME_SIZE];

        /** Creates a new instance and sets {@code dwSize}. */
        public BLUETOOTH_DEVICE_INFO() {
            dwSize = size();
        }
    }

    /**
     * The {@code BLUETOOTH_DEVICE_SEARCH_PARAMS} structure specifies search criteria for Bluetooth device searches.
     *
     * @see <a href=
     *      "https://learn.microsoft.com/en-us/windows/win32/api/bluetoothapis/ns-bluetoothapis-bluetooth_device_search_params">BLUETOOTH_DEVICE_SEARCH_PARAMS</a>
     */
    @FieldOrder({ "dwSize", "fReturnAuthenticated", "fReturnRemembered", "fReturnUnknown", "fReturnConnected",
            "fIssueInquiry", "cTimeoutMultiplier", "hRadio" })
    class BLUETOOTH_DEVICE_SEARCH_PARAMS extends Structure {
        /** Size of the structure, in bytes. */
        public int dwSize;
        /** Whether to return authenticated devices. */
        public boolean fReturnAuthenticated;
        /** Whether to return remembered devices. */
        public boolean fReturnRemembered;
        /** Whether to return unknown devices. */
        public boolean fReturnUnknown;
        /** Whether to return connected devices. */
        public boolean fReturnConnected;
        /** Whether to issue a new inquiry. */
        public boolean fIssueInquiry;
        /** Timeout for the inquiry in increments of 1.28 seconds. Maximum value is 48. */
        public byte cTimeoutMultiplier;
        /** Handle to the radio on which to perform the inquiry. Set to {@code null} for all radios. */
        public HANDLE hRadio;

        /** Creates a new instance and sets {@code dwSize}. */
        public BLUETOOTH_DEVICE_SEARCH_PARAMS() {
            dwSize = size();
        }
    }

    /**
     * The {@code BLUETOOTH_FIND_RADIO_PARAMS} structure facilitates the enumeration of installed Bluetooth radios.
     *
     * @see <a href=
     *      "https://learn.microsoft.com/en-us/windows/win32/api/bluetoothapis/ns-bluetoothapis-bluetooth_find_radio_params">BLUETOOTH_FIND_RADIO_PARAMS</a>
     */
    @FieldOrder({ "dwSize" })
    class BLUETOOTH_FIND_RADIO_PARAMS extends Structure {
        /** Size of the structure, in bytes. */
        public int dwSize;

        /** Creates a new instance and sets {@code dwSize}. */
        public BLUETOOTH_FIND_RADIO_PARAMS() {
            dwSize = size();
        }
    }

    /**
     * The {@code BLUETOOTH_RADIO_INFO} structure contains information about a Bluetooth radio.
     *
     * @see <a href=
     *      "https://learn.microsoft.com/en-us/windows/win32/api/bluetoothapis/ns-bluetoothapis-bluetooth_radio_info">BLUETOOTH_RADIO_INFO</a>
     */
    @FieldOrder({ "dwSize", "address", "szName", "ulClassofDevice", "lmpSubversion", "manufacturer" })
    class BLUETOOTH_RADIO_INFO extends Structure {
        /** Size of the structure, in bytes. */
        public int dwSize;
        /** Address of the local Bluetooth radio. */
        public BLUETOOTH_ADDRESS address;
        /** Name of the local Bluetooth radio. */
        public char[] szName = new char[BLUETOOTH_MAX_NAME_SIZE];
        /** Class of Device for the local Bluetooth radio. */
        public int ulClassofDevice;
        /** Manufacturer-specific subversion data. */
        public short lmpSubversion;
        /** Manufacturer of the Bluetooth radio, expressed as a BTH_MFG_Xxx value. */
        public short manufacturer;

        /** Creates a new instance and sets {@code dwSize}. */
        public BLUETOOTH_RADIO_INFO() {
            dwSize = size();
        }
    }

    /**
     * Finds the first Bluetooth radio installed on the system.
     *
     * @param pbtfrp  A pointer to a {@link BLUETOOTH_FIND_RADIO_PARAMS} structure.
     * @param phRadio A pointer that receives the handle of the first radio found.
     * @return A handle to use with {@link #BluetoothFindNextRadio} and {@link #BluetoothFindRadioClose}, or
     *         {@code null} on failure. Call {@code GetLastError} for error information.
     * @see <a href=
     *      "https://learn.microsoft.com/en-us/windows/win32/api/bluetoothapis/nf-bluetoothapis-bluetoothfindfirstradio">BluetoothFindFirstRadio</a>
     */
    HANDLE BluetoothFindFirstRadio(BLUETOOTH_FIND_RADIO_PARAMS pbtfrp, HANDLEByReference phRadio);

    /**
     * Finds the next installed Bluetooth radio.
     *
     * @param hFind   The handle returned by {@link #BluetoothFindFirstRadio}.
     * @param phRadio A pointer that receives the handle of the next radio found.
     * @return {@code true} if another radio was found, {@code false} otherwise.
     * @see <a href=
     *      "https://learn.microsoft.com/en-us/windows/win32/api/bluetoothapis/nf-bluetoothapis-bluetoothfindnextradio">BluetoothFindNextRadio</a>
     */
    boolean BluetoothFindNextRadio(HANDLE hFind, HANDLEByReference phRadio);

    /**
     * Closes the enumeration handle for Bluetooth radios.
     *
     * @param hFind The handle returned by {@link #BluetoothFindFirstRadio}.
     * @return {@code true} on success, {@code false} on failure.
     * @see <a href=
     *      "https://learn.microsoft.com/en-us/windows/win32/api/bluetoothapis/nf-bluetoothapis-bluetoothfindradioclose">BluetoothFindRadioClose</a>
     */
    boolean BluetoothFindRadioClose(HANDLE hFind);

    /**
     * Retrieves information about a Bluetooth radio.
     *
     * @param hRadio     A handle to the Bluetooth radio obtained from {@link #BluetoothFindFirstRadio}.
     * @param pRadioInfo A pointer to a {@link BLUETOOTH_RADIO_INFO} structure to receive the radio information.
     * @return {@code ERROR_SUCCESS} (0) on success, or an error code on failure.
     * @see <a href=
     *      "https://learn.microsoft.com/en-us/windows/win32/api/bluetoothapis/nf-bluetoothapis-bluetoothgetradioinfo">BluetoothGetRadioInfo</a>
     */
    int BluetoothGetRadioInfo(HANDLE hRadio, BLUETOOTH_RADIO_INFO pRadioInfo);

    /**
     * Begins the enumeration of Bluetooth devices.
     *
     * @param pbtsp A pointer to a {@link BLUETOOTH_DEVICE_SEARCH_PARAMS} structure specifying search criteria.
     * @param pbtdi A pointer to a {@link BLUETOOTH_DEVICE_INFO} structure to receive the first device found.
     * @return A handle to use with {@link #BluetoothFindNextDevice} and {@link #BluetoothFindDeviceClose}, or
     *         {@code null} if no devices are found.
     * @see <a href=
     *      "https://learn.microsoft.com/en-us/windows/win32/api/bluetoothapis/nf-bluetoothapis-bluetoothfindfirstdevice">BluetoothFindFirstDevice</a>
     */
    HANDLE BluetoothFindFirstDevice(BLUETOOTH_DEVICE_SEARCH_PARAMS pbtsp, BLUETOOTH_DEVICE_INFO pbtdi);

    /**
     * Finds the next Bluetooth device.
     *
     * @param hFind The handle returned by {@link #BluetoothFindFirstDevice}.
     * @param pbtdi A pointer to a {@link BLUETOOTH_DEVICE_INFO} structure to receive the next device found.
     * @return {@code true} if another device was found, {@code false} otherwise.
     * @see <a href=
     *      "https://learn.microsoft.com/en-us/windows/win32/api/bluetoothapis/nf-bluetoothapis-bluetoothfindnextdevice">BluetoothFindNextDevice</a>
     */
    boolean BluetoothFindNextDevice(HANDLE hFind, BLUETOOTH_DEVICE_INFO pbtdi);

    /**
     * Closes the enumeration handle for Bluetooth devices.
     *
     * @param hFind The handle returned by {@link #BluetoothFindFirstDevice}.
     * @return {@code true} on success, {@code false} on failure.
     * @see <a href=
     *      "https://learn.microsoft.com/en-us/windows/win32/api/bluetoothapis/nf-bluetoothapis-bluetoothfinddeviceclose">BluetoothFindDeviceClose</a>
     */
    boolean BluetoothFindDeviceClose(HANDLE hFind);
}
