import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { User } from '../model/user';
import { AuthenticationService } from '../service/authentication.service';
import { HttpDataService } from '../service/http/http-data-service.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  user: User = new User();
  errorMessage :string;
  invalidLogin = false;
  loginSuccess = false;
  returnUrl: string;
  public token: String;
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpDataService,
    private authenticationService: AuthenticationService) { 

    }

  ngOnInit() {
    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/courses';
  }


   login() {
   this.http
   .post('service/authenticate', {
   username: this.user.username,
   password: this.user.password
   })
   .subscribe(
     res => {
      this.invalidLogin = false;
      this.loginSuccess = true;
      console.log('response=========',res['token']);
      this.authenticationService.token = res['token'];
      this.authenticationService.registerSuccessfulLoginForJwt(this.user.username);
      this.router.navigateByUrl('courses');
     },
     err => {
      this.invalidLogin = true;
      this.loginSuccess = false;
     },
     () => {
      
     }
   );
    }
}
