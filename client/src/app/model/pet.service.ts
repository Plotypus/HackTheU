import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Rx';

import {Pet} from './pet';

@Injectable()
export class PetService {
  constructor(private http: Http) {
  }

  getPet(id: string): Observable<Pet> {
    let url: string = '' + id;
    return this.http.get(url)
      .map(this.parsePet)
      .retryWhen(error => error.delay(1000)).timeout(3000)
      .catch(err => Observable.of(new Pet('', '', '', '', '', '')));
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

  parsePet(response: Response): Pet {
    let items = response.json();
    return new Pet(items.name, items.age, items.species, items.breed, items.weight, items.photo);
  }

  parseResult(response: Response): string {
    return response.text();
  }
}
