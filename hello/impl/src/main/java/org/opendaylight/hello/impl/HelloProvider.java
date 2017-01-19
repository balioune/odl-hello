/*
 * Copyright © 2015 Alioune, BA. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.hello.impl;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.DataChangeListener;
//import org.opendaylight.controller.md.sal.binding.api.NotificationService;
import org.opendaylight.controller.sal.binding.api.NotificationService;
import org.opendaylight.controller.md.sal.common.api.data.AsyncDataBroker.DataChangeScope;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.RpcRegistration;
import org.opendaylight.controller.sal.binding.api.BindingAwareProvider;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.GreetingRegistry;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.HelloService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.greeting.registry.GreetingRegistryEntry;
import org.opendaylight.yangtools.concepts.ListenerRegistration;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloProvider implements BindingAwareProvider, AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(HelloProvider.class);
    private RpcRegistration<HelloService> helloService;
    private ListenerRegistration<DataChangeListener> listener;
    private InstanceIdentifier<GreetingRegistryEntry> iid =
            InstanceIdentifier.create(GreetingRegistry.class)
                .child(GreetingRegistryEntry.class);
    @Override
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

    @Override
    public void close() throws Exception {
        LOG.info("HelloProvider Closed");
    }

}
