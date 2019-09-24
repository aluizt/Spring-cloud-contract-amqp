package br.com.rabbitspringcloudcontractconsumidor;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGoesOnlineMessage {
    private String user;
}
