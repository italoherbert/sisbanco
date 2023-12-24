import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { MatCardModule } from '@angular/material/card';
import { ToolbarComponent } from '../../shared/toolbar/toolbar.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [ 
    MatCardModule,
    
    ToolbarComponent
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.sass'
})
export class HomeComponent {

  username : string | null = '';

  constructor( 
    private actRoute: ActivatedRoute, 
  ) {}

  ngOnInit() {
    this.username = this.actRoute.snapshot.paramMap.get( 'username' );
  }

}
