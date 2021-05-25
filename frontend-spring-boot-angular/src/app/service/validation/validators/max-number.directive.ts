import { Directive, Input } from '@angular/core';
import {
  NG_VALIDATORS,
  Validator,
  FormControl,
  ValidationErrors
} from '@angular/forms';

@Directive({
  selector: '[maxnumber][ngModel]',
  providers: [
    { provide: NG_VALIDATORS, useExisting: MaxNumberDirective, multi: true }
  ]
})
export class MaxNumberDirective implements Validator {
  @Input()
  maxnumber: number;

  constructor() {}

  validate(c: FormControl): ValidationErrors {
    return c.value > this.maxnumber
      ? { maxnumber: { actualvalue: c.value, maxvalue: this.maxnumber } }
      : null;
  }
}
