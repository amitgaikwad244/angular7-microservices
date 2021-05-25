package com.demo.microservices.microservicecoursemanagement.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


import com.demo.microservices.microservicecoursemanagement.utils.PropertyUtils;


@Component
public class SessionAttributes {

	
	public String getUserId() {
		return "USERID000002";
	}


	public String getUserName() {
		return "SUPERADMIN";
	}

	
	public int getBankId() {
		return 1;
	}

	
	public int getRoleId() {
		return 1;
	}

	
	public Map<String, Object> getMAPSIntegrationData() {
		Map<String, Object> data = new HashMap<>();
		data.put("uri", "template-master");
		data.put("callbackURL", "http://192.168.83.215/ABPro/MAPS/Master_Screen/MS_KeyMe_sktblkeyme.ASPX");
		data.put("logoffURL", PropertyUtils.getProperty("logoffURL"));
		return data;
	}

}
