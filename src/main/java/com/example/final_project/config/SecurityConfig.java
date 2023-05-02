package com.example.final_project.config;


import com.example.final_project.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig  {
    private final PersonDetailsService personDetailsService;

    // метод хэширования
    @Bean
    public PasswordEncoder getPasswordEncode(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // конфигурируем работу Spring Security
        http
                .authorizeHttpRequests() // указываем что все страницы должны быть защищены аутентификацией
                .requestMatchers("/admin").hasRole("ADMIN") // указываем на то, что страница /admin доступна пользователям с ролью ADMIN
                // c помощью permitAll указываем что перечисленные страницы доступны не аутентифицированным пользователям
                .requestMatchers("/authentication", "/error", "/registration", "/resources/**", "/static/**", "/css/**", "/js/**", "/img/**").permitAll()
                // все остальные страницы доступны аутентифицированным пользователям с данными ролями
                .anyRequest().hasAnyRole("USER", "ADMIN")
                .and() // указываем что дальше настраивается аутентификация и соединяем ее с настройкой доступа
                .formLogin().loginPage("/authentication") // указываем какой url запрос будет отправляться при заходе на защищенные страницы
                .loginProcessingUrl("/process_login") // указываем на какой адрес будут отправляться данные с формы. Нам уже не нужно будет создавать метод в контроллере и обрабатывать данные с формы. Мы задали url, который используется по умолчанию для обработки формы аутентификации по средствам Spring Security. Spring Security будет ждать объект с формы аутентификации и затем сверять логин и пароль с данными в БД
                .defaultSuccessUrl("/index", true) // указываем на какой url необходимо направить пользователя после успешной аутентификации. Вторым аргументом указывается true чтобы перенаправление шло в любом случае послу успешной аутентификации
                .failureUrl("/authentication?error") // указываем куда необходимо перенаправить пользователя при проваленной аутентификации. В запрос будет передан объект error, который будет проверяться на форме, и при наличии данного объекта в запросе будет выводиться сообщение "Неправильный логин или пароль"
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/authentication");
        return http.build();
    }

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
        authenticationManagerBuilder.userDetailsService(personDetailsService)
        .passwordEncoder(getPasswordEncode());
    }
}
