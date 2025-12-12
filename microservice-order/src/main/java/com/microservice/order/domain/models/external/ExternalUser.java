package com.microservice.order.domain.models.external;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class ExternalUser {
    private String name;
    private String lastName;
    private String secondName;
    private String secondLastname;
    private String phone;
    private UUID gender;
    private String avatarUrl;
    private Date dateOfBirthday;
    private LocalDateTime createAt;
    private LocalDateTime updatedAt;
    private Boolean isDelete;

}
