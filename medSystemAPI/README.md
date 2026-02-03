# 🏥 MedSystem API

Sistema de gerenciamento de consultas médicas com autenticação JWT e controle de acesso baseado em roles (ADMIN, DOCTOR, PATIENT).

---

## 🚀 Início Rápido

### Pré-requisitos

- **Java 17+**
- **Maven 3.6+**
- **Docker & Docker Compose**
- **Git**

### Setup em 4 Passos

```bash
# 1. Clone o repositório
git clone <url-do-repositorio>
cd medSystemAPI

# 2. Configure variáveis de ambiente (crie o arquivo login.env)
# Windows PowerShell:
@"
JWT_SECRET=jXwd0yHNMizz8YbeS/yyLPGF/S7fjLDpuugyzBGULasX2IkuuqX9ffgu4n+rCehU8XzZ5tTXUU5Z44/qDwUJNQ==
ADMIN_EMAIL=admin@medsystem.com
ADMIN_PASSWORD=admin@123
"@ | Out-File -FilePath login.env -Encoding UTF8

# Linux/Mac:
cat > login.env << 'EOF'
JWT_SECRET=jXwd0yHNMizz8YbeS/yyLPGF/S7fjLDpuugyzBGULasX2IkuuqX9ffgu4n+rCehU8XzZ5tTXUU5Z44/qDwUJNQ==
ADMIN_EMAIL=admin@medsystem.com
ADMIN_PASSWORD=admin@123
EOF

# 3. Suba o banco de dados
docker-compose up -d

# 4. Rode a aplicação (Windows PowerShell)
Get-Content login.env | ForEach-Object {if ($_ -match '^([^#][^=]+)=(.+)$') {[Environment]::SetEnvironmentVariable($matches[1].Trim(), $matches[2].Trim(), 'Process')}}; .\mvnw.cmd spring-boot:run

# Ou Linux/Mac:
export $(cat login.env | grep -v '^#' | xargs) && ./mvnw spring-boot:run
```

### ✅ Pronto! Acesse:

- **API:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **PgAdmin:** http://localhost:5050 (admin@medsystem.com / admin@123)

**Credenciais Admin:** `admin@medsystem.com` / `admin@123`

---

## 🔌 Conectando com o Front-End

### URL Base

```
http://localhost:8080
```

### Headers Necessários

```http
Content-Type: application/json
Authorization: Bearer {seu-token-jwt}
```

### Fluxo de Autenticação

1. **Registrar usuário** (sem autenticação):

```http
POST /auth/register/paciente
POST /auth/register/medico
```

2. **Admin aprova usuário**:

```http
POST /admin/users/{userId}/approve
```

3. **Login** (recebe o token):

```http
POST /auth/login
→ Retorna: { "token": "eyJhbGciOi..." }
```

4. **Usar o token** nas próximas requisições:

```http
GET /appointments/patient/1/my-consultations
Authorization: Bearer eyJhbGciOi...
```

### CORS

CORS está configurado para aceitar requisições de qualquer origem em desenvolvimento. Para produção, configure em `SecurityConfigurations.java`.

---

## 📚 Funcionalidades

### Autenticação

- Login com JWT
- Registro de médicos e pacientes
- Aprovação de usuários pelo admin
- Controle por roles (ADMIN, DOCTOR, PATIENT)

### Consultas

- Agendamento com validação de horário (Seg-Sáb, 07:00-19:00)
- Seleção automática de médico ou busca por CRM/nome
- Cancelamento com 24h de antecedência
- Listagem filtrada por paciente/médico

### Validações

- **CPF**: Formato + dígitos verificadores
- **CRM**: `CRM/UF 000000` + UF válida
- **CEP**: `00000-000`
- **Email**: RFC + unicidade
- **Telefone**: Múltiplos formatos

---

## � Controle de Acesso (Roles)

