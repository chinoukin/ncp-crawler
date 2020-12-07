package com.wisea;

import org.apache.catalina.connector.Connector;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
@MapperScan(basePackages = "com.wisea.mapper")
public class Application {
    @Value("${server.http.port}")
    private int httpPort;
    @Value("${server.port}")
    private int httpsPort;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(10000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        return restTemplate;
    }

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(httpPort);
        tomcat.addAdditionalTomcatConnectors(connector); // 添加http
        return tomcat;
    }


//    @Bean
//    public ServletWebServerFactory undertowFactory() {
//        UndertowServletWebServerFactory undertowFactory = new UndertowServletWebServerFactory();
//        undertowFactory.addBuilderCustomizers((Undertow.Builder builder) -> {
//            builder.addHttpListener(httpPort, "0.0.0.0");
//            // 开启HTTP2
//            builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true);
//        });
//
////        undertowFactory.addDeploymentInfoCustomizers(deploymentInfo -> {
////            // 开启HTTP自动跳转至HTTPS
////            deploymentInfo.addSecurityConstraint(new SecurityConstraint()
////                    .addWebResourceCollection(new WebResourceCollection().addUrlPattern("/*"))
////                    .setTransportGuaranteeType(TransportGuaranteeType.CONFIDENTIAL)
////                    .setEmptyRoleSemantic(SecurityInfo.EmptyRoleSemantic.PERMIT))
////                    .setConfidentialPortManager(exchange -> httpsPort);
////        });
//        return undertowFactory;
//    }


}
