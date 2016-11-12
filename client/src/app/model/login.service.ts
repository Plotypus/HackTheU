import {Injectable} from '@angular/core';
import {Http} from '@angular/http';
import {Observable} from 'rxjs/Rx';

@Injectable()
export class LoginService {
  user: string = '';

  constructor(private http: Http) {
  }

  setUser(user: string): Observable<string> {
    this.user = user;
    return Observable.of('');
  }

  login(username: string, password: string): Observable<string> {
    let url: string = '';
    return this.http.post(url, {'username': username, 'password': password})
      .map(response => response.text()).catch(err => Observable.of('1'));
  }

  logout(): Observable<string> {
    this.user = '';
    return Observable.of('');
  }
}
