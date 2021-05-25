export interface MerchantMaster {
  merchantId: string;
  merchantName: string;
  merchantCode: string;
}

export interface TemplateMaster {
  templateId: any;
  templateName: string;
}

export class AssignTemplateModel {
  constructor() {}
  srNo: number;
  template: any;
  startDate: Date;
  expiryDate: Date;
  isDefault: YesNo;
  isActive: YesNo;
  comment: string;
  isDeleted: boolean;
  tmplAssignType = 'DATEWISE';
  noOfDays: number;
}

export enum YesNo {
  YES = 'Y',
  NO = 'N'
}

export class SchemeMasterModel {
  constructor() {}
  template: any;
  schemeName: string;
  schemeColumn: string;
  schemeType: string;
  schemeTypeDescription: string;
  schemeTypeName: string;
}

export class Page {
  constructor() {}
  //The number of elements in the page
  pageSize: number = 0;
  //The total number of elements
  totalElements: number = 0;
  //The current page number
  pageNumber: number = 0;
  //Page will be sorted based on this parameter values
  sorts: Sort[];
  //Page will be filtered based on this parameter values
  filter: Filter[];

  public static initialPage(): Page {
    let p = new Page();
    p.pageNumber = 0;
    p.pageSize = 10;
    return p;
  }
}

export enum Direction {
  asc,
  desc
}

export interface Sort {
  prop: string;
  dir: Direction;
}

export interface Filter {
  prop: string;
  value: string;
}

//Added By Amit for Aggregator
/*export interface AggregatorMaster {
  merchantId: string;
  merchantName: string;
  merchantCode: string;
}*/

export class HierarchyMasterModel {
  constructor() {}
  template: any;
  hierarchyName: string;
  hierarchyColumn: string;
  hierarchyCode: string;
  hierarchyTypeDescription: string;
  hierarchyTypeName: string;
}