package com.demo.microservices.microservicecoursemanagement.repository;

import com.demo.microservices.microservicecoursemanagement.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
