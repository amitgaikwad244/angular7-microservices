import { Page } from '../../model/CustomModels';
import { HttpDataService } from '../http/http-data-service.service';
import { Observable, Subject } from 'rxjs';

export abstract class PaginationService {
  abstract getId(row);

  constructor(
    protected http: HttpDataService,
    protected url: string,
    protected resPropertyName: string
  ) {}

  rows: any[];
  page: Page = Page.initialPage();
  additionalData: Object;

  public fetchPage<T>(pageInfo): Observable<T> {
    let obs: Subject<T> = new Subject<T>();

    this.page.pageNumber =
      pageInfo.offset != null ? pageInfo.offset : pageInfo.pageNumber;

    let finalData: any = this.page;
    if (this.additionalData != null) {
      finalData = this.additionalData;
      finalData['pageOptions'] = this.page;
    }

    this.http.post(this.url, finalData).subscribe(res => {
      this.rows = res[this.resPropertyName]['content'];
      this.page.totalElements = res[this.resPropertyName]['totalElements'];
      obs.next();
    });
    return obs.asObservable();
  }

  onSort(event) {
    this.page.sorts = event.sorts;
    this.fetchPage(this.page);
  }
}
