# 📡 Mensageria Entre os Microsserviços

## 🎯 Objetivo

Garantir que, ao excluir **Palavras** ou **Etiquetas**, o
**Relationship-Service** receba eventos de exclusão e remova
relacionamentos órfãos automaticamente.

------------------------------------------------------------------------

# 🧩 Arquitetura da Mensageria

    [Word-Service]  -- envia -->  fila: palavras  --> [Relationship-Service]

    [Tag-Service]   -- envia -->  fila: etiquetas --> [Relationship-Service]

Cada serviço emite eventos do seu domínio, enquanto o
Relationship-Service consome ambos.

------------------------------------------------------------------------

# 📨 Filas Utilizadas

  ----------------------------------------------------------------------------------
  Serviço Emissor    Evento Emitido           Nome da Fila    Consumidor
  ------------------ ------------------------ --------------- ----------------------
  Word-Service       palavra.excluida         palavras        Relationship-Service
  ----------------------------------------------------------------------------------
  
  Tag-Service        etiqueta.excluida        etiquetas       Relationship-Service
  ----------------------------------------------------------------------------------

------------------------------------------------------------------------

# 📤 Publicação de Eventos

## Word-Service

``` java
rabbitTemplate.convertAndSend("palavras", idPalavra);
```

## Tag-Service

``` java
rabbitTemplate.convertAndSend("etiquetas", idEtiqueta);
```

------------------------------------------------------------------------

# 📥 Consumo no Relationship-Service

## Listener para Palavras

``` java
@RabbitListener(queues = "palavras")
public void onPalavraExcluida(Long id) {
    relacionamentoRepository.deleteByIdPalavra(id);
}
```

## Listener para Etiquetas

``` java
@RabbitListener(queues = "etiquetas")
public void onEtiquetaExcluida(Long id) {
    relacionamentoRepository.deleteByIdEtiqueta(id);
}
```

------------------------------------------------------------------------

# 🧹 Limpeza Automática no Banco


# 🎨 Ilustração

    📦 TABELA ANTES
    ---------------------------------
    | idPalavra | idEtiqueta |
    ---------------------------------
    |    10     |     5      |
    |    3      |     5      |
    |    7      |     2      |
    ---------------------------------

    ❌ Eventos recebidos:
      - palavra.excluida(10)
      - palavra.excluida(3)

    🔧 Relationship-Service limpando...

    📦 TABELA DEPOIS
    ---------------------------------
    | idPalavra | idEtiqueta |
    ---------------------------------
    |     7     |     2      |
    ---------------------------------
