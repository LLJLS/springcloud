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

import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;

import java.util.Iterator;

/**
 * @className DroolsApp.java
 * @description //TODO
 * @author Administrator
 * @version 1.0
 * @date 2021/6/17 10:21
 */
public class DroolsApp {
    public static void main(String[] args) {
        System.setProperty("drools.dateformat","yyyy-MM-dd HH:mm:ss");
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieClasspathContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieClasspathContainer.newKieSession("ksession-rules");
        Fact fact = new Fact();
        fact.setAge(18);
        kieSession.getAgenda().getAgendaGroup("test1").setFocus();
        kieSession.insert(fact);
        System.out.println(fact.getSucc());
//        kieSession.fireAllRules();
//        new Thread(()->{
//            kieSession.fireUntilHalt();
//        }).start();
        QueryResults query1 = kieSession.getQueryResults("query1");
        Iterator<QueryResultsRow> iterator = query1.iterator();
        for (QueryResultsRow row:query1) {
            Fact $s = (Fact)row.get("$s");
        }
        kieSession.setGlobal("count",10);
        kieSession.halt();
        System.out.println(fact.getSucc());
        kieSession.dispose();
    }
}