package me.whiteship.designpatterns._03_behavioral_patterns._13_chain_of_responsibilities._03_java;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import org.springframework.core.annotation.Order;

@Order(1)
@WebFilter(urlPatterns = "/hello")
public class F1 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("F1 - 요청 전 처리");
        chain.doFilter(request, response);
        System.out.println("F1 - 요청 후 처리");
    }
}
