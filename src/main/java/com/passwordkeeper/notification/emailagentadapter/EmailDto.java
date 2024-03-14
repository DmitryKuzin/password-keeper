package com.passwordkeeper.notification.emailagentadapter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDto {

    private String email;
    private String content;

    @Override
    public String toString() {
        return "EmailDto{" +
                "email='" + email + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
