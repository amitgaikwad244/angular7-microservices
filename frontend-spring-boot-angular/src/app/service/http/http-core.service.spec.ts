import { TestBed, inject } from '@angular/core/testing';

import { HttpCoreService } from './http-core.service';

describe('HttpCoreService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [HttpCoreService]
    });
  });

  it('should be created', inject(
    [HttpCoreService],
    (service: HttpCoreService) => {
      expect(service).toBeTruthy();
    }
  ));
});
