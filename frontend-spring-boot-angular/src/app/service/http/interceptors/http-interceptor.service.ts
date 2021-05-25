import { Injectable, Inject } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpHeaders,
  HttpInterceptor,
  HttpRequest,
  HttpResponse
} from '@angular/common/http';
import { Router } from '@angular/router';
import { tap, finalize } from 'rxjs/operators';
import { NgxSpinnerService } from 'ngx-spinner';
import { environment } from '../../../../environments/environment';

import { AlertService } from '../../alert/alert-service';
import { Observable } from 'rxjs';
import { AuthenticationService } from '../../authentication.service';

@Injectable()
export class HttpInterceptorService implements HttpInterceptor {
  // counter to track if all the requests are completed before hiding the loader animation(spinner)
  private static requestCounter = 0;

  constructor(
    private alertService: AlertService,
    private router: Router,
    private spinner: NgxSpinnerService,
    private authenticationService: AuthenticationService
  ) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    this.requestStart();

     if (!req.url.startsWith('/')) {
      req = req.clone({
        url: `${environment.prependURL}${req.url}${environment.appendURL}`      
       });
     }
     
      if (this.authenticationService.isUserLoggedIn() && req.url.indexOf('authenticate') === -1) {
        req = req.clone({
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${this.authenticationService.token}`
            })
        });
    }
  
    return next.handle(req).pipe(
      tap(
        (event: HttpEvent<any>) => {},
        (err: any) => {
          if (err.status === 401 && err.error === 'INVALID_SESSION') {
            console.log('Session Expired. Please Relogin.');
            window.top.location.href =
              environment.prependURL + 'reLogin' + environment.appendURL;
          } else if (err.status === 401) {
            console.log('User is not Authorized to access "' + req.url + '"');
            this.router.navigate(['un-authorized-access']);
          } else if (err.status === 0) {
            this.alertService.error(
              'Cannot connect to server. Try again after some time.'
            );
            throw Error(err);
          }
        }
      ),
      finalize(() => {
        this.requestEnd();
      })
    );
  }

  requestStart() {
    HttpInterceptorService.requestCounter += 1;

    // For showing Spinner if the spinner is not already running
    if (HttpInterceptorService.requestCounter === 1) {
      setTimeout(() => {
        this.spinner.show();
      });
    }
  }

  requestEnd() {
    HttpInterceptorService.requestCounter -= 1;

    // For hiding Spinner when there is no pending request
    if (HttpInterceptorService.requestCounter === 0) {
      setTimeout(() => {
        this.spinner.hide();
      });
    }
  }
}
  

