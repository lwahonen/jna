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
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.W32APITypeMapper;

/**
 * Module Name:
 *     wlanapi.h
 * Abstract:
 *     Definitions and data structures for wlan auto config client side API.
 *
 * @author Eran Leshem
 */
public interface WlanApi extends Library {
    WlanApi INSTANCE = Native.load("wlanapi", WlanApi.class);
    int WLAN_MAX_NAME_LENGTH = 256;

    @Structure.FieldOrder({"dwNumberOfItems", "dwIndex", "InterfaceInfo"})
    class WLAN_INTERFACE_INFO_LIST extends Structure {
        public int dwNumberOfItems;
        public int dwIndex;
        public WLAN_INTERFACE_INFO[] InterfaceInfo = new WLAN_INTERFACE_INFO[1];

        public WLAN_INTERFACE_INFO_LIST() {
            setAutoSynch(false);
        }

        public WLAN_INTERFACE_INFO_LIST(Pointer p) {
            super(p);
        }

        @Override
        public void read() {
            // First element contains array size
            dwNumberOfItems = getPointer().getInt(0);
            if (dwNumberOfItems > 0) {
                InterfaceInfo = (WLAN_INTERFACE_INFO[]) new WLAN_INTERFACE_INFO().toArray(dwNumberOfItems);
                super.read();
            } else {
                InterfaceInfo = new WLAN_INTERFACE_INFO[0];
            }
        }
    }

    /**
     * Sruct WLAN_INTERFACE_INFO defines the basic information for an interface.
     */
    @Structure.FieldOrder({"InterfaceGuid", "strInterfaceDescription", "isState"})
    class WLAN_INTERFACE_INFO extends Structure {
        public Guid.GUID InterfaceGuid;
        public char[] strInterfaceDescription = new char[WLAN_MAX_NAME_LENGTH];

        /**
         * See {@link WLAN_INTERFACE_STATE} for possible values
         */
        public int isState;
    }

    /**
     * Structure WLAN_CONNECTION_ATTRIBUTES defines attributes of a wireless connection.
     */
    @Structure.FieldOrder({"isState", "wlanConnectionMode", "strProfileName", "wlanAssociationAttributes",
                           "wlanSecurityAttributes"})
    class WLAN_CONNECTION_ATTRIBUTES extends Structure {
        /**
         * See {@link WLAN_INTERFACE_STATE} for possible values
         */
        public int isState;

        /**
         * See {@link WLAN_CONNECTION_MODE} for possible values
         */
        public int wlanConnectionMode;
        public char[] strProfileName = new char[WLAN_MAX_NAME_LENGTH];
        public WLAN_ASSOCIATION_ATTRIBUTES wlanAssociationAttributes;
        public WLAN_SECURITY_ATTRIBUTES wlanSecurityAttributes;

        public WLAN_CONNECTION_ATTRIBUTES(Pointer p) {
            super(p);
        }
    }

    /**
     * The states of the network (interface).
     */
    interface WLAN_INTERFACE_STATE {
        int wlan_interface_state_not_ready = 0;
        int wlan_interface_state_connected = 1;
        int wlan_interface_state_ad_hoc_network_formed = 2;
        int wlan_interface_state_disconnecting = 3;
        int wlan_interface_state_disconnected = 4;
        int wlan_interface_state_associating = 5;
        int wlan_interface_state_discovering = 6;
        int wlan_interface_state_authenticating = 7;
    }

    interface WLAN_CONNECTION_MODE {
        int wlan_connection_mode_profile = 0;
        int wlan_connection_mode_temporary_profile = 1;
        int wlan_connection_mode_discovery_secure = 2;
        int wlan_connection_mode_discovery_unsecure = 3;
        int wlan_connection_mode_auto = 4;
        int wlan_connection_mode_invalid = 5;
    }

    /**
     * Structure WLAN_ASSOCIATION_ATTRIBUTES defines attributes of a wireless association.
     * The unit for Rx/Tx rate is Kbits/second.
     */
    @Structure.FieldOrder({"dot11Ssid", "dot11BssType", "dot11Bssid", "dot11PhyType", "uDot11PhyIndex",
                           "wlanSignalQuality", "ulRxRate", "ulTxRate"})
    class WLAN_ASSOCIATION_ATTRIBUTES extends Structure {
        public Windot11.DOT11_SSID dot11Ssid;

