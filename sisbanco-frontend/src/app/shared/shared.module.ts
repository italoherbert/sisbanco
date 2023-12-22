import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,

    RouterModule,
    FormsModule,
    HttpClientModule
  ],
  exports: [
    RouterModule,
    FormsModule,
    HttpClientModule
  ]
})
export class SharedModule { }
