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


## Compile the yang model 
        cd hello/api

        mvn clean install -DskipTests
### Create new class HelloWordImpl

        vim impl/src/main/java/org/opendaylight/hello/impl/HelloWordImpl.java

Here is the content of the new Class


        /*
         * Copyright © 2015 Alioune, BA. and others.  All rights reserved.
         *
         * This program and the accompanying materials are made available under the
         * terms of the Eclipse Public License v1.0 which accompanies this distribution,
         * and is available at http://www.eclipse.org/legal/epl-v10.html
         */
        package org.opendaylight.hello.impl;
        
        import java.util.concurrent.Future;
        
        import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.HelloService;
        import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.HelloWorldInput;
        import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.HelloWorldOutput;
        import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.HelloWorldOutputBuilder;
        import org.opendaylight.yangtools.yang.common.RpcResult;
        import org.opendaylight.yangtools.yang.common.RpcResultBuilder;
        
        public class HelloWorldImpl implements HelloService{
        
                @Override
                public Future<RpcResult<HelloWorldOutput>> helloWorld(HelloWorldInput input) {
		HelloWorldOutput output = new HelloWorldOutputBuilder()
                .setGreeting("Hello " + input.getName())
                .build();
		return RpcResultBuilder.success(output).buildFuture();
                }
        
        }

Modify the class HelloProvider in hello/impl/src/main/java/org/opendaylight/hello/impl/

Add the attributes: private RpcRegistration<HelloService> helloService

Instancite in helloService = session.addRpcImplementation(HelloService.class, new HelloWorldImpl());

Content of HelloProvider after theses modifications

        /*
         * Copyright © 2015 Alioune, BA. and others.  All rights reserved.
         *
         * This program and the accompanying materials are made available under the
         * terms of the Eclipse Public License v1.0 which accompanies this distribution,
         * and is available at http://www.eclipse.org/legal/epl-v10.html
         */
        package org.opendaylight.hello.impl;
        
        import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
        import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.RpcRegistration;
        import org.opendaylight.controller.sal.binding.api.BindingAwareProvider;
        import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.hello.rev150105.HelloService;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        
        public class HelloProvider implements BindingAwareProvider, AutoCloseable {
        
            private static final Logger LOG = LoggerFactory.getLogger(HelloProvider.class);
            private RpcRegistration<HelloService> helloService;
            @Override
            public void onSessionInitiated(ProviderContext session) {
                helloService = session.addRpcImplementation(HelloService.class, new HelloWorldImpl());
                LOG.info("HelloProvider Session Initiated");
            }
        
            @Override
            public void close() throws Exception {
                LOG.info("HelloProvider Closed");
            }
        
        }

