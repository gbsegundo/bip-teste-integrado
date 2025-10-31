import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { BeneficioService } from '../../services/beneficio.service';
import { Beneficio } from '../../models/beneficio.model';

@Component({
  selector: 'app-transferencia',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './transferencia.component.html',
  styleUrls: ['./transferencia.component.css']
})
export class TransferenciaComponent implements OnInit {
  private fb = inject(FormBuilder);
  private beneficioService = inject(BeneficioService);

  form!: FormGroup;
  beneficios = signal<Beneficio[]>([]);
  loading = signal(false);
  loadingBeneficios = signal(false);
  error = signal<string | null>(null);
  success = signal<string | null>(null);
  selectedFrom = signal<Beneficio | null>(null);
  selectedTo = signal<Beneficio | null>(null);

  ngOnInit() {
    this.initForm();
    this.carregarBeneficios();
  }

  private initForm() {
    this.form = this.fb.group({
      fromId: ['', Validators.required],
      toId: ['', Validators.required],
      amount: [0, [Validators.required, Validators.min(0.01)]]
    });

    // Atualizar seleções quando os campos mudarem
    this.form.get('fromId')?.valueChanges.subscribe(id => {
      this.selectedFrom.set(this.beneficios().find(b => b.id === +id) || null);
    });

    this.form.get('toId')?.valueChanges.subscribe(id => {
      this.selectedTo.set(this.beneficios().find(b => b.id === +id) || null);
    });
  }

  private carregarBeneficios() {
    this.loadingBeneficios.set(true);
    
    this.beneficioService.listarAtivos().subscribe({
      next: (data) => {
        this.beneficios.set(data);
        this.loadingBeneficios.set(false);
      },
      error: (err) => {
        this.error.set(err);
        this.loadingBeneficios.set(false);
      }
    });
  }

  transferir() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const { fromId, toId, amount } = this.form.value;

    if (fromId === toId) {
      this.error.set('Origem e destino não podem ser iguais!');
      return;
    }

    this.loading.set(true);
    this.error.set(null);
    this.success.set(null);

    this.beneficioService.transferir({
      fromId: +fromId,
      toId: +toId,
      amount: +amount
    }).subscribe({
      next: () => {
        this.success.set(`✅ Transferência de ${this.formatarValor(amount)} realizada com sucesso!`);
        this.form.reset();
        this.selectedFrom.set(null);
        this.selectedTo.set(null);
        this.carregarBeneficios();
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err);
        this.loading.set(false);
      }
    });
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.form.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched));
  }

  getFieldError(fieldName: string): string | null {
    const field = this.form.get(fieldName);
    
    if (field?.hasError('required')) {
      return 'Este campo é obrigatório';
    }
    if (field?.hasError('min')) {
      return 'Valor deve ser maior que zero';
    }
    
    return null;
  }

  formatarValor(valor: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(valor);
  }

  calcularSaldoAposTransferencia(beneficio: Beneficio | null, tipo: 'from' | 'to'): number {
    if (!beneficio) return 0;
    
    const amount = this.form.get('amount')?.value || 0;
    
    if (tipo === 'from') {
      return beneficio.valor - amount;
    } else {
      return beneficio.valor + amount;
    }
  }
}


