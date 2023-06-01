package com.huntercodexs.simplejob;

import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.stereotype.Component;

@Component
public class ProductValidator implements Validator<ProductEntity> {

    @Override
    public void validate(ProductEntity productEntity) throws ValidationException {

        if (productEntity.getId() == null) {

            throw new ValidationException("id must not be null");
        }
        if (productEntity.getName() == null) {
            throw new ValidationException("name must not be null");
        }
    }
}
