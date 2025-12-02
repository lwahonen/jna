/* Copyright (c) 2025 Eran Leshem, All Rights Reserved
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

import com.sun.jna.Library;
import com.sun.jna.Structure;

/**
 * Module Name:
 *     windot11.h
 * Abstract:
 *     Definitions for native 802.11 miniport driver specifications.
 *
 * @author Eran Leshem
 */
public interface Windot11 extends Library {
    int DOT11_SSID_MAX_LENGTH = 32; // 32 bytes

    interface DOT11_BSS_TYPE {
        int dot11_BSS_type_infrastructure = 1;
        int dot11_BSS_type_independent = 2;
        int dot11_BSS_type_any = 3;
    }

    @Structure.FieldOrder({"uSSIDLength", "ucSSID"})
    class DOT11_SSID extends Structure {
        public WinDef.ULONG uSSIDLength;
        public byte[] ucSSID = new byte[DOT11_SSID_MAX_LENGTH];
    }

    @Structure.FieldOrder("ucDot11MacAddress")
    class DOT11_MAC_ADDRESS extends Structure {
        public WinDef.UCHAR[] ucDot11MacAddress = new WinDef.UCHAR[6];
    }

    interface DOT11_PHY_TYPE {
        int dot11_phy_type_unknown = 0;
        int dot11_phy_type_any = dot11_phy_type_unknown;
        int dot11_phy_type_fhss = 1;
        int dot11_phy_type_dsss = 2;
        int dot11_phy_type_irbaseband = 3;
        int dot11_phy_type_ofdm = 4;
        int dot11_phy_type_hrdsss = 5;
        int dot11_phy_type_erp = 6;
        int dot11_phy_type_ht = 7;
        int dot11_phy_type_IHV_start = 0x80000000;
        int dot11_phy_type_IHV_end = 0xffffffff;
    }

    // DOT11_AUTH_ALGO_LIST
    interface DOT11_AUTH_ALGORITHM {
        int DOT11_AUTH_ALGO_80211_OPEN = 1;
        int DOT11_AUTH_ALGO_80211_SHARED_KEY = 2;
        int DOT11_AUTH_ALGO_WPA = 3;
        int DOT11_AUTH_ALGO_WPA_PSK = 4;
        int DOT11_AUTH_ALGO_WPA_NONE = 5;               // used in NatSTA only
        int DOT11_AUTH_ALGO_RSNA = 6;
        int DOT11_AUTH_ALGO_RSNA_PSK = 7;
        int DOT11_AUTH_ALGO_WPA3 = 8;
        int DOT11_AUTH_ALGO_WPA3_ENT_192 = DOT11_AUTH_ALGO_WPA3;
        int DOT11_AUTH_ALGO_WPA3_SAE = 9;
        int DOT11_AUTH_ALGO_OWE = 10;
        int DOT11_AUTH_ALGO_WPA3_ENT = 11;
        int DOT11_AUTH_ALGO_IHV_START = 0x80000000;
        int DOT11_AUTH_ALGO_IHV_END = 0xffffffff;
    }

    // Cipher algorithm Ids (for little endian platform)
    interface DOT11_CIPHER_ALGORITHM {
        int DOT11_CIPHER_ALGO_NONE = 0x00;
        int DOT11_CIPHER_ALGO_WEP40 = 0x01;
        int DOT11_CIPHER_ALGO_TKIP = 0x02;
        int DOT11_CIPHER_ALGO_CCMP = 0x04;
        int DOT11_CIPHER_ALGO_WEP104 = 0x05;
        int DOT11_CIPHER_ALGO_BIP = 0x06;
        int DOT11_CIPHER_ALGO_GCMP = 0x08;
        int DOT11_CIPHER_ALGO_GCMP_256 = 0x09;
        int DOT11_CIPHER_ALGO_CCMP_256 = 0x0a;
        int DOT11_CIPHER_ALGO_BIP_GMAC_128 = 0x0b;
        int DOT11_CIPHER_ALGO_BIP_GMAC_256 = 0x0c;
        int DOT11_CIPHER_ALGO_BIP_CMAC_256 = 0x0d;
        int DOT11_CIPHER_ALGO_WPA_USE_GROUP = 0x100;
        int DOT11_CIPHER_ALGO_RSN_USE_GROUP = 0x100;
        int DOT11_CIPHER_ALGO_WEP = 0x101;
        int DOT11_CIPHER_ALGO_IHV_START = 0x80000000;
        int DOT11_CIPHER_ALGO_IHV_END = 0xffffffff;
    }
}
