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