import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    RouterLink, 
    RouterLinkActive,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatTooltipModule
  ],
  template: `
    <mat-toolbar color="primary" class="navbar-toolbar elevation-2">
      <div class="container navbar-content">
        <div class="navbar-brand">
          <mat-icon class="brand-icon">account_balance_wallet</mat-icon>
          <span class="brand-text">Sistema de Benefícios</span>
        </div>
        
        <nav class="navbar-menu">
          <a 
            mat-button 
            routerLink="/beneficios" 
            routerLinkActive="active-link"
            matTooltip="Gerenciar benefícios"
            class="nav-button"
          >
            <mat-icon>list</mat-icon>
            <span class="hide-on-mobile">Benefícios</span>
          </a>
          
          <a 
            mat-button 
            routerLink="/transferencia" 
            routerLinkActive="active-link"
            matTooltip="Realizar transferências"
            class="nav-button"
          >
            <mat-icon>swap_horiz</mat-icon>
            <span class="hide-on-mobile">Transferência</span>
          </a>
        </nav>
      </div>
    </mat-toolbar>
  `,
  styles: [`
    .navbar-toolbar {
      position: sticky;
      top: 0;
      z-index: 1000;
      box-shadow: 0 2px 8px rgba(0,0,0,0.15) !important;
    }

    .navbar-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      width: 100%;
      max-width: 1200px;
      margin: 0 auto;
      padding: 0 16px;
    }

    .navbar-brand {
      display: flex;
      align-items: center;
      gap: 12px;
      font-size: 20px;
      font-weight: 500;
      color: white;
    }

    .brand-icon {
      font-size: 28px;
      width: 28px;
      height: 28px;
      animation: pulse 2s ease-in-out infinite;
    }

    .brand-text {
      font-weight: 500;
      letter-spacing: 0.5px;
    }

    .navbar-menu {
      display: flex;
      gap: 8px;
    }

    .nav-button {
      color: white !important;
      display: flex;
      align-items: center;
      gap: 8px;
      border-radius: 8px !important;
      padding: 0 16px !important;
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
    }

    .nav-button:hover {
      background-color: rgba(255, 255, 255, 0.15) !important;
      transform: translateY(-2px);
    }

    .nav-button.active-link {
      background-color: rgba(255, 255, 255, 0.2) !important;
      font-weight: 500;
    }

    .nav-button mat-icon {
      font-size: 24px;
      width: 24px;
      height: 24px;
    }

    @keyframes pulse {
      0%, 100% {
        transform: scale(1);
      }
      50% {
        transform: scale(1.1);
      }
    }

    @media (max-width: 599px) {
      .brand-text {
        font-size: 16px;
      }
      
      .navbar-menu {
        gap: 4px;
      }
      
      .nav-button {
        min-width: 48px;
        padding: 0 8px !important;
      }
    }
  `]
})
export class NavbarComponent {}
