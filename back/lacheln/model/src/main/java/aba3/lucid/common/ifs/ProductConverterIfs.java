package aba3.lucid.common.ifs;

import aba3.lucid.domain.company.entity.CompanyEntity;

public interface ProductConverterIfs<Entity, Req, Res> {

    Res toResponse(Entity entity);

    Entity toEntity(Req req, CompanyEntity entity);

}
