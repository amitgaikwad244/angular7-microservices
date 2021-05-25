package com.demo.microservices.microservicecoursemanagement.intercomm;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.demo.microservices.microservicecoursemanagement.model.User;
import com.demo.microservices.microservicecoursemanagement.model.response.ResponseObj;

import java.util.List;

@FeignClient(name="netflix-zuul-api-gateway-server")
public interface UserClient {

    @RequestMapping(method = RequestMethod.POST, value = "/service/names", consumes = "application/json")
    List<String> getUserNames(@RequestBody List<Long> userIdList);
    
    @RequestMapping(method = RequestMethod.POST, value = "api/user/service/registration", consumes = "application/json")
    ResponseObj addUser(@RequestBody User user);
}
