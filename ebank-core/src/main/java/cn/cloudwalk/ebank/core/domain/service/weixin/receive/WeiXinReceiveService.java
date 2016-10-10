package cn.cloudwalk.ebank.core.domain.service.weixin.receive;

import cn.cloudwalk.ebank.core.domain.model.weixin.account.WeiXinAccountEntity;
import cn.cloudwalk.ebank.core.domain.model.weixin.receive.WeiXinReceiveEntity;
import cn.cloudwalk.ebank.core.domain.service.weixin.account.IWeiXinAccountService;
import cn.cloudwalk.ebank.core.domain.service.weixin.receive.command.WeiXinReceiveCommand;
import cn.cloudwalk.ebank.core.domain.service.weixin.receive.command.WeiXinReceivePaginationCommand;
import cn.cloudwalk.ebank.core.repository.Pagination;
import cn.cloudwalk.ebank.core.repository.weixin.receive.IWeiXinReceiveRepository;
import cn.cloudwalk.ebank.core.support.exception.WeiXinAccountNotFoundException;
import cn.cloudwalk.ebank.core.support.utils.CustomSecurityContextHolderUtil;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by liwenhe on 2016/10/9.
 *
 * @author 李文禾
 */
@Service("weiXinReceiveService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class WeiXinReceiveService implements IWeiXinReceiveService {

    @Autowired
    private IWeiXinReceiveRepository<WeiXinReceiveEntity, String> weiXinReceiveRepository;

    @Autowired
    private IWeiXinAccountService weiXinAccountService;

    @Autowired
    private MessageSourceAccessor message;

    @Override
    public Pagination<WeiXinReceiveEntity> pagination(WeiXinReceivePaginationCommand command) {
        String username = CustomSecurityContextHolderUtil.getUsername();
        WeiXinAccountEntity accountEntity = weiXinAccountService.findByUsername(username);

        if (null != accountEntity) {
            // 添加查询条件
            List<Criterion> criterions = new ArrayList<>();
            if (!StringUtils.isEmpty(command.getNickname())) {
                criterions.add(Restrictions.like("nickname", command.getNickname(), MatchMode.ANYWHERE));
            }
            if (!StringUtils.isEmpty(command.getContent())) {
                criterions.add(Restrictions.like("content", command.getContent(), MatchMode.ANYWHERE));
            }
            if (!CustomSecurityContextHolderUtil.hasRole("administrator"))
                criterions.add(Restrictions.eq("accountEntity.id", accountEntity.getId()));

            // 添加排序条件
            List<Order> orders = new ArrayList<>();
            orders.add(Order.desc("createdDate"));

            return weiXinReceiveRepository.pagination(command.getPage(), command.getPageSize(), criterions, orders);
        } else {
            return new Pagination<WeiXinReceiveEntity>(command.getPage(), command.getPageSize(), 0, Collections.EMPTY_LIST);
        }
    }

    @Override
    public WeiXinReceiveEntity save(WeiXinReceiveCommand command) {
        String username = CustomSecurityContextHolderUtil.getUsername();
        WeiXinAccountEntity accountEntity = weiXinAccountService.findByUsername(username);
        if (null == accountEntity) {
            throw new WeiXinAccountNotFoundException(message.getMessage("WeiXinMenuService.WeiXinAccountNotFoundException"));
        }

        WeiXinReceiveEntity entity = new WeiXinReceiveEntity(
                command.getMsgId(),
                command.getMsgType(),
                command.getContent(),
                command.getFromUserName(),
                command.getToUserName(),
                command.getNickname(),
                null,
                false,
                new Date(),
                accountEntity
        );

        weiXinReceiveRepository.save(entity);
        return entity;
    }

    @Override
    public void response(String id, String content) {

    }

    @Override
    public void delete(String id) {
        WeiXinReceiveEntity entity = weiXinReceiveRepository.getById(id);
        weiXinReceiveRepository.delete(entity);
    }

}