package com.airy.remind.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Airy on 2018/7/20
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */
@Entity
public class Remind {

    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String content;
    private String datetime;
    private boolean isFinished;
    @Generated(hash = 1659685709)
    public Remind(Long id, String name, String content, String datetime,
            boolean isFinished) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.datetime = datetime;
        this.isFinished = isFinished;
    }
    @Generated(hash = 1173539496)
    public Remind() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getDatetime() {
        return this.datetime;
    }
    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
    public Boolean getIsFinished() {
        return this.isFinished;
    }
    public void setIsFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

}
