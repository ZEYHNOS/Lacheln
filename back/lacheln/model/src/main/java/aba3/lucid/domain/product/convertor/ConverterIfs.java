package aba3.lucid.domain.product.convertor;

public interface ConverterIfs<Entity, Req, Res> {

    Res toResponse(Entity entity);

    Entity toEntity(Req req);

}
