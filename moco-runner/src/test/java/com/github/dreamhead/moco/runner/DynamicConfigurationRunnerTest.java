package com.github.dreamhead.moco.runner;

import com.github.dreamhead.moco.bootstrap.StartArgs;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static com.github.dreamhead.moco.RemoteTestUtils.port;
import static com.github.dreamhead.moco.RemoteTestUtils.root;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DynamicConfigurationRunnerTest extends AbstractRunnerTest {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void should_reload_configuration() throws IOException, InterruptedException {
        final File config = tempFolder.newFile();
        changeFileContent(config, "[{\"response\" :{" +
                "\"text\" : \"foo\"" +
                "}}]");

        RunnerFactory factory = new RunnerFactory("SHUTDOWN");
        runner = factory.createRunner(new StartArgs(port(), 9090, config.getAbsolutePath(), null, null, null));
        runner.run();
        assertThat(helper.get(root()), is("foo"));

        changeFileContent(config, "[{\"response\" :{" +
                "\"text\" : \"foobar\"" +
                "}}]");

        waitChangeHappens();

        assertThat(helper.get(root()), is("foobar"));
    }
}
