package br.com.rabbitspringcloudcontractconsumidor;

import io.codearte.accurest.dsl.Accurest;
import io.codearte.accurest.messaging.AccurestMessage;
import io.codearte.accurest.messaging.AccurestMessaging;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubFinder;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(ids = "br.com:rabbit-spring-cloud-contract-produtor:+:stubs:6565",
        stubsMode = StubRunnerProperties.StubsMode.LOCAL)
@ActiveProfiles("test")
public class ListenerTest {

    @Autowired
    StubTrigger stubTrigger;

    @Autowired
    StubFinder stubFinder;

    @Autowired
    UserPresenceListener userPresenceListener;


    @Test
    public void shouldReceiveNotification() {
        stubTrigger.trigger("userTest");

        assertEquals(this.userPresenceListener.getUser().getUser(),"Alexandre");



    }

//    @Rule
//    public StubRunnerRule rule = new StubRunnerRule()
//            .downloadStub("br.com:rabbit-spring-cloud-contract-produtor")
//            .withPort(6543)
//            .stubsMode(StubRunnerProperties.StubsMode.LOCAL);
//            //.repoRoot("https://my.nexus.com/");
//
//    @Test
//    public void should_return_OK_from_a_stub() {
//        String object = new RestTemplate()
//                .getForObject("http://localhost:6543/", String.class);
//
//        BDDAssertions.then(object).isEqualTo("OK");
//    }


}