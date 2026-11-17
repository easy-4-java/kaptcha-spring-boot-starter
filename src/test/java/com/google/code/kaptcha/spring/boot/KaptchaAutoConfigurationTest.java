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
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.web.servlet.ServletRegistrationBean;

import com.google.code.kaptcha.spring.boot.ext.SessionKaptchaResolver;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {{ @link KaptchaAutoConfiguration }}.
 *
 * <p>Verifies the auto-configuration activates under the expected conditions
 * and exposes its declared beans.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("KaptchaAutoConfiguration Tests")
class KaptchaAutoConfigurationTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner();

    @Test
    @DisplayName("Auto-configuration class can be instantiated")
    void testInstantiation() {
        KaptchaAutoConfiguration configuration = new KaptchaAutoConfiguration();
        assertThat(configuration).isNotNull();
    }

    @Test
    @DisplayName("Auto-configuration loads when 'kaptcha.enabled=true'")
    void testLoadsWhenEnabledPropertySet() {
        runner.withUserConfiguration(KaptchaAutoConfiguration.class)
                .withPropertyValues(
                        "kaptcha.enabled=true",
                        "kaptcha.pattern=/verify.jpg",
                        "kaptcha.captcha-store-key=verify-code",
                        "kaptcha.captcha-date-store-key=verify-date",
                        "kaptcha.captcha-timeout=120000",
                        "kaptcha.parameters[kaptcha.textproducer.char.length]=5")
                .run(context -> {
                    assertThat(context).hasSingleBean(KaptchaAutoConfiguration.class);
                    KaptchaProperties properties = context.getBean(KaptchaProperties.class);
                    assertThat(properties.isEnabled()).isTrue();
                    assertThat(properties.getPattern()).isEqualTo("/verify.jpg");
                    assertThat(properties.getCaptchaStoreKey()).isEqualTo("verify-code");
                    assertThat(properties.getCaptchaDateStoreKey()).isEqualTo("verify-date");
                    assertThat(properties.getCaptchaTimeout()).isEqualTo(120000);
                    SessionKaptchaResolver resolver = context.getBean(SessionKaptchaResolver.class);
                    assertThat(resolver.getCaptchaStoreKey()).isEqualTo("verify-code");
                    assertThat(resolver.getCaptchaDateStoreKey()).isEqualTo("verify-date");
                    assertThat(resolver.getCaptchaTimeout()).isEqualTo(120000);
                    ServletRegistrationBean<?> registration = context.getBean(ServletRegistrationBean.class);
                    assertThat(registration.getUrlMappings()).contains("/verify.jpg");
                    assertThat(registration.getInitParameters()).containsEntry("kaptcha.textproducer.char.length", "5");
                });
    }

    @Test
    @DisplayName("Auto-configuration loads by default")
    void testLoadedWhenPropertyAbsent() {
        runner.withUserConfiguration(KaptchaAutoConfiguration.class)
                .run(context -> assertThat(context).hasSingleBean(KaptchaAutoConfiguration.class));
    }

    @Test
    @DisplayName("Auto-configuration is absent when 'kaptcha.enabled=false'")
    void testNotLoadedWhenDisabled() {
        runner.withUserConfiguration(KaptchaAutoConfiguration.class)
                .withPropertyValues("kaptcha.enabled=false")
                .run(context -> assertThat(context).doesNotHaveBean(KaptchaAutoConfiguration.class));
    }
}
