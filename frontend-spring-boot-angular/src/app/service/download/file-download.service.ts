import { Injectable } from '@angular/core';
import { HttpCoreService } from '../http/http-core.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { CookieService } from 'ngx-cookie-service';

@Injectable()
export class FileDownloadService {
  constructor(private http: HttpClient, private cookieService: CookieService) {}

  public downloadFile(fileName: string) {
    if (fileName != null) {
      this.http
        .get('download/file/' + fileName, {
          responseType: 'blob',
          headers: new HttpHeaders({
            'X-XSRF-TOKEN': this.cookieService.get('XSRF-TOKEN')
          })
        })
        .subscribe(res => {
          if (window.navigator.msSaveOrOpenBlob) {
            //IE & Edge
            window.navigator.msSaveBlob(res, fileName);
          } else {
            const url = window.URL.createObjectURL(res);
            const a = document.createElement('a');
            a.setAttribute('style', 'display:none;');
            document.body.appendChild(a);
            a.href = url;
            a.download = fileName;
            a.click();
          }
        });
    } else {
      console.log(
        'Since filename is not provided. Download request will not be initiated'
      );
    }
  }
}
