export enum ValidationMessages {
  required = 'This field is Required',
  onlyNumber = 'Only Numbers are Allowed',
  minnumber = 'Minimum value should be {min}',
  maxnumber = 'Maximum allowed value is {max}',
  minlength = 'Minimum {length} characters required',
  maxlength = 'Maximum {length} characters are allowed',
  greaterThanInclusive = 'Should be greater than or equal to {minValue}',
  greaterThan = 'Should be greater than {minValue}'
}

export enum ValidationPatterns {
  onlyNumber = '^[0-9]*$',
  onlyNumberWiththreedecimal = '^[0-9]{1,3}$',
  onlyThreeDgits = '^[0-9]{1,3}$',
  onlyEightDigits = '^[0-9]{1,8}$',

  twoDigitTwoDecimal = '[0-9]{0,2}(.[0-9]{1,2})?',
  twoDigitThreeDecimal = '[0-9]{0,2}(.[0-9]{1,3})?',
  twoDigitFourDecimal = '[0-9]{0,2}(.[0-9]{1,4})?',

  decimalNumber = '[0-9]{0,3}(.[0-9]{1,3})?',
  twoDigitEightDecimal = '[0-9]{0,2}(.[0-9]{1,8})?',
  threeDigitThreeDecimal = '[0-9]{0,3}(.[0-9]{1,3})?',
  threeDigitTwoDecimal = '[0-9]{0,3}(.[0-9]{1,2})?',
  fourDigitTwoDecimal = '[0-9]{0,4}(.[0-9]{1,2})?'
}

export class MessageUtils {
  public static interpolate(msg: string, values: {}): string {
    for (var key in values) {
      msg = msg.replace('{' + key + '}', values[key]);
    }
    return msg;
  }
}
