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


package org.apache.oodt.cas.resource.util;

//JDK imports
import java.util.logging.Level;
import java.util.logging.Logger;

//OODT imports
import org.apache.oodt.cas.resource.batchmgr.Batchmgr;
import org.apache.oodt.cas.resource.batchmgr.BatchmgrFactory;
import org.apache.oodt.cas.resource.jobqueue.JobQueue;
import org.apache.oodt.cas.resource.jobqueue.JobQueueFactory;
import org.apache.oodt.cas.resource.jobrepo.JobRepository;
import org.apache.oodt.cas.resource.jobrepo.JobRepositoryFactory;
import org.apache.oodt.cas.resource.monitor.Monitor;
import org.apache.oodt.cas.resource.monitor.MonitorFactory;
import org.apache.oodt.cas.resource.scheduler.Scheduler;
import org.apache.oodt.cas.resource.scheduler.SchedulerFactory;
import org.apache.oodt.cas.resource.structs.JobInput;
import org.apache.oodt.cas.resource.structs.JobInstance;

/**
 * @author mattmann
 * @version $Revision$
 * 
 * <p>
 * Generic object creation facilities for the Resource Manager.
 * </p>
 */
public final class GenericResourceManagerObjectFactory {

  /* our log stream */
  private static final Logger LOG = Logger
      .getLogger(GenericResourceManagerObjectFactory.class.getName());

  private GenericResourceManagerObjectFactory() throws InstantiationException {
    throw new InstantiationException("Don't construct factory classes!");
  }

  /**
   * Constructs a new {@link JobInput} implementation from the given
   * <code>className</code>.
   * 
   * @param className
   *          The implementation class for the {@link JobInput}
   * @return A new {@link JobInput} implementation.
   */
  public static JobInput getJobInputFromClassName(String className) {
    try {
      Class inputClass = Class.forName(className);
      return (JobInput) inputClass.newInstance();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      LOG.log(Level.WARNING,
          "ClassNotFoundException when loading job input class " + className
              + " Message: " + e.getMessage());
    } catch (InstantiationException e) {
      e.printStackTrace();
      LOG.log(Level.WARNING,
          "InstantiationException when loading job input class " + className
              + " Message: " + e.getMessage());
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      LOG.log(Level.WARNING,
          "IllegalAccessException when loading job input class " + className
              + " Message: " + e.getMessage());
    }

    return null;
  }

  /**
   * Constructs a new {@link JobInstance} implementation from the given
   * <code>className</code>.
   * 
   * @param className
   *          The name of the implementation class for the {@link JobInstance}
   *          to construct.
   * @return A new {@link JobInstance} implementation.
   */
  public static JobInstance getJobInstanceFromClassName(String className) {
    try {
      Class instClass = Class.forName(className);
      return (JobInstance) instClass.newInstance();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      LOG.log(Level.WARNING,
          "ClassNotFoundException when loading job instance class " + className
              + " Message: " + e.getMessage());
    } catch (InstantiationException e) {
      e.printStackTrace();
      LOG.log(Level.WARNING,
          "InstantiationException when loading job instance class " + className
              + " Message: " + e.getMessage());
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      LOG.log(Level.WARNING,
          "IllegalAccessException when loading job instance class " + className
              + " Message: " + e.getMessage());
    }

    return null;
  }

  /**
   * Creates a new {@link JobQueue} implementation from the given
   * {@link JobQueueFactory} class name.
   * 
   * @param serviceFactory
   *          The class name of the {@link JobQueueFactory} to use to create new
   *          {@link JobQueue}s.
   * @return A new implementation of a {@link JobQueue}.
   */
  public static JobQueue getJobQueueServiceFromFactory(String serviceFactory) {
    Class clazz = null;
    JobQueueFactory factory = null;

    try {
      clazz = Class.forName(serviceFactory);
      factory = (JobQueueFactory) clazz.newInstance();
      return factory.createQueue();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      LOG.log(Level.WARNING,
          "ClassNotFoundException when loading jobqueue factory class "
              + serviceFactory + " Message: " + e.getMessage());
    } catch (InstantiationException e) {
      e.printStackTrace();
      LOG.log(Level.WARNING,
          "InstantiationException when loading jobqueue factory class "
              + serviceFactory + " Message: " + e.getMessage());
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      LOG.log(Level.WARNING,
          "IllegalAccessException when loading jobqueue factory class "
              + serviceFactory + " Message: " + e.getMessage());
    }

    return null;
  }
  
