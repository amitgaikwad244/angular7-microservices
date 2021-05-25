import { Directive, ElementRef, OnInit, OnDestroy, Input } from '@angular/core';
import { NgModel } from '@angular/forms';
import { TooltipDirective } from 'ngx-bootstrap/tooltip';
import { Subscription } from 'rxjs';

import { ValidationMessages, MessageUtils } from './validation.utils';

@Directive({
  selector: '[app-validated][ngModel]',
  providers: [NgModel, TooltipDirective],
  host: {
    '(mouseenter)': 'inputFocusIn()',
    '(mouseleave)': 'inputFocusOut()'
  }
})
export class ValidatedDirective implements OnInit, OnDestroy {
  /* For Custom messages
		add property validation-msg
		with required messages in this format
		'{"required":"Message"}'
		Complete tag will be
		<input app-validated validation-msg='{"required":"Message"}' required type="text">
	*/
  @Input('validation-msg')
  private messages: string;

  private messageObj: Object;

  private oldTooltipText;

  private formSubscription: Subscription;

  private statusChangeSubscription: Subscription;

  constructor(
    private el: ElementRef,
    private ngModel: NgModel,
    private toolTip: TooltipDirective
  ) {}

  showError(el: ElementRef, toolTip: TooltipDirective, ngModel: NgModel) {
    el.nativeElement.classList.add('is-invalid');
    toolTip.containerClass = 'rejectionTooltip';
    toolTip.tooltip = this.fetchValidationMessage(ngModel.errors);

    if (toolTip.isOpen) {
      toolTip.hide();
      setTimeout(() => {
        toolTip.show();
      }, 200);
    } else {
      toolTip.show();
    }
  }

  ngOnInit() {
    //Capturing original tooltip text so as to replace to original
    //when there is no validation Error
    this.oldTooltipText = this.toolTip.tooltip;

    //For custom validation messages if provided by user using
    //'validation-msg' attribute in input tag
    if (this.messages != null) this.messageObj = JSON.parse(this.messages);
    else this.messageObj = {};

    //Subscription in order to handle form submission
    this.formSubscription = this.ngModel.formDirective.ngSubmit.subscribe(
      $event => {
        if (this.ngModel.invalid) {
          this.showError(this.el, this.toolTip, this.ngModel);
        }
      }
    );

    this.statusChangeSubscription = this.ngModel.statusChanges.subscribe(
      status => {
        if (this.ngModel.dirty && this.ngModel.invalid) {
          this.showError(this.el, this.toolTip, this.ngModel);
        } else if (this.ngModel.valid) {
          this.el.nativeElement.classList.remove('is-invalid');
          this.toolTip.containerClass = '';
          this.toolTip.tooltip = this.oldTooltipText;
          this.toolTip.hide();
        }
      }
    );
  }

  ngOnDestroy() {
    this.formSubscription.unsubscribe();
    this.statusChangeSubscription.unsubscribe();
  }

  inputFocusOut() {
    this.toolTip.hide();
  }
  inputFocusIn() {
    this.toolTip.show();
  }

  fetchValidationMessage(errors) {
    if (errors.required) {
      return this.messageObj['required'] == null
        ? ValidationMessages.required
        : this.messageObj['required'];
    } else if (errors.minnumber) {
      return MessageUtils.interpolate(ValidationMessages.minnumber, {
        min: errors.minnumber.minvalue
      });
    } else if (errors.maxnumber) {
      return MessageUtils.interpolate(ValidationMessages.maxnumber, {
        max: errors.maxnumber.maxvalue
      });
    } else if (errors.minlength) {
      return MessageUtils.interpolate(ValidationMessages.minlength, {
        length: errors.minlength.requiredLength
      });
    } else if (errors.maxlength) {
      return MessageUtils.interpolate(ValidationMessages.maxlength, {
        length: errors.maxlength.requiredLength
      });
    } else if (errors.greaterThan) {
      if (errors.greaterThan.minInclusive)
        return MessageUtils.interpolate(
          ValidationMessages.greaterThanInclusive,
          { minValue: errors.greaterThan.minValue }
        );
      else
        return MessageUtils.interpolate(ValidationMessages.greaterThan, {
          minValue: errors.greaterThan.minValue
        });
    } else if (errors['validate-server-side']) {
      return errors['validate-server-side'];
    } else {
      console.log(
        'ValidatedDirective.fetchValidationMessage(): No error match found. Returning the received object as it is: ',
        errors
      );
      return errors;
    }
  }
}
