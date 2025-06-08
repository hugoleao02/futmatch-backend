# Arquitetura e Padrões do Projeto FutMatch

## Visão Geral
O projeto FutMatch segue os princípios da Clean Architecture (Arquitetura Limpa) e utiliza padrões de design modernos para garantir uma base de código manutenível, testável e escalável.

## Estrutura do Projeto

### Camadas da Aplicação

#### 1. Domain (Domínio)
- Contém as entidades de negócio e regras de domínio
- Localização: `src/main/java/br/com/futmatch/domain`
- Características:
  - Independente de frameworks
  - Contém as regras de negócio centrais
  - Define as interfaces dos repositórios

#### 2. Application (Aplicação)
- Contém os casos de uso e lógica de aplicação
- Localização: `src/main/java/br/com/futmatch/application`
- Características:
  - Implementa a lógica de negócio
  - Define DTOs para entrada e saída
  - Orquestra as operações entre as camadas

#### 3. Infrastructure (Infraestrutura)
- Implementa as interfaces definidas nas camadas superiores
- Localização: `src/main/java/br/com/futmatch/infrastructure`
- Características:
  - Implementações concretas dos repositórios
  - Adaptadores para frameworks externos
  - Configurações do Spring Boot

## Padrões de Design Utilizados

### 1. Injeção de Dependência
- Utiliza `@RequiredArgsConstructor` do Lombok
- Dependências injetadas via construtor
- Facilita a testabilidade e manutenção

### 2. DTO Pattern
- Separação clara entre objetos de domínio e objetos de transferência
- Exemplo: `PartidaRequest` e `PartidaResponse`

### 3. Repository Pattern
- Abstração do acesso a dados
- Interfaces definidas no domínio
- Implementações na camada de infraestrutura

### 4. Use Case Pattern
- Encapsula a lógica de negócio
- Exemplo: `CriarPartidaUseCase`
- Facilita a manutenção e testabilidade

## Convenções e Boas Práticas

### Nomenclatura
- Classes de domínio: substantivos simples (ex: `Usuario`, `Partida`)
- Casos de uso: verbos + substantivo (ex: `CriarPartidaUseCase`)
- DTOs: sufixo Request/Response (ex: `PartidaRequest`, `PartidaResponse`)

### Segurança
- Autenticação via Spring Security
- Validação de usuário autenticado
- Uso de `@AuthenticationPrincipal`

### Validação
- Uso de `@Valid` para validação de DTOs
- Validações de negócio na camada de aplicação

## Tecnologias Principais
- Java
- Spring Boot
- Spring Security
- Lombok
- Jakarta Validation

## Fluxo de Dados
1. Controller recebe requisição HTTP
2. DTO é validado
3. Use Case é executado
4. Regras de negócio são aplicadas
5. Repositório é utilizado para persistência
6. Resposta é retornada ao cliente

## Considerações de Manutenção
- Manter a separação de responsabilidades
- Seguir os princípios SOLID
- Manter a independência de frameworks
- Priorizar a testabilidade
- Documentar alterações significativas 