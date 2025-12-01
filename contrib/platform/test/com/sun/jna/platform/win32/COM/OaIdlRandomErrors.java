/* Copyright (c) 2024 Lauri Ahonen, All Rights Reserved
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
package com.sun.jna.platform.win32.COM;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.sun.jna.platform.win32.OaIdl.FUNCDESC;
import com.sun.jna.platform.win32.OaIdl.TLIBATTR;
import com.sun.jna.platform.win32.OaIdl.TYPEATTR;
import com.sun.jna.platform.win32.OaIdl.VARDESC;

public class OaIdlRandomErrors {
    static Set<String> includedFilenames = new HashSet<>();

    public static void main(String[] args) {
        String[] filenames = {"accessibilitycpl.dll", "activeds.tlb", "adprovider.dll", "amcompat.tlb", "apds.dll", "AppIdPolicyEngineApi.dll", "atl.dll", "atl100.dll", "atl110.dll", "AuditPolicyGPInterop.dll", "azroles.dll", "bcdsrv.dll", "capiprovider.dll", "catsrvut.dll", "cdosys.dll", "certcli.dll", "certenc.dll", "CertEnroll.dll", "certmgr.dll", "cic.dll", "clbcatq.dll", "cngprovider.dll", "comrepl.dll", "comsnap.dll", "comsvcs.dll", "connect.dll", "correngine.dll", "cryptext.dll", "DefaultPrinterProvider.dll", "DevicePairing.dll", "DevicePairingProxy.dll", "DfsShlEx.dll", "DiagCpl.dll", "dmocx.dll", "DMRServer.dll", "dnshc.dll", "dot3dlg.dll", "dot3hc.dll", "dpapiprovider.dll", "dskquota.dll", "dtsh.dll", "dxtmsft.dll", "dxtrans.dll", "EditionUpgradeHelper.dll", "EditionUpgradeManagerObj.dll", "EhStorAPI.dll", "EhStorShell.dll", "es.dll", "eventcls.dll", "expsrv.dll", "findnetprinters.dll", "FirewallAPI.dll", "FirewallControlPanel.dll", "fphc.dll", "FXSCOM.dll", "FXSCOMEX.dll", "gpprefcl.dll", "Groupinghc.dll", "HelpPaneProxy.dll", "hnetcfg.dll", "iasads.dll", "iasdatastore.dll", "iashlpr.dll", "iasnap.dll", "iasrad.dll", "iassam.dll", "iassdo.dll", "iassvcs.dll", "icsvc.dll", "ieframe.dll", "iepeers.dll", "igdDiag.dll", "imapi2.dll", "imapi2fs.dll", "inetcomm.dll", "InkEd.dll", "InkObjCore.dll", "ipnathlp.dll", "JavaScriptCollectionAgent.dll", "jscript.dll", "L2SecHC.dll", "LocationApi.dll", "MbaeApi.dll", "mmcndmgr.dll", "msaatext.dll", "msdatsrc.tlb", "msdtcuiu.dll", "msdxm.tlb", "msfeeds.dll", "msftedit.dll", "mshtml.tlb", "mshtmled.dll", "msi.dll", "msjtes40.dll", "MsraLegacy.tlb", "MsRdpWebAccess.dll", "mssitlb.dll", "mssrch.dll", "mstscax.dll", "msvbvm60.dll", "MSVidCtl.dll", "mswmdm.dll", "msxml3.dll", "msxml4.dll", "msxml6.dll", "mycomput.dll", "NaturalLanguage6.dll", "ndfapi.dll", "ndishc.dll", "netcenter.dll", "netcorehc.dll", "netprofm.dll", "NetworkCollectionAgent.dll", "nlahc.dll", "odbcconf.dll", "officecsp.dll", "oleacc.dll", "oleprn.dll", "pla.dll", "PNPXAssoc.dll", "PNPXAssocPrx.dll", "Pnrphc.dll", "PortableDeviceApi.dll", "PortableDeviceClassExtension.dll", "PortableDeviceConnectApi.dll", "PortableDeviceTypes.dll", "PrintConfig.dll", "printui.dll", "prnntfy.dll", "provcore.dll", "psisdecd.dll", "pstorec.dll", "puiapi.dll", "puiobj.dll", "qedit.dll", "quartz.dll", "rasdiag.dll", "rasgcw.dll", "rdpcorets.dll", "rdpencom.dll", "RdpRelayTransport.dll", "rdpsharercom.dll", "rdpviewerax.dll", "RegCtrl.dll", "rendezvousSession.tlb", "riched20.dll", "RoamingSecurity.dll", "RotMgr.dll", "scripto.dll", "scrobj.dll", "scrrun.dll", "sdiageng.dll", "sdohlp.dll", "Sens.dll", "shdocvw.dll", "shell32.dll", "shgina.dll", "signdrv.dll", "simpdata.tlb", "SMBHelperClass.dll", "SmiEngine.dll", "sppcomapi.dll", "sppwmi.dll", "SRH.dll", "srm.dll", "stclient.dll", "stdole2.tlb", "stdole32.tlb", "swprv.dll", "SysFxUI.dll", "TabbtnEx.dll", "tapi3.dll", "taskschd.dll", "termmgr.dll", "TransportDSA.dll", "TSWorkspace.dll", "tvratings.dll", "ucmhc.dll", "UIAnimation.dll", "UIAutomationCore.dll", "uicom.dll", "upnp.dll", "usbmon.dll", "VAN.dll", "vbscript.dll", "WaaSMedicPS.dll", "wavemsp.dll", "WfHC.dll", "wiaaut.dll", "wiascanprofiles.dll", "win32spl.dll", "wincredprovider.dll", "windowslivelogin.dll", "winethc.dll", "winhttpcom.dll", "WinMsoIrmProtector.dll", "WinOpcIrmProtector.dll", "WinSATAPI.dll", "wisp.dll", "wkspbrokerAx.dll", "WLanConn.dll", "wlandlg.dll", "WLanHC.dll", "wlanpref.dll", "wlanui.dll", "wlidcli.dll", "wlidprov.dll", "wmdmlog.dll", "WMNetMgr.dll", "wmp.dll", "wmpdxm.dll", "wmpshell.dll", "WorkFoldersShell.dll", "workfolderssvc.dll", "WPDSp.dll", "wscapi.dll", "wshcon.dll", "wshext.dll", "WsmAuto.dll", "wuapi.dll", "wvc.dll", "WWanAPI.dll", "WWanHC.dll", "xwizards.dll", "xwreg.dll", "xwtpdui.dll", "xwtpw32.dll"};
        includedFilenames.addAll(Arrays.asList(filenames));
        processDir(new File("C:\\Windows\\System32"));
        processDir(new File("C:\\Windows\\SysWOW64"));
        System.out.println("Finished.");
    }

    protected static void processDir(File dir) {
        for (File file : dir.listFiles()) {
            if (includedFilenames.contains(file.getName())) {
                processFile(file);
            }
        }
    }

    protected static void processFile(File file) {
        try {
            System.out.println(file);
            TypeLibUtil libUtil = new TypeLibUtil(file.toString());

            // Lib attributes
            TLIBATTR libAttr = libUtil.getLibAttr();
            libUtil.ReleaseTLibAttr(libAttr);

            // Types
            int count = libUtil.getTypeInfoCount();
            System.out.println("Type count: " + count);
            for (int i = 0; i < count; i++) {
                TypeInfoUtil typeUtil = libUtil.getTypeInfoUtil(i);

                // Type attributes
                TYPEATTR typeAttr = typeUtil.getTypeAttr();
                typeUtil.ReleaseTypeAttr(typeAttr);

                // Functions
                for (int j = 0; j < typeAttr.cFuncs.intValue(); j++) {
                    FUNCDESC funcDesc = typeUtil.getFuncDesc(j);
                    typeUtil.ReleaseFuncDesc(funcDesc);
                }

                // Variables
                for (int j = 0; j < typeAttr.cVars.intValue(); j++) {
                    VARDESC varDesc = typeUtil.getVarDesc(j);
                    typeUtil.ReleaseVarDesc(varDesc);
                }
            }

            libUtil.getTypelib().Release();

        } catch (Throwable e) {
            e.printStackTrace(System.out);
        }
    }
}