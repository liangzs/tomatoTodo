package liangzs.com.tomatotodo.data.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

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
    private boolean isPlaying;

    @Generated(hash = 1814269412)
    public Task(Long id, String title, String content, boolean isPlaying) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isPlaying = isPlaying;
    }

    public Task(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Generated(hash = 733837707)
    public Task() {
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

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public boolean getIsPlaying() {
        return this.isPlaying;
    }

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
