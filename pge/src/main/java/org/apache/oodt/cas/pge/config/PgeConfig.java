/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.apache.oodt.cas.pge.config;

//JDK imports
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author bfoster
 * @version $Revision$
 * 
 * <p>
 * Configuration file for CAS-PGE
 * </p>.
 */
public class PgeConfig {

    private List<DynamicConfigFile> dynamicConfigFiles;

    private List<OutputDir> outputDirs;

    private Object[] propertyAdderCustomArgs;

    private String exeDir;

    private String shellType;

    private List<String> exeCmds;

    public PgeConfig() {
        this.shellType = "sh";
        this.outputDirs = new LinkedList<OutputDir>();
        this.dynamicConfigFiles = new LinkedList<DynamicConfigFile>();
        this.exeCmds = new LinkedList<String>();
    }

    public void addDynamicConfigFile(DynamicConfigFile dynamicConfigFile) {
        dynamicConfigFiles.add(dynamicConfigFile);
    }

    public List<DynamicConfigFile> getDynamicConfigFiles() {
        return this.dynamicConfigFiles;
    }

    public void addOuputDirAndExpressions(OutputDir outputDir) {
        this.outputDirs.add(outputDir);
    }

    public List<OutputDir> getOuputDirs() {
        return this.outputDirs;
    }

    public void setExeDir(String exeDir) {
        this.exeDir = exeDir;
    }

    public String getExeDir() {
        return this.exeDir;
    }

    public void setShellType(String shellType) {
        if (shellType != null && !shellType.equals(""))
            this.shellType = shellType;
    }

    public String getShellType() {
        return this.shellType;
    }

    public void setExeCmds(List<String> exeCmds) {
        this.exeCmds = exeCmds;
    }

    public List<String> getExeCmds() {
        return this.exeCmds;
    }

    public void setPropertyAdderCustomArgs(Object[] args) {
        propertyAdderCustomArgs = args;
    }

    public Object[] getPropertyAdderCustomArgs() {
        return propertyAdderCustomArgs != null ? propertyAdderCustomArgs
                : new Object[0];
    }

}
