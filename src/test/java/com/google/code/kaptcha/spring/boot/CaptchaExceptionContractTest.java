package com.google.code.kaptcha.spring.boot;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CaptchaExceptionContractTest {

    @Test
    void defaultMessagesIdentifyTheFailure() {
        assertThat(new com.google.code.kaptcha.spring.boot.exception.CaptchaIncorrectException())
                .hasMessage("captcha incorrect");
        assertThat(new com.google.code.kaptcha.spring.boot.exception.CaptchaNotFoundException())
                .hasMessage("no comparable captcha found");
        assertThat(new com.google.code.kaptcha.spring.boot.exception.CaptchaTimeoutException())
                .hasMessage("captcha expired");
        assertThat(new com.google.code.kaptcha.spring.boot.ext.exception.CaptchaIncorrectException())
                .hasMessage("captcha incorrect");
        assertThat(new com.google.code.kaptcha.spring.boot.ext.exception.CaptchaNotFoundException())
                .hasMessage("no comparable captcha found");
        assertThat(new com.google.code.kaptcha.spring.boot.ext.exception.CaptchaTimeoutException())
                .hasMessage("captcha expired");
    }

    @Test
    void customMessagesArePreserved() {
        assertThat(new com.google.code.kaptcha.spring.boot.exception.CaptchaIncorrectException("invalid"))
                .hasMessage("invalid");
        assertThat(new com.google.code.kaptcha.spring.boot.exception.CaptchaNotFoundException("missing"))
                .hasMessage("missing");
        assertThat(new com.google.code.kaptcha.spring.boot.exception.CaptchaTimeoutException("old"))
                .hasMessage("old");
        assertThat(new com.google.code.kaptcha.spring.boot.ext.exception.CaptchaIncorrectException("invalid"))
                .hasMessage("invalid");
        assertThat(new com.google.code.kaptcha.spring.boot.ext.exception.CaptchaNotFoundException("missing"))
                .hasMessage("missing");
        assertThat(new com.google.code.kaptcha.spring.boot.ext.exception.CaptchaTimeoutException("old"))
                .hasMessage("old");
    }
}
