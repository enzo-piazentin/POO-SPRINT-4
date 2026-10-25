<img width="1373" height="337" alt="Captura de tela 2026-10-25 004859" src="https://github.com/user-attachments/assets/f53e8c0f-ea5d-438c-9690-11518e041e7e" /># MOTIVA - Sprint 4: API REST com Spring Boot + JPA

API REST profissional para o sistema MOTIVA, migrado de JDBC puro (Sprint 3) para Spring Boot + Spring Data JPA.

## Índice

| Item | Link                                           |
|---|------------------------------------------------|
| Visão geral | [Acessar seção](#visão-geral)                  |
| Arquitetura | [Acessar seção](#arquitetura)                  |
| Pré-requisitos | [Acessar seção](#pré-requisitos)               |
| Como rodar | [Acessar seção](#como-rodar)                   |
| Endpoints da API | [Acessar seção](#endpoints-da-api)             |
| Exemplos cURL | [Acessar seção](#exemplos-curl)                |
| Perguntas de reflexão | [Acessar seção](#perguntas-de-reflexão)        |
| Prints do Projeto executando | [Acessar seção](#Prints-do-Projeto-executando) |

## Visão geral

A API REST expõe todas as funcionalidades do sistema MOTIVA:
- CRUD completo de equipes de manutenção
- CRUD completo de trechos da rodovia
- CRUD completo de intervenções operacionais
- Geração de relatórios de prioridade com histórico
- Consultas por período de relatórios

**Migração Sprint 3 → Sprint 4:**
- JDBC puro → Spring Data JPA
- DAO manual → Repository gerado pelo Spring
- Console → API REST (JSON)
- SQL manual → Derived queries

## Arquitetura

```
src/main/java/br/com/motiva/
├── MotivaApplication.java          # @SpringBootApplication (main)
├── model/                          # @Entity — entidades JPA
│   ├── EquipeManutencao.java
│   ├── TrechoRodovia.java
│   ├── IntervencaoOperacional.java
│   └── RelatorioPrioridade.java
├── repository/                     # Interfaces JpaRepository
│   ├── EquipeManutencaoRepository.java
│   ├── TrechoRodoviaRepository.java
│   ├── IntervencaoOperacionalRepository.java
│   └── RelatorioPrioridadeRepository.java
├── service/                        # @Service — regras de negócio
│   ├── EquipeManutencaoService.java
│   ├── TrechoRodoviaService.java
│   ├── IntervencaoOperacionalService.java
│   └── RelatorioPrioridadeService.java
└── controller/                     # @RestController — endpoints REST
    ├── EquipeManutencaoController.java
    ├── TrechoRodoviaController.java
    ├── IntervencaoOperacionalController.java
    └── RelatorioPrioridadeController.java
```

**Camadas:** Controller → Service → Repository → Banco Oracle FIAP

## Pré-requisitos

- Java 17+
- Maven 3.6+
- Oracle Database FIAP (oracle.fiap.com.br:1521:ORCL)
- Tabelas já criadas (reaproveitadas da Sprint 3)

## Como rodar

### 1. Configurar credenciais do banco

Edite `src/main/resources/application.properties`:

```properties
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
```

### 2. Compilar e rodar com Maven

```bash
mvn clean install
mvn spring-boot:run
```

A API estará disponível em `http://localhost:8080`

### 3. Testar com cURL ou Postman

Ver exemplos abaixo.

## Endpoints da API

### Equipes de Manutenção

| Método | Rota | Descrição | Status |
|---|---|---|---|
| GET | `/api/equipes` | Listar todas | 200 |
| GET | `/api/equipes/{id}` | Buscar por ID | 200 / 404 |
| POST | `/api/equipes` | Criar equipe | 201 |
| PUT | `/api/equipes/{id}` | Atualizar equipe | 200 / 404 |
| DELETE | `/api/equipes/{id}` | Remover equipe | 204 / 404 |
| GET | `/api/equipes/especialidade/{esp}` | Buscar por especialidade | 200 |
| GET | `/api/equipes/direcao/{dir}` | Buscar por direção | 200 |

### Trechos da Rodovia

| Método | Rota | Descrição | Status |
|---|---|---|---|
| GET | `/api/trechos` | Listar todos | 200 |
| GET | `/api/trechos/{km}` | Buscar por KM | 200 / 404 |
| POST | `/api/trechos` | Criar trecho | 201 |
| PUT | `/api/trechos/{km}` | Atualizar trecho | 200 / 404 |
| DELETE | `/api/trechos/{km}` | Remover trecho | 204 / 404 |
| GET | `/api/trechos/tipo/{tipo}` | Buscar por tipo de terreno | 200 |
| GET | `/api/trechos/altura-minima/{min}` | Buscar por altura mínima | 200 |

### Intervenções Operacionais

| Método | Rota | Descrição | Status |
|---|---|---|---|
| GET | `/api/intervencoes` | Listar todas | 200 |
| GET | `/api/intervencoes/{id}` | Buscar por ID | 200 / 404 |
| POST | `/api/intervencoes` | Criar intervenção | 201 |
| PUT | `/api/intervencoes/{id}` | Atualizar intervenção | 200 / 404 |
| DELETE | `/api/intervencoes/{id}` | Remover intervenção | 204 / 404 |
| GET | `/api/intervencoes/tipo/{tipo}` | Buscar por tipo | 200 |
| GET | `/api/intervencoes/responsavel/{resp}` | Buscar por responsável | 200 |
| GET | `/api/intervencoes/trecho/{km}` | Buscar por trecho | 200 |

### Relatórios de Prioridade

| Método | Rota | Descrição | Status |
|---|---|---|---|
| POST | `/api/relatorios` | Gerar relatório atual | 201 |
| GET | `/api/relatorios` | Listar histórico completo | 200 |
| GET | `/api/relatorios/periodo?inicio=...&fim=...` | Buscar por período | 200 |

## Exemplos cURL

### Criar Equipe

```bash
curl -X POST http://localhost:8080/api/equipes \
  -H "Content-Type: application/json" \
  -d '{"nome": "Equipe Leste", "especialidade": "ROCADA", "direcao": "LESTE"}'
```

### Listar Equipes

```bash
curl http://localhost:8080/api/equipes
```

### Criar Trecho

```bash
curl -X POST http://localhost:8080/api/trechos \
  -H "Content-Type: application/json" \
  -d '{"km": 250, "alturaVegetacao": 75.0, "tipoTerreno": "UMIDO", "equipeId": 1}'
```

### Listar Trechos

```bash
curl http://localhost:8080/api/trechos
```

### Criar Intervenção

```bash
curl -X POST http://localhost:8080/api/intervencoes \
  -H "Content-Type: application/json" \
  -d '{"kmTrecho": 250, "tipo": "ROCADA_MECANIZADA", "responsavel": "Equipe Leste"}'
```

### Listar Intervenções

```bash
curl http://localhost:8080/api/intervencoes
```

### Gerar Relatório de Prioridade

```bash
curl -X POST http://localhost:8080/api/relatorios
```

### Listar Histórico de Relatórios

```bash
curl http://localhost:8080/api/relatorios
```

### Buscar Relatórios por Período

```bash
curl "http://localhost:8080/api/relatorios/periodo?inicio=2024-01-01T00:00:00&fim=2024-12-31T23:59:59"
```

### Buscar Trechos por Altura Mínima

```bash
curl http://localhost:8080/api/trechos/altura-minima/50.0
```

### Atualizar Trecho

```bash
curl -X PUT http://localhost:8080/api/trechos/250 \
  -H "Content-Type: application/json" \
  -d '{"alturaVegetacao": 30.0, "tipoTerreno": "SECO", "equipeId": 1}'
```

### Deletar Trecho

```bash
curl -X DELETE http://localhost:8080/api/trechos/250
```

## Perguntas de Reflexão

### 1. Por que o Repository é uma interface e não uma classe? Quem escreve a implementação e quando?

O Repository é uma interface porque o Spring Data JPA gera a implementação automaticamente em tempo de execução. Quando definimos uma interface que estende `JpaRepository<T, ID>`, o Spring cria uma classe proxy dinâmica que implementa todos os métodos CRUD básicos e também gera as queries derivadas a partir dos nomes dos métodos (ex: `findByTipo`, `findByAlturaVegetacaoGreaterThanEqual`). Isso acontece durante a inicialização da aplicação (startup), quando o Spring escaneia os pacotes, encontra as interfaces anotadas com `@Repository`, e injeta as implementações geradas. O desenvolvedor só precisa declarar a interface com a assinatura correta — o Spring cuida do resto.

### 2. O pattern DAO da Sprint 3 "morreu" na migração ou só mudou de forma? Explique com as suas palavras.

O pattern DAO não morreu, ele foi absorvido e evoluiu. Na Sprint 3, tínhamos classes DAO manuais (ex: `EquipeManutencaoDAO`) com ~40 linhas por operação, escrevendo SQL manualmente com `PreparedStatement`, tratando `Connection`, `ResultSet`, etc. Na Sprint 4, o Repository do Spring Data JPA é essencialmente um DAO gerado automaticamente. A responsabilidade de acesso a dados continua existindo, mas agora está encapsulada pelo framework. A diferença é que não escrevemos mais SQL manual — o Spring gera as queries baseadas nas convenções de nomenclatura dos métodos. O conceito de "Data Access Object" permanece, mas a implementação mudou de manual para declarativa.

### 3. Por que a validação de nivelVegetacao >= 0 (Sprint 1) deve ficar no Service e não no Controller?

A validação deve ficar no Service porque é uma regra de negócio, e o Controller deve apenas orquestrar a requisição HTTP. Se colocássemos a validação no Controller, estaríamos misturando responsabilidades: o Controller trata de protocolo HTTP (status codes, parsing de JSON, rotas), enquanto o Service trata das regras de domínio (validações, cálculos, lógica de priorização). Além disso, se a validação estiver no Service, ela pode ser reutilizada por outros pontos da aplicação (ex: outro endpoint, um job agendado, testes unitários), garantindo consistência em todo o sistema. A separação de responsabilidades é um princípio fundamental da arquitetura em camadas.

### 4. No JDBC puro vocês escreviam SQL. Onde está o SQL do findByTipo()? Quem o gerou?

O SQL do `findByTipo()` não está escrito em nenhum lugar do código fonte. O Spring Data JPA gera o SQL automaticamente em tempo de execução com base no nome do método. Quando declaramos `List<TrechoRodovia> findByTipoTerreno(String tipoTerreno);`, o Spring interpreta o padrão "findBy" + nome do atributo e gera uma query JPQL/HQL equivalente a algo como `SELECT t FROM TrechoRodovia t WHERE t.tipoTerreno = :tipoTerreno`. Essa JPQL é então traduzida para SQL nativo do Oracle pelo Hibernate. Podemos ver o SQL gerado ativando `spring.jpa.show-sql=true` no `application.properties`. O desenvolvedor não escreve SQL manual — apenas segue as convenções de nomenclatura do Spring Data.

## Prints do Projeto executando

<img width="1373" height="337" alt="Captura de tela 2026-10-25 004859" src="https://github.com/user-attachments/assets/2958d116-0d3a-46ab-95aa-933aa3b0d8ee" />
<img width="1371" height="330" alt="Captura de tela 2026-10-25 004912" src="https://github.com/user-attachments/assets/768bc9f1-42aa-49c8-8fc1-aaaa592c3e9d" />
<img width="1380" height="451" alt="Captura de tela 2026-10-25 004923" src="https://github.com/user-attachments/assets/d2c89012-5c0a-4aca-ba69-4a93153471bb" />
<img width="1378" height="437" alt="Captura de tela 2026-10-25 004934" src="https://github.com/user-attachments/assets/e87cd9d8-de60-424b-bef7-021f322eb378" />
<img width="1376" height="405" alt="Captura de tela 2026-10-25 004946" src="https://github.com/user-attachments/assets/2a6fb764-e81f-48bd-b97b-d959ad62da38" />
<img width="1377" height="397" alt="Captura de tela 2026-10-25 004955" src="https://github.com/user-attachments/assets/0866914e-1b59-4375-be59-663cd4ec9f2d" />
<img width="1377" height="432" alt="Captura de tela 2026-10-25 005005" src="https://github.com/user-attachments/assets/37d7003c-29b3-4dde-92f5-ed9e20770839" />
<img width="1387" height="425" alt="Captura de tela 2026-10-25 005015" src="https://github.com/user-attachments/assets/7038daad-9135-46f3-9366-4abe13e659de" />
<img width="1378" height="442" alt="Captura de tela 2026-10-25 005040" src="https://github.com/user-attachments/assets/db747b3e-9b4e-40c5-95ec-345f605a6435" />
<img width="1378" height="430" alt="Captura de tela 2026-10-25 005049" src="https://github.com/user-attachments/assets/448b57ed-11b5-4823-a9ee-04e82fd06a61" />
<img width="1607" height="533" alt="Captura de tela 2026-10-25 005104" src="https://github.com/user-attachments/assets/f2d12faa-efa9-4fd9-8072-3fc48c918650" />
<img width="1610" height="578" alt="Captura de tela 2026-10-25 005110" src="https://github.com/user-attachments/assets/a8dfaf1a-26d8-4d6f-8d58-8687d36c7550" />


## Tecnologias Utilizadas

- Spring Boot 3.2.0
- Spring Data JPA
- Oracle JDBC Driver (ojdbc17)
- Oracle Database FIAP
- Maven
- Java 17
