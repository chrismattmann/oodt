
DROP TABLE workflows IF EXISTS;
DROP TABLE event_workflow_map IF EXISTS;
DROP TABLE workflow_tasks IF EXISTS;
DROP TABLE workflow_task_map IF EXISTS;
DROP TABLE workflow_instances IF EXISTS;
DROP TABLE workflow_instance_metadata IF EXISTS;
DROP TABLE workflow_conditions IF EXISTS;
DROP TABLE workflow_condition_map IF EXISTS;
DROP TABLE workflow_task_configuration IF EXISTS;
DROP TABLE workflow_condition_configuration IF EXISTS;

CREATE TABLE workflows 
(workflow_id int NOT NULL PRIMARY KEY, 
workflow_name varchar(255));

CREATE TABLE event_workflow_map
(workflow_id int NOT NULL,
event_name varchar(255) NOT NULL);

CREATE TABLE workflow_tasks
(workflow_task_id int NOT NULL PRIMARY KEY,
workflow_task_name varchar(255),
workflow_task_class varchar(255));

CREATE TABLE workflow_task_map
(workflow_id int NOT NULL,
workflow_task_id int NOT NULL,
task_order int NOT NULL);

CREATE TABLE workflow_instances
(workflow_instance_id int NOT NULL PRIMARY KEY,
workflow_instance_status varchar(255),
workflow_id int NOT NULL,
current_task_id int NOT NULL,
start_date_time varchar(255),
end_date_time varchar(255),
current_task_start_date_time varchar(255),
current_task_end_date_time varchar(255));

CREATE TABLE workflow_instance_metadata
(workflow_instance_id int NOT NULL,
workflow_met_key varchar(1000) NOT NULL,
workflow_met_val varchar(1000) NOT NULL);

CREATE TABLE workflow_conditions
(workflow_condition_id int NOT NULL PRIMARY KEY,
workflow_condition_name varchar(255) NOT NULL,
workflow_condition_class varchar(255) NOT NULL);

CREATE TABLE workflow_condition_map
(workflow_task_id int NOT NULL, 
workflow_condition_id int NOT NULL,
condition_order int NOT NULL);

CREATE TABLE workflow_task_configuration
(workflow_task_id int NOT NULL,
property_name varchar(1000) NOT NULL,
property_value varchar(1000) NOT NULL);

CREATE TABLE workflow_condition_configuration
(workflow_condition_id int NOT NULL,
property_name varchar(1000) NOT NULL,
property_value varchar(1000) NOT NULL);

INSERT INTO workflow_conditions VALUES ('1', 'CheckCond', 'org.apache.oodt.cas.workflow.examples.CheckForMetadataKeys');
INSERT INTO workflow_condition_configuration VALUES ('1', 'reqMetKeys', 'Met1,Met2,Met3');
INSERT INTO workflow_conditions VALUES ('2', 'FalseCond', 'org.apache.oodt.cas.workflow.examples.FalseCondition');
INSERT INTO workflow_conditions VALUES ('3', 'TrueCond', 'org.apache.oodt.cas.workflow.examples.TrueCondition');
