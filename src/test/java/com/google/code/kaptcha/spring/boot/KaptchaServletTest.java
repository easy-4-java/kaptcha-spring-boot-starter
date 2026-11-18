package com.google.code.kaptcha.spring.boot;

import com.google.code.kaptcha.spring.boot.ext.SessionKaptchaResolver;
import com.google.code.kaptcha.spring.boot.ext.servlet.ExtendKaptchaServlet;
import com.google.code.kaptcha.spring.boot.servlet.KaptchaJakartaServlet;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServlet;
import java.io.ByteArrayInputStream;

import static org.assertj.core.api.Assertions.assertThat;

class KaptchaServletTest {

    @Test
    void extendServletWritesJpegAndStoresCaptcha() throws Exception {
        SessionKaptchaResolver resolver = new SessionKaptchaResolver();
        verifyServlet(new ExtendKaptchaServlet(resolver), resolver);
    }

    @Test
    void legacyNamedServletWritesJpegAndStoresCaptcha() throws Exception {
        SessionKaptchaResolver resolver = new SessionKaptchaResolver();
        verifyServlet(new KaptchaJakartaServlet(resolver), resolver);
    }

    private void verifyServlet(HttpServlet servlet, SessionKaptchaResolver resolver) throws Exception {
        MockServletConfig config = new MockServletConfig();
        config.addInitParameter("kaptcha.textproducer.char.length", "4");
        servlet.init(config);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/kaptcha");
        MockHttpServletResponse response = new MockHttpServletResponse();
        servlet.service(request, response);

        assertThat(response.getContentType()).isEqualTo("image/jpeg");
        assertThat(response.getHeader("Cache-Control")).contains("no-store");
        assertThat(ImageIO.read(new ByteArrayInputStream(response.getContentAsByteArray()))).isNotNull();
        assertThat(request.getSession().getAttribute(resolver.getCaptchaStoreKey()))
                .isInstanceOf(String.class);
        assertThat(request.getSession().getAttribute(resolver.getCaptchaDateStoreKey()))
                .isNotNull();
    }
}
