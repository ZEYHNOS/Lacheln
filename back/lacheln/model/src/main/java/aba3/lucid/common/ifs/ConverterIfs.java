package aba3.lucid.common.ifs;

public interface ConverterIfs<Entity, Req, Res> {

    Res toResponse(Entity entity);

    Entity toEntity(Req req);

}
