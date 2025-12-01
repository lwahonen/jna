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

public class VarDescBug {

    public static void main(String[] args){
        TypeLibUtil libUtil = new TypeLibUtil("C:\\Windows\\System32\\stdole2.tlb");
        int count = libUtil.getTypeInfoCount();
        for (int i = 0; i < count; i++) {
            TypeInfoUtil infoUtil = new TypeInfoUtil(libUtil.getTypeInfo(i));
            OaIdl.TYPEATTR attr = infoUtil.getTypeAttr();
            for (int j = 0; j < attr.cVars.intValue(); j++) {
                try {
                    infoUtil.getVarDesc(j);
                } catch (Exception e) {
                    System.out.println("ERROR:" + e);
                }
            }
        }
    }

}