| Endpoint                                          | ADMIN    | DOCTOR      | PATIENT     |
| ------------------------------------------------- | -------- | ----------- | ----------- |
| `GET /appointments`                               | ✅ Todas | ❌          | ❌          |
| `POST /appointments`                              | ✅       | ❌          | ✅ Próprias |
| `DELETE /appointments/{id}`                       | ✅       | ✅ Próprias | ✅ Próprias |
| `GET /appointments/patient/{id}/my-consultations` | ✅       | ❌          | ✅ Próprias |
| `GET /appointments/doctor/{id}/my-consultations`  | ✅       | ✅ Próprias | ❌          |
| `GET /doctors`                                    | ✅       | ✅          | ✅          |
| `GET /patients`                                   | ✅       | ❌          | ❌          |

---

## 📡 Principais Endpoints

### Autenticação (Públicos)

```
POST /auth/login                    # Login
POST /auth/register/paciente        # Registro de paciente
POST /auth/register/medico          # Registro de médico
GET  /auth/specialties              # Listar especialidades
```

### Consultas (Autenticados)

```
GET    /appointments                                    # Listar todas (ADMIN)
POST   /appointments                                    # Agendar consulta
DELETE /appointments/{id}                               # Cancelar consulta
GET    /appointments/patient/{id}/my-consultations      # Consultas do paciente
GET    /appointments/doctor/{id}/my-consultations       # Consultas do médico
```

### Médicos e Pacientes

```
GET /doctors          # Listar médicos (todos podem acessar)
GET /patients         # Listar pacientes (apenas ADMIN)
```

### Admin

```
GET  /admin/users/pending           # Usuários pendentes de aprovação
POST /admin/users/{id}/approve      # Aprovar usuário
POST /admin/users/{id}/reject       # Rejeitar usuário
```

**Documentação completa:** `http://localhost:8080/swagger-ui.html`

---

## 🐳 Docker

### Subir o Banco de Dados

```bash
docker-compose up -d
```

### Ver logs

```bash
docker-compose logs -f postgres
```

### Parar containers

```bash
docker-compose down
```

### Resetar banco (apaga todos os dados!)

```bash
docker-compose down -v
docker-compose up -d
```

### Acessar PgAdmin

- URL: `http://localhost:5050`
- Email: `admin@medsystem.com`
- Senha: `admin@123`

**Configurar conexão no PgAdmin:**

- Host: `postgres`
- Port: `5432`
- Database: `postgres`
- Username: `postgres`
- Password: `root`

---

## 🧪 Testando a API

### 1. Pelo Swagger

Acesse `http://localhost:8080/swagger-ui.html` e teste diretamente pela interface.

### 2. Por cURL

**Login:**

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@medsystem.com",
    "password": "admin@123"
  }'
```

**Agendar consulta:**

```bash
curl -X POST http://localhost:8080/appointments \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN" \
  -d '{
    "patientId": 1,
    "doctorCrm": "CRM/SP 123456",
    "appointmentTime": "2026-02-10T14:00:00"
  }'
```

---

## 📤 Compartilhando o Projeto

### Para um novo desenvolvedor configurar:

1. **Clone o repositório**
2. **Crie o arquivo `login.env`** (veja seção Instalação)
3. **Suba o Docker:** `docker-compose up -d`
4. **Rode a aplicação:** `./mvnw spring-boot:run`

### Compartilhando via Git

```bash
# Commitar apenas código (login.env está no .gitignore)
git add .
git commit -m "feat: implementação completa da API"
git push origin main
```

⚠️ **O arquivo `login.env` NÃO deve ser commitado!** Ele contém credenciais sensíveis e já está no `.gitignore`.

---

## ⚙️ Configuração das Variáveis de Ambiente

Crie o arquivo `login.env` na raiz do projeto:

```bash
# JWT Configuration
JWT_SECRET=jXwd0yHNMizz8YbeS/yyLPGF/S7fjLDpuugyzBGULasX2IkuuqX9ffgu4n+rCehU8XzZ5tTXUU5Z44/qDwUJNQ==

