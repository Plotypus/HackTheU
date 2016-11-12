import {Injectable} from '@angular/core';
import {Http} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import {User} from "./user";

@Injectable()
export class LoginService {
  user: string = '';

  constructor(private http: Http) {
  }

  setUser(user: string): Observable<string> {
    this.user = user;
    return Observable.of('');
  }

  register(user: User): Observable<string> {
    let url: string = '';
    return this.http.post(url, {
      'username': user.username,
      'password': user.password,
      'location': user.location
    }).map(response => 'User registration succeeded')
      .catch(err => Observable.of('User registration failed'));
  }

  login(username: string, password: string): Observable<string> {
    let url: string = '';
    return this.http.post(url, {'username': username, 'password': password})
      .map(response => response.text()).catch(err => Observable.of(''));
  }

  logout(): Observable<string> {
    this.user = '';
    return Observable.of('');
  }
}
