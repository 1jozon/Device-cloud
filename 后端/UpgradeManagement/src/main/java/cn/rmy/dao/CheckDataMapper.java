package cn.rmy.dao;

import cn.rmy.common.beans.checkData.CheckData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CheckDataMapper {

    /**
     * 插入批量
     *
     * @param checkData 检查数据
     */
    @Insert("insert into tb_check (id, instrument_id, model_id, patient_age, patient_sex, patient_name, patient_area, project_id, test_rlu, test_result, reference_range," +
            "unit, dilu_ratio, reagent_batch_id, sample_type, exception, check_time, create_time, update_time, version, deleted) " +
            "values (#{id}, #{instrumentId}, #{modelId}, #{patientAge}, #{patientSex}, #{patientName}, #{patientArea}, #{projectId}, #{testRlu}, #{testResult}, #{referenceRange}, " +
            "#{unit}, #{diluRatio}, #{reagentBatchId}, #{sampleType}, #{exception}, #{checkTime}, #{createTime}, #{updateTime}, #{version}, #{deleted})")
    void insertBatch(CheckData checkData);
}
