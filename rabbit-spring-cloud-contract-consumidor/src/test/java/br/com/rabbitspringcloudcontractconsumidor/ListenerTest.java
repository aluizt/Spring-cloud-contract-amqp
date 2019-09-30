package br.com.rabbitspringcloudcontractconsumidor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(ids = "br.com:rabbit-spring-cloud-contract-produtor:+:6565",
        repositoryRoot = "git://https://github.com/aluizt/stubs.git",
        stubsMode = StubRunnerProperties.StubsMode.REMOTE)
public class ListenerTest {

    @Autowired
    StubTrigger stubTrigger;

    @Autowired
    UserListener userListener;

    @Test
    public void shouldReceiveMessageWithUserAlexandre() {
        stubTrigger.trigger("userTest");
        User response = this.userListener.getUser();

        assertEquals(response.getName(), "Alexandre");
    }
}