# Sockets TCP e UDP em Java

Dois exemplos de sockets com dois testes: enviar 1.000 mensagens numeradas ou uma mensagem de 1.500 bytes. No TCP, o cliente envia linhas pela mesma conexão. No UDP, envia um datagrama por mensagem. Os servidores exibem o conteúdo e o tamanho recebido, sem enviar respostas.

## 1. Preparar

Com JDK 17 ou superior, compile na pasta do projeto:

```sh
javac -encoding UTF-8 *.java
```

Para executar em duas máquinas, compile nas duas. Descubra o IP da máquina do servidor; no Linux, use:

```sh
hostname -I
```

Nos comandos abaixo, substitua `192.168.1.10` pelo IP do servidor acessível pela outra máquina. As máquinas precisam ter comunicação pela rede e permitir a porta 5000 no firewall para o protocolo utilizado.

Para testar na mesma máquina, use `localhost` ou execute os clientes sem argumento.

## 2. Demonstrar TCP

No terminal do servidor:

```sh
java TcpServer
```

No terminal do cliente:

```sh
java TcpClient 192.168.1.10
```

O servidor mostra o endereço e a porta de origem do cliente, recebe as linhas e identifica o encerramento da conexão:

```text
Conectado: /192.168.1.20:49152
Recebido (10 caracteres): Mensagem 1
Recebido (10 caracteres): Mensagem 2
...
Recebido (13 caracteres): Mensagem 1000
Conexão encerrada: /192.168.1.20:49152
```

O IP e a porta de origem variam. Execute o cliente novamente para mostrar uma nova conexão.

Pontos para explicar no código:

- `ServerSocket(5000)` abre a porta TCP do servidor.
- `accept()` espera uma conexão e retorna o socket daquele cliente.
- `new Socket(host, 5000)` estabelece a conexão no cliente.
- O `for` envia as 1.000 linhas usando a mesma conexão.
- `newLine()` separa as mensagens; `readLine()` lê até essa separação.
- TCP transporta um fluxo de bytes: uma escrita não corresponde necessariamente a um pacote ou a uma leitura no destino.
- `flush()` envia os dados acumulados no buffer da aplicação para o socket.
- O fechamento do cliente faz `readLine()` retornar `null` após a leitura dos dados restantes. O servidor volta ao `accept()`.
- TCP oferece entrega confiável e ordenada dos bytes. Uma falha de conexão pode interromper a transferência; este exemplo não recupera falhas nem confirma o processamento no servidor.

## 3. Demonstrar UDP

No terminal do servidor:

```sh
java UdpServer
```

No terminal do cliente:

```sh
java UdpClient 192.168.1.10
```

O servidor mostra cada mensagem junto do endereço e da porta de origem:

```text
Recebido de /192.168.1.20:49153 (10 bytes): Mensagem 1
Recebido de /192.168.1.20:49153 (10 bytes): Mensagem 2
...
```

Observe a numeração recebida. Podem aparecer lacunas ou mensagens fora de ordem, mas isso não precisa acontecer. Execute o cliente novamente para repetir o envio; a numeração recomeça em 1.

Pontos para explicar no código:

- `DatagramSocket(5000)` abre a porta UDP do servidor; não há `accept()`.
- `new DatagramSocket()` deixa o sistema escolher a porta de origem do cliente.
- Cada `DatagramPacket` contém os bytes da mensagem e, no envio, o IP e a porta de destino.
- `send()` envia um datagrama; `receive()` espera um datagrama e informa seu remetente.
- UDP preserva os limites dos datagramas. `getLength()` indica quantos bytes foram recebidos, evitando ler a parte não utilizada do buffer.
- O buffer tem 1024 bytes, suficientes para as mensagens numeradas. Datagramas maiores que o buffer são truncados na recepção.
- UDP não garante entrega, ordem ou ausência de duplicação. O número na mensagem permite observar esses efeitos, sem corrigi-los.
- O servidor continua esperando datagramas: não há conexão cujo fechamento sinalize o fim das 1.000 mensagens.

## 4. Demonstrar uma mensagem maior que o buffer UDP

Mantenha os servidores ativos e execute os clientes com o segundo argumento `grande`:

```sh
java TcpClient 192.168.1.10 grande
java UdpClient 192.168.1.10 grande
```

Na mesma máquina:

```sh
java TcpClient localhost grande
java UdpClient localhost grande
```

Cada cliente envia uma única mensagem criada com `"A".repeat(1500)`. Cada `A` ocupa um byte em UTF-8, portanto a mensagem tem 1.500 caracteres e 1.500 bytes. O TCP acrescenta a quebra de linha, que `readLine()` remove na leitura.

Observe os tamanhos informados nos servidores:

| TCP | UDP |
|---|---|
| Recebe 1.500 caracteres | Recebe 1.024 bytes, se o datagrama chegar |
| `readLine()` monta a linha lendo o fluxo até a quebra de linha | `receive()` copia apenas o que cabe no buffer de 1.024 bytes |
| A linha pode chegar por várias leituras internas | Os 476 bytes restantes do datagrama são descartados |

Pontos para explicar:

- A próxima chamada a `receive()` espera outro datagrama; ela não recupera os 476 bytes descartados.
- Esse truncamento é causado pelo tamanho do buffer da aplicação. UDP não tem um limite de 1.024 bytes.
- No TCP, receber a linha completa não significa que ela viajou em um único pacote.
- O TCP exibe `message.length()` e o UDP exibe `packet.getLength()`. Neste teste com `A`, caracteres e bytes têm o mesmo tamanho; textos com acentos podem ocupar mais bytes em UTF-8.

Para mostrar a correção, altere apenas `new byte[1024]` para `new byte[2048]` em `UdpServer.java`. Encerre o servidor UDP com `Ctrl+C`, compile e inicie novamente:

```sh
javac -encoding UTF-8 UdpServer.java
java UdpServer
```

Execute `java UdpClient 192.168.1.10 grande` novamente. Se o datagrama chegar, o servidor agora exibirá 1.500 bytes. Depois da demonstração, restaure o buffer para 1.024 bytes e recompile para repetir o teste original.

Encerre os servidores com `Ctrl+C`. Sem o argumento `grande`, os clientes continuam enviando as 1.000 mensagens numeradas.

## 5. Comparar

| Característica | TCP | UDP |
|---|---|---|
| Comunicação | Conexão estabelecida antes do envio | Datagramas enviados sem estabelecer conexão |
| Unidade vista pela aplicação | Fluxo de bytes, separado em linhas neste exemplo | Um datagrama por mensagem neste exemplo |
| Entrega e ordem | Bytes entregues em ordem, com retransmissão pelo protocolo | Sem garantia de entrega ou ordem |
| Identificação do remetente | Obtida do socket da conexão | Obtida de cada datagrama |
| Fim do envio neste exemplo | Fechamento da conexão pelo cliente | Não existe sinalização de término ao servidor |

Os dois servidores usam o número 5000, mas TCP e UDP têm espaços de portas separados e podem executar juntos. A porta de origem dos clientes é escolhida pelo sistema. Estes servidores processam os recebimentos sequencialmente, sem threads adicionais.

A mensagem final do cliente indica que ele concluiu o envio local; não confirma que o servidor processou as mensagens. Receber todos os datagramas em uma execução não transforma UDP em um protocolo confiável. Este exemplo também não mede qual protocolo é mais rápido: a rede, os buffers e as impressões no terminal influenciam o resultado.
