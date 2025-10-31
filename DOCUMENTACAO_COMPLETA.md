# 📚 Documentação Completa - Sistema de Gerenciamento de Benefícios BIP

## 📋 Índice

1. [Visão Geral do Projeto](#visão-geral-do-projeto)
2. [Arquitetura e Tecnologias](#arquitetura-e-tecnologias)
3. [Estrutura do Projeto](#estrutura-do-projeto)
4. [Backend - Spring Boot](#backend---spring-boot)
5. [Frontend - Angular](#frontend---angular)
6. [Banco de Dados](#banco-de-dados)
7. [API REST - Endpoints](#api-rest---endpoints)
8. [Funcionalidades Implementadas](#funcionalidades-implementadas)
9. [Validações e Segurança](#validações-e-segurança)
10. [Testes](#testes)
11. [Configurações](#configurações)
12. [Como Executar](#como-executar)
13. [Troubleshooting](#troubleshooting)

---

## 🎯 Visão Geral do Projeto

Sistema completo de gerenciamento de benefícios desenvolvido com arquitetura em camadas, incluindo:

- **Backend REST API** (Spring Boot 3.1.5 + Java 17)
- **Frontend Web** (Angular 17 + TypeScript 5.4)
- **Banco de Dados** (H2 para desenvolvimento / PostgreSQL para produção)
- **Testes Unitários** (JUnit 5 + Mockito)
- **Documentação API** (Swagger/OpenAPI)

### Objetivos do Sistema

- Gerenciar benefícios (criar, editar, desativar, excluir)
- Realizar transferências de valores entre benefícios
- Validar operações financeiras
- Garantir integridade dos dados através de locking
- Fornecer interface moderna e responsiva

---

## 🏗️ Arquitetura e Tecnologias

### Stack Tecnológico

#### Backend
- **Framework**: Spring Boot 3.1.5
- **Linguagem**: Java 17
- **Build Tool**: Maven 3.x
- **ORM**: Spring Data JPA / Hibernate
- **Banco de Dados**: 
  - H2 (desenvolvimento)
  - PostgreSQL (produção)
- **Validação**: Jakarta Bean Validation
- **Documentação API**: Springdoc OpenAPI 2.3.0
- **Testes**: JUnit 5, Mockito

#### Frontend
- **Framework**: Angular 17
- **Linguagem**: TypeScript 5.4
- **UI Components**: Angular Material 17
- **State Management**: Angular Signals
- **HTTP Client**: RxJS Observables
- **Build Tool**: Angular CLI

#### Banco de Dados
- **Schema**: SQL DDL (PostgreSQL/H2 compatível)
- **Migrations**: JPA DDL Auto (update/validate)

---

## 📁 Estrutura do Projeto

```
bip-teste-integrado/
│
│
├── 📂 backend-module/                    # Backend Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/backend/
│   │   │   │   ├── BackendApplication.java
│   │   │   │   ├── config/
│   │   │   │   │   └── CorsConfig.java          # Configuração CORS
│   │   │   │   ├── controller/
│   │   │   │   │   └── BeneficioController.java # REST Controllers
│   │   │   │   ├── dto/
│   │   │   │   │   └── TransferenciaRequest.java
│   │   │   │   ├── exception/
│   │   │   │   │   └── GlobalExceptionHandler.java
│   │   │   │   ├── model/
│   │   │   │   │   └── Beneficio.java           # Entidade JPA
│   │   │   │   ├── repository/
│   │   │   │   │   └── BeneficioRepository.java # Spring Data JPA
│   │   │   │   └── service/
│   │   │   │       └── BeneficioService.java    # Lógica de negócio
│   │   │   └── resources/
│   │   │       ├── application.yml              # Configuração padrão (PostgreSQL)
│   │   │       ├── application-dev.yml          # Configuração H2
│   │   │       ├── application-prod.yml         # Configuração produção
│   │   │       └── data.sql                     # Dados iniciais H2
│   │   └── test/
│   │       └── java/com/example/backend/service/
│   │           └── BeneficioServiceTest.java    # Testes unitários
│   ├── pom.xml                            # Dependências Maven
│
├── 📂 frontend/                           # Frontend Angular
│   ├── src/
│   │   ├── app/
│   │   │   ├── app.component.ts           # Componente raiz
│   │   │   ├── app.routes.ts              # Configuração de rotas
│   │   │   ├── components/
│   │   │   │   └── navbar/
│   │   │   │       └── navbar.component.ts
│   │   │   ├── interceptors/
│   │   │   │   └── error.interceptor.ts   # Interceptor de erros
│   │   │   ├── models/
│   │   │   │   └── beneficio.model.ts     # Interfaces TypeScript
│   │   │   ├── pages/
│   │   │   │   ├── beneficio-form/        # Formulário criar/editar
│   │   │   │   ├── beneficio-list/        # Lista de benefícios
│   │   │   │   └── transferencia/         # Tela de transferência
│   │   │   └── services/
│   │   │       └── beneficio.service.ts   # Serviço HTTP
│   │   ├── index.html
│   │   ├── main.ts                        # Bootstrap
│   │   └── styles.css                     # Estilos globais
│   ├── angular.json                       # Configuração Angular
│   ├── package.json                       # Dependências npm
│   ├── proxy.conf.json                    # Configuração proxy
│
├── 📂 db/                                 # Scripts SQL
│   ├── schema.sql                         # Schema do banco
│   └── seed.sql                           # Dados iniciais
│
└── 📂 ejb-module/                         # Módulo EJB (legado)
    └── src/main/java/com/example/ejb/
        ├── Beneficio.java
        └── BeneficioEjbService.java
```

---

## 🔧 Backend - Spring Boot

### Camadas da Aplicação

#### 1. **Controller Layer** (`BeneficioController`)
- Recebe requisições HTTP
- Valida entrada com `@Valid`
- Retorna respostas HTTP padronizadas
- Documentação Swagger integrada

**Endpoints principais:**
- `GET /api/beneficios` - Lista todos
- `GET /api/beneficios/ativos` - Lista ativos
- `GET /api/beneficios/{id}` - Busca por ID
- `GET /api/beneficios/buscar?nome=X` - Busca por nome
- `POST /api/beneficios` - Cria novo
- `PUT /api/beneficios/{id}` - Atualiza
- `PATCH /api/beneficios/{id}/desativar` - Desativa
- `DELETE /api/beneficios/{id}` - Remove
- `POST /api/beneficios/transferir` - Transfere valores

#### 2. **Service Layer** (`BeneficioService`)
- Contém lógica de negócio
- Gerencia transações (`@Transactional`)
- Validações de negócio
- Implementa locking para transferências

**Métodos principais:**
- `listarTodos()` - Lista todos os benefícios
- `listarAtivos()` - Lista apenas ativos
- `buscarPorId(Long id)` - Busca por ID
- `buscarPorNome(String nome)` - Busca por nome
- `criar(Beneficio)` - Cria novo benefício
- `atualizar(Long id, Beneficio)` - Atualiza existente
- `desativar(Long id)` - Soft delete
- `deletar(Long id)` - Hard delete
- `transferir(Long fromId, Long toId, BigDecimal amount)` - Transferência com validações

#### 3. **Repository Layer** (`BeneficioRepository`)
- Interface Spring Data JPA
- Métodos de consulta customizados
- Locking pessimista para transferências

**Métodos customizados:**
- `findByAtivoTrue()` - Busca benefícios ativos
- `findByIdWithLock(Long id)` - Busca com lock pessimista
- `findByNomeContainingIgnoreCase(String nome)` - Busca por nome

#### 4. **Model Layer** (`Beneficio`)
- Entidade JPA mapeada para tabela `BENEFICIO`
- Validações Bean Validation
- Lifecycle callbacks (createdAt, updatedAt)
- Versionamento para optimistic locking

**Campos:**
- `id` (BIGINT, PK, AUTO_INCREMENT)
- `nome` (VARCHAR(100), NOT NULL)
- `descricao` (VARCHAR(255))
- `valor` (DECIMAL(15,2), NOT NULL)
- `ativo` (BOOLEAN, DEFAULT TRUE)
- `version` (BIGINT, para optimistic locking)
- `createdAt` (TIMESTAMP)
- `updatedAt` (TIMESTAMP)

---

## 🎨 Frontend - Angular

### Arquitetura Frontend

#### 1. **Components (Standalone)**
- `BeneficioListComponent` - Listagem e busca
- `BeneficioFormComponent` - Criação e edição
- `TransferenciaComponent` - Transferência de valores
- `NavbarComponent` - Barra de navegação

#### 2. **Services**
- `BeneficioService` - Comunicação HTTP com backend
  - Métodos: `listarTodos()`, `listarAtivos()`, `buscarPorId()`, `buscarPorNome()`, `criar()`, `atualizar()`, `desativar()`, `deletar()`, `transferir()`

#### 3. **Interceptors**
- `ErrorInterceptor` - Tratamento global de erros HTTP

#### 4. **Models**
- `Beneficio` - Interface do benefício
- `TransferenciaRequest` - Interface da requisição de transferência
- `ErrorResponse` - Interface de resposta de erro

### Funcionalidades da Interface

#### Página de Listagem
- ✅ Lista todos os benefícios
- ✅ Busca por nome em tempo real
- ✅ Formatação de valores em R$
- ✅ Status visual (ativo/inativo)
- ✅ Ações: Editar, Desativar, Excluir
- ✅ Feedback visual (loading, erros)
- ✅ Design Material responsivo

#### Página de Formulário
- ✅ Criar novo benefício
- ✅ Editar benefício existente
- ✅ Validações em tempo real
- ✅ Mensagens de erro contextuais
- ✅ Formatação de valores
- ✅ Redirecionamento após salvar

#### Página de Transferência
- ✅ Seleção de origem e destino
- ✅ Validação de saldo suficiente
- ✅ Preview de saldos após transferência
- ✅ Cálculo automático
- ✅ Validações numa formulário
- ✅ Feedback de sucesso/erro

---

## 🗄️ Banco de Dados

### Schema

```sql
CREATE TABLE BENEFICIO (
  ID BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  NOME VARCHAR(100) NOT NULL,
  DESCRICAO VARCHAR(255),
  VALOR DECIMAL(15,2) NOT NULL,
  ATIVO BOOLEAN DEFAULT TRUE,
  VERSION BIGINT DEFAULT 0
);
```

### Dados Iniciais

```sql
INSERT INTO BENEFICIO (NOME, DESCRICAO, VALOR, ATIVO, VERSION) VALUES
('Vale Alimentação', 'Benefício de vale alimentação mensal', 500.00, true, 0),
('Vale Transporte', 'Benefício de vale transporte mensal', 200.00, true, 0),
('Plano de Saúde', 'Plano de saúde empresarial', 350.00, true, 0),
('Vale Refeição', 'Vale refeição diário', 25.00, true, 0),
('Auxílio Educação', 'Auxílio para cursos e treinamentos', 1000.00, true, 0);
```

### Configurações

#### Desenvolvimento (H2)
- Banco em memória
- DDL Auto: `create-drop`
- Console H2 habilitado em `/h2-console`

#### Produção (PostgreSQL)
- DDL Auto: `update` ou `validate`
- Configuração via variáveis de ambiente
- Pool de conexões HikariCP

---

## 🌐 API REST - Endpoints

### Base URL
```
http://localhost:8080/api/beneficios
```

### Documentação Swagger
```
http://localhost:8080/swagger-ui.html
```

### Endpoints Detalhados

#### 1. Listar Todos os Benefícios
```http
GET /api/beneficios
```

**Resposta 200:**
```json
[
  {
    "id": 1,
    "nome": "Vale Alimentação",
    "descricao": "Benefício de vale alimentação mensal",
    "valor": 500.00,
    "ativo": true,
    "version": 0,
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  }
]
```

#### 2. Listar Benefícios Ativos
```http
GET /api/beneficios/ativos
```

#### 3. Buscar por ID
```http
GET /api/beneficios/{id}
```

**Resposta 404:**
```json
{
  "status": 400,
  "message": "Benefício não encontrado com ID: 999",
  "timestamp": "2024-01-01T10:00:00"
}
```

#### 4. Buscar por Nome
```http
GET /api/beneficios/buscar?nome=Vale
```

#### 5. Criar Benefício
```http
POST /api/beneficios
Content-Type: application/json
```

**Body:**
```json
{
  "nome": "Novo Benefício",
  "descricao": "Descrição do benefício",
  "valor": 1000.00,
  "ativo": true
}
```

**Resposta 201:**
```json
{
  "id": 6,
  "nome": "Novo Benefício",
  "descricao": "Descrição do benefício",
  "valor": 1000.00,
  "ativo": true,
  "version": 0,
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

#### 6. Atualizar Benefício
```http
PUT /api/beneficios/{id}
Content-Type: application/json
```

**Body:**
```json
{
  "nome": "Benefício Atualizado",
  "descricao": "Nova descrição",
  "valor": 1500.00,
  "ativo": true
}
```

#### 7. Desativar Benefício
```http
PATCH /api/beneficios/{id}/desativar
```

**Resposta 204:** No Content

#### 8. Excluir Benefício
```http
DELETE /api/beneficios/{id}
```

**Resposta 204:** No Content

#### 9. Transferir Valores
```http
POST /api/beneficios/transferir
Content-Type: application/json
```

**Body:**
```json
{
  "fromId": 1,
  "toId": 2,
  "amount": 100.00
}
```

**Validações:**
- ✅ IDs não podem ser nulos
- ✅ Valor deve ser positivo
- ✅ Origem e destino devem ser diferentes
- ✅ Ambos devem estar ativos
- ✅ Saldo suficiente na origem

**Resposta 200:** Sucesso

**Resposta 409 (Conflict):**
```json
{
  "status": 409,
  "message": "Saldo insuficiente. Saldo atual: 500.00, Valor da transferência: 1000.00",
  "timestamp": "2024-01-01T10:00:00"
}
```

---

## ✅ Funcionalidades Implementadas

### Backend

#### ✅ CRUD Completo
- [x] Criar benefício
- [x] Listar todos os benefícios
- [x] Listar apenas ativos
- [x] Buscar por ID
- [x] Buscar por nome (case-insensitive)
- [x] Atualizar benefício
- [x] Desativar benefício (soft delete)
- [x] Excluir benefício (hard delete)

#### ✅ Transferência de Valores
- [x] Transferência entre benefícios
- [x] Validação de saldo suficiente
- [x] Validação de benefícios ativos
- [x] Locking pessimista (evita race conditions)
- [x] Deadlock prevention (ordem consistente de locks)
- [x] Transações ACID

#### ✅ Validações
- [x] Validação de campos obrigatórios
- [x] Validação de tipos de dados
- [x] Validação de regras de negócio
- [x] Validação de valores positivos
- [x] Validação de tamanhos de campos

#### ✅ Segurança e Integridade
- [x] Pessimistic locking para transferências
- [x] Optimistic locking (@Version)
- [x] Deadlock prevention
- [x] Validação de estado
- [x] Tratamento de exceções global

#### ✅ Documentação
- [x] Swagger/OpenAPI integrado
- [x] Documentação de endpoints
- [x] Exemplos de requisições/respostas

### Frontend

#### ✅ Gerenciamento de Benefícios
- [x] Listagem com paginação visual
- [x] Busca em tempo real
- [x] Formulário de criação
- [x] Formulário de edição
- [x] Desativação de benefícios
- [x] Exclusão de benefícios
- [x] Validação de formulários

#### ✅ Transferência de Valores
- [x] Interface de transferência
- [x] Seleção de origem/destino
- [x] Preview de saldos
- [x] Validação de saldo
- [x] Cálculo automático
- [x] Feedback visual

#### ✅ Interface do Usuário
- [x] Design Material Design
- [x] Responsividade
- [x] Animações e transições
- [x] Feedback de loading
- [x] Mensagens de erro/sucesso
- [x] Formatação de valores (R$)
- [x] Navegação intuitiva

#### ✅ Tratamento de Erros
- [x] Interceptor de erros HTTP
- [x] Mensagens amigáveis
- [x] Retry em caso de falha
- [x] Validação client-side

---

## 🔒 Validações e Segurança

### Validações Backend

#### Validações de Entrada (Bean Validation)
- `@NotBlank` - Nome obrigatório
- `@NotNull` - Valor obrigatório
- `@PositiveOrZero` - Valor >= 0
- `@Positive` - Valor > 0 (transferência)
- `@Size` - Tamanho máximo de campos

#### Validações de Negócio
1. **Transferência:**
   - IDs não nulos
   - Valor positivo
   - Origem ≠ Destino
   - Benefícios ativos
   - Saldo suficiente

2. **Criação:**
   - ID deve ser nulo
   - Campos obrigatórios preenchidos

3. **Atualização:**
   - ID deve existir
   - Valores válidos

### Locking e Concorrência

#### Pessimistic Locking
```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT b FROM Beneficio b WHERE b.id = :id")
Optional<Beneficio> findByIdWithLock(@Param("id") Long id);
```

- Usado em transferências
- Bloqueia registro durante operação
- Previne lost updates
- Deadlock prevention via ordem consistente

#### Optimistic Locking
```java
@Version
@Column(name = "VERSION")
private Long version;
```

- Versão incrementada a cada atualização
- Detecta conflitos de concorrência
- Útil para operações de leitura frequente

### CORS Configuration

```java
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
```

- Permite requisições do frontend
- Configuração global via `CorsConfig`
- Suporte a credenciais

---

## 🧪 Testes

### Testes Implementados

#### `BeneficioServiceTest`

**Testes de Transferência:**
- ✅ `testTransferirComSucesso()` - Transferência válida
- ✅ `testTransferirComSaldoInsuficiente()` - Validação de saldo
- ✅ `testTransferirComValorNegativo()` - Validação de valor
- ✅ `testTransferirParaMesmoId()` - Validação de IDs diferentes
- ✅ `testTransferirComBeneficioInativo()` - Validação de estado

**Testes de CRUD:**
- ✅ `testCriarBeneficio()` - Criação válida
- ✅ `testBuscarPorId()` - Busca por ID
- ✅ `testBuscarPorIdNaoEncontrado()` - Tratamento de erro

**Cobertura:**
- Métodos principais testados
- Casos de sucesso e erro
- Validações de negócio
- Mocking de dependências

### Executar Testes

```powershell
cd backend-module
mvn test
```

---

## ⚙️ Configurações

### Backend (application.yml)

#### Desenvolvimento (Padrão - PostgreSQL)
```yaml
spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:beneficios_db}
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:postgres}
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: update
```

#### Desenvolvimento (H2 - Profile: dev)
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: create-drop
```

#### Produção (Profile: prod)
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
```

### Frontend (angular.json)

```json
{
  "serve": {
    "options": {
      "proxyConfig": "proxy.conf.json"
    }
  }
}
```

### Proxy Configuration (proxy.conf.json)

```json
{
  "/api": {
    "target": "http://localhost:8080",
    "secure": false,
    "changeOrigin": true
  }
}
```

---

## 🚀 Como Executar

### Pré-requisitos

- **JDK 17+**
- **Maven 3.8+**
- **Node.js 20+**
- **npm 10+**
- **PostgreSQL** (opcional, para produção)

### 1. Configurar Banco de Dados (PostgreSQL)

```sql
CREATE DATABASE beneficios_db;
```

Execute o schema:
```bash
psql -U postgres -d beneficios_db -f db/schema.sql
```

### 2. Executar Backend

#### Opção A: Script PowerShell (Windows)
```powershell
cd backend-module
.\fix-jdk.ps1    # Primeira vez
.\build.ps1      # Compilar
.\run.ps1        # Executar
```

#### Opção B: Maven direto
```powershell
cd backend-module
mvn clean install
mvn spring-boot:run
```

#### Opção C: Com profile H2
```powershell
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

**Verificar:**
- Backend: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui.html

### 3. Executar Frontend

#### Opção A: Script PowerShell
```powershell
cd frontend
.\install.ps1    # Primeira vez
.\start.ps1      # Executar
```

#### Opção B: npm direto
```powershell
cd frontend
npm install
npm start
```

**Verificar:**
- Frontend: http://localhost:4200

### 4. Acessar Aplicação

🌐 **http://localhost:4200**

---

## 🔧 Troubleshooting

### Problema: Erro de CORS

**Solução:**
- ✅ CORS já configurado no backend (`CorsConfig.java`)
- ✅ Proxy configurado no Angular (`proxy.conf.json`)
- ✅ Verificar que backend está rodando na porta 8080

### Problema: Banco de dados não conecta

**Solução PostgreSQL:**
```powershell
# Verificar se PostgreSQL está rodando
# Configurar variáveis de ambiente
$env:DB_HOST="localhost"
$env:DB_PORT="5432"
$env:DB_NAME="beneficios_db"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="sua_senha"
```

**Solução H2:**
```powershell
# Usar profile dev
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Problema: Porta já em uso

**Backend:**
```yaml
# application.yml
server:
  port: 8081  # Alterar porta
```

**Frontend:**
```powershell
ng serve --port 4201
```

### Problema: Erro de compilação Maven

**Solução:**
```powershell
cd backend-module
.\fix-maven.ps1
mvn clean install -U
```

### Problema: Dependências npm

**Solução:**
```powershell
cd frontend
Remove-Item -Recurse -Force node_modules
Remove-Item package-lock.json
npm install
```

---

## 📊 Status de Validação

### ✅ Backend
- [x] Compilação sem erros
- [x] Testes passando
- [x] API REST funcional
- [x] Validações implementadas
- [x] Locking implementado
- [x] Tratamento de erros
- [x] Documentação Swagger
- [x] CORS configurado

### ✅ Frontend
- [x] Compilação sem erros
- [x] Componentes funcionais
- [x] Integração com API
- [x] Validações de formulário
- [x] Tratamento de erros
- [x] Design responsivo
- [x] Proxy configurado

### ✅ Banco de Dados
- [x] Schema criado
- [x] Dados iniciais
- [x] Configuração H2
- [x] Configuração PostgreSQL
- [x] Migrations automáticas

### ✅ Documentação
- [x] README principal
- [x] Documentação de API
- [x] Guias de instalação
- [x] Troubleshooting
- [x] Documentação completa (este arquivo)

---

## 📝 Conclusão

Sistema completo e funcional de gerenciamento de benefícios com:

✅ **Arquitetura em camadas** bem definida  
✅ **CRUD completo** implementado  
✅ **Transferência de valores** com validações robustas  
✅ **Locking** para garantir integridade  
✅ **Frontend moderno** com Angular Material  
✅ **API REST** documentada com Swagger  
✅ **Testes unitários** implementados  
✅ **Configurações** para dev e produção  

**Pronto para uso e desenvolvimento!** 🚀

---

**Última atualização:** 2024-01-01  
**Versão:** 1.0.0  
**Autor:** Sistema BIP

