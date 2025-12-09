/* Copyright (c) 2025 Lauri Ahonen, All Rights Reserved
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

import com.sun.jna.platform.win32.COM.ITypeInfo;
import com.sun.jna.platform.win32.COM.TypeInfoUtil;
import com.sun.jna.platform.win32.COM.TypeLibUtil;
import java.io.File;
import org.junit.Test;

import static org.junit.Assert.*;


public class OaIdlTest {

    public OaIdlTest() {
    }

    /**
     * Test for TYPEDESC and FUNCDESC union handling.
     * This test iterates through functions in a Shell32 type library interface
     * and verifies that JNA correctly reads the discriminated unions in
     * TYPEDESC
     * and handles the lprgelemdescParam array correctly when cParams=0.
     */
    @Test
    public void testReadFuncDesc() {
        TypeLibUtil libUtil = new TypeLibUtil("{50A7E9B0-70EF-11D1-B75A-00A0C90564FE}", 1, 0); // C:\Windows\SysWOW64\shell32.dll

        // At time of test writing index 21 as IShellFolderViewDual3
        int typeIndex = 21;

        ITypeInfo typeInfo = libUtil.getTypeInfo(typeIndex);
        TypeInfoUtil util = new TypeInfoUtil(typeInfo);
        OaIdl.TYPEATTR attr = util.getTypeAttr();

        System.out.println(util.getDocumentation(new OaIdl.MEMBERID(-1)).getName());

        assertTrue("Expecting at least 1 function in the target type", attr.cFuncs.intValue() > 0);

        for (int i = 0; i < attr.cFuncs.intValue(); i++) {
            OaIdl.FUNCDESC func = util.getFuncDesc(i);
            TypeInfoUtil.TypeInfoDoc funcDoc = util.getDocumentation(func.memid);
            System.out.println("Function " + i + ": " + funcDoc.getName() + " (cParams=" + func.cParams.shortValue() + ")");
            util.ReleaseFuncDesc(func);
        }

        // Reaching the end of the function is an implicit test that reading
        // succeeded and not exception was raised
    }

    @Test
    public void testReadVarDesc() {
        TypeLibUtil libUtil = new TypeLibUtil("C:\\Windows\\System32\\stdole2.tlb");
        int count = libUtil.getTypeInfoCount();
        for (int i = 0; i < count; i++) {
            TypeInfoUtil infoUtil = new TypeInfoUtil(libUtil.getTypeInfo(i));
            OaIdl.TYPEATTR attr = infoUtil.getTypeAttr();
            for (int j = 0; j < attr.cVars.intValue(); j++) {
                infoUtil.getVarDesc(j);
            }
        }
        // Reaching the end of the function is an implicit test that reading
        // succeeded and not exception was raised
    }

    @Test
    public void testOaidlRandomErrors() {
        String[] filenames = new String[]{
            "accessibilitycpl.dll",
            "activeds.tlb",
            "adprovider.dll",
            "amcompat.tlb",
            "apds.dll",
            "AppIdPolicyEngineApi.dll",
            "atl.dll",
            "atl100.dll",
            "atl110.dll",
            "AuditPolicyGPInterop.dll",
            "azroles.dll",
            "bcdsrv.dll",
            "capiprovider.dll",
            "catsrvut.dll",
            "cdosys.dll",
            "certcli.dll",
            "certenc.dll",
            "CertEnroll.dll",
            "certmgr.dll",
            "cic.dll",
            "clbcatq.dll",
            "cngprovider.dll",
            "comrepl.dll",
            "comsnap.dll",
            "comsvcs.dll",
            "connect.dll",
            "correngine.dll",
            "cryptext.dll",
            "DefaultPrinterProvider.dll",
            "DevicePairing.dll",
            "DevicePairingProxy.dll",
            "DfsShlEx.dll",
            "DiagCpl.dll",
            "dmocx.dll",
            "DMRServer.dll",
            "dnshc.dll",
            "dot3dlg.dll",
            "dot3hc.dll",
            "dpapiprovider.dll",
            "dskquota.dll",
            "dtsh.dll",
            "dxtmsft.dll",
            "dxtrans.dll",
            "EditionUpgradeHelper.dll",
            "EditionUpgradeManagerObj.dll",
            "EhStorAPI.dll",
            "EhStorShell.dll",
            "es.dll",
            "eventcls.dll",
            "expsrv.dll",
            "findnetprinters.dll",
            "FirewallAPI.dll",
            "FirewallControlPanel.dll",
            "fphc.dll",
            "FXSCOM.dll",
            "FXSCOMEX.dll",
            "gpprefcl.dll",
            "Groupinghc.dll",
            "HelpPaneProxy.dll",
            "hnetcfg.dll",
            "iasads.dll",
            "iasdatastore.dll",
            "iashlpr.dll",
            "iasnap.dll",
            "iasrad.dll",
            "iassam.dll",
            "iassdo.dll",
            "iassvcs.dll",
            "icsvc.dll",
            "ieframe.dll",
            "iepeers.dll",
            "igdDiag.dll",
            "imapi2.dll",
            "imapi2fs.dll",
            "inetcomm.dll",
            "InkEd.dll",
            "InkObjCore.dll",
            "ipnathlp.dll",
            "JavaScriptCollectionAgent.dll",
            "jscript.dll",
            "L2SecHC.dll",
            "LocationApi.dll",
            "MbaeApi.dll",
            "mmcndmgr.dll",
            "msaatext.dll",
            "msdatsrc.tlb",
            "msdtcuiu.dll",
            "msdxm.tlb",
            "msfeeds.dll",
            "msftedit.dll",
            "mshtml.tlb",
            "mshtmled.dll",
            "msi.dll",
            "msjtes40.dll",
            "MsraLegacy.tlb",
            "MsRdpWebAccess.dll",
            "mssitlb.dll",
            "mssrch.dll",
            "mstscax.dll",
            "msvbvm60.dll",
            "MSVidCtl.dll",
            "mswmdm.dll",
            "msxml3.dll",
            "msxml4.dll",
            "msxml6.dll",
            "mycomput.dll",
            "NaturalLanguage6.dll",
            "ndfapi.dll",
            "ndishc.dll",
            "netcenter.dll",
            "netcorehc.dll",
            "netprofm.dll",
            "NetworkCollectionAgent.dll",
            "nlahc.dll",
            "odbcconf.dll",
            "officecsp.dll",
            "oleacc.dll",
            "oleprn.dll",
            "pla.dll",
            "PNPXAssoc.dll",
            "PNPXAssocPrx.dll",
            "Pnrphc.dll",
            "PortableDeviceApi.dll",
            "PortableDeviceClassExtension.dll",
            "PortableDeviceConnectApi.dll",
            "PortableDeviceTypes.dll",
            "PrintConfig.dll",
            "printui.dll",
            "prnntfy.dll",
            "provcore.dll",
            "psisdecd.dll",
            "pstorec.dll",
            "puiapi.dll",
            "puiobj.dll",
            "qedit.dll",
            "quartz.dll",
            "rasdiag.dll",
            "rasgcw.dll",
            "rdpcorets.dll",
            "rdpencom.dll",
            "RdpRelayTransport.dll",
            "rdpsharercom.dll",
            "rdpviewerax.dll",
            "RegCtrl.dll",
            "rendezvousSession.tlb",
            "riched20.dll",
            "RoamingSecurity.dll",
            "RotMgr.dll",
            "scripto.dll",
            "scrobj.dll",
            "scrrun.dll",
            "sdiageng.dll",
            "sdohlp.dll",
            "Sens.dll",
            "shdocvw.dll",
            "shell32.dll",
            "shgina.dll",
            "signdrv.dll",
            "simpdata.tlb",
            "SMBHelperClass.dll",
            "SmiEngine.dll",
            "sppcomapi.dll",
            "sppwmi.dll",
            "SRH.dll",
            "srm.dll",
            "stclient.dll",
            "stdole2.tlb",
            "stdole32.tlb",
            "swprv.dll",
            "SysFxUI.dll",
            "TabbtnEx.dll",
            "tapi3.dll",
            "taskschd.dll",
            "termmgr.dll",
            "TransportDSA.dll",
            "TSWorkspace.dll",
            "tvratings.dll",
            "ucmhc.dll",
            "UIAnimation.dll",
            "UIAutomationCore.dll",
            "uicom.dll",
            "upnp.dll",
            "usbmon.dll",
            "VAN.dll",
            "vbscript.dll",
            "WaaSMedicPS.dll",
            "wavemsp.dll",
            "WfHC.dll",
            "wiaaut.dll",
            "wiascanprofiles.dll",
            "win32spl.dll",
            "wincredprovider.dll",
            "windowslivelogin.dll",
            "winethc.dll",
            "winhttpcom.dll",
            "WinMsoIrmProtector.dll",
            "WinOpcIrmProtector.dll",
            "WinSATAPI.dll",
            "wisp.dll",
            "wkspbrokerAx.dll",
            "WLanConn.dll",
            "wlandlg.dll",
            "WLanHC.dll",
            "wlanpref.dll",
            "wlanui.dll",
            "wlidcli.dll",
            "wlidprov.dll",
            "wmdmlog.dll",
            "WMNetMgr.dll",
            "wmp.dll",
            "wmpdxm.dll",
            "wmpshell.dll",
            "WorkFoldersShell.dll",
            "workfolderssvc.dll",
            "WPDSp.dll",
            "wscapi.dll",
            "wshcon.dll",
            "wshext.dll",
            "WsmAuto.dll",
            "wuapi.dll",
            "wvc.dll",
            "WWanAPI.dll",
            "WWanHC.dll",
            "xwizards.dll",
            "xwreg.dll",
            "xwtpdui.dll",
            "xwtpw32.dll"
        };

        boolean atLeastOneTypeLibraryWithTypes = false;
        for (String directory : new String[]{"C:\\Windows\\System32", "C:\\Windows\\SysWOW64"}) {
            for (String filename : filenames) {
                File file = new File(directory, filename);
                if (file.exists()) {
                    TypeLibUtil libUtil = new TypeLibUtil(file.toString());
                    // Lib attributes
                    OaIdl.TLIBATTR libAttr = libUtil.getLibAttr();
                    libUtil.ReleaseTLibAttr(libAttr);
                    // Types
                    int count = libUtil.getTypeInfoCount();
                    if(count > 0) {
                        atLeastOneTypeLibraryWithTypes = true;
                    }
                    System.out.printf("%s => %d types%n", file, count);
                    for (int i = 0; i < count; i++) {
                        TypeInfoUtil typeUtil = libUtil.getTypeInfoUtil(i);
                        // Type attributes
                        OaIdl.TYPEATTR typeAttr = typeUtil.getTypeAttr();
                        typeUtil.ReleaseTypeAttr(typeAttr);
                        // Functions
                        for (int j = 0; j < typeAttr.cFuncs.intValue(); j++) {
                            OaIdl.FUNCDESC funcDesc = typeUtil.getFuncDesc(j);
                            typeUtil.ReleaseFuncDesc(funcDesc);
                        }       // Variables
                        for (int j1 = 0; j1 < typeAttr.cVars.intValue(); j1++) {
                            OaIdl.VARDESC varDesc = typeUtil.getVarDesc(j1);
                            typeUtil.ReleaseVarDesc(varDesc);
                        }
                    }
                    libUtil.getTypelib().Release();
                }
            }
        }

        assertTrue("No typelibrary with types read", atLeastOneTypeLibraryWithTypes);

        // Reaching the end of the function is an implicit test that reading
        // succeeded and not exception was raised
    }

}
