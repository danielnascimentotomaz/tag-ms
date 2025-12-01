package com.gft.tag_ms.producer;

import com.gft.tag_ms.dto.EtiquetaExcluidaMensage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EtiquetaExcluidaProducer implements IEtiquetaExcluidaProducer{

    //<serviço>.<domínio>.exchange
    public static   final String EXCHANGE_NAME = "tag-ms.etiqueta.exchange";


    //<serviço>.<domínio>.<ação>.routing-key
    public  static  final String ROUTING_KEY_Name = "tag-ms.etiqueta.excluida.rk";


    private final RabbitTemplate rabbitTemplate;

    public EtiquetaExcluidaProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void notifyEtiquetaExcluida(EtiquetaExcluidaMensage message) {
        log.info("Enviando mesagem de etiqueta excluida:{}",message);

        rabbitTemplate.convertAndSend(EXCHANGE_NAME,ROUTING_KEY_Name,message);
    }
}
