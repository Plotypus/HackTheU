import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Rx';

import {Pet} from './pet';

@Injectable()
export class PetService {
  constructor(private http: Http) {
  }

  find(id: string): Observable<Pet[]> {
    let url: string = '' + id;
    return this.http.get(url)
      .map(this.parsePet).catch(err => Observable.of([
        new Pet('Rascal', '1', 'Dog', 'Cocker Spaniel', '50', ''),
        new Pet('Snickers', '2', 'Cat', 'Tabby', '25', '')
      ]));
  }

  postPet(id: string, pet: Pet): Observable<string> {
    let url: string = '' + id;

    return this.http.post(url, {
      'name': pet.name,
      'age': pet.age,
      'species': pet.species,
      'breed': pet.breed,
      'weight': pet.weight,
      'photo': pet.photo
    }).map(this.parseResult).catch(err => Observable.of(''));
  }

  parsePet(response: Response): Pet[] {
    let items = response.json();
    let result: Pet[] = [];

    for (let item of items)
      result.push(new Pet(item.name, item.age, item.species, item.breed, item.weight, item.photo));

    return result;
  }

  parseResult(response: Response): string {
    return response.text();
  }
}
