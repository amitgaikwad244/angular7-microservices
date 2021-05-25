import { Injector } from '@angular/core';

export class DependencyProvider {
  private static _injector: Injector;

  public static getDependency(token: any): any {
    return DependencyProvider.injector.get(token);
  }

  public static set injector(injector: Injector) {
    DependencyProvider._injector = injector;
  }

  public static get injector(): Injector {
    return DependencyProvider._injector;
  }
}
