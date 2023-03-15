package cn.rmy.service.imp;

import cn.rmy.common.beans.Instrument;
import cn.rmy.common.pojo.dto.City;
import cn.rmy.common.pojo.dto.Province;
import cn.rmy.dao.InstrumentDao;
import cn.rmy.service.DistributeService;
import cn.rmy.service.Impl.InstrumentServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 仪器分布实现
 *
 * @author chu
 * @date 2021/12/01
 */
@Service
@Transactional
public class DistributeServiceImp implements DistributeService {

    @Autowired
    private InstrumentDao instrumentDao;

    /**
     * 获得省份分布
     *
     * @return {@link List}<{@link Province}>
     */
    @Override
    public List<Province> getProvinceDistribute() {
        List<Instrument> instruments = getAllInstruments();
        if (instruments.size() == 0){
            return null;
        }
        List<Province> provinceList = new ArrayList<>();
        List<String> proNames = new ArrayList<>();
        for (Instrument ins : instruments){
            //System.out.print(ins.getInstrumentAddress() + ' ');
            String address = ins.getInstrumentAddress();
            Map<String, String> splitAddress = splitAddress(address);
            Province province = new Province();
            if (splitAddress.containsKey("province")){
                String provinceName = splitAddress.get("province");
                if (provinceName == null || provinceName.length() == 0){
                    continue;
                }
                if (proNames.contains(provinceName)){
                    //已经存在该省统计
                    for (Province info : provinceList){
                        if (info.getProvince().equals(provinceName)){
                            int value = info.getValue() + 1;
                            info.setValue(value);
                            break;
                        }else{
                            continue;
                        }
                    }
                }else{
                    proNames.add(provinceName);
                    province.setProvince(provinceName);
                    province.setValue(1);
                    provinceList.add(province);
                }
            }
        }

        return provinceList;
    }

    /**
     * 获得城市分布
     *
     * @param province 省
     * @return {@link List}<{@link City}>
     */
    @Override
    public List<City> getCityDistribute(String province) {
        if (province.length() == 0 || province == null){
            return null;
        }
        List<Instrument> instruments = getAllInstruments();
        if (instruments.size() == 0 || instruments == null){
            return null;
        }
        List<City> cityList = new ArrayList<>();
        List<String> cityNames = new ArrayList<>();
        for (Instrument ins : instruments){
            String address = ins.getInstrumentAddress();
            Map<String, String> splitAddress = splitAddress(address);
            City city = new City();
            if (splitAddress.containsKey("province") && province.equals(splitAddress.get("province"))){
                if (splitAddress.containsKey("city")){
                    String cityName = splitAddress.get("city");
                    if (cityNames.contains(cityName)){
                        //统计过
                        for (City info : cityList){
                            if (info.getCity().equals(cityName)){
                                int value = info.getValue() + 1;
                                info.setValue(value);
                                break;
                            }else {
                                continue;
                            }
                        }
                    }else{
                        cityNames.add(cityName);
                        city.setCity(cityName);
                        city.setValue(1);
                        cityList.add(city);
                    }
                }
            }else{
                continue;
            }
        }
        return cityList;
    }

    /**
     * 拆分地址
     *
     * @param address 地址
     * @return {@link Map}<{@link String}, {@link String}>
     */
    @Override
    public Map<String, String> splitAddress(String address) {
        //1级 省 自治区  2级 市 自治州 地区 3级：区县市旗(镇？)
        String province = null, city = null, provinceAndCity = null, town = null ;
        Map<String, String> row = new LinkedHashMap<>();
        List<Map<String, String>> table = new ArrayList<>();
        Map<String,String> resultMap = new HashMap<>(4);

        if (address.startsWith("香港")) {
            resultMap.put("province","香港");
            return resultMap;
        } else if (address.contains("澳门")) {
            resultMap.put("province","澳门");
            return resultMap;
        } else if (address.contains("台湾")) {
            resultMap.put("province","台湾");
            return resultMap;
        } else {
            //普通地址
            String regex = "((?<provinceAndCity>[^市]+市|.*?自治州|.*?区|.*县)(?<town>[^区]+区|.*?市|.*?县|.*?路|.*?街|.*?道|.*?镇|.*?旗)(?<detailAddress>.*))";
            Matcher m = Pattern.compile(regex).matcher(address);
            while (m.find()) {
                provinceAndCity = m.group("provinceAndCity");
                String regex2 = "((?<province>[^省]+省|.+自治区|上海市|北京市|天津市|重庆市|上海|北京|天津|重庆)(?<city>.*))";
                Matcher m2 = Pattern.compile(regex2).matcher(provinceAndCity);
                while (m2.find()) {
                    province = m2.group("province");
                    row.put("province", province == null ? "" : province.trim());
                    city = m2.group("city");
                    row.put("city", city == null ? "" : city.trim());
                }
                town = m.group("town");
                row.put("town", town == null ? "" : town.trim());
                table.add(row);
            }
        }
        if (table != null && table.size() > 0) {
            if (StringUtils.isNotBlank(table.get(0).get("province"))) {
                province = table.get(0).get("province");
                //对自治区进行处理
                if (province.contains("自治区")) {
                    if (province.contains("内蒙古")) {
                        province = province.substring(0,4);
                    }  else {
                        province = province.substring(0,3);
                    }

                }
            }
            if (StringUtils.isNotBlank(province)) {
                if (StringUtils.isNotBlank(table.get(0).get("city"))) {
                    city = table.get(0).get("city");
                    if (city.equals("上海市") || city.equals("重庆市") || city.equals("北京市") || city.equals("天津市")) {
                        province = table.get(0).get("city");
                    }
                }

                else if (province.equals("上海市") || province.equals("重庆市") || province.equals("北京市") || province.equals("天津市")) {
                    city = province;
                }
                if (StringUtils.isNotBlank(table.get(0).get("town"))) {
                    town = table.get(0).get("town");
                }
                province = province.substring(0,province.length() - 1);

            }

        } else {
            return resultMap;
        }
        resultMap.put("province",province);
        resultMap.put("city",city);
        resultMap.put("district",town);

        return resultMap;
    }

    /**
     * 获得所有仪器
     *
     * @return {@link List}<{@link Instrument}>
     */
    @Override
    public List<Instrument> getAllInstruments() {
        QueryWrapper<Instrument> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("id",1)
                .eq("deleted", 0);
        List<Instrument> list = instrumentDao.selectList(queryWrapper);
        if (list.size() == 0){
            return null;
        }else{
            return list;
        }
    }


}
