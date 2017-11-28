/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.test.http.functional.requester;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mule.extension.http.internal.request.HttpNotificationInfo.HTTP_REQUEST_COMPLETE;
import static org.mule.extension.http.internal.request.HttpNotificationInfo.HTTP_REQUEST_START;
import static org.mule.runtime.api.notification.AbstractServerNotification.getActionName;
import static org.mule.runtime.core.api.context.notification.ServerNotificationManager.createDefaultNotificationManager;
import static org.mule.test.http.functional.TestConnectorMessageNotificationListener.register;
import org.mule.runtime.core.api.context.MuleContextBuilder;
import org.mule.test.http.functional.TestConnectorMessageNotificationListener;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;

public class HttpRequestNotificationsTestCase extends AbstractHttpRequestTestCase {

  @Override
  protected String getConfigFile() {
    return "http-request-notifications-config.xml";
  }

  @Override
  protected void configureMuleContext(MuleContextBuilder contextBuilder) {
    contextBuilder.setNotificationManager(register(createDefaultNotificationManager()));
    super.configureMuleContext(contextBuilder);
  }

  @Test
  public void receiveNotification() throws Exception {
    CountDownLatch latch = new CountDownLatch(2);
    TestConnectorMessageNotificationListener listener =
        new TestConnectorMessageNotificationListener(latch, "requestFlow/http:request");
    muleContext.getNotificationManager().addListener(listener);

    flowRunner("requestFlow").withPayload(TEST_MESSAGE).run().getMessage();

    latch.await(1000, MILLISECONDS);

    assertThat(listener.getNotificationActionNames(),
               contains(getActionName(HTTP_REQUEST_START), getActionName(HTTP_REQUEST_COMPLETE)));

//    // End event should have appended http.status and http.reason as inbound properties
//    Message message = listener.getNotifications(getActionName(HTTP_REQUEST_COMPLETE)).get(0).getEvent().getMessage();
//    // For now, check the response, since we no longer have control over the MuleEvent generated, only the Message
//    assertThat((HttpResponseAttributes) response.getAttributes().getValue(),
//               HttpMessageAttributesMatchers.hasStatusCode(OK.getStatusCode()));
//    assertThat((HttpResponseAttributes) response.getAttributes().getValue(),
//               HttpMessageAttributesMatchers.hasReasonPhrase(OK.getReasonPhrase()));
//
//    Message requestMessage = listener.getNotifications(getActionName(HTTP_REQUEST_START)).get(0).getEvent().getMessage();
//    assertThat(requestMessage, equalTo(message));
  }

}
