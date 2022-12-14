# hazelcast-classcast
A demo app which reproduces Class cast exceptions in Hazelcast when using Kafka Avro Deserializer using Schema registry. 
The main class is inside the package `hazelcast.classcast` with the name `App`. If using any IDE like intelliJ, make sure to run `gradle build` so that Avro classes are generated.
The main class for producer is inside `producer.kafka` with the name `Producer`.

More details in the Hazelcast community's slack thread: https://hazelcastcommunity.slack.com/archives/C015Q2TUBKL/p1664777280871099 

## Steps to run
- `cd` to the root of the repo.
- Run `docker-compose up` and wait for everything to come up.
- Run `./gradlew clean build`
- Start the data production by running: `java -cp ./app/build/libs/app-all.jar producer.kafka.Producer`
- In another terminal run (from the root of the repo):  `java -cp ./app/build/libs/app-all.jar hazelcast.classcast.App`
  - This will submit a job to the cluster. You can view the logs inside `docker-compose` logs. It will work fine as this is the first submission.
- The error doesn't happen when you submit the pipeline for the first time but happens upon subsequent submissions.
  - Simply re-run `java -cp ./app/build/libs/app-all.jar hazelcast.classcast.App`
    - It will cancel and resubmit the job.
    
When we re-run the command, you will be able to see Class cast exceptions. The logs can be seen as a part of the docker-compose log output.
A sample exception looks like this: 
```
com.hazelcast.jet.JetException: Exception in ProcessorTasklet{custom_job1.0/fused(filter, map, flat-map, filter-2, map-2, map-3, map-4)#9}: java.lang.ClassCastException: class com.abcd.avro.A cannot be cast to class com.abcd.avro.A (com.abcd.avro.A is in unnamed module of loader com.hazelcast.jet.impl.deployment.JetClassLoader @128e6c99; com.abcd.avro.A is in unnamed module of loader com.hazelcast.jet.impl.deployment.JetClassLoader @4167af2d)
	at com.hazelcast.jet.impl.execution.TaskletExecutionService$CooperativeWorker.runTasklet(TaskletExecutionService.java:400) ~[hazelcast-5.0.2.jar:5.0.2]
	at java.util.concurrent.CopyOnWriteArrayList.forEach(CopyOnWriteArrayList.java:807) ~[?:?]
	at com.hazelcast.jet.impl.execution.TaskletExecutionService$CooperativeWorker.run(TaskletExecutionService.java:356) ~[hazelcast-5.0.2.jar:5.0.2]
	at java.lang.Thread.run(Thread.java:829) ~[?:?]
java.lang.ClassCastException: class com.abcd.avro.A cannot be cast to class com.abcd.avro.A (com.abcd.avro.A is in unnamed module of loader com.hazelcast.jet.impl.deployment.JetClassLoader @128e6c99; com.abcd.avro.A is in unnamed module of loader com.hazelcast.jet.impl.deployment.JetClassLoader @4167af2d)
	at at com.abcd.efgh.CLASS1.method1(CLASS1.java:39) ~[?:?]
	at com.abcd.efgh.CLASS2.lambda$method2$58ce31d8$1(CLASS2.java:131) ~[?:?]
	at com.hazelcast.function.FunctionEx.apply(FunctionEx.java:48) ~[hazelcast-5.0.2.jar:5.0.2]
	at com.hazelcast.jet.impl.pipeline.Planner.lambda$mergeMapFunctions$7af1335b$1(Planner.java:234) ~[hazelcast-5.0.2.jar:5.0.2]
	at com.hazelcast.function.FunctionEx.apply(FunctionEx.java:48) ~[hazelcast-5.0.2.jar:5.0.2]
	at com.hazelcast.jet.impl.pipeline.Planner.lambda$fuseFlatMapTransforms$8790af0c$1(Planner.java:191) ~[hazelcast-5.0.2.jar:5.0.2]
	at com.hazelcast.function.FunctionEx.apply(FunctionEx.java:48) ~[hazelcast-5.0.2.jar:5.0.2]
	at com.hazelcast.function.FunctionEx.lambda$andThen$581ce147$1(FunctionEx.java:82) ~[hazelcast-5.0.2.jar:5.0.2]
	at com.hazelcast.function.FunctionEx.apply(FunctionEx.java:48) ~[hazelcast-5.0.2.jar:5.0.2]
	at com.hazelcast.jet.core.AbstractProcessor$FlatMapper.tryProcess(AbstractProcessor.java:566) ~[hazelcast-5.0.2.jar:5.0.2]
	at com.hazelcast.jet.impl.processor.TransformP.tryProcess(TransformP.java:45) ~[hazelcast-5.0.2.jar:5.0.2]
	at com.hazelcast.jet.core.AbstractProcessor.tryProcess0(AbstractProcessor.java:187) ~[hazelcast-5.0.2.jar:5.0.2]
	at com.hazelcast.jet.core.AbstractProcessor.process0(AbstractProcessor.java:590) ~[hazelcast-5.0.2.jar:5.0.2]
	at com.hazelcast.jet.core.AbstractProcessor.process(AbstractProcessor.java:108) ~[hazelcast-5.0.2.jar:5.0.2]
	at com.hazelcast.jet.impl.execution.ProcessorTasklet.lambda$processInbox$2f647568$2(ProcessorTasklet.java:440) ~[hazelcast-5.0.2.jar:5.0.2]
	at com.hazelcast.jet.function.RunnableEx.run(RunnableEx.java:31) ~[hazelcast-5.0.2.jar:5.0.2]
	at com.hazelcast.jet.impl.util.Util.doWithClassLoader(Util.java:516) ~[hazelcast-5.0.2.jar:5.0.2]
	at com.hazelcast.jet.impl.execution.ProcessorTasklet.processInbox(ProcessorTasklet.java:440) ~[hazelcast-5.0.2.jar:5.0.2]
	at com.hazelcast.jet.impl.execution.ProcessorTasklet.stateMachineStep(ProcessorTasklet.java:305) ~[hazelcast-5.0.2.jar:5.0.2]
	at com.hazelcast.jet.impl.execution.ProcessorTasklet.stateMachineStep(ProcessorTasklet.java:300) ~[hazelcast-5.0.2.jar:5.0.2]
	at com.hazelcast.jet.impl.execution.ProcessorTasklet.stateMachineStep(ProcessorTasklet.java:281) ~[hazelcast-5.0.2.jar:5.0.2]
	at com.hazelcast.jet.impl.execution.ProcessorTasklet.call(ProcessorTasklet.java:255) ~[hazelcast-5.0.2.jar:5.0.2]
	at com.hazelcast.jet.impl.execution.TaskletExecutionService$CooperativeWorker.runTasklet(TaskletExecutionService.java:388) ~[hazelcast-5.0.2.jar:5.0.2]
	... 3 more
  ```
  
