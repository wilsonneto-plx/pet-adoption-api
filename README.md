<div align="center">

# 🐾 Pet Adoption API

Sistema de gerenciamento de adoção de pets desenvolvido como uma **API REST** utilizando **Java**, **Spring Boot** e **MySQL**.

<p>
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" />
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Data_JPA-59666C?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Lombok-BC4521?style=for-the-badge&logo=lombok&logoColor=white" />
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" />
  <img src="https://img.shields.io/badge/JUnit_5-25A162?style=for-the-badge&logo=junit5&logoColor=white" />
  <img src="https://img.shields.io/badge/Mockito-78A641?style=for-the-badge" />
</p>

</div>

---

<table>
<tr>

<td width="180" align="center">

<img src="https://github.com/wilsonneto-plx.png" width="150" alt="Wilson de Andrade Veloso Neto">

</td>

<td>

## 👨‍💻 Backend Java Developer

**Wilson de Andrade Veloso Neto**

🎓 Bacharelando em Ciência da Computação — UESPI

📚 Aprimorando conhecimentos em Java e Backend pela Alura

☕ Java | Spring Boot | APIs REST | JUnit 5 | Mockito | Postgress | MySQL

<p>

<a href="https://github.com/wilsonneto-plx">
<img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white">
</a>

<a href="https://www.linkedin.com/in/wilson-neto-5b1207398/">
<img src="https://img.shields.io/badge/LinkedIn-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white">
</a>

</p>

</td>

</tr>
</table>

---

# 📖 Sobre

A **Pet Adoption API** é uma API REST desenvolvida para gerenciar o processo de adoção de pets.

A aplicação oferece operações completas de **CRUD** para **Pets** e **Tutores**, além do gerenciamento de **Adoções**, permitindo cadastrá-las, listá-las, aprová-las e reprová-las.

O projeto foi desenvolvido seguindo boas práticas de desenvolvimento com o ecossistema **Spring**, utilizando **arquitetura em camadas**, **DTOs**, **Bean Validation**, **tratamento global de exceções** e **testes automatizados** para garantir a confiabilidade da aplicação.

---

# ✨ Principais características

* ✅ API REST seguindo boas práticas
* ✅ Arquitetura em camadas
* ✅ Persistência de dados com Spring Data JPA
* ✅ Banco de dados MySQL
* ✅ DTOs para comunicação entre camadas
* ✅ Validação de dados com Bean Validation
* ✅ Tratamento global de exceções
* ✅ Testes unitários com JUnit 5 e Mockito
* ✅ Testes de Controller utilizando MockMvc

---

# 🏗️ Arquitetura do projeto

O projeto foi desenvolvido seguindo uma **arquitetura em camadas**, buscando uma melhor organização das responsabilidades, separação das regras de negócio e facilidade de manutenção.

A aplicação está estruturada nos seguintes pacotes:

```text
📦 src/main/java/adopet.api
│
├── 📂 controller
│   └── Responsável pelos endpoints REST e gerenciamento das requisições HTTP
│
├── 📂 dto
│   └── Objetos de transferência de dados entre as camadas da aplicação
│
├── 📂 exception
│   └── Gerenciamento e tratamento global das exceções da aplicação
│
├── 📂 model
│   └── Contém as entidades JPA que representam os dados persistidos
│
├── 📂 repository
│   └── Responsável pela comunicação com o banco de dados através do Spring Data JPA
│
├── 📂 service
│   └── Contém as regras de negócio e lógica da aplicação
│
├── 📂 validacoes
│   └── Regras de validação dos dados recebidos pela API
│
└── 📂 config
    └── Configurações da documentação Swagger/OpenAPI

Os testes automatizados estão organizados separadamente em:

📦 src/test/java/adopet.api
│
├── 📂 service
│   └── Testes das regras de negócio utilizando JUnit 5 e Mockito
│
└── 📂 controller
    └── Testes dos endpoints REST utilizando MockMvc
```

---

## 📷 Documentação da API

A documentação dos endpoints foi criada utilizando **Swagger/OpenAPI**.

<p align="center">
  <img src="docs/swagger-overview.png" width="900">
</p>

## 📷 Exemplo de endpoint

Exemplo da documentação de um endpoint da API utilizando Swagger/OpenAPI.

<p align="center">
  <img src="docs/swagger-pet-endpoint.png" width="900">
</p>
