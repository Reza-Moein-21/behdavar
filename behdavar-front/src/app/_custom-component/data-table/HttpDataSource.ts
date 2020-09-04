import {CollectionViewer, DataSource} from "@angular/cdk/collections";
import {BehaviorSubject, Observable, of} from "rxjs";
import {catchError, finalize, map} from "rxjs/operators";
import {PagingRequest, PagingResponse, SearchCriteria} from "./PaginationModel";
import {HttpClient} from "@angular/common/http";

export default class HttpDataSource<T> implements DataSource<T> {
  public loadingSubject = new BehaviorSubject<boolean>(false);
  public totalRecordSubject = new BehaviorSubject<number>(0);


  private subject = new BehaviorSubject<T[]>(null);
  private url: string;
  private httpClient: HttpClient;
  private filters: SearchCriteria[];
  private request: PagingRequest;

  constructor(url: string, httpClient: HttpClient, filters?: SearchCriteria[]) {
    this.url = url;
    this.httpClient = httpClient;
    this.filters = filters;
  }

  connect(collectionViewer: CollectionViewer): Observable<T[]> {
    return this.subject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.subject.complete();
  }

  public find(request: PagingRequest) {
    // start active loading
    this.loadingSubject.next(true);

    this.request = request;
    request.filters = this.filters;
    // call http service
    this.httpClient.post<PagingResponse<T>>(this.url, request)
      .pipe(catchError(err => this.handleError(err)), finalize(() => this.stopLoading()))
      .pipe(map(value => this.mapResponseToDto(value)))
      .subscribe(value => this.subject.next(value));
  }

  public reload() {
    if (this.request) {
      this.find(this.request);
    }
  }

  private stopLoading() {
    this.loadingSubject.next(false);
  }

  private mapResponseToDto(res: PagingResponse<T>) {
    if (!res)
      return [];

    this.totalRecordSubject.next(res.total);
    return res.data as T[];
  }

  private handleError(result?: T) {
    return (error: any): Observable<T> => {
      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead
      console.log(`Pagination failed: ${error.message}`);
      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

}
