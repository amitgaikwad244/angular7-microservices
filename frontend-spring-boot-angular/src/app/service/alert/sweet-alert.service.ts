import { Injectable } from '@angular/core';
import { AlertService } from './alert-service';
import { ResponseMsg, ResponseMsgType } from '../../model/Response';
import swal, { SweetAlertOptions } from 'sweetalert2';

@Injectable()
export class SweetAlertService implements AlertService {
  constructor() {}

  success(msg: string, isHtml: boolean = false): Promise<any> {
    return this.alertMsg(msg, 'success', isHtml);
  }
  info(msg: string, isHtml: boolean = false): Promise<any> {
    return this.alertMsg(msg, 'info', isHtml);
  }
  warning(msg: string, isHtml: boolean = false): Promise<any> {
    return this.alertMsg(msg, 'warning', isHtml);
  }
  error(msg: string, isHtml: boolean = false): Promise<any> {
    return this.alertMsg(msg, 'error', isHtml);
  }
  question(msg: string, isHtml: boolean = false): Promise<any> {
    return this.alertMsg(msg, 'question', isHtml);
  }
  alert(msg: ResponseMsg, isHtml: boolean = false): Promise<any> {
    return this.alertMsg(msg.msg, msg.msgType, isHtml);
  }

  confirm(title: string, msg: string, isHtml: boolean = false) {
    const params: SweetAlertOptions = {
      title: title,
      type: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes',
      cancelButtonText: 'No'
    };
    if (isHtml) {
      params.html = msg;
    } else {
      params.text = msg;
    }
    return swal(params);
  }

  private alertMsg(
    msg: string,
    type: 'success' | 'error' | 'warning' | 'info' | 'question',
    isHtml: boolean
  ): Promise<any> {
    const params: SweetAlertOptions = {
      title: type.toUpperCase(),
      type: type
    };
    if (isHtml) {
      params.html = msg;
    } else {
      params.text = msg;
    }
    return swal(params);
  }
}
