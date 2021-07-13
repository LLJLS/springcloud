/**
 * Software License Declaration.
 * <p>
 * zhilingsd.com, Co,. Ltd.
 * Copyright © 2016 All Rights Reserved.
 * <p>
 * Copyright Notice
 * This documents is provided to zhilingsd contracting agent or authorized programmer only.
 * This source code is written and edited by zhilingsd Co,.Ltd Inc specially for financial
 * business contracting agent or authorized cooperative company, in order to help them to
 * install, programme or central control in certain project by themselves independently.
 * <p>
 * Disclaimer
 * If this source code is needed by the one neither contracting agent nor authorized programmer
 * during the use of the code, should contact to zhilingsd Co,. Ltd Inc, and get the confirmation
 * and agreement of three departments managers  - Research Department, Marketing Department and
 * Production Department.Otherwise zhilingsd will charge the fee according to the programme itself.
 * <p>
 * Any one,including contracting agent and authorized programmer,cannot share this code to
 * the third party without the agreement of zhilingsd. If Any problem cannot be solved in the
 * procedure of programming should be feedback to zhilingsd Co,. Ltd Inc in time, Thank you!
 */
package com.kevin.drools.entity;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @className Test.java
 * @description //TODO
 * @author Administrator
 * @version 1.0
 * @date 2021/6/25 11:08
 */
public class Test {
    public static void main(String[] args) {
        L l = new LImpl();
        S s = new SImpl();
        s.setL(l);
        SImpl s1 = (SImpl)s;
        s1.save();


    }

}

interface S {
    void setL(L l);
}

class SImpl implements S {

    private L l;
    @Override
    public void setL(L l) {
        this.l = l;
    }

    public void save() {
        System.out.println("插入一条数据");
        EImpl save = new EImpl(this, "save");
        l.handle(save);
    }

}

interface L {
    void handle(E e);
}

class LImpl implements L {

    @Override
    public void handle(E e) {
        String type = e.getType();
        if (E.SAVE.equals(type)) {
            System.out.println("添加操作");
        }
    }
}

interface E {
    String SAVE = "save";
    S getS();
    String getType();
}

class EImpl implements E {

    private S s;
    private String type;

    public EImpl(S s, String type) {
        this.s = s;
        this.type = type;
    }

    @Override
    public S getS() {
        return s;
    }

    @Override
    public String getType() {
        if (type.equals("save")) {
            return SAVE;
        }
        return null;
    }
}


