## java-woof

An intentionally vulnerable application, to test Snyk's
Runtime Protection offering.

### How?

Everything should drive from Gradle. You need `java` 8
installed and working. On Windows, use `gradlew.bat` in
place of `./gradlew`.

#### First, check you can build:
```text
$ ./gradlew startWithAgent
...

  You must specify a project ID.
  
  Please run `snyk monitor`, and collect the id from the results'
    settings page,then re-run, for example:

     ./gradlew -PprojectId=4567901-2345-6789-0123-45678912345 startWithAgent
...
```

#### Then, run [snyk cli](https://snyk.io/docs/using-snyk/):
```text
$ snyk monitor

Monitoring java-woof...

Explore this snapshot at https://app.snyk.io/org/yall/project/4567901-2345-6789-0123-45678912345
```

####Now, you can start the app with the agent:
```text
$ ./gradlew -PprojectId=4567901-2345-6789-0123-45678912345 startWithAgent
...

,------------------------------------------------------------,
|                                                            |
|      The demo application has started successfully.        |
|                                                            |
|  If you are using the agent, you should be able            |
|    to see some lines like this above:                      |
|     snyk-agent initialisation: switching logging to: ...   |
|                                                            |
|                                                            |
|  You can visit the application on http://localhost:8080/   |
|                                                            |
|        You can stop the application with ctrl+c.           |
|                                                            |
`------------------------------------------------------------'

...
```

#### Exploit!

You can visit [the application](http://localhost:8080/), and
exploit it by uploading a zip file.

#### View results

If you then visit your Snyk project page again, you will see
an alert that the method has been called in runtime.
