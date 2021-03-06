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
import java.util.logging.Level;

//OODT imports
import org.apache.oodt.commons.option.CmdLineOptionInstance;

/**
 * 
 * @author bfoster
 * @version $Revision$
 *
 * <p>Describe your class here</p>.
 */
public class ClassExistsCmdLineOptionValidator implements
        CmdLineOptionValidator {

    public boolean validate(CmdLineOptionInstance optionInst) {
        for (String value : optionInst.getValues()) {
            try {
                Class.forName(value);
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Option value " + value + " for option "
                        + optionInst.getOption().getId()
                        + " is not a valid class");
                return false;
            }
        }
        return true;
    }

}
