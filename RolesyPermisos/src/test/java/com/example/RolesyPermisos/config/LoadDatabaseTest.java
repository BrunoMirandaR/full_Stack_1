package com.example.RolesyPermisos.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.RolesyPermisos.repository.PermisoRepository;
import com.example.RolesyPermisos.repository.RoleRepository;

@ExtendWith(MockitoExtension.class)
class LoadDatabaseTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PermisoRepository permisoRepository;

    @Test
    void initDatabase_shouldSeedRolesAndPermisosWhenEmpty() throws Exception {
        when(roleRepository.count()).thenReturn(0L);
        when(permisoRepository.count()).thenReturn(0L);

        LoadDatabase loadDatabase = new LoadDatabase();
        loadDatabase.initDatabase(roleRepository, permisoRepository).run();

        verify(permisoRepository).saveAll(org.mockito.ArgumentMatchers.anyList());
        verify(roleRepository).saveAll(org.mockito.ArgumentMatchers.anyList());
    }

    @Test
    void initDatabase_shouldSkipWhenDataExists() throws Exception {
        when(roleRepository.count()).thenReturn(1L);

        LoadDatabase loadDatabase = new LoadDatabase();
        loadDatabase.initDatabase(roleRepository, permisoRepository).run();

        verify(roleRepository, never()).saveAll(org.mockito.ArgumentMatchers.anyList());
        verify(permisoRepository, never()).saveAll(org.mockito.ArgumentMatchers.anyList());
    }
}