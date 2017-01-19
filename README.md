### Create hello RPC the will return 'hello + $username'

$username is the input given by internal ODL Apps (features) or users that invoque the RPC

### Model a simple HelloWorld RPC

        module hello {
            yang-version 1;
            namespace "urn:opendaylight:params:xml:ns:yang:hello";
            prefix "hello";
        
            revision "2015-01-05" {
                description "Initial revision of hello model";
            }
            
            rpc hello-world {
                input {
                  leaf name {
                    type string;
                  }
                }
                
                output {
                  leaf greeting {
                    type string;
                  }
                }
            }
        }


### Create new class HelloWordImpl

        vim impl/src/main/java/org/opendaylight/hello/impl/HelloWordImpl.java

 
   


        /*
        * Copyright © 2015 Alioune, BA. and others.  All rights reserved.
        *
        * This program and the accompanying materials are made available under the
        * terms of the Eclipse Public License v1.0 which accompanies this distribution,
        * and is available at http://www.eclipse.org/legal/epl-v10.html
        */
        package org.opendaylight.hello.impl;

        
        import java.util.concurrent.Future;

        import org.opendaylight.controller.md.sal.binding.api.DataBroker;
        import org.opendaylight.controller.md.sal.binding.api.ReadOnlyTransaction;
        import org.opendaylight.controller.md.sal.binding.api.WriteTransaction;
        import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
        import org.opendaylight.controller.md.sal.common.api.data.ReadFailedException;
        import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
        import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.GreetingRegistry;
        import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.GreetingRegistryBuilder;
        import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.HelloService;
        import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.HelloWorldInput;
        import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.HelloWorldOutput;
        import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.HelloWorldOutputBuilder;
        import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.greeting.registry.GreetingRegistryEntry;
        import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.greeting.registry.GreetingRegistryEntryBuilder;
        import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.greeting.registry.GreetingRegistryEntryKey;
        import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
        import org.opendaylight.yangtools.yang.common.RpcResult;
        import org.opendaylight.yangtools.yang.common.RpcResultBuilder;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;

        import com.google.common.base.Optional;
        import com.google.common.util.concurrent.CheckedFuture;
        import com.google.common.util.concurrent.Futures;
        
        
        public class HelloWorldImpl implements HelloService{

        private static final Logger LOG = LoggerFactory.getLogger(HelloWorldImpl.class);
        private DataBroker db;
        
        public HelloWorldImpl(DataBroker db) {
           this.db = db;
           initializeDataTree(this.db);
        }
        
        @Override
        public Future<RpcResult<HelloWorldOutput>> helloWorld(HelloWorldInput input) {
        
           HelloWorldOutput output = new HelloWorldOutputBuilder()
                .setGreeting("Hello " + input.getName())
                .build();
           writeToGreetingRegistry(input,output);
           return RpcResultBuilder.success(output).buildFuture();
        }
        
        }

