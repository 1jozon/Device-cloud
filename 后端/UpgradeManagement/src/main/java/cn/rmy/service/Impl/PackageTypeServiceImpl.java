package cn.rmy.service.Impl;

import cn.rmy.beans.PackageType;
import cn.rmy.beans.SelectResult;
import cn.rmy.dao.PackageTypeDao;
import cn.rmy.service.PackageTypeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PackageTypeServiceImpl implements PackageTypeService {

    @Autowired
    private PackageTypeDao packageTypeDao;

    @Override
    public int insertType(PackageType packageType) {
        int rec;

        QueryWrapper<PackageType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_name",packageType.getTypeName());
        if(packageTypeDao.selectOne(queryWrapper)!=null) rec = -1;
        else rec = packageTypeDao.insert(packageType);
        return rec;
    }

    @Override
    public int updateType(PackageType packageType) {
        //if(packageType.getId()==0) return -1;
        int rec=0;
        if(packageTypeDao.selectById(packageType.getId())==null) rec=-1;
        else{
            PackageType pp = packageTypeDao.selectById(packageType.getId());
            pp.setTypeName(packageType.getTypeName());
            rec = packageTypeDao.updateById(pp);
        }
        return rec;
    }

    @Override
    public int delete(int id) {
        int rec;
        if(packageTypeDao.selectById(id)==null) rec = -1;
        else rec = packageTypeDao.deleteById(id);

        return rec;
    }

    @Override
    public List<PackageType> getAllType() {
        QueryWrapper<PackageType> queryWrapper = new QueryWrapper<>();
        List<PackageType> list = packageTypeDao.selectList(queryWrapper);
        return list;
    }

    @Override
    public SelectResult getTypeByCondition(PackageType packageType, int current, int size) {
        QueryWrapper<PackageType> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("type_name",packageType.getTypeName());

        Page<PackageType> page = new Page<>(current,size);
        packageTypeDao.selectPage(page,queryWrapper);
        List<PackageType> list = page.getRecords();
        return new SelectResult(page.getTotal(),list);
    }
}
