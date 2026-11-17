package com.google.code.kaptcha.spring.boot;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CaptchaExceptionMessagesTest {

    @Test
    void defaultMessagesDescribeValidationFailures() {
        assertThat(new com.google.code.kaptcha.spring.boot.exception.CaptchaIncorrectException().getMessage())
                .isEqualTo("captcha incorrect");
        assertThat(new com.google.code.kaptcha.spring.boot.exception.CaptchaNotFoundException().getMessage())
                .isEqualTo("no comparable captcha found");
        assertThat(new com.google.code.kaptcha.spring.boot.exception.CaptchaTimeoutException().getMessage())
                .isEqualTo("captcha expired");
        assertThat(new com.google.code.kaptcha.spring.boot.ext.exception.CaptchaIncorrectException().getMessage())
                .isEqualTo("captcha incorrect");
        assertThat(new com.google.code.kaptcha.spring.boot.ext.exception.CaptchaNotFoundException().getMessage())
                .isEqualTo("no comparable captcha found");
        assertThat(new com.google.code.kaptcha.spring.boot.ext.exception.CaptchaTimeoutException().getMessage())
                .isEqualTo("captcha expired");
    }

    @Test
    void customMessagesRemainAvailableToCallers() {
        String message = "captcha validation failed";
        assertThat(new com.google.code.kaptcha.spring.boot.exception.CaptchaIncorrectException(message).getMessage())
                .isEqualTo(message);
        assertThat(new com.google.code.kaptcha.spring.boot.exception.CaptchaNotFoundException(message).getMessage())
                .isEqualTo(message);
        assertThat(new com.google.code.kaptcha.spring.boot.exception.CaptchaTimeoutException(message).getMessage())
                .isEqualTo(message);
        assertThat(new com.google.code.kaptcha.spring.boot.ext.exception.CaptchaIncorrectException(message).getMessage())
                .isEqualTo(message);
        assertThat(new com.google.code.kaptcha.spring.boot.ext.exception.CaptchaNotFoundException(message).getMessage())
                .isEqualTo(message);
        assertThat(new com.google.code.kaptcha.spring.boot.ext.exception.CaptchaTimeoutException(message).getMessage())
                .isEqualTo(message);
    }
}
