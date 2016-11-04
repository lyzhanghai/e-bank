package cn.cloudwalk.ebank.core.domain.model.weixin.newstemplate;

import cn.cloudwalk.ebank.core.domain.model.weixin.account.WeiXinAccountEntity;
import cn.cloudwalk.ebank.core.support.entity.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by liwenhe on 2016/10/8.
 *
 * @author 李文禾
 */
@Entity
@Table(name = "weixin_template_news_items")
public class WeiXinNewsItemsTemplateEntity extends AbstractEntity {

    private String                      title;                  // 图文模板内容项标题

    private String                      author;                 // 图文模板内容项作者

    private String                      picUrl;                 // 图文模板内容项图片

    private String                      description;            // 图文模板内容项描述

    private String                      url;                    // 图文模板内容项地址

    private String                      content;                // 图文模板内容项内容

    private Integer                     order;                  // 图文模板内容项排序

    private Date                        createdDate;            // 创建日期

    private WeiXinNewsTemplateEntity    newsTemplateEntity;     // 图文模板内容项所属模板

    private WeiXinAccountEntity         accountEntity;          // 关联公众号

    public WeiXinNewsItemsTemplateEntity() {
        super();
    }

    public WeiXinNewsItemsTemplateEntity(String title, String author, String picUrl, String description, String url,
                                         String content, Integer order, Date createdDate,
                                         WeiXinNewsTemplateEntity newsTemplateEntity, WeiXinAccountEntity accountEntity) {
        this();
        this.title = title;
        this.author = author;
        this.picUrl = picUrl;
        this.description = description;
        this.url = url;
        this.content = content;
        this.order = order;
        this.createdDate = createdDate;
        this.newsTemplateEntity = newsTemplateEntity;
        this.accountEntity = accountEntity;
    }

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    @Column(name = "author")
    public String getAuthor() {
        return author;
    }

    @Column(name = "pic_url")
    public String getPicUrl() {
        return picUrl;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    @Column(name = "content")
    public String getContent() {
        return content;
    }

    @Column(name = "`order`")
    public Integer getOrder() {
        return order;
    }

    @Column(name = "created_date")
    public Date getCreatedDate() {
        return createdDate;
    }

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "news_template_id")
    public WeiXinNewsTemplateEntity getNewsTemplateEntity() {
        return newsTemplateEntity;
    }

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "account_id", nullable = false)
    public WeiXinAccountEntity getAccountEntity() {
        return accountEntity;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setNewsTemplateEntity(WeiXinNewsTemplateEntity newsTemplateEntity) {
        this.newsTemplateEntity = newsTemplateEntity;
    }

    public void setAccountEntity(WeiXinAccountEntity accountEntity) {
        this.accountEntity = accountEntity;
    }
}