# Admin Configuration
ADMIN_EMAIL=admin@medsystem.com
ADMIN_PASSWORD=admin@123
```

**Gerando uma chave JWT segura:**

```bash
# Linux/Mac
openssl rand -base64 64

# Windows PowerShell
[Convert]::ToBase64String((1..64 | ForEach-Object { Get-Random -Maximum 256 }))
```

---

## 🛠️ Tecnologias Utilizadas

- Java 17
- Spring Boot 3.5.7
- Spring Security + JWT
- PostgreSQL 15
- Docker
- Swagger/OpenAPI
- Hibernate Validator

---

## 📁 Estrutura do Projeto

```
src/main/java/ifba/edu/br/medSystemAPI/
├── config/              # Configurações (Security, Swagger, Admin)
├── controllers/         # Endpoints REST
├── services/            # Lógica de negócio
├── repositories/        # Acesso ao banco
├── models/
│   ├── entities/        # Entidades JPA
│   └── enums/           # Enums (Role, Specialty, AppointmentStatus)
├── dtos/                # DTOs de request/response
├── exceptions/          # Exceções customizadas
├── security/            # Filtros de segurança JWT
├── validators/          # Validadores customizados
└── utils/               # Utilitários (ValidationUtils)
```

---

## � Troubleshooting

### Erro: "Could not resolve placeholder 'JWT_SECRET'"

- **Solução:** Crie o arquivo `login.env` com as variáveis necessárias

### Erro: "Connection refused" ao acessar o banco

- **Solução:** Certifique-se que o Docker está rodando: `docker-compose up -d`

### Porta 8080 já está em uso

- **Solução:** Mude a porta em `application.properties`: `server.port=8081`

### Erro ao rodar testes

- **Solução:** As variáveis de ambiente são carregadas automaticamente em `src/test/resources/application.properties`

---

## 📝 Licença

Este projeto é de uso educacional.

---

## 👥 Contribuidores

Desenvolvido por **[Seu Nome]** - IFBA

---

### 2️⃣ Configure as Variáveis de Ambiente

#### Windows (PowerShell):

```powershell
Copy-Item login.env.example login.env
```

#### Linux/Mac:

```bash
cp login.env.example login.env
```

#### Edite o arquivo `login.env`:

```properties
# JWT Configuration
JWT_SECRET=sua-chave-secreta-aqui-minimo-256-bits

# Admin Configuration
ADMIN_EMAIL=admin@medsystem.com
ADMIN_PASSWORD=admin@123
```

#### 🔑 Gere uma Chave JWT Segura:

**Windows (PowerShell):**

```powershell
[Convert]::ToBase64String((1..64 | ForEach-Object { Get-Random -Maximum 256 }))
```

**Linux/Mac:**

```bash
openssl rand -base64 64
```

> ⚠️ **IMPORTANTE:** Nunca commite o arquivo `login.env` (já está no `.gitignore`)!

### 3️⃣ Suba o Banco de Dados com Docker

```bash
docker-compose up -d
```

Isso irá criar:

- **PostgreSQL** na porta `5432` (com **persistência de dados**)
- **PgAdmin** na porta `5050` para gerenciar o banco visualmente

#### Acesse o PgAdmin:

- **URL:** http://localhost:5050
- **Email:** `admin@medsystem.com`
- **Senha:** `admin@123`

#### Conecte ao PostgreSQL via PgAdmin:

- **Host:** `postgres` (nome do container)
- **Port:** `5432`
- **Database:** `postgres`
- **Username:** `postgres`
- **Password:** `root`

---

## 🏃 Rodando o Projeto

### Opção 1: Com Maven Wrapper (Recomendado)

#### Windows (PowerShell):

```powershell
# Carregue as variáveis de ambiente
Get-Content login.env | ForEach-Object {
    if ($_ -match '^([^#][^=]+)=(.+)$') {
        $name = $matches[1].Trim()
        $value = $matches[2].Trim()
        [Environment]::SetEnvironmentVariable($name, $value, 'Process')
    }
}

