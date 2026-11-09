# Sockets TCP e UDP em Java

Clientes e servidores que enviam uma mensagem e devolvem o mesmo texto. O TCP usa uma conexão e lê uma linha; o UDP envia e recebe datagramas.

## Compilar

Com JDK 17 ou superior, na pasta do projeto:

```sh
javac -encoding UTF-8 *.java
```

## Executar

Para TCP, inicie o servidor em um terminal:

```sh
java TcpServer
```

Em outro terminal, execute o cliente:

```sh
java TcpClient
```

Para UDP, siga o mesmo processo:

```sh
java UdpServer
```

```sh
java UdpClient
```

Digite `Olá, servidor!` no cliente:

```text
Mensagem: Olá, servidor!
Resposta: Olá, servidor!
```

O servidor exibe `Recebido: Olá, servidor!` e continua aguardando novos clientes. Ambos os protocolos usam a porta 5000. No UDP, use mensagens de até 1024 bytes e mantenha o servidor ativo, pois o cliente aguarda a resposta sem timeout. Encerre o servidor com `Ctrl+C`.
