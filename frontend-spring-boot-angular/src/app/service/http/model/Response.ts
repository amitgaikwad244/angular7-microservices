import { SweetAlertType } from 'sweetalert2';

export interface CompleteResponse {
  data: Response;
  validationError: ValidationError;
  msg: ResponseMsg;
}

export interface Response {
  data: Object;
}

export interface ValidationError {
  fieldErrors: FieldError[];
  actionError: string;
}

export interface FieldError {
  field: string;
  message: string;
}

export interface ResponseMsg {
  msg: string;
  msgType: ResponseMsgType;
}

export enum ResponseMsgType {
  SUCCESS = 'success',
  INFO = 'info',
  WARNING = 'warning',
  ERROR = 'error',
  QUESTION = 'question'
}

export namespace ResponseMsgType {
  export function sweetAlertType(type: ResponseMsgType): SweetAlertType {
    return ResponseMsgType[type];
  }
}
