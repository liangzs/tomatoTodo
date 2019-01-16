package liangzs.com.tomatotodo.data.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

import liangzs.com.tomatotodo.modules.homePage.MainPresenter;

import static liangzs.com.tomatotodo.modules.homePage.MainPresenter.NONE;

/**
 * @author liangzs
 * @Date 2019/1/14
 */
@Entity
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Long id;
    private String title;
    private String content;
    @MainPresenter.ClockType
    private String cloclType = NONE;

    public Task(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public Task(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Task() {
    }

    @Generated(hash = 118288489)
    public Task(Long id, String title, String content, String cloclType) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.cloclType = cloclType;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public String getCloclType() {
        return cloclType;
    }

    public void setCloclType(String cloclType) {
        this.cloclType = cloclType;
    }
}
