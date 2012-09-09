package com.feiyuxmu.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/*", asyncSupported = true)
public class TestAsyncServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 68349781578761068L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 	resp.setContentType("text/html;charset=UTF-8");
	        PrintWriter out = resp.getWriter();
	        out.println("进入Servlet的时间：" + new Date() + ".");
	        out.flush();
	        
	        //在子线程中执行业务调用，并由其负责输出响应，主线程退出
	        AsyncContext ctx = req.startAsync();
	        ctx.addListener(new AsyncListener(){

				@Override
				public void onComplete(AsyncEvent event) throws IOException {
					PrintWriter out = event.getSuppliedResponse().getWriter();
					out.println("Servlet子线程完成时间："+ new Date() + ".");
				}

				@Override
				public void onTimeout(AsyncEvent event) throws IOException {
					PrintWriter out = event.getSuppliedResponse().getWriter();
					out.println("Servlet子线程超时时间："+ new Date() + ".");
				}

				@Override
				public void onError(AsyncEvent event) throws IOException {
					PrintWriter out = event.getSuppliedResponse().getWriter();
					out.println("Servlet子线程出错时间："+ new Date() + ".");
				}

				@Override
				public void onStartAsync(AsyncEvent event) throws IOException {
					PrintWriter out = event.getSuppliedResponse().getWriter();
					out.println("Servlet子线程开始时间："+ new Date() + ".");
				}
	        	
	        }, req, resp);
	        new Thread(new Executor(ctx)).start();
	        
	        out.println("结束Servlet的时间：" + new Date() + ".");
	        out.flush();
	}
	
	

}
