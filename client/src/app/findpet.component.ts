import {Component} from '@angular/core';
import {LoginService} from "./model/login.service";
import {PetService} from "./model/pet.service";
import {Pet} from "./model/pet";

@Component({
  templateUrl: './view/findpet.component.html',
  styleUrls: ['./app.component.css'],
  providers: [PetService]
})
export class FindPetComponent {
  user: string;
  species: string;

  listings: Pet[];
  totalListings: Pet[];
  currentIndex: number = 0;

  constructor(private loginService: LoginService, private petService: PetService) {
    this.user = loginService.user;
  }

  find() {
    this.petService.find(this.user).subscribe(response => this.totalListings = this.listings = response);
  }

  changeSpecies(species) {
    this.listings = this.totalListings.filter(pet => pet.species == species);
  }
}
