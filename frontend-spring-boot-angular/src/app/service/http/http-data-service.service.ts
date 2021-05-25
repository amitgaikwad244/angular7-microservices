import { HttpCoreService } from './http-core.service';
import { Injectable } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { Observable, Subject } from 'rxjs';
import { SnackbarService } from 'ngx-snackbar';
import {
  Response,
  CompleteResponse,
  ResponseMsgType,
  FieldError
} from './model/Response';
import { AlertService } from '../alert/alert-service';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable()
export class HttpDataService {
  constructor(
    private httpCore: HttpCoreService,
    private alertService: AlertService,
    private http: HttpClient,
    private snackbarService: SnackbarService
  ) {}

  post<T>(url: string, body: any, formObj?: FormGroup): Observable<T> {
    let resData: Subject<T> = new Subject<T>();
    this.httpCore.post(url, body).subscribe((res: CompleteResponse) => {
      if (res.msg != null) {
        this.alertService.alert(res.msg).then(() => {
          resData.complete();
        });
      }

      if (res.validationError != null) {
        if (res.validationError.actionError != null) {
          this.alertService.info(res.validationError.actionError).then(() => {
            this.handleFieldErrors(res, formObj);
          });
        } else {
          this.handleFieldErrors(res, formObj);
        }
      }

      if (res.data != null) {
        resData.next((res.data as any) as T);
      }
    });
    return resData.asObservable();
  }

  postFileRequest(
    url: string,
    body: any,
    formObj?: FormGroup
  ): Observable<Response> {
    let formdata: FormData = new FormData();
    formdata = this.appendFormData(body, formdata, null);
    return this.post(url, formdata);
  }

  postWithOptions<T>(
    url: string,
    body: any,
    requestOptions: Object
  ): Observable<Object> {
    return this.httpCore.post(url, body, requestOptions);
  }

  private handleFieldErrors(res: CompleteResponse, formObj: FormGroup) {
    if (res.validationError.fieldErrors.length > 0) {
      if (formObj != null) {
        res.validationError.fieldErrors.forEach((obj: FieldError) => {
          let control = formObj.controls[obj.field];
          if (control != null && control instanceof FormControl) {
            control.setErrors({ 'validate-server-side': obj.message });
          } else {
            this.showSnackBar(obj);
            console.log(
              'Field Error Found but Control is not found in FormObject. Hence displaying snackbar. Details:',
              obj
            );
          }
        });
      } else {
        res.validationError.fieldErrors.forEach((obj: FieldError) => {
          this.showSnackBar(obj);
        });
        console.log(
          'Field Errors Found but FormObject is null. Hence showing snackbar. Details: ',
          res.validationError.fieldErrors
        );
      }
    }
  }

  appendFormData(data: Object, formData: FormData, objkey): FormData {
    for (let key in data) {
      if (
        typeof data[key] === 'string' ||
        typeof data[key] === 'number' ||
        data[key] instanceof Date
      ) {
        formData.append(objkey != null ? objkey + '.' + key : key, data[key]);
      } else {
        if (key == 'multipart') {
          let array = data[key];
          for (let arrKey in array) {
            var objkeytoUse = objkey.replace('ARRAY', '[' + arrKey + ']');
            formData.append(
              objkey != null ? objkeytoUse + '.' + key : key,
              array[arrKey]
            );
          }
        } else {
          this.appendFormData(
            data[key],
            formData,
            objkey != null ? objkey + '.' + key : key
          );
        }
      }
    }
    return formData;
  }

  showSnackBar(obj: FieldError) {
    this.snackbarService.add({
      msg: '<strong>Info: <strong>' + obj.message,
      action: {
        text: 'x',
        color: '#B57A7F'
      }
    });
  }
}
