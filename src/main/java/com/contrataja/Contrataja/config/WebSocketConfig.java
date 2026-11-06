    package com.contrataja.Contrataja.config;

    import org.springframework.context.annotation.Configuration;
    import org.springframework.messaging.simp.config.MessageBrokerRegistry;
    import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
    import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
    import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

    // essa anotação diz ao spring que essa classe contém configurações da aplicação
    @Configuration
    //Ativa o websocket com suporte a mensgens usando o protocolo stomp, que organiza as mensagens em destinos
    @EnableWebSocketMessageBroker
    //classe qye implementa  a interface de configuração do websokcet, permitiindo personalizar o broker e os endpoints
    public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
        // indica que eu estou sobreescrevendo um metodo da interface, trocando o comportamento padrão pela minha configuração
        @Override
        // método que configura o broker, que é quem recebe as mensagens e entrega para os clientes incritos
        //broker é o intermediaroi das mensagens, ele recebe o que um usuario envia e repassa para todos que estão incritos naquele destino
        public void configureMessageBroker(MessageBrokerRegistry config) {
            //atiava um broker simples em memoria e define que os destinos que começam com /topic são os canais de saida, por onde o servidor envia mensagen para os clientes
            config.enableSimpleBroker("/topic");
            //define que as mensagens enviadas pelos usuarios para o servidor começam com /app, é o canal de entrada, e o spring encaminha essas mensagens para os metodos com @Messagemapping
            config.setApplicationDestinationPrefixes("/app");
        }

        @Override
        //metodo que registra o endpoint de entrada da conexão websocket, por onde o front se conecta ao servidor
        public void registerStompEndpoints(StompEndpointRegistry registry) {
            //define o caminho /ws como a pporta de entrada da  conexão webspcket, é nesse endereço que o front se conecta
            registry.addEndpoint("/ws")
                    //libera o aceeso de qualquer origem ou seja qualquer aplicação consegue se conectar * é todas as origens
                    .setAllowedOriginPatterns("*");
        }
    }