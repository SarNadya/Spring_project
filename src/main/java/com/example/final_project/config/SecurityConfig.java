package com.example.final_project.config;


import com.example.final_project.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig  {
    private final PersonDetailsService personDetailsService;

    // отключаем защиту паролей в рамках тестирования приложения
    @Bean
    public PasswordEncoder getPasswordEncode(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // конфигурируем работу Spring Security
        http
                .csrf().disable() // отключаем защиту от межсайтовой подделки запросов
                .authorizeHttpRequests() // указываем что все страницы должны быть защищены аутентификацией
                // указываем что не аутентифицированные пользователи могут зайти на страницу аутентификации и на объект ошибки
                // c помощью permitAll указываем что не аутентифицированные пользователи могут заходить на перечисленные страницы
                .requestMatchers("/authentication", "/error").permitAll()
                .anyRequest().authenticated()
                .and() // указываем что дальше настраивается аутентификация и соединяем ее с настройкой доступа
                .formLogin().loginPage("/authentication") // указываем какой url запрос будет отправляться при заходе на защищенные страницы
                .loginProcessingUrl("/process_login") // указываем на какой адрес будут отправляться данные с формы. Нам уже не нужно будет создавать метод в контроллере и обрабатывать данные с формы. Мы задали url, который используется по умолчанию для обработки формы аутентификации по средствам Spring Security. Spring Security будет ждать объект с формы аутентификации и затем сверять логин и пароль с данными в БД
                .defaultSuccessUrl("/index", true) // Указываем на какой url необходимо направить пользователя после успешной аутентификации. Вторым аргументом указывается true чтобы перенаправление шло в любом случае послу успешной аутентификации
                .failureUrl("/authentication?error"); // Указываем куда необходимо перенаправить пользователя при проваленной аутентификации. В запрос будет передан объект error, который будет проверяться на форме, и при наличии данного объекта в запросе будет выводиться сообщение "Неправильный логин или пароль"
        return http.build();
    }

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
        authenticationManagerBuilder.userDetailsService(personDetailsService);
    }
}
