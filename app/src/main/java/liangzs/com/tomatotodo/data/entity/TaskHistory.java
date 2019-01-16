package liangzs.com.tomatotodo.data.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

import org.greenrobot.greendao.annotation.Generated;

/**
 * @author liangzs
 * @Date 2019/1/14
 */
@Entity
public class TaskHistory {

    @Id
    private Long id;
    private String title;
    private String content;
    private Date date;

    public TaskHistory(Task task) {
        this.title = task.getTitle();
        this.content = getContent();
        this.date = new Date();
    }

    @Generated(hash = 1271100193)
    public TaskHistory(Long id, String title, String content, Date date) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    @Generated(hash = 56465440)
    public TaskHistory() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
