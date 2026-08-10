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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sun.jna.platform.win32.BluetoothApis.BLUETOOTH_DEVICE_INFO;
import com.sun.jna.platform.win32.BluetoothApis.BLUETOOTH_DEVICE_SEARCH_PARAMS;
import com.sun.jna.platform.win32.BluetoothApis.BLUETOOTH_FIND_RADIO_PARAMS;
import com.sun.jna.platform.win32.BluetoothApis.BLUETOOTH_RADIO_INFO;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinNT.HANDLEByReference;

/**
 * Tests for {@link BluetoothApis}.
 */
public class BluetoothApisTest {

    @Test
    public void testStructureSizes() {
        BLUETOOTH_FIND_RADIO_PARAMS radioParams = new BLUETOOTH_FIND_RADIO_PARAMS();
        assertTrue("BLUETOOTH_FIND_RADIO_PARAMS size should be at least 4", radioParams.dwSize >= 4);

        BLUETOOTH_DEVICE_SEARCH_PARAMS searchParams = new BLUETOOTH_DEVICE_SEARCH_PARAMS();
        assertTrue("BLUETOOTH_DEVICE_SEARCH_PARAMS size should be at least 32", searchParams.dwSize >= 32);

        BLUETOOTH_DEVICE_INFO deviceInfo = new BLUETOOTH_DEVICE_INFO();
        assertTrue("BLUETOOTH_DEVICE_INFO size should be at least 560", deviceInfo.dwSize >= 560);

        BLUETOOTH_RADIO_INFO radioInfo = new BLUETOOTH_RADIO_INFO();
        assertTrue("BLUETOOTH_RADIO_INFO size should be at least 520", radioInfo.dwSize >= 520);
    }

    @Test
    public void testBluetoothFindFirstRadio() {
        BLUETOOTH_FIND_RADIO_PARAMS radioParams = new BLUETOOTH_FIND_RADIO_PARAMS();
        HANDLEByReference phRadio = new HANDLEByReference();

        // This may return null if no Bluetooth radio is present, which is acceptable
        HANDLE hFind = BluetoothApis.INSTANCE.BluetoothFindFirstRadio(radioParams, phRadio);
        if (hFind != null) {
            try {
                HANDLE hRadio = phRadio.getValue();
                try {
                    assertNotNull("Radio handle should not be null", hRadio);

                    BLUETOOTH_RADIO_INFO radioInfo = new BLUETOOTH_RADIO_INFO();
                    int result = BluetoothApis.INSTANCE.BluetoothGetRadioInfo(hRadio, radioInfo);
                    assertTrue("BluetoothGetRadioInfo should succeed", result == 0);

                    // Enumerate devices
                    BLUETOOTH_DEVICE_SEARCH_PARAMS searchParams = new BLUETOOTH_DEVICE_SEARCH_PARAMS();
                    searchParams.fReturnAuthenticated = true;
                    searchParams.fReturnRemembered = true;
                    searchParams.fReturnConnected = true;
                    searchParams.fReturnUnknown = false;
                    searchParams.fIssueInquiry = false;
                    searchParams.cTimeoutMultiplier = 0;
                    searchParams.hRadio = hRadio;

                    BLUETOOTH_DEVICE_INFO deviceInfo = new BLUETOOTH_DEVICE_INFO();
                    HANDLE hFindDevice = BluetoothApis.INSTANCE.BluetoothFindFirstDevice(searchParams, deviceInfo);
                    if (hFindDevice != null) {
                        BluetoothApis.INSTANCE.BluetoothFindDeviceClose(hFindDevice);
                    }
                } finally {
                    Kernel32.INSTANCE.CloseHandle(hRadio);
                }
            } finally {
                BluetoothApis.INSTANCE.BluetoothFindRadioClose(hFind);
            }
        }
    }
}
