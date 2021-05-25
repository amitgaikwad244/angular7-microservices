import { Directive } from '@angular/core';
import {
  NG_VALIDATORS,
  Validator,
  FormControl,
  ValidationErrors
} from '@angular/forms';

@Directive({
  selector: '[validate-server-side]',
  providers: [
    {
      provide: NG_VALIDATORS,
      useExisting: ValidateServerSideDirective,
      multi: true
    }
  ]
})
export class ValidateServerSideDirective implements Validator {
  constructor() {}

  /*
'validate-server-side' will always be valid for client side
only time when it will be invalid is when we receive a
serverside validation fielderror.
It is set in HttpDataService.post() method
*/
  validate(c: FormControl): ValidationErrors {
    return null;
  }
}
