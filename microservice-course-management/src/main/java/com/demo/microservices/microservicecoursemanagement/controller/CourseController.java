package com.demo.microservices.microservicecoursemanagement.controller;

import com.demo.microservices.microservicecoursemanagement.intercomm.UserClient;
import com.demo.microservices.microservicecoursemanagement.model.Transaction;
import com.demo.microservices.microservicecoursemanagement.model.User;
import com.demo.microservices.microservicecoursemanagement.model.response.ResponseMsgType;
import com.demo.microservices.microservicecoursemanagement.model.response.ResponseObj;
import com.demo.microservices.microservicecoursemanagement.service.CourseService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class CourseController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    private UserClient userClient;

    @Autowired
    private CourseService courseService;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private Environment env;
    
    @Autowired    
    private ResponseObj res;
    
    
    @Value("${spring.application.name}")
    private String serviceId;

    @GetMapping("/service/port")
    public String getPort(){
        return "Service is working at port : " + env.getProperty("local.server.port");
    }

    @GetMapping("/service/instances")
    public ResponseEntity<?> getInstances() {
        return ResponseEntity.ok(discoveryClient.getInstances(serviceId));
    }

    @GetMapping("/service/user/{userId}")
    public ResponseEntity<?> findTransactionsOfUser(@PathVariable Long userId){
        return ResponseEntity.ok(courseService.findTransactionsOfUser(userId));
    }

    @GetMapping("/service/all")
    public ResponseEntity<?> findAllCourses(){
        return ResponseEntity.ok(courseService.allCourses());
    }

    @PostMapping("/service/enroll")
    public ResponseEntity<?> saveTransaction(@RequestBody Transaction transaction) {
        transaction.setDateOfIssue(LocalDateTime.now());
        transaction.setCourse(courseService.findCourseById(transaction.getCourse().getId()));
        return new ResponseEntity<>(courseService.saveTransaction(transaction), HttpStatus.CREATED);
    }

    @GetMapping("/service/course/{courseId}")
    public ResponseEntity<?> findStudentsOfCourse(@PathVariable Long courseId){
        List<Transaction> transactions = courseService.findTransactionsOfCourse(courseId);
        if(CollectionUtils.isEmpty(transactions)){
           return ResponseEntity.notFound().build();
        }
        List<Long> userIdList = transactions.parallelStream().map(t -> t.getUserId()).collect(Collectors.toList());
        List<String> students = userClient.getUserNames(userIdList);
        return ResponseEntity.ok(students);
    }
    
    @PostMapping("/service/restCall")
    public ResponseObj restTemplateCall(@RequestBody User usermodel) {
    	
    	//RestTemplate template = new RestTemplate();
    	
    	
//    	HttpHeaders headers = new HttpHeaders();
//    	headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//    	headers.setContentType(MediaType.APPLICATION_JSON);
//
//    	HttpEntity<User> requestEntity = 
//    	     new HttpEntity<>(reqmodel, headers);
//    	
//    	ResponseEntity<ResponseObj> response = 
//    	   template.exchange("url", HttpMethod.POST, requestEntity, 
//    			   ResponseObj.class);
    	logger.info("before rest call");
    	ResponseObj response=userClient.addUser(usermodel);
    	logger.info("Response Data {} ====",response);
    	Map<String,Object> finalRes=ckeckResponseObj(response);
    	
    	
    	//res.addData("userList", reqmodel);
        return response;
    }
    
    public  Map<String,Object> ckeckResponseObj(ResponseObj completeResponse) {
    	Map<String,Object> resMapData =null;
//    	 if (completeResponse.getMsg().getMsgType() !=ResponseMsgType.SUCCESS) {
//    	      
//    	      }

    	      if (completeResponse.getValidationError() != null) {
    	       
    	      }

    	      if (completeResponse.getData() != null) {
    	    resMapData =completeResponse.getData();
    	    	 logger.info("Response map Data list{} ====",resMapData);
    	      }
    	      
    	      return resMapData;
    	      
    }
    
}
