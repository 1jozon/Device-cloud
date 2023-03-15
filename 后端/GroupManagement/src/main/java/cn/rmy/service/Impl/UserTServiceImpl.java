package cn.rmy.service.Impl;

import cn.rmy.common.beans.groupManager.UserT;
import cn.rmy.dao.UserTDao;
import cn.rmy.service.UserTService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTServiceImpl implements UserTService {

    @Autowired
    private UserTDao userTDao;

    @Override
    public int insert(UserT userT) {
        int rec = userTDao.insert(userT);
        return rec;
    }

    @Override
    public UserT getUserByUserId(String userId) {
        QueryWrapper<UserT> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        UserT user = userTDao.selectOne(wrapper);

        return user;
    }
}
