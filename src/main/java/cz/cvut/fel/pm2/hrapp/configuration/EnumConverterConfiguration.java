package cz.cvut.fel.pm2.hrapp.configuration;


import cz.cvut.fel.pm2.hrapp.model.enums.ContractType;
import cz.cvut.fel.pm2.hrapp.model.enums.UserRole;
import cz.cvut.fel.pm2.hrapp.model.enums.WorkPercentage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class EnumConverterConfiguration {

    @Bean(name = "org.openapitools.configuration.EnumConverterConfiguration.contractTypeConverter")
    Converter<String, ContractType> contractTypeConverter() {
        return new Converter<String, ContractType>() {
            @Override
            public ContractType convert(String source) {
                return ContractType.fromValue(source);
            }
        };
    }
    @Bean(name = "org.openapitools.configuration.EnumConverterConfiguration.userRoleConverter")
    Converter<String, UserRole> userRoleConverter() {
        return new Converter<String, UserRole>() {
            @Override
            public UserRole convert(String source) {
                return UserRole.fromValue(source);
            }
        };
    }
    @Bean(name = "org.openapitools.configuration.EnumConverterConfiguration.workPercentageConverter")
    Converter<String, WorkPercentage> workPercentageConverter() {
        return new Converter<String, WorkPercentage>() {
            @Override
            public WorkPercentage convert(String source) {
                return WorkPercentage.fromValue(source);
            }
        };
    }

}
