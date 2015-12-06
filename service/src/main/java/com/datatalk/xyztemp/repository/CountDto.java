package com.datatalk.xyztemp.repository;

/**
 * Created by mavlarn on 15/12/3.
 */
public class CountDto {

    private Long id;
    private Long count;

    public CountDto(Long id, Long count) {
        this.id = id;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
