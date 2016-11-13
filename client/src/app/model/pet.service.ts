import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Rx';

import {Pet} from './pet';

@Injectable()
export class PetService {
  constructor(private http: Http) {
  }

  host: string = 'http://155.99.150.195:8080';

  testData = [
    new Pet(1, 'Rascal', '1', 'Dog', 'Cocker Spaniel', '50', ['Dave']),
    new Pet(2, 'Snickers', '2', 'Cat', 'Tabby', '25', ['Jim', 'Jill'])
  ];

  find(id: string): Observable<Pet[]> {
    let url: string = this.host + '/listings/near/' + id;
    return this.http.get(url)
      .map(this.parsePet).catch(err => Observable.of(this.testData));
  }

  getInterested(id: string): Observable<Pet[]> {
    let url: string = this.host + '/listings/interested' + id;
    return this.http.get(url)
      .map(this.parsePet).catch(err => Observable.of(this.testData));
  }

  getPostings(id: string): Observable<Pet[]> {
    let url: string = this.host + '/listings/' + id;
    return this.http.get(url)
      .map(this.parsePet).catch(err => Observable.of(this.testData));
  }

  interested(id: string, listingId: string): Observable<string> {
    let url: string = this.host + '/listings/interested/' + id + '/' + listingId;
    return this.http.post(url, {})
      .map(this.parseResult).catch(err => Observable.of(''));
  }

  postPet(id: string, pet: Pet): Observable<string> {
    let url: string = this.host + '/listing/' + id;
    return this.http.post(url, {
      'name': pet.name,
      'age': pet.age,
      'species': pet.species,
      'breed': pet.breed,
      'weight': pet.weight,
      'interested': pet.interested
    }).map(this.parseResult).catch(err => Observable.of(''));
  }

  parsePet(response: Response): Pet[] {
    let items = response.json();
    let result: Pet[] = [];

    for (let item of items)
      result.push(new Pet(item.id, item.name, item.age, item.species, item.breed, item.weight, item.interested));

    return result;
  }

  parseResult(response: Response): string {
    return response.text();
  }
}
