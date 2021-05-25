package com.demo.microservices.microservicecoursemanagement.common.logs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.demo.microservices.microservicecoursemanagement.model.SessionAttributes;

@Service
public class CustomLogger {
	@Autowired
    private Tracer t;
	
	private static final String LINE_SEPERATOR = "-------------------------------------------------------------------------------";
	private final String mainPackageName = this.getClass().getPackage().getName().substring(0, this.getClass().getPackage().getName().indexOf('.'));
	private final ObjectMapper mapper = new ObjectMapper();
	private final Configuration config = ((LoggerContext) LogManager.getContext(false)).getConfiguration();
	 
	public String logException(Exception e) {
		return logException(null, e, null, null);
	}

	public String logException(List<Throwable> exceptionList) {
		return logException(null, exceptionList, null, null);
	}

	public String logException(JoinPoint joinPoint, Exception e, SessionAttributes sessionAttrs, HttpServletRequest request) {
		return logException(joinPoint, Arrays.asList(e), sessionAttrs, request);
	}

	public String logException(JoinPoint joinPoint, List<Throwable> e, SessionAttributes sessionAttrs, HttpServletRequest request) {
		// SET LOG FILE NAME TO USE
		String logFileName = DateFormatUtils.format(System.currentTimeMillis(), "dd-MMM-yyyy") + "_" + t.currentSpan().context().traceId()+"-"+t.currentSpan().context().spanId();
		ThreadContext.clearAll();
		ThreadContext.put("logFileName", logFileName);

		// GET LOGGER CLASS which has thrown the exception
		Logger logger = LogManager.getLogger(this);
		logger.error("Tracer - {}", t.toString());
		logger.error("Exception Report - {}", logFileName);
		logger.error("Exception Occurred On: {}", new Date());
		logger.error("Exception Service Name - {}" );
		logger.error(LINE_SEPERATOR);
		logger.error(Thread.currentThread());
		logger.error("Exception Message: {}", e.get(0).getMessage());
		logger.error("Exception Cause: {}", e.get(0).getCause());
		logger.error(LINE_SEPERATOR);

		logger.error("JVM Details");
		logger.error(LINE_SEPERATOR);
		Runtime rt = Runtime.getRuntime();
		logger.error("AvailableProcessors: {}", rt.availableProcessors());
		logger.error("TotalMemory: {}", rt.totalMemory() / 1024 / 1024 + "MB");
		logger.error("FreeMemory: {}", rt.freeMemory() / 1024 / 1024 + "MB");
		logger.error("MaxMemory: {}", rt.maxMemory() / 1024 / 1024 + "MB");
		logger.error(LINE_SEPERATOR);

		if (sessionAttrs != null) {
			logger.error("User Details");
			logger.error(LINE_SEPERATOR);
			logger.error("User Name: {}", sessionAttrs.getUserName());
			logger.error("User Id: {}", sessionAttrs.getUserId());
			printBrowserDetails(request, logger);
			logger.error(LINE_SEPERATOR);
		}
		if (request != null) {
			logger.error("Request Details");
			logger.error(LINE_SEPERATOR);
			logger.error("URL: {}", request.getRequestURL());
			StringBuffer headers = new StringBuffer("");
			Enumeration<String> headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String currElement = headerNames.nextElement();
				headers.append("," + currElement + ":" + request.getHeader(currElement));
			}
			logger.error("Request Headers: {{}}", headers.toString().substring(1));
			logger.error("Method: {}", request.getMethod());
			if (joinPoint != null) {
				logger.error("Request Parameters: [");
				for (Object obj : joinPoint.getArgs()) {
					try {
						logger.error(mapper.writeValueAsString(obj));
					} catch (JsonProcessingException e1) {
						logger.error(obj);
					}
				}
				logger.error("]");
			}
			logger.error(LINE_SEPERATOR);
		}
		logger.error("Exception Details");
		logger.error(LINE_SEPERATOR);

		for (Throwable exception : e) {
			for (StackTraceElement s : exception.getStackTrace()) {
				if (s.getClassName().startsWith(mainPackageName)) {
					logger.error(s);
				}
			}
			logger.error("");
		}

		logger.error(LINE_SEPERATOR);
		logger.error("Complete StackTrace");
		logger.error(LINE_SEPERATOR);
		for (Throwable throwable : e) {
			logger.error("", throwable);
		}
		logger.error(LINE_SEPERATOR);

		
		// ROLL LOGS FROM DEFAULT FILE TO CURRENT LOG FILE 'logFileName'
	RollingFileAppender r = config.getAppender("exceptionFileAppender");
	logger.error(r.toString());
	r.getManager().rollover();
		return logFileName;
	}

	private void printBrowserDetails(HttpServletRequest request, Logger logger) {

		String browserDetails = request.getHeader("User-Agent");
		String userAgent = browserDetails;
		String user = userAgent.toLowerCase();

		String os = "";
		String browser = "";

		// =================OS=======================
		if (userAgent.toLowerCase().indexOf("windows") >= 0) {
			os = "Windows";
		} else if (userAgent.toLowerCase().indexOf("mac") >= 0) {
			os = "Mac";
		} else if (userAgent.toLowerCase().indexOf("x11") >= 0) {
			os = "Unix/Linux";
		} else if (userAgent.toLowerCase().indexOf("android") >= 0) {
			os = "Android";
		} else if (userAgent.toLowerCase().indexOf("iphone") >= 0) {
			os = "IPhone";
		} else {
			os = "UnKnown, More-Info: " + userAgent;
		}
		// ===============Browser===========================
		if (user.contains("msie")) {
			String substring = userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
			browser = substring.split(" ")[0].replace("MSIE", "IE") + "-" + substring.split(" ")[1];
		} else if (user.contains("safari") && user.contains("version")) {
			browser = (userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0] + "-" + (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
		} else if (user.contains("opr") || user.contains("opera")) {
			if (user.contains("opera"))
				browser = (userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0] + "-" + (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
			else if (user.contains("opr"))
				browser = ((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera");
		} else if (user.contains("chrome")) {
			browser = (userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
		} else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1) || (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1) || (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1)) {
			browser = "Netscape-?";

		} else if (user.contains("firefox")) {
			browser = (userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
		} else if (user.contains("rv")) {
			browser = "IE";
		} else {
			browser = "UnKnown, More-Info: " + userAgent;
		}

		logger.error("User Agent: " + browserDetails);
		logger.error("Operating System: " + os);
		logger.error("Browser Name: " + browser);
	}

}
