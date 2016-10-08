package cn.cloudwalk.ebank.core.domain.model.weixin.texttemplate;

import cn.cloudwalk.ebank.core.domain.model.weixin.account.WeiXinAccountEntity;
import cn.cloudwalk.ebank.core.support.entity.AbstractEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by liwenhe on 2016/10/8.
 *
 * @author 李文禾
 */
@Entity
@Table(name = "weixin_template_text")
public class WeiXinTextTemplateEntity extends AbstractEntity {

    private String              name;                   // 文本消息模板名称

    private String              content;                // 文本消息模板内容

    private Date                createdDate;            // 创建日期

    private WeiXinAccountEntity accountEntity;          // 关联公众号

    @Id
    @GenericGenerator(name = "weixin_text_template_entity_generator", strategy = "uuid")
    @GeneratedValue(generator = "weixin_text_template_entity_generator")
    @Override
    public String getId() {
        return super.getId();
    }

    @Version
    @Override
    public Integer getVersion() {
        return super.getVersion();
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    @Column(name = "content")
    public String getContent() {
        return content;
    }

    @Column(name = "created_date")
    public Date getCreatedDate() {
        return createdDate;
    }

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "account_id", nullable = false)
    public WeiXinAccountEntity getAccountEntity() {
        return accountEntity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setAccountEntity(WeiXinAccountEntity accountEntity) {
        this.accountEntity = accountEntity;
    }
}