  /**
   * Creates a new Batchmgr implementation from a given String name of the
   * corresponding {@link BatchmgrFactory}.
   * 
   * @param serviceFactory
   *          The name of the {@link BatchmgrFactory} class to use to create
   *          {@link Batchmgr}s.
   * @return A new {@link Batchmgr} implementation.
   */
  public static Batchmgr getBatchmgrServiceFromFactory(String serviceFactory) {
    Class clazz = null;
    BatchmgrFactory factory = null;

    try {
      clazz = Class.forName(serviceFactory);
      factory = (BatchmgrFactory) clazz.newInstance();
      return factory.createBatchmgr();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      LOG.log(Level.WARNING,
          "ClassNotFoundException when loading batchmgr factory class "
              + serviceFactory + " Message: " + e.getMessage());
    } catch (InstantiationException e) {
      e.printStackTrace();
      LOG.log(Level.WARNING,
          "InstantiationException when loading batchmgr factory class "
              + serviceFactory + " Message: " + e.getMessage());
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      LOG.log(Level.WARNING,
          "IllegalAccessException when loading batchmgr factory class "
              + serviceFactory + " Message: " + e.getMessage());
    }

    return null;
  }

  /**
   * Creates a new {@link Monitor} implementation from the given String name of
   * the {@link MonitorFactory}.
   * 
   * @param serviceFactory
   *          The name of the {@link MonitorFactory} class to use to create
   *          {@link Monitor}s.
   * @return A new {@link Monitor} implementation.
   */
  public static Monitor getMonitorServiceFromFactory(String serviceFactory) {
    Class clazz = null;
    MonitorFactory factory = null;

    try {
      clazz = Class.forName(serviceFactory);
      factory = (MonitorFactory) clazz.newInstance();
      return factory.createMonitor();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      LOG.log(Level.WARNING,
          "ClassNotFoundException when loading monitor factory class "
              + serviceFactory + " Message: " + e.getMessage());
    } catch (InstantiationException e) {
      e.printStackTrace();
      LOG.log(Level.WARNING,
          "InstantiationException when loading monitor factory class "
              + serviceFactory + " Message: " + e.getMessage());
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      LOG.log(Level.WARNING,
          "IllegalAccessException when loading monitor factory class "
              + serviceFactory + " Message: " + e.getMessage());
    }

    return null;

  }

  /**
   * Creates a new {@link Scheduler} from the given String name of the
   * {@link SchedulerFactory}.
   * 
   * @param serviceFactory
   *          The class name of the {@link SchedulerFactory} to use to create
   *          the new {@link Scheduler}.
   * @return A new {@link Scheduler} implementation.
   */
  public static Scheduler getSchedulerServiceFromFactory(String serviceFactory) {
    Class clazz = null;
    SchedulerFactory factory = null;

    try {
      clazz = Class.forName(serviceFactory);
      factory = (SchedulerFactory) clazz.newInstance();
      return factory.createScheduler();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      LOG.log(Level.WARNING,
          "ClassNotFoundException when loading scheduler factory class "
              + serviceFactory + " Message: " + e.getMessage());
    } catch (InstantiationException e) {
      e.printStackTrace();
      LOG.log(Level.WARNING,
          "InstantiationException when loading scheduler factory class "
              + serviceFactory + " Message: " + e.getMessage());
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      LOG.log(Level.WARNING,
          "IllegalAccessException when loading scheduler factory class "
              + serviceFactory + " Message: " + e.getMessage());
    }

    return null;
  }
  
  /**
   * Creates a new {@link JobRepository} implementation from the given
   * name of the {@link JobRepositoryFactory}.
   * @param serviceFactory The class name of the {@link JobRepositoryFactory}
   * to use to create a {@link JobRepository}.
   * @return A new {@link JobRepository} from the given service factory.
   */
  public static JobRepository getJobRepositoryFromServiceFactory(String serviceFactory) {
    Class clazz = null;
    JobRepositoryFactory factory = null;

    try {
      clazz = Class.forName(serviceFactory);
      factory = (JobRepositoryFactory) clazz.newInstance();
      return factory.createRepository();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      LOG.log(Level.WARNING,
          "ClassNotFoundException when loading job repo factory class "
              + serviceFactory + " Message: " + e.getMessage());
    } catch (InstantiationException e) {
      e.printStackTrace();
      LOG.log(Level.WARNING,
          "InstantiationException when loading job rep factory class "
              + serviceFactory + " Message: " + e.getMessage());
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      LOG.log(Level.WARNING,
          "IllegalAccessException when loading job repo factory class "
              + serviceFactory + " Message: " + e.getMessage());
    }

    return null;
  }

}
