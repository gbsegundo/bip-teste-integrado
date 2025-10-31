import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { BeneficioService } from '../../services/beneficio.service';
import { Beneficio } from '../../models/beneficio.model';

// Angular Material imports
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatTableModule } from '@angular/material/table';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-beneficio-list',
  standalone: true,
  imports: [
    CommonModule, 
    RouterLink, 
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatTableModule,
    MatChipsModule,
    MatProgressSpinnerModule,
    MatTooltipModule,
    MatSnackBarModule
  ],
  templateUrl: './beneficio-list.component.html',
  styleUrls: ['./beneficio-list.component.css']
})
export class BeneficioListComponent implements OnInit {
  private beneficioService = inject(BeneficioService);
  private snackBar = inject(MatSnackBar);
  
  beneficios = signal<Beneficio[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);
  searchTerm = signal('');
  displayedColumns = ['id', 'nome', 'descricao', 'valor', 'status', 'acoes'];

  ngOnInit() {
    this.carregarBeneficios();
  }

  carregarBeneficios() {
    this.loading.set(true);
    this.error.set(null);
    
    this.beneficioService.listarTodos().subscribe({
      next: (data) => {
        this.beneficios.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err);
        this.loading.set(false);
        this.snackBar.open(err, 'Fechar', { duration: 5000 });
      }
    });
  }

  buscar() {
    const termo = this.searchTerm().trim();
    
    if (!termo) {
      this.carregarBeneficios();
      return;
    }

    this.loading.set(true);
    this.error.set(null);
    
    this.beneficioService.buscarPorNome(termo).subscribe({
      next: (data) => {
        this.beneficios.set(data);
        this.loading.set(false);
        this.snackBar.open(`${data.length} benefício(s) encontrado(s)`, 'OK', { duration: 3000 });
      },
      error: (err) => {
        this.error.set(err);
        this.loading.set(false);
        this.snackBar.open(err, 'Fechar', { duration: 5000 });
      }
    });
  }

  desativar(id: number, nome: string) {
    if (!confirm(`Deseja realmente desativar o benefício "${nome}"?`)) {
      return;
    }

    this.beneficioService.desativar(id).subscribe({
      next: () => {
        this.snackBar.open('Benefício desativado com sucesso!', 'OK', { duration: 3000 });
        this.carregarBeneficios();
      },
      error: (err) => {
        this.snackBar.open(`Erro ao desativar: ${err}`, 'Fechar', { duration: 5000 });
      }
    });
  }

  deletar(id: number, nome: string) {
    if (!confirm(`Deseja realmente EXCLUIR o benefício "${nome}"? Esta ação não pode ser desfeita!`)) {
      return;
    }

    this.beneficioService.deletar(id).subscribe({
      next: () => {
        this.snackBar.open('Benefício excluído com sucesso!', 'OK', { duration: 3000 });
        this.carregarBeneficios();
      },
      error: (err) => {
        this.snackBar.open(`Erro ao excluir: ${err}`, 'Fechar', { duration: 5000 });
      }
    });
  }

  formatarValor(valor: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(valor);
  }
}
