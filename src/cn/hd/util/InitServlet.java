package cn.hd.util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

public class InitServlet extends HttpServlet{
	protected Logger  log = Logger.getLogger(getClass()); 
	
	public void init() throws ServletException {

		log.info("wx shop init successful!");
	}
}
