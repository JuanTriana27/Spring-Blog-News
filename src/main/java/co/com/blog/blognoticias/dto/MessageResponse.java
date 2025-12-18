package co.com.blog.blognoticias.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageResponse {
    private String mensaje;
    private Object data; // Para enviar datos adicionales si es necesario
    private String codigoError; // Opcional, para códigos de error específicos

    public MessageResponse(String mensaje) {
        this.mensaje = mensaje;
    }

    public MessageResponse(String mensaje, Object data) {
        this.mensaje = mensaje;
        this.data = data;
    }
}