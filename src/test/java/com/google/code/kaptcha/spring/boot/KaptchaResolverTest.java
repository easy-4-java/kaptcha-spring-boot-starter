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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Date;

import com.google.code.kaptcha.spring.boot.ext.KaptchaResolver;
import com.google.code.kaptcha.spring.boot.ext.SessionKaptchaResolver;
import com.google.code.kaptcha.spring.boot.ext.exception.CaptchaIncorrectException;
import com.google.code.kaptcha.spring.boot.ext.exception.CaptchaTimeoutException;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for {{ @link KaptchaResolver }}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("KaptchaResolver Tests")
class KaptchaResolverTest {

    @Test
    @DisplayName("Missing captcha is rejected")
    void testMissingCaptcha() {
        KaptchaResolver resolver = new SessionKaptchaResolver();
        resolver.init("captcha", "captcha-date", 60000);

        assertThrows(CaptchaIncorrectException.class,
                () -> resolver.validCaptcha(new MockHttpServletRequest(), "abc"));
    }

    @Test
    @DisplayName("Expired captcha is rejected")
    void testExpiredCaptcha() {
        KaptchaResolver resolver = new SessionKaptchaResolver();
        resolver.init("captcha", "captcha-date", 1000);
        MockHttpServletRequest request = new MockHttpServletRequest();
        resolver.setCaptcha(request, new MockHttpServletResponse(), "abc", new Date(System.currentTimeMillis() - 2000));

        assertThrows(CaptchaTimeoutException.class,
                () -> resolver.validCaptcha(request, "abc"));
    }
}
