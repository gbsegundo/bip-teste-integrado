export interface Beneficio {
  id?: number;
  nome: string;
  descricao?: string;
  valor: number;
  ativo?: boolean;
  version?: number;
  createdAt?: string;
  updatedAt?: string;
}

export interface TransferenciaRequest {
  fromId: number;
  toId: number;
  amount: number;
}

export interface ErrorResponse {
  status: number;
  message: string;
  timestamp: string;
}


