import { Routes } from '@angular/router';

import { ToolbarComponent } from './shared/toolbar/toolbar.component';

import { LoginComponent } from './features/login/login.component';
import { HomeComponent } from './features/home/home.component';

export const routes: Routes = [
    { path: '', redirectTo: '/login', pathMatch: 'full' },

    { path: 'login', component: LoginComponent },
    { path: 'home', component: HomeComponent }
];
