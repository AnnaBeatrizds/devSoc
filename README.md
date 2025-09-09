# SOC - Avaliação Dev

## _Projeto de avaliação para desenvolvedores Java_

Este projeto consiste num sistema de cadastro de funcionários, onde os usuários podem cadastrar novos funcionários, consultar informações existentes e aceder a dados através de serviços web.

---

### Conteúdo do Projeto

O projeto inclui:

* **CRUD (Create, Read, Update, Delete)** para funcionários (inserção, busca, edição e exclusão).
* **Gerenciamento de agendas** e compromissos com exames.
* **Serviço Web SOAP** para a busca de funcionários.
* **Funcionalidade de relatórios** em HTML e Excel.

---
### Telas da Aplicação

**Dashboard**

O dashboard oferece uma visão geral do sistema com estatísticas rápidas, como o número de 
funcionários ativos, compromissos agendados para o dia e exames periódicos a vencer. Também apresenta um gráfico de exames realizados por mês.

<img width="1345" height="624" alt="image" src="https://github.com/user-attachments/assets/16a03eeb-e43f-46a2-a992-72fcceafaa1a" />



**Funcionários**

A tela de listagem de funcionários permite a busca por ID ou nome e oferece opções para editar ou excluir registros.

<img width="1364" height="638" alt="image" src="https://github.com/user-attachments/assets/62e0588b-a976-487a-bcc5-2455c723e348" />

**Lista de Agendas**

A tela de listagem de agenda permite a busca por ID ou nome e oferece opções para criar, editar ou excluir registros.

<img width="1357" height="637" alt="image" src="https://github.com/user-attachments/assets/cdd68f2b-1b70-4026-a2d6-b0a772d31109" />

**Agendamento de Compromissos**

O fluxo de agendamento começa na página de agendamento (agendamento.jsp), onde o usuário seleciona o funcionário, 
a agenda e a data. Em seguida, a aplicação mostra os horários disponíveis e permite a seleção de exames.

<img width="1364" height="642" alt="image" src="https://github.com/user-attachments/assets/a16519dc-d9c5-434c-8d87-f93d6439a9ae" />

**Lista de Compromissos**

A tela de listagem de compromissos permite a busca por ID ou nome por funcionario e oferece opções para editar ou excluir registros

<img width="1348" height="634" alt="image" src="https://github.com/user-attachments/assets/670547eb-0f8f-460e-85ae-a865ecba4cdc" />


**Relatórios**

A página de relatórios permite filtrar compromissos por um período de tempo, gerando uma tabela que pode ser
visualizada na própria tela, impressa ou exportada para um arquivo Excel.

<img width="1354" height="636" alt="image" src="https://github.com/user-attachments/assets/1f30471b-685c-46c3-8cbb-8ea9f6684a9c" />


### Tecnologias Utilizadas

O projeto utiliza as seguintes tecnologias:

* **Java 8**: A linguagem base da aplicação.
* **Apache Struts 2**: Framework para gerenciar a camada web.
* **Apache Maven**: Ferramenta de gerenciamento de dependências e construção do projeto.
* **H2 Database**: Banco de dados em memória utilizado em tempo de execução.
* **Jetty**: Servidor web embutido para rodar a aplicação.
* **Bootstrap 5**: Framework CSS para o layout e estilo das páginas JSP.
* **JAX-WS (SOAP)**: Para a implementação de serviços web.
* **Apache POI**: Para gerar relatórios em Excel.

---

### Instalação

#### Pré-Requisitos

* Java 8 JDK instalado.
* Ferramenta de controle de versão **Git** instalada.
* **Opcional**: Eclipse IDE, Maven e SoapUI para desenvolvimento e testes.

#### Aceder ao Projeto

Abra o terminal e navegue até a pasta onde deseja clonar o projeto. Utilize o seguinte comando para clonar o repositório:

```bash
git clone https://code-commit-public-at-199807956881:9EIl03WdaNtYgqodZOOsGBjY14ZUR8f9okxICwJ6Mxo=@[git-codecommit.sa-east-1.amazonaws.com/v1/repos/AvaliacaoDev](https://git-codecommit.sa-east-1.amazonaws.com/v1/repos/AvaliacaoDev)

<img width="1365" height="626" alt="image" src="https://github.com/user-attachments/assets/b2e4c10d-4572-470b-a151-9219a2fc5e35" />

v
