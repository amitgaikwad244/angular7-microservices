import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthenticationService } from '../service/authentication.service';
import { CourseService } from '../service/course.service';
import { HttpDataService } from '../service/http/http-data-service.service';

@Component({
  selector: 'app-courses',
  templateUrl: './courses.component.html',
  styleUrls: ['./courses.component.css']
})
export class CoursesComponent implements OnInit {
  courses = [];

  constructor(private route: ActivatedRoute,
    private router: Router,
    private http: HttpDataService,
    private authenticationService: AuthenticationService,
    private courseService: CourseService) { }

  ngOnInit() {
    this.http
    .post('service/registration', {
      username:"test",password:"test",name:"test"
    })
    .subscribe(
      res => {
       console.log('response=========',res);
      },
      err => {
      },
      () => {
      }
    );
  }

}
