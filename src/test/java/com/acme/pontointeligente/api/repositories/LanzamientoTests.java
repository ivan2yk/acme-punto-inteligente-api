package com.acme.pontointeligente.api.repositories;

import com.acme.pontointeligente.api.entities.Empresa;
import com.acme.pontointeligente.api.entities.Funcionario;
import com.acme.pontointeligente.api.entities.Lanzamiento;
import com.acme.pontointeligente.api.enums.PerfilEnum;
import com.acme.pontointeligente.api.enums.TipoEnum;
import com.acme.pontointeligente.api.utils.PasswordUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Ivan on 1/10/2018.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class LanzamientoTests {

    @Autowired
    private LanzamientoRepository lanzamientoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    private Long funcionarioId;

    @BeforeEach
    public void setUp() {
        Empresa empresa = Empresa.builder()
                .razonSocial("Empresa Ejemplo")
                .cnpj("51463645000100")
                .build();

        this.empresaRepository.save(empresa);

        Funcionario funcionario = Funcionario.builder()
                .name("Ivan")
                .perfil(PerfilEnum.ROLE_USUARIO)
                .senha(PasswordUtils.gerarBCrypt("123456"))
                .cpf("46567850")
                .email("leivagarcia18@gmail.com")
                .empresa(empresa)
                .build();

        Funcionario fnc = this.funcionarioRepository.save(funcionario);
        this.funcionarioId = fnc.getId();

        Lanzamiento lanzamiento1 = Lanzamiento.builder()
                .fecha(LocalDate.of(2018, 10, 1))
                .tipo(TipoEnum.INICIO_ALMUERZO)
                .funcionario(fnc)
                .build();

        Lanzamiento lanzamiento2 = Lanzamiento.builder()
                .fecha(LocalDate.of(2018, 9, 30))
                .tipo(TipoEnum.INICIO_ALMUERZO)
                .funcionario(fnc)
                .build();

        this.lanzamientoRepository.save(lanzamiento1);
        this.lanzamientoRepository.save(lanzamiento2);
    }

    @AfterEach
    public void tearDown() {
        this.empresaRepository.deleteAll();
    }

    @Test
    public void testFindByFuncionarioId() {
        List<Lanzamiento> lanzamientos = this.lanzamientoRepository.findByFuncionarioId(funcionarioId);

        assertEquals(2, lanzamientos.size());
    }

    @Test
    public void testFindByFuncionarioIdPaginado() {
        PageRequest page = PageRequest.of(0, 10);
        Page<Lanzamiento> lanzamientos = this.lanzamientoRepository.findByFuncionarioId(funcionarioId, page);

        assertEquals(2, lanzamientos.getTotalElements());
        assertEquals(1, lanzamientos.getTotalPages());
    }

}
