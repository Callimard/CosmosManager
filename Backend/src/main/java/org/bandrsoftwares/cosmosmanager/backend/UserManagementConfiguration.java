package org.bandrsoftwares.cosmosmanager.backend;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "user-management")
public class UserManagementConfiguration {

    private String emailSendFrom;

    private String emailValidationUrl;

    private String frontResetPasswordUrl;
}
