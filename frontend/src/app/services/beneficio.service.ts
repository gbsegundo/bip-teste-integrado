import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Beneficio, TransferenciaRequest } from '../models/beneficio.model';

@Injectable({
  providedIn: 'root'
})
export class BeneficioService {
  private http = inject(HttpClient);
  // Usando caminho relativo para aproveitar o proxy do Angular
  private apiUrl = '/api/beneficios';

  listarTodos(): Observable<Beneficio[]> {
    return this.http.get<Beneficio[]>(this.apiUrl)
      .pipe(catchError(this.handleError));
  }

  listarAtivos(): Observable<Beneficio[]> {
    return this.http.get<Beneficio[]>(`${this.apiUrl}/ativos`)
      .pipe(catchError(this.handleError));
  }

  buscarPorId(id: number): Observable<Beneficio> {
    return this.http.get<Beneficio>(`${this.apiUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  buscarPorNome(nome: string): Observable<Beneficio[]> {
    return this.http.get<Beneficio[]>(`${this.apiUrl}/buscar`, {
      params: { nome }
    }).pipe(catchError(this.handleError));
  }

  criar(beneficio: Beneficio): Observable<Beneficio> {
    return this.http.post<Beneficio>(this.apiUrl, beneficio)
      .pipe(catchError(this.handleError));
  }

  atualizar(id: number, beneficio: Beneficio): Observable<Beneficio> {
    return this.http.put<Beneficio>(`${this.apiUrl}/${id}`, beneficio)
      .pipe(catchError(this.handleError));
  }

  desativar(id: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}/desativar`, {})
      .pipe(catchError(this.handleError));
  }

  deletar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  transferir(request: TransferenciaRequest): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/transferir`, request)
      .pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Ocorreu um erro desconhecido';
    
    if (error.error instanceof ErrorEvent) {
      // Erro do cliente
      errorMessage = `Erro: ${error.error.message}`;
    } else {
      // Erro do servidor
      if (error.error?.message) {
        errorMessage = error.error.message;
      } else if (error.status === 0) {
        errorMessage = 'Não foi possível conectar ao servidor. Verifique se o backend está rodando.';
      } else {
        errorMessage = `Erro ${error.status}: ${error.message}`;
      }
    }
    
    return throwError(() => errorMessage);
  }
}


