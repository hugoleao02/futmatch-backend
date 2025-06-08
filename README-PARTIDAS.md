# FutMatch - Módulo de Criação de Partidas

## Visão Geral

Este módulo implementa a funcionalidade de criação de partidas no FutMatch, seguindo rigorosamente a **Arquitetura Hexagonal (Ports and Adapters)**.

## Funcionalidades Implementadas

- ✅ Criação de partidas de futebol e futsal
- ✅ Validação de dados de entrada
- ✅ Autenticação JWT obrigatória
- ✅ Criador automaticamente incluído como participante
- ✅ Suporte a partidas públicas e privadas
- ✅ Validações de negócio específicas por esporte

## Arquitetura

### Camada de Domínio (`domain`)
- **Modelos**: `Partida`, `Participacao`, `Usuario`
- **Enums**: `Esporte`, `TipoPartida`, `StatusParticipacao`
- **Portas**: `PartidaRepositoryPort`
- **Exceções**: `UsuarioNotFoundException`

### Camada de Aplicação (`application`)
- **DTOs**: `PartidaRequest`, `PartidaResponse`
- **Casos de Uso**: `CriarPartidaUseCase`
- **Serviços**: `PartidaApplicationService`

### Camada de Infraestrutura (`infrastructure`)
- **Entities JPA**: `PartidaEntity`, `ParticipacaoEntity` (usam enums do domínio)
- **Mappers**: `PartidaMapper`, `ParticipacaoMapper` (simplificados)
- **Repositórios**: `PartidaSpringRepository`
- **Adapters**: `PartidaJpaAdapter`
- **Controllers**: `MatchController`

## API Endpoints

### POST `/api/partidas`
Cria uma nova partida.

**Headers:**
```
Authorization: Bearer {jwt_token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "nome": "Pelada do Sábado",
  "esporte": "FUTEBOL", // FUTEBOL ou FUTSAL
  "latitude": -23.5505,
  "longitude": -46.6333,
  "dataHora": "2024-12-15T14:00:00",
  "totalJogadores": 10,
  "tipoPartida": "PUBLICA" // PUBLICA ou PRIVADA
}
```

**Response (201 Created):**
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

## Validações

### Validações de Entrada
- Nome da partida: obrigatório
- Esporte: obrigatório (FUTEBOL ou FUTSAL)
- Coordenadas: obrigatórias e dentro dos limites válidos
- Data/hora: obrigatória e no presente/futuro
- Total de jogadores: entre 2 e 22
- Tipo de partida: obrigatório (PUBLICA ou PRIVADA)

### Validações de Negócio
- Futsal: máximo 10 jogadores
- Usuário criador deve existir
- Criador é automaticamente incluído como participante confirmado

## Tratamento de Erros

### 400 Bad Request
- Dados de entrada inválidos
- Validações de negócio falharam

### 401 Unauthorized
- Token JWT inválido ou ausente

### 404 Not Found
- Usuário criador não encontrado

### Exemplo de Erro:
```json
{
  "error": "Futsal permite no máximo 10 jogadores"
}
```

## Como Testar

1. **Registrar um usuário:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"nome":"João Silva","email":"joao@example.com","senha":"123456"}'
```

2. **Fazer login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"joao@example.com","senha":"123456"}'
```

3. **Criar partida (use o token retornado no login):**
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

## Banco de Dados

As tabelas são criadas automaticamente pelo Hibernate:

- `partidas`: armazena os dados das partidas
- `participacoes`: gerencia a participação dos usuários nas partidas

## Próximos Passos

- Implementar listagem de partidas
- Adicionar funcionalidade de participar/sair de partidas
- Implementar busca por localização
- Adicionar notificações para participantes 