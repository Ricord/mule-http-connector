/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.http.api.notification;

import static org.mule.runtime.api.notification.NotificationType.TRACE;
import org.mule.runtime.extension.api.notification.NotificationActionDefinition;
import org.mule.runtime.extension.api.notification.NotificationInfo;
import org.mule.runtime.api.notification.NotificationType;

/**
 * Custom {@link NotificationInfo} regarding HTTP connector related events.
 *
 * @since 1.1
 */
public class HttpNotificationInfo<T extends HttpData> implements NotificationInfo<T> {

  private HttpNotificationAction action;
  private T data;

  public HttpNotificationInfo(T data, HttpNotificationAction action) {
    this.action = action;
    this.data = data;
  }

  @Override
  public NotificationType getType() {
    return TRACE;
  }

  @Override
  public String getEventName() {
    return "HttpNotification";
  }

  @Override
  public T getData() {
    return data;
  }

  @Override
  public HttpNotificationAction getAction() {
    return action;
  }

  @Override
  public boolean isSynchronous() {
    return true;
  }

  public enum HttpNotificationAction implements NotificationActionDefinition<HttpNotificationAction> {

    REQUEST_START("Started HTTP request"), REQUEST_COMPLETE("Completed HTTP request");

    private final String description;

    HttpNotificationAction(String description) {
      this.description = description;
    }

    @Override
    public String getDescription() {
      return description;
    }

  }

}
