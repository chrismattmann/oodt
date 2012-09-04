/**
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
package org.apache.oodt.cas.workflow.engine.processor;

//OODT import
import org.apache.oodt.cas.workflow.lifecycle.WorkflowLifecycleManager;
import org.apache.oodt.cas.workflow.structs.Priority;
import org.apache.oodt.cas.workflow.structs.WorkflowInstance;

/**
 * 
 * WorkflowProcessor which handles Workflow Pre/Post Conditions.
 * 
 * @author bfoster
 * @author mattmann
 * @version $Revision$
 * 
 */
public class ConditionProcessor extends TaskProcessor {

  public ConditionProcessor(WorkflowLifecycleManager lifecycleManager) {
    super(lifecycleManager);
  }

  @Override
  public void setPreConditions(WorkflowProcessor preConditions) {
    // not allowed
  }

  @Override
  public void setPostConditions(WorkflowProcessor postConditions) {
    // not allowed
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.apache.oodt.cas.workflow.engine.processor.TaskProcessor#setWorkflowInstance
   * (org.apache.oodt.cas.workflow.structs.WorkflowInstance)
   */
  @Override
  public void setWorkflowInstance(WorkflowInstance instance) {
    instance.setPriority(Priority
        .getPriority(instance.getPriority().getValue() - 0.1));
    super.setWorkflowInstance(instance);
  }

}