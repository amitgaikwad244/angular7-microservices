import { Directive, Input } from '@angular/core';
import {
  NG_VALIDATORS,
  Validator,
  FormControl,
  ValidationErrors
} from '@angular/forms';

@Directive({
  selector: '[minnumber][ngModel]',
  providers: [
    { provide: NG_VALIDATORS, useExisting: MinNumberDirective, multi: true }
  ]
})
export class MinNumberDirective implements Validator {
  @Input()
  minnumber: number;

  constructor() {}

  validate(c: FormControl): ValidationErrors {
    return c.value < this.minnumber
      ? { minnumber: { actualvalue: c.value, minvalue: this.minnumber } }
      : null;
  }
}
