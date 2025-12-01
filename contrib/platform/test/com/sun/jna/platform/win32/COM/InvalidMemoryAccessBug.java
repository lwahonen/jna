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