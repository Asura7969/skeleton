package com.asura.open.api.mode;

/**
 * @author asura7969
 * @create 2022-04-25-20:56
 */
public class ReqBody {

    private Integer id;
    private String name;

    @Override
    public String toString() {
        return "ReqBody{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public ReqBody() {
    }

    public ReqBody(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
