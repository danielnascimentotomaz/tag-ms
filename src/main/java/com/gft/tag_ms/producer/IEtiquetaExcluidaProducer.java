package com.gft.tag_ms.producer;

import com.gft.tag_ms.dto.EtiquetaExcluidaMensage;

public interface IEtiquetaExcluidaProducer {

    void notifyEtiquetaExcluida(final EtiquetaExcluidaMensage mensage);
}
