import { Injectable } from '@angular/core';
import { ResponseMsg } from '../../model/Response';

/*
Generic alert service which will be independent of the underlying implementation
Use the isHtml parameter of all the methods with care
If the msg is received from sme external service alway keep the isHtml parameter to false
because keeping it true will make the application vulnerable to HTML injection
 */
@Injectable()
export abstract class AlertService {
  abstract success(msg: string, isHtml?: boolean): Promise<any>;
  abstract info(msg: string, isHtml?: boolean): Promise<any>;
  abstract warning(msg: string, isHtml?: boolean): Promise<any>;
  abstract error(msg: string, isHtml?: boolean): Promise<any>;
  abstract question(msg: string, isHtml?: boolean): Promise<any>;
  abstract alert(msg: ResponseMsg, isHtml?: boolean): Promise<any>;
  abstract confirm(title: string, msg: string, isHtml?: boolean): Promise<any>;
}
