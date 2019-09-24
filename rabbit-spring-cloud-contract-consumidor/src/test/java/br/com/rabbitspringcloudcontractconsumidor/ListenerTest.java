package br.com.rabbitspringcloudcontractconsumidor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubFinder;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(ids = "br.com:rabbit-spring-cloud-contract-produtor:+:stubs:6565",
        stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class ListenerTest {

    @Autowired
    StubTrigger stubTrigger;

    @Autowired
    StubFinder stubFinder;

    @Autowired
    UserListener userListener;

    @Test
    public void shouldReceiveNotification() {
        stubTrigger.trigger("userTest");

        assertEquals(this.userListener.getUser().getUser(), "Alexandre");
    }
}