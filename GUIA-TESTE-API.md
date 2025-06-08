# 🧪 **Guia Completo para Testar a API FutMatch**

## 📋 **Pré-requisitos**
- ✅ Aplicação rodando em `http://localhost:8080`
- ✅ PostgreSQL configurado e rodando
- ✅ Postman, Insomnia, ou PowerShell para fazer requisições

## 🚀 **Métodos de Teste**

### **1. Usando PowerShell (Windows)**

#### **Script Completo de Teste:**
```powershell
# Gerar email único
$timestamp = (Get-Date).Ticks
$email = "user$timestamp@test.com"

Write-Host "=== TESTE API FUTMATCH ===" -ForegroundColor Green
Write-Host "Email: $email" -ForegroundColor Cyan

# 1. Registrar usuário
$registerBody = @{
    nome = "João Silva"
    email = $email
    senha = "123456"
} | ConvertTo-Json

$registerResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/register" -Method POST -Body $registerBody -ContentType "application/json"
Write-Host "✅ Usuário registrado!" -ForegroundColor Green

# 2. Login
$loginBody = @{
    email = $email
    senha = "123456"
} | ConvertTo-Json

$loginResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method POST -Body $loginBody -ContentType "application/json"
$token = $loginResponse.token
Write-Host "✅ Login realizado!" -ForegroundColor Green

# 3. Criar partida
$partidaBody = @{
    nome = "Pelada do Sábado"
    esporte = "FUTEBOL"
    latitude = -23.5505
    longitude = -46.6333
    dataHora = (Get-Date).AddDays(7).ToString("yyyy-MM-ddTHH:mm:ss")
    totalJogadores = 10
    tipoPartida = "PUBLICA"
} | ConvertTo-Json

$headers = @{
    "Authorization" = "Bearer $token"
    "Content-Type" = "application/json"
}

$partidaResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/partidas" -Method POST -Body $partidaBody -Headers $headers
Write-Host "✅ Partida criada!" -ForegroundColor Green
Write-Host "ID: $($partidaResponse.id)" -ForegroundColor Cyan
```

### **2. Usando cURL (Linux/Mac/Windows)**

#### **1. Registrar Usuário:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@example.com",
    "senha": "123456"
  }'
```

#### **2. Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@example.com",
    "senha": "123456"
  }'
```

#### **3. Criar Partida (substitua SEU_TOKEN):**
```bash
curl -X POST http://localhost:8080/api/partidas \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -d '{
    "nome": "Pelada do Sábado",
    "esporte": "FUTEBOL",
    "latitude": -23.5505,
    "longitude": -46.6333,
    "dataHora": "2024-12-15T14:00:00",
    "totalJogadores": 10,
    "tipoPartida": "PUBLICA"
  }'
```

### **3. Usando Postman/Insomnia**

#### **Configuração das Requisições:**

**1. POST** `http://localhost:8080/api/auth/register`
```json
{
  "nome": "João Silva",
  "email": "joao@example.com",
  "senha": "123456"
}
```

**2. POST** `http://localhost:8080/api/auth/login`
```json
{
  "email": "joao@example.com",
  "senha": "123456"
}
```

**3. POST** `http://localhost:8080/api/partidas`
- **Headers:** `Authorization: Bearer {token_do_login}`
```json
{
  "nome": "Pelada do Sábado",
  "esporte": "FUTEBOL",
  "latitude": -23.5505,
  "longitude": -46.6333,
  "dataHora": "2024-12-15T14:00:00",
  "totalJogadores": 10,
  "tipoPartida": "PUBLICA"
}
```

## 📝 **Exemplos de Dados de Teste**

### **Partida de Futebol:**
```json
{
  "nome": "Pelada do Domingo",
  "esporte": "FUTEBOL",
  "latitude": -23.5505,
  "longitude": -46.6333,
  "dataHora": "2024-12-15T14:00:00",
  "totalJogadores": 22,
  "tipoPartida": "PUBLICA"
}
```

### **Partida de Futsal:**
```json
{
  "nome": "Futsal da Galera",
  "esporte": "FUTSAL",
  "latitude": -23.5505,
  "longitude": -46.6333,
  "dataHora": "2024-12-16T19:00:00",
  "totalJogadores": 10,
  "tipoPartida": "PRIVADA"
}
```

## ✅ **Respostas Esperadas**

### **Registro/Login (200/201):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "joao@example.com"
}
```

### **Partida Criada (201):**
```json
{
  "id": 1,
  "nome": "Pelada do Sábado",
  "esporte": "FUTEBOL",
  "latitude": -23.5505,
  "longitude": -46.6333,
  "dataHora": "2024-12-15T14:00:00",
  "totalJogadores": 10,
  "tipoPartida": "PUBLICA",
  "criadorId": 1,
  "criadorNome": "João Silva",
  "participantesConfirmados": 1
}
```

## ❌ **Possíveis Erros**

### **400 - Bad Request:**
```json
{
  "nome": "Nome da partida é obrigatório",
  "esporte": "Esporte é obrigatório"
}
```

### **401 - Unauthorized:**
```json
{
  "error": "Token JWT inválido"
}
```

### **403 - Forbidden:**
```json
{
  "error": "Acesso negado"
}
```

### **409 - Conflict:**
```json
{
  "error": "Email já cadastrado: joao@example.com"
}
```

## 🔧 **Troubleshooting**

### **Se receber 403 Forbidden:**
1. Verifique se o token JWT está sendo enviado corretamente
2. Confirme que o header `Authorization: Bearer {token}` está correto
3. Verifique se o token não expirou (24 horas)

### **Se receber 400 Bad Request:**
1. Verifique se todos os campos obrigatórios estão preenchidos
2. Confirme que a data está no formato correto: `yyyy-MM-ddTHH:mm:ss`
3. Verifique se os enums estão corretos: `FUTEBOL/FUTSAL`, `PUBLICA/PRIVADA`

### **Se receber 409 Conflict:**
1. Use um email diferente para registro
2. Ou faça login com o email existente

## 🎯 **Validações Implementadas**

- ✅ **Nome**: obrigatório, não vazio
- ✅ **Esporte**: FUTEBOL ou FUTSAL
- ✅ **Coordenadas**: latitude (-90 a 90), longitude (-180 a 180)
- ✅ **Data/Hora**: presente ou futuro
- ✅ **Total Jogadores**: 2 a 22 (Futsal máximo 10)
- ✅ **Tipo**: PUBLICA ou PRIVADA
- ✅ **Autenticação**: JWT obrigatório

## 🚀 **Scripts Prontos**

Execute os scripts que criamos:
- `teste-final.ps1` - Teste completo
- `teste-debug.ps1` - Teste com logs
- `test-requests.http` - Para VS Code REST Client

## 📊 **Banco de Dados**

As tabelas são criadas automaticamente:
- `usuarios` - dados dos usuários
- `partidas` - dados das partidas
- `participacoes` - participação dos usuários nas partidas

O criador da partida é automaticamente incluído como participante confirmado! 