# Execute a aplicação
.\mvnw.cmd spring-boot:run
```

#### Linux/Mac:

```bash
# Carregue as variáveis de ambiente
export $(cat login.env | grep -v '^#' | xargs)

# Execute a aplicação
./mvnw spring-boot:run
```

### Opção 2: Com Maven Instalado

```bash
mvn spring-boot:run
```

### ✅ Aplicação Rodando!

- **API:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs

### 🔐 Credenciais Padrão (Admin)

Na **primeira execução**, um usuário admin é criado automaticamente:

- **Email:** `admin@medsystem.com`
- **Senha:** `admin@123`

---

## 🤝 Compartilhando o Projeto em Equipe

### Para o Desenvolvedor que vai Compartilhar:

1. **Commite apenas os arquivos necessários:**

   ```bash
   git add .
   git commit -m "feat: setup inicial do projeto"
   git push origin main
   ```

2. **NÃO commite:**
   - ❌ `login.env` (credenciais sensíveis)
   - ❌ `target/` (arquivos compilados)
   - ❌ `.idea/`, `.vscode/` (configurações de IDE)

3. **Compartilhe:**
   - ✅ O link do repositório
   - ✅ Instruções do `README.md`
   - ✅ O arquivo `login.env.example`

### Para Quem vai Rodar em Outra Máquina:

1. **Clone o repositório:**

   ```bash
   git clone https://github.com/seu-usuario/medSystemAPI.git
   cd medSystemAPI
   ```

2. **Crie o arquivo `login.env`:**

   ```bash
   cp login.env.example login.env
   ```

3. **Gere uma chave JWT (ou use a mesma da equipe):**

   ```powershell
   # Windows
   [Convert]::ToBase64String((1..64 | ForEach-Object { Get-Random -Maximum 256 }))
   ```

4. **Suba o banco de dados:**

   ```bash
   docker-compose up -d
   ```

5. **Rode a aplicação:**

   ```powershell
   # Windows
   .\mvnw.cmd spring-boot:run
   ```

6. **Acesse o Swagger:**
   ```
   http://localhost:8080/swagger-ui.html
   ```

### 🔄 Persistência de Dados Entre Máquinas

Os dados do PostgreSQL são salvos em um **Docker volume** (`postgres_data`), garantindo que:

- ✅ Dados não são perdidos ao parar o container
- ✅ Dados podem ser compartilhados via export/import do volume
- ✅ Cada desenvolvedor tem seu próprio banco local

---

## 🔐 Endpoints e Controle de Acesso

### 🌐 Endpoints Públicos (Sem Autenticação)

| Método | Endpoint                  | Descrição                     |
| ------ | ------------------------- | ----------------------------- |
| `POST` | `/auth/login`             | Login (retorna JWT)           |
| `POST` | `/auth/register/medico`   | Registro de médico            |
| `POST` | `/auth/register/paciente` | Registro de paciente          |
| `GET`  | `/auth/specialties`       | Listar especialidades médicas |

### 🔒 Endpoints Protegidos (Requerem JWT)

| Endpoint                                          | ADMIN | PATIENT | DOCTOR | Descrição                  |
| ------------------------------------------------- | :---: | :-----: | :----: | -------------------------- |
| `GET /doctors`                                    |  ✅   |   ✅    |   ✅   | Listar médicos             |
| `GET /doctors/{id}`                               |  ✅   |   ✅    |   ✅   | Detalhes de médico         |
| `PUT /doctors/{id}`                               |  ✅   |   ❌    |  ✅\*  | Atualizar médico           |
| `DELETE /doctors/{id}`                            |  ✅   |   ❌    |   ❌   | Deletar médico             |
| `GET /patients`                                   |  ✅   |   ❌    |   ❌   | Listar **todos** pacientes |
| `GET /patients/{id}`                              |  ✅   |  ✅\*   |   ❌   | Detalhes de paciente       |
| `PUT /patients/{id}`                              |  ✅   |  ✅\*   |   ❌   | Atualizar paciente         |
| `DELETE /patients/{id}`                           |  ✅   |   ❌    |   ❌   | Deletar paciente           |
| `GET /appointments`                               |  ✅   |   ❌    |   ❌   | Listar **todas** consultas |
| `POST /appointments`                              |  ✅   |   ✅    |   ❌   | Agendar consulta           |
| `DELETE /appointments/{id}`                       |  ✅   |  ✅\*   |  ✅\*  | Cancelar consulta          |
| `GET /appointments/patient/{id}/my-consultations` |  ✅   |  ✅\*   |   ❌   | Consultas do paciente      |
| `GET /appointments/doctor/{id}/my-consultations`  |  ✅   |   ❌    |  ✅\*  | Consultas do médico        |
| `PUT /admin/approve-user/{id}`                    |  ✅   |   ❌    |   ❌   | Aprovar usuário            |

**\* Apenas seus próprios recursos**

---

## 📝 Exemplos de Uso

### 1️⃣ Registrar um Paciente

```bash
curl -X POST http://localhost:8080/auth/register/paciente \
  -H "Content-Type: application/json" \
  -d '{
    "email": "maria@email.com",
    "password": "senha123",
    "name": "Maria Santos",
    "phone": "(71) 98765-4321",
    "cpf": "123.456.789-09",
    "address": {
      "street": "Rua das Flores",
      "number": "100",
      "neighborhood": "Centro",
      "city": "Salvador",
      "state": "BA",
      "zipCode": "40000-000"
    }
  }'
