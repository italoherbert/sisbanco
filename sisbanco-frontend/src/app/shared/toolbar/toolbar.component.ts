import { Component, ElementRef, ViewChild } from '@angular/core';
import { Router, RouterModule } from '@angular/router';

import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';

import { CommonModule } from '@angular/common';

import { LoginService } from '../../core/services/login.service';

@Component({
  selector: 'app-toolbar',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,

    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule
  ],
  templateUrl: './toolbar.component.html',
  styleUrl: './toolbar.component.sass'
})
export class ToolbarComponent {

  constructor( 
    private router : Router,
    private loginService : LoginService
  ) {}

  ngOnInit() {
    this.router.navigate(['/home'] );
  }

  logout() {
    this.loginService.logout();
    this.router.navigate( [ '/login' ] ); 
  }

}