        /**
         * See {@link Windot11.DOT11_BSS_TYPE} for possible values
         */
        public int dot11BssType;
        public Windot11.DOT11_MAC_ADDRESS dot11Bssid;

        /**
         * See {@link Windot11.DOT11_BSS_TYPE} for possible values
         */
        public int dot11PhyType;
        public WinDef.ULONG uDot11PhyIndex;
        public WinDef.ULONG wlanSignalQuality;   // WLAN_SIGNAL_QUALITY
        public WinDef.ULONG ulRxRate;
        public WinDef.ULONG ulTxRate;

        public WLAN_ASSOCIATION_ATTRIBUTES(Pointer p) {
            super(p);
        }
    }

    @Structure.FieldOrder({"bSecurityEnabled", "bOneXEnabled", "dot11AuthAlgorithm", "dot11CipherAlgorithm"})
    class WLAN_SECURITY_ATTRIBUTES extends Structure {
        public boolean bSecurityEnabled;
        public boolean bOneXEnabled;

        /**
         * See {@link Windot11.DOT11_AUTH_ALGORITHM} for possible values
         */
        public int dot11AuthAlgorithm;

        /**
         * See {@link Windot11.DOT11_CIPHER_ALGORITHM} for possible values
         */
        public int dot11CipherAlgorithm;

        public WLAN_SECURITY_ATTRIBUTES() {
            super(W32APITypeMapper.UNICODE);
        }
    }

    /**
     * OpCodes for set/query interfaces.
     */
    interface WLAN_INTF_OPCODE {
        int wlan_intf_opcode_autoconf_start = 0x000000000;
        int wlan_intf_opcode_autoconf_enabled = 1;
        int wlan_intf_opcode_background_scan_enabled = 2;
        int wlan_intf_opcode_media_streaming_mode = 3;
        int wlan_intf_opcode_radio_state = 4;
        int wlan_intf_opcode_bss_type = 5;
        int wlan_intf_opcode_interface_state = 6;
        int wlan_intf_opcode_current_connection = 7;
        int wlan_intf_opcode_channel_number = 8;
        int wlan_intf_opcode_supported_infrastructure_auth_cipher_pairs = 9;
        int wlan_intf_opcode_supported_adhoc_auth_cipher_pairs = 10;
        int wlan_intf_opcode_supported_country_or_region_string_list = 11;
        int wlan_intf_opcode_current_operation_mode = 12;
        int wlan_intf_opcode_supported_safe_mode = 13;
        int wlan_intf_opcode_certified_safe_mode = 14;
        int wlan_intf_opcode_hosted_network_capable = 15;
        int wlan_intf_opcode_autoconf_end = 0x0fffffff;
        int wlan_intf_opcode_msm_start = 0x10000100;
        int wlan_intf_opcode_statistics = 0x10000101;
        int wlan_intf_opcode_rssi = 0x10000102;
        int wlan_intf_opcode_msm_end = 0x1fffffff;
        int wlan_intf_opcode_security_start = 0x20010000;
        int wlan_intf_opcode_security_end = 0x2fffffff;
        int wlan_intf_opcode_ihv_start = 0x30000000;
        int wlan_intf_opcode_ihv_end = 0x3fffffff;
    }

    interface WLAN_OPCODE_VALUE_TYPE {
        int wlan_opcode_value_type_query_only = 0;
        int wlan_opcode_value_type_set_by_group_policy = 1;
        int wlan_opcode_value_type_set_by_user = 2;
        int wlan_opcode_value_type_invalid = 3;
    }

    int WlanOpenHandle(int dwClientVersion, Pointer pReserved, IntByReference pdwNegotiatedVersion,
                       WinNT.HANDLEByReference phClientHandle);
    int WlanCloseHandle(WinNT.HANDLE hClientHandle, Pointer pReserved);
    int WlanEnumInterfaces(WinNT.HANDLE hClientHandle, Pointer pReserved, PointerByReference ppInterfaceList);

    /**
     * @param OpCode See {@link WLAN_INTF_OPCODE} for possible values
     * @param pWlanOpcodeValueType See {@link WLAN_OPCODE_VALUE_TYPE} for possible values
     */
    int WlanQueryInterface(WinNT.HANDLE hClientHandle, Guid.GUID pInterfaceGuid, int OpCode,
                           Pointer pReserved, IntByReference pDataSize, PointerByReference ppData,
                           IntByReference pWlanOpcodeValueType);
    void WlanFreeMemory(Pointer pMemory);
}
