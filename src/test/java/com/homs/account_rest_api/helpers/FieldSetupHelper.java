package com.homs.account_rest_api.helpers;

import com.homs.account_rest_api.repository.impl.AccountMysqlRepository;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

public class FieldSetupHelper {

    public Field setInternalAccountFieldHelper(String fieldName, String content, AccountMysqlRepository obj) throws NoSuchFieldException, IllegalAccessException {
        Field queryField = AccountMysqlRepository.class.getDeclaredField(fieldName);
        queryField.setAccessible(true);
        queryField.set(obj, content);
        return queryField;
    }

}
