## Model a simple HelloWorld GreetingRegistry

In this part, we are going to create a container for storing a list of greeting.
By doing that, all users that call the rpc and the greeting they receive are stored in the date tree 

## Add ib api/src/main/yang/hello.yang

        container greeting-registry {
            list greeting-registry-entry {
                key "name";
                leaf name {
                    type string;
                }
                leaf greeting {
                    type string;
                }
            }
        }

## Rebuild the project from hello/api

         mvn clean install -DskipTests


## Make DataBroker available to HelloWorldImp.java

## Create the attribute db

        private DataBroker db;
    
## Create a constructor

        public HelloWorldImpl(DataBroker db) {
            this.db = db;
            initializeDataTree(this.db);
        }
    
## Create these methods in HelloWordImpl

        private void initializeDataTree(DataBroker db)
        private void writeToGreetingRegistry(HelloWorldInput input, HelloWorldOutput output)
        private InstanceIdentifier<GreetingRegistryEntry> toInstanceIdentifier(HelloWorldInput input)

## Modify the method helloWorld

        public Future<RpcResult<HelloWorldOutput>> helloWorld(HelloWorldInput input) {
            HelloWorldOutput output = new HelloWorldOutputBuilder()
                    .setGreeting("Hello " + input.getName())
                   .build();
            writeToGreetingRegistry(input,output);
            return RpcResultBuilder.success(output).buildFuture();
        }
        

## Alter the HelloProvider.java to pass the DataBroker to the HelloWorldImpl(...) contstructor: 

        DataBroker db = session.getSALService(DataBroker.class);
        helloService = session.addRpcImplementation(HelloService.class, new HelloWorldImpl(db));
        
