/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.http.api.notification;

import static org.mule.runtime.api.metadata.DataType.fromType;
import static org.mule.runtime.api.notification.NotificationType.TRACE;
import org.mule.runtime.api.metadata.DataType;
import org.mule.runtime.api.notification.NotificationType;
import org.mule.runtime.extension.api.notification.NotificationActionDefinition;

/**
 * HTTP notification actions.
 *
 * @since 1.1
 */
public enum HttpNotificationAction implements NotificationActionDefinition<HttpNotificationAction> {

  REQUEST_START("Started HTTP request", fromType(HttpRequestData.class)), REQUEST_COMPLETE("Completed HTTP request",
      fromType(HttpResponseData.class));

  private final String description;
  private final DataType dataType;

  HttpNotificationAction(String description, DataType dataType) {
    this.description = description;
    this.dataType = dataType;
  }

  @Override
  public NotificationType getType() {
    return TRACE;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public boolean isSynchronous() {
    return true;
  }

  @Override
  public DataType getDataType() {
    return dataType;
  }

}
