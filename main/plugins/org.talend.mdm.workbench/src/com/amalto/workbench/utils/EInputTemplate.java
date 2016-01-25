// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package com.amalto.workbench.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum EInputTemplate {
    APPLICATION_ITEMPK(
                       EContentType.APPLICATION_ITEMPK.getName(),
                       "<item-pOJOPK>\n\t<data-cluster-pOJOPK><ids>?</ids></data-cluster-pOJOPK>\n\t<concept-name>?</concept-name>\n\t<ids>?</ids>\n</item-pOJOPK>"), //$NON-NLS-1$
    // batchproject,callJob,codec,crossreferencing,csvparser,dumpandgo,fixedlengthparser,groupedlinesreader,
    // linereader,partialupdate,project,regexp,replace,route,tisCall,workflowtrigger,xpath,xslt

    // transformer parameters(Process)
    BATCHPROJECT("batchproject",//$NON-NLS-1$
                 "<parameters>\n\t\t<dataClusterName>?</dataClusterName>\n\t\t<dataModelName>?</dataModelName>\n\t\t<conceptName>?</conceptName>\n</parameters>"), //$NON-NLS-1$
    CALLJOB("callJob",//$NON-NLS-1$
            "<configuration>\n\t\t<url>?</url>\n\t\t<contextParam>\n\t\t\t<name>?</name>\n\t\t\t<value>?</value>\n\t\t</contextParam>\n\t\t<contextParam>\n\t\t\t<name>?</name>\n\t\t\t<value>?</value>\n\t\t</contextParam>\n\t\t<contextParam>\n\t\t\t<name>?</name>\n\t\t\t<value>?</value>\n\t\t</contextParam>\n\t\t<username>?</username>\n\t\t<password>?</password>\n</configuration>\n\n\n"), //$NON-NLS-1$
    CODEC("codec", "<parameters>\n\t\t<method>?</method>\n\t\t<algorithm>?</algorithm>\n</parameters>"), //$NON-NLS-1$//$NON-NLS-2$
    CROSSREFERENCING("crossreferencing",//$NON-NLS-1$
                     "<parameters>\n\t\t<CrossRef>\n\t\t\t<xrefName>?</xrefName>\n\t\t\t<xrefCluster>?</xrefCluster>\n\t\t\t<xrefRootElement>?</xrefRootElement>\n\t\t\t<xrefIn>\n\t\t\t\t<mapping>\n\t\t\t\t\t<xrefElement>?</xrefElement>\n\t\t\t\t\t<xrefPath>?</xrefPath>\n\t\t\t\t</mapping>\n\t\t\t</xrefIn>\n\t\t\t<xrefOut>\n\t\t\t\t<mapping>\n\t\t\t\t\t<xrefElement>?</xrefElement>\n\t\t\t\t\t<xrefPath>?</xrefPath>\n\t\t\t\t</mapping>\n\t\t\t</xrefOut>\n\t\t</CrossRef>\n</parameters>"), //$NON-NLS-1$
    CSVPARSER("csvparser",//$NON-NLS-1$
              "<parameters>\n\t\t<separator>?</separator>\n\t\t<headersOnFirstLine>?</headersOnFirstLine>\n\t\t<template>\n\t\t<![CDATA[\n\t\t\t<MyXml>\n\t\t\t\t\t<Field1>?</Field1>\n\t\t\t\t\t<Combo>?</Combo>\n\t\t\t\t\t<NotInterpreted>?</NotInterpreted>\n\t\t\t\t\t<HeaderReference>?</HeaderReference>\n\t\t\t\t\t[LOOP lineNumber.*]\n\t\t\t\t\t<Line>\n\t\t\t\t\t\t\t<Quantity>?</Quantity> \n\t\t\t\t\t</Line>\n\t\t\t\t\t[/LOOP] \n\t\t\t</MyXml>\n\t\t]]>\n\t\t</template>\n</parameters>"), //$NON-NLS-1$
    DUMPANDGO("dumpandgo", ""), //$NON-NLS-1$//$NON-NLS-2$
    FIXEDLENGTHPARSER("fixedlengthparser",//$NON-NLS-1$
                      "<parameters>\n\t\t<template>\n\t\t\t<MyXml>\n\t\t\t\t\t<Field1>?</Field1> \n\t\t\t\t\t<Combo>?</Combo>\n\t\t\t\t\t<NotInterpreted>?</NotInterpreted>\n\t\t\t</MyXml>\n\t\t</template>\n</parameters>"), //$NON-NLS-1$
    GROUPEDLINESREADER("groupedlinesreader",//$NON-NLS-1$
                       "<parameters>\n                 <ignoreFirstLines>?</ignoreFirstLines>\n                 <type>?</type>\n                 <position>?</position>\n                 <separator>?</separator>\n\n</parameters>"), //$NON-NLS-1$
    LINEREADER("linereader", "<parameters>\n\t\t<ignoreFirstLines>?</ignoreFirstLines>\n</parameters>"), //$NON-NLS-1$//$NON-NLS-2$
    PARTIALUPDATE("partialupdate",//$NON-NLS-1$
                  "<parameters>\n\t\t<pivot>?</pivot>\n\t\t<overwrite>?</overwrite>\n\t\t<keyXPath>?</keyXPath>\n\t\t<startingPosition>?</startingPosition>\n\t\t<dataCluster>?</dataCluster>\n\t\t<dataModel>?</dataModel>\n</parameters>"), //$NON-NLS-1$
    PROJECT("project",//$NON-NLS-1$
            "<parameters>\n\t\t<defaultDataCluster>?</defaultDataCluster>\n\t\t<defaultDataModel>?</defaultDataModel>\n\t\t<overwrite>?</overwrite>\n</parameters>"), //$NON-NLS-1$
    REGEXP("regexp",//$NON-NLS-1$
           "<parameters>\n\t\t<regexp>?</regexp>\n\t\t<contentType>?</contentType>\n\t\t<resultPattern><![CDATA[\n\t\t\t\t<result>?</result>\n\t\t]]></resultPattern>\n</parameters>"), //$NON-NLS-1$
    REPLACE("replace",//$NON-NLS-1$
            "<parameters>\n\t\t<regexp>?</regexp>\n\t\t<contentType>?</contentType>\n\t\t<replacement>?</replacement>\n</parameters>"), //$NON-NLS-1$
    ROUTE("route", ""), //$NON-NLS-1$//$NON-NLS-2$
    TISCALL("callJob",//$NON-NLS-1$
            "<configuration>\n\t\t<url>?</url>\n\t\t<contextParam>\n\t\t\t<name>?</name>\n\t\t\t<value>?</value>\n\t\t</contextParam>\n\t\t<username>?</username>\n\t\t<password>?</password>\n</configuration>\n\n\n"), //$NON-NLS-1$
    WORKFLOWTRIGGER("workflowtrigger",//$NON-NLS-1$
                    "<parameters>\n\t\t<processId>?</processId>\n\t\t<processVersion>?</processVersion>\n\t\t<variable>\n\t\t\t<scope>?</scope>\n\t\t\t<name>?</name>\n\t\t\t<type>?</type>\n\t\t\t<fromItem>?</fromItem>\n\t\t\t<xpath>?</xpath>\n\t\t</variable>\n\t\t<variable>\n\t\t\t<scope>?</scope>\n\t\t\t<activityId>?</activityId>\n\t\t\t<name>?</name>\n\t\t\t<type>?</type>\n\t\t\t<fromItem>?</fromItem>\n\t\t\t<value>?</value>\n\t\t</variable>\n\t</parameters>\n\n"), //$NON-NLS-1$
    XPATH("xpath", "<parameters>\n\t\t<xPath>?</xPath>\n\t\t<contentType>?</contentType>\n</parameters>"), //$NON-NLS-1$//$NON-NLS-2$
    XSLT("xslt",//$NON-NLS-1$
         "<xsl:stylesheet xmlns:xsl='http://www.w3.org/1999/XSL/Transform' xmlns:mdm='java:com.amalto.core.plugin.base.xslt.MdmExtension' version='1.0'>  \n"//$NON-NLS-1$
                 + "    <xsl:output method='xml' indent='yes' omit-xml-declaration='yes'/>  \n"//$NON-NLS-1$
                 + "    <xsl:template match='/' priority='1'> \n\n" + "    </xsl:template> \n" + "</xsl:stylesheet>\n"), //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$

    // calltransformer, dumptoconsole, itemdispatcher, jdbc, logging, loggingsmtp, svn,smtp,workflow

    // RoutingRole(trigger)
    CALLTRANSFORMER("callprocess", "process=?"), //$NON-NLS-1$//$NON-NLS-2$
    DUMPTOCONSOLE("dumptoconsole", ""), //$NON-NLS-1$//$NON-NLS-2$
    ITEMDISPATCHER("itemdispatcher",//$NON-NLS-1$
                   "<parameters>\n\t<transformer>\n\t\t<allInOne>?</allInOne>\n\t\t<assignTo>?</assignTo>\n\t</transformer>\n</parameters>"), //$NON-NLS-1$
    JDBC("jdbc",//$NON-NLS-1$
         "<parameters>\n\t<driverClassName>?</driverClassName>\n\t<url>?</url>\n\t<username>?</username>\n\t<password>?</password>\n\t<transformer>?</transformer>\n</parameters>"), //$NON-NLS-1$
    LOGGING("logging", ""), //$NON-NLS-1$//$NON-NLS-2$
    LOGGINGSMTP("loggingsmtp",//$NON-NLS-1$
                "<parameters>\n\t<from>?</from>\n\t<to>?</to>\n\t<cc>?</cc>\n\t<bcc>?</bcc>\n\t<logFileName>?</logFileName>\n\t<subjectprefix>?</subjectprefix>\n\t<logfilename>?</logfilename>\n\t<transformer>?</transformer>\n</parameters>"), //$NON-NLS-1$
    SMTP("smtp",//$NON-NLS-1$
         "<parameters>\n\t<from>?</from>\n\t<to>?</to>\n\t<cc>?</cc>\n\t<bcc>?</bcc>\n\t<subjectprefix>?</subjectprefix>\n\t<logfilename>?</logfilename>\n\t<process>?</process>\n</parameters>\n"), //$NON-NLS-1$
    SVN("svn", ""), //$NON-NLS-1$//$NON-NLS-2$
    WORKFLOW("workflow",//$NON-NLS-1$
             "<parameters>\n\t\t<processId>?</processId>\n\t\t<processVersion>?</processVersion>\n\t\t<variable>\n\t\t\t<scope>?</scope>\n\t\t\t<name>?</name>\n\t\t\t<type>?</type>\n\t\t\t<fromItem>?</fromItem>\n\t\t\t<xpath>?</xpath>\n\t\t</variable>\n\t\t<variable>\n\t\t\t<scope>?</scope>\n\t\t\t<activityId>?</activityId>\n\t\t\t<name>?</name>\n\t\t\t<type>?</type>\n\t\t\t<fromItem>?</fromItem>\n\t\t\t<value>?</value>\n\t\t</variable>\n\t</parameters>\n\n"), //$NON-NLS-1$
    SYNCHRONIZATION("synchronization",//$NON-NLS-1$
                    "<synchronization-configuration>\n\t<planName>?</planName>\n\t<action>START_DIFFERENTIAL</action>\n</synchronization-configuration>"); //$NON-NLS-1$

    String name;

    String content;

    EInputTemplate(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static Set<String> allTemplates() {
        Set<String> list = new HashSet<String>();
        for (EInputTemplate type : values()) {
            list.add(type.name);
        }
        return list;
    }

    public static Map<String, EInputTemplate> getXtentisObjexts() {

        Map<String, EInputTemplate> map = new HashMap<String, EInputTemplate>();
        for (int i = 0; i < values().length; i++) {
            map.put(String.valueOf(values()[i].getName()), values()[i]);
        }
        return map;
    }
}
