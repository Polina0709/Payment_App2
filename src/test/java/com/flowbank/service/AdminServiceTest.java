package com.flowbank.service;

import com.flowbank.entity.Admin;
import com.flowbank.repository.AdminRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    private final AdminRepository adminRepository = mock(AdminRepository.class);
    private final AdminService adminService = new AdminService(adminRepository);

    @Test
    void testFindByUsername() {
        Admin admin = new Admin();
        admin.setUsername("admin");

        when(adminRepository.findByUsername("admin")).thenReturn(Optional.of(admin));

        Optional<Admin> found = adminService.findByUsername("admin");

        assertTrue(found.isPresent());
        assertEquals("admin", found.get().getUsername());
    }

    @Test
    void testCreateAdmin() {
        Admin admin = new Admin();
        admin.setUsername("admin");

        when(adminRepository.save(any(Admin.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Admin created = adminService.createAdmin(admin);

        assertNotNull(created.getId());
        assertEquals("admin", created.getUsername());
        verify(adminRepository, times(1)).save(admin);
    }
}