```

### 2️⃣ Login (Obter JWT)

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@medsystem.com",
    "password": "admin@123"
  }'
```

**Resposta:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 3️⃣ Aprovar Usuário (ADMIN)

```bash
curl -X PUT http://localhost:8080/admin/approve-user/2 \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

### 4️⃣ Agendar Consulta (PATIENT)

```bash
curl -X POST http://localhost:8080/appointments \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -d '{
    "patientId": 2,
    "doctorCrm": "CRM/BA 123456",
    "appointmentTime": "2026-03-15T14:00:00"
  }'
```

### 5️⃣ Listar Minhas Consultas (PATIENT)

```bash
curl -X GET http://localhost:8080/appointments/patient/2/my-consultations \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

---

## 🧪 Testes

### Rodar Todos os Testes

#### Windows (PowerShell):

```powershell
# Carregue as variáveis de ambiente
Get-Content login.env | ForEach-Object {
    if ($_ -match '^([^#][^=]+)=(.+)$') {
        $name = $matches[1].Trim()
        $value = $matches[2].Trim()
        [Environment]::SetEnvironmentVariable($name, $value, 'Process')
    }
}

# Execute os testes
.\mvnw.cmd test
```

#### Linux/Mac:

```bash
export $(cat login.env | grep -v '^#' | xargs)
./mvnw test
```

### Rodar Teste Específico

```bash
./mvnw test -Dtest=NomeDaClasseDeTeste
```

---

## 🔧 Troubleshooting

### Problema 1: Porta 8080 já está em uso

**Erro:**

```
Web server failed to start. Port 8080 was already in use.
```

**Solução:**

```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :8080
kill -9 <PID>
```

### Problema 2: Banco de dados não conecta

**Erro:**

```
Connection to localhost:5432 refused
```

**Solução:**

```bash
# Verifique se o Docker está rodando
docker ps

# Reinicie os containers
docker-compose down
docker-compose up -d

# Veja os logs
docker-compose logs postgres
```

### Problema 3: JWT_SECRET não carregado

**Erro:**

```
The JWT secret key is missing!
```

**Solução:**

