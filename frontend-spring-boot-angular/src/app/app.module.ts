//angular dependencies
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

//additional third party dependencies
import { TooltipModule, TooltipConfig } from 'ngx-bootstrap/tooltip';
import { ModalModule } from 'ngx-bootstrap/modal';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { ButtonsModule } from 'ngx-bootstrap/buttons';
import { NgxSpinnerModule } from 'ngx-spinner';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { NgSelectModule } from '@ng-select/ng-select';
import { SnackbarModule } from 'ngx-snackbar';
import { CookieService } from 'ngx-cookie-service';

//services

// import { HttpCoreService } from './service/http/http-core.service';
// import { HttpDataService } from './service/http/http-data-service.service';

 import { AlertService } from './service/alert/alert-service';
 import { SweetAlertService } from './service/alert/sweet-alert.service';


//directives
import { ValidatedDirective } from './service/validation/validated.directive';
import { MinNumberDirective } from './service/validation/validators/min-number.directive';
import { MaxNumberDirective } from './service/validation/validators/max-number.directive';
import { GreaterThanDirective } from './service/validation/validators/greater-than.directive';


import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { AuthenticationService } from './service/authentication.service';
import { CourseService } from './service/course.service';
import { CoursesComponent } from './courses/courses.component';
import { AuthGuardService } from './service/auth-guard.service';
import { LogoutComponent } from './logout/logout.component';
import { MenuComponent } from './menu/menu.component';
import { HttpInterceptorService } from './service/http/interceptors/http-interceptor.service';
import { HttpCoreService } from './service/http/http-core.service';
import { HttpDataService } from './service/http/http-data-service.service';
import { FileDownloadService } from './service/download/file-download.service';

@NgModule({
  declarations: [
    ValidatedDirective,
    MinNumberDirective,
    MaxNumberDirective,
    GreaterThanDirective,
    AppComponent,
    LoginComponent,
    CoursesComponent,
    LogoutComponent,
    MenuComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule,
    NgxDatatableModule,
    TooltipModule.forRoot(),
    ModalModule.forRoot(),
    BsDatepickerModule.forRoot(),
    ButtonsModule.forRoot(),
    NgxSpinnerModule,
    NgSelectModule,
    SnackbarModule.forRoot()
  ],
  providers: [
    HttpCoreService,
    HttpDataService,
    FileDownloadService,
    AuthenticationService,
    CourseService,
    AuthGuardService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpInterceptorService,
      multi: true
    },
     { provide: AlertService, useClass: SweetAlertService },
    { provide: TooltipConfig, useFactory: defaultAlertConfig }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

export function defaultAlertConfig(): TooltipConfig {
  return Object.assign(new TooltipConfig(), {
    placement: 'bottom',
    container: 'body'
  });
}
