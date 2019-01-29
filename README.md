## java-woof

An intentionally vulnerable application, to test Snyk's
Runtime Protection offering.


### How?

Everything should drive from Gradle, which is bundled in this repository.

You just need a Java JDK 8 or above, installed and working.

The examples use `./gradlew` everywhere. On Windows, use `gradlew.bat` in place of `./gradlew`,
for example, `gradlew.bat startWithAgent`.


#### First, check you can build:

```text
$ ./gradlew startWithAgent
...

  You must specify a project ID.
  
  Please run `snyk monitor`, collect the id from the results' settings page,
    then re-run `startWithAgent` using that ID.

  For example (you *must* change the projectId!):

     ./gradlew -PprojectId=4567901-2345-6789-0123-45678912345 startWithAgent
...
```


#### Then, run [snyk cli](https://snyk.io/docs/using-snyk/):

```text
$ snyk monitor

Monitoring java-woof...

Explore this snapshot at https://app.snyk.io/org/yall/project/4567901-2345-6789-0123-45679012345
```


#### Now, you can start the app with the agent:

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
| You can try the supplied exploit with ./gradlew runExploit |
|                                                            |
|        You can stop the application with ctrl+c.           |
|                                                            |
`------------------------------------------------------------'

...
```


#### Exploit!

You can visit [the application](http://localhost:8080/), and
exploit it by uploading a zip file, for example, the provided `zip-slip.zip`.

Alternatively, you can upload from the command line, using:

```text
$ ./gradlew runExploit
> Task :runExploit
The file contains these entries:
 * good.txt


An exploit attempt has been made.
```

If your application is protected, then you should
  shortly see the warning on [your Snyk dashboard](https://app.snyk.io/).
