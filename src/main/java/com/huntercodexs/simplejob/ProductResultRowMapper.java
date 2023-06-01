package com.huntercodexs.simplejob;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ProductResultRowMapper implements RowMapper<ProductEntity> {
    @Override
    public ProductEntity mapRow(ResultSet rs, int i) throws SQLException {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(rs.getLong("id"));
        productEntity.setDescription(rs.getString("name"));
        productEntity.setName(rs.getString("description"));
        productEntity.setName(rs.getString("price"));
        return productEntity;
    }
}
