/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.http.internal.request;

import static org.mule.runtime.api.notification.AbstractServerNotification.EXTENSION_EVENT_ACTION_START_RANGE;
import static org.mule.runtime.api.notification.AbstractServerNotification.TYPE_TRACE;
import static org.mule.runtime.extension.api.notification.NotificationHandler.register;
import org.mule.runtime.extension.api.notification.NotificationInfo;

import java.net.URI;

/**
 * Custom {@link org.mule.runtime.api.notification.Notification} regarding HTTP connector related events.
 *
 * @since 1.1
 */
public class HttpNotificationInfo implements NotificationInfo<URI> {

  private static final int HTTP_ACTION_BASE = EXTENSION_EVENT_ACTION_START_RANGE * 2;
  private static int ACTION_INDEX = 0;

  public static final int HTTP_REQUEST_START = ++ACTION_INDEX + HTTP_ACTION_BASE;
  public static final int HTTP_REQUEST_COMPLETE = ++ACTION_INDEX + HTTP_ACTION_BASE;

  static {
    register("Started HTTP request", HTTP_REQUEST_START);
    register("Completed HTTP request", HTTP_REQUEST_COMPLETE);
  }

  private int action;
  private URI uri;

  public HttpNotificationInfo(URI uri, int action) {
    this.action = action;
    this.uri = uri;
  }

  @Override
  public String getType() {
    return TYPE_TRACE;
  }

  @Override
  public String getEventName() {
    return "HttpNotification";
  }

  @Override
  public URI getData() {
    return uri;
  }

  @Override
  public int getAction() {
    return action;
  }

}
