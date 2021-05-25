import { TestBed, inject } from '@angular/core/testing';

import { HttpDataService } from './http-data-service.service';

describe('HttpDataServiceService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [HttpDataService]
    });
  });

  it('should be created', inject(
    [HttpDataService],
    (service: HttpDataService) => {
      expect(service).toBeTruthy();
    }
  ));
});
