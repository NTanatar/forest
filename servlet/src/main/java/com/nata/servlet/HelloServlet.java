package com.nata.servlet;

import java.io.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello")
public class HelloServlet extends HttpServlet {

    public void init() {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();

        if (request.getParameter("name") != null) {
            session.setAttribute("name", request.getParameter("name"));
        }

        String name = (String) session.getAttribute("name");

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        if (name != null) {
            out.println("Hello," + name + "!");
        } else {
            out.println("Hello!");
        }
    }

    public void destroy() {
    }
}