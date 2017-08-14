/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.test.http.functional.requester;

import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mule.runtime.http.api.HttpHeaders.Names.CONTENT_TYPE;
import static org.mule.test.http.AllureConstants.HttpFeature.HTTP_EXTENSION;
import static org.mule.test.http.AllureConstants.HttpFeature.HttpStory.MULTIPART;
import org.mule.extension.http.api.HttpResponseAttributes;
import org.mule.runtime.core.api.InternalEvent;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Story;
import org.eclipse.jetty.server.Request;
import org.junit.Ignore;
import org.junit.Test;

@Feature(HTTP_EXTENSION)
@Story(MULTIPART)
@Ignore("MULE-12985: Move multipart test cases to HTTP service and adapt extension ones")
@Issue("MULE-12985")
public class HttpRequestMultipartTestCase extends AbstractHttpRequestTestCase {

  private static final String BOUNDARY = "bec89590-35fe-11e5-a966-de100cec9c0d";
  private static final String CONTENT_DISPOSITION_HEADER = "Content-Disposition: form-data; name=\"partName\"\r\n";
  private static final String MULTIPART_FORMAT = "--%1$s\r\n %2$sContent-Type: text/plain\n\r\ntest\r\n--%1$s--\r\n";
  private static final String CONTENT_DISPOSITION_PATH = "/contentDisposition";


  @Override
  protected String getConfigFile() {
    return "http-request-multipart-config.xml";
  }

  @Override
  protected void handleRequest(Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setHeader(CONTENT_TYPE, String.format("multipart/form-data; boundary=%s", BOUNDARY));
    response.setStatus(SC_OK);
    extractBaseRequestParts(baseRequest);
    String contentDispositionHeader;
    if (baseRequest.getUri().getPath().equals(CONTENT_DISPOSITION_PATH)) {
      contentDispositionHeader = CONTENT_DISPOSITION_HEADER;
    } else {
      contentDispositionHeader = "";
    }
    response.getWriter().print(String.format(MULTIPART_FORMAT, BOUNDARY, contentDispositionHeader));
    baseRequest.setHandled(true);
  }

  @Test
  public void getMultipartContentWithContentDisposition() throws Exception {
    testWithPath(CONTENT_DISPOSITION_PATH);
  }

  @Test
  public void getMultipartContentWithoutContentDisposition() throws Exception {
    testWithPath("/");
  }

  private void testWithPath(String path) throws Exception {
    InternalEvent response = flowRunner("requestFlow").withVariable("requestPath", path).run();
    Object attributes = response.getMessage().getAttributes().getValue();
    assertThat(attributes, instanceOf(HttpResponseAttributes.class));
    //    assertThat(((MultiPartPayload) response.getMessage().getPayload().getValue()), hasSize(1));
  }
}