```powershell
# Windows: Carregue manualmente
Get-Content login.env | ForEach-Object {
    if ($_ -match '^([^#][^=]+)=(.+)$') {
        $name = $matches[1].Trim()
        $value = $matches[2].Trim()
        [Environment]::SetEnvironmentVariable($name, $value, 'Process')
    }
}
```

### Problema 4: Permissão negada ao acessar recurso

**Erro:**

```
403 Forbidden
```

**Solução:**

- ✅ Verifique se o usuário foi **aprovado pelo admin**
- ✅ Confirme se está usando o **JWT correto** no header `Authorization: Bearer <token>`
- ✅ Valide se a **role** do usuário tem permissão para o endpoint

### Problema 5: CPF inválido ao cadastrar

**Erro:**

```
CPF inválido
```

**Solução:**

- Use um CPF válido (com dígitos verificadores corretos)
- Exemplos: `123.456.789-09`, `111.444.777-35`
- Gerador online: https://www.4devs.com.br/gerador_de_cpf

---

## 📁 Estrutura do Projeto

```
medSystemAPI/
├── src/
│   ├── main/
│   │   ├── java/ifba/edu/br/medSystemAPI/
│   │   │   ├── config/              # Configurações (CORS, Security, Swagger, Admin)
│   │   │   ├── controllers/         # Endpoints REST
│   │   │   ├── dtos/                # Data Transfer Objects
│   │   │   │   ├── address/request/
│   │   │   │   ├── appointment/
│   │   │   │   ├── auth/
│   │   │   │   ├── doctor/
│   │   │   │   └── patient/
│   │   │   ├── exceptions/          # Exceções customizadas + GlobalExceptionHandler
│   │   │   ├── models/              # Entidades JPA + Enums
│   │   │   │   ├── entities/
│   │   │   │   └── enums/
│   │   │   ├── repositories/        # Interfaces JPA
│   │   │   ├── security/            # Filtros JWT
│   │   │   └── services/            # Lógica de negócio
│   │   └── resources/
│   │       └── application.properties
│   └── test/                        # Testes unitários
├── target/                          # Arquivos compilados (não commitar)
├── docker-compose.yml               # PostgreSQL + PgAdmin
├── pom.xml                          # Dependências Maven
├── .gitignore                       # Arquivos ignorados pelo Git
├── login.env.example                # Template de variáveis de ambiente
├── login.env                        # Variáveis de ambiente (não commitar)
└── README.md                        # Este arquivo
```

---

## 🤝 Contribuindo

1. **Fork** o projeto
2. Crie uma **branch** para sua feature:
   ```bash
   git checkout -b feature/nova-funcionalidade
   ```
3. **Commit** suas mudanças:
   ```bash
   git commit -m "feat: adiciona validação de CPF"
   ```
4. **Push** para a branch:
   ```bash
   git push origin feature/nova-funcionalidade
   ```
5. Abra um **Pull Request**

### 📏 Convenção de Commits

Usamos [Conventional Commits](https://www.conventionalcommits.org/):

- `feat`: Nova funcionalidade
- `fix`: Correção de bug
- `docs`: Documentação
- `refactor`: Refatoração de código
- `test`: Adição de testes
- `chore`: Mudanças em ferramentas/config

---

## 📄 Licença

Este projeto está sob a licença **MIT**. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 👥 Autores

- **Equipe MedSystem** - [GitHub](https://github.com/seu-usuario)

---

## 📞 Suporte

- **Email:** suporte@medsystem.com
- **Issues:** [GitHub Issues](https://github.com/seu-usuario/medSystemAPI/issues)

---

## 🌟 Recursos Adicionais

- [Documentação do Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Swagger UI](http://localhost:8080/swagger-ui.html)
- [PostgreSQL Docs](https://www.postgresql.org/docs/)
- [JWT.io](https://jwt.io/)

---

<div align="center">
  <p>Desenvolvido com ❤️ pela equipe MedSystem</p>
  <p>⭐ Dê uma estrela se este projeto foi útil!</p>
</div>
