/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.http.api.request.proxy;

import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.dsl.xml.TypeDsl;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.display.Password;
import org.mule.runtime.http.api.client.proxy.ProxyConfig;

/**
 * Basic HTTP Proxy configuration based on host and port, and optionally a username and password for proxy authentication.
 *
 * @since 1.0
 */
@Alias("proxy")
@TypeDsl(allowTopLevelDefinition = true)
public class DefaultProxyConfig implements ProxyConfig {

  /**
   * Host where the proxy requests will be sent.
   */
  @Parameter
  private String host;

  /**
   * Port where the proxy requests will be sent.
   */
  @Parameter
  private int port = Integer.MAX_VALUE;

  /**
   * The username to authenticate against the proxy.
   */
  @Parameter
  @Optional
  private String username;

  /**
   * The password to authenticate against the proxy.
   */
  @Parameter
  @Optional
  @Password
  private String password;

  /**
   * A list of comma separated hosts against which the proxy should not be used
   */
  @Parameter
  @Optional
  String nonProxyHosts;

  public String getHost() {
    return host;
  }


  public int getPort() {
    return port;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getNonProxyHosts() {
    return nonProxyHosts;
  }

}
