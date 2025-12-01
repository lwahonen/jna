package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.OaIdl;

/**
 * Test for TYPEDESC and FUNCDESC union handling.
 * This test iterates through functions in a Shell32 type library interface
 * and verifies that JNA correctly reads the discriminated unions in TYPEDESC
 * and handles the lprgelemdescParam array correctly when cParams=0.
 */
public class InvalidMemoryAccessBug {
    public static void main(String[] args) {
        TypeLibUtil libUtil = new TypeLibUtil("{50A7E9B0-70EF-11D1-B75A-00A0C90564FE}", 1, 0); // C:\Windows\SysWOW64\shell32.dll
        int typeIndex = 21;

        ITypeInfo typeInfo = libUtil.getTypeInfo(typeIndex);
        TypeInfoUtil util = new TypeInfoUtil(typeInfo);
        OaIdl.TYPEATTR attr = util.getTypeAttr();

        for (int i = 0; i < attr.cFuncs.intValue(); i++) {
            OaIdl.FUNCDESC func = util.getFuncDesc(i);
            TypeInfoUtil.TypeInfoDoc funcDoc = util.getDocumentation(func.memid);
            System.out.println("Function " + i + ": " + funcDoc.getName() + " (cParams=" + func.cParams.shortValue() + ")");
            util.ReleaseFuncDesc(func);
        }
    }
}