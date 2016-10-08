package cn.cloudwalk.ebank.core.domain.service.weixin.menu;

import cn.cloudwalk.ebank.core.domain.model.weixin.account.WeiXinAccountEntity;
import cn.cloudwalk.ebank.core.domain.model.weixin.menu.WeiXinMenuEntity;
import cn.cloudwalk.ebank.core.domain.service.weixin.account.IWeiXinAccountService;
import cn.cloudwalk.ebank.core.domain.service.weixin.menu.command.WeiXinMenuCommand;
import cn.cloudwalk.ebank.core.domain.service.weixin.menu.command.WeiXinMenuPaginationCommand;
import cn.cloudwalk.ebank.core.repository.Pagination;
import cn.cloudwalk.ebank.core.repository.weixin.menu.IWeiXinMenuRepository;
import cn.cloudwalk.ebank.core.support.security.utils.CustomSecurityContextHolderUtil;
import com.arm4j.weixin.exception.WeiXinRequestException;
import com.arm4j.weixin.request.menu.WeiXinMenuCreateRequest;
import com.arm4j.weixin.request.menu.entity.MenuButtonEntity;
import com.arm4j.weixin.request.menu.entity.MenuEntity;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by liwenhe on 2016/10/8.
 *
 * @author 李文禾
 */
