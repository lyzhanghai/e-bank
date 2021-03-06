package cn.cloudwalk.ebank.core.domain.service.weixin.menu;

import cn.cloudwalk.ebank.core.domain.model.weixin.menu.WeiXinMenuEntity;
import cn.cloudwalk.ebank.core.domain.service.weixin.menu.command.WeiXinMenuCommand;
import com.arm4j.weixin.exception.WeiXinRequestException;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import java.util.List;
import java.util.Map;

/**
 * Created by liwenhe on 2016/10/8.
 *
 * @author 李文禾
 */
public interface IWeiXinMenuService {

    List<Map<String, Object>> dataset();

    List<WeiXinMenuEntity> findAll();

    List<WeiXinMenuEntity> findAll(List<Criterion> criterions, List<Order> orders, Map<String, FetchMode> fetchModeMap);

    List<WeiXinMenuEntity> findByParentIsNullAndMenuCustom(String menuCustomId);

    List<WeiXinMenuEntity> findByParentAndMenuCustomIsNull();

    List<WeiXinMenuEntity> findByParentId(String parentId);

    WeiXinMenuEntity findByIdAndFetch(String id);

    WeiXinMenuEntity save(WeiXinMenuCommand command);

    WeiXinMenuEntity update(WeiXinMenuCommand command);

    void delete(String id);

    void sync() throws WeiXinRequestException;

}
