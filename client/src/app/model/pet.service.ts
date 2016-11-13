import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Rx';

import {Pet} from './pet';

@Injectable()
export class PetService {
  constructor(private http: Http) {
  }

  host: string = 'http://155.99.150.195:8080';

  find(id: string): Observable<string[]> {
    let url: string = this.host + '/listings/near/' + id;
    return this.http.get(url)
      .map(this.parsePet).catch(err => Observable.of([]));
  }

  getInterested(id: string): Observable<string[]> {
    let url: string = this.host + '/listings/interested/' + id;
    return this.http.get(url)
      .map(this.parsePet).catch(err => Observable.of([]));
  }

  getPostings(id: string): Observable<string[]> {
    let url: string = this.host + '/listings/' + id;
    return this.http.get(url)
      .map(this.parsePet).catch(err => Observable.of([]));
  }

  interested(id: string, listingId: string): Observable<string> {
    let url: string = this.host + '/listings/interested/' + id + '/' + listingId;
    return this.http.post(url, {})
      .map(this.parseResult).catch(err => Observable.of(''));
  }

  postPet(id: string, pet: Pet): Observable<string> {
    let url: string = this.host + '/listing/add/' + id;
    return this.http.post(url, {
      'name': pet.name,
      'age': pet.age,
      'species': pet.species,
      'breed': pet.breed,
      'weight': pet.weight
    }).map(this.parseResult).catch(err => Observable.of(''));
  }

  parsePet(response: Response): string[] {
    let items = response.json();
    let result: string[] = [];

    for (let item of items)
      result.push(item.toString());

    return result;
  }

  parseResult(response: Response): string {
    return response.text();
  }
}
