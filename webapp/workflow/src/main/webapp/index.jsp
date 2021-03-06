<!--
Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements.  See the NOTICE.txt file distributed with this work for
additional information regarding copyright ownership.  The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License.  You may obtain a copy of
the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
License for the specific language governing permissions and limitations under
the License.
-->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<jsp:include page="inc/header.jsp"/>

  <h3>Welcome to the <%= application.getInitParameter("org.apache.oodt.cas.workflow.webapp.display.name") %>!</h3>
  
  
  <p>You can:
    <ul>
      <li><a href="./viewWorkflowInstances.jsp">View active Workflows</a></li>
      <li><a href="./viewWorkflows.jsp">View what Workflow Descriptions are available.</a></li>
      <li><a href="./viewEventToWorkflowMap.jsp">View what Workflows are associated with different Events</a></li>
    </ul>
   
<jsp:include page="inc/footer.jsp">
	<jsp:param name="showNavigation" value="false"/>
</jsp:include>
