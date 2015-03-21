package com.cn.action;

import com.cn.test.TestOutput;
import com.cn.util.Config;
import com.cn.util.Out;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Created by root on 12/19/14.
 */
public class CheckFileExistServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileName = request.getParameter("filename");
        Out out = new Out(response);
        if (new File(Config.zipFileDir + fileName).exists() || new File(Config.oldFileDir + fileName).exists()) {
            out.printText("1");
        } else {
            out.printText("0");
        }
        out.close();
        
       
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
