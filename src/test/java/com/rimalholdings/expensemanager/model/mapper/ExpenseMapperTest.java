package com.rimalholdings.expensemanager.model.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rimalholdings.expensemanager.data.dto.BaseDTOInterface;
import com.rimalholdings.expensemanager.data.dto.ExpenseDTO;
import com.rimalholdings.expensemanager.data.entity.ExpenseEntity;
import com.rimalholdings.expensemanager.service.ExpenseService;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExpenseMapperTest {

    private ExpenseMapper expenseMapper;

    @Mock
    private ExpenseService expenseService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        expenseMapper = new ExpenseMapper(objectMapper, expenseService);
    }

    @Test
    void mapToDTO_ValidExpenseDTO_ReturnsExpenseEntity() {
        // Arrange
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setId(1L);
        expenseDTO.setDueDate("2022-01-01 12:00:00");
        expenseDTO.setVendorId(1L);
        expenseDTO.setTotalAmount(BigDecimal.valueOf(100.0));
        expenseDTO.setAmountDue(BigDecimal.valueOf(50.0));
        expenseDTO.setDescription("Test Expense");

        // Act
        ExpenseEntity expenseEntity = expenseMapper.mapToDTO(expenseDTO);

        // Assert
        assertNotNull(expenseEntity);
        assertEquals(expenseDTO.getId(), expenseEntity.getId());
        assertEquals(Timestamp.valueOf(expenseDTO.getDueDate()), expenseEntity.getDueDate());
        assertEquals(expenseDTO.getVendorId(), expenseEntity.getVendor().getId());
        assertEquals(expenseDTO.getTotalAmount(), expenseEntity.getTotalAmount());
        assertEquals(expenseDTO.getAmountDue(), expenseEntity.getAmountDue());
        assertEquals(expenseDTO.getDescription(), expenseEntity.getDescription());
    }

    @Test
    void mapToDTO_InvalidDTOType_ThrowsIllegalArgumentException() {
        // Arrange
        BaseDTOInterface invalidDTO = mock(BaseDTOInterface.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> expenseMapper.mapToDTO(invalidDTO));
    }

    @Test
    void deleteEntity_ValidId_CallsExpenseServiceDeleteById() {
        // Arrange
        Long id = 1L;

        // Act
        expenseMapper.deleteEntity(id);

        // Assert
        verify(expenseService, times(1)).deleteById(id);
    }

    @Test
    void getEntity_ValidId_ReturnsConvertedDtoToString() {
        // Arrange
        Long id = 1L;
        ExpenseEntity expenseEntity = new ExpenseEntity();
        when(expenseService.findById(id)).thenReturn(expenseEntity);

        // Act
        String result = expenseMapper.getEntity(id);

        // Assert
        assertEquals(expenseMapper.convertDtoToString(expenseEntity), result);
    }

    @Test
    void saveOrUpdateEntity_ValidExpenseDTO_ReturnsConvertedDtoToString() {
    // Arrange
    ExpenseDTO expenseDTO = new ExpenseDTO();
    expenseDTO.setDueDate("2022-01-01 12:00:00");
    ExpenseEntity expenseEntity = new ExpenseEntity();
    expenseEntity.setDueDate(Timestamp.valueOf(expenseDTO.getDueDate()));
    when(expenseService.save(any(ExpenseEntity.class))).thenReturn(expenseEntity);

    // Act
    String result = expenseMapper.saveOrUpdateEntity(expenseDTO);

    // Assert
    assertEquals(expenseMapper.convertDtoToString(expenseEntity), result);
}

    @Test
    void saveOrUpdateEntity_InvalidDTOType_ThrowsIllegalArgumentException() {
        // Arrange
        BaseDTOInterface invalidDTO = mock(BaseDTOInterface.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> expenseMapper.saveOrUpdateEntity(invalidDTO));
    }

    @Test
    void getAllEntities_ValidPageable_ReturnsExpenseServiceFindAll() {
        // Arrange
        Pageable pageable = mock(Pageable.class);
        Page<ExpenseEntity> expectedPage = mock(Page.class);
        when(expenseService.findAll(pageable)).thenReturn(expectedPage);

        // Act
        Page<ExpenseEntity> result = expenseMapper.getAllEntities(pageable);

        // Assert
        assertEquals(expectedPage, result);
    }
}