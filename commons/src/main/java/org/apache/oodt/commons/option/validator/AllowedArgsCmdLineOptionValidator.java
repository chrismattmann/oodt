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


package org.apache.oodt.commons.option.validator;

//JDK imports
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

//OODT imports
import org.apache.oodt.commons.option.CmdLineOptionInstance;

//Spring imports
import org.springframework.beans.factory.annotation.Required;

/**
 * 
 * @author bfoster
 * @version $Revision$
 *
 * <p>Describe your class here</p>.
 */
public class AllowedArgsCmdLineOptionValidator implements
        CmdLineOptionValidator {

    private List<String> allowedArgs;

    public AllowedArgsCmdLineOptionValidator() {
        this.allowedArgs = new LinkedList<String>();
    }

    public boolean validate(CmdLineOptionInstance optionInst) {
        for (String value : optionInst.getValues()) {
            if (!allowedArgs.contains(value)) {
                LOG.log(Level.SEVERE, "Option value " + value
                        + " is not allowed for option "
                        + optionInst.getOption().getId()
                        + " - Allowed values = " + this.getAllowedArgs());
                return false;
            }
        }
        return true;
    }

    public List<String> getAllowedArgs() {
        return allowedArgs;
    }

    @Required
    public void setAllowedArgs(List<String> allowedArgs) {
        this.allowedArgs = allowedArgs;
    }

}
