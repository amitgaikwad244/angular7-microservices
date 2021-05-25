export enum Month {
  Jan,
  Feb,
  Mar,
  Apr,
  May,
  Jun,
  Jul,
  Aug,
  Sep,
  Oct,
  Nov,
  Dec
}

export namespace Month {
  export function quater(quaterNo: number): string[] {
    let months: string[] = [];
    for (var i = (quaterNo - 1) * 3, j = 0; i < quaterNo * 3; i++, j++) {
      months[j] = Month[i];
    }
    return months;
  }
  export function halfYear(halfNo: number): string[] {
    let months: string[] = [];
    for (var i = (halfNo - 1) * 6, j = 0; i < halfNo * 6; i++, j++) {
      months[j] = Month[i];
    }
    return months;
  }
  export function fullYear(): string[] {
    let months: string[] = [];
    for (var i = 0; i < 12; i++) {
      months[i] = Month[i];
    }
    return months;
  }
}
