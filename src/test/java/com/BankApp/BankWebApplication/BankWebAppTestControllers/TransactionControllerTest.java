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
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testTransaction() throws Exception {
        testTransactionDeposit();
        testTransactionWithdraw();
        testTransactionTransfer();
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
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].address", Matchers.is("Lahore, Pakistan")))
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
    public void testTransactionDeposit() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/transactions/deposit")
                        .with(testUser("atif", "USER"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType("application/json")
                        .content("{\"amount\":\"500\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/transactions/deposit")
                        .with(testUser("admin", "ADMIN"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType("application/json")
                        .content("{\"amount\":\"500\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testTransactionTransfer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/transactions/transfer?receiver=user")
                        .with(testUser("atif", "USER"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType("application/json")
                        .content("{\"amount\":\"50\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/transactions/transfer?receiver=user")
                        .with(testUser("admin", "ADMIN"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType("application/json")
                        .content("{\"amount\":\"50\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testTransactionWithdraw() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/transactions/withdraw")
                        .with(testUser("atif", "USER"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType("application/json")
                        .content("{\"amount\":\"50\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/transactions/withdraw")
                        .with(testUser("admin", "ADMIN"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType("application/json")
                        .content("{\"amount\":\"50\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    private RequestPostProcessor testUser(String userName, String authoriy) {
        return SecurityMockMvcRequestPostProcessors.user(userName).authorities(new SimpleGrantedAuthority(authoriy));
    }
}
