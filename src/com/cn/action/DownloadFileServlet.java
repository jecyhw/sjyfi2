package com.cn.action;

import com.cn.util.File.DownloadFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by SNNU on 2015/3/17.
 */
public class DownloadFileServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isDeleted = false;
        if (request.getParameter("isDeleted").equals("1")) {
            isDeleted = true;
        }
        new DownloadFile().work(response, request.getParameter("downloadFile"), isDeleted);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