@Service("weiXinMenuService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class WeiXinMenuService implements IWeiXinMenuService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IWeiXinMenuRepository<WeiXinMenuEntity, String> weiXinMenuRepository;

    @Autowired
    private IWeiXinAccountService weiXinAccountService;

    @Override
    public Pagination<WeiXinMenuEntity> pagination(WeiXinMenuPaginationCommand command) {
        String username = CustomSecurityContextHolderUtil.getUsername();
        WeiXinAccountEntity accountEntity = weiXinAccountService.findByUsername(username);
        if (null != accountEntity) {
            // 添加查询条件
            List<Criterion> criterions = new ArrayList<>();
            if (!StringUtils.isEmpty(command.getName())) {
                criterions.add(Restrictions.like("name", command.getName(), MatchMode.ANYWHERE));
            }
            if (!StringUtils.isEmpty(command.getKey())) {
                criterions.add(Restrictions.like("key", command.getKey(), MatchMode.ANYWHERE));
            }
            if (null != command.getType() && !command.getType().isOnlyQuery()) {
                criterions.add(Restrictions.eq("type", command.getType()));
            }
            if (null != command.getMsgType() && !command.getMsgType().isOnlyQuery()) {
                criterions.add(Restrictions.eq("msgType", command.getMsgType()));
            }
            if (!CustomSecurityContextHolderUtil.hasRole("administrator"))
                criterions.add(Restrictions.eq("accountEntity.id", accountEntity.getId()));

            // 添加排序条件
            List<Order> orders = new ArrayList<>();
            orders.add(Order.asc("order"));

            // 添加fetch mode
            Map<String, FetchMode> fetchModeMap = new HashMap<>();
            fetchModeMap.put("parent", FetchMode.JOIN);
            fetchModeMap.put("accountEntity", FetchMode.JOIN);

            return weiXinMenuRepository.pagination(command.getPage(), command.getPageSize(), criterions, orders, fetchModeMap);
        } else {
            return new Pagination<WeiXinMenuEntity>(command.getPage(), command.getPageSize(), 0, Collections.EMPTY_LIST);
        }
    }

    @Override
    public List<WeiXinMenuEntity> findAll() {
        String username = CustomSecurityContextHolderUtil.getUsername();
        WeiXinAccountEntity accountEntity = weiXinAccountService.findByUsername(username);

        if (null != accountEntity) {
            // 添加查询条件
            List<Criterion> criterions = new ArrayList<>();
            criterions.add(Restrictions.eq("accountEntity.id", accountEntity.getId()));

            // 添加排序条件
            List<Order> orders = new ArrayList<>();
            orders.add(Order.asc("order"));

            return weiXinMenuRepository.findAll(criterions, orders, null);
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<WeiXinMenuEntity> findByParentIsNull() {
        String username = CustomSecurityContextHolderUtil.getUsername();
        WeiXinAccountEntity accountEntity = weiXinAccountService.findByUsername(username);

        if (null != accountEntity) {
            // 添加查询条件
            List<Criterion> criterions = new ArrayList<>();
            criterions.add(Restrictions.eq("accountEntity.id", accountEntity.getId()));
            criterions.add(Restrictions.isNull("parent"));

            // 添加排序条件
            List<Order> orders = new ArrayList<>();
            orders.add(Order.asc("order"));

            return weiXinMenuRepository.findAll(criterions, orders, null);
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<WeiXinMenuEntity> findByParentId(String parentId) {
        // 添加查询条件
        List<Criterion> criterions = new ArrayList<>();
        criterions.add(Restrictions.eq("parent.id", parentId));

        // 添加排序条件
        List<Order> orders = new ArrayList<>();
        orders.add(Order.asc("order"));

        // 添加fetch
        Map<String, FetchMode> fetchModeMap = new HashMap<>();
        fetchModeMap.put("parent", FetchMode.JOIN);

        return weiXinMenuRepository.findAll(criterions, orders, fetchModeMap);
    }

    @Override
    public WeiXinMenuEntity save(WeiXinMenuCommand command) {
        String username = CustomSecurityContextHolderUtil.getUsername();
        WeiXinAccountEntity accountEntity = weiXinAccountService.findByUsername(username);
        if (null == accountEntity) {
            throw new NullPointerException("公众号不能为空");
        }

        WeiXinMenuEntity parent = weiXinMenuRepository.getById(command.getParent());
        WeiXinMenuEntity entity = new WeiXinMenuEntity(
                command.getName(),
                command.getKey(),
                command.getUrl(),
                command.getTemplateId(),
                command.getOrder(),
                command.getType(),
                command.getMsgType(),
                parent,
                accountEntity
        );

        weiXinMenuRepository.save(entity);
        return entity;
    }

    @Override
    public void delete(String id) {
        WeiXinMenuEntity entity = weiXinMenuRepository.getById(id);
        weiXinMenuRepository.delete(entity);
    }

    @Override
    public boolean sync() throws WeiXinRequestException {
        String username = CustomSecurityContextHolderUtil.getUsername();
        WeiXinAccountEntity accountEntity = weiXinAccountService.findByUsername(username);

        List<MenuButtonEntity> allMenuButtonEntities = new ArrayList<>();
        List<WeiXinMenuEntity> weiXinMenuEntities = this.findByParentIsNull();
        for (WeiXinMenuEntity parentIsNullMenu : weiXinMenuEntities) {
            // 第一级菜单
            MenuButtonEntity firstMenuButtonEntity = new MenuButtonEntity();
            firstMenuButtonEntity.setName(parentIsNullMenu.getName());

            // 第二级菜单
            List<MenuButtonEntity> secondMenuButtonEntities = new ArrayList<>();
            List<WeiXinMenuEntity> menuEntities = this.findByParentId(parentIsNullMenu.getId());
            for (WeiXinMenuEntity entity : menuEntities) {
                MenuButtonEntity menuButtonEntity = new MenuButtonEntity();
                menuButtonEntity.setName(entity.getName());
                menuButtonEntity.setKey(entity.getKey());
                menuButtonEntity.setType(entity.getType().name().toLowerCase());
                menuButtonEntity.setUrl(entity.getUrl());
                secondMenuButtonEntities.add(menuButtonEntity);
            }

            // 第二级菜单添加至第一级菜单
            firstMenuButtonEntity.setSubButton(secondMenuButtonEntities);

            // 第一级菜单添加至容器中
            allMenuButtonEntities.add(firstMenuButtonEntity);
        }

        // 同步至微信中
        MenuEntity menuEntity = new MenuEntity();
        menuEntity.setButtons(allMenuButtonEntities);
        return WeiXinMenuCreateRequest.request(weiXinAccountService.getAccessToken(accountEntity.getAppId()), menuEntity);
    }
}
