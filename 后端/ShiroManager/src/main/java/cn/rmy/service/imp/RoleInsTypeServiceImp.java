package cn.rmy.service.imp;

import cn.rmy.common.beans.shiroUsers.RoleInsTypeInfo;
import cn.rmy.dao.RoleInstypeDao;
import cn.rmy.service.RoleInsTypeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色ins imp类型服务
 *
 * @author chu
 * @date 2021/12/23
 */
@Service
public class RoleInsTypeServiceImp implements RoleInsTypeService {

    @Autowired
    private RoleInstypeDao roleInstypeDao;

    @Override
    public int addInfo(String roleName, List<String> insTypes) {

        for (String insType : insTypes){
            RoleInsTypeInfo roleInsTypeInfo = new RoleInsTypeInfo();
            roleInsTypeInfo.setRoleName(roleName);
            roleInsTypeInfo.setInsType(insType);

            int rec = roleInstypeDao.insert(roleInsTypeInfo);
            if (rec != 1){
                continue;
            }
        }
        return 1;

    }

    /**
     * 删除
     *
     * @param roleName 角色名
     * @param insTypes ins类型
     * @return int
     */
    @Override
    public int delete(String roleName, List<String> insTypes) {

        for (String insType : insTypes){
            QueryWrapper<RoleInsTypeInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("role_name", roleName)
                    .eq("ins_type", insType);
            int rec = roleInstypeDao.delete(queryWrapper);
            if (rec != 1){
                continue;
            }
        }
        return 1;

    }

    /**
     * 得到
     *
     * @param roleName 角色名
     * @param insType  ins类型
     * @return {@link RoleInsTypeInfo}
     */
    @Override
    public RoleInsTypeInfo get(String roleName, String insType) {
        QueryWrapper<RoleInsTypeInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_name",roleName)
                .eq("ins_type",insType);
        RoleInsTypeInfo info = roleInstypeDao.selectOne(queryWrapper);

        return info;
    }

    /**
     * 所有ins类型
     *
     * @param roleName 角色名
     * @return {@link List}<{@link String}>
     */
    @Override
    public List<String> allInsTypes(String roleName) {
        QueryWrapper<RoleInsTypeInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_name",roleName)
                .groupBy("ins_type");
        List<RoleInsTypeInfo> infoList = roleInstypeDao.selectList(queryWrapper);
        if (infoList == null || infoList.size() == 0){
            return null;
        }
        List<String> insTypes = new ArrayList<>();
        for (RoleInsTypeInfo info : infoList){
            insTypes.add(info.getInsType());
        }
        return insTypes;
    }
}
