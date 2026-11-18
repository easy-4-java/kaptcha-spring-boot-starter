/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.code.kaptcha.spring.boot;

import com.google.code.kaptcha.spring.boot.ext.SessionKaptchaResolver;
import com.google.code.kaptcha.spring.boot.ext.exception.CaptchaIncorrectException;
import com.google.code.kaptcha.spring.boot.ext.exception.CaptchaTimeoutException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {{ @link SessionKaptchaResolver }}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("SessionKaptchaResolver Tests")
class SessionKaptchaResolverTest {

    @Test
    @DisplayName("Instance can be created via constructor")
    void testInstantiation() {
        SessionKaptchaResolver instance = new SessionKaptchaResolver();
        assertThat(instance).isNotNull();
    }

    @Test
    void storesAndValidatesCaptchaInDistinctSessionAttributes() throws Exception {
        SessionKaptchaResolver resolver = new SessionKaptchaResolver();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        resolver.setCaptcha(request, response, "Ab12", new Date());

        assertThat(resolver.getCaptchaStoreKey()).isNotEqualTo(resolver.getCaptchaDateStoreKey());
        assertThat(resolver.validCaptcha(request, "ab12")).isTrue();
        assertThat(resolver.validCaptcha(request, "wrong")).isFalse();
    }

    @Test
    void rejectsMissingAndExpiredCaptcha() {
        SessionKaptchaResolver resolver = new SessionKaptchaResolver();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertThatThrownBy(() -> resolver.validCaptcha(request, ""))
                .isInstanceOf(CaptchaIncorrectException.class);
        assertThatThrownBy(() -> resolver.validCaptcha(request, "1234"))
                .isInstanceOf(CaptchaIncorrectException.class);

        resolver.setCaptcha(request, response, "1234", new Date(0));
        assertThatThrownBy(() -> resolver.validCaptcha(request, "1234"))
                .isInstanceOf(CaptchaTimeoutException.class);
    }

    @Test
    void honorsExplicitSessionKeysAndTimeout() {
        SessionKaptchaResolver resolver = new SessionKaptchaResolver();
        resolver.init("code", "created", 120000);
        assertThat(resolver.getCaptchaStoreKey()).isEqualTo("code");
        assertThat(resolver.getCaptchaDateStoreKey()).isEqualTo("created");
        assertThat(resolver.getCaptchaTimeout()).isEqualTo(120000);
    }
}
