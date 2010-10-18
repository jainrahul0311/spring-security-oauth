/*
 * Copyright 2008 Web Cohesion
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.security.oauth2.config;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.security.oauth2.consumer.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.consumer.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.consumer.webserver.WebServerProfileResourceDetails;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author Ryan Heaton
 */
public class OAuth2ResourceBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

  @Override
  protected Class getBeanClass(Element element) {
    if ("web-server".equals(element.getAttribute("type")) || "web_server".equals(element.getAttribute("type"))) {
      return WebServerProfileResourceDetails.class;
    }
    return BaseOAuth2ProtectedResourceDetails.class;
  }

  @Override
  protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
    String id = element.getAttribute("id");
    if (!StringUtils.hasText(id)) {
      parserContext.getReaderContext().error("An id must be supplied on a resource element.", element);
    }
    builder.addPropertyValue("id", id);
    
    String type = element.getAttribute("type");
    if (StringUtils.hasText(type)) {
      builder.addPropertyValue("grantType", type);
    }

    String accessTokenUri = element.getAttribute("accessTokenUri");
    if (!StringUtils.hasText(accessTokenUri)) {
      parserContext.getReaderContext().error("An accessTokenUri must be supplied on a resource element.", element);
    }
    builder.addPropertyValue("accessTokenUri", accessTokenUri);
    
    String clientId = element.getAttribute("clientId");
    if (!StringUtils.hasText(clientId)) {
      parserContext.getReaderContext().error("An clientId must be supplied on a resource element.", element);
    }
    builder.addPropertyValue("clientId", clientId);
    
    String clientSecret = element.getAttribute("clientSecret");
    if (StringUtils.hasText(clientSecret)) {
      builder.addPropertyValue("secretRequired", "true");
      builder.addPropertyValue("clientSecret", clientSecret);
    }

    String clientAuthenticationScheme = element.getAttribute("clientAuthenticationScheme");
    if (StringUtils.hasText(clientSecret)) {
      builder.addPropertyValue("clientAuthenticationScheme", clientAuthenticationScheme);
    }

    String userAuthorizationUri = element.getAttribute("userAuthorizationUri");
    if (StringUtils.hasText(userAuthorizationUri)) {
      builder.addPropertyValue("userAuthorizationUri", userAuthorizationUri);
    }

    String preEstablishedRedirectUri = element.getAttribute("preEstablishedRedirectUri");
    if (StringUtils.hasText(preEstablishedRedirectUri)) {
      builder.addPropertyValue("preEstablishedRedirectUri", preEstablishedRedirectUri);
    }

    String requireImmediateAuthorization = element.getAttribute("requireImmediateAuthorization");
    if (StringUtils.hasText(requireImmediateAuthorization)) {
      builder.addPropertyValue("requireImmediateAuthorization", requireImmediateAuthorization);
    }

    String scope = element.getAttribute("scope");
    if (StringUtils.hasText(scope)) {
      List<String> scopeList = new ArrayList<String>();
      for (StringTokenizer tokenizer = new StringTokenizer(scope, ","); tokenizer.hasMoreTokens();) {
        String tok = tokenizer.nextToken();
        scopeList.add(tok.trim());
      }

      builder.addPropertyValue("scoped", "true");
      builder.addPropertyValue("scope", scopeList);
    }

    OAuth2ProtectedResourceDetails.BearerTokenMethod btm = OAuth2ProtectedResourceDetails.BearerTokenMethod.header;
    String bearerTokenMethod = element.getAttribute("bearerTokenMethod");
    if (StringUtils.hasText(bearerTokenMethod)) {
      btm = OAuth2ProtectedResourceDetails.BearerTokenMethod.valueOf(bearerTokenMethod);
    }
    builder.addPropertyValue("bearerTokenMethod", btm);


  }
}