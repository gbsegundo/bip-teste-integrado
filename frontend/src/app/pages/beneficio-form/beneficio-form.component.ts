import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { BeneficioService } from '../../services/beneficio.service';

@Component({
  selector: 'app-beneficio-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './beneficio-form.component.html',
  styleUrls: ['./beneficio-form.component.css']
})
export class BeneficioFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private beneficioService = inject(BeneficioService);

  form!: FormGroup;
  isEditMode = signal(false);
  beneficioId = signal<number | null>(null);
  loading = signal(false);
  error = signal<string | null>(null);
  success = signal<string | null>(null);

  ngOnInit() {
    this.initForm();
    
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode.set(true);
      this.beneficioId.set(+id);
      this.carregarBeneficio(+id);
    }
  }

  private initForm() {
    this.form = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      descricao: ['', Validators.maxLength(255)],
      valor: [0, [Validators.required, Validators.min(0)]],
      ativo: [true]
    });
  }

  private carregarBeneficio(id: number) {
    this.loading.set(true);
    this.error.set(null);

    this.beneficioService.buscarPorId(id).subscribe({
      next: (beneficio) => {
        this.form.patchValue(beneficio);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err);
        this.loading.set(false);
      }
    });
  }

  salvar() {
    // Limpar mensagens anteriores
    this.error.set(null);
    this.success.set(null);

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.error.set('Por favor, corrija os erros no formulário antes de continuar.');
      return;
    }

    this.loading.set(true);

    const beneficio = this.form.value;

    const request = this.isEditMode()
      ? this.beneficioService.atualizar(this.beneficioId()!, beneficio)
      : this.beneficioService.criar(beneficio);

    request.subscribe({
      next: () => {
        this.loading.set(false);
        this.success.set(`Benefício ${this.isEditMode() ? 'atualizado' : 'criado'} com sucesso!`);
        
        // Redirecionar após 2 segundos
        setTimeout(() => {
          this.router.navigate(['/beneficios']);
        }, 2000);
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
    if (field?.hasError('minlength')) {
      const minLength = field.errors?.['minlength'].requiredLength;
      return `Mínimo de ${minLength} caracteres`;
    }
    if (field?.hasError('maxlength')) {
      const maxLength = field?.errors?.['maxlength'].requiredLength;
      return `Máximo de ${maxLength} caracteres`;
    }
    if (field?.hasError('min')) {
      return 'Valor não pode ser negativo';
    }
    
    return null;
  }

  formatarValor(): string {
    const valor = this.form.get('valor')?.value || 0;
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(valor);
  }
}
