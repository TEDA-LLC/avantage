//package com.example.demo;
//
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
//public class CORSFilter implements Filter {
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//
//        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
//        httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
//        httpResponse.setHeader("Access-Control-Allow-Headers",
//                "Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization, X-Auth-Token, X-Requested-With");
//        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
//
//        if ("OPTIONS".equals(httpRequest.getMethod())) {
//            httpResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
//            return;
//        }
//
//        chain.doFilter(request, response);
//    }
//
//    // Other methods from the Filter interface (init, destroy) can be left empty or implemented as needed.
//}
