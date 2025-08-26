package com.granja.service;

import com.granja.dto.CustomerDTO;
import com.granja.entity.Customer;
import com.granja.repository.CustomerRepository;
import com.granja.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o CustomerService.
 * 
 * <p>
 * Testa todas as operações CRUD e validações
 * relacionadas ao gerenciamento de clientes.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("João Silva");
        customer.setCpf("12345678901");
        customer.setDiscountEligible(true);
        customer.setRegistrationDate(LocalDateTime.now());

        customerDTO = CustomerDTO.builder()
                .name("João Silva")
                .cpf("12345678901")
                .discountEligible(true)
                .build();
    }

    @Test
    void deveCriarClienteComSucesso() {
        // Given
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // When
        CustomerDTO resultado = customerService.createCustomer(customerDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getName());
        assertEquals("12345678901", resultado.getCpf());
        assertTrue(resultado.getDiscountEligible());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void deveFalharAoCriarClienteComNomeVazio() {
        // Given
        CustomerDTO customerDTOInvalido = CustomerDTO.builder()
                .name("")
                .cpf("12345678901")
                .discountEligible(true)
                .build();

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            customerService.createCustomer(customerDTOInvalido);
        });
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void deveFalharAoCriarClienteComCPFInvalido() {
        // Given
        CustomerDTO customerDTOInvalido = CustomerDTO.builder()
                .name("João Silva")
                .cpf("123")
                .discountEligible(true)
                .build();

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            customerService.createCustomer(customerDTOInvalido);
        });
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void deveFalharAoCriarClienteComCPFDuplicado() {
        // Given
        when(customerRepository.existsByCpf("12345678901")).thenReturn(true);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            customerService.createCustomer(customerDTO);
        });
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void deveAtualizarClienteComSucesso() {
        // Given
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDTO customerDTOAtualizado = CustomerDTO.builder()
                .name("João Silva Atualizado")
                .cpf("12345678901")
                .discountEligible(false)
                .build();

        // When
        CustomerDTO resultado = customerService.updateCustomer(1L, customerDTOAtualizado);

        // Then
        assertNotNull(resultado);
        assertEquals("João Silva Atualizado", resultado.getName());
        assertFalse(resultado.getDiscountEligible());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void deveFalharAoAtualizarClienteInexistente() {
        // Given
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            customerService.updateCustomer(999L, customerDTO);
        });
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void deveFalharAoAtualizarClienteComCPFDuplicado() {
        // Given
        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setCpf("98765432100");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.findByCpf("98765432100")).thenReturn(Optional.of(customer2));

        CustomerDTO customerDTOAtualizado = CustomerDTO.builder()
                .name("João Silva")
                .cpf("98765432100")
                .discountEligible(true)
                .build();

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            customerService.updateCustomer(1L, customerDTOAtualizado);
        });
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void deveRecuperarClientePorId() {
        // Given
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        // When
        CustomerDTO resultado = customerService.getCustomerById(1L);

        // Then
        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getName());
        assertEquals(1L, resultado.getId());
    }

    @Test
    void deveFalharAoRecuperarClienteInexistente() {
        // Given
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            customerService.getCustomerById(999L);
        });
    }

    @Test
    void deveRecuperarTodosOsClientes() {
        // Given
        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setName("Maria Santos");
        customer2.setCpf("98765432100");
        customer2.setDiscountEligible(false);

        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer, customer2));

        // When
        List<CustomerDTO> resultado = customerService.getAllCustomers();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("João Silva", resultado.get(0).getName());
        assertEquals("Maria Santos", resultado.get(1).getName());
    }

    @Test
    void deveRecuperarClientePorCPF() {
        // Given
        when(customerRepository.findByCpf("12345678901")).thenReturn(Optional.of(customer));

        // When
        CustomerDTO resultado = customerService.getCustomerById(1L);

        // Then
        assertNotNull(resultado);
        assertEquals("12345678901", resultado.getCpf());
    }

    @Test
    void deveFalharAoRecuperarClientePorCPFInexistente() {
        // Given
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            customerService.getCustomerById(999L);
        });
    }

    @Test
    void deveRecuperarClientesPorElegibilidadeDeDesconto() {
        // Given
        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setName("Maria Santos");
        customer2.setCpf("98765432100");
        customer2.setDiscountEligible(false);

        when(customerRepository.findByDiscountEligible(true)).thenReturn(Arrays.asList(customer));
        when(customerRepository.findByDiscountEligible(false)).thenReturn(Arrays.asList(customer2));

        // When
        List<CustomerDTO> clientesComDesconto = customerService.getCustomersByDiscountEligibility(true);
        List<CustomerDTO> clientesSemDesconto = customerService.getCustomersByDiscountEligibility(false);

        // Then
        assertNotNull(clientesComDesconto);
        assertEquals(1, clientesComDesconto.size());
        assertTrue(clientesComDesconto.get(0).getDiscountEligible());

        assertNotNull(clientesSemDesconto);
        assertEquals(1, clientesSemDesconto.size());
        assertFalse(clientesSemDesconto.get(0).getDiscountEligible());
    }

    @Test
    void deveRemoverClienteComSucesso() {
        // Given
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        doNothing().when(customerRepository).deleteById(1L);

        // When
        customerService.deleteCustomer(1L);

        // Then
        verify(customerRepository).deleteById(1L);
    }

    @Test
    void deveFalharAoRemoverClienteInexistente() {
        // Given
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            customerService.deleteCustomer(999L);
        });
        verify(customerRepository, never()).deleteById(any());
    }
}
