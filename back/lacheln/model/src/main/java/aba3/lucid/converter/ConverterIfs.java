package aba3.lucid.converter;

public interface ConverterIfs<Entity, Req, Res> {

    Res toResponse(Entity entity);

    Entity toEntity(Req req);

}
