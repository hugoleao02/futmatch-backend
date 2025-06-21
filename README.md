# FutMatch Backend

Backend do sistema FutMatch, uma plataforma para gerenciamento de partidas de futebol.

## Arquitetura

O projeto utiliza a Arquitetura Hexagonal (também conhecida como Ports and Adapters), que separa a aplicação em três camadas principais:

### Domínio
- Contém as regras de negócio e entidades principais
- Value Objects para conceitos do domínio
- Portas (interfaces) que definem contratos com o mundo exterior

### Aplicação
- Implementa os casos de uso da aplicação
- Orquestra as operações entre domínio e adaptadores
- Contém DTOs para transferência de dados

### Infraestrutura
- Adaptadores de entrada (controllers)
- Adaptadores de saída (repositories)
- Configurações técnicas

## Tecnologias

- Java 17
- Spring Boot 3.x
- Spring Security
- Spring Data JPA
- PostgreSQL
- Maven
- Swagger/OpenAPI

## Requisitos

- JDK 17
- Maven 3.8+
- PostgreSQL 12+

## Configuração

1. Clone o repositório
```bash
git clone https://github.com/seu-usuario/futmatch-backend.git
cd futmatch-backend
```

2. Configure o banco de dados
```properties
# application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/futmatch
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

3. Execute o projeto
```bash
mvn spring-boot:run
```

## Documentação da API

A documentação da API está disponível através do Swagger UI:
- Desenvolvimento: http://localhost:8080/swagger-ui.html
- Produção: https://api.futmatch.com/swagger-ui.html

## Estrutura do Projeto

```
src/main/java/br/com/futmatch/
├── domain/           # Camada de domínio
│   ├── models/      # Entidades de domínio
│   ├── ports/       # Portas (interfaces)
│   └── valueobjects/# Value Objects
├── application/      # Camada de aplicação
│   ├── dtos/        # Data Transfer Objects
│   ├── services/    # Serviços de aplicação
│   └── usecases/    # Casos de uso
└── infrastructure/   # Camada de infraestrutura
    ├── adapters/    # Adaptadores
    ├── config/      # Configurações
    └── security/    # Configurações de segurança
```

## Testes

```bash
# Executar todos os testes
mvn test

# Executar testes com cobertura
mvn verify
```

## Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

## Licença

Este projeto está licenciado sob a licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes. 