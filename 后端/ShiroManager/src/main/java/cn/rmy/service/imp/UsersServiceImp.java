package cn.rmy.service.imp;

import cn.rmy.common.beans.Instrument;
import cn.rmy.common.beans.shiroUsers.UserInfo;
import cn.rmy.common.dto.Role;
import cn.rmy.common.dto.Users;
import cn.rmy.common.dto.UserRole;
import cn.rmy.dao.InstrumentDao;
import cn.rmy.dao.UsersDao;
import cn.rmy.service.Impl.UserWithInstServiceImpl;
import cn.rmy.service.UserToGroupService;
import cn.rmy.service.UsersService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UsersServiceImp implements UsersService {

    private Logger logger = LoggerFactory.getLogger(UsersService.class);

    @Autowired
    private UserInfoServiceImp userInfoService;

    @Autowired
    private RoleServiceImp roleService;

    @Autowired
    private UserRoleServiceImp userRoleService;

    @Autowired
    private UserToGroupService userToGroupService;

    @Autowired
    private UserWithInstServiceImpl userWithInstService;

    @Autowired
    private InstrumentDao instrumentDao;

    @Autowired
    private UsersDao usersDao;

    @Override
    public Users getUserById(String userId) {
        UserInfo userInfo = userInfoService.getUserById(userId);
        if (userInfo == null){
            return null;
        }
        List<UserRole> userRoles =userRoleService.getByUserId(userId);
        Set<Role> roles =new HashSet<>();
        Map<Integer,String> roleNames = new HashMap<>();
        if (userRoles.size() > 0){
            for (UserRole userRole : userRoles){
                Role role = roleService.getByRoleId(userRole.getRoleId());
                if (role == null){
                    continue;
                }
                roles.add(role);
                roleNames.putIfAbsent(role.getRoleId(),role.getRoleName());
            }
        }
        List<String> groupNames = userToGroupService.getGroupNameByUserId(userInfo.getId());

        Users users = new Users(userInfo.getId(),userInfo.getUserId(),userInfo.getUserName(),userInfo.getUserGender(),
                userInfo.getUserPassword(),userInfo.getUserPhone(),userInfo.getUserEmail(),userInfo.getRoleId(),userInfo.getRegistStatus(),
                userInfo.getUserSalt(),userInfo.getUserAvatar(),userInfo.getLastLoginIp(),userInfo.getCreateTime(),roles,roleNames,groupNames);
        return users;
    }

    /**
     * 获取当前用户insId列表
     *
     * @return {@link List}<{@link String}>
     */
    @Override
    public List<String> getcurrentUserInsIdList(String userId) {

        List<Instrument> insList = userWithInstService.getInstsByUserId(userId);
        List<String> insIdList = new ArrayList<>();

        //超级管理员--所有仪器id
        List<UserRole> userRoleList = userRoleService.getByUserId(userId);
        if(userRoleList != null && userRoleList.size() > 0){
            for(UserRole userRole : userRoleList){
                if(userRole.getRoleId() == 8){
                    insIdList = getAllInsId();
                }
            }
        }

        if(insIdList != null && !insList.isEmpty()){
            for(Instrument ins : insList){
                insIdList.add(ins.getInstrumentId());
            }
        }
        return (insIdList != null && insIdList.size() != 0) ? insIdList : null;
    }

    /**
     * 得到所有ins id
     *
     * @return {@link List}<{@link String}>
     */
    @Override
    public List<String> getAllInsId() {
        List<Instrument> instrumentList = new ArrayList<>();
        List<String> insIds = new ArrayList<>();
        QueryWrapper<Instrument> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("instrument_id");
        instrumentList = instrumentDao.selectList(queryWrapper);

        if(instrumentList != null && instrumentList.size() > 0){
            for (Instrument instrument : instrumentList){
                insIds.add(instrument.getInstrumentId());
            }
        }else{
            return null;
        }

        return insIds;

    }

}
