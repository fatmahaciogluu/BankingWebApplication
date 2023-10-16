package com.BankApp.BankWebApplication.BankWebAppTestControllers;


import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountHolderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAccountHolder() throws Exception {
        testAccountHolderGet();
        testAccountHolderPost();
        testAccountHolderDelete();
    }

    @Test
    public void testAccountHolderGet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts")
                        .with(testUser("admin", "ADMIN")))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username", Matchers.is("admin")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email", Matchers.is("admin@example.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].password", Matchers.is("$2a$12$X2xwwVG4dU6LZH4cA8x0UeFzl39cTZW1kg2Zcqe6a/kjylR1Hx6wa")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].address", Matchers.is("Istanbul, Turkey")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].roles", Matchers.is("ADMIN")));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/admin")
                        .with(testUser("admin", "ADMIN")))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/admin")
                        .with(testUser("user", "USER")))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


    @Test
    public void testAccountHolderPost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/accounts")
                        .with(testUser("fh", "USER"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType("application/json")
                        .content("{\"username\":\"user2\",\"password\":\"123\",\"email\":\"test2@gmail.com\",\"address\":\"Lahore\",\"roles\":\"USER\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/accounts")
                        .with(testUser("fh", "ADMIN"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType("application/json")
                        .content("{\"username\":\"user100\",\"password\":\"123\",\"email\":\"test100@gmail.com\",\"address\":\"Lahore\",\"roles\":\"USER\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void testAccountHolderDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/accounts/fh")
                        .with(testUser("user", "USER"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/accounts/admin")
                        .with(testUser("admin", "ADMIN"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }


    private RequestPostProcessor testUser(String userName, String authoriy) {
        return SecurityMockMvcRequestPostProcessors.user(userName).authorities(new SimpleGrantedAuthority(authoriy));
    }
}