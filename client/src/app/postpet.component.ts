import {Component} from '@angular/core';

import {Pet} from './pet';

@Component({
  selector: 'post-pet',
  templateUrl: './postpet.component.html',
  styleUrls: ['./app.component.css']
})
export class PostPetComponent {
  pet: Pet = new Pet('', '', '', '', '', '');


}
