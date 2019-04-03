package io.snyk.woof.server;

import io.dropwizard.lifecycle.ServerLifecycleListener;
import org.eclipse.jetty.server.Server;

public class WelcomeBanner implements ServerLifecycleListener {
    @Override
    public void serverStarted(Server server) {
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            return;
        }

        System.out.println();
        System.out.println();
        System.out.println(",------------------------------------------------------------,");
        System.out.println("|                                                            |");
        System.out.println("|      The demo application has started successfully.        |");
        System.out.println("|                                                            |");
        System.out.println("|  If you are using the agent, you should be able            |");
        System.out.println("|    to see some lines like this above:                      |");
        System.out.println("|     snyk-agent initialisation: switching logging to: ...   |");
        System.out.println("|                                                            |");
        System.out.println("|                                                            |");
        System.out.println("|  You can visit the application on http://localhost:" + getLocalPort(server) + "/   |");
        System.out.println("|                                                            |");
        System.out.println("| You can try the supplied exploit with ./gradlew runExploit |");
        System.out.println("|                                                            |");
        System.out.println("|        You can stop the application with ctrl+c.           |");
        System.out.println("|                                                            |");
        System.out.println("`------------------------------------------------------------'");
        System.out.println();
        System.out.println();
        System.out.println("The server is running. Status and logs will continue below:");
    }
}
