import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NavbarComponent } from '../../navbar/navbar.component';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [RouterModule, NavbarComponent],
  templateUrl: './main-layoput.component.html',
  styleUrls: ['./main-layoput.component.css'],
})
export class MainLayoutComponent {}
