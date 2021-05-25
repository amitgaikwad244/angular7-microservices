import { Directive, Input } from '@angular/core';
import {
  NG_VALIDATORS,
  Validator,
  FormControl,
  ValidationErrors
} from '@angular/forms';

@Directive({
  selector: '[greater-than][ngModel]',
  providers: [
    { provide: NG_VALIDATORS, useExisting: GreaterThanDirective, multi: true }
  ]
})
export class GreaterThanDirective implements Validator {
  @Input('greater-than') min: number;

  @Input('greater-than-inclusive') minInclusive: boolean = false;

  constructor() {}

  validate(c: FormControl): ValidationErrors {
    if (c.value != null) {
      let result = c.value > this.min;

      if (this.minInclusive) result = c.value >= this.min;

      return result
        ? null
        : {
            greaterThan: {
              actualvalue: c.value,
              minValue: this.min,
              minInclusive: this.minInclusive
            }
          };
    } else return null;
  }
}
