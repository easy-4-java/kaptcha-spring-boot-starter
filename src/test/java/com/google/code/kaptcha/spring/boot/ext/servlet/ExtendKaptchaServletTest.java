package com.google.code.kaptcha.spring.boot.ext.servlet;

import java.awt.image.BufferedImage;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.spring.boot.ext.KaptchaResolver;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ExtendKaptchaServletTest {

    @Test
    void getStoresCaptchaAndReturnsJpegWithoutCaching() throws Exception {
        KaptchaResolver resolver = mock(KaptchaResolver.class);
        ExtendKaptchaServlet servlet = new ExtendKaptchaServlet(resolver);
        MockServletConfig config = new MockServletConfig();
        config.addInitParameter(Constants.KAPTCHA_PRODUCER_IMPL, FixedProducer.class.getName());
        servlet.init(config);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        servlet.doGet(request, response);

        verify(resolver).setCaptcha(eq(request), eq(response), eq("AbC"), any(java.util.Date.class));
        assertThat(response.getContentType()).isEqualTo("image/jpeg");
        assertThat(response.getHeader("Cache-Control")).contains("no-store");
        assertThat(response.getContentAsByteArray()).isNotEmpty();
    }

    public static class FixedProducer implements Producer {
        @Override
        public String createText() {
            return "AbC";
        }

        @Override
        public BufferedImage createImage(String text) {
            return new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        }
    }
}
