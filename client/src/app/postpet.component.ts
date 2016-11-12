import {Component} from '@angular/core';
import {Router} from '@angular/router';

import {Pet} from './model/pet';
import {PetService} from './model/pet.service';
import {LoginService} from './model/login.service';

@Component({
  templateUrl: './view/postpet.component.html',
  styleUrls: ['./app.component.css'],
  providers: [PetService]
})
export class PostPetComponent {
  user: string;
  message: string;
  pet: Pet = new Pet('', '', '', '', '', '');

  constructor(private petService: PetService, private router: Router, private loginService: LoginService) {
    this.user = loginService.user;
  }

  postPet(): void {
    if (!this.pet.name || !this.pet.species)
      this.message = 'At least a name and species is required';
    else
      this.petService.postPet(this.user, this.pet).subscribe(response => this.router.navigate(['/findpet']));
  }
}
