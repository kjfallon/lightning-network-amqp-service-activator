Lightning Network AMQP Service Activator
========================================


This application (LNASA) is a proof of concept that demonstrates how a [messaging gateway](https://www.enterpriseintegrationpatterns.com/patterns/messaging/MessagingGateway.html) between the [Lightning Network](https://lightning.network) (LN) and an enterprise service bus (ESB), queueing system du jour, or a broadcast domain that feeds distributed microservices may be implemented.  The goal here is to provide a real-time integration that makes LN events available to internal business processes (transaction processing, fufillment, and audit for example).  When complete this application will support command messages from the ESB to control the LN node making this application a simple [service activator](https://www.enterpriseintegrationpatterns.com/patterns/messaging/MessagingAdapter.html).  A derivitive of this application could be deployed with each LN node in a cluster of nodes behind and external facing equalizer.

This application is written in Java using the [Spring Framework](https://spring.io/), specifically [Spring Boot](https://spring.io/projects/spring-boot).  This application uses Java classes from the [LightningJ](http://www.lightningj.org) project which provides access to the [Lightning Network Daemon's](https://github.com/LightningNetwork/lnd) [gRPC](https://grpc.io/) API.  

The [Lightning Network](https://lightning.network) is a high performance layer that is built on top of the [Bitcoin](https://github.com/bitcoin/bitcoin) blockchain.  The [Lightning Network](https://lightning.network) allows high volume and/or high speed transactions between individuals and organizations that are settled and enforced by smart contracts on the immutable public ledger of the global [Bitcoin](https://github.com/bitcoin/bitcoin) blockchain.  This allows for high performance streams of micropayments (or macropayments) between businesses and their customers, as well as within federations, consortiums, and partnerships.  

When executed this application establishes a persistent connection to the [Lightning Network Daemon (lnd)](https://github.com/LightningNetwork/lnd) node specified in this application's [encrypted.properties](src/main/resources/encrypted.properties) file and then starts broadcasting events to  [AMQP](https://en.wikipedia.org/wiki/Advanced_Message_Queuing_Protocol) exchanges.  Events are broadcast when the lnd node receives transactions, invoices, or the node's local topology changes.  Every five seconds a heartbeat message is published to an [AMQP](https://en.wikipedia.org/wiki/Advanced_Message_Queuing_Protocol) exchange that identifies the node and whether the node is responsive or not.  

This application is built with Apache Maven.  To compile and execute this application you need Maven and Java JDK 1.8+.  At runtime you must provide an environment variable LNASA_KEY.  If you use the support I provided for encrypted property file values then LNASA_KEY should contain the key, otherwise set it to a dummy value.  

To-do
-----
- [X] Create [Spring Boot](https://spring.io/projects/spring-boot) system service
- [X] Implement [LightningJ](http://www.lightningj.org)  to communicate with lnd
- [X] Subscribe to LN events (invoice, transaction, topology) from lnd node with LightningJ via [gRPC](https://grpc.io/)
- [X] Publish LN events to [AMQP](https://en.wikipedia.org/wiki/Advanced_Message_Queuing_Protocol) service bus
- [X] Poll lnd node for heartbeat
- [X] Publish heartbeat status to [AMQP](https://en.wikipedia.org/wiki/Advanced_Message_Queuing_Protocol) service bus
- [ ] Listen to [AMQP](https://en.wikipedia.org/wiki/Advanced_Message_Queuing_Protocol) event bus for command messages for the LN node

Log file excerpt of LNASA executing
---------------------------------------------------
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.0.3.RELEASE)

2018-07-18 19:17:05.529  INFO 4145 --- [           main] htningNetworkServiceActivatorApplication : Starting LightningNetworkServiceActivatorApplication
2018-07-18 19:17:05.533  INFO 4145 --- [           main] htningNetworkServiceActivatorApplication : No active profile set, falling back to default profiles: default
2018-07-18 19:17:05.581  INFO 4145 --- [           main] s.c.a.AnnotationConfigApplicationContext : Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@1d119efb: startup date [Thu Jul 18 19:17:05 EDT 2018]; root of context hierarchy
2018-07-18 19:17:06.222  INFO 4145 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'org.springframework.amqp.rabbit.annotation.RabbitBootstrapConfiguration' of type [org.springframework.amqp.rabbit.annotation.RabbitBootstrapConfiguration$$EnhancerBySpringCGLIB$$b109c5e5] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
2018-07-18 19:17:06.670  INFO 4145 --- [           main] c.e.lnsa.configuration.EncryptedProps    : Encrypted properties file loaded.
2018-07-18 19:17:06.688  INFO 4145 --- [           main] com.example.lnsa.configuration.LndApi    : Creating synchronous channel to Lightning Node API...
2018-07-18 19:17:12.403  INFO 4145 --- [           main] com.example.lnsa.configuration.LndApi    : Creating asynchronous channel to Lightning Node API...
2018-07-18 19:17:12.417  INFO 4145 --- [           main] c.e.lnsa.components.LndApiSubscriptions  : Subscribing to Lightning Network ChannelGraph topology updates.
2018-07-18 19:17:17.907  INFO 4145 --- [           main] c.e.lnsa.components.LndApiSubscriptions  : Subscribing to Lightning Network transactions.
2018-07-18 19:17:17.909  INFO 4145 --- [           main] c.e.lnsa.components.LndApiSubscriptions  : Subscribing to Lightning Network invoices.
2018-07-18 19:17:18.261  INFO 4145 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Registering beans for JMX exposure on startup
2018-07-18 19:17:18.268  INFO 4145 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Bean with name 'rabbitConnectionFactory' has been autodetected for JMX exposure
2018-07-18 19:17:18.271  INFO 4145 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Located managed bean 'rabbitConnectionFactory': registering with JMX server as MBean [org.springframework.amqp.rabbit.connection:name=rabbitConnectionFactory,type=CachingConnectionFactory]
2018-07-18 19:17:18.297  INFO 4145 --- [           main] o.s.c.support.DefaultLifecycleProcessor  : Starting beans in phase 2147483647
2018-07-18 19:17:18.304  INFO 4145 --- [           main] s.a.ScheduledAnnotationBeanPostProcessor : No TaskScheduler/ScheduledExecutorService bean found for scheduled processing
2018-07-18 19:17:18.316  INFO 4145 --- [           main] htningNetworkServiceActivatorApplication : Started LightningNetworkServiceActivatorApplication in 28.229 seconds (JVM running for 34.161)
2018-07-18 19:17:18.317  INFO 4145 --- [pool-9-thread-1] .s.a.AnnotationAsyncExecutionInterceptor : No task executor bean found for async processing: no bean of type TaskExecutor and no bean named 'taskExecutor' either
2018-07-18 19:17:18.536  INFO 4145 --- [cTaskExecutor-1] o.s.a.r.c.CachingConnectionFactory       : Attempting to connect to: localhost:5672
2018-07-18 19:17:18.734  INFO 4145 --- [           main] c.example.lnsa.components.StartUpTasks   : Startup Lnd Info: {"identity_pubkey":"037e011f6fcc2ba9b80f6e6ee93cf5d293cadeec162cb51a71cd839fd208d17a28","alias":"037e071c6fab2ba9b91f","num_pending_channels":0,"num_active_channels":0,"num_peers":0,"block_height":1354517,"block_hash":"00000000000000425732ae3eb310384b8eaee76d1ff8470badc60f974a5b9d4e","synced_to_chain":true,"testnet":true,"chains":[],"uris":[],"best_header_timestamp":1532026321,"version":"0.4.2-beta commit=93207608b5a433ddfc15cfe56bb060e1c720166e"}
2018-07-18 19:17:18.735  INFO 4145 --- [           main] c.example.lnsa.components.StartUpTasks   : Startup Lnd Wallet balance : {"total_balance":0,"confirmed_balance":0,"unconfirmed_balance":0}
2018-07-18 19:17:18.735  INFO 4145 --- [           main] c.example.lnsa.components.StartUpTasks   : Startup Lnd Peers: {"peers":[]}
2018-07-18 19:17:18.736  INFO 4145 --- [           main] c.example.lnsa.components.StartUpTasks   : Startup Lnd Channels: {"channels":[]}
2018-07-18 19:17:18.902  INFO 4145 --- [cTaskExecutor-1] o.s.a.r.c.CachingConnectionFactory       : Created new connection: SpringAMQP#2534eea2:0/SimpleConnection@5b98b590 [delegate=amqp://guest@127.0.0.1:5672/, localPort= 56883]
2018-07-18 19:17:18.973  INFO 4145 --- [cTaskExecutor-1] c.e.lnsa.services.AmqpEventPublisher     : AMQP Published to 'LN_HEARTBEAT_EXCHANGE' message ''LND_OK 037e011f6fcc2ba9b80f''
2018-07-18 19:17:23.360  INFO 4145 --- [cTaskExecutor-2] c.e.lnsa.services.AmqpEventPublisher     : AMQP Published to 'LN_HEARTBEAT_EXCHANGE' message ''LND_OK 037e011f6fcc2ba9b80f''
2018-07-18 19:17:28.362  INFO 4145 --- [cTaskExecutor-3] c.e.lnsa.services.AmqpEventPublisher     : AMQP Published to 'LN_HEARTBEAT_EXCHANGE' message ''LND_OK 037e011f6fcc2ba9b80f''
```
References
-------------
Lightning Network (https://lightning.network)  
Wikipedia: Lightning Network (https://en.wikipedia.org/wiki/Lightning_Network)  
Lightning Network Daemon (https://github.com/LightningNetwork/lnd)  
LightningJ (http://www.lightningj.org)  
Bitcoin (https://github.com/bitcoin/bitcoin)  
Wikipedia: Bitcoin (https://en.wikipedia.org/wiki/Bitcoin)  
Spring Boot (https://spring.io/projects/spring-boot)  

