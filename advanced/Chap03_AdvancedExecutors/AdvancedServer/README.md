![alt text](https://raw.githubusercontent.com/ukmjkim/concurrency/master/advanced/Chap03_AdvancedExecutors/AdvancedServer/docs/SequenceDiagram.png)



https://www.websequencediagrams.com/

title AdvancedServer

```
ConcurrentServer->+RequestTask: thread.start()
RequestTask-->-ConcurrentServer:
note right of ConcurrentServer: (pendingConnections, taskController)
ConcurrentServer->ConcurrentServer: socket.accept()
ConcurrentServer->ConcurrentServer: pendingConnections.put()
RequestTask->RequestTask: pendingConnections.take()
RequestTask->+ParallelCache: cache.get()
ParallelCache-->-RequestTask: null or CacheItem
RequestTask->+CommandFactory: getCommand()
CommandFactory->+Command: new Command
Command-->-CommandFactory: command
CommandFactory-->-RequestTask: command
RequestTask->ServerExecutor: executor.submit()
ServerExecutor->RequestTask: ServerTask
RequestTask->RequestTask: taskController.computeIfAbsent

alt Stop Command
    ServerExecutor->ConcurrentServer: shutdown()
else Else
    alt Cancel Command
        ServerExecutor->ConcurrentServer: cancelTasks()
    else Query/Report/Status/Error Command
        ServerExecutor->ConcurrentServer: finishTask()
end
```

