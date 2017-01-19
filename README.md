## Create Litener

In this part, we will create a data change listener that listens on GreetingRegistry tree and write a log in karaf

## create the class GreetingRegistryDataChangeListenerFuture in mpl/src/main/java/org/opendaylight/hello/


        /*
         * Copyright © 2015 Alioune, BA. and others.  All rights reserved.
         *
         * This program and the accompanying materials are made available under the
         * terms of the Eclipse Public License v1.0 which accompanies this distribution,
         * and is available at http://www.eclipse.org/legal/epl-v10.html
         */
        
        package org.opendaylight.hello.impl;
        
        import com.google.common.util.concurrent.AbstractFuture;
        
        import org.opendaylight.controller.md.sal.binding.api.DataBroker;
        import org.opendaylight.controller.md.sal.binding.api.DataChangeListener;
        import org.opendaylight.controller.md.sal.common.api.data.AsyncDataBroker.DataChangeScope;
        import org.opendaylight.controller.md.sal.common.api.data.AsyncDataChangeEvent;
        import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
        import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.GreetingRegistry;
        import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.greeting.registry.GreetingRegistryEntry;
        import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.greeting.registry.GreetingRegistryEntryKey;
        import org.opendaylight.yangtools.concepts.ListenerRegistration;
        import org.opendaylight.yangtools.yang.binding.DataObject;
        import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        
        public class GreetingRegistryDataChangeListenerFuture extends AbstractFuture<GreetingRegistryEntry> implements DataChangeListener, AutoCloseable{
          
        private static final Logger LOG = LoggerFactory.getLogger(GreetingRegistryDataChangeListenerFuture.class);
        private String name;
        private ListenerRegistration<DataChangeListener> registration;
            
        	
        public GreetingRegistryDataChangeListenerFuture(DataBroker db,String name) {
                super();
                this.name = name;
                InstanceIdentifier<GreetingRegistryEntry> iid =
                        InstanceIdentifier.create(GreetingRegistry.class)
                            .child(GreetingRegistryEntry.class);
                this.registration = db.registerDataChangeListener(LogicalDatastoreType.OPERATIONAL,
                        iid, this, DataChangeScope.BASE);
        }
        
        @Override
        public void close() throws Exception {
            if(registration != null) {
              registration.close();
            }
        }
        
        @Override
        public void onDataChanged(AsyncDataChangeEvent<InstanceIdentifier<?>, DataObject> event) {

		InstanceIdentifier<GreetingRegistryEntry> iid =
                InstanceIdentifier.create(GreetingRegistry.class)
                .child(GreetingRegistryEntry.class,new GreetingRegistryEntryKey(this.name));

        if(event.getCreatedData().containsKey(iid) ) {
            if(event.getCreatedData().get(iid) instanceof GreetingRegistryEntry) {
                this.set((GreetingRegistryEntry) event.getCreatedData().get(iid));
                LOG.info("GreetingRegistry tree has been changed");
                LOG.info("New entry {} ", event.toString());
            }
            quietClose();
        } else if (event.getUpdatedData().containsKey(iid)) {
            if(event.getUpdatedData().get(iid) instanceof GreetingRegistryEntry) {
                this.set((GreetingRegistryEntry) event.getUpdatedData().get(iid));
                LOG.info("GreetingRegistry tree has been changed");
                LOG.info("New entry {} ", event.toString());
            }
            quietClose();
        }
		
        }
        
        private void quietClose() {
            try {
                this.close();
            } catch (Exception e) {
                throw new IllegalStateException("Unable to close registration",e);
            }
        }
        }
 

Modify HelloProvider
- Add new attributes

        private ListenerRegistration<DataChangeListener> listener;
        private InstanceIdentifier<GreetingRegistryEntry> iid =
        InstanceIdentifier.create(GreetingRegistry.class)
                .child(GreetingRegistryEntry.class);
        public void onSessionInitiated(ProviderContext session) {
            	
             /** Get DataBroker and Notification services**/
             DataBroker db = session.getSALService(DataBroker.class);
             NotificationService notificationService = session.getSALService(NotificationService.class);
        
             helloService = session.addRpcImplementation(HelloService.class, new HelloWorldImpl(db));
           
             /** we should set a name for testing **/
             listener = db.registerDataChangeListener(LogicalDatastoreType.OPERATIONAL, 
                    iid, new GreetingRegistryDataChangeListenerFuture(db, "alioune"), DataChangeScope.SUBTREE);
             LOG.info("HelloProvider Session Initiated");
        }

