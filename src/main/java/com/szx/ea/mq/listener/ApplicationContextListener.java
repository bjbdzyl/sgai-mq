package com.szx.ea.mq.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.szx.core.utils.AppServiceHelper;

@WebListener
public class ApplicationContextListener implements ServletContextListener {
	
	private static final Log logger = LogFactory.getLog(ApplicationContextListener.class);

    public void contextInitialized(ServletContextEvent event) {
    	try {
			logger.info("开始加载系统参数...");
			
			ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
        	logger.info(" - Put Spring Context to AppServiceHelper");
        	AppServiceHelper.setApplicationContext(applicationContext);
        	
			logger.info("加载系统参数结束！");

        } catch (Exception e) {
        	logger.error("error detail:",e);
        }
    }

    public void contextDestroyed(ServletContextEvent arg0) {

    }
}
