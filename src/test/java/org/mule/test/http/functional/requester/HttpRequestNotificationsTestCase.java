/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.test.http.functional.requester;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mule.runtime.core.api.context.notification.ServerNotificationManager.createDefaultNotificationManager;
import static org.mule.runtime.http.api.HttpConstants.HttpStatus.OK;
import static org.mule.runtime.http.api.HttpConstants.Method.POST;
import static org.mule.test.http.functional.TestConnectorMessageNotificationListener.register;
import org.mule.extension.http.api.notification.HttpRequestData;
import org.mule.extension.http.api.notification.HttpResponseData;
import org.mule.runtime.api.notification.NotificationListener;
import org.mule.runtime.core.api.context.MuleContextBuilder;
import org.mule.runtime.extension.api.notification.ExtensionNotification;

import java.util.LinkedList;
import java.util.List;
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

    ExtensionNotificationListener listener = new ExtensionNotificationListener(latch);
    muleContext.getNotificationManager().addListener(listener);
    muleContext.getNotificationManager().addInterfaceToType(ExtensionNotificationListener.class, ExtensionNotification.class);

    flowRunner("requestFlow").withPayload(TEST_MESSAGE).run().getMessage();

    latch.await(1000, MILLISECONDS);

    assertThat(listener.getNotifications().stream().map(n -> n.getAction().getId()).collect(toList()),
               hasItems("HTTP:REQUEST_START", "HTTP:REQUEST_COMPLETE"));

    // verify that request data was collected
    ExtensionNotification extensionNotification1 = listener.getNotifications().get(0);
    assertThat(extensionNotification1.getData(), is(instanceOf(HttpRequestData.class)));
    HttpRequestData requestData = (HttpRequestData) extensionNotification1.getData();
    assertThat(requestData.getMethod(), is(POST.name()));
    assertThat(requestData.getQueryParams().getAll("query"), containsInAnyOrder("param", "otherParam"));
    assertThat(requestData.getHeaders().getAll("header"), containsInAnyOrder("value", "otherValue"));

    // verify that response data was collected
    ExtensionNotification extensionNotification2 = listener.getNotifications().get(1);
    assertThat(extensionNotification2.getData(), is(instanceOf(HttpResponseData.class)));
    HttpResponseData responseData = (HttpResponseData) extensionNotification2.getData();
    assertThat(responseData.getStatusCode(), is(OK.getStatusCode()));
    assertThat(responseData.getReasonPhrase(), is(OK.getReasonPhrase()));

  }

  private class ExtensionNotificationListener implements NotificationListener<ExtensionNotification> {

    private CountDownLatch latch;
    private List<ExtensionNotification> notifications = new LinkedList<>();

    public ExtensionNotificationListener(CountDownLatch latch) {
      this.latch = latch;
    }

    @Override
    public void onNotification(ExtensionNotification notification) {
      notifications.add(notification);
      latch.countDown();
    }

    public List<ExtensionNotification> getNotifications() {
      return notifications;
    }
  }